package com.raybiztech.separation.dao;

import java.util.List;
import java.util.Map;

import com.raybiztech.TimeActivity.business.EmpoloyeeHiveActivity;
import com.raybiztech.appraisals.PIPManagement.business.PIP;
import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.dao.DAO;
import com.raybiztech.biometric.business.BioAttendance;
import com.raybiztech.date.Date;
import com.raybiztech.separation.business.ExitFeedBack;
import com.raybiztech.separation.business.PrimaryReason;
import com.raybiztech.separation.business.Separation;
import com.raybiztech.separation.business.SeparationComments;
import com.raybiztech.separation.business.SeparationStatus;

public interface SeparationDao extends DAO {

	public Map<String, Object> getResignationList(String multipleSearch, Integer startIndex, Integer endIndex,
			Date fromDate, Date toDate, String status,String empStatus);

	public Map<String, Object> getEmployeeResignationListUnderManager(List<Long> managersId, String multipleSearch,
			Integer startIndex, Integer endIndex, Date fromDate, Date toDate, String status, Employee employee);

	public Map<String, Object> getEmployeeResignationList(String multipleSearch, Integer startIndex, Integer endIndex,
			Date fromDate, Date toDate, Employee employee, String status);

	public ExitFeedBack checkExitFeedBackForm(Long separationId);

	Boolean isSepartionExists(Employee employee);

	public List<Separation> getRelivingEndDateEmployees();

	public Separation getEmployeeSepartion(Long employeeId);

	public Map<String, Object> getSeparationChartDetails(List<PrimaryReason> primaryReasons, Date from, Date to);

	public Map<String, Object> getInititateResignationList(String multiplesearch, Integer startIndex, Integer endIndex,
			Date fromDate, Date toDate, String status, Employee loggedEmployee);

	public Map<String, Object> getInititateResignationListUnderManager(Employee employee, String multiplesearch,
			Integer startIndex, Integer endIndex, Date fromDate, Date toDate, String status);
	
	public Employee getDeliveryManagerOfEmployee( Employee employee);

	public Boolean checkPIP(Long employeeId);

	public ExitFeedBack getEmployeePIPexitFeedBack(Long employeeId);

	public List<PIP> getEmployeePIPList(Long employeeId);
	
	Boolean isSepartionExistsForEmployee(Employee employee);
	
	public Separation getEmployeeSepartionForLeave(Long employeeId);	

	//public List<Long> AllEmpIds();
}
