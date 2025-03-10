package com.raybiztech.appraisals.builder;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.business.VisaDetails;
import com.raybiztech.appraisals.business.VisaLookUp;
import com.raybiztech.appraisals.dao.DAO;
import com.raybiztech.appraisals.dto.VisaDetailDTO;
import com.raybiztech.appraisals.security.utils.SecurityUtils;
import com.raybiztech.date.Second;
import com.raybiztech.recruitment.service.JobPortalService;
import com.raybiztech.recruitment.utils.DateParser;

@Component("visaDetailsBuilder")
public class VisaDetailsBuilder {

	@Autowired
	DAO dao;
	@Autowired
	SecurityUtils securityUtils;
	
	@Autowired
	JobPortalService jobPortalServiceImpl;

	Logger logger = Logger.getLogger(VisaDetailsBuilder.class);

	public VisaDetails toEntity(VisaDetailDTO visaDetailDTO) {
		Long loggedInEmpId = securityUtils.getLoggedEmployeeIdforSecurityContextHolder();
		Employee employee = dao.findBy(Employee.class, loggedInEmpId);

		VisaDetails visaDetails = null;
		if (visaDetailDTO != null) {
			visaDetails = new VisaDetails();
			try {
				visaDetails.setDateOfIssue(DateParser.toDate(visaDetailDTO.getDateOfIssue()));
				visaDetails.setDateOfExpire(DateParser.toDate(visaDetailDTO.getDateOfExpire()));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			VisaLookUp visaLookUp = dao.findBy(VisaLookUp.class, visaDetailDTO.getVisaTypeId());
			visaDetails.setVisaLookUp(visaLookUp);
			visaDetails.setEmployee(dao.findBy(Employee.class, loggedInEmpId));
			visaDetails.setCreatedBy(employee.getEmployeeId());
			visaDetails.setCreatedDate(new Second());
		}

		return visaDetails;

	}

	public VisaDetailDTO toDTO(VisaDetails visaDetails) {

		Employee createdBy = null, updatedBy = null;
		createdBy = visaDetails.getCreatedBy() != null ? dao.findBy(Employee.class, visaDetails.getCreatedBy()) : null;
		updatedBy = visaDetails.getUpdatedBy() != null ? dao.findBy(Employee.class, visaDetails.getUpdatedBy()) : null;
		VisaDetailDTO dto = null;
		if (visaDetails != null) {
			dto = new VisaDetailDTO();
			dto.setId(visaDetails.getId());
			dto.setDateOfIssue(visaDetails.getDateOfIssue().toString("dd/MM/yyyy"));
			dto.setDateOfExpire(visaDetails.getDateOfExpire().toString("dd/MM/yyyy"));
			dto.setVisaTypeId(visaDetails.getVisaLookUp().getId());
			dto.setVisaType(visaDetails.getVisaLookUp().getVisaType());
			dto.setCountryId(visaDetails.getVisaLookUp().getCountry().getId().longValue());
			dto.setCountryName(visaDetails.getVisaLookUp().getCountry().getName());
			if (visaDetails.getEmployee() != null) {
				dto.setEmpName(visaDetails.getEmployee().getFullName());
				dto.setEmpId(visaDetails.getEmployee().getEmployeeId());
			}
			dto.setCreatedBy(createdBy != null ? createdBy.getFullName() : "");
			dto.setUpdatedBy(updatedBy != null ? updatedBy.getFullName() : "");
			dto.setCreatedDate(
					visaDetails.getCreatedDate() != null ? visaDetails.getCreatedDate().toString("dd/MM/yyyy") : "");
			dto.setUpdatedDate(
					visaDetails.getUpdatedDate() != null ? visaDetails.getUpdatedDate().toString("dd/MM/yyyy") : "");
			if(visaDetails.getVisaDetailsPath()!=null) {
			String path = visaDetails.getVisaDetailsPath();
			String frontsplitPath = path.substring(path.lastIndexOf("/") + 1);
			dto.setVisaDetailsPath("../visaDetailsPath/"+frontsplitPath);
			dto.setVisaDetailsData(jobPortalServiceImpl.getVisaImage(visaDetails.getId()).getVisaDetailsData());
			}
			String visaThumbpath = visaDetails.getVisaThumbPicture();
			if(visaThumbpath!=null) {
				String splitedvisaThumbpath = visaThumbpath.substring(visaThumbpath.lastIndexOf("/") + 1);
				dto.setVisaThumbPicture("../visadetails/" + splitedvisaThumbpath);
			}
			
		}

		return dto;

	}

	public VisaDetails toEditEntity(VisaDetailDTO dto) {
		Long loggedInEmpId = securityUtils.getLoggedEmployeeIdforSecurityContextHolder();
		Employee employee = dao.findBy(Employee.class, loggedInEmpId);
		VisaDetails visaDetails = null;
		if (dto != null) {
			visaDetails = dao.findBy(VisaDetails.class, dto.getId());
			VisaLookUp visaLookUp = dao.findBy(VisaLookUp.class, dto.getVisaTypeId());
			visaDetails.setVisaLookUp(visaLookUp);
			
			try {
				visaDetails.setDateOfIssue(DateParser.toDate(dto.getDateOfIssue()));
				visaDetails.setDateOfExpire(DateParser.toDate(dto.getDateOfExpire()));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			visaDetails.setUpdatedBy(employee.getEmployeeId());
			visaDetails.setUpdatedDate(new Second());
			//visaDetails.setVisaDetailsPath(dto.getVisaDetailsPath());
		}

		return visaDetails;
	}
	
	public List<VisaDetailDTO> toDTOList(List<VisaDetails> visaDetailsList) {
		List<VisaDetailDTO> visaDetailDTOList = null;
		if (visaDetailsList != null) {
			visaDetailDTOList = new ArrayList<VisaDetailDTO>();
			for (VisaDetails visaDetails : visaDetailsList) {
				visaDetailDTOList.add(toDTO(visaDetails));
			}
		}
		return visaDetailDTOList;

	}
}
