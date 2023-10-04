package com.openclassrooms.tourguide.service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.openclassrooms.tourguide.concurrent.ExecutorServices;
import com.openclassrooms.tourguide.concurrent.SupplierUserLocation;
import com.openclassrooms.tourguide.helper.InternalTestHelper;
import com.openclassrooms.tourguide.service.dto.AttractionDistanceDTO;
import com.openclassrooms.tourguide.service.dto.AttractionNearDTO;
import com.openclassrooms.tourguide.tracker.Tracker;
import com.openclassrooms.tourguide.user.User;
import com.openclassrooms.tourguide.user.UserReward;

import gpsUtil.GpsUtil;
import gpsUtil.location.Location;
import gpsUtil.location.VisitedLocation;
import tripPricer.Provider;
import tripPricer.TripPricer;

@Service
public class TourGuideService {

	private Logger logger = LoggerFactory.getLogger(TourGuideService.class);

	private final GpsUtil gpsUtil;
	private final RewardsService rewardsService;
	private final TripPricer tripPricer = new TripPricer();
	public final Tracker tracker;
	boolean testMode = true;

	private final ExecutorService executor;

	public TourGuideService(GpsUtil gpsUtil, RewardsService rewardsService) {
		this.gpsUtil = gpsUtil;
		this.rewardsService = rewardsService;

		Locale.setDefault(Locale.US);

		executor = ExecutorServices.getEsTourGuideService();

		if (testMode) {
			logger.info("TestMode enabled");
			logger.debug("Initializing users");
			initializeInternalUsers();
			logger.debug("Finished initializing users");
		}

		tracker = new Tracker(this);
		tracker.startTracking();
	}

	public List<UserReward> getUserRewards(User user) {
		return user.getUserRewards();
	}

	public VisitedLocation getUserLocation(User user) {
		VisitedLocation visitedLocation = null;
		try {
			visitedLocation = (user.getVisitedLocations().size() > 0) ? user.getLastVisitedLocation()
					: trackUserLocation(user).get();
		} catch (InterruptedException e) {
			logger.error(e.getMessage());
		} catch (ExecutionException e) {
			logger.error(e.getMessage());
		}
		return visitedLocation;
	}

	public Optional<User> getUser(String userName) {
		return Optional.of(internalUserMap.get(userName));
	}

	public List<User> getAllUsers() {
		return internalUserMap.values().stream().collect(Collectors.toList());
	}

	public void addUser(User user) {
		if (!internalUserMap.containsKey(user.getUserName())) {
			internalUserMap.put(user.getUserName(), user);
		}
	}

	public List<Provider> getTripDeals(User user) {
		int cumulatativeRewardPoints = user.getUserRewards().stream().mapToInt(i -> i.getRewardPoints()).sum();

		List<Provider> providers = tripPricer.getPrice(tripPricerApiKey,
				user.getUserId(),
				user.getUserPreferences().getNumberOfAdults(),
				user.getUserPreferences().getNumberOfChildren(),
				user.getUserPreferences().getTripDuration(),
				cumulatativeRewardPoints);

		user.setTripDeals(providers);

		return providers;
	}

	public CompletableFuture<VisitedLocation> trackUserLocation(User user) {
		try {
			SupplierUserLocation supplierUserLocation = new SupplierUserLocation(user, gpsUtil, rewardsService);

			return CompletableFuture.supplyAsync(supplierUserLocation, executor);
		} catch (Throwable e) {
			logger.error(e.getMessage());
		}
		return null;
	}

	public void trackLocationForAllUsers(List<User> allUsers) {
		try {
			List<CompletableFuture<VisitedLocation>> listCompletableFuture = new ArrayList<>();

			allUsers.stream().map(u -> {
				CompletableFuture<VisitedLocation> cpf = trackUserLocation(u);
				listCompletableFuture.add(cpf);

				return u;
			}).collect(Collectors.toList());

			CompletableFuture.allOf(listCompletableFuture.toArray(new CompletableFuture[listCompletableFuture.size()])).join();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	public List<AttractionNearDTO> getNearByAttractions(final String userName) {
		User user = getUser(userName).get();

		getUserLocation(user);

		Location locationUser = user.getLastVisitedLocation().location;

		return gpsUtil.getAttractions().parallelStream()
			.map(a -> {
				Location l = new Location(a.latitude, a.longitude);
				AttractionDistanceDTO adDTO = new AttractionDistanceDTO(
						a,
						l,
						rewardsService.getDistance(l, locationUser));
				return adDTO;
			})
			.sorted(Comparator.comparingDouble(AttractionDistanceDTO::getDistance))
			.limit(5)
			.map(dto -> {
				AttractionNearDTO obj = new AttractionNearDTO(
					dto.getAttraction().attractionName,
					dto.getLocation(),
					locationUser,
					dto.getDistance(),
					rewardsService.getRewardPoints(dto.getAttraction(), user));

				return obj;
			}).collect(Collectors.toList());

	}

	/**********************************************************************************
	 *
	 * Methods Below: For Internal Testing
	 *
	 **********************************************************************************/
	private static final String tripPricerApiKey = "test-server-api-key";
	// Database connection will be used for external users, but for testing purposes
	// internal users are provided and stored in memory
	private final Map<String, User> internalUserMap = new HashMap<>();

	private void initializeInternalUsers() {
		IntStream.range(0, InternalTestHelper.getInternalUserNumber()).forEach(i -> {
			String userName = "internalUser" + i;
			String phone = "000";
			String email = userName + "@tourGuide.com";
			User user = new User(UUID.randomUUID(), userName, phone, email);
			generateUserLocationHistory(user);

			internalUserMap.put(userName, user);
		});
		logger.debug("Created " + InternalTestHelper.getInternalUserNumber() + " internal test users.");
	}

	private void generateUserLocationHistory(User user) {
		IntStream.range(0, 3).forEach(i -> {
			user.addToVisitedLocations(new VisitedLocation(user.getUserId(),
					new Location(generateRandomLatitude(), generateRandomLongitude()), getRandomTime()));
		});
	}

	private double generateRandomLongitude() {
		double leftLimit = -180;
		double rightLimit = 180;
		return leftLimit + new Random().nextDouble() * (rightLimit - leftLimit);
	}

	private double generateRandomLatitude() {
		double leftLimit = -85.05112878;
		double rightLimit = 85.05112878;
		return leftLimit + new Random().nextDouble() * (rightLimit - leftLimit);
	}

	private Date getRandomTime() {
		LocalDateTime localDateTime = LocalDateTime.now().minusDays(new Random().nextInt(30));
		return Date.from(localDateTime.toInstant(ZoneOffset.UTC));
	}

}
