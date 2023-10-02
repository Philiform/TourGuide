package com.openclassrooms.tourguide.concurrent;

import com.openclassrooms.tourguide.service.RewardsService;
import com.openclassrooms.tourguide.user.User;

public class RunnableUser implements Runnable {

	private User user;
	private RewardsService rewardsService;

	public RunnableUser(User user, RewardsService rewardsService) {
		this.user = user;
		this.rewardsService = rewardsService;
	}

	@Override
	public void run() {
		rewardsService.calculateRewards(user);
	}

}
