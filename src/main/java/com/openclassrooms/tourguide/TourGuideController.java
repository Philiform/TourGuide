package com.openclassrooms.tourguide;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

	@Autowired
	TourGuideService tourGuideService;

	@GetMapping("/")
	public String index() {
		return "Greetings from TourGuide!";
	}

	@GetMapping("/getLocation")
	public VisitedLocation getLocation(@RequestParam String userName) {
		return tourGuideService.getUserLocation(getUser(userName));
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
	@GetMapping("/getNearbyAttractions")
	public List<AttractionNearDTO> getNearbyAttractions(@RequestParam String userName) {
		return tourGuideService.getNearByAttractions(userName);
	}

	@GetMapping("/getRewards")
	public List<UserReward> getRewards(@RequestParam String userName) {
		return tourGuideService.getUserRewards(getUser(userName));
	}

	@GetMapping("/getTripDeals")
	public List<Provider> getTripDeals(@RequestParam String userName) {
		return tourGuideService.getTripDeals(getUser(userName));
	}

	private User getUser(String userName) {
		return tourGuideService.getUser(userName);
	}

}