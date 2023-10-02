package com.openclassrooms.tourguide.concurrent;

import com.openclassrooms.tourguide.service.RewardsService;
import com.openclassrooms.tourguide.user.User;
import com.openclassrooms.tourguide.user.UserReward;

import gpsUtil.location.Attraction;
import gpsUtil.location.VisitedLocation;

public class RunnableReward implements Runnable {

	private User user;
	private Attraction attraction;
	private RewardsService rewardsService;

	public RunnableReward(User user, Attraction attraction, RewardsService rewardsService) {
		this.user = user;
		this.attraction = attraction;
		this.rewardsService = rewardsService;
	}

	@Override
	public void run() {
		if(user.getUserRewards().stream().filter(r -> r.getAttraction().attractionName.equals(attraction.attractionName)).count() == 0) {
			VisitedLocation visitedLocation =  user.getLastVisitedLocation();
			if(rewardsService.nearAttraction(visitedLocation, attraction)) {
				user.addUserReward(new UserReward(visitedLocation, attraction, rewardsService.getRewardPoints(attraction, user)));
			}
		}
	}

}
