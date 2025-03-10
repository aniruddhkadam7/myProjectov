package com.raybiztech.SQAAudit.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.raybiztech.SQAAudit.Exception.SQAAuditAlreadyExistsException;
import com.raybiztech.SQAAudit.builder.SQAAuditBuilder;
import com.raybiztech.SQAAudit.business.SQAAuditForm;
import com.raybiztech.SQAAudit.business.SQAAuditTimeline;
import com.raybiztech.SQAAudit.business.SQAAuditees;
import com.raybiztech.SQAAudit.business.SQAAuditors;
import com.raybiztech.SQAAudit.dao.SQAAuditDAO;
import com.raybiztech.SQAAudit.dto.SQAAuditFormDto;
import com.raybiztech.SQAAudit.dto.SQAAuditTimelineDto;
import com.raybiztech.SQAAudit.dto.SQAAuditeesDto;
import com.raybiztech.SQAAudit.dto.SQAAuditorsDto;
import com.raybiztech.SQAAudit.mailNotification.SQAAuditMailNotification;
import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.exception.FileUploaderUtilException;
import com.raybiztech.appraisals.properties.PropBean;
import com.raybiztech.appraisals.security.utils.SecurityUtils;
import com.raybiztech.appraisals.utils.FileUploaderUtil;
import com.raybiztech.date.Date;
import com.raybiztech.projectmanagement.builder.AuditBuilder;
import com.raybiztech.projectmanagement.business.Audit;
import com.raybiztech.projectmanagement.business.Project;
import com.raybiztech.projectmanagement.service.ProjectService;
import com.raybiztech.recruitment.utils.DateParser;
import com.raybiztech.recruitment.utils.FileUploaderUtility;
import com.raybiztech.rolefeature.business.Permission;
import com.raybiztech.utils.SecondParser;

@Service("sqaAuditServiceImpl")
@Transactional
public class SQAAuditServiceImpl implements SQAAuditService{
	
	@Autowired
	SecurityUtils securityUtils;
	
	@Autowired
	SQAAuditBuilder sqaAuditbuilder;
	
	@Autowired
	SQAAuditDAO sqaDaoImpl;
	
	@Autowired
	PropBean propBean;
	
	@Autowired
	SQAAuditMailNotification sqaAuditMailNotification;
	
	@Autowired
	ProjectService projectService;
	
	@Autowired
	AuditBuilder auditBuilder;
	
	
	

	@Override
	public void saveAuditForm(SQAAuditFormDto dto) {
		
		SQAAuditForm form1 = new SQAAuditForm(); 
		Long id = (Long) sqaDaoImpl.save(sqaAuditbuilder.toEntity(dto));
		SQAAuditForm form = sqaDaoImpl.findBy(SQAAuditForm.class, id);
	}

	@Override
	public Map<String, Object> getProjectAuditList(Long projectId, Integer startIndex, Integer endIndex) {
		
		Employee loggedInemployee = (Employee) securityUtils.getLoggedEmployeeDetailsSecurityContextHolder().get("employee");
		Map<String, Object> auditMap = new HashMap<>();
		
		
		Permission totalList = sqaDaoImpl.checkForPermission("TotalProjectSQAAuditList", loggedInemployee);
		
		if(totalList.getView()) {
			 auditMap = sqaDaoImpl.getProjectAuditList(projectId, startIndex, endIndex);
		}
		else {
			 auditMap = sqaDaoImpl.getSubmittedProjectAuditList(projectId, startIndex, endIndex);
		}
		
		
		List<SQAAuditForm> auditList = (List<SQAAuditForm>) auditMap.get("list");
		List<SQAAuditFormDto> dtoList = sqaAuditbuilder.toDTOList(auditList);
		Map<String, Object> map = new HashMap<>();
		map.put("list", dtoList);
		map.put("size", auditMap.get("size"));
		return map;
	}

	@Override
	public SQAAuditFormDto getAuditDetails(Long auditId) {
		SQAAuditForm form = sqaDaoImpl.findBy(SQAAuditForm.class, auditId);
		return sqaAuditbuilder.toDto(form);
	}

	@Override
	public void submitAuditForm(SQAAuditFormDto dto) {
		
		Date sqaAuditDate = null;
		try {
		 sqaAuditDate = DateParser.toDate(dto.getAuditDate());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		String start[] = dto.getStartTime().split("/");
		String end[] = dto.getEndTime().split("/");
		int startTimeMilliSeconds = Integer.parseInt(start[3]) * 3600000
				+ Integer.parseInt(start[4]) * 60000;
		int endTimeMilliSeconds = Integer.parseInt(end[3]) * 3600000
				+ Integer.parseInt(end[4]) * 60000;
		
		
		List<SQAAuditForm> auditList = sqaDaoImpl.getListOfAuditsOnSelectedDate(sqaAuditDate);
		Boolean auditAlreadyExists = false;
		if(auditList.size() != 0) {
			
			for(SQAAuditForm audit : auditList) {
				int auditStartTime = audit.getStartTime().getHourOfDay().getValue()*3600000+
						audit.getStartTime().getMinuteOfHour().getValue()*60000;
				int auditEndtime = audit.getEndTime().getHourOfDay().getValue()*3600000+
						audit.getEndTime().getMinuteOfHour().getValue()*60000;
				
				if(dto.getId() != null){
					if(!dto.getId().equals(audit.getId()) && ((startTimeMilliSeconds >= auditStartTime && startTimeMilliSeconds < auditEndtime)
							|| (endTimeMilliSeconds > auditStartTime && endTimeMilliSeconds <= auditEndtime) ||
							(startTimeMilliSeconds <= auditStartTime && endTimeMilliSeconds >= auditEndtime))) {
						auditAlreadyExists = true;
						break;
				}
				}
				
				else if((startTimeMilliSeconds >= auditStartTime && startTimeMilliSeconds < auditEndtime)
							|| (endTimeMilliSeconds > auditStartTime && endTimeMilliSeconds <= auditEndtime) ||
							(startTimeMilliSeconds <= auditStartTime && endTimeMilliSeconds >= auditEndtime)) {
						auditAlreadyExists = true;
						break;
					}
				
			}
			
			if(!auditAlreadyExists) {
				saveOrSubmitAuditForm(dto);
			}
			else {
				throw new SQAAuditAlreadyExistsException("auditAlreadyExists");
			}
		}
		else {
			saveOrSubmitAuditForm(dto);
		}
		
		
	}

	public void saveOrSubmitAuditForm(SQAAuditFormDto dto) {
		Logger logger = Logger.getLogger(SQAAuditServiceImpl.class);
		
		if(dto.getId() !=null) {
			
			SQAAuditForm form = sqaDaoImpl.findBy(SQAAuditForm.class, dto.getId());
			
			
			SQAAuditForm oldForm = null;
			try {
				 oldForm =(SQAAuditForm) form.clone();
				 // For Deep Cloning for Mutable object "Auditors"
				 Set<SQAAuditors> oldAuditors1 = form.getAuditors();
				 Set<SQAAuditors> oldAuditors2 = new HashSet<SQAAuditors>();
				for(SQAAuditors auditors: oldAuditors1){
					oldAuditors2.add( (SQAAuditors) auditors.clone());
				}
				oldForm.auditors = oldAuditors2;
				 // For Deep Cloning for Mutable object "Auditors"
				 Set<SQAAuditees> oldAuditees1 = form.getAuditees();
				 Set<SQAAuditees> oldAuditees2= new HashSet<SQAAuditees>();
				for(SQAAuditees auditees: oldAuditees1){
					oldAuditees2.add((SQAAuditees) auditees.clone());
				}
				oldForm.auditors = oldAuditors2;
				oldForm.auditees = oldAuditees2;
				
			} catch (CloneNotSupportedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			form = sqaAuditbuilder.toEntity(dto);
			sqaDaoImpl.update(form);
			
			List<Audit> audits = (List<Audit>) auditBuilder.updateAuditToSQAAudit(form, form.getId(), oldForm, "SQAAuditForm", "Updated");
			
			 for(Audit audit:audits)
			    {
				 sqaDaoImpl.save(audit);
			    }
			
			SQAAuditTimeline audit = sqaAuditbuilder.toSQAAuditTimelineEntity(dto.getId(),"Updated");
			sqaDaoImpl.save(audit);
			
			sqaAuditMailNotification.sendSQAAuditScheduleOrRescheduleMail(form.getId());
			
			
		}
		else {
			
			//SQAAuditForm form = sqaDaoImpl.findBy(SQAAuditForm.class, dto.getId());
			//System.out.println("in else save");
			SQAAuditForm form = new SQAAuditForm();
			form = sqaAuditbuilder.toEntity(dto);
			Long id = (Long) sqaDaoImpl.save(form);
			List<Audit> audits = (List<Audit>) auditBuilder.createAuditToSQAAudit(form, id, "SQAAuditForm", "Created"); 
		    for(Audit sqa:audits)
		    {  	
		    	sqaDaoImpl.save(sqa);
		    }
		    
		    SQAAuditTimeline audit = sqaAuditbuilder.toSQAAuditTimelineEntity(id,"Created");
			
			Long id1 =(Long)sqaDaoImpl.save(audit);
			
			if(dto.getFormStatus().equalsIgnoreCase("Submit")) {
				sqaAuditMailNotification.sendSQAAuditScheduleOrRescheduleMail(id);
			}
		}
				
		}

	@Override
	public void updateAuditForm(SQAAuditFormDto dto) {
		
		Logger logger = Logger.getLogger(SQAAuditServiceImpl.class);
		if(dto != null) {
			SQAAuditForm form = sqaDaoImpl.findBy(SQAAuditForm.class, dto.getId());
			
			logger.warn("in method");
			Employee loggedInEmployee = (Employee) securityUtils.getLoggedEmployeeDetailsSecurityContextHolder().get("employee");
			
			SQAAuditForm oldForm = null;
			try {
				 oldForm =(SQAAuditForm) form.clone();
				 // For Deep Cloning for Mutable object "Auditors"
				
				 Set<SQAAuditors> oldAuditors1 = form.getAuditors();
				 Set<SQAAuditors> oldAuditors2 = new HashSet<SQAAuditors>();
				for(SQAAuditors auditors: oldAuditors1){
					oldAuditors2.add( (SQAAuditors) auditors.clone());
				}
				oldForm.auditors = oldAuditors2;
				
				 // For Deep Cloning for Mutable object "Auditors"
				 Set<SQAAuditees> oldAuditees1 = form.getAuditees();
				 Set<SQAAuditees> oldAuditees2= new HashSet<SQAAuditees>();
				for(SQAAuditees auditees: oldAuditees1){
					oldAuditees2.add((SQAAuditees) auditees.clone());
				}
				oldForm.auditors = oldAuditors2;
				oldForm.auditees = oldAuditees2;
				
			} catch (CloneNotSupportedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// Removing and adding Auditor and Auditees
				logger.warn("Auditors = "+form.getAuditors()+" Auditees = "+ form.getAuditees());
				form.getAuditors().clear();
				form.getAuditees().clear();
				Set<Long> auditors = dto.getAuditorIds();
				Set<SQAAuditors> auditorList = new HashSet<>();
				SQAAuditors auditor = null;
				for (Long empId : auditors) {
					if (auditors != null) {
						auditor = new SQAAuditors();
						Employee emp = sqaDaoImpl.findBy(Employee.class, empId);
						auditor.setAuditor(emp);
						auditorList.add(auditor);
					}
				}

				form.getAuditors().addAll(auditorList);
				Set<Long> auditees = dto.getAuditeeIds();
				Set<SQAAuditees> auditeesList = new HashSet<>();
				SQAAuditees auditee = null;
				for (Long empId : auditees) {
					if (auditees != null) {
						auditee = new SQAAuditees();
						Employee emp = sqaDaoImpl.findBy(Employee.class, empId);
						auditee.setAuditee(emp);
						auditeesList.add(auditee);
					}
				}

				form.getAuditees().addAll(auditeesList);
				
				// End of 
			
			if(loggedInEmployee.getRole().contains("SQA")){
				form.setPci(dto.getPci() != null ? dto.getPci() : null);
				try {
					if(dto.getFollowUpDate() != null){
					form.setFollowUpDate(DateParser.toDate(dto.getFollowUpDate()));
					}
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(dto.getComments() != null) {
				form.setSqaComments(dto.getComments());
				}
				form.setFormStatus("SQA Update");
			}else{
				if(dto.getComments() != null) {
				form.setPmComments(dto.getComments());
				}
				form.setFormStatus("PM Update");
			}
			
			//form.setFormStatus(dto.getFormStatus());
			form.setContainsFile(dto.getContainsFile()?Boolean.TRUE: Boolean.FALSE);
			form.setUpdatedBy(loggedInEmployee);
			form.setUpdatedDate(new Date());
			
			if(dto.getEndTime() != null){
				form.setEndTime(SecondParser.toSecond(dto.getEndTime()));
			}
			if(dto.getSqaFilesPath() != null){
				logger.warn("in Sqa file path");
				form.setSqaFilesPath(dto.getSqaFilesPath());
				String filePath = dto.getSqaFilesPath();
				String fileName = filePath.substring(filePath.lastIndexOf("/") + 1);
			}
			if(dto.getPmFilesPath() != null){
				logger.warn("in pm file path");
				form.setPmFilesPath(dto.getPmFilesPath());
				String filePath = dto.getPmFilesPath();
				String fileName = filePath.substring(filePath.lastIndexOf("/") + 1);
			}
			
			sqaDaoImpl.update(form);
			SQAAuditTimeline audit = sqaAuditbuilder.toSQAAuditTimelineEntity(form.getId(),"Updated");
			sqaDaoImpl.save(audit);
			
			List<Audit> audits=	auditBuilder.updateAuditToSQAAudit(form,form.getId(), oldForm, "SQAAuditForm", "UPDATED");
			
			for(Audit audit1:audits)
			{
				sqaDaoImpl.save(audit1);
			}
			if(!dto.getContainsFile()) {
				if(loggedInEmployee.getRole().contains("SQA")){
					if(form.getPmComments() != null || form.getPmFilesPath() != null){
						sqaAuditMailNotification.sendFollowUpAuditStatusReportToManager(form.getId());
					}
					else{
						sqaAuditMailNotification.sendAuditReportToManager(form.getId());
					}
					
				}else{
					sqaAuditMailNotification.sendFollowupAuditReportToSQATeam(form.getId());
				}
			}
		}
	}

	@Override
	public void uploadSQAAuditFiles(MultipartFile mpf, String auditId) {
		if(auditId != null) {
			SQAAuditForm form = sqaDaoImpl.findBy(SQAAuditForm.class, Long.parseLong(auditId));
			SQAAuditForm oldForm = null;
			// Cloning obj 
			try{
				oldForm = (SQAAuditForm) form.clone();
			}
			catch(CloneNotSupportedException e){
				e.printStackTrace();
			}
			
			FileUploaderUtility fileUploaderUtility = new FileUploaderUtility();
			String path = null;
			try {
				path = fileUploaderUtility.uploadSQAAuditFiles(form,mpf,propBean);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if(path != null) {
				/*if(status.equalsIgnoreCase("SQA Update")) {
					form.setSqaFilesPath(path);
				}
				else if(status.equalsIgnoreCase("PM Update")) {
					form.setPmFilesPath(path);
				}*/
				Employee loggedInEmployee = (Employee) securityUtils.getLoggedEmployeeDetailsSecurityContextHolder().get("employee");
				
				if(loggedInEmployee.getRole().contains("SQA")){
					form.setSqaFilesPath(path);
				}else{
					form.setPmFilesPath(path);
				}
				
			sqaDaoImpl.update(form);
			
			
			SQAAuditTimeline audit = sqaAuditbuilder.toSQAAuditTimelineEntity(Long.parseLong(auditId),"Updated");
			sqaDaoImpl.save(audit);
			
			List<Audit> audits = (List<Audit>) auditBuilder.updateAuditToSQAAudit(form, form.getId(), oldForm, "SQAAuditForm", "Updated");
			
			 for(Audit audit1:audits)
			    {
				 sqaDaoImpl.save(audit1);
			    }
			
			/*if(status.equalsIgnoreCase("SQA Update")) {
				sqaAuditMailNotification.sendAuditReportToManager(form.getId());
			}
			else if(status.equalsIgnoreCase("PM Update")) {
				sqaAuditMailNotification.sendFollowupAuditReportToSQATeam(form.getId());
			}*/
			
			if(loggedInEmployee.getRole().contains("SQA")){
				if(form.getPmComments() != null || form.getPmFilesPath() != null){
					System.out.println("In IF PM comment or Path founded");
					sqaAuditMailNotification.sendFollowUpAuditStatusReportToManager(form.getId());
				}
				else{
					sqaAuditMailNotification.sendAuditReportToManager(form.getId());
				}
			}else{
				sqaAuditMailNotification.sendFollowupAuditReportToSQATeam(form.getId());
			}
			
			}
		}
		
	}

	@Override
	public void downloadSQAAuditFile(String fileName, HttpServletResponse response) {
		
		FileUploaderUtil util = new FileUploaderUtil();
		try {
			util.downloadSQAAuditFile(response, fileName, propBean);
		} catch (Exception e) {
			throw new FileUploaderUtilException("Exception occured while downloading a file"
						+ e.getMessage(),e);
		}
		
		
	}

	@Override
	public void deleteProjectAuditDetails(Long auditId) {
		sqaDaoImpl.delete(sqaDaoImpl.findBy(SQAAuditForm.class, auditId));
		
	}

	@Override
	public Map<String, Object> getSQAAuditReport(Integer startIndex,Integer endIndex,String multiSearch,String from,String to,String SQAAuditSelectionDate,
			String auditStatus,String auditRescheduleStatus) {
		
		Date fromDate = null;
		Date toDate = null;
		if (SQAAuditSelectionDate.equalsIgnoreCase("Custom")) {
			try {
				fromDate = DateParser.toDate(from);
				toDate = DateParser.toDate(to);
			} catch (ParseException e) {
				e.printStackTrace();
			}

		}

		else {
			Map<String, Date> dateMap = sqaDaoImpl
					.getCustomDates(SQAAuditSelectionDate);
			fromDate = dateMap.get("startDate");
			toDate = dateMap.get("endDate");
		}

		
		
		Employee loggedInemployee = (Employee) securityUtils.getLoggedEmployeeDetailsSecurityContextHolder().get("employee");
		Map<String, Object> auditMap = new HashMap<>();
		
		String loggedInemployeeName = null;
		//System.out.println("condition for "+loggedInemployee.getRole().equalsIgnoreCase("Manager"));
		
		//System.out.println("role"+loggedInemployee.getRole());
		
		
		//System.out.println("loggedInemployeeName" +loggedInemployee.getFullName());
		
		
     Permission totalList = sqaDaoImpl.checkForPermission("TotalProjectSQAAuditList", loggedInemployee);
		
		if(totalList.getView()) {
			//System.out.println("in if");
			 auditMap = sqaDaoImpl.getSQAAuditsReports(startIndex, endIndex,multiSearch,fromDate,toDate,auditStatus,auditRescheduleStatus);
		}
		else {
			//System.out.println("in else");
			List<Long> managerIds = new ArrayList<Long>();
			managerIds.add(loggedInemployee.getEmployeeId());
			
			managerIds.addAll(projectService.mangerUnderManager(loggedInemployee.getEmployeeId()));
				
		auditMap = sqaDaoImpl.getSubmittedSQAAuditReport(startIndex, endIndex,multiSearch,fromDate,toDate,auditStatus,managerIds,auditRescheduleStatus);
		
		/*for(Long id : managerIds){
			System.out.println("id" +id);
		}*/
			
			/*if(loggedInemployee.getEmpRole().getName().contains("Manager")){
				loggedInemployeeName = loggedInemployee.getFullName();
			 auditMap = sqaDaoImpl.getSubmittedSQAAuditReport(startIndex, endIndex,multiSearch,fromDate,toDate,auditStatus,loggedInemployeeName);
			}*/
		}
		
		
		/*if(loggedInemployee.getEmpRole().getName().contains("Manager")){
			loggedInemployeeName = loggedInemployee.getFullName();
		 auditMap = sqaDaoImpl.getSubmittedSQAAuditReport(startIndex, endIndex,multiSearch,fromDate,toDate,auditStatus,loggedInemployeeName);
		}else{
		auditMap = sqaDaoImpl.getSQAAuditsReports(startIndex, endIndex,multiSearch,fromDate,toDate,auditStatus);
		}
		*/
		
		List<SQAAuditForm> auditList = (List<SQAAuditForm>) auditMap.get("list");
		List<SQAAuditFormDto> dtoList = sqaAuditbuilder.toDTOList(auditList);
		Map<String, Object> map = new HashMap<>();
		map.put("list", dtoList);
		map.put("size", auditMap.get("size"));
		return map;
	}

	@Override
	public List<SQAAuditTimelineDto> getSQAAuditTimelineDetails(Long auditId) {
		List<SQAAuditTimeline> auditList = sqaDaoImpl.getSQAAuditTimelineDetails(auditId);
		return sqaAuditbuilder.toSQATimelineDtoList(auditList);
	}

	@Override
	public ByteArrayOutputStream exportSQAReport(String SQAAuditSelectionDate,
			String auditStatus,String auditRescheduleStatus, String startdate, String enddate,
			String multiSearch) throws Exception {
		
		Date fromDate = null;
		Date toDate = null;
		if (SQAAuditSelectionDate.equalsIgnoreCase("Custom")) {
			try {
				fromDate = DateParser.toDate(startdate);
				toDate = DateParser.toDate(enddate);
			} catch (ParseException e) {
				e.printStackTrace();
			}

		}

		else {
			Map<String, Date> dateMap = sqaDaoImpl
					.getCustomDates(SQAAuditSelectionDate);
			fromDate = dateMap.get("startDate");
			toDate = dateMap.get("endDate");
		}


	
		
		Employee loggedInemployee = (Employee) securityUtils.getLoggedEmployeeDetailsSecurityContextHolder().get("employee");
		Map<String, Object> auditMap = new HashMap<>();
		
		String loggedInemployeeName = null;
		  Permission totalList = sqaDaoImpl.checkForPermission("TotalProjectSQAAuditList", loggedInemployee);
			
			if(totalList.getView()) {
				 auditMap = sqaDaoImpl.getSQAAuditsReports(null, null,multiSearch,fromDate,toDate,auditStatus,auditRescheduleStatus);
			}
			else {
				List<Long> managerIds = new ArrayList<Long>();
				managerIds.add(loggedInemployee.getEmployeeId());
				
				managerIds.addAll(projectService.mangerUnderManager(loggedInemployee.getEmployeeId()));
					
			auditMap = sqaDaoImpl.getSubmittedSQAAuditReport(null, null,multiSearch,fromDate,toDate,auditStatus,managerIds,auditRescheduleStatus);
			}
			
		
		
		List<SQAAuditForm> auditList = (List<SQAAuditForm>) auditMap.get("list");
		List<SQAAuditFormDto> dtoList = sqaAuditbuilder.toDTOList(auditList);
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream();

		int rowIndex = 1;
		Workbook workbook = new HSSFWorkbook();
		Sheet sheet = workbook.createSheet();
		Row row1 = sheet.createRow(0);
		Font font = workbook.createFont();
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		font.setFontHeightInPoints((short) 10);
		CellStyle style = workbook.createCellStyle();

		style.setAlignment(CellStyle.ALIGN_CENTER);
		style.setFont(font);

		Cell cell0 = row1.createCell(0);
		cell0.setCellValue("Audit Type ");
		cell0.setCellStyle(style);
		
		Cell cell1 = row1.createCell(1);
		cell1.setCellValue("Project Name");
		cell1.setCellStyle(style);
		
		Cell cell2 = row1.createCell(2);
		cell2.setCellValue("Project Manager");
		cell2.setCellStyle(style);

		Cell cell3 = row1.createCell(3);
		cell3.setCellValue("Audit Date");
		cell3.setCellStyle(style);

		Cell cell4 = row1.createCell(4);
		cell4.setCellValue("Audit Start Time");
		cell4.setCellStyle(style);

		Cell cell5 = row1.createCell(5);
		cell5.setCellValue("Audit End Time");
		cell5.setCellStyle(style);

		Cell cell6 = row1.createCell(6);
		cell6.setCellValue("Auditors");
		cell6.setCellStyle(style);

		Cell cell7 = row1.createCell(7);
		cell7.setCellValue("Auditees");
		cell7.setCellStyle(style);
		
		Cell cell8 = row1.createCell(8);
		cell8.setCellValue("PCI(%)");
		cell8.setCellStyle(style);

		Cell cell9 = row1.createCell(9);
		cell9.setCellValue("Follow-up Date");
		cell9.setCellStyle(style);

		Cell cell10 = row1.createCell(10);
		cell10.setCellValue("Status");
		cell10.setCellStyle(style);

		Cell cell11 = row1.createCell(11);
		cell11.setCellValue("Audit Rescheduled");
		cell11.setCellStyle(style);

		for (SQAAuditFormDto dto : dtoList) {

			Row row = sheet.createRow(rowIndex++);

			Cell cel0 = row.createCell(0);
			cel0.setCellValue(dto.getAuditType());
			
			if(dto.getProjectId() != null){
				
				Project project = sqaDaoImpl.findBy(Project.class, dto.getProjectId());
				
				Cell cel1 = row.createCell(1);
				cel1.setCellValue(project.getProjectName());
				
				Cell cel2 = row.createCell(2);
				cel2.setCellValue(project.getProjectManager().getFullName());
				
			}else{
				Cell cel1 = row.createCell(1);
				cel1.setCellValue(dto.getProjectName());
				
				Cell cel2 = row.createCell(2);
				cel2.setCellValue(dto.getProjectManager());
			}

			Cell cel3 = row.createCell(3);
			cel3.setCellValue(dto.getAuditDate());


			Cell cel4 = row.createCell(4);
			cel4.setCellValue(dto.getStartTime());

			Cell cel5 = row.createCell(5);
			cel5.setCellValue(dto.getEndTime());
			
			Set<SQAAuditorsDto> auditors = dto.getAuditors();
			String auditorsList = null;
			if(auditors != null) {
				Boolean flag = true;
			for(SQAAuditorsDto auditor : auditors) {
				if(flag) {
				auditorsList = auditor.getFullName();
				flag = false;
				}
				else {
					auditorsList = auditorsList + " , " + auditor.getFullName();
				}
			}
			
		}

			Cell cel6 = row.createCell(6);
			cel6.setCellValue(auditorsList);
			
			Set<SQAAuditeesDto> auditees = dto.getAuditees();
			String auditeesList = null;
			if(auditees != null) {
				Boolean flag = true;
			for(SQAAuditeesDto auditee : auditees) {
				if(flag) {
					auditeesList = auditee.getFullName();
				flag = false;
				}
				else {
					auditeesList = auditeesList + " , " + auditee.getFullName();
				}
			}
			
		}

			Cell cel7 = row.createCell(7);
			cel7.setCellValue(auditeesList);
			
			Cell cel8 = row.createCell(8);
			cel8.setCellValue(dto.getPci() != null ? dto.getPci() : "N/A");

			Cell cel9 = row.createCell(9);
			cel9.setCellValue(dto.getFollowUpDate() != null ? dto.getFollowUpDate() : "N/A");
			
			Cell cel10 = row.createCell(10);
			cel10.setCellValue(dto.getAuditStatus());
			
           if(dto.getAuditRescheduleStatus().equals(Boolean.TRUE)){
			Cell cel11 = row.createCell(11);
			cel11.setCellValue("Yes");
           }else{
        	   Cell cel11 = row.createCell(11);
   			cel11.setCellValue("No");
           }


			sheet.autoSizeColumn(0);
			sheet.autoSizeColumn(1);
			sheet.autoSizeColumn(2);
			sheet.autoSizeColumn(3);
			sheet.autoSizeColumn(4);
			sheet.autoSizeColumn(5);
			sheet.autoSizeColumn(6);
			sheet.autoSizeColumn(7);
			sheet.autoSizeColumn(8);
			sheet.autoSizeColumn(9);
			sheet.autoSizeColumn(10);
			sheet.autoSizeColumn(11);
		}

		workbook.write(bos);
		bos.flush();
		bos.close();
		// for exporting code end here
		return bos;
		
	}

	public void closeAudit(Long auditId) {
		if(auditId != null) {
			SQAAuditForm form = sqaDaoImpl.findBy(SQAAuditForm.class, auditId);
			SQAAuditForm oldForm = null;
			try {
				 oldForm =(SQAAuditForm) form.clone();
			}
			catch(CloneNotSupportedException e){
				e.printStackTrace();
			}
			
			form.setAuditStatus("Closed");
			sqaDaoImpl.update(form);
			SQAAuditTimeline audit = sqaAuditbuilder.toSQAAuditTimelineEntity(form.getId(),"Updated");
			sqaDaoImpl.save(audit);
			List<Audit> audits=	auditBuilder.updateAuditToSQAAudit(form,form.getId(), oldForm, "SQAAuditForm", "UPDATED");
			
			for(Audit audit1:audits)
			{
				sqaDaoImpl.save(audit1);
			}
			sqaAuditMailNotification.sendAuditClosureMail(auditId);
			}
		}

	@Override
	public ByteArrayOutputStream exportProjectAuditList(Long projectId)
			throws Exception {
		
		Employee loggedInemployee = (Employee) securityUtils.getLoggedEmployeeDetailsSecurityContextHolder().get("employee");
		Map<String, Object> auditMap = new HashMap<>();
		
		
		Permission totalList = sqaDaoImpl.checkForPermission("TotalProjectSQAAuditList", loggedInemployee);
		
		if(totalList.getView()) {
			 auditMap = sqaDaoImpl.getProjectAuditList(projectId, null, null);
		}
		else {
			 auditMap = sqaDaoImpl.getSubmittedProjectAuditList(projectId, null, null);
		}
		
		
		List<SQAAuditForm> auditList = (List<SQAAuditForm>) auditMap.get("list");
		List<SQAAuditFormDto> dtoList = sqaAuditbuilder.toDTOList(auditList);
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream();

		int rowIndex = 1;
		Workbook workbook = new HSSFWorkbook();
		Sheet sheet = workbook.createSheet();
		Row row1 = sheet.createRow(0);
		Font font = workbook.createFont();
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		font.setFontHeightInPoints((short) 10);
		CellStyle style = workbook.createCellStyle();

		style.setAlignment(CellStyle.ALIGN_CENTER);
		style.setFont(font);

		Cell cell0 = row1.createCell(0);
		cell0.setCellValue("Audit Type ");
		cell0.setCellStyle(style);
		
		Cell cell1 = row1.createCell(1);
		cell1.setCellValue("Project Name");
		cell1.setCellStyle(style);
		
		Cell cell2 = row1.createCell(2);
		cell2.setCellValue("Project Manager");
		cell2.setCellStyle(style);

		Cell cell3 = row1.createCell(3);
		cell3.setCellValue("Audit Date");
		cell3.setCellStyle(style);

		Cell cell4 = row1.createCell(4);
		cell4.setCellValue("Audit Start Time");
		cell4.setCellStyle(style);

		Cell cell5 = row1.createCell(5);
		cell5.setCellValue("Audit End Time");
		cell5.setCellStyle(style);

		Cell cell6 = row1.createCell(6);
		cell6.setCellValue("Auditors");
		cell6.setCellStyle(style);

		Cell cell7 = row1.createCell(7);
		cell7.setCellValue("Auditees");
		cell7.setCellStyle(style);
		
		Cell cell8 = row1.createCell(8);
		cell8.setCellValue("PCI(%)");
		cell8.setCellStyle(style);

		Cell cell9 = row1.createCell(9);
		cell9.setCellValue("Follow-up Date");
		cell9.setCellStyle(style);

		Cell cell10 = row1.createCell(10);
		cell10.setCellValue("Status");
		cell10.setCellStyle(style);

		Cell cell11 = row1.createCell(11);
		cell11.setCellValue("Audit Rescheduled");
		cell11.setCellStyle(style);

		for (SQAAuditFormDto dto : dtoList) {

			Row row = sheet.createRow(rowIndex++);

			Cell cel0 = row.createCell(0);
			cel0.setCellValue(dto.getAuditType());
			
			if(dto.getProjectId() != null){
				
				Project project = sqaDaoImpl.findBy(Project.class, dto.getProjectId());
				
				Cell cel1 = row.createCell(1);
				cel1.setCellValue(project.getProjectName());
				
				Cell cel2 = row.createCell(2);
				cel2.setCellValue(project.getProjectManager().getFullName());
				
			}else{
				Cell cel1 = row.createCell(1);
				cel1.setCellValue(dto.getProjectName());
				
				Cell cel2 = row.createCell(2);
				cel2.setCellValue(dto.getProjectManager());
			}

			Cell cel3 = row.createCell(3);
			cel3.setCellValue(dto.getAuditDate());


			Cell cel4 = row.createCell(4);
			cel4.setCellValue(dto.getStartTime());

			Cell cel5 = row.createCell(5);
			cel5.setCellValue(dto.getEndTime());
			
			Set<SQAAuditorsDto> auditors = dto.getAuditors();
			String auditorsList = null;
			if(auditors != null) {
				Boolean flag = true;
			for(SQAAuditorsDto auditor : auditors) {
				if(flag) {
				auditorsList = auditor.getFullName();
				flag = false;
				}
				else {
					auditorsList = auditorsList + " , " + auditor.getFullName();
				}
			}
			
		}

			Cell cel6 = row.createCell(6);
			cel6.setCellValue(auditorsList);
			
			Set<SQAAuditeesDto> auditees = dto.getAuditees();
			String auditeesList = null;
			if(auditees != null) {
				Boolean flag = true;
			for(SQAAuditeesDto auditee : auditees) {
				if(flag) {
					auditeesList = auditee.getFullName();
				flag = false;
				}
				else {
					auditeesList = auditeesList + " , " + auditee.getFullName();
				}
			}
			
		}

			Cell cel7 = row.createCell(7);
			cel7.setCellValue(auditeesList);
			
			Cell cel8 = row.createCell(8);
			cel8.setCellValue(dto.getPci() != null ? dto.getPci() : "N/A");

			Cell cel9 = row.createCell(9);
			cel9.setCellValue(dto.getFollowUpDate() != null ? dto.getFollowUpDate() : "N/A");
			
			Cell cel10 = row.createCell(10);
			cel10.setCellValue(dto.getAuditStatus());
			
           if(dto.getAuditRescheduleStatus().equals(Boolean.TRUE)){
			Cell cel11 = row.createCell(11);
			cel11.setCellValue("Yes");
           }else{
        	   Cell cel11 = row.createCell(11);
   			cel11.setCellValue("No");
           }


			sheet.autoSizeColumn(0);
			sheet.autoSizeColumn(1);
			sheet.autoSizeColumn(2);
			sheet.autoSizeColumn(3);
			sheet.autoSizeColumn(4);
			sheet.autoSizeColumn(5);
			sheet.autoSizeColumn(6);
			sheet.autoSizeColumn(7);
			sheet.autoSizeColumn(8);
			sheet.autoSizeColumn(9);
			sheet.autoSizeColumn(10);
			sheet.autoSizeColumn(11);
		}

		workbook.write(bos);
		bos.flush();
		bos.close();
		// for exporting code end here
		return bos;
		
	}
	// New SQA Time Line Report
	
	@Override
	public Map<String,Object>  getNewSQAAuditTimelineDetails(Long auditId) {
		/*
		 * List<SQAAuditTimeline> auditList =
		 * sqaDaoImpl.getSQAAuditTimelineDetails(auditId); return
		 * sqaAuditbuilder.toSQATimelineDtoList(auditList);
		 */
		
		  Map<String,List<Audit>>sqaAudit=sqaDaoImpl.getAuditTimeLine(auditId);
		 
		return sqaAuditbuilder.toSQATimelineDtoList(sqaAudit);
	}
		
	@Override
	public List<SQAAuditForm> getAuditWhoseAuditStatusIsOpen() {
		// TODO Auto-generated method stub
		return sqaDaoImpl.getAuditWhoseAuditStatusIsOpen();
	}

	
		

}
