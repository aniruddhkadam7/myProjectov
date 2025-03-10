package com.raybiztech.appraisalmanagement.builder;

import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.raybiztech.appraisalmanagement.business.AppraisalKPIData;
import com.raybiztech.appraisalmanagement.dto.AppraisalKPIDataDto;
import com.raybiztech.appraisalmanagement.dto.ManagerCommentsDto;
import com.raybiztech.appraisals.builder.EmployeeBuilder;
import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.dao.DAO;
import com.raybiztech.appraisals.security.utils.SecurityUtils;

@Component("appraisalKPIDataBuilder")
public class AppraisalKPIDataBuilder {

	@Autowired
	EmployeeBuilder employeeBuilder;
	@Autowired
	DAO dao;
	@Autowired
	SecurityUtils securityUtils;

	Logger logger = Logger.getLogger(AppraisalKPIDataBuilder.class);

	public AppraisalKPIData toEntity(AppraisalKPIDataDto appraisalKpiDataDto) {
		Long loggedInEmpId = securityUtils
				.getLoggedEmployeeIdforSecurityContextHolder();
		AppraisalKPIData appraisalKpiData = null;
		if (appraisalKpiDataDto != null) {
			appraisalKpiData = new AppraisalKPIData();
			appraisalKpiData.setDescription(appraisalKpiDataDto
					.getDescription());
			appraisalKpiData.setFeedback(appraisalKpiDataDto
					.getEmployeeFeedback());
			appraisalKpiData.setRating(appraisalKpiDataDto.getEmployeeRating());
			appraisalKpiData.setLevel(0);
			appraisalKpiData.setEmpId(loggedInEmpId);
			appraisalKpiData.setName(appraisalKpiDataDto.getName());
		}

		return appraisalKpiData;
	}

	public AppraisalKPIDataDto toDto(AppraisalKPIData appraisalKpiDataEntity) {
		AppraisalKPIDataDto appraisalKpiDataDto = null;
		if (appraisalKpiDataEntity != null) {
			appraisalKpiDataDto = new AppraisalKPIDataDto();
			appraisalKpiDataDto.setDescription(appraisalKpiDataEntity
					.getDescription());
			appraisalKpiDataDto.setFrequency(appraisalKpiDataEntity
					.getFrequency());

			appraisalKpiDataDto.setTarget(appraisalKpiDataEntity.getTarget());
			appraisalKpiDataDto.setEmployeeFeedback(appraisalKpiDataEntity
					.getFeedback());
			appraisalKpiDataDto.setEmployeeRating(appraisalKpiDataEntity
					.getRating());
			appraisalKpiDataDto.setId(appraisalKpiDataEntity.getId());
			Set<ManagerCommentsDto> managerCommentsDtos = new HashSet<ManagerCommentsDto>();
			if (appraisalKpiDataEntity.getManagersFeedback() != null) {
				Set<AppraisalKPIData> managersFeedback = appraisalKpiDataEntity
						.getManagersFeedback();

				for (AppraisalKPIData appraisalKPIData : managersFeedback) {
					ManagerCommentsDto commentsDto = new ManagerCommentsDto();
					if (appraisalKPIData.getStatus()
							.equalsIgnoreCase("PENDING")
							|| appraisalKPIData.getStatus().equalsIgnoreCase(
									"OPENFORDISCUSSION")
							|| appraisalKPIData.getStatus().equalsIgnoreCase(
									"PENDINGAGREEMENT")) {

						if (appraisalKPIData
								.getEmpId()
								.equals(securityUtils
										.getLoggedEmployeeIdforSecurityContextHolder())) {
							commentsDto.setStatus(appraisalKPIData.getStatus());
							appraisalKpiDataDto
									.setManagerFeedback(appraisalKPIData
											.getFeedback());
							appraisalKpiDataDto
									.setManagerRating(appraisalKPIData
											.getRating());

						}
					}
					if (appraisalKPIData.getStatus().equalsIgnoreCase("SUBMIT")
							|| appraisalKPIData.getStatus().equalsIgnoreCase(
									"OPENFORDISCUSSION")
							|| appraisalKPIData.getStatus().equalsIgnoreCase(
									"PENDINGAGREEMENT")) {
						commentsDto.setManagerComments(appraisalKPIData
								.getFeedback());
						commentsDto.setManagerRating(appraisalKPIData
								.getRating());
						commentsDto.setStatus(appraisalKPIData.getStatus());

					}

					commentsDto.setLevel(appraisalKPIData.getLevel());
					commentsDto.setId(appraisalKPIData.getId());
					commentsDto.setEmployeeId(appraisalKPIData.getEmpId());
					Employee employee = dao.findBy(Employee.class,
							appraisalKPIData.getEmpId());
					commentsDto.setEmployeeName(employee.getFullName());

					managerCommentsDtos.add(commentsDto);

				}
			}

			appraisalKpiDataDto.setName(appraisalKpiDataEntity.getName());
			appraisalKpiDataDto.setManagerCommentsDtos(managerCommentsDtos);

		}
		return appraisalKpiDataDto;
	}

	public Set<AppraisalKPIData> toEntitySet(
			Set<AppraisalKPIDataDto> appraisalKpiDataDtoSet) {
		Set<AppraisalKPIData> appraisalKpiData = new HashSet<AppraisalKPIData>();
		for (AppraisalKPIDataDto appraisalKpiDataSet : appraisalKpiDataDtoSet) {
			appraisalKpiData.add(toEntity(appraisalKpiDataSet));
		}
		return appraisalKpiData;

	}

	public Set<AppraisalKPIDataDto> toDtoSet(
			Set<AppraisalKPIData> appraisalKpiDataEntity) {
		Set<AppraisalKPIDataDto> appraisalKpiDataDtoSet = new HashSet<AppraisalKPIDataDto>();
		for (AppraisalKPIData appraisalKpiData : appraisalKpiDataEntity) {
			appraisalKpiDataDtoSet.add(toDto(appraisalKpiData));
		}
		return appraisalKpiDataDtoSet;
	}
}
