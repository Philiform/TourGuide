package com.openclassrooms.tourguide.listener;

import org.springframework.stereotype.Component;

import com.openclassrooms.tourguide.concurrent.ExecutorServices;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;

@Component
public class MyListener implements ServletContextListener {

	@Override
	public void contextDestroyed(ServletContextEvent event) {
		ExecutorServices.shutdownNowEsRewardsService();
		ExecutorServices.shutdownNowEsTourGuideService();
		ExecutorServices.shutdownNowEsTracker();
	}

}
