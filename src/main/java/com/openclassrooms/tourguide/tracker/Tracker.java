package com.openclassrooms.tourguide.tracker;

import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.openclassrooms.tourguide.concurrent.ExecutorServices;
import com.openclassrooms.tourguide.service.TourGuideService;
import com.openclassrooms.tourguide.user.User;

public class Tracker {

	private Logger logger = LoggerFactory.getLogger(Tracker.class);

	private static final long trackingPollingInterval = TimeUnit.MINUTES.toSeconds(5);
	private final ScheduledExecutorService executor;

	private final TourGuideService tourGuideService;

	public Tracker(TourGuideService tourGuideService) {
		this.tourGuideService = tourGuideService;

		executor = ExecutorServices.getEsTracker();
	}

	public void startTracking() {
		executor.scheduleAtFixedRate(
				 getRunnable(),
				 0,
				 trackingPollingInterval,
				 TimeUnit.SECONDS);

	}

	private Runnable getRunnable() {
		final Runnable runnableTrackUsersLocation = new Runnable() {
			@Override
			public void run() {
				StopWatch stopWatch = new StopWatch();

				List<User> users = tourGuideService.getAllUsers();
				logger.debug("Begin Tracker. Tracking " + users.size() + " users.");
				stopWatch.start();

				tourGuideService.trackLocationForAllUsers(users);

				stopWatch.stop();
				logger.debug("Tracker Time Elapsed: " + TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()) + " seconds.");
				stopWatch.reset();

				logger.debug("Tracker sleeping");
			}
		};

		return runnableTrackUsersLocation;
	}

}
