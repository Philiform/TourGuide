package com.openclassrooms.tourguide.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executors;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.openclassrooms.tourguide.concurrent.ExecutorServices;
import com.openclassrooms.tourguide.helper.InternalTestHelper;
import com.openclassrooms.tourguide.user.User;
import com.openclassrooms.tourguide.user.UserReward;

import gpsUtil.GpsUtil;
import gpsUtil.location.Attraction;
import gpsUtil.location.VisitedLocation;
import rewardCentral.RewardCentral;

public class TestRewardsService {

	private GpsUtil gpsUtil;
	private RewardsService rewardsService;

	@BeforeEach
	public void initEach(){
		ExecutorServices.setEsRewardsService(Executors.newCachedThreadPool());

		gpsUtil = new GpsUtil();
		rewardsService = new RewardsService(gpsUtil, new RewardCentral());
	}

	@AfterEach
	public void cleanUpEach(){
		ExecutorServices.shutdownNowEsRewardsService();;
	}

	@Test
	public void userGetRewards() {
		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
		Attraction attraction = gpsUtil.getAttractions().get(0);

		user.addToVisitedLocations(new VisitedLocation(user.getUserId(), attraction, new Date()));
		rewardsService.calculateRewards(user);

		List<UserReward> userRewards = user.getUserRewards();

		assertTrue(userRewards.size() == 1);
	}

	@Test
	public void isWithinAttractionProximity() {
		Attraction attraction = gpsUtil.getAttractions().get(0);

		assertTrue(rewardsService.isWithinAttractionProximity(attraction, attraction));
	}

	@Test
	public void nearAllAttractions() {
		ExecutorServices.setEsTourGuideService(Executors.newCachedThreadPool());
		ExecutorServices.setEsTracker(Executors.newScheduledThreadPool(1));

		rewardsService.setProximityBuffer(Integer.MAX_VALUE);

		InternalTestHelper.setInternalUserNumber(1);
		TourGuideService tourGuideService = new TourGuideService(gpsUtil, rewardsService);
		User user = tourGuideService.getAllUsers().get(0);

		rewardsService.calculateRewards(user);

		List<UserReward> userRewards = tourGuideService.getUserRewards(user);

		ExecutorServices.shutdownNowEsTourGuideService();;
		ExecutorServices.shutdownNowEsTracker();

		assertEquals(gpsUtil.getAttractions().size(), userRewards.size());
	}

}
