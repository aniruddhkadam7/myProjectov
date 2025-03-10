package com.raybiztech.appraisals.observation.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.raybiztech.appraisals.observation.dto.ObservationDTO;
import com.raybiztech.appraisals.observation.dto.PerformanceRatingsDTO;
import com.raybiztech.appraisals.observation.dto.SearchObservationDTO;
import com.raybiztech.projectmanagement.dto.ReportDTO;

public interface ObservationService {

	Long addObservation(ObservationDTO observationDTO);

	void updateObservation(ObservationDTO observationDTO);

	void deleteObservation(Long id);

	Map<String, Object> ObservationOfEmployee(Long id, Integer startIndex,
			Integer endIndex);

	List<ReportDTO> getActiveEmployees(Long id);

	Map<String, Object> ObservationOfReport(Long empId, Integer startIndex,
			Integer endIndex);

	Map<String, Object> searchObservation(SearchObservationDTO observationDTO,
			Long empId, Integer startIndex, Integer endIndex);

	void downloadFile(HttpServletResponse response, String fileName);

	Map<String, Object> getObservationHistory(String dateStatus,
			Integer startIndex, Integer endIndex, Long emid, Integer fromMonth,
			Integer fromYear, Integer toMonth, Integer toYear);

	List<PerformanceRatingsDTO> getAllPerformanceRatings();

}
