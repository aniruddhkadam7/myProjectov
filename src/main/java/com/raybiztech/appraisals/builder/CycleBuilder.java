package com.raybiztech.appraisals.builder;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.raybiztech.appraisals.business.Cycle;
import com.raybiztech.appraisals.dto.CycleDTO;

@Component("cycleBuilder")
public class CycleBuilder {

	@Autowired
	AppraisalBuilder appraisalBuilder;

	Logger logger = Logger.getLogger(CycleBuilder.class);
	
	public Cycle createCycleEntity(CycleDTO cycleDto) {
		Cycle cycle = new Cycle();
		if (cycleDto != null) {
			cycle.setCycleId(cycleDto.getId());
			cycle.setName(cycleDto.getName());
			cycle.setPercentage_Done(cycleDto.getPercentage_Done());
			cycle.setStatus(cycleDto.getStatus());
			cycle.setToDate(cycleDto.getToDate());
			cycle.setFromDate(cycleDto.getFromDate());
			cycle.setDescription(cycleDto.getDescription());
		}
		return cycle;
	}

	public CycleDTO createCycleDTO(Cycle cycle) {

		CycleDTO cycleDto = new CycleDTO();
		if (cycle != null) {
			cycleDto.setId(cycle.getCycleId());
			cycleDto.setName(cycle.getName());
			cycleDto.setPercentage_Done(cycle.getPercentage_Done());
			cycleDto.setStatus(cycle.getStatus());
			cycleDto.setToDate(convertDate(cycle.getToDate()));
			cycleDto.setFromDate(convertDate(cycle.getFromDate()));
			cycleDto.setDescription(cycle.getDescription());
			/*
			 * Set<Appraisal> appraisalList = cycle.getAppraisals();
			 * List<AppraisalDTO> appraisalDtoList = appraisalBuilder
			 * .getAppraisalDTOList(new ArrayList<Appraisal>(appraisalList));
			 * cycleDto.setAppraisalsDto(new HashSet<AppraisalDTO>(
			 * appraisalDtoList));
			 */
		}
		return cycleDto;
	}

	public List<CycleDTO> getCycleDTOList(List<Cycle> cycleList) {
		List<CycleDTO> cycleDtoList = new ArrayList<CycleDTO>();
		if (cycleList != null) {
			for (Cycle cycle : cycleList) {
				cycleDtoList.add(createCycleDTO(cycle));
			}
		}

		return cycleDtoList;
	}

	public String convertDate(String dateStr) {
	    Date date = null;
	    DateFormat fromFormat = new SimpleDateFormat("yyyy/MM/dd");
	    fromFormat.setLenient(false);
	    DateFormat toFormat = new SimpleDateFormat("dd-MMM-yyyy");
	    toFormat.setLenient(false);
	    try{
	        date = fromFormat.parse(dateStr);
	    } catch(ParseException e){
	        e.printStackTrace();
	    }
	    
	    logger.info("date after formatting : "+toFormat.format(date));
	    return toFormat.format(date);
	}

}