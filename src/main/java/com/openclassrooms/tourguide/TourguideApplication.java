package com.openclassrooms.tourguide;

import java.util.concurrent.Executors;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.openclassrooms.tourguide.concurrent.ExecutorServices;

@SpringBootApplication
public class TourguideApplication {

	public static void main(String[] args) {

		ExecutorServices.setEsRewardsService(Executors.newCachedThreadPool());
		ExecutorServices.setEsTourGuideService(Executors.newCachedThreadPool());
		ExecutorServices.setEsTracker(Executors.newScheduledThreadPool(1));

		SpringApplication.run(TourguideApplication.class, args);
	}

}
