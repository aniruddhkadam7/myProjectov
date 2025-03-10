package com.raybiztech.separation.builder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.dao.DAO;
import com.raybiztech.appraisals.security.utils.SecurityUtils;
import com.raybiztech.date.Second;
import com.raybiztech.separation.business.ClearanceCertificate;
import com.raybiztech.separation.business.Separation;
import com.raybiztech.separation.dto.ClearanceCertificateDTO;
import com.raybiztech.separation.dto.SeparationDTO;

@Component("clearanceCertificateBuilder")
public class ClearanceCertificateBuilder {
	@Autowired
	DAO dao;
	@Autowired
	SecurityUtils securityUtils;


	public ClearanceCertificate toEntity(
			ClearanceCertificateDTO clearanceCertificateDTO) {
		ClearanceCertificate clearanceCertificate = null;
		if (clearanceCertificateDTO != null) {
			clearanceCertificate = new ClearanceCertificate();
			Employee employee=(Employee)securityUtils.getLoggedEmployeeDetailsSecurityContextHolder().get("employee");
			clearanceCertificate.setEmployee(employee);
			clearanceCertificate.setComments(clearanceCertificateDTO.getComments());
			clearanceCertificate.setIsDue(clearanceCertificateDTO.getIsDue());
			clearanceCertificate.setCreatedDate(new Second());
			clearanceCertificate.setAddedBy(clearanceCertificateDTO.getAddedBy());

		}
		return clearanceCertificate;

	}

	public ClearanceCertificateDTO toDTO(
			ClearanceCertificate clearanceCertificate) {
		ClearanceCertificateDTO clearanceCertificateDTO = null;
		if (clearanceCertificate != null) {
			clearanceCertificateDTO = new ClearanceCertificateDTO();
			clearanceCertificateDTO.setCcId(clearanceCertificate.getCcId());
			clearanceCertificateDTO.setComments(clearanceCertificate
					.getComments());
			clearanceCertificateDTO.setEmployeeId(clearanceCertificate
					.getEmployee().getEmployeeId());
			clearanceCertificateDTO.setEmployeeName(clearanceCertificate
					.getEmployee().getFullName());
			clearanceCertificateDTO.setIsDue(clearanceCertificate.getIsDue());
			clearanceCertificateDTO.setCreatedDate(clearanceCertificate
					.getCreatedDate().toString("dd/MM/yyyy"));
			clearanceCertificateDTO.setAddedBy(clearanceCertificate.getAddedBy());

		}

		return clearanceCertificateDTO;
	}

	public Set<ClearanceCertificateDTO> convertEntityToDTO(
			Set<ClearanceCertificate> clearanceCertificate) {
		Set<ClearanceCertificateDTO> clearanceCertificateDTOList = null;
		if (clearanceCertificate != null) {
			clearanceCertificateDTOList = new HashSet<ClearanceCertificateDTO>();
			for (ClearanceCertificate clearanceCertificates : clearanceCertificate) {
				clearanceCertificateDTOList.add(toDTO(clearanceCertificates));
			}

		}
		return clearanceCertificateDTOList;
	}
	
	public List<ClearanceCertificateDTO> toDtoList(List<ClearanceCertificate> clearance, Separation separation) {
		List<ClearanceCertificateDTO> clearanceDto = null;
		if (clearance != null) {
			clearanceDto = new ArrayList<ClearanceCertificateDTO>();
			for (ClearanceCertificate clearanceCertificate : clearance) {
				clearanceDto.add(toDTOClearance(clearanceCertificate, separation));
			}
		}
		return clearanceDto;
	}
	
	public ClearanceCertificateDTO toDTOClearance(
			ClearanceCertificate clearanceCertificate, Separation separation) {
		ClearanceCertificateDTO clearanceCertificateDTO = null;
		if (clearanceCertificate != null) {
			clearanceCertificateDTO = new ClearanceCertificateDTO();
			clearanceCertificateDTO.setCcId(clearanceCertificate.getCcId());
			clearanceCertificateDTO.setComments(clearanceCertificate
					.getComments());
			clearanceCertificateDTO.setEmployeeId(clearanceCertificate
					.getEmployee().getEmployeeId());
			clearanceCertificateDTO.setEmployeeName(clearanceCertificate
					.getEmployee().getFullName());
			clearanceCertificateDTO.setIsDue(clearanceCertificate.getIsDue());
			clearanceCertificateDTO.setCreatedDate(clearanceCertificate
					.getCreatedDate().toString("dd/MM/yyyy"));
			clearanceCertificateDTO.setSeperationEmpId(separation.getEmployee()
					.getEmployeeId());
			clearanceCertificateDTO.setSeperationEmpName(separation
					.getEmployee().getEmployeeFullName());
			clearanceCertificateDTO.setAddedBy(clearanceCertificate.getAddedBy());
		}

		return clearanceCertificateDTO;
	}
}
