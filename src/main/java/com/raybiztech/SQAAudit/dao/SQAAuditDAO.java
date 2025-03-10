package com.raybiztech.SQAAudit.dao;

import java.util.List;
import java.util.Map;

import com.raybiztech.SQAAudit.business.SQAAuditForm;
import com.raybiztech.SQAAudit.business.SQAAuditTimeline;
import com.raybiztech.appraisals.dao.DAO;
import com.raybiztech.date.Date;
import com.raybiztech.projectmanagement.business.Audit;

public interface SQAAuditDAO extends DAO {

	Map<String, Object> getProjectAuditList(Long projectId, Integer startIndex, Integer endIndex);

	List<SQAAuditForm> getListOfAuditsOnSelectedDate(Date sqaAuditDate);

	Map<String, Object> getSubmittedProjectAuditList(Long projectId, Integer startIndex, Integer endIndex);
	
	Map<String ,Object> getSQAAuditsReports(Integer startIndex, Integer endIndex ,String multiSearch,Date fromDate, Date toDate,
			String auditStatus,String auditRescheduleStatus);
	
	Map<String ,Object> getSubmittedSQAAuditReport(Integer startIndex, Integer endIndex ,String multiSearch,Date fromDate, Date toDate,
			String auditStatus ,List<Long> managerIds ,String auditRescheduleStatus);

	List<SQAAuditTimeline> getSQAAuditTimelineDetails(Long auditId);
	
	Map<String , List<Audit>>  getAuditTimeLine(Long id);
	
	List<SQAAuditForm> getAuditWhoseAuditStatusIsOpen();


}
