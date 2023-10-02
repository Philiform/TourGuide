package com.openclassrooms.tourguide.service.dto;

import gpsUtil.location.Location;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttractionNearDTO {

	private String name;
	private Location locationAttraction;
	private Location locationUser;
	private double distance;
	private int rewardPoints;

	@Override
	public String toString() {
		return 	"AttractionNearDTO [name=" + name +
				", locationAttraction=(lat=" + locationAttraction.latitude +
				", long=" + locationAttraction.longitude + ")" +
				", locationUser=(lat=" + locationUser.latitude +
				", long=" + locationUser.longitude + ")" +
				", distance=" + distance +
				", rewardPoints=" + rewardPoints + "]";
	}

}
