package com.openclassrooms.tourguide.concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;

public class ExecutorServices {

	private static ExecutorService esRewardsService = null;
	private static ExecutorService esTourGuideService = null;
	private static ScheduledExecutorService esTracker = null;

	public  ExecutorServices() {
	}

	public static ExecutorService getEsRewardsService() {
		return esRewardsService;
	}

	public static void setEsRewardsService(ExecutorService esRewardsService) {
		ExecutorServices.esRewardsService = esRewardsService;
	}

	public static void shutdownNowEsRewardsService() {
		esRewardsService.shutdownNow();
	}

	public static ExecutorService getEsTourGuideService() {
		return esTourGuideService;
	}

	public static void setEsTourGuideService(ExecutorService esTourGuideService) {
		ExecutorServices.esTourGuideService = esTourGuideService;
	}

	public static void shutdownNowEsTourGuideService() {
		esTourGuideService.shutdownNow();
	}

	public static ScheduledExecutorService getEsTracker() {
		return esTracker;
	}

	public static void setEsTracker(ScheduledExecutorService esTracker) {
		ExecutorServices.esTracker = esTracker;
	}

	public static void shutdownNowEsTracker() {
		esTracker.shutdownNow();
	}

}
