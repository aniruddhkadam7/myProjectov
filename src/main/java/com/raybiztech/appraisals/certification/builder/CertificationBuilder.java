package com.raybiztech.appraisals.certification.builder;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.certification.Dao.CertificateDao;
import com.raybiztech.appraisals.certification.Dto.CertificateTypeDto;
import com.raybiztech.appraisals.certification.Dto.CertificationDto;
import com.raybiztech.appraisals.certification.business.CertificateType;
import com.raybiztech.appraisals.certification.business.Certification;
import com.raybiztech.appraisals.dao.DAO;
import com.raybiztech.appraisals.security.utils.SecurityUtils;
import com.raybiztech.date.Date;
import com.raybiztech.recruitment.business.Technology;
import com.raybiztech.recruitment.utils.DateParser;

@Component("certificationBuilder")
public class CertificationBuilder {

	@Autowired
	CertificateDao certificateDao;
	
	@Autowired
	DAO dao;
	
	@Autowired
	SecurityUtils securityUtils;

	public Certification toEntity(CertificationDto certificationDto) {
		Certification certification = null;

		if (certificationDto != null) {
			certification = new Certification();
			certification.setId(certificationDto.getId());
			certification.setCode(certificationDto.getCode());
			certification.setTechnology(certificationDto.getTechnology() != null ? certificationDto.getTechnology() : null);
			certification.setCertificateType(certificationDto.getCertificateType() != null ? certificationDto.getCertificateType() : null);
			certification.setDescription(certificationDto.getDescription());
			certification.setName(certificationDto.getName());
			try {
				if (certificationDto.getPercent() != null)
					certification.setPercentage(Double
							.parseDouble(certificationDto.getPercent()));
			} catch (NumberFormatException ex) {
				
			}

			// certification.setSkill(certificationDto.getSkill());
			try {
				certification.setCompletedDate(DateParser
						.toDate(certificationDto.getCompletedDate()));
				certification.setExpiryDate(DateParser.toDate(certificationDto
						.getExpiryDate()));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return certification;
	}

	public CertificationDto toDto(Certification certification) {
		CertificationDto certificationDto = null;
		if (certification != null) {
			certificationDto = new CertificationDto();
			certificationDto.setId(certification.getId());
			certificationDto.setCode(certification.getCode());
			certificationDto.setTechnology(certification.getTechnology() != null ? certification.getTechnology() : null);
			certificationDto.setCertificateType(certification.getCertificateType() != null ? certification.getCertificateType() : null);
			certificationDto.setName(certification.getName());
			certificationDto.setDescription(certification.getDescription());
			if (certification.getExpiryDate() != null)
				certificationDto.setExpiryDate(certification.getExpiryDate()
						.toString("dd/MM/yyyy"));
			if (certification.getCompletedDate() != null)
				certificationDto.setCompletedDate(certification
						.getCompletedDate().toString("dd/MM/yyyy"));
			if (certification.getPercentage() != null)
				certificationDto.setPercent(certification.getPercentage()
						.toString());
			certificationDto.setSkill(certification.getSkill());

			certificationDto.setEmployeeId(certification.getEmployee()
					.getEmployeeId());

		}
		return certificationDto;
	}

	public List<CertificationDto> toDtoList(List<Certification> list) {
		List<CertificationDto> dtosList = new ArrayList<CertificationDto>();
		for (Certification certification : list) {
			dtosList.add(toDto(certification));
		}
		return dtosList;
	}
	
	public CertificateType toCertificateTypeEntity(CertificateTypeDto dto) {
		Long empId = securityUtils.getLoggedEmployeeIdforSecurityContextHolder();
		CertificateType certificate = null;
		if(dto != null) {
			if (dto.getId() == null) {
				certificate = new CertificateType();
				certificate.setCreatedBy(empId);
				certificate.setCreatedDate(new Date());
			} else {
				certificate = dao.findBy(CertificateType.class, dto.getId());
				certificate.setUpdatedBy(empId);
				certificate.setUpdatedDate(new Date());
			}
			
			certificate.setTechnology(dao.findBy(Technology.class, dto.getTechnologyId()));
			certificate.setCertificateType(dto.getCertificateType());
		}
		return certificate;
		
	}

	public CertificateTypeDto toCertificateTypeDto(CertificateType certificate) {
		CertificateTypeDto dto = null;
		if (certificate != null) {
			dto = new CertificateTypeDto();
			dto.setId(certificate.getId());
			dto.setTechnologyId(certificate.getTechnology().getId());
			dto.setTechnologyName(certificate.getTechnology().getName());
			dto.setCertificateType(certificate.getCertificateType());
			Employee createdEmp = dao.findBy(Employee.class, certificate.getCreatedBy());
			dto.setCreatedBy(createdEmp.getFullName());
			dto.setCreatedDate(certificate.getCreatedDate().toString("dd/MM/yyyy"));
			if (certificate.getUpdatedBy() != null) {
				Employee updatedEmp = dao.findBy(Employee.class, certificate.getUpdatedBy());
				dto.setUpdatedBy(updatedEmp.getFullName());
			}
			if (certificate.getUpdatedDate() != null) {
				dto.setUpdatedDate(certificate.getUpdatedDate().toString("dd/MM/yyyy"));
			}
		}
		return dto;

	}
	
	public List<CertificateTypeDto> toCertificateTypeDTOList(List<CertificateType> certificateList) {
		List<CertificateTypeDto> dtoList = null;
		if (certificateList != null) {
			dtoList = new ArrayList<>();
			for (CertificateType certificate : certificateList) {
				dtoList.add(toCertificateTypeDto(certificate));
			}
		}
		return dtoList;
	}
}
