package com.raybiztech.appraisalmanagement.dao;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestParam;

import com.raybiztech.appraisalmanagement.business.AppraisalCycle;
import com.raybiztech.appraisalmanagement.business.AppraisalForm;
import com.raybiztech.appraisalmanagement.business.AppraisalFormAvgRatings;
import com.raybiztech.appraisalmanagement.business.Designation;
import com.raybiztech.appraisalmanagement.business.DesignationKRAMapping;
import com.raybiztech.appraisalmanagement.business.KPI;
import com.raybiztech.appraisalmanagement.business.KRA;
import com.raybiztech.appraisalmanagement.business.ReviewAudit;
import com.raybiztech.appraisalmanagement.dto.SearchQueryParamsInAppraisalForm;
import com.raybiztech.appraisalmanagement.dto.SearchQueryParamsInKRA;
import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.dao.DAO;
import com.raybiztech.date.Date;

public interface AppraisalDao extends DAO {

	AppraisalCycle getcurrentYearAppraisalCycle();

	DesignationKRAMapping getAllKrasUnderDesignation(String designationName,
			String departmentName);

	AppraisalForm activeCycleApprailsalForm(Long employeeId);

	Map<String, Object> getAllAppraisalForm(Integer startIndex,
			Integer endIndex, Long cycleId);

	List<AppraisalCycle> getActiveCyclesOtharthan(AppraisalCycle appraisalCycle);

	List<AppraisalCycle> getActiveCycles();

	Map<String, Object> getManagerAppraisalForms(Integer startIndex,
			Integer endIndex, Long employeeID, Long cycleId);

	List<AppraisalCycle> getOverriddenCycles(AppraisalCycle appraisalCycle);

	DesignationKRAMapping getAllKrasUnderCyclewithDesignation(Long cycleId,
			Long designationId);

	List<AppraisalCycle> getCycles(String name);

	public List<AppraisalCycle> getOverriddenCycles(
			AppraisalCycle appraisalCycle, Long id);

	public Map<String, Object> getAllAppraisalFormByAdmin(
			SearchQueryParamsInAppraisalForm paramsInAppraisalForm,
			List<Long> appraisalIds);

	public Map<String, Object> getAllAppraisalFormByManager(
			SearchQueryParamsInAppraisalForm paramsInAppraisalForm,
			List<Long> listOfManagers,List<Long> appraisalIds);

	/*
	 * public Map<String, Object> searchByManagerName (Integer startIndex,
	 * Integer endIndex, Long employeeID, Long cycleId, String searchstring);
	 */

	public List<KPI> getAllKpiForIndividualKra(Long kraId);

	public List<Designation> getDesignationsOfDept(Long deptId);

	public Boolean isDuplicateKRA(String kraName, Long departmentId,
			Long designationId);

	public List<KRA> getDesignationWiseKRAs(Long departmentId,
			Long designationId);

	Map<String, Object> searchKRAData(
			SearchQueryParamsInKRA searchQueryParamsInKRA);

	Map<String, Object> getDesignationsUnderCycle(Integer startIndex,
			Integer endIndex, Long cycleId);

	Map<String, Object> getEmployeeAppraisalForms(
			SearchQueryParamsInAppraisalForm paramsInAppraisalForm);

	Double getDesignationKraPercentage(Long departmentId, Long designationId);

	public List<KRA> krasForIndividual(Employee employee);

	void copyTheCycleData(@RequestParam Long oldCycleId,
			@RequestParam Long newCycleId);

	public Boolean getAppraisalCycleList(Long newCycleId);

	List<ReviewAudit> getReviewComments(Long appriasalFormId);

	List<Long> appraisalId(SearchQueryParamsInAppraisalForm appraisal,
			Date fromDate, Date toDate);
	//method for getting Employees not submitted appraisal form.
	Map<String, Object> getNotSubmittedFormEmployees(SearchQueryParamsInAppraisalForm appraisal , List<Long> appraisalIds);
	
	List<AppraisalForm> getLoggedInEmployeeAppraisalForm(Long employeeId,List<Long> cycleIds);
	List<Long>getAppraisalCycleIds();
	
}
