package com.raybiztech.appraisalmanagement.builder;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.raybiztech.appraisalmanagement.business.AppraisalCycle;
import com.raybiztech.appraisalmanagement.business.AppraisalForm;
import com.raybiztech.appraisalmanagement.business.AppraisalFormAvgRatings;
import com.raybiztech.appraisalmanagement.business.AppraisalKPIData;
import com.raybiztech.appraisalmanagement.business.AppraisalKRAData;
import com.raybiztech.appraisalmanagement.business.FormStatus;
import com.raybiztech.appraisalmanagement.dao.AppraisalDao;
import com.raybiztech.appraisalmanagement.dto.AppraisalFormAvgRatingsDto;
import com.raybiztech.appraisalmanagement.dto.AppraisalFormDto;
import com.raybiztech.appraisalmanagement.dto.AppraisalFormListDto;
import com.raybiztech.appraisalmanagement.dto.AppraisalKPIDataDto;
import com.raybiztech.appraisalmanagement.dto.AppraisalKRADataDto;
import com.raybiztech.appraisals.builder.EmployeeBuilder;
import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.dao.DAO;
import com.raybiztech.appraisals.exceptions.InvalidRatingsException;
import com.raybiztech.appraisals.security.utils.SecurityUtils;
import com.raybiztech.date.Date;
import com.raybiztech.date.Second;
import com.raybiztech.separation.builder.SeparationBuilder;

@Component("appraisalFormBuilder")
public class AppraisalFormBuilder {

	@Autowired
	EmployeeBuilder employeeBuilder;
	@Autowired
	AppraisalCycleBuilder appraisalCycleBuilder;
	@Autowired
	AppraisalKRADataBuilder appraisalKRADataBuilder;
	@Autowired
	DAO dao;
	@Autowired
	SecurityUtils securityUtils;
	@Autowired
	AppraisalDao appraisalDaoImpl;

	@Autowired
	SeparationBuilder separationBuilder;

	Logger logger = Logger.getLogger(AppraisalFormBuilder.class);

	public AppraisalForm toEntity(AppraisalFormDto appraisalFormDto) {
		Employee employee = dao.findBy(Employee.class,
				securityUtils.getLoggedEmployeeIdforSecurityContextHolder());
		AppraisalForm appraisalForm = null;
		if (appraisalFormDto != null) {

			if (appraisalFormDto.getId() == null) {
				appraisalForm = new AppraisalForm();
			} else {
				appraisalForm = dao.findBy(AppraisalForm.class,
						appraisalFormDto.getId());
			}
			// appraisalForm.setId(appraisalFormDto.getId());
			AppraisalCycle appraisalCycle = dao.findBy(AppraisalCycle.class,
					appraisalFormDto.getAppraisalCycle().getId());
			appraisalForm.setAppraisalCycle(appraisalCycle);
			if (appraisalFormDto.getKra() != null) {
				appraisalForm.setKra(appraisalKRADataBuilder
						.toEntitySet(appraisalFormDto.getKra()));
			}
			appraisalForm.setFormStatus(FormStatus.valueOf(appraisalFormDto
					.getFormStatus()));

			Set<AppraisalFormAvgRatings> avgRatingses = new HashSet<AppraisalFormAvgRatings>();
			for (int i = 0; i <= appraisalCycle.getLevelOfHierarchy(); i++) {
				if (i <= 2) {
					AppraisalFormAvgRatings formAvgRatings = null;
					Employee manager1 = dao.findBy(Employee.class, employee
							.getManager().getEmployeeId());
					Employee manager2 = dao.findBy(Employee.class, manager1
							.getManager().getEmployeeId());
					if (i == 0) {
						// for employee
						formAvgRatings = new AppraisalFormAvgRatings();
						formAvgRatings.setEmployeeId(employee.getEmployeeId());
						formAvgRatings.setEmployeeName(employee.getFullName());
						formAvgRatings.setDepartmentName(employee
								.getDepartmentName());
						formAvgRatings.setDesignationName(employee
								.getDesignation());

					} else if (i == 1) {
						// for first manager
						formAvgRatings = new AppraisalFormAvgRatings();
						formAvgRatings.setEmployeeId(manager1.getEmployeeId());
						formAvgRatings.setEmployeeName(manager1.getFullName());
						formAvgRatings.setDepartmentName(manager1
								.getDepartmentName());
						formAvgRatings.setDesignationName(manager1
								.getDesignation());
					} else if (i == 2) {
						// for second manager
						formAvgRatings = new AppraisalFormAvgRatings();
						formAvgRatings.setEmployeeId(manager2.getEmployeeId());
						formAvgRatings.setEmployeeName(manager2.getFullName());
						formAvgRatings.setDepartmentName(manager2
								.getDepartmentName());
						formAvgRatings.setDesignationName(manager2
								.getDesignation());
					}
					formAvgRatings.setiAgree(Boolean.FALSE);
					formAvgRatings.setLevel(i);
					avgRatingses.add(formAvgRatings);
				}
			}
			appraisalForm.setFormAvgRatings(avgRatingses);
			appraisalForm.setRequestDiscussion(Boolean.FALSE);

		}
		return appraisalForm;
	}

	public AppraisalFormListDto toFormList(AppraisalForm appraisalFrmEntity) {
		AppraisalFormListDto appraisalFromDto = new AppraisalFormListDto();
		if (appraisalFrmEntity != null) {
			if (appraisalFrmEntity.getManagesList() != null) {
				String managers[] = appraisalFrmEntity.getManagesList().split(
						",");
				Employee employee = dao.findBy(Employee.class,
						Long.parseLong(managers[0]));
				appraisalFromDto.setManager1Name(employee.getFullName());
			}
			appraisalFromDto.setId(appraisalFrmEntity.getId());
			// appraisalFromDto.setEmployee(employeeBuilder.createAppraisalDTO(appraisalFrmEntity.getEmployee()));
			if (appraisalFrmEntity.getFormStatus().name()
					.equalsIgnoreCase("PENDING")
					|| appraisalFrmEntity.getFormStatus().name()
							.equalsIgnoreCase("SUBMIT")) {
				String managers[] = appraisalFrmEntity.getManagesList().split(
						",");
				Employee employee = dao.findBy(Employee.class,
						Long.parseLong(managers[managers.length - 1]));
				appraisalFromDto.setPendingWith("Pending with "
						+ employee.getFullName());
			}
			if (appraisalFrmEntity.getFormStatus().name()
					.equalsIgnoreCase("SAVE"))
			{
				appraisalFromDto.setManager1Name(appraisalFrmEntity.getEmployee().getManager().getFullName());
				
			}

			if(appraisalFrmEntity.getAppraisalCycle().getAppraisalPeriod().getMinimum()
					.equals(appraisalFrmEntity.getAppraisalCycle().getAppraisalPeriod().getMaximum())) {
				appraisalFromDto.setCycleStartDate(appraisalFrmEntity
						.getAppraisalCycle().getAppraisalPeriod().getMinimum()
						.toString("MMM yyyy"));
			}
			else {
				appraisalFromDto.setCycleStartDate(appraisalFrmEntity.getAppraisalCycle().getAppraisalPeriod().getMinimum().toString("MMM yyyy")
						+ "-" + appraisalFrmEntity.getAppraisalCycle().getAppraisalPeriod().getMaximum().toString("MMM yyyy"));
			}
			
			appraisalFromDto.setFormStatus(appraisalFrmEntity.getFormStatus()
					.toString());
			appraisalFromDto.setFormStatusvalue(appraisalFrmEntity
					.getFormStatus().getValue());
			Double overallAvgRating = 0.0;
			Double overallAvgRatingCount = 0.0;
			if (appraisalFrmEntity.getFormAvgRatings() != null || appraisalFrmEntity.getFormStatus().name().equalsIgnoreCase("SAVE")) {
				Set<AppraisalFormAvgRatings> setOfAvgRatings = appraisalFrmEntity
						.getFormAvgRatings();
				for (AppraisalFormAvgRatings formAvgRatings : setOfAvgRatings) {
					if (formAvgRatings.getDefaultAvgRating() != null
							|| appraisalFrmEntity.getFormStatus().name().equalsIgnoreCase("SAVE")) {
						if (formAvgRatings.getEmployeeId().equals(
								appraisalFrmEntity.getEmployee()
										.getEmployeeId())
								&& (formAvgRatings.getLevel().equals(0))) {
							appraisalFromDto
									.setEmpDepartmentName(formAvgRatings
											.getDepartmentName());
							appraisalFromDto
									.setEmpDesignationName(formAvgRatings
											.getDesignationName());
							appraisalFromDto.setEmpAvgRating(formAvgRatings
									.getDefaultAvgRating());
							appraisalFromDto.setEmpId(formAvgRatings
									.getEmployeeId());
							appraisalFromDto.setEmployeeName(formAvgRatings
									.getEmployeeName());
						} else {
							overallAvgRatingCount++;
							overallAvgRating = overallAvgRating
									+ (formAvgRatings.getAdjustedAvgRating() != null ? formAvgRatings
											.getAdjustedAvgRating()
											: formAvgRatings
													.getDefaultAvgRating()!=null?formAvgRatings
															.getDefaultAvgRating():0.0);
						}
					}
				}
			}
			appraisalFromDto.setOverallAvgRating(overallAvgRating
					/ overallAvgRatingCount);
		}
		return appraisalFromDto;
	}

	public AppraisalFormDto toDto(AppraisalForm appraisalFrmEntity) {
		AppraisalFormDto appraisalFromDto = null;
		if (appraisalFrmEntity != null) {
			appraisalFromDto = new AppraisalFormDto();
			Long loggedInEmpid = securityUtils
					.getLoggedEmployeeIdforSecurityContextHolder();
			if (appraisalFrmEntity.getManagesList() != null) {
				String managers[] = appraisalFrmEntity.getManagesList().split(
						",");
				Employee employee = dao.findBy(Employee.class,
						Long.parseLong(managers[0]));
				appraisalFromDto.setManager1Name(employee.getFullName());
				if ((String.valueOf(loggedInEmpid))
						.equalsIgnoreCase(managers[managers.length - 1])) {
					if (appraisalFrmEntity.getFormStatus().name()
							.equalsIgnoreCase("SUBMIT")
							|| appraisalFrmEntity.getFormStatus().name()
									.equalsIgnoreCase("PENDING")
							|| appraisalFrmEntity.getFormStatus().name()
									.equalsIgnoreCase("OPENFORDISCUSSION")
							|| appraisalFrmEntity.getFormStatus().name()
									.equalsIgnoreCase("PENDINGAGREEMENT")) {
						appraisalFromDto
								.setAppraisalFormStatus("NotSubmittedByYou");
					}
					if (appraisalFrmEntity.getFormStatus().name()
							.equalsIgnoreCase("OPENFORDISCUSSION")
							|| appraisalFrmEntity.getFormStatus().name()
									.equalsIgnoreCase("PENDINGAGREEMENT")) {
						appraisalFromDto
								.setOpenForDiscussionFlag("FinalManager");
					}
				}
			}
			appraisalFromDto.setId(appraisalFrmEntity.getId());
			appraisalFromDto.setEmployee(employeeBuilder
					.createAppraisalDTO(appraisalFrmEntity.getEmployee()));
			appraisalFromDto.setAppraisalCycle(appraisalCycleBuilder
					.toDto(appraisalFrmEntity.getAppraisalCycle()));
			appraisalFromDto.setKra(appraisalKRADataBuilder
					.toDtoSet(appraisalFrmEntity.getKra()));
			if (appraisalFrmEntity.getFormStatus().name()
					.equalsIgnoreCase("PENDING")
					|| appraisalFrmEntity.getFormStatus().name()
							.equalsIgnoreCase("SUBMIT")) {
				String managers[] = appraisalFrmEntity.getManagesList().split(
						",");
				Employee employee = dao.findBy(Employee.class,
						Long.parseLong(managers[managers.length - 1]));
				appraisalFromDto.setPendingWith("Pending with "
						+ employee.getFullName());
			}

			appraisalFromDto.setFormStatus(appraisalFrmEntity.getFormStatus()
					.toString());
			appraisalFromDto.setFormStatusvalue(appraisalFrmEntity
					.getFormStatus().getValue());
			// appraisalFromDto.setFormRating(appraisalFrmEntity.getFormAverageRating());
			Set<AppraisalFormAvgRatingsDto> formAvgRatingsDtos = new HashSet<AppraisalFormAvgRatingsDto>();
			Double overallAvgRating = 0.0;
			Double overallAvgRatingCount = 0.0;
			if (appraisalFrmEntity.getFormAvgRatings() != null) {
				Set<AppraisalFormAvgRatings> setOfAvgRatings = appraisalFrmEntity
						.getFormAvgRatings();
				for (AppraisalFormAvgRatings formAvgRatings : setOfAvgRatings) {
					AppraisalFormAvgRatingsDto avgRatingsDto = new AppraisalFormAvgRatingsDto();
					if (formAvgRatings.getDefaultAvgRating() != null) {

						if (formAvgRatings.getEmployeeId().equals(
								appraisalFrmEntity.getEmployee()
										.getEmployeeId())
								&& (formAvgRatings.getLevel().equals(0))) {
							appraisalFromDto
									.setEmpDepartmentName(formAvgRatings
											.getDepartmentName());
							appraisalFromDto
									.setEmpDesignationName(formAvgRatings
											.getDesignationName());
							appraisalFromDto.setEmpAvgRating(formAvgRatings
									.getDefaultAvgRating());
							// overallAvgRatingCount++;
							// overallAvgRating = overallAvgRating +
							// formAvgRatings.getDefaultAvgRating();
						} else {
							overallAvgRatingCount++;
							overallAvgRating = overallAvgRating
									+ (formAvgRatings.getAdjustedAvgRating() != null ? formAvgRatings
											.getAdjustedAvgRating()
											: formAvgRatings
													.getDefaultAvgRating());
						}
					}
					avgRatingsDto.setId(formAvgRatings.getId());
					avgRatingsDto.setEmployeeId(formAvgRatings.getEmployeeId());
					avgRatingsDto.setEmployeeName(formAvgRatings
							.getEmployeeName());
					avgRatingsDto.setDepartmentName(formAvgRatings
							.getDepartmentName());
					avgRatingsDto.setDesignationName(formAvgRatings
							.getDesignationName());
					avgRatingsDto.setDefaultAvgRating(formAvgRatings
							.getDefaultAvgRating() != null ? formAvgRatings
							.getDefaultAvgRating() : null);
					avgRatingsDto.setAdjustedAvgRating(formAvgRatings
							.getAdjustedAvgRating());
					avgRatingsDto.setFinalFeedback(formAvgRatings
							.getFinalFeedback());
					avgRatingsDto.setLevel(formAvgRatings.getLevel());
					avgRatingsDto.setiAgree(formAvgRatings.getiAgree());
					avgRatingsDto.setDiscussionSummary(formAvgRatings
							.getDiscussionSummary());
					formAvgRatingsDtos.add(avgRatingsDto);
					if (loggedInEmpid.equals(formAvgRatings.getEmployeeId())) {
						if (formAvgRatings.getiAgree())
							appraisalFromDto.setiAgreeFlag(Boolean.TRUE);
					}

				}

			}

			appraisalFromDto.setOverallAvgRating(overallAvgRating
					/ overallAvgRatingCount);
			appraisalFromDto
					.setFinalRating(appraisalFrmEntity.getFinalRAting());
			appraisalFromDto.setDiscussionOn(appraisalFrmEntity
					.getDiscussionOn() != null ? appraisalFrmEntity
					.getDiscussionOn().toString("dd/MM/yyyy") : null);
			// appraisalFromDto.setDiscussionSummary(appraisalFrmEntity.getDiscussionSummary());
			appraisalFromDto.setAvgRatingsDtos(formAvgRatingsDtos);
			appraisalFromDto
					.setClosedOn(appraisalFrmEntity.getClosedOn() != null ? appraisalFrmEntity
							.getClosedOn().toString("dd/MM/yyyy") : null);
			appraisalFromDto.setClosedSummary(appraisalFrmEntity
					.getClosedSummary());
			appraisalFromDto.setRequestDiscussion(appraisalFrmEntity
					.getRequestDiscussion());
			appraisalFromDto.setClosedStatus(appraisalFrmEntity
					.getClosedStatus());
			if (appraisalFrmEntity.getClosedBy() != null) {
				Employee employee = dao.findBy(Employee.class,
						appraisalFrmEntity.getClosedBy());
				appraisalFromDto.setClosedBy(employee.getFullName());

			}
		}
		return appraisalFromDto;
	}

	public List<AppraisalFormDto> toDtoList(
			List<AppraisalForm> appraisalFormEntity) {
		List<AppraisalFormDto> appraisalFormList = new ArrayList<AppraisalFormDto>();
		if (appraisalFormEntity != null) {
			for (AppraisalForm appraisalForm : appraisalFormEntity) {
				appraisalFormList.add(toDto(appraisalForm));
			}
		}
		return appraisalFormList;
	}

	public Set<AppraisalKRAData> buildmanagerFeedBack(
			AppraisalFormDto appraisalFormDto) {
		return appraisalKRADataBuilder.toEntitySet(appraisalFormDto.getKra());
	}

	public AppraisalForm updateEmployeeAppraisalForm(
			AppraisalForm appraisalForm, AppraisalFormDto appraisalFormDto) {
		Set<AppraisalKRAData> kraList = convertKRADTOtoEntity(appraisalFormDto
				.getKra());
		appraisalForm.setKra(kraList);
		appraisalForm.setUpdatedBy(securityUtils
				.getLoggedEmployeeIdforSecurityContextHolder());
		appraisalForm.setUpdatedDate(new Second());

		if (appraisalFormDto.getFormStatus().equalsIgnoreCase("SUBMIT"))
			appraisalForm.setFormAvgRatings(convertFromDtoToAvgRating(
					appraisalForm, appraisalFormDto));
		return appraisalForm;
	}

	// this method is used for converting appraisalformdto to appraisalfrom
	// average rating entity
	public Set<AppraisalFormAvgRatings> convertFromDtoToAvgRating(
			AppraisalForm appraisalForm, AppraisalFormDto appraisalFormDto) {
		Map<String, Object> employeeDetails = securityUtils
				.getLoggedEmployeeDetailsSecurityContextHolder();
		Employee loggedEmployee = (Employee) employeeDetails.get("employee");
		Set<AppraisalFormAvgRatings> avgRatingses = appraisalForm
				.getFormAvgRatings();
		Boolean avgRatingIsExistFlag = false;
		for (AppraisalFormAvgRatings avgRatings : avgRatingses) {
			// average rating id is already exists for loggedin employee
			if (avgRatings.getEmployeeId().equals(
					loggedEmployee.getEmployeeId())) {
				avgRatings.setDefaultAvgRating(defaultAvgRating(
						appraisalFormDto, loggedEmployee.getEmployeeId()));
				avgRatings.setAdjustedAvgRating(appraisalFormDto
						.getAdjustedAvgRating() != null ? (Double
						.parseDouble(appraisalFormDto.getAdjustedAvgRating()))
						: null);

				avgRatings
						.setFinalFeedback(appraisalFormDto.getFinalFeedback());
				avgRatingIsExistFlag = true;
				avgRatingses.add(avgRatings);
				break;
			}

		}
		// if it is new one
		if (!avgRatingIsExistFlag) {
			AppraisalFormAvgRatings newAvgRating = new AppraisalFormAvgRatings();
			newAvgRating.setEmployeeId(loggedEmployee.getEmployeeId());
			newAvgRating.setEmployeeName(loggedEmployee.getFullName());
			newAvgRating.setDepartmentName(loggedEmployee.getDepartmentName());
			newAvgRating.setDesignationName(loggedEmployee.getDesignation());
			newAvgRating.setDefaultAvgRating(defaultAvgRating(appraisalFormDto,
					loggedEmployee.getEmployeeId()));
			if (appraisalFormDto.getAdjustedAvgRating() != null) {
				newAvgRating.setAdjustedAvgRating(Double
						.parseDouble(appraisalFormDto.getAdjustedAvgRating()));
			}
			newAvgRating.setFinalFeedback(appraisalFormDto.getFinalFeedback());
			newAvgRating.setiAgree(Boolean.FALSE);
			if (appraisalForm.getManagesList() != null) {
				String[] managerList = appraisalForm.getManagesList()
						.split(",");
				newAvgRating.setLevel(managerList.length);

			} else
				newAvgRating.setLevel(0);

			avgRatingses.add(newAvgRating);
		}

		return avgRatingses;
	}

	public Double defaultAvgRating(AppraisalFormDto appraisalFormDto,
			Long loggedInEmpId) {

		Double kpiManagerRating = 0.0;
		Double kpiManagerRatingCount = 0.0;
		Double kpiAvgRating = 0.0;
		AppraisalForm appraisalForm = dao.findBy(AppraisalForm.class,
				appraisalFormDto.getId());

		Set<AppraisalKRADataDto> aDataDtos = appraisalFormDto.getKra();

		for (AppraisalKRADataDto a : aDataDtos) {
			Set<AppraisalKPIDataDto> akpidds = a.getKpis();
			for (AppraisalKPIDataDto kPIDataDto : akpidds) {
				// managers are not exists
				if (appraisalForm.getManagesList() != null) {
					kpiManagerRatingCount++;
					String[] managersList = appraisalForm.getManagesList()
							.split(",");

					if (managersList.length == 1
							&& !managersList[0]
									.equals(loggedInEmpId.toString())) {

						if (kPIDataDto.getEmployeeRating() == -1) {
							kpiManagerRatingCount--;

						} else {
							kpiManagerRating = kpiManagerRating
									+ kPIDataDto.getEmployeeRating();
						}
					} else {
						if (kPIDataDto.getManagerRating() == -1) {
							kpiManagerRatingCount--;

						} else {
							kpiManagerRating = kpiManagerRating
									+ kPIDataDto.getManagerRating();
						}
					}
				}

				else {
					kpiManagerRatingCount++;
					if (appraisalForm.getEmployee().getEmployeeId()
							.equals(loggedInEmpId)) {
						if (kPIDataDto.getEmployeeRating() == -1) {
							kpiManagerRatingCount--;

						} else {
							kpiManagerRating = kpiManagerRating
									+ kPIDataDto.getEmployeeRating();
						}
					}

				}
			}
		}
		kpiAvgRating = Math.round(kpiManagerRating / kpiManagerRatingCount
				* 100.0) / 100.0;
		return kpiAvgRating;
	}

	public Set<AppraisalKRAData> convertKRADTOtoEntity(
			Set<AppraisalKRADataDto> kraDTOList) {
		Set<AppraisalKRAData> kraList = new HashSet<AppraisalKRAData>();
		for (AppraisalKRADataDto dto : kraDTOList) {
			AppraisalKRAData kRAData = dao.findBy(AppraisalKRAData.class,
					dto.getId());
			kRAData.setKpis(convertKPIDTOtoEntity(dto.getKpis()));
			kraList.add(kRAData);
		}
		return kraList;
	}

	public Set<AppraisalKPIData> convertKPIDTOtoEntity(
			Set<AppraisalKPIDataDto> kipDTOList) {
		Long loggedInEmpId = securityUtils
				.getLoggedEmployeeIdforSecurityContextHolder();

		Set<AppraisalKPIData> kpiList = new HashSet<AppraisalKPIData>();
		for (AppraisalKPIDataDto appraisalKpiDataDto : kipDTOList) {
			// if the employee rating is not in between 0 to 10 we are throwing
			// error message
			if (appraisalKpiDataDto.getEmployeeRating() != null)
				if (appraisalKpiDataDto.getEmployeeRating() < -1
						|| appraisalKpiDataDto.getEmployeeRating() > 10) {
					throw new InvalidRatingsException(
							"Given rating is invalid please select a rating value between 0 to 10 .");
				}
			AppraisalKPIData kpiData = dao.findBy(AppraisalKPIData.class,
					appraisalKpiDataDto.getId());
			AppraisalForm appraisalForm = dao.findBy(AppraisalForm.class,
					kpiData.getAppraisalKRAData().getAppraisalForm().getId());
			if (appraisalForm.getManagesList() != null) {
				String managersList = appraisalForm.getManagesList();
				String[] managersEmpids = managersList.split(",");

				if ((managersEmpids.length == 1 && managersEmpids[0]
						.equalsIgnoreCase(loggedInEmpId.toString()))
						|| managersEmpids.length > 1) {
					// if the manager rating is not in between 0 to 10 we are
					// throwing error message
					if (appraisalKpiDataDto.getManagerRating() != null)
						if (appraisalKpiDataDto.getManagerRating() < -1
								|| appraisalKpiDataDto.getManagerRating() > 10) {
							throw new InvalidRatingsException(
									"Given rating is invalid please select a rating value between 0 to 10 .");
						}

					Boolean ratingByManager = false;
					// Set<AppraisalKPIData> appraisalKPIDatas = new
					// HashSet<AppraisalKPIData>();
					Set<AppraisalKPIData> appraisalKPIDatas = kpiData
							.getManagersFeedback();
					for (AppraisalKPIData id : appraisalKPIDatas) {
						// If the manager coments are already given we are using
						// this if loop
						if (id.getEmpId().equals(loggedInEmpId)) {
							id.setFeedback(appraisalKpiDataDto
									.getManagerFeedback());
							id.setRating(appraisalKpiDataDto.getManagerRating());
							id.setStatus(appraisalForm.getFormStatus().name());
							ratingByManager = true;
							appraisalKPIDatas.add(id);
							break;

						}
					}
					// If the manager coments new we are using this if loop
					if (!ratingByManager) {
						AppraisalKPIData id = null;
						id = new AppraisalKPIData();
						id.setFeedback(appraisalKpiDataDto.getManagerFeedback());
						id.setRating(appraisalKpiDataDto.getManagerRating());
						id.setLevel(managersEmpids.length);
						id.setName(kpiData.getName());
						id.setStatus(appraisalForm.getFormStatus().name());
						id.setDescription(kpiData.getDescription());
						id.setEmpId(loggedInEmpId);
						appraisalKPIDatas.add(id);

					}
					kpiData.setManagersFeedback(appraisalKPIDatas);

				} else {
					// "employee appraisal status is submit"
					kpiData.setFeedback(appraisalKpiDataDto
							.getEmployeeFeedback());
					kpiData.setRating(appraisalKpiDataDto.getEmployeeRating());
					kpiData.setStatus(appraisalForm.getFormStatus().name());
				}

			} else {
				// "employee appraisal status is save
				kpiData.setFeedback(appraisalKpiDataDto.getEmployeeFeedback());
				kpiData.setRating(appraisalKpiDataDto.getEmployeeRating());
				kpiData.setStatus(appraisalForm.getFormStatus().name());
				kpiData.setEmpId(loggedInEmpId);
				kpiData.setLevel(0);
			}
			kpiList.add(kpiData);
		}
		return kpiList;
	}

	// Appraisal confirmation
	public AppraisalForm conformationAppriasalForm(
			AppraisalFormDto appraisalFormDto) {
		Long loggedInEmpId = securityUtils
				.getLoggedEmployeeIdforSecurityContextHolder();
		AppraisalForm appraisalForm = dao.findBy(AppraisalForm.class,
				appraisalFormDto.getId());
		Set<AppraisalFormAvgRatings> avgRatingses = appraisalForm
				.getFormAvgRatings();

		// if (appraisalFormDto.getFormStatus().equalsIgnoreCase("Completed")) {
		Integer iAgreeCount = 0;
		for (AppraisalFormAvgRatings avgRatings : avgRatingses) {
			if (avgRatings.getEmployeeId().equals(loggedInEmpId)) {
				avgRatings.setiAgree(Boolean.TRUE);
				avgRatings.setDiscussionSummary(appraisalFormDto
						.getDiscussionSummary());
			}
			if (avgRatings.getiAgree()) {
				iAgreeCount++;
			}
		}
		String[] managerList = appraisalForm.getManagesList().split(",");
		if (managerList[managerList.length - 1]
				.equals(loggedInEmpId.toString())) {
			// appraisalForm.setFinalRAting(appraisalFormDto.getFinalRating());
			// appraisalForm.setDiscussionSummary(appraisalFormDto.getDiscussionSummary());
			appraisalForm.setFormStatus(FormStatus.PENDINGAGREEMENT);
			try {
				appraisalForm.setDiscussionOn(Date.parse(
						appraisalFormDto.getDiscussionOn(), "dd/MM/yyyy"));
			} catch (ParseException ex) {
				java.util.logging.Logger.getLogger(
						AppraisalFormBuilder.class.getName()).log(Level.SEVERE,
						null, ex);
			}
		}
		Integer avg = avgRatingses.size();
		// if (avg.equals(iAgreeCount)) {
		appraisalForm.setFormStatus(FormStatus.COMPLETED);
		// }
		// }
		return appraisalForm;
	}
	//for getting employees not submitted appraisal forms
	public AppraisalFormListDto appraisalFormNotSubmitted(Employee employee,AppraisalCycle appraisalMonth) {
		AppraisalFormListDto  appraisalFormListDto=null;
		if(employee!=null)
		{
			appraisalFormListDto =new AppraisalFormListDto();
			appraisalFormListDto.setEmpId(employee.getEmployeeId());
			appraisalFormListDto.setAppraisalFormStatus("NOT-Submitted");
			if(appraisalMonth!=null)
			{
				if(appraisalMonth.getAppraisalPeriod().getMinimum().equals(appraisalMonth.getAppraisalPeriod().getMaximum())) {
					appraisalFormListDto.setCycleStartDate(appraisalMonth.getAppraisalPeriod().getMinimum().toString("MMM yyyy"));
				}
				else {
					appraisalFormListDto.setCycleStartDate(appraisalMonth.getAppraisalPeriod().getMinimum().toString("MMM yyyy")
							+ "-" + appraisalMonth.getAppraisalPeriod().getMaximum().toString("MMM yyyy"));
				}
			
			}
			appraisalFormListDto.setEmployeeName(employee.getEmployeeFullName());
			appraisalFormListDto.setEmpDepartmentName(employee.getDepartmentName());
			appraisalFormListDto.setEmpDesignationName(employee.getDesignation());
			appraisalFormListDto.setManager1Name(employee.getManager().getFullName());
		}
		
		return appraisalFormListDto;
		
	}

	

	public List<AppraisalFormListDto> appraisalFormNotSubmittedList(List<Employee> employees,AppraisalCycle appraisalMonth)
	{
		List<AppraisalFormListDto> formListDto=new ArrayList<AppraisalFormListDto>();
		if(employees!=null)
		{
			for(Employee employee:employees)
			{

				formListDto.add(appraisalFormNotSubmitted(employee,appraisalMonth));
			}
		}
		return formListDto;
	}
	
	//method for export functionality of employees not submitted appraisal forms 
		public List<AppraisalForm> exportappraisalFormsList(List<Employee> employees){
			List<AppraisalForm> appraisalForms = null;
			if(employees != null) {
				appraisalForms = new ArrayList<AppraisalForm>();
				for(Employee employee2:employees) {
					appraisalForms.add(exportappraisalForm(employee2));
				}
				
			}
			
			return  appraisalForms;
		}
		
		public AppraisalForm exportappraisalForm(Employee employee) {
			AppraisalForm appraisalForm = null;
			if(employee!=null) {
				appraisalForm = new AppraisalForm();
				appraisalForm.setEmployee(employee);
				AppraisalCycle appraisalCycle=appraisalDaoImpl.getcurrentYearAppraisalCycle();
				appraisalForm.setAppraisalCycle(appraisalCycle);
						
			}
			return appraisalForm;
			
		}
	
	


}
