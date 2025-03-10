package com.raybiztech.projectmanagement.builder;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.stereotype.Component;

import com.raybiztech.appraisals.dao.DAO;
import com.raybiztech.payslip.utility.AES256Encryption;
import com.raybiztech.projectmanagement.business.ChangeRequest;
import com.raybiztech.projectmanagement.business.Project;
import com.raybiztech.projectmanagement.business.ProjectNumbers;
import com.raybiztech.projectmanagement.dto.ProjectNumbersDTO;

@Component("projectNumbersBuilder")
public class ProjectNumbersBuilder {

	@Autowired
	DAO dao;

	org.apache.log4j.Logger logger = org.apache.log4j.Logger
			.getLogger(ProjectNumbersBuilder.class);

	public ProjectNumbers toEntity(ProjectNumbersDTO numbersDTO) {

		ProjectNumbers projectNumbers = null;
		if (numbersDTO != null) {
			projectNumbers = new ProjectNumbers();
			Project project = dao.findBy(Project.class,
					numbersDTO.getProjectId());

			String saltKey = KeyGenerators.string().generateKey();
			AES256Encryption aes256Encryption = new AES256Encryption(
					String.valueOf(project.getId()), saltKey);
			projectNumbers.setSaltKey(saltKey);
			projectNumbers.setId(numbersDTO.getId());
			projectNumbers.setProject(project);
			projectNumbers.setProjectAmount(aes256Encryption.encrypt(numbersDTO
					.getProjectAmount()));
			projectNumbers.setCurrency(numbersDTO.getCurrency());
			projectNumbers.setHourlyRate(aes256Encryption.encrypt(numbersDTO
					.getHourlyRate()));
			projectNumbers.setDescription(numbersDTO.getDescription());

			ChangeRequest changeRequest = (numbersDTO.getChangeRequestId() != null) ? dao
					.findBy(ChangeRequest.class,
							numbersDTO.getChangeRequestId()) : null;

			projectNumbers
					.setChangeRequest((changeRequest != null) ? changeRequest
							: null);

			projectNumbers.setPoNumber(numbersDTO.getPoNumber());
		}
		return projectNumbers;
	}

	public ProjectNumbersDTO toDto(ProjectNumbers numbers) {
		ProjectNumbersDTO projectNumbersDTO = null;
		if (numbers != null) {

			AES256Encryption aes256Encryption = new AES256Encryption(
					String.valueOf(numbers.getProject().getId()),
					numbers.getSaltKey());
			projectNumbersDTO = new ProjectNumbersDTO();
			projectNumbersDTO.setId(numbers.getId());
			projectNumbersDTO.setProjectId(numbers.getProject().getId());
			projectNumbersDTO.setProjectname(numbers.getProject()
					.getProjectName());
			projectNumbersDTO.setProjectAmount(aes256Encryption.decrypt(numbers
					.getProjectAmount()));
			projectNumbersDTO
					.setHourlyRate((numbers.getHourlyRate() != null) ? aes256Encryption
							.decrypt(numbers.getHourlyRate()) : null);
			projectNumbersDTO.setCurrency(numbers.getCurrency());
			projectNumbersDTO.setDescription(numbers.getDescription());

			projectNumbersDTO
					.setPoNumber((numbers.getPoNumber() != "") ? numbers
							.getPoNumber() : "N/A");

			if (numbers.getChangeRequest() != null) {
				projectNumbersDTO.setChangeRequestId(numbers.getChangeRequest()
						.getId());
				projectNumbersDTO.setCrName(numbers.getChangeRequest()
						.getName());
				projectNumbersDTO.setCrhours(numbers.getChangeRequest()
						.getDuration());

			}

		}
		return projectNumbersDTO;
	}

	public List<ProjectNumbersDTO> getProjectNumbersList(
			List<ProjectNumbers> projectNumbersList) {
		List<ProjectNumbersDTO> projectNumbersDTOs = null;
		if (projectNumbersList != null) {
			projectNumbersDTOs = new ArrayList<ProjectNumbersDTO>();
			for (ProjectNumbers numbers : projectNumbersList) {
				projectNumbersDTOs.add(toDto(numbers));
			}
		}
		return projectNumbersDTOs;
	}

}
