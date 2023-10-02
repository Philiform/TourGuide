package com.openclassrooms.tourguide.service.dto;

import gpsUtil.location.Attraction;
import gpsUtil.location.Location;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttractionDistanceDTO {

	private Attraction attraction;
	private Location location;
	private double distance;

}
