package com.raybiztech.meetingrequest.builder;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.raybiztech.meetingrequest.business.Location;
import com.raybiztech.meetingrequest.dto.LocationDto;
import java.util.ArrayList;
import java.util.List;

@Component("locationBuilder")
public class LocationBuilder {

	Logger logger = Logger.getLogger(LocationBuilder.class);

	public Location toEntity(LocationDto locationDto) {

		Location location = null;
		if (locationDto != null) {
			location = new Location();
			location.setId(locationDto.getId());
			location.setLocationName(locationDto.getLocationName());
		}

		return location;

	}

	public LocationDto toDto(Location location) {
		
		LocationDto locationDto = null;
		if (location != null) {
			locationDto = new LocationDto();
			locationDto.setId(location.getId());
			locationDto.setLocationName(location.getLocationName());
		}
		return locationDto;
	}

	public List<LocationDto> convertEntityListToDtoList(
			List<Location> locationlist) {

		List<LocationDto> locationDtos = null;
		if (locationlist != null) {
			locationDtos = new ArrayList<LocationDto>();
			for (Location location : locationlist) {
				if(location.getLocationStatus().equals(Boolean.TRUE))
				{
					locationDtos.add(toDto(location));
				}
				
			}
		}
		return locationDtos;
	}

	public List<Location> convertDtoListToEntityList(
			List<LocationDto> locationDtos) {
		List<Location> locations = null;
		if (locationDtos != null) {
			locations = new ArrayList<Location>();
			for (LocationDto locationDto : locationDtos) {
				locations.add(toEntity(locationDto));
			}
		}
		return locations;
	}
}
