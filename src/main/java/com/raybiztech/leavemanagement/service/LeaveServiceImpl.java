package com.raybiztech.leavemanagement.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.raybiztech.appraisals.alerts.builder.AlertBuilder;
import com.raybiztech.appraisals.alerts.business.Alert;
import com.raybiztech.appraisals.builder.EmployeeBuilder;
import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.dao.DAO;
import com.raybiztech.appraisals.properties.PropBean;
import com.raybiztech.appraisals.security.utils.SecurityUtils;
import com.raybiztech.businesscalendar.BusinessCalendar;
import com.raybiztech.date.Date;
import com.raybiztech.date.DateRange;
import com.raybiztech.date.DayOfMonth;
import com.raybiztech.date.Duration;
import com.raybiztech.date.MonthOfYear;
import com.raybiztech.date.MonthYear;
import com.raybiztech.date.TimeUnit;
import com.raybiztech.date.YearOfEra;
import com.raybiztech.leavemanagement.builder.LeaveApplicationBuilder;
import com.raybiztech.leavemanagement.builder.LeaveBuilder;
import com.raybiztech.leavemanagement.builder.LeaveCategoryBuilder;
import com.raybiztech.leavemanagement.builder.LeaveSummaryBuilder;
import com.raybiztech.leavemanagement.business.CarryForwardLeave;
import com.raybiztech.leavemanagement.business.LeaveDebit;
import com.raybiztech.leavemanagement.business.LeaveSettingsLookup;
import com.raybiztech.leavemanagement.business.LeaveStatus;
import com.raybiztech.leavemanagement.business.LeaveType;
import com.raybiztech.leavemanagement.business.PayrollCutOffDates;
import com.raybiztech.leavemanagement.business.ProbationPeriod;
import com.raybiztech.leavemanagement.business.algorithm.DateParser;
import com.raybiztech.leavemanagement.business.algorithm.ProcessAlgorithm;
import com.raybiztech.leavemanagement.dao.LeaveDAO;
import com.raybiztech.leavemanagement.dto.EmpLeaveDTO;
import com.raybiztech.leavemanagement.dto.LeaveApplicationDTO;
import com.raybiztech.leavemanagement.dto.LeaveDTO;
import com.raybiztech.leavemanagement.dto.LeaveSummaryDTO;
import com.raybiztech.leavemanagement.dto.SearchCriteriaDTO;
import com.raybiztech.leavemanagement.exceptions.EmployeeNotFoundException;
import com.raybiztech.leavemanagement.exceptions.LeaveAlreadyAppliedException;
import com.raybiztech.leavemanagement.exceptions.LeaveNotFoundException;
import com.raybiztech.leavemanagement.exceptions.LeaveUpdationCannotProcess;
import com.raybiztech.leavemanagement.exceptions.MailCantSendException;
import com.raybiztech.leavemanagement.exceptions.NotEnoughLeavesAvaialableException;
import com.raybiztech.leavemanagement.exceptions.ProbationException;
import com.raybiztech.leavemanagement.exceptions.UnderNoticeException;
import com.raybiztech.leavemanagement.mailNotification.LeaveApplicationAcknowledgement;
import com.raybiztech.leavemanagement.utils.EmployeeUtils;
import com.raybiztech.leavemanagement.utils.LeaveManagementUtils;
import com.raybiztech.mail.sender.MessageSender;
import com.raybiztech.mailtemplates.util.LeaveManagementMailConfiguration;
import com.raybiztech.projectmanagement.service.ProjectService;
import com.raybiztech.recruitment.business.Department;
import com.raybiztech.recruitment.business.Gender;
import com.raybiztech.recruitment.dto.GenderDTO;
import com.raybiztech.rolefeature.business.Permission;
import com.raybiztech.separation.dao.SeparationDao;

/**
 * @author bargavakumar
 *
 */
@Service("leaveService")
public class LeaveServiceImpl implements LeaveService {

	@Autowired
	LeaveApplicationBuilder leaveApplicationLeaveBuilder;
	@Autowired
	LeaveDAO leaveDAO;
	@Autowired
	LeaveApplicationAcknowledgement leaveApplicationAcknowledgement;
	@Autowired
	LeaveBuilder leaveBuilder;
	@Autowired
	EmployeeBuilder employeeBuilder;
	@Autowired
	LeaveSummaryBuilder leaveSummaryBuilder;
	@Autowired
	LeaveCategoryBuilder leaveCategoryBuilder;
	@Autowired
	LeaveManagementUtils leaveManagementUtils;
	@Autowired
	EmployeeUtils employeeUtils;
	@Autowired
	ProcessAlgorithm processAlgorithm;
	@Autowired
	AlertBuilder alertBuilder;
	@Autowired
	DAO dao;
	@Autowired
	ProjectService projectService;
	@Autowired
	LeaveManagementMailConfiguration leaveManagementMailConfiguration;
	@Autowired
	SecurityUtils securityUtils;

	@Autowired
	MessageSender messageSender;

	@Autowired
	PropBean propBean;

	@Autowired
	SeparationDao separationDaoImpl;

	public static final Logger logger = Logger
			.getLogger(LeaveServiceImpl.class);

	@Transactional
	@Override
	public void applyLeave(LeaveApplicationDTO leaveDTO) {
		long id = leaveDTO.getEmployeeId();
		// System.out.println("id:"+id);
		Employee empId = dao.findBy(Employee.class, id);
		// System.out.println("condition:"+!empId.getStatusName().equalsIgnoreCase("underNotice"));
		if (empId.getStatusName().equalsIgnoreCase("Active")
				&& !empId.isUnderNotice()) {

			LeaveDebit leaveDebit = new LeaveApplicationBuilder(leaveDAO)
					.createLeaveEntity(leaveDTO);

			BusinessCalendar employeeBusinessCalendar = leaveDAO.findByName(
					Department.class,
					leaveDebit.getEmployee().getDepartmentName())
					.getBusinessCalendar();

			// System.out.println("employee business calendar "+employeeBusinessCalendar);

			leaveDebit.setNumberOfDays(leaveManagementUtils.appliedDays(
					leaveDebit, employeeBusinessCalendar));

			logger.warn("no of days"
					+ leaveManagementUtils.appliedDays(leaveDebit,
							employeeBusinessCalendar));

			List<LeaveDebit> list = leaveDAO.numberOfTimesApplied(leaveDebit);
			if (list.size() >= 1) {
				throw new LeaveAlreadyAppliedException(
						"Leave already applied on mentioned period.");
			}

			DateRange presentDateRange = new DateRange(leaveManagementUtils
					.getCurrentFinancialPeriod().getMinimum(), new Date());

			logger.warn("present date range" + presentDateRange);
			// Calculating carryForwardedLeaves 
			Double carryForwardedLeaves = leaveDAO
					.getEmployeeCarryForwardedLeaves(leaveDebit.getEmployee(),
							presentDateRange);
			logger.warn("carryForwardLeaves" + carryForwardedLeaves);

			if (carryForwardedLeaves == null) {
				carryForwardedLeaves = 0.0;
			}
			// For a employee in probation during the financial year cron his /
			// her leaves are calculated
			// as below.
			Date joiningDate = empId.getJoiningDate();
			
			// removed below condition as employees who are in probation period
	        // should get leaves monthly.
	        // Therir is no probation period condition we removed below condition . it is applied from jan2019.
	        // if we need probation condition then uncomment below code.

			/*Date probationPeriodEndDate = joiningDate.shift(

			new Duration(TimeUnit.MONTH, leaveDAO.getLeaveSettings()
					.getProbationPeriod()));
			DateRange joinProbRange = new DateRange(joiningDate,
					probationPeriodEndDate);

			
			  if(joinProbRange.contains(presentDateRange.getMinimum())){
			 carryForwardedLeaves = 0.0; }*/
			 

			Double creditedLeaves = 0d;
			
			// Here calculating Credited leave by calling getCreditedLeaves(-,-) method.
			creditedLeaves = leaveManagementUtils.getCreditedLeaves(
					empId.getEmployeeId(), presentDateRange);
			
			// Adding both carryForwardedLeaves + creditedLeaves = totalCreditedLeaves
			Double totalCreditedLeaves = carryForwardedLeaves + creditedLeaves;

			logger.warn("creditedLeaves is :" + creditedLeaves);

			logger.warn("carryForwardedLeaves is :" + carryForwardedLeaves);
			// logger.warn("creditedLeaves is :" + creditedLeaves);

			logger.warn("totalCreditedLeaves is :" + totalCreditedLeaves);

			Double debitedLeaves = leaveDAO.getAllDebitedLeaves(
					leaveDebit.getEmployee(),
					leaveManagementUtils.getCurrentFinancialPeriod());

			if (debitedLeaves == null) {
				debitedLeaves = 0.0;
			}

			Integer maxAccuraral = leaveDAO.getLeaveSettings()
					.getMaxAccrualPerYear();
			
			Boolean lastMonthLeavesflag = this.checkLeavesForPreviousMonth(
					leaveDebit, totalCreditedLeaves - debitedLeaves);
			
			Date leaveConditionDate = new Date(DayOfMonth.valueOf(16),
					MonthOfYear.valueOf(0), YearOfEra.valueOf(2019));
			String from = leaveDTO.getFromDate();
			Date parsedFromDate = null;
			try {
				parsedFromDate = DateParser.toDate(from);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			Boolean probationFlag = false;

			logger.warn("parsedFromDate" + parsedFromDate);
			logger.warn("leaveConditionDate" + leaveConditionDate);

			LeaveSettingsLookup leaveSettings = leaveDAO.getLeaveSettings();

			logger.warn("aug condition"
					+ employeeUtils.isInProbationPeriod(empId, leaveSettings));

			/*
			 * SortedSet<LeaveCategoryDTO> leaveCategories = null; if
			 * (employeeUtils.isInProbationPeriod(empId, leaveSettings)) {
			 * 
			 * leaveCategories = getlopCategories(); } else {
			 * 
			 * leaveCategories = getAllLeaveCategories(); }
			 */

			// below functionality is for employees who are probation period
			// will apply the leave after 15th january 2019.
			// if employee is in probation period he is tring to apply leave
			// before 16th january 2019 exception will be thrown.

			if (employeeUtils.isInProbationPeriod(empId, leaveSettings)) {
				logger.warn("agust condition for day"
						+ (parsedFromDate.equals(leaveConditionDate) || parsedFromDate
								.isAfter(leaveConditionDate)));
				if (parsedFromDate.equals(leaveConditionDate)
						|| parsedFromDate.isAfter(leaveConditionDate)) {
					probationFlag = true;
				} else {
					probationFlag = false;
					throw new ProbationException(
							"You are in probation period so you can apply leave after 15 January 2019");
				}

				logger.warn("probationFlag" + probationFlag);

			}

			// logger.warn("flag:"+lastMonthLeavesflag);

			if (lastMonthLeavesflag == false || probationFlag == true) {

				logger.warn("in process");

				leaveDAO.save(processAlgorithm.processLeave(leaveDebit,
						totalCreditedLeaves - debitedLeaves, maxAccuraral));

				leaveManagementMailConfiguration
						.sendApplyMailNotification(leaveDebit);

				/*
				 * try { leaveApplicationAcknowledgement
				 * .sendLeaveApplicationAcknowledgement(leaveDebit); } catch
				 * (MailCantSendException e) {
				 * logger.error("Mail cannot be send"); }
				 */

				// send Alert to manager for Leave Approval.
				Alert alert = alertBuilder.createApplayLeaveAlert(leaveDebit);
				leaveDAO.save(alert);
			}
		} else {
			throw new UnderNoticeException(
					"You are in UnderNotice so you can't apply leave");
		}
	}

	@Transactional
	@Override
	public void approveLeave(Long id, String adminComments) {

		Map<String, Object> employeeDetailMap = securityUtils
				.getLoggedEmployeeDetailsSecurityContextHolder();

		Employee loggedEmployee = (Employee) employeeDetailMap.get("employee");

		/*
		 * LeaveDebit leaveId = dao.findBy(LeaveDebit.class, id);
		 * 
		 * 
		 * if(leaveId.getEmployee().getProjectManager() != null &&
		 * !leaveId.getEmployee
		 * ().getProjectManager().getEmployeeId().equals(loggedEmployee
		 * .getEmployeeId()) ){ logger.warn("exception"); throw new
		 * LeaveCannotApprovedException("ProjectManager exits"); }
		 */

		LeaveDebit leave = null;
		try {
			leave = leaveDAO.findBy(LeaveDebit.class, id);
			if (leave.getVersion() >= 1) {
				throw new LeaveUpdationCannotProcess("Leave cannot be Approved");
			}
			leave.setManagerComments(adminComments);
			leave.setStatus(LeaveStatus.Approved);
			logger.warn("before save " + leave.getVersion());
			// this will give employee object

			// // Here we are putting approvedBy string because there is
			// scheduler
			// // which runs on last day of month and auto approves all pending
			// // leaves in such case we need to set ApprovedBy "Auto Approved"
			leave.setApprovedBy(loggedEmployee.getFullName());

			leaveDAO.saveOrUpdate(leave);
			logger.warn("after save " + leave.getVersion());
			/*
			 * try { leaveApplicationAcknowledgement
			 * .sendLeaveApproveAcknowledgement(leave); } catch
			 * (MailCantSendException e) { logger.error("Mail cannot be send");
			 * }
			 */

			leaveManagementMailConfiguration
					.sendLeaveApproveAcknowledgement(leave);

		} catch (LeaveNotFoundException e) {
			throw new LeaveNotFoundException(e.getMessage());
		}

		// send Alert to Employee once Leave approved By Manager.
		Alert alert = alertBuilder.createLeaveApprovedAlert(leave);
		leaveDAO.save(alert);
		logger.warn("after save of alert " + leave.getVersion());

	}

	@Transactional
	@Override
	public void cancelLeave(Long id) {
		LeaveDebit leave = null;
		try {
			leave = leaveDAO.findBy(LeaveDebit.class, id);

			System.out.println("version of cancel leave " + leave.getVersion());
			if (leave.getVersion() >= 1) {
				throw new LeaveUpdationCannotProcess(
						"Leave cannot be Cancelled");
			}
			// Checking whether the applied leave before the financial cycle or
			// not .
			if (leaveManagementUtils.checkPreviousCycleLeave(leave)) {
				updateCarryForward(leave);
			}
			leave.setStatus(LeaveStatus.Cancelled);
			// leave.setStatusChangedOn(new Date());
			leaveDAO.saveOrUpdate(leave);
			/*
			 * try { leaveApplicationAcknowledgement
			 * .sendLeaveCancellationAcknowledgement(leave); } catch
			 * (MailCantSendException e) { logger.error("Mail cannot be send");
			 * }
			 */

			leaveManagementMailConfiguration
					.sendCancelLeaveMailNotification(leave);

		} catch (LeaveNotFoundException e) {
			throw new LeaveNotFoundException(e.getMessage());
		}
		// send Alert to manager if we Cancel a Leave.
		Alert alert = alertBuilder.createLeaveCancelledAlert(leave);
		leaveDAO.save(alert);
	}

	@Transactional
	@Override
	public Map<String, Object> getEmployeeLeaves(Integer startIndex,
			Integer endIndex, Long employeeId) {

		Map<String, Object> leaveDetailsMap = leaveDAO.getAllLeaves(startIndex,
				endIndex, employeeId);
		List<LeaveDebit> leaveDebit = (List) leaveDetailsMap
				.get("leaveDebitsList");
		Integer numberOfLeaves = (Integer) leaveDetailsMap.get("size");
		List<LeaveDTO> leaveDTOList = leaveBuilder
				.createLeaveDTOEntityList(leaveDebit);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", leaveDTOList);
		map.put("size", numberOfLeaves);
		return map;

	}

	@Transactional
	@Override
	public LeaveSummaryDTO getLeaveSummary(Long employeeId) {

		Employee employee = leaveDAO.findBy(Employee.class, employeeId);
		Date probationPeriodEndDate = employee.getJoiningDate().shift(
				new Duration(TimeUnit.MONTH, leaveDAO.getLeaveSettings()
						.getProbationPeriod()));

		DateRange presentDateRange = new DateRange(leaveManagementUtils
				.getCurrentFinancialPeriod().getMinimum(), new Date());

		Double employeeCarryForwardedLeaves = leaveDAO
				.getEmployeeCarryForwardedLeaves(employee, presentDateRange);
		if (employeeCarryForwardedLeaves == null) {
			employeeCarryForwardedLeaves = 0.0;
		}

		// For a employee in probation during the financial year cron his / her
		// leaves are calculated
		// as below.
		Date joiningDate = employee.getJoiningDate();
		
		// removed below condition as employees who are in probation period
        // should get leaves monthly.
        // Therir is no probation period condition we removed below condition . it is applied from jan2019.
        // if we need probation condition then uncomment below code.
		
		/*DateRange joinProbRange = new DateRange(joiningDate,
				probationPeriodEndDate);

		if (joinProbRange.contains(presentDateRange.getMinimum())) {
			employeeCarryForwardedLeaves = 0.0;
		}*/
		
		
		logger.warn("carryForwardedLeaves is :" + employeeCarryForwardedLeaves);

		Double creditedLeaves = 0d;

		Double leaveValue = 0.0;

		creditedLeaves = leaveManagementUtils.getCreditedLeaves(employeeId,
				presentDateRange);

		logger.warn("calculated credited leave is :" + creditedLeaves);

		Double totalCreditedLeaves = employeeCarryForwardedLeaves
				+ creditedLeaves;

		// logger.warn("totalCreditedLeaves is :" + totalCreditedLeaves);

		if (totalCreditedLeaves > leaveDAO.getLeaveSettings()
				.getMaxLeavesEarned()) {
			totalCreditedLeaves = new Double(leaveDAO.getLeaveSettings()
					.getMaxLeavesEarned());
		}

		logger.warn("totalCreditedLeaves is :" + totalCreditedLeaves);
		List<LeaveDebit> leavedebits = leaveDAO.getLeaveDebitedSet(employee,
				leaveManagementUtils.getCurrentFinancialPeriod());

		// New functionality for employees who are in probation period.
		// here we fixed the date as 11-08-2018,this is used for
		// if employee joins after 10th day of month then leave should not be
		// added for the following month.

		/*Date augDate = new Date(DayOfMonth.valueOf(11), MonthOfYear.valueOf(7),
				YearOfEra.valueOf(2018));

		logger.warn("joining date" + employee.getJoiningDate());
		logger.warn("condition for getting date"
				+ (employee.getJoiningDate().equals(augDate) || employee
						.getJoiningDate().isAfter(augDate)));

		if (employee.getJoiningDate().equals(augDate)
				|| employee.getJoiningDate().isAfter(augDate)) {
			logger.warn("day condition"
					+ (employee.getJoiningDate().getDayOfMonth().getValue() > 10));
			if (employee.getJoiningDate().getDayOfMonth().getValue() > 10) {
				logger.warn("leaves condition" + (totalCreditedLeaves >= 1.25));
				if (totalCreditedLeaves >= 1.25) {
					totalCreditedLeaves = totalCreditedLeaves - 1.25;
				}
			}
		}*/

		logger.warn("after date condition" + totalCreditedLeaves);

		LeaveSummaryDTO leaveSummaryDTO = leaveSummaryBuilder
				.createLeaveSummaryDTO(
						employeeBuilder.createEmployeeDTO(employee),
						totalCreditedLeaves, leavedebits,
						leaveDAO.getAllLeaveCategories());

		logger.warn("condition" + probationPeriodEndDate.isAfter(new Date()));

		// removed below condition due to employees who are in probation period
		// also get the monthly leaves.

		/*
		 * if (probationPeriodEndDate.isAfter(new Date())) {
		 * leaveSummaryDTO.setAllCreditedLeaves(0.0);
		 * leaveSummaryDTO.setAllAvailableLeaves(0.0); }
		 */

		logger.warn("leaveSummaryDTO :" + leaveSummaryDTO.toString());
		return leaveSummaryDTO;
	}

	@Transactional
	@Override
	public boolean isInProbation(Long employeeId) {
		Employee employee = leaveDAO.findBy(Employee.class, employeeId);
		if (employee == null) {
			throw new EmployeeNotFoundException();
		}
		LeaveSettingsLookup leaveSettings = leaveDAO.getLeaveSettings();

		return employeeUtils.isInProbationPeriod(employee, leaveSettings);

	}

	@Transactional
	@Override
	public Map<String, Object> getAllEmployeeLeaves(Integer startIndex,
			Integer endIndex, Long managerEmployeeId) {

		Map<String, Object> leaveDebitMap = new HashMap<String, Object>();
		List<LeaveDebit> resourceAndreporteesList = new ArrayList<LeaveDebit>();

		Set<LeaveDebit> finalList = new HashSet<LeaveDebit>();

		Employee employee = leaveDAO.findBy(Employee.class, managerEmployeeId);
		// Boolean totalListFlag = false;
		// Boolean reporteeListFlag = false;
		Permission adminPermission = leaveDAO.checkForPermission(
				"Leave Approvals", employee);
		Permission managerPermission = leaveDAO.checkForPermission(
				"Hierarchy Leave Approvals", employee);

		// for admin
		if (adminPermission.getView() && !managerPermission.getView()) {
			leaveDebitMap = leaveDAO.getAllEmployeePendingLeaves(startIndex,
					endIndex, getMonthPeiod());
		}
		// for manager
		else if (adminPermission.getView() && managerPermission.getView()) {

			Map<String, Object> leaves = null;

			Map<String, Object> leaveDebit = null;

			List<Long> managerIds = projectService.mangerUnderManager(employee
					.getEmployeeId());

			// getting employees under manager
			leaveDebit = leaveDAO.getReporteePendingLeaves(startIndex,
					endIndex, managerIds, getMonthPeiod());

			List<LeaveDebit> reporteesList = (List<LeaveDebit>) leaveDebit
					.get("leaveDebitList");

			Long projectMangerId = employee.getEmployeeId();

			// System.out.println("projectMangerId" + projectMangerId);

			// geeting employees under project manager

			leaves = leaveDAO.getResorces(startIndex, endIndex,
					projectMangerId, getMonthPeiod());

			List<LeaveDebit> resourcesList = (List<LeaveDebit>) leaves
					.get("leaveDebitList");

			reporteesList.addAll(resourcesList);

			finalList.addAll(reporteesList);

			resourceAndreporteesList.addAll(finalList);
			// System.out.println("finallist" + resourceAndreporteesList);

			leaveDebitMap.put("leaveDebitList", resourceAndreporteesList);
			leaveDebitMap.put("size", resourceAndreporteesList.size());

		}

		List<LeaveDebit> leaveDebit = (List) leaveDebitMap
				.get("leaveDebitList");
		Integer noOfEmployeeLeaves = (Integer) leaveDebitMap.get("size");

		List<LeaveDTO> leaveDTOsList = leaveBuilder
				.createLeaveDTOEntityList(leaveDebit);

		Set<EmpLeaveDTO> leaveSummary = new HashSet();

		for (LeaveDebit leave : leaveDebit) {
			// LeaveDTO dTO=new LeaveDTO();
			EmpLeaveDTO dTO = new EmpLeaveDTO();
			// EmpLeaveDTO dummyDto = new EmpLeaveDTO();
			// if (leaveSummary != null) {
			Boolean flag = false;
			for (EmpLeaveDTO empLeaveDTO : leaveSummary) {
				if (empLeaveDTO.getEmpId().equals(
						leave.getEmployee().getEmployeeId())) {
					flag = true;
					break;
					// System.out.println("in if block ");
				}
			}
			if (!flag) {
				LeaveSummaryDTO leaveSummaryDTO1 = getLeaveSummary(leave
						.getEmployee().getEmployeeId());
				dTO.setEmpId(leave.getEmployee().getEmployeeId());
				dTO.setRemainingDays(leaveSummaryDTO1.getAllAvailableLeaves());
				dTO.setApprovedLeaves(leaveSummaryDTO1.getAllTakenLeaves());

				dTO.setPendingLeaves(leaveSummaryDTO1.getAllPendingLeaves());
				dTO.setTotalDays(leaveSummaryDTO1.getAllCreditedLeaves());
				dTO.setEmpName(leaveSummaryDTO1.getEmployeeDTO().getFullName());
				dTO.setCancelAfterApprovalLeaves(leaveSummaryDTO1
						.getAllCancelAfterApprovalLeaves());
				leaveSummary.add(dTO);
			}
			// } else {
			// LeaveSummaryDTO leaveSummaryDTO1 =
			// getLeaveSummary(leave.getEmployee().getEmployeeId());
			// dTO.setRemainingDays(leaveSummaryDTO1.getAllAvailableLeaves());
			// dTO.setPendingLeaves(leaveSummaryDTO1.getAllPendingLeaves());
			// dTO.setTotalDays(leaveSummaryDTO1.getAllCreditedLeaves());
			// dTO.setEmpName(leaveSummaryDTO1.getEmployeeDTO().getFullName());
			// leaveDTOsList.add(leaveSummaryDTO1.);
			// //System.err.println("leave summary employee id"+leave.getEmployee().getEmployeeId());
			// System.err.println("emp name:" + dTO.getEmpName() +
			// "remaing leaves are:" + dTO.getRemainingDays() +
			// " total credited leaves" + dTO.getTotalDays() + "pending leaves:"
			// + dTO.getPendingLeaves());
			// }

		}
		Map<String, Object> allEmpLeavesMap = new HashMap<String, Object>();
		allEmpLeavesMap.put("allEmpLeavesList", leaveDTOsList);
		allEmpLeavesMap.put("size", noOfEmployeeLeaves);
		allEmpLeavesMap.put("employeeSummary", leaveSummary);
		return allEmpLeavesMap;

	}

	@Transactional
	@Override
	public Map<String, Object> searchEmployees(Integer startIndex,
			Integer endIndex, SearchCriteriaDTO searchCriteriaDTO) {

		Map<String, Object> leaveDebitMap = new HashMap<String, Object>();
		List<LeaveDebit> resourceAndreporteesList = new ArrayList<LeaveDebit>();

		Set<LeaveDebit> finalList = new HashSet<LeaveDebit>();

		Employee employee = leaveDAO.findBy(Employee.class,
				searchCriteriaDTO.getManagerId());

		Boolean totalLeaveFlag = false;
		Boolean partialLeaveFlag = false;
		Permission adminPermission = leaveDAO.checkForPermission(
				"Leave Approvals", employee);
		Permission managerPermission = leaveDAO.checkForPermission(
				"Hierarchy Leave Approvals", employee);
		if (adminPermission.getView() && !managerPermission.getView()) {
			totalLeaveFlag = true;
		} else if (adminPermission.getView() && managerPermission.getView()) {
			partialLeaveFlag = true;
		}

		if (totalLeaveFlag) {
			leaveDebitMap = leaveDAO.searchEmployeesAsAdmin(startIndex,
					endIndex, searchCriteriaDTO);
		} else if (partialLeaveFlag) {

			Map<String, Object> leavesList = null;

			Map<String, Object> leaveDebit = null;

			List<Long> managerIds = projectService.mangerUnderManager(employee
					.getEmployeeId());
			// getting employees under manager
			leaveDebit = leaveDAO.searchEmployees(startIndex, endIndex,
					managerIds, searchCriteriaDTO);

			List<LeaveDebit> reporteesList = (List<LeaveDebit>) leaveDebit
					.get("leaveDebitList");

			Long projectMangerId = employee.getEmployeeId();

			System.out.println("projectMangerId" + projectMangerId);
			// getting employees under projectmanager
			leavesList = leaveDAO.getResorcesList(startIndex, endIndex,
					projectMangerId, searchCriteriaDTO);

			List<LeaveDebit> resourcesList = (List<LeaveDebit>) leavesList
					.get("leaveDebitList");

			reporteesList.addAll(resourcesList);

			finalList.addAll(reporteesList);

			resourceAndreporteesList.addAll(finalList);
			// System.out.println("finallist" + resourceAndreporteesList);

			leaveDebitMap.put("leaveDebitList", resourceAndreporteesList);
			leaveDebitMap.put("size", resourceAndreporteesList.size());

		}
		List<LeaveDebit> leaveDebit = (List) leaveDebitMap
				.get("leaveDebitList");
		Integer employeeList = (Integer) leaveDebitMap.get("size");

		Collections.sort(leaveDebit, new Comparator<LeaveDebit>() {
			@Override
			public int compare(LeaveDebit o1, LeaveDebit o2) {

				int k = 0;
				if (o1.getPeriod().getMinimum()
						.isAfter(o2.getPeriod().getMinimum())) {
					k = -1;
				}
				if (o1.getPeriod().getMinimum()
						.isBefore(o2.getPeriod().getMinimum())) {
					k = 1;
				}
				return k;
			}
		});
		List<LeaveDTO> leavesList = leaveBuilder
				.createLeaveDTOEntityList(leaveDebit);

		Set<EmpLeaveDTO> leaveSummary = new HashSet();

		// Here if leave debits are not empty than leave summary of respective
		// debits are shown else if user selects member than that members leave
		// summary is shown
		if (!leaveDebit.isEmpty()) {

			for (LeaveDebit leave : leaveDebit) {
				EmpLeaveDTO dTO = new EmpLeaveDTO();

				Boolean flag = false;
				for (EmpLeaveDTO empLeaveDTO : leaveSummary) {
					if (empLeaveDTO.getEmpId().equals(
							leave.getEmployee().getEmployeeId())) {
						flag = true;
						break;

					}
				}
				if (!flag) {
					LeaveSummaryDTO leaveSummaryDTO1 = getLeaveSummary(leave
							.getEmployee().getEmployeeId());
					dTO.setEmpId(leave.getEmployee().getEmployeeId());
					dTO.setRemainingDays(leaveSummaryDTO1
							.getAllAvailableLeaves());
					dTO.setPendingLeaves(leaveSummaryDTO1.getAllPendingLeaves());
					dTO.setApprovedLeaves(leaveSummaryDTO1.getAllTakenLeaves());
					dTO.setTotalDays(leaveSummaryDTO1.getAllCreditedLeaves());
					dTO.setCancelAfterApprovalLeaves(leaveSummaryDTO1
							.getAllCancelAfterApprovalLeaves());
					dTO.setEmpName(leaveSummaryDTO1.getEmployeeDTO()
							.getFullName());
					leaveSummary.add(dTO);
				}
			}
		} else if (leaveDebit.isEmpty()
				&& searchCriteriaDTO.getMember() != null) {
			EmpLeaveDTO dTO = new EmpLeaveDTO();
			LeaveSummaryDTO leaveSummaryDTO1 = getLeaveSummary(searchCriteriaDTO
					.getMember());
			dTO.setEmpId(searchCriteriaDTO.getMember());
			dTO.setRemainingDays(leaveSummaryDTO1.getAllAvailableLeaves());
			dTO.setPendingLeaves(leaveSummaryDTO1.getAllPendingLeaves());
			dTO.setApprovedLeaves(leaveSummaryDTO1.getAllTakenLeaves());
			dTO.setTotalDays(leaveSummaryDTO1.getAllCreditedLeaves());
			dTO.setCancelAfterApprovalLeaves(leaveSummaryDTO1
					.getAllCancelAfterApprovalLeaves());
			dTO.setEmpName(leaveSummaryDTO1.getEmployeeDTO().getFullName());
			leaveSummary.add(dTO);

		}

		Map<String, Object> serachLeavesMap = new HashMap<String, Object>();
		serachLeavesMap.put("searchLeaves", leavesList);
		serachLeavesMap.put("size", employeeList);
		serachLeavesMap.put("leaveSummary", leaveSummary);
		return serachLeavesMap;
	}

	@Transactional
	@Override
	public void rejectlLeave(Long leaveId) throws LeaveNotFoundException {

		Map<String, Object> employeeDetailMap = securityUtils
				.getLoggedEmployeeDetailsSecurityContextHolder();

		Employee loggedEmployee = (Employee) employeeDetailMap.get("employee");

		/*
		 * LeaveDebit leavesId = dao.findBy(LeaveDebit.class, leaveId);
		 * 
		 * 
		 * if(leavesId.getEmployee().getProjectManager() != null &&
		 * !leavesId.getEmployee
		 * ().getProjectManager().getEmployeeId().equals(loggedEmployee
		 * .getEmployeeId()) ){ logger.warn("exception"); throw new
		 * LeaveCannotApprovedException("ProjectManager exits"); }
		 */

		LeaveDebit leave = null;
		try {
			leave = leaveDAO.findBy(LeaveDebit.class, leaveId);
			if (leave.getVersion() >= 1) {
				// For approving the cancel after approval for a leave by
				// manager
				if (leave.getVersion() == 2) {
					leave.setStatus(LeaveStatus.Rejected);

				} else {
					throw new LeaveUpdationCannotProcess(
							"Leave cannot be Rejected");
				}
			}

			leave.setStatus(LeaveStatus.Rejected);
			// Checking whether the applied leave before the financial cycle or
			// not .
			if (leaveManagementUtils.checkPreviousCycleLeave(leave)) {
				updateCarryForward(leave);
			}
			/*
			 * try { leaveApplicationAcknowledgement
			 * .sendLeaveRejectionAcknowledgement(leave); } catch
			 * (MailCantSendException e) { logger.error("Mail cannot be send");
			 * }
			 */
			leaveManagementMailConfiguration
					.sendLeaveRejectionMailNotification(leave);

		} catch (LeaveNotFoundException e) {
			throw new LeaveNotFoundException(e.getMessage());
		}
		// send Alert to Employee if Manager Reject the Leave.
		Alert alert = alertBuilder.createLeaveRejectedAlert(leave);
		leaveDAO.save(alert);
	}

	@Transactional
	@Override
	public DateRange getMonthPeiod() {
		DateRange monthPeriod = leaveManagementUtils.getCurrentMonthPeriod();
		Date currentDate = new Date();

		MonthYear currentMonth = new MonthYear();

		logger.info("current month is :" + currentMonth.toString());
		MonthYear lastMonth = currentMonth.shift(new Duration(TimeUnit.MONTH,
				-1));
		logger.info("last month is :" + lastMonth.toString());

		Date nextCutOffDate = new Date(DayOfMonth.valueOf(leaveDAO
				.getLeaveSettings().getPayrollCutoffDate()),
				MonthOfYear.valueOf(currentDate.getMonthOfYear().getValue()),
				YearOfEra.valueOf(currentDate.getYearOfEra().getValue()));

		logger.info("nextCutOffDate is :" + nextCutOffDate.toString());
		if (currentDate.getMonthOfYear().getValue() == nextCutOffDate
				.getMonthOfYear().getValue()
				&& currentDate.isBefore(nextCutOffDate)) {
			logger.info("last month  is :" + lastMonth.toString());
			monthPeriod = leaveManagementUtils
					.getConstructMonthPeriod(lastMonth);

		}
		logger.info("returning month period is :"
				+ monthPeriod.getMinimum().getJavaDate() + " max is "
				+ monthPeriod.getMaximum().getJavaDate());
		return monthPeriod;
	}

	@Transactional
	@Override
	public void cancelAfterApproval(Long id) {
		LeaveDebit leave = null;
		try {
			leave = leaveDAO.findBy(LeaveDebit.class, id);
			// if (leave.getVersion() >= 1) {
			// throw new LeaveUpdationCannotProcess(
			// "Leave cannot be Cancelled");
			// }
			leave.setStatus(LeaveStatus.CancelAfterApproval);

			// leave.setStatusChangedOn(new Date());

			leaveDAO.saveOrUpdate(leave);
			try {
				leaveApplicationAcknowledgement
						.sendLeaveCancellationAcknowledgement(leave);
			} catch (MailCantSendException e) {
				logger.error("Mail cannot be send");
			}
		} catch (LeaveNotFoundException e) {
			throw new LeaveNotFoundException(e.getMessage());
		}
		// send Alert to manager if we Cancel a Leave.

		Alert alert = alertBuilder.createLeaveCancelledAlert(leave);
		leaveDAO.save(alert);

	}

	public void updateCarryForward(LeaveDebit leave) {
		DateRange currentFinanace = leaveManagementUtils
				.getCurrentFinancialPeriod();
		CarryForwardLeave carryForwardLeave = leaveDAO.getCarryForwardLeave(
				leave.getEmployee(), currentFinanace.getMinimum());
		Double totalLeaves = carryForwardLeave.getDaysCredited()
				+ leave.getNumberOfDays();

		if (totalLeaves > 5d) {
			totalLeaves = 5d;
		}
		carryForwardLeave.setDaysCredited(totalLeaves);
		leaveDAO.saveOrUpdate(carryForwardLeave);
	}

	@Override
	public boolean checkprojectManagerexits(Long leaveid) {

		Map<String, Object> employeeDetailMap = securityUtils
				.getLoggedEmployeeDetailsSecurityContextHolder();

		Employee loggedEmployee = (Employee) employeeDetailMap.get("employee");

		LeaveDebit leavesIds = dao.findBy(LeaveDebit.class, leaveid);

		// if(leavesIds.getEmployee().getProjectManager() != null &&
		// leavesIds.getEmployee().getProjectManager().getEmployeeId() !=
		// loggedEmployee.getEmployeeId() ){
		// logger.warn("exception");
		// throw new LeaveCannotApprovedException("ProjectManager exits");
		// }
		//
		Boolean flag = Boolean.FALSE;

		/*
		 * logger.warn("Login employee id"+loggedEmployee.getEmployeeId());
		 * logger.warn("employee project manager"+
		 * leavesIds.getEmployee().getProjectManager().getEmployeeId());
		 * logger.warn("condition"
		 * +!leavesIds.getEmployee().getProjectManager().getEmployeeId()
		 * .equals(loggedEmployee.getEmployeeId())); logger.warn("jjjjj"
		 * +(leavesIds.getEmployee().getProjectManager().getEmployeeId() !=
		 * loggedEmployee.getEmployeeId()));
		 */
		if (leavesIds.getEmployee().getProjectManager() != null
				&& !leavesIds.getEmployee().getProjectManager().getEmployeeId()
						.equals(loggedEmployee.getEmployeeId())) {
			flag = Boolean.TRUE;
		}

		return flag;
	}

	public Boolean checkLeavesForPreviousMonth(LeaveDebit leaveDebit,
			Double avialableLeaves) {

		DateRange previousMonthRange = leaveManagementUtils
				.getPreviousMonthPeriod();
		DateRange currentMonthRange = leaveManagementUtils
				.getCurrentMonthPeriod();
		Boolean flag = false;
		Double previousMonthLeaves = 0.0;

		 /*System.out.println("previousMonthFirstDay:"+previousMonthRange.getMinimum());
		 System.out.println("previousMonthLastDay:"+previousMonthRange.getMaximum());
		 System.out.println("currentMonthFirstDay:"+currentMonthRange.getMinimum());
		 System.out.println("currentMonthLastDay:"+currentMonthRange.getMaximum());*/

		/*if (((leaveDebit.getPeriod().getMinimum()
				.equals(previousMonthRange.getMinimum()) || leaveDebit
				.getPeriod().getMinimum()
				.isAfter(previousMonthRange.getMinimum())) && (leaveDebit
				.getPeriod().getMaximum()
				.equals(previousMonthRange.getMaximum()) || leaveDebit
				.getPeriod().getMaximum()
				.isBefore(previousMonthRange.getMaximum())))
				||
				// We are not able to apply in current month which is credited on 24th. Because those leaves belong to next month.
				(((leaveDebit.getLeaveAppliedOn().getDayOfMonth().getValue())<= 24) && ((leaveDebit.getPeriod()
						.getMinimum().equals(currentMonthRange.getMinimum()) || leaveDebit
						.getPeriod().getMinimum()
						.isAfter(currentMonthRange.getMinimum())) && (leaveDebit
						.getPeriod().getMaximum()
						.equals(currentMonthRange.getMaximum()) || leaveDebit
						.getPeriod().getMaximum()
						.isBefore(currentMonthRange.getMaximum()))))) {*/
		
		if((leaveDebit.getPeriod().getMinimum().equals(currentMonthRange.getMinimum())) ||
				leaveDebit.getPeriod().getMinimum().isAfter(currentMonthRange.getMinimum())
				&&(leaveDebit.getPeriod().getMinimum().isBefore(currentMonthRange.getMaximum())) ||
				(leaveDebit.getPeriod().getMinimum().equals(currentMonthRange.getMaximum()))){
			
			if(((leaveDebit.getLeaveAppliedOn().getDayOfMonth().getValue())>= 24) && 
					((leaveDebit.getLeaveAppliedOn().getDayOfMonth().getValue())<= currentMonthRange.getMaximum().getDayOfMonth().getValue())){
				
				// From Date belong to future Date then we are allowing to apply whole leave
				if(leaveDebit.getPeriod().getMaximum().isAfter(currentMonthRange.getMaximum())){
					logger.warn("availableLeaves:"+avialableLeaves);
					if(leaveDebit.getLeaveCategory().getLeaveType()
							.equals(LeaveType.EARNED)){
						
					if (Math.floor(avialableLeaves) != 0) {
						
						previousMonthLeaves = avialableLeaves;

					}

					 logger.warn("appiled days:"+leaveDebit.getNumberOfDays());
					
					
						if (leaveDebit.getNumberOfDays() > previousMonthLeaves) {

							flag = true;

							System.out.println("You Cannot apply for" + " "
									+ Math.round(leaveDebit.getNumberOfDays()) + " "
									+ "days," + "As You have only" + " "
									+ previousMonthLeaves + " " + "days");
							throw new NotEnoughLeavesAvaialableException(
									"You don't have enough leaves to apply for last month");

						}
						
					}
					
				}
				else{
					
				logger.warn("availableLeaves:"+avialableLeaves);
					if(leaveDebit.getLeaveCategory().getLeaveType()
							.equals(LeaveType.EARNED)){
						
					if (Math.floor(avialableLeaves) != 0) {
						
						previousMonthLeaves = avialableLeaves - 1.25;

					}

					 logger.warn("appiled days:"+leaveDebit.getNumberOfDays());
					
					
						if (leaveDebit.getNumberOfDays() > previousMonthLeaves) {

							flag = true;

							System.out.println("You Cannot apply for" + " "
									+ Math.round(leaveDebit.getNumberOfDays()) + " "
									+ "days," + "As You have only" + " "
									+ previousMonthLeaves + " " + "days");
							throw new NotEnoughLeavesAvaialableException(
									"You don't have enough leaves to apply for last month");

						}
						
					}
				}

				}
				
				
			}
		return flag;
		}

	@Override
	public List<Integer> probationPeriod() {
		List<ProbationPeriod> list = leaveDAO.getProbationMonths();
		List<Integer> monthsList = new ArrayList<Integer>();
		for(ProbationPeriod months:list){
		   monthsList.add(Integer.parseInt(months.getProbationMonth()));
		}
		return monthsList;
	}

	@Override
	public List<Integer> getCuttOffDates() {
		List<PayrollCutOffDates> list = leaveDAO.getCuttOffDates();
		List<Integer> datesList = new ArrayList<Integer>();
		for(PayrollCutOffDates dates:list){
			datesList.add(Integer.parseInt(dates.getCutOffDates()));
		}
		return datesList;
	}

			
	}

	

