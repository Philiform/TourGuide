package com.openclassrooms.tourguide;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.openclassrooms.tourguide.service.TourGuideService;
import com.openclassrooms.tourguide.service.dto.AttractionNearDTO;
import com.openclassrooms.tourguide.user.User;
import com.openclassrooms.tourguide.user.UserReward;

import gpsUtil.location.VisitedLocation;
import tripPricer.Provider;

@RestController
public class TourGuideController {

	private Logger logger = LoggerFactory.getLogger(TourGuideController.class);

	@Autowired
	TourGuideService tourGuideService;

	private HttpStatus httpStatus;

	@SuppressWarnings("deprecation")
	@GetMapping("/")
	public ResponseEntity<String> index() {
		List<User> l = new ArrayList<>();

		try {
			l = tourGuideService.getAllUsers();

			if(l.size() > 0) {
				httpStatus = HttpStatus.OK;
			} else {
				httpStatus = HttpStatus.NO_CONTENT;
			}
		} catch (Exception e) {
			logger.error("/: Exception : " + e.getMessage().toString());
			httpStatus = HttpStatus.METHOD_FAILURE;
		}

		return new ResponseEntity<String>("Greetings from TourGuide!", HttpStatus.OK);
	}

	@SuppressWarnings("deprecation")
	@GetMapping("/getLocation")
	public ResponseEntity<Optional<VisitedLocation>> getLocation(@RequestParam String userName) throws InterruptedException {
		logger.info("/getLocation         : user: " + userName);

		Optional<VisitedLocation> v = Optional.empty();

		try {
			Optional<User> user = tourGuideService.getUser(userName);
			v = Optional.of(tourGuideService.getUserLocation(user.get()));

			if(!Optional.of(v).isEmpty()) {
				httpStatus = HttpStatus.OK;
			} else {
				httpStatus = HttpStatus.NO_CONTENT;
			}
		} catch (Exception e) {
			logger.error("/getLocation: Exception : " + e.getMessage());
			httpStatus = HttpStatus.METHOD_FAILURE;
		}

		return new ResponseEntity<Optional<VisitedLocation>>(v, httpStatus);
	}

	// TODO: Change this method to no longer return a List of Attractions.
	// Instead: Get the closest five tourist attractions to the user - no matter how
	// far away they are.
	// Return a new JSON object that contains:
	// Name of Tourist attraction,
	// Tourist attractions lat/long,
	// The user's location lat/long,
	// The distance in miles between the user's location and each of the
	// attractions.
	// The reward points for visiting each Attraction.
	// Note: Attraction reward points can be gathered from RewardsCentral
	@SuppressWarnings("deprecation")
	@GetMapping("/getNearbyAttractions")
	public ResponseEntity<List<AttractionNearDTO>> getNearbyAttractions(@RequestParam String userName) throws InterruptedException {
		logger.info("/getNearbyAttractions: user: " + userName);

		List<AttractionNearDTO> l = new ArrayList<>();

		try {
			l = tourGuideService.getNearByAttractions(userName);

			if(l.size() > 0) {
				httpStatus = HttpStatus.OK;
			} else {
				httpStatus = HttpStatus.NO_CONTENT;
			}
		} catch (Exception e) {
			logger.error("/getNearbyAttractions: Exception : " + e.getMessage().toString());
			httpStatus = HttpStatus.METHOD_FAILURE;
		}

		return new ResponseEntity<List<AttractionNearDTO>>(l, httpStatus);
	}

	@SuppressWarnings("deprecation")
	@GetMapping("/getRewards")
	public ResponseEntity<List<UserReward>> getRewards(@RequestParam String userName) {
		logger.info("/getRewards          : user: " + userName);

		List<UserReward> l = new ArrayList<>();

		try {
			Optional<User> user = tourGuideService.getUser(userName);
			l = tourGuideService.getUserRewards(user.get());

			if(l.size() > 0) {
				httpStatus = HttpStatus.OK;
			} else {
				httpStatus = HttpStatus.NO_CONTENT;
			}
		} catch (Exception e) {
			logger.error("/getReawards: Exception : " + e.getMessage().toString());
			httpStatus = HttpStatus.METHOD_FAILURE;
		}

		return new ResponseEntity<List<UserReward>>(l, httpStatus);
	}

	@SuppressWarnings("deprecation")
	@GetMapping("/getTripDeals")
	public ResponseEntity<List<Provider>> getTripDeals(@RequestParam String userName) {
		logger.info("/getTripDeals        : user: " + userName);

		Optional<User> user = tourGuideService.getUser(userName);
		List<Provider> l = new ArrayList<>();

		try {
			l = tourGuideService.getTripDeals(user.get());

			if(l.size() > 0) {
				httpStatus = HttpStatus.OK;
			} else {
				httpStatus = HttpStatus.NO_CONTENT;
			}
		} catch (Exception e) {
			logger.error("/getTripDeals: Exception : " + e.getMessage().toString());
			httpStatus = HttpStatus.METHOD_FAILURE;
		}

		return new ResponseEntity<List<Provider>>(l, httpStatus);
	}

}