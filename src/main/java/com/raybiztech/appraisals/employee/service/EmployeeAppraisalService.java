package com.raybiztech.appraisals.employee.service;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;

import com.raybiztech.appraisals.dto.AppraisalDTO;
import com.raybiztech.appraisals.dto.KPIRatingDTO;
import com.raybiztech.appraisals.dto.MyDTO;

public interface EmployeeAppraisalService {
	AppraisalDTO getEmployeeAppraisalData(Long employeeId);

	void saveKPIRating(MyDTO dto);
	
	void deleteKpiRating(KPIRatingDTO dto);
	
	void submitEmployeeAppraisal(Long employeeId);
	
	void acknowledgeAppraisal(Long employeeId);
	
	String uploadFile(MultipartFile mpf);
	
	void downloadFile(HttpServletResponse response, String fileName);
}
