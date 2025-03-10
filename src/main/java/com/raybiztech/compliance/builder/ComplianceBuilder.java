package com.raybiztech.compliance.builder;


import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import com.raybiztech.appraisals.business.EmpDepartment;
import com.raybiztech.appraisals.dao.DAO;
import com.raybiztech.appraisals.security.utils.SecurityUtils;
import com.raybiztech.compliance.business.Compliance;
import com.raybiztech.compliance.business.Recurring;
import com.raybiztech.compliance.business.ComplianceTask;
import com.raybiztech.compliance.business.ComplianceTaskStatus;
import com.raybiztech.compliance.business.ComplianceTaskSubmit;
import com.raybiztech.compliance.business.Priority;
import com.raybiztech.compliance.dto.ComplianceDTO;
import com.raybiztech.compliance.dto.ComplianceTaskDTO;
import com.raybiztech.compliance.dto.ComplianceTaskSubmitDTO;
import com.raybiztech.date.Calendar;
import com.raybiztech.date.Date;
import com.raybiztech.date.Second;
import com.raybiztech.recruitment.utils.DateParser;

@Component("complianceBuilder")
public class ComplianceBuilder {

	@Autowired
	DAO dao;
	@Autowired
	SecurityUtils securityUtils;
	
	//Returning Compliance for ComplianceDTO
	public Compliance toEntity(ComplianceDTO complianceDTO) {
		Compliance compliance = null;
		if(complianceDTO!=null) {
			Long complianceId = complianceDTO.getComplianceId();
			if(complianceId==null) {
				//On Compliance addition we create new Compliance Object
				compliance = new Compliance();
			}else {
				//On editing a Compliance we will Load the Compliance from DB
				compliance = dao.findBy(Compliance.class, complianceId);
			}
			
			compliance.setComplianceName(complianceDTO.getComplianceName());
			String departmentName = complianceDTO.getDepartmentName();
			if(departmentName!=null) {
				EmpDepartment department = dao.findByUniqueProperty(EmpDepartment.class, "departmentName", departmentName);
				compliance.setDepartment(department);
			}
			compliance.setEmailTo(complianceDTO.getEmailTo());
			compliance.setBeforeNotification(complianceDTO.getBeforeNotification());
			compliance.setRecurring(getRecurring(complianceDTO.getRecurring()));
			compliance.setComplianceDate(toDate(complianceDTO.getComplianceDate()));
			compliance.setEscalation(complianceDTO.getEscalation());
			compliance.setEscalationEmail(complianceDTO.getEscalationEmail());
			compliance.setDescription(complianceDTO.getDescription());
			compliance.setPriority(getPriority(complianceDTO.getPriority()));
			if(complianceId==null) {
				compliance.setCreatedBy(securityUtils.getLoggedEmployeeIdforSecurityContextHolder());
				compliance.setCreatedDate(new Second());
				//on Compliance Addition we will create a set of ComplianceTask and add Single task to it . 
				Set<ComplianceTask> complianceTasks = new HashSet<ComplianceTask>();
				complianceTasks.add(getComplainceTask(compliance));
				compliance.setComplianceTasks(complianceTasks);
				
			}else {
				//On update the existing tasks will be Same and tracking is done .
				compliance.setUpdatedBy(securityUtils.getLoggedEmployeeIdforSecurityContextHolder());
				compliance.setUpdatedDate(new Second());
			}
			
		}
		return compliance;
	}
	public ComplianceDTO toDTO(Compliance compliance) {
		ComplianceDTO complianceDTO = null;
		if(compliance!=null) {
			complianceDTO = new ComplianceDTO();
			complianceDTO.setComplianceName(compliance.getComplianceName());
			complianceDTO.setEmailTo(compliance.getEmailTo());
			complianceDTO.setComplianceDate(compliance.getComplianceDate().toString("dd/MM/yyyy"));
			complianceDTO.setBeforeNotification(compliance.getBeforeNotification());
			complianceDTO.setComplianceId(compliance.getComplianceId());
			complianceDTO.setEscalation(compliance.getEscalation());
			complianceDTO.setEscalationEmail(compliance.getEscalationEmail());
			complianceDTO.setRecurring(compliance.getRecurring().getRecurring());
			complianceDTO.setPriority(compliance.getPriority().getPriority());
			EmpDepartment department = compliance.getDepartment();
			if(department!=null) {
				complianceDTO.setDepartmentName(department.getDepartmentName());
			}
			complianceDTO.setDescription(compliance.getDescription());
		}
		return complianceDTO;
	}
	
	public ComplianceTaskDTO toDTO(ComplianceTask complianceTask) {
		ComplianceTaskDTO complianceTaskDTO = null;
		if(complianceTask!=null) {
			complianceTaskDTO = new ComplianceTaskDTO();
			complianceTaskDTO.setComplianceName(complianceTask.getComplianceName());
			complianceTaskDTO.setComplianceTaskId(complianceTask.getComplianceTaskId());
			complianceTaskDTO.setCreatedDate(complianceTask.getCreatedDate().toString("dd/MM/yyyy"));
			complianceTaskDTO.setComplianceDate(complianceTask.getComplianceDate().toString("dd/MM/yyyy"));
			complianceTaskDTO.setDepartment(complianceTask.getCompliance().getDepartment().getDepartmentName());
			complianceTaskDTO.setComplianceTaskStatus(complianceTask.getComplianceTaskStatus().getComplianceTaskStatus());
		}
		return complianceTaskDTO;
	}
	
	//Returning Task for Compliance
	public ComplianceTask getComplainceTask(Compliance compliance) {
		ComplianceTask complianceTask = null;
		if(compliance!=null) {
			complianceTask = new ComplianceTask();
			complianceTask.setComplianceName(compliance.getComplianceName());
			complianceTask.setCreatedBy(compliance.getCreatedBy());
			complianceTask.setCreatedDate(new Second());
			complianceTask.setComplianceTaskStatus(getComplianceStatus("In Progress"));
			if(compliance.getComplianceId()==null) {
				complianceTask.setComplianceDate(compliance.getComplianceDate());
			}
			
			complianceTask.setCompliance(compliance);
		}
		return complianceTask;
	}
	
	public List<ComplianceTaskDTO> toDTOList(List<ComplianceTask> list) {
		List<ComplianceTaskDTO> dtos = null;
		if(list!=null) {
			
			dtos = new ArrayList<ComplianceTaskDTO>();
			for(ComplianceTask complianceTask : list ) {
				//System.out.println("complaince"+complianceTask.getCompliance());
				dtos.add(toDTO(complianceTask));
			}
		}
		return dtos;
	}
	
	public ComplianceTaskSubmit toEntity(ComplianceTaskSubmitDTO dto) {
		ComplianceTaskSubmit cTaskSubmit = null;
		if(dto!=null) {
			cTaskSubmit = new ComplianceTaskSubmit();
			cTaskSubmit.setActualSubmitDate(toDate(dto.getActualSubmitDate()));
			cTaskSubmit.setComments(dto.getComments());
			ComplianceTaskDTO taskDTO = dto.getComplianceTaskDTO();
			if(taskDTO!=null) {
			ComplianceTask	task = dao.findBy(ComplianceTask.class, taskDTO.getComplianceTaskId());
			cTaskSubmit.setComplianceTask(task);
			}
			cTaskSubmit.setCreatedBy(securityUtils.getLoggedEmployeeIdforSecurityContextHolder());
			cTaskSubmit.setCreatedDate(new Second());
			
		}
		return cTaskSubmit;
	}
	
	public List<ComplianceDTO> toComplianceDTOList(List<Compliance> list) {
		List<ComplianceDTO> dtoList = null;
		if(list!=null) {
			dtoList = new ArrayList<>();
			for(Compliance compliance : list) {
				dtoList.add(toDTO(compliance));
			}
		}
		return dtoList;
	}
	//Returning Recurring Enum for string
	public Recurring getRecurring(String recurring) {
		switch (recurring) {
		case "Daily":
			return Recurring.DAILY;
		case "Weekly":
			return  Recurring.WEEKLY;
		case "Monthly":
			return Recurring.MONTHLY;
		}
		return null;
	}
	//Returning ComplianceTaskStatus Enum for string
	public ComplianceTaskStatus getComplianceStatus(String complianceTaskStatus) {
		switch (complianceTaskStatus) {
		case "In Progress":
			
			return ComplianceTaskStatus.INPROGRESS;
		case "Closed":
			return ComplianceTaskStatus.CLOSED;	
		}
		return null;
	}
	//Returning Priority Enum for string
	public Priority getPriority(String priority) {
		switch (priority) {
		case "Low":
			return Priority.LOW;
		case "Normal":
			return Priority.NORMAL;
		case "High":
			return Priority.HIGH;
		case "Urgent":
			return Priority.URGENT;
		case "Immediate":
			return Priority.IMMEDIATE;

		}
		return null;
	}
	//Return Date for valid DateString
	public Date toDate(String date) {
		Date dateObj = null;
		try {
			dateObj = DateParser.toDate(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dateObj;
	}
	public Date getComplianceDate(Compliance compliance) {
		//generating the Compliance Date Dynamic
		Date complianceDate = compliance.getComplianceDate();
		Calendar calendar = compliance.getComplianceDate().getCalendar();
		Integer count = compliance.getComplianceTasks().size();
		switch (compliance.getRecurring()) {
		case DAILY:
			return complianceDate.next();
		case WEEKLY:
			calendar.add(Calendar.WEEK_OF_MONTH, count);
			return  new Date(calendar.getTimeInMillis());
		case MONTHLY:
			calendar.add(Calendar.MONTH_OF_YEAR, count);
			return new Date(calendar.getTimeInMillis());
		}
		return null;
	}
}
