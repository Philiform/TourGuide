package com.openclassrooms.tourguide.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.openclassrooms.tourguide.concurrent.ExecutorServices;
import com.openclassrooms.tourguide.concurrent.RunnableReward;
import com.openclassrooms.tourguide.concurrent.RunnableUser;
import com.openclassrooms.tourguide.user.User;

import gpsUtil.GpsUtil;
import gpsUtil.location.Attraction;
import gpsUtil.location.Location;
import gpsUtil.location.VisitedLocation;
import rewardCentral.RewardCentral;

@Service
public class RewardsService {

	private Logger logger = LoggerFactory.getLogger(RewardsService.class);

	private static final double STATUTE_MILES_PER_NAUTICAL_MILE = 1.15077945;

	// proximity in miles
    private int defaultProximityBuffer = 10;
	private int proximityBuffer = defaultProximityBuffer;
	private int attractionProximityRange = 200;
	private final GpsUtil gpsUtil;
	private final RewardCentral rewardsCentral;

	private final ExecutorService executor;
	private List<CompletableFuture<Void>> listCompletableFuture = new ArrayList<>();
	private List<Attraction> listAttractions = new ArrayList<>();

	public RewardsService(GpsUtil gpsUtil, RewardCentral rewardCentral) {
		this.gpsUtil = gpsUtil;
		this.rewardsCentral = rewardCentral;
		executor = ExecutorServices.getEsRewardsService();

		init();
	}

	private void init() {
		listAttractions.addAll(gpsUtil.getAttractions());
	}

	public void setProximityBuffer(int proximityBuffer) {
		this.proximityBuffer = proximityBuffer;
	}

	public void setDefaultProximityBuffer() {
		proximityBuffer = defaultProximityBuffer;
	}

	public void calculateRewardsForAllUsers(List<User> allUsers) {
		logger.info("calculateRewardsForAllUsers");

		listCompletableFuture.clear();

		allUsers.stream().map(u -> {
			RunnableUser runnableUser = new RunnableUser(u, this);
			CompletableFuture<Void> cpf = CompletableFuture.runAsync(runnableUser, executor);
			listCompletableFuture.add(cpf);

			return u;
		}).collect(Collectors.toList());

		CompletableFuture.allOf(listCompletableFuture.toArray(new CompletableFuture[listCompletableFuture.size()])).join();
	}

	public void calculateRewards(User user) {
		List<CompletableFuture<Void>> listCompletableFuture2 = new ArrayList<>();

		listAttractions.stream().map(a -> {
			RunnableReward runnableReward = new RunnableReward(user, a, this);
			CompletableFuture<Void> cpf = CompletableFuture.runAsync(runnableReward, executor);
			listCompletableFuture2.add(cpf);

			return a;
		}).collect(Collectors.toList());

		CompletableFuture.allOf(listCompletableFuture2.toArray(new CompletableFuture[listCompletableFuture2.size()])).join();
	}

	public boolean isWithinAttractionProximity(Attraction attraction, Location location) {
		return getDistance(attraction, location) > attractionProximityRange ? false : true;
	}

	public boolean nearAttraction(VisitedLocation visitedLocation, Attraction attraction) {
		return getDistance(attraction, visitedLocation.location) > proximityBuffer ? false : true;
	}

	public int getRewardPoints(Attraction attraction, User user) {
		return rewardsCentral.getAttractionRewardPoints(attraction.attractionId, user.getUserId());
	}

	public double getDistance(Location loc1, Location loc2) {
        double lat1 = Math.toRadians(loc1.latitude);
        double lon1 = Math.toRadians(loc1.longitude);
        double lat2 = Math.toRadians(loc2.latitude);
        double lon2 = Math.toRadians(loc2.longitude);

        double angle = Math.acos(Math.sin(lat1) * Math.sin(lat2)
                               + Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon1 - lon2));

        double nauticalMiles = 60 * Math.toDegrees(angle);
        double statuteMiles = STATUTE_MILES_PER_NAUTICAL_MILE * nauticalMiles;
        return statuteMiles;
	}

}
