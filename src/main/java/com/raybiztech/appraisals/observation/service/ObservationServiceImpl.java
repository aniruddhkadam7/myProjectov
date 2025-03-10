package com.raybiztech.appraisals.observation.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.raybiztech.appraisals.alerts.builder.AlertBuilder;
import com.raybiztech.appraisals.alerts.business.Alert;
import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.dao.DAO;
import com.raybiztech.appraisals.exception.FileUploaderUtilException;
import com.raybiztech.appraisals.observation.builder.ObservationBuilder;
import com.raybiztech.appraisals.observation.business.Observation;
import com.raybiztech.appraisals.observation.business.PerformanceRatings;
import com.raybiztech.appraisals.observation.dao.ObservationDAO;
import com.raybiztech.appraisals.observation.dto.ObservationDTO;
import com.raybiztech.appraisals.observation.dto.PerformanceRatingsDTO;
import com.raybiztech.appraisals.observation.dto.SearchObservationDTO;
import com.raybiztech.appraisals.properties.PropBean;
import com.raybiztech.appraisals.utils.FileUploaderUtil;
import com.raybiztech.date.Date;
import com.raybiztech.date.DateRange;
import com.raybiztech.date.MonthOfYear;
import com.raybiztech.date.MonthYear;
import com.raybiztech.date.YearOfEra;
import com.raybiztech.leavemanagement.utils.LeaveManagementUtils;
import com.raybiztech.mailtemplates.util.PerformanceManagementMailConfiguration;
import com.raybiztech.projectmanagement.builder.ReportBuilder;
import com.raybiztech.projectmanagement.dao.ResourceManagementDAO;
import com.raybiztech.projectmanagement.dto.ReportDTO;
import com.raybiztech.projectmanagement.service.ProjectService;
import com.raybiztech.recruitment.dao.JobPortalDAO;
import com.raybiztech.rolefeature.business.Permission;

@Service
@Transactional
public class ObservationServiceImpl implements ObservationService {

	@Autowired
	ObservationDAO observationDAO;
	@Autowired
	ObservationBuilder observationBuilder;
	@Autowired
	ReportBuilder builder;
	@Autowired
	ResourceManagementDAO resourceManagementDAO;
	@Autowired
	AlertBuilder alertBuilder;
	@Autowired
	PropBean propBean;
	@Autowired
	ProjectService projectService;
	@Autowired
	DAO dao;
	@Autowired
	PerformanceManagementMailConfiguration performanceManagementMailConfiguration;
	@Autowired
	JobPortalDAO jobPortalDAOImpl;

	Logger logger = Logger.getLogger(ObservationServiceImpl.class);

	// @CacheEvict(value = "observations", allEntries = true)
	@Override
	public Long addObservation(ObservationDTO observationDTO) {

		Observation observation = observationBuilder
				.convertFormObservationDTOtoObservation(observationDTO);
		// Boolean duplication =
		// observationDAO.checkForDuplication(observation);

		// If there is no observation already added for the particular month and
		// employee from the same manegr then we are saving into data base
		// if (duplication) {

		observationDAO.save(observation);
		Employee employee = observationDAO.findBy(Employee.class,
				observationDTO.getEmployee().getPersonId());
		Employee addedBy = observationDAO.findBy(Employee.class, observationDTO
				.getAddedBy().getPersonId());
		Alert alert = alertBuilder.createObservationAlert(employee, addedBy);

		performanceManagementMailConfiguration
				.sendObservationMailNotification(observationDTO);

		/* mailNotification.sendObservationMailNotification(observationDTO); */
		observationDAO.save(alert);
		Long id = observation.getId();

		return id;
		// }
		// throw exception if the observation is already given
		// else {
		// throw new DuplicateObservationException(
		// "The observation was already given for this month.");
		//
		// }
	}

	@Override
	public void updateObservation(ObservationDTO observationDTO) {
		Observation observation = observationBuilder
				.convertFormObservationDTOtoObservation(observationDTO);
		observationDAO.saveOrUpdate(observation);

	}

	@Override
	public void deleteObservation(Long id) {
		Observation observation = observationDAO.findBy(Observation.class, id);
		observationDAO.delete(observation);
	}

	@Override
	public Map<String, Object> ObservationOfEmployee(Long id,
			Integer startIndex, Integer endIndex) {
		Employee employee = observationDAO.findBy(Employee.class, id);
		Set<Observation> observations = null;
		observations = employee.getObservation();
		List<Observation> observationList = new ArrayList<Observation>(
				observations);
		Map<String, Object> observationMap = null;
		Integer noOfRecords = null;
		noOfRecords = observations.size();
		if (noOfRecords < endIndex) {
			observationList = new ArrayList<Observation>(observations).subList(
					startIndex, noOfRecords);
		} else {
			observationList = new ArrayList<Observation>(observations).subList(
					startIndex, endIndex);
		}
		if (observationList != null) {
			Collections.sort(observationList, new Comparator<Observation>() {
				@Override
				public int compare(Observation o1, Observation o2) {
					int k = 0;
					if (o1.getDate().isAfter(o2.getDate())) {
						k = -1;
					}
					if (o1.getDate().isBefore(o2.getDate())) {
						k = 1;
					}
					return k;
				}
			});
		}
		List<ObservationDTO> observationDTOs = observationBuilder
				.convertFormObservationToObservationDTOList(observationList);
		observationMap = new HashMap<String, Object>();
		observationMap.put("list", observationDTOs);
		observationMap.put("size", noOfRecords);

		return observationMap;
	}

	@Override
	public List<ReportDTO> getActiveEmployees(Long id) {

		Employee employee = observationDAO.findBy(Employee.class, id);
		List<Employee> masterlist = new ArrayList<Employee>();
		List<Employee> managerReportees=new ArrayList<Employee>();
		Set<Employee> set = null;

		List<Employee> reportmanagerlist = resourceManagementDAO
				.activeEmployeeList(employee);
		//dao.mangerUnderManager(id);

		
		Permission observationListPermission = dao.checkForPermission(
				"Observation List", employee);
		Permission hierarchyObservationList = dao.checkForPermission(
				"Hierarchy Observation List", employee);
		if (observationListPermission.getView()
				&& hierarchyObservationList.getView()) {
			
			for (Employee emp : reportmanagerlist) {
				List<Employee> reportemployees = resourceManagementDAO
						.activeEmployeeList(emp);

				managerReportees.addAll(reportemployees);
			}
			managerReportees.addAll(reportmanagerlist);
			set=new HashSet<Employee>(managerReportees);
			set.addAll(resourceManagementDAO.getAllocationEmployees(employee));
			masterlist.addAll(set);

		}else{
			masterlist.addAll(reportmanagerlist);
		}
		List<ReportDTO> employees = builder.convertEmplyeeTOReport(masterlist);

		return employees;

	}

	// @Cacheable(value = "observations", key = "#id")
	@Override
	public Map<String, Object> ObservationOfReport(Long id, Integer startIndex,
			Integer endIndex) {

		Employee employee = observationDAO.findBy(Employee.class, id);
		Set<Observation> observations = null;
		List<Observation> observationList = null;
		Map<String, Object> observationReportMap = null;
		Integer noOfRecords = null;

		List<Observation> allObservations = null;
		List<Observation> sortedlist = null;
		// List<Observation> sortedlist=new
		// ArrayList<Observation>(observations);
		Permission observationListPermission = dao.checkForPermission(
				"Observation List", employee);
		Permission hierarchyObservationList = dao.checkForPermission(
				"Hierarchy Observation List", employee);

		// if (employee.getRole().equalsIgnoreCase("Manager")) {
		if (observationListPermission.getView()
				&& hierarchyObservationList.getView()) {

			List<Long> managerIds = projectService.mangerUnderManager(employee
					.getEmployeeId());
			observations = observationDAO
					.getEmployeeObservationsListUnderManager(managerIds);

			// List<Employee> employees = observationDAO
			// .managerReporties(employee);

			// for (Employee e : employees) {
			//
			// if (e.getRole().equalsIgnoreCase("Manager")) {
			//
			// observations.addAll(observationDAO
			// .getEmployeeObservationsListUnderManager(e));
			// }
			//
			// }

			noOfRecords = observations.size();

			if (endIndex >= noOfRecords) {
				endIndex = noOfRecords;

			}
			// logger.warn("the size of the observations is "+noOfRecords);
			// sortedlist = new ArrayList<Observation>(observations);

		}
		// else if (employee.getRole().equalsIgnoreCase("admin")) {
		else {

			allObservations = observationDAO.getAllEmployeesUnderAdmin();
			noOfRecords = (Integer) allObservations.size();
		}
		// if (allObservations != null) {
		// Collections.sort(allObservations, new Comparator<Observation>() {
		// @Override
		// public int compare(Observation o1, Observation o2) {
		// int k = 0;
		// if (o1.getDate().isAfter(o2.getDate())) {
		// k = -1;
		// }
		// if (o1.getDate().isBefore(o2.getDate())) {
		// k = 1;
		// }
		// return k;
		//
		// }
		// });
		// }
		//
		if (observations != null) {
			sortedlist = new ArrayList<Observation>(observations);

			Collections.sort(sortedlist, new Comparator<Observation>() {
				@Override
				public int compare(Observation o1, Observation o2) {
					int k = 0;
					if (o1.getId() > o2.getId()) {
						k = -1;
					}
					if (o1.getId() < o2.getId()) {
						k = 1;
					}
					return k;

				}
			});
		}

		if (observationListPermission.getView()
				&& !hierarchyObservationList.getView()) {

			endIndex = endIndex < allObservations.size() ? endIndex
					: allObservations.size();
			observationList = new ArrayList<Observation>(allObservations)
					.subList(startIndex, endIndex);
			List<ObservationDTO> observationDTO = observationBuilder
					.convertFormObservationToObservationDTOList(observationList);
			observationReportMap = new HashMap<String, Object>();
			observationReportMap.put("list", observationDTO);
			observationReportMap.put("size", noOfRecords);
			return observationReportMap;

		} else {

			if (sortedlist != null) {
				endIndex = endIndex < sortedlist.size() ? endIndex : sortedlist
						.size();
				observationList = new ArrayList<Observation>(sortedlist)
						.subList(startIndex, endIndex);
			}

			List<ObservationDTO> observationDTO = observationBuilder
					.convertFormObservationToObservationDTOList(observationList);

			observationReportMap = new HashMap<String, Object>();
			observationReportMap.put("list", observationDTO);
			observationReportMap.put("size", noOfRecords);
			return observationReportMap;
		}

	}

	@Override
	public Map<String, Object> searchObservation(
			SearchObservationDTO observationDTO, Long empId,
			Integer startIndex, Integer endIndex) {

		Employee employee = observationDAO.findBy(Employee.class, empId);
		Set<Observation> observations = null;
		List<Observation> observationList = null;
		Map<String, Object> observationReportMap = null;
		List<ObservationDTO> observationDTOs = new ArrayList<ObservationDTO>();
		Integer noOfRecords = null;
		Map<String, Date> map = new HashMap<String, Date>();
		Date finalFromDate = null;
		Date finalToDate = null;
		if (observationDTO.getMonthStatus() != null) {
			if (!observationDTO.getMonthStatus().equals("custom")) {
				map = dao.getCustomDates(observationDTO.getMonthStatus());
				finalFromDate = map.get("startDate");
				finalToDate = map.get("endDate");

			} else {
				LeaveManagementUtils leaveManagementUtils = new LeaveManagementUtils();

				MonthOfYear fromMonthOfYear = MonthOfYear
						.valueOf(observationDTO.getFromMonthVal() - 1);
				YearOfEra fromYearOfEra = YearOfEra.valueOf(observationDTO
						.getFromYearVal());
				MonthOfYear toMonthOfYear = MonthOfYear.valueOf(observationDTO
						.getToMonthVal() - 1);
				YearOfEra toYearOfEra = YearOfEra.valueOf(observationDTO
						.getToYearVal());

				DateRange fromMonthPeriod = leaveManagementUtils
						.getConstructMonthPeriod(new MonthYear(fromMonthOfYear,
								fromYearOfEra));

				DateRange toMonthPeriod = leaveManagementUtils
						.getConstructMonthPeriod(new MonthYear(toMonthOfYear,
								toYearOfEra));

				finalFromDate = fromMonthPeriod.getMinimum();

				finalToDate = toMonthPeriod.getMaximum();
			}
		}

		Permission observationListPermission = dao.checkForPermission(
				"Observation List", employee);
		Permission hierarchyObservationList = dao.checkForPermission(
				"Hierarchy Observation List", employee);
		if (observationListPermission.getView()
				&& hierarchyObservationList.getView()) {
			List<Long> empIds = projectService.mangerUnderManager(empId);
			observations = observationDAO
					.getEmployeeObservationsListUnderManager(empIds,
							observationDTO, finalFromDate, finalToDate);
			observationList = new ArrayList<Observation>(observations);

			noOfRecords = observations.size();
			if (endIndex >= noOfRecords) {
				endIndex = noOfRecords;
			}
			observationList = new ArrayList<Observation>(observations).subList(
					startIndex, endIndex);

		} else if (observationListPermission.getView()
				&& !hierarchyObservationList.getView()) {
			// If you selected for the non rated employees
			if (observationDTO.getNotRatedFlag()) {
				List<Employee> empList = observationDAO
						.getNonRatedEmployees(observationDTO);
				observationDTOs = observationBuilder
						.convertEmployeeToObservationDTO(empList,
								observationDTO.getSelecteedMonthandYear());
				noOfRecords = observationDTOs.size();
				if (endIndex >= noOfRecords) {
					endIndex = noOfRecords;
				}
				observationDTOs = observationDTOs.subList(startIndex, endIndex);

			} else {

				observationReportMap = observationDAO
						.getEmployeeObservationsListUnderAdmin(startIndex,
								endIndex, observationDTO, finalFromDate,
								finalToDate);
				observationList = new ArrayList<Observation>(
						(Set) observationReportMap.get("list"));
				noOfRecords = (Integer) observationReportMap.get("size");
			}

		} else if (!observationListPermission.getView()
				&& !hierarchyObservationList.getView()) {
			observationReportMap = observationDAO
					.getEmployeeIndvisualObservations(startIndex, endIndex,
							observationDTO, finalFromDate, finalToDate);
			observationList = new ArrayList<Observation>(
					(Set) observationReportMap.get("list"));
			noOfRecords = (Integer) observationReportMap.get("size");
		}
		List<ObservationDTO> searchobservationList=new ArrayList<ObservationDTO>();
		observationReportMap = new HashMap<String, Object>();
		if (!observationDTO.getNotRatedFlag()) {
			if (observationList != null) {
				Collections.sort(observationList,
						new Comparator<Observation>() {
							@Override
							public int compare(Observation o1, Observation o2) {
								int k = 0;
								if (o1.getId() > o2.getId()) {
									k = -1;
								}
								if (o1.getId() < o2.getId()) {
									k = 1;
								}
								return k;

							}
						});
			}
			List<ObservationDTO> searchObservationDTO = observationBuilder
					.convertFormObservationToObservationDTOList(observationList);
		
			
			if (endIndex <= searchObservationDTO.size()) {
				searchobservationList = searchObservationDTO.subList(startIndex, endIndex);
			} else {
				searchobservationList = searchObservationDTO.subList(startIndex, searchObservationDTO.size());
			}

			
			if (endIndex >= noOfRecords) {
				endIndex = noOfRecords;

			}
			observationReportMap.put("list", searchobservationList);
			observationReportMap.put("size", noOfRecords);
		}
		// If you selected for the non rated employees
		else {
			observationReportMap.put("list", observationDTOs);
			observationReportMap.put("size", noOfRecords);

		}

		return observationReportMap;

	}

	@Override
	public void downloadFile(HttpServletResponse response, String fileName) {

		try {
			FileUploaderUtil util = new FileUploaderUtil();
			util.obsFiledownload(response, fileName, propBean);
		} catch (Exception ex) {
			throw new FileUploaderUtilException(
					"Exception occured while uploading a file in Legal "
							+ ex.getMessage(), ex);
		}

	}

	/*
	 * @Override public List<ObservationDTO> getObservationHistory( String
	 * status, Long emid, Integer fromMonth, Integer fromYear, Integer toMonth,
	 * Integer toYear) { List<Observation> observationList = null; if
	 * (status.equalsIgnoreCase("custom")) { LeaveManagementUtils
	 * leaveManagementUtils = new LeaveManagementUtils();
	 * 
	 * MonthOfYear fromMonthOfYear = MonthOfYear.valueOf(fromMonth - 1);
	 * YearOfEra fromYearOfEra = YearOfEra.valueOf(fromYear); MonthOfYear
	 * toMonthOfYear = MonthOfYear.valueOf(toMonth - 1); YearOfEra toYearOfEra =
	 * YearOfEra.valueOf(toYear);
	 * 
	 * DateRange fromMonthPeriod = leaveManagementUtils
	 * .getConstructMonthPeriod(new MonthYear(fromMonthOfYear, fromYearOfEra));
	 * 
	 * DateRange toMonthPeriod = leaveManagementUtils
	 * .getConstructMonthPeriod(new MonthYear(toMonthOfYear, toYearOfEra));
	 * 
	 * Date finalFromDate = fromMonthPeriod.getMinimum();
	 * 
	 * Date finalToDate = toMonthPeriod.getMaximum();
	 * 
	 * observationList = observationDAO.getAllMonthwiseObservation(
	 * finalFromDate, finalToDate, emid); } else { Employee employee =
	 * observationDAO.findBy(Employee.class, emid); Set<Observation>
	 * observations = null; observations = employee.getObservation();
	 * observationList = new ArrayList<Observation>(observations);
	 * 
	 * } if (observationList != null) { Collections.sort(observationList, new
	 * Comparator<Observation>() {
	 * 
	 * @Override public int compare(Observation o1, Observation o2) { int k = 0;
	 * if (o1.getObservationMonth().isAfter( o2.getObservationMonth())) { k =
	 * -1; } if (o1.getObservationMonth().isBefore( o2.getObservationMonth())) {
	 * k = 1; } return k;
	 * 
	 * } }); } List observationData = null; if
	 * (status.equalsIgnoreCase("ThreeMonths")) { observationData =
	 * (observationList.size() > 5) ? observationList .subList(0, 3) :
	 * observationList; } else if (status.equalsIgnoreCase("SixMonths")) {
	 * observationData = (observationList.size() > 5) ? observationList
	 * .subList(0, 6) : observationList; } else if
	 * (status.equalsIgnoreCase("year")) { observationData =
	 * (observationList.size() > 12) ? observationList .subList(0, 12) :
	 * observationList; } else if (status.equalsIgnoreCase("All") ||
	 * status.equalsIgnoreCase("custom")) { observationData = observationList; }
	 * if (observationData != null) Collections.reverse(observationData); return
	 * observationBuilder.getGraphs(observationData); }
	 */

	@Override
	public Map<String, Object> getObservationHistory(String dateStatus,
			Integer startIndex, Integer endIndex, Long emid, Integer fromMonth,
			Integer fromYear, Integer toMonth, Integer toYear) {

		List<Observation> observationList = null;
		Map<String, Object> observMap = null;
		Integer noOfRecords = null;

		if (dateStatus.equalsIgnoreCase("year")) {
			Calendar previousDates = Calendar.getInstance();
			fromMonth = previousDates.get(Calendar.MONTH); // beware of month
															// indexing from
															// zero
			fromYear = previousDates.get(Calendar.YEAR) - 1;
			logger.info("if loop fromYear:" + fromYear + "fromMonth:"
					+ fromMonth);
			Calendar currentDates = Calendar.getInstance();
			toMonth = currentDates.get(Calendar.MONTH) + 1; // beware of month
															// indexing from
															// zero
			toYear = currentDates.get(Calendar.YEAR);
			logger.info("if loop toYear:" + toYear + "toMonth:" + toMonth);
		}

		// if (dateStatus.equalsIgnoreCase("custom")) {

		LeaveManagementUtils leaveManagementUtils = new LeaveManagementUtils();

		MonthOfYear fromMonthOfYear = MonthOfYear.valueOf(fromMonth - 1);
		YearOfEra fromYearOfEra = YearOfEra.valueOf(fromYear);
		MonthOfYear toMonthOfYear = MonthOfYear.valueOf(toMonth - 1);
		YearOfEra toYearOfEra = YearOfEra.valueOf(toYear);

		DateRange fromMonthPeriod = leaveManagementUtils
				.getConstructMonthPeriod(new MonthYear(fromMonthOfYear,
						fromYearOfEra));

		DateRange toMonthPeriod = leaveManagementUtils
				.getConstructMonthPeriod(new MonthYear(toMonthOfYear,
						toYearOfEra));

		Date finalFromDate = fromMonthPeriod.getMinimum();

		Date finalToDate = toMonthPeriod.getMaximum();

		// List<Long> managers = projectService.mangerUnderManager(emid);

		observMap = observationDAO.getAllMonthwiseObservation(startIndex,
				endIndex, finalFromDate, finalToDate, emid);
		Set<Observation> observationSet = (Set<Observation>) observMap
				.get("list");
		observationList = new ArrayList<Observation>(observationSet);

		// } else {
		//
		// Employee employee = observationDAO.findBy(Employee.class, emid);
		// Set<Observation> observations = null;
		// observations = employee.getObservation();
		// observationList = new ArrayList<Observation>(observations);
		//
		// }

		// List observationData = null;
		// if (dateStatus.equalsIgnoreCase("ThreeMonths")) {
		// observationData = (observationList.size() > 5) ? observationList
		// .subList(0, 3) : observationList;
		// } else if (dateStatus.equalsIgnoreCase("SixMonths")) {
		// observationData = (observationList.size() > 5) ? observationList
		// .subList(0, 6) : observationList;
		// } else if (dateStatus.equalsIgnoreCase("year")) {
		// observationData = (observationList.size() > 12) ? observationList
		// .subList(0, 12) : observationList;
		// } else if (dateStatus.equalsIgnoreCase("All")
		// || dateStatus.equalsIgnoreCase("custom")) {
		//
		// observationData = observationList;
		//
		// }
		// observationData = observationList;
		if (observationList != null)
			noOfRecords = observationList.size();
		// Collections.reverse(observationData);

		if (observationList != null) {
			Collections.sort(observationList, new Comparator<Observation>() {
				@Override
				public int compare(Observation o1, Observation o2) {
					return o2.getId().compareTo(o1.getId());
				}
			});
		}

		List<ObservationDTO> observationDTOs = observationBuilder
				.ToDTOList(observationList);

		float totalObservations = 0;
		for (ObservationDTO dto : observationDTOs) {
			totalObservations = totalObservations + dto.getRating();
		}
		// float total = noOfRecords;

		// float average = totalObservations / total;

		// double doubleValue = (double) Math.round(average * 100) / 100;

		if (endIndex >= noOfRecords) {
			endIndex = noOfRecords;

		}
		observationDTOs = observationDTOs.subList(startIndex, endIndex);

		observMap = new HashMap<String, Object>();
		observMap.put("list", observationDTOs);
		observMap.put("size", noOfRecords);
		// observMap.put("average", doubleValue);
		observMap.put("average", Math.round(totalObservations / noOfRecords));
		return observMap;
	}

	@Override
	public List<PerformanceRatingsDTO> getAllPerformanceRatings() {
		List<PerformanceRatings> ratings = observationDAO
				.get(PerformanceRatings.class);

		if (ratings != null) {
			Collections.sort(ratings, new Comparator<PerformanceRatings>() {

				@Override
				public int compare(PerformanceRatings o1, PerformanceRatings o2) {
					return o2.getRating().compareTo(o1.getRating());
				}

			});
		}

		List<PerformanceRatingsDTO> dto = null;
		if (ratings != null) {
			dto = new ArrayList<PerformanceRatingsDTO>();
			for (PerformanceRatings performanceRatings : ratings) {
				PerformanceRatingsDTO performanceRatingsDTO = new PerformanceRatingsDTO();
				performanceRatingsDTO.setId(performanceRatings.getId());
				performanceRatingsDTO.setRating(performanceRatings.getRating());
				// performanceRatingsDTO.setLabel(performanceRatings.getLabel());
				dto.add(performanceRatingsDTO);
			}

		}
		return dto;

	}
}
