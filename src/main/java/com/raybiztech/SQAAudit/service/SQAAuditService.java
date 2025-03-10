package com.raybiztech.SQAAudit.service;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;

import com.raybiztech.SQAAudit.business.SQAAuditForm;
import com.raybiztech.SQAAudit.dto.SQAAuditFormDto;
import com.raybiztech.SQAAudit.dto.SQAAuditTimelineDto;

public interface SQAAuditService {

	void saveAuditForm(SQAAuditFormDto dto);

	Map<String, Object> getProjectAuditList(Long projectId, Integer startIndex, Integer endIndex);

	SQAAuditFormDto getAuditDetails(Long auditId);

	void submitAuditForm(SQAAuditFormDto dto);

	void updateAuditForm(SQAAuditFormDto dto);

	void uploadSQAAuditFiles(MultipartFile mpf, String parameter);

	void downloadSQAAuditFile(String fileName, HttpServletResponse response);

	void deleteProjectAuditDetails(Long auditId);
	
	// For Retriving SQA details whose follow-up end date is today
	
	List<SQAAuditForm> getAuditWhoseAuditStatusIsOpen();
	
	Map<String,Object> getSQAAuditReport(Integer startIndex,Integer endIndex,String multiSearch,String from,String to,String SQAAuditSelectionDate,
			String auditStatus,String auditRescheduleStatus);
	
	List<SQAAuditTimelineDto> getSQAAuditTimelineDetails(Long auditId);
	
	Map<String,Object> getNewSQAAuditTimelineDetails(Long auditId);

	void closeAudit(Long auditId);
	
	//exportSQAReport(SQAAuditSelectionDate,auditStatus,startdate,enddate,multiSearch)
	
	ByteArrayOutputStream exportSQAReport(
			String SQAAuditSelectionDate, String auditStatus,String auditRescheduleStatus,String startdate,
			String enddate, String multiSearch) throws Exception;
	
	ByteArrayOutputStream exportProjectAuditList(
			Long projectId ) throws Exception;
	
}
