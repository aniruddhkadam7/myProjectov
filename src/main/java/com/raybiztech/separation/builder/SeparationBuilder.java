package com.raybiztech.separation.builder;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.dao.DAO;
import com.raybiztech.appraisals.security.utils.SecurityUtils;
import com.raybiztech.date.Date;
import com.raybiztech.date.DayOfMonth;
import com.raybiztech.date.MonthOfYear;
import com.raybiztech.date.Second;
import com.raybiztech.date.YearOfEra;
import com.raybiztech.leavemanagement.business.algorithm.DateParser;
import com.raybiztech.rolefeature.business.Permission;
import com.raybiztech.separation.business.ClearanceCertificate;
import com.raybiztech.separation.business.ExitFeedBack;
import com.raybiztech.separation.business.PrimaryReason;
import com.raybiztech.separation.business.Separation;
import com.raybiztech.separation.business.SeparationComments;
import com.raybiztech.separation.business.SeparationStatus;
import com.raybiztech.separation.chart.ChartDetails;
import com.raybiztech.separation.chart.SeparationChart;
import com.raybiztech.separation.chart.SeparationData;
import com.raybiztech.separation.dto.ExitFeedBackDTO;
import com.raybiztech.separation.dto.PrimaryReasonDTO;
import com.raybiztech.separation.dto.SeparationDTO;

@Component("separationBuilder")
public class SeparationBuilder {
	@Autowired
	DAO dao;
	@Autowired
	ClearanceCertificateBuilder clearanceCertificateBuilder;
	@Autowired
	SeparationCommentsBuilder separationCommentsBuilder;
	@Autowired
	SecurityUtils securityUtils;

	Logger logger = Logger.getLogger(SeparationComments.class);

	public Separation toAddEntity(SeparationDTO separationDTO) {

		Separation separation = null;
		if (separationDTO != null) {
			separation = new Separation();
			Employee employee = dao.findBy(Employee.class, separationDTO.getEmployeeId());
			separation.setEmployee(employee);

			separation.setPrimaryReason(dao.findBy(PrimaryReason.class, separationDTO.getPrimaryReasonId()));
			separation.setResignationDate(stringToDate(separationDTO.getResignationDate()));
			separation.setEmployeeComments(separationDTO.getEmployeeComments());
			separation.setIsRevoked(Boolean.FALSE);
			separation.setIsprocessInitiated(Boolean.FALSE);

			separation.setRelievingDate(new Date(DayOfMonth.valueOf(new Date().getDayOfMonth().getValue() + 90),
					MonthOfYear.valueOf(new Date().getMonthOfYear().getValue()),
					YearOfEra.valueOf(new Date().getYearOfEra().getValue())));

			separation.setReasonComments(separationDTO.getReasonComments());

			separation.setStatus(SeparationStatus.SUBMITRESIGNATION);
			separation.setCreatedDate(new Second());

			Set<SeparationComments> separationComments = new HashSet<SeparationComments>();

			SeparationComments comments = new SeparationComments();
			comments.setEmployee(employee);
			comments.setStatus(SeparationStatus.SUBMITRESIGNATION);
			comments.setComments(separationDTO.getEmployeeComments());
			comments.setCreatedDate(new Second());
			separationComments.add(comments);

			separation.setComments(separationComments);
		

		}
		return separation;

	}

	//
	public Separation toEditEntity(SeparationDTO separationDTO, Employee loggedemployee) {

		Separation separation = dao.findBy(Separation.class, separationDTO.getSeparationId());

		SeparationStatus separationStatus = this.getSeprationStatus(separation, loggedemployee);

		separation.setStatus(separationStatus);

		SeparationComments newcomments = new SeparationComments();
		newcomments.setEmployee(loggedemployee);
		newcomments.setComments(separationDTO.getManagerComments());
		newcomments.setWithdrawComments(separationDTO.getWithdrawComments());
		newcomments.setStatus(separationStatus);
		newcomments.setCreatedDate(new Second());

		separation.getComments().add(newcomments);

		return separation;
	}

	public Separation toEntity(SeparationDTO separationdto) {

		Separation separation = null;
		if (separationdto != null) {
			separation = new Separation();
			separation.setEmployee(dao.findBy(Employee.class, separationdto.getEmployeeId()));
			separation.setPrimaryReason(dao.findBy(PrimaryReason.class, separationdto.getPrimaryReasonId()));
			// separation.setCertificate(clearanceCertificateBuilder.toEntity(separationdto.getCertificate()));
			separation.setStatus(SeparationStatus.valueOf(separationdto.getStatus()));

			separation.setRelievingDate(stringToDate(separationdto.getRelievingDate()));

			separation.setResignationDate(stringToDate(separationdto.getResignationDate()));

			if (separationdto.getReasonComments() != null) {
				separation.setReasonComments(separationdto.getReasonComments());
			}

		}
		return separation;
	}

	public SeparationDTO toDto(Separation separation) {
		SeparationDTO separationDto = null;
		if (separation != null) {
			separationDto = new SeparationDTO();
			Employee employee = separation.getEmployee();
			separationDto.setEmployeeId(employee != null ? employee.getEmployeeId() : null);
			separationDto.setEmployeeName(employee != null ? employee.getFullName() : "N/A");
			PrimaryReason primary = separation.getPrimaryReason();
			separationDto.setPrimaryReasonName(primary != null ? primary.getReasonName() : "N/A");
			separationDto.setResignationDate(separation.getResignationDate().toString("dd/MM/yyyy"));
			separationDto.setRelievingDate(separation.getRelievingDate().toString("dd/MM/yyyy"));
			separationDto.setReasonComments(separation.getReasonComments());
			separationDto.setStatus(separation.getStatus().getSeperationStatus());
			separationDto.setSeparationId(separation.getSeparationId());
			separationDto.setEmployeeComments(separation.getEmployeeComments());

			separationDto.setSeperationComments(separationCommentsBuilder.toDTOSET(separation.getComments()));

			Employee loggedEmployee = (Employee) securityUtils.getLoggedEmployeeDetailsSecurityContextHolder()
					.get("employee");

			separationDto
					.setCanberevoked((loggedEmployee.equals(separation.getEmployee()) && !separation.getIsRevoked())
							? Boolean.TRUE : Boolean.FALSE);

			separationDto.setIsRevoked(separation.getIsRevoked());

			separationDto
					.setCertificateDTO(clearanceCertificateBuilder.convertEntityToDTO(separation.getCertificate()));

			separationDto.setShowCommentsBox(loggedEmployee.equals(separation.getEmployee())
					|| separation.getIsRevoked() || separation.getStatus().getSeperationStatus() == "Relieved"
							? Boolean.FALSE : Boolean.TRUE);
			separationDto.setShowEditButton(
					SeparationStatus.RELIEVED.equals(separation.getStatus()) ? Boolean.FALSE : Boolean.TRUE);
		}

		return separationDto;

	}

	public List<SeparationDTO> toDtoList(List<Separation> separationlist) {
		List<SeparationDTO> separationDto = null;
		if (separationlist != null) {
			separationDto = new ArrayList<SeparationDTO>();
			for (Separation separation : separationlist) {
				separationDto.add(toResignationGridList(separation));
			}
		}
		return separationDto;
	}

	public PrimaryReason DtoToEntity(PrimaryReasonDTO reasonDto) {
		PrimaryReason reason = null;
		if (reasonDto != null) {
			reason = new PrimaryReason();
			reason.setReasonId(reasonDto.getReasonId());
			reason.setReasonName(reasonDto.getReasonName());
		}
		return reason;
	}

	public PrimaryReasonDTO EntityToDto(PrimaryReason reason) {
		PrimaryReasonDTO reasonDto = null;
		if (reason != null) {
			reasonDto = new PrimaryReasonDTO();
			reasonDto.setReasonId(reason.getReasonId());
			reasonDto.setReasonName(reason.getReasonName());
		}
		return reasonDto;
	}

	public List<PrimaryReasonDTO> toDto(List<PrimaryReason> reason) {
		List<PrimaryReasonDTO> reasonDto = null;
		if (reason != null) {
			reasonDto = new ArrayList<PrimaryReasonDTO>();
			for (PrimaryReason reasonList : reason) {
				reasonDto.add(EntityToDto(reasonList));
			}
		}
		return reasonDto;
	}

	public Date stringToDate(String date) {

		Date date2 = null;
		try {
			date2 = DateParser.toDate(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date2;
	}

	public String dateToString(Date date) {
		String stringDate = null;
		stringDate = DateParser.toString(date);
		return stringDate;
	}

	// updating the status by checking conditions

	public SeparationStatus getSeprationStatus(Separation separation, Employee loggedEmployee) {

		List<String> roles = new ArrayList<String>(Arrays.asList("Delivery Manager", "HR Manager", "HR", "admin"));

		Employee separationEmployee = separation.getEmployee();

		Employee separationEmployeemanager = dao.findBy(Employee.class,
				separationEmployee.getManager().getEmployeeId());

		SeparationStatus separationStatus = null;
		if (checkCommentsAlreadyGivenByEmployee(loggedEmployee, separation)) {
			separationStatus = separation.getStatus();
		} else if (separation.getStatus().equals(SeparationStatus.UNDERNOTICE)) {
			separationStatus = separation.getStatus();
		} else if (roles.contains(loggedEmployee.getRole())) {
			separationStatus = SeparationStatus.UNDERNOTICE;
		} else if (loggedEmployee.equals(separationEmployeemanager)) {
			separationStatus = SeparationStatus.DISCUSSIONWITHREPORTINGMANAGER;
		} else {
			separationStatus = separation.getStatus();
		}
		return separationStatus;

	}

	public Boolean checkCommentsAlreadyGivenByEmployee(Employee employee, Separation separation) {

		for (SeparationComments comments : separation.getComments()) {
			if (comments.getEmployee().equals(employee)) {
				return Boolean.TRUE;
			}
		}
		return Boolean.FALSE;
	}

	public ExitFeedBack toExitFeedBackEntity(ExitFeedBackDTO exitFeedBackDTO) {
		ExitFeedBack exitFeedBack = new ExitFeedBack();
		exitFeedBack.setDislikeAboutCompany(exitFeedBackDTO.getDislikeAboutCompany());
		exitFeedBack.setEducationalBackground(exitFeedBackDTO.getEducationalBackground());
		exitFeedBack.setEmployee(dao.findBy(Employee.class, exitFeedBackDTO.getEmployeeId()));
		exitFeedBack.setExpectations(exitFeedBackDTO.getExpectations());
		exitFeedBack.setExpectationsFulfilled(exitFeedBackDTO.getExpectationsFulfilled());
		exitFeedBack.setJoinLater(exitFeedBackDTO.getJoinLater());
		exitFeedBack.setLikeAboutCompany(exitFeedBackDTO.getLikeAboutCompany());
		exitFeedBack.setOpportunityForGrowth(exitFeedBackDTO.getOpportunityForGrowth());
		exitFeedBack.setOrganisationCulture(exitFeedBackDTO.getOrganisationCulture());
		exitFeedBack.setOtherCommnets(exitFeedBackDTO.getOtherCommnets());
		exitFeedBack.setPersonelPolicies(exitFeedBackDTO.getPersonelPolicies());
		exitFeedBack.setPrimaryReason(dao.findBy(PrimaryReason.class, exitFeedBackDTO.getPrimaryReasonId()));
		exitFeedBack.setPromotion(exitFeedBackDTO.getPromotion());
		exitFeedBack.setRecognitionOfwork(exitFeedBackDTO.getRecognitionOfwork());
		exitFeedBack.setRoleClarity(exitFeedBackDTO.getRoleClarity());
		exitFeedBack.setSalary(exitFeedBackDTO.getSalary());
		exitFeedBack.setSeparation(dao.findBy(Separation.class, exitFeedBackDTO.getSeparationId()));
		exitFeedBack.setSuperiorGuidance(exitFeedBackDTO.getSuperiorGuidance());
		exitFeedBack.setIsPIP(false);

		return exitFeedBack;

	}

	public ExitFeedBackDTO toExitFeedBackDTO(ExitFeedBack exitFeedBack) {
		ExitFeedBackDTO exitFeedBackDTO = new ExitFeedBackDTO();
		if (exitFeedBack != null) {

			exitFeedBackDTO.setDislikeAboutCompany(
					exitFeedBack.getDislikeAboutCompany() == null ? "N/A" : exitFeedBack.getDislikeAboutCompany());
			exitFeedBackDTO.setEducationalBackground(
					exitFeedBack.getEducationalBackground() == null ? "N/A" : exitFeedBack.getEducationalBackground());
			exitFeedBackDTO.setEmployeeId(exitFeedBack.getEmployee().getEmployeeId());
			exitFeedBackDTO.setEmployeeName(exitFeedBack.getEmployee().getEmployeeFullName());
			exitFeedBackDTO.setExitFeedBackId(exitFeedBack.getId());
			exitFeedBackDTO
					.setExpectations(exitFeedBack.getExpectations() == null ? "N/A" : exitFeedBack.getExpectations());
			exitFeedBackDTO.setJoinLater(exitFeedBack.getJoinLater() == null ? "N/A" : exitFeedBack.getJoinLater());
			exitFeedBackDTO.setLikeAboutCompany(
					exitFeedBack.getLikeAboutCompany() == null ? "N/A" : exitFeedBack.getLikeAboutCompany());
			exitFeedBackDTO.setOpportunityForGrowth(
					exitFeedBack.getOpportunityForGrowth() == null ? "N/A" : exitFeedBack.getOpportunityForGrowth());
			exitFeedBackDTO.setOrganisationCulture(
					exitFeedBack.getOrganisationCulture() == null ? "N/A" : exitFeedBack.getOrganisationCulture());
			exitFeedBackDTO.setOtherCommnets(
					exitFeedBack.getOtherCommnets() == null ? "N/A" : exitFeedBack.getOtherCommnets());
			exitFeedBackDTO.setPersonelPolicies(
					exitFeedBack.getPersonelPolicies() == null ? "N/A" : exitFeedBack.getPersonelPolicies());
			exitFeedBackDTO.setPrimaryReasonId(exitFeedBack.getPrimaryReason().getReasonId());
			exitFeedBackDTO.setPrimaryReasonName(exitFeedBack.getPrimaryReason().getReasonName());
			exitFeedBackDTO.setPromotion(exitFeedBack.getPromotion() == null ? "N/A" : exitFeedBack.getPromotion());
			exitFeedBackDTO.setRecognitionOfwork(
					exitFeedBack.getRecognitionOfwork() == null ? "N/A" : exitFeedBack.getRecognitionOfwork());
			exitFeedBackDTO
					.setRoleClarity(exitFeedBack.getRoleClarity() == null ? "N/A" : exitFeedBack.getRoleClarity());
			exitFeedBackDTO.setSalary(exitFeedBack.getSalary() == null ? "N/A" : exitFeedBack.getSalary());
			exitFeedBackDTO.setSeparationId(exitFeedBack.getSeparation().getSeparationId());
			exitFeedBackDTO.setSuperiorGuidance(
					exitFeedBack.getSuperiorGuidance() == null ? "N/A" : exitFeedBack.getSuperiorGuidance());
			exitFeedBackDTO.setExpectationsFulfilled(
					exitFeedBack.getExpectationsFulfilled() == null ? "N/A" : exitFeedBack.getExpectationsFulfilled());
		}

		return exitFeedBackDTO;
	}

	public SeparationDTO toResignationGridList(Separation separation) {

		List<String> roles = new ArrayList<String>(Arrays.asList("HR Manager", "HR", "admin"));

		Employee loggedemployee = (Employee) securityUtils.getLoggedEmployeeDetailsSecurityContextHolder()
				.get("employee");

		SeparationDTO separationDto = null;
		if (separation != null) {
			separationDto = new SeparationDTO();
			Employee employee = separation.getEmployee();
			separationDto.setEmployeeId(employee != null ? employee.getEmployeeId() : null);
			separationDto.setEmployeeName(employee != null ? employee.getFullName() : "N/A");
			PrimaryReason primary = separation.getPrimaryReason();
			separationDto.setPrimaryReasonName(primary != null ? primary.getReasonName() : "N/A");
			separationDto.setResignationDate(separation.getResignationDate().toString("dd/MM/yyyy"));
			separationDto.setRelievingDate(separation.getRelievingDate().toString("dd/MM/yyyy"));
			separationDto.setInitiatedDate
			(separation.getInitiatedDate() != null ? separation.getInitiatedDate().toString("dd/MM/yyyy") : null );
			separationDto.setStatus(separation.getStatus().getSeperationStatus());
			separationDto.setEmpStatus(employee.getStatusName()!=null?employee.getStatusName():null);
			/*if(separation.getStatus().equals(SeparationStatus.ABSCOND))
			{
				separationDto.setIsAbsconded(true);
			}*/
			
			Employee manager = dao.findBy(Employee.class, employee.getManager().getEmployeeId());

			String managerName = manager.getFullName();
			separationDto.setManagerName(managerName);

			separationDto.setSeparationId(separation.getSeparationId());
			separationDto.setIsprocessInitiated(separation.getIsprocessInitiated());

			Permission ccPermission = dao.checkForPermission("Manager Cleranace", loggedemployee);

			/*
			 * ccPermission.getView() &&
			 * manager.equals(loggedemployee)?Boolean.TRUE:Boolean.FALSE;
			 */
			// logger.warn("ccPermission" + ccPermission.getView());

			/*
			 * Boolean value = (ccPermission != null) ?
			 * ((manager.equals(loggedemployee) ||
			 * loggedemployee.getRole().equalsIgnoreCase("admin")) &&
			 * ccPermission.getView() ||
			 * loggedemployee.getRole().equalsIgnoreCase("HR Manager") ||
			 * loggedemployee.getRole().equalsIgnoreCase("HR")) ? Boolean.TRUE :
			 * Boolean.FALSE : Boolean.FALSE;
			 */

			Boolean value = (ccPermission != null)
					? ((manager.equals(loggedemployee) || roles.contains(loggedemployee.getRole()))
							&& ccPermission.getView()) ? Boolean.TRUE : Boolean.FALSE
					: Boolean.FALSE;
			separationDto.setShowManagerClearance(value);

			Permission timelinePermission = dao.checkForPermission("ShowTimeLine", loggedemployee);

			/*
			 * Boolean timeline = (timelinePermission != null) ?
			 * ((manager.equals(loggedemployee) ||
			 * loggedemployee.getRole().equalsIgnoreCase("admin") ||
			 * loggedemployee.getRole().equalsIgnoreCase("Delivery Manager") ||
			 * loggedemployee.getRole().equalsIgnoreCase("HR Manager") ||
			 * loggedemployee.getRole().equalsIgnoreCase("HR")) &&
			 * timelinePermission.getView()) ? Boolean.TRUE : Boolean.FALSE :
			 * Boolean.FALSE;
			 */
			Boolean timeline = (timelinePermission != null)
					? ((manager.equals(loggedemployee) || loggedemployee.getRole().equalsIgnoreCase("Delivery Manager")
							|| roles.contains(loggedemployee.getRole())) && timelinePermission.getView()) ? Boolean.TRUE
									: Boolean.FALSE
					: Boolean.FALSE;

			separationDto.setShowTimeline(timeline);

			Map<String, Boolean> map = this.getClearanceCss(separation);

			Set<String> set = map.keySet();

			separationDto.setManagerCcCss(set.contains("Manager")
					? (map.get("Manager") ? "btn btn-danger" : "btn btn-success") : "btn btn-primary");

			separationDto.setAdminCcCss(set.contains("Admin")
					? (map.get("Admin") ? "btn btn-danger" : "btn btn-success") : "btn btn-primary");

			separationDto.setFinanaceCcCss(set.contains("Finance")
					? (map.get("Finance") ? "btn btn-danger" : "btn btn-success") : "btn btn-primary");

			separationDto.setItCcCss(
					set.contains("IT") ? (map.get("IT") ? "btn btn-danger" : "btn btn-success") : "btn btn-primary");

			separationDto.setHrCcCss(
					set.contains("HR") ? (map.get("HR") ? "btn btn-danger" : "btn btn-success") : "btn btn-primary");
			
			//logger.warn("personal email "+employee.getPersonalEmail());
			
			if(employee.getPersonalEmail() != null){
				separationDto.setPersonalEmailFlag(Boolean.TRUE);
			}else{
				separationDto.setPersonalEmailFlag(Boolean.FALSE);
			}
			

		}

		return separationDto;
	}

	Map<String, Boolean> getClearanceCss(Separation separation) {

		// List<String> list = new ArrayList<String>();
		Map<String, Boolean> map = new HashMap<String, Boolean>();

		for (ClearanceCertificate clearanceCertificate : separation.getCertificate()) {
			// list.add(clearanceCertificate.getAddedBy());
			map.put(clearanceCertificate.getAddedBy(), clearanceCertificate.getIsDue());
		}

		return map;
	}

	public SeparationChart buildChart(Map<String, Object> map, Date fromDate, Date toDate) {

		SeparationChart separationchart = new SeparationChart();

		ChartDetails chart = new ChartDetails();
		chart.setCaption("Resignation Primary Reasons - Chart ");
		if(fromDate!=null && toDate!=null){
		chart.setSubcaption("From " + fromDate.toString("dd/MM/yyyy") + " To " + toDate.toString("dd/MM/yyyy"));
		}
		chart.setEnablemultislicing("0");
		chart.setPlottooltext("Reason : $label count : $datavalue");
		chart.setShowlabels("0");
		chart.setShowlegend("1");
		chart.setShowpercentvalues("1");
		chart.setShowpercentintooltip("1");
		chart.setSlicingdistance("15");
		chart.setStartingangle("150");
		chart.setTheme("fint");

		List<SeparationData> separationDataList = new ArrayList<SeparationData>();
		for (Entry<String, Object> entry : map.entrySet()) {
			SeparationData data = new SeparationData();
			data.setLabel(entry.getKey());
			data.setValue(entry.getValue().toString());
			separationDataList.add(data);
		}

		separationchart.setChart(chart);
		separationchart.setData(separationDataList);

		return separationchart;
	}

	
}
