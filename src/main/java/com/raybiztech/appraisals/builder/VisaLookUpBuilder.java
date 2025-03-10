package com.raybiztech.appraisals.builder;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.raybiztech.appraisals.business.VisaLookUp;
import com.raybiztech.appraisals.dao.DAO;
import com.raybiztech.appraisals.dto.VisaLookUpDTO;
import com.raybiztech.appraisals.security.utils.SecurityUtils;
import com.raybiztech.projectmanagement.invoice.lookup.CountryLookUp;

@Component("visaLookUpBuilder")
public class VisaLookUpBuilder {

	@Autowired
	SecurityUtils securityUtils;
	@Autowired
	DAO dao;

	public VisaLookUp convertDTOToEntity(VisaLookUpDTO visaLookUpDTO) {
		VisaLookUp visaLookUp = null;
		if (visaLookUpDTO != null) {
			visaLookUp = new VisaLookUp();
			visaLookUp.setVisaType(visaLookUpDTO.getVisaType());
			visaLookUp.setCountry(dao.findBy(CountryLookUp.class, visaLookUpDTO.getCountryId()));
		}

		return visaLookUp;
	}

	public VisaLookUpDTO convertEntityToDTO(VisaLookUp visaLookUp) {
		VisaLookUpDTO visaLookUpDTO = null;
		if (visaLookUp != null) {
			visaLookUpDTO = new VisaLookUpDTO();
			visaLookUpDTO.setVisaTypeId(visaLookUp.getId());
			visaLookUpDTO.setVisaType(visaLookUp.getVisaType());
			visaLookUpDTO.setCountryId(visaLookUp.getCountry().getId().longValue());
			visaLookUpDTO.setCountryName(visaLookUp.getCountry().getName());
		}
		return visaLookUpDTO;
	}

	public List<VisaLookUpDTO> getDTOList(List<VisaLookUp> visaLookUps) {
		List<VisaLookUpDTO> visaLookUpDTOs = null;
		if (visaLookUps != null) {
			visaLookUpDTOs = new ArrayList<VisaLookUpDTO>();
			for (VisaLookUp visaLookUp : visaLookUps) {
				visaLookUpDTOs.add(convertEntityToDTO(visaLookUp));
			}
		}
		return visaLookUpDTOs;
	}


}
