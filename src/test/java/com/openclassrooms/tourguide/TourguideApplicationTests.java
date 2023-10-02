package com.openclassrooms.tourguide;

import java.util.concurrent.Executors;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.openclassrooms.tourguide.concurrent.ExecutorServices;

@SpringBootTest
class TourguideApplicationTests {

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		ExecutorServices.setEsRewardsService(Executors.newCachedThreadPool());
		ExecutorServices.setEsTourGuideService(Executors.newCachedThreadPool());
		ExecutorServices.setEsTracker(Executors.newScheduledThreadPool(1));
	}

	@AfterAll
	public static void cleanUp() {
		ExecutorServices.shutdownNowEsRewardsService();;
		ExecutorServices.shutdownNowEsTourGuideService();;
		ExecutorServices.shutdownNowEsTracker();;
	}

	@Test
	void contextLoads() {
	}

}
