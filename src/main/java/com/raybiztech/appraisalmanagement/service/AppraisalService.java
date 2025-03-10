package com.raybiztech.appraisalmanagement.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.springframework.web.bind.annotation.RequestParam;

import com.raybiztech.appraisalmanagement.business.Frequency;
import com.raybiztech.appraisalmanagement.dto.AppraisalCycleDto;
import com.raybiztech.appraisalmanagement.dto.AppraisalFormDto;
import com.raybiztech.appraisalmanagement.dto.AppraisalKPIDataDto;
import com.raybiztech.appraisalmanagement.dto.DesignationKRAMappingDto;
import com.raybiztech.appraisalmanagement.dto.KRADto;
import com.raybiztech.appraisalmanagement.dto.ReviewAuditDto;
import com.raybiztech.appraisalmanagement.dto.SearchQueryParamsInAppraisalForm;

public interface AppraisalService {

	void addCycle(AppraisalCycleDto appraisalCycleDto);

	List<AppraisalCycleDto> getAllAppraisalCycle();

	AppraisalFormDto getAppraisalForm(Long employeeId);

	void employeeAppraisalForm(AppraisalFormDto appraisalFormDto);

	void managerAppraisalForm(AppraisalFormDto appraisalFormDto);

	Map<String, Object> getCurrentForms(Integer startIndex, Integer endIndex,
			Long employeeID, Long cycleId);

	AppraisalCycleDto getCycle(Long cycleId);

	void updateCycle(AppraisalCycleDto appraisalCycleDto);

	AppraisalCycleDto getActiveCycleData();

	List<KRADto> getAllKrasUnderDesignation(Long cycleId, Long designationId);

	void validateCycle(AppraisalCycleDto appraisalCycleDto);

	void validateCycleRange(AppraisalCycleDto appraisalCycleDto);

	public Map<String, Object> searchEmployee(
			SearchQueryParamsInAppraisalForm paramsInAppraisalForm);

	/*
	 * public Map<String, Object> searchByManagerName(Integer startIndex,
	 * Integer endIndex, Long employeeID, Long cycleId,String searchString);
	 */

	Map<String, Object> getDesignationsUnderCycle(Integer startIndex,
			Integer endIndex, Long cycleId);

	Double employeeAppraisalFormForRating(AppraisalFormDto appraisalFormDto);

	AppraisalFormDto getExistingAppraisalForm(Long appraisalFormId);

	void closeAppraisalForm(AppraisalFormDto appraisalFormDto);

	void appraisalConfirmation(AppraisalFormDto appraisalFormDto);

	void copyTheCycleData(@RequestParam Long oldCycleId,
			@RequestParam Long newCycleId);

	public Boolean isAlreadyExist(Long newCycleId);

	public void requestDiscussion(Long appraisalFormId);

	public void saveReviewComments(String comments, Long appraisalFormId);

	public ByteArrayOutputStream exportAppraisalList(
			SearchQueryParamsInAppraisalForm paramsInAppraisalForm)
			throws IOException;

	Map<String, Object> getReviewComments(Long appriasalFormId);

	public void changedKPISRating(AppraisalFormDto kpis);

}
