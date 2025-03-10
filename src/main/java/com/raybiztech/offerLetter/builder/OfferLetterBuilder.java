package com.raybiztech.offerLetter.builder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.offerLetter.business.OfferLetter;
import com.raybiztech.offerLetter.dto.OfferLetterDto;

@Component("offerLetterbuilder")
public class OfferLetterBuilder {
	
	public OfferLetterDto toDto(OfferLetter offerLetter){
		
		Logger  logger = Logger.getLogger(OfferLetterBuilder.class);
		
		OfferLetterDto offerletterdto = null;
		
		if(offerLetter != null){
			offerletterdto = new OfferLetterDto();
			offerletterdto.setId(offerLetter.getId());
			
			Employee employee = offerLetter.getCreatedBy();
			offerletterdto.setCreatedBy(employee != null ? employee.getFirstName() : "N/A");
			offerletterdto.setEmployeeId(employee != null ? employee.getEmployeeId() : null);
			offerletterdto.setCreatedDate(offerLetter.getCreatedDate().toString("dd/MM/yyyy"));
			
			String offerletterPath = offerLetter.getOfferLetterdoc();
			
			offerletterdto.setOfferLetterdoc(offerletterPath);
			
			if (offerletterPath != null) {
				
				logger.warn("offerlettername"+(offerletterPath.substring(offerletterPath.lastIndexOf("/")+1,offerletterPath.length())));
				offerletterdto.setOfferLetterName(offerletterPath.substring(offerletterPath.lastIndexOf("/")+1,offerletterPath.length()));
				
			}
		}
		
		return offerletterdto;
		
	}
	
	public List<OfferLetterDto> toDtoList(List<OfferLetter> offerLetterList) {
		List<OfferLetterDto> offersletterdDto = null;
		if (offerLetterList != null) {
			offersletterdDto = new ArrayList<OfferLetterDto>();
			for (OfferLetter offerLetter : offerLetterList) {
				offersletterdDto.add(toDto(offerLetter));
			}
		}
		return offersletterdDto;
	}
	
	

	

}
