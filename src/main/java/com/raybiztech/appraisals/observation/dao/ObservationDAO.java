package com.raybiztech.appraisals.observation.dao;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.dao.DAO;
import com.raybiztech.appraisals.observation.business.Observation;
import com.raybiztech.appraisals.observation.dto.ObservationDTO;
import com.raybiztech.appraisals.observation.dto.SearchObservationDTO;
import com.raybiztech.assetmanagement.business.AssetAudit;
import com.raybiztech.date.Date;
import com.raybiztech.date.DateRange;
import com.raybiztech.leavemanagement.business.LeaveDebit;

public interface ObservationDAO extends DAO {

	Set<Observation> getEmployeeObservationsListUnderManager(
			List<Long> managerIds);

	Map<String, Object> getEmployeeObservationsListUnderAdmin(
			Integer startIndex, Integer endIndex);

	List<Employee> managerReporties(Employee employee);

	Set<Observation> getEmployeeObservationsListUnderManager(List<Long> empids,
			SearchObservationDTO observation, Date fromDate, Date toDate);

	Map<String, Object> getEmployeeObservationsListUnderAdmin(
			Integer startIndex, Integer endIndex,
			SearchObservationDTO observation, Date fromDate, Date toDate);

	List<Observation> getAllEmployeesUnderAdmin();

	Map<String, Object> getAllMonthwiseObservation(Integer startIndex,
			Integer endIndex, Date fromDate, Date toDate, Long employeeId);

	List<Employee> getNonRatedEmployees(
			SearchObservationDTO searchObservationDTO);

	Boolean checkForDuplication(Observation observation);
	
	Map<String, Object> getEmployeeIndvisualObservations(
			Integer startIndex, Integer endIndex,
			SearchObservationDTO observation, Date minDate, Date maxDate);

}
