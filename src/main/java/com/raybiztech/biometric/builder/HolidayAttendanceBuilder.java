package com.raybiztech.biometric.builder;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.raybiztech.biometric.dto.BioAttendanceDTO;
import com.raybiztech.recruitment.business.Holidays;

@Component("holidayAttendanceBuilder")
public class HolidayAttendanceBuilder {

	Logger logger = Logger.getLogger(HolidayAttendanceBuilder.class);

	public BioAttendanceDTO createHolidayBioAttendanceDTO(Holidays holiday) {

		BioAttendanceDTO bioAttendanceDTO = new BioAttendanceDTO();
		bioAttendanceDTO.setId(holiday.getId());
		bioAttendanceDTO.setStart(holiday.getDate().toString("yyyy-MM-dd"));
		bioAttendanceDTO.setColor("#ebaa4b");
		bioAttendanceDTO.setTitle(holiday.getName());
		bioAttendanceDTO.setOverlap(false);
		System.out.println("country here:" + holiday.getCountry());

		return bioAttendanceDTO;
	}
	public SortedSet<BioAttendanceDTO> createHolidayAttendanceDTOSET(
			Set<Holidays> holidaysSet,String country) {
		// TODO Auto-generated method stub

		logger.info("Total holidays are "+holidaysSet.size());
		
		SortedSet<BioAttendanceDTO> holidayAttendanceDtoSet = new TreeSet<BioAttendanceDTO>();
		for (Holidays holiday : holidaysSet) {
			if(holiday.getCountry().equals(country)){
			holidayAttendanceDtoSet.add(createHolidayBioAttendanceDTO(holiday));
			}
             

		}
		return holidayAttendanceDtoSet;
	}

	public SortedSet<BioAttendanceDTO> createHolidayAttendanceDTOSet(
			Set<Holidays> holidaysSet) {
		// TODO Auto-generated method stub

		logger.info("Total holidays are "+holidaysSet.size());
		
		SortedSet<BioAttendanceDTO> holidayAttendanceDtoSet = new TreeSet<BioAttendanceDTO>();
		for (Holidays holiday : holidaysSet) {

			holidayAttendanceDtoSet.add(createHolidayBioAttendanceDTO(holiday));

		}
		return holidayAttendanceDtoSet;
	}

}
