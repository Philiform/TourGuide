package com.openclassrooms.tourguide;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.openclassrooms.tourguide.service.RewardsService;

import gpsUtil.GpsUtil;
import rewardCentral.RewardCentral;
import tripPricer.TripPricer;

@Configuration
public class TourGuideModule {

	@Bean
	GpsUtil getGpsUtil() {
		return new GpsUtil();
	}

	@Bean
	RewardsService getRewardsService() {
		return new RewardsService(getGpsUtil(), getRewardCentral());
	}

	@Bean
	RewardCentral getRewardCentral() {
		return new RewardCentral();
	}

	@Bean
	TripPricer getTripPricer() {
		return new TripPricer();
	}

}
