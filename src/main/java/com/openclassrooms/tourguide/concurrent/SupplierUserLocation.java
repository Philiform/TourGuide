package com.openclassrooms.tourguide.concurrent;

import com.google.common.base.Supplier;
import com.openclassrooms.tourguide.service.RewardsService;
import com.openclassrooms.tourguide.user.User;

import gpsUtil.GpsUtil;
import gpsUtil.location.VisitedLocation;

public class SupplierUserLocation implements Supplier<VisitedLocation> {

	private User user;
	private GpsUtil gpsUtil;
	private RewardsService rewardsService;

	public SupplierUserLocation(User user, GpsUtil gpsUtil, RewardsService rewardsService) {
		this.user = user;
		this.gpsUtil = gpsUtil;
		this.rewardsService = rewardsService;
	}

	@Override
	public VisitedLocation get() {
		VisitedLocation visitedLocation = gpsUtil.getUserLocation(user.getUserId());

		user.addToVisitedLocations(visitedLocation);

		rewardsService.calculateRewards(user);

		return visitedLocation;
	}

}
