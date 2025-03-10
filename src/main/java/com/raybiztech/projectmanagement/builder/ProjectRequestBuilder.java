/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.raybiztech.projectmanagement.builder;

import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.dao.DAO;
import com.raybiztech.appraisals.security.utils.SecurityUtils;
import com.raybiztech.date.Date;
import com.raybiztech.date.DateRange;
import com.raybiztech.projectmanagement.business.Client;
import com.raybiztech.projectmanagement.business.Country;
import com.raybiztech.projectmanagement.business.ProjectCheckList;
import com.raybiztech.projectmanagement.business.ProjectInitiationChecklist;
import com.raybiztech.projectmanagement.business.ProjectModel;
import com.raybiztech.projectmanagement.business.ProjectRequest;
import com.raybiztech.projectmanagement.business.ProjectRequestMilestone;
import com.raybiztech.projectmanagement.business.ProjectType;
import com.raybiztech.projectmanagement.dto.ProjectInitiationChecklistDTO;
import com.raybiztech.projectmanagement.dto.ProjectRequestDTO;
import com.raybiztech.projectmanagement.dto.ProjectRequestMilestoneDTO;
import com.raybiztech.projectmanagement.service.ProjectService;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author anil
 */
@Component("projectRequestBuilder")
public class ProjectRequestBuilder {

    Logger logger = Logger.getLogger(ProjectRequestBuilder.class);
    @Autowired
    SecurityUtils securityUtils;
    @Autowired
    DAO dao;
    @Autowired
    ProjectService projectService;
    public ProjectRequest createProjectRequestEntity(ProjectRequestDTO prdto, Employee employee) {

        ProjectRequest projectRequest =null;
        if(prdto.getId()!=null){
        	projectRequest=dao.findBy(ProjectRequest.class, prdto.getId());
        	//projectRequest.setId(prdto.getId());
        }else{
        	projectRequest = new ProjectRequest();
        }
        
        projectRequest.setProjectName(prdto.getProjectName());
        projectRequest.setProjectManager(employee);
        projectRequest.setStatus(prdto.getStatus());
        projectRequest.setDescription(prdto.getDescription());
        projectRequest.setRequiredResources(prdto.getRequiredResources());
        projectRequest.setType(ProjectType.valueOf(prdto.getType()));
        projectRequest.setNewClient(prdto.getNewClient());
        projectRequest.setTechnology(prdto.getTechnology());
        Long loggedInEmpId = securityUtils.getLoggedEmployeeIdforSecurityContextHolder();
        Employee requestedBy=dao.findBy(Employee.class, loggedInEmpId);
        projectRequest.setRequestedBy(requestedBy);
        projectRequest.setOrganization(prdto.getOrganization());
        projectRequest.setPersonName(prdto.getPersonName());
        projectRequest.setEmail(prdto.getEmail());
        projectRequest.setAddress(prdto.getAddress());
        projectRequest.setIntrnalOrNot(prdto.getIntrnalOrNot());
        Country country = dao.findByUniqueProperty(Country.class, "name", prdto.getCountry());
        projectRequest.setCountry(country);
        projectRequest.setCc(prdto.getCc());
        projectRequest.setBcc(prdto.getBcc());
        projectRequest.setChecklist(checkListToentity(prdto.getChelist()));
        projectRequest.setModel(ProjectModel.valueOf(prdto.getModel()));
        projectRequest.setProjectContactPerson(prdto.getProjectContactPerson());
        projectRequest.setProjectContactEmail(prdto.getProjectContactEmail());
        projectRequest.setBillingContactPerson(prdto.getBillingContactPerson());
        projectRequest.setBillingContactPersonEmail(prdto.getBillingContactPersonEmail());
        projectRequest.setPlatform(prdto.getPlatform());
        projectRequest.setDomain(prdto.getDomain());
        
      
        Set<ProjectRequestMilestoneDTO> dtos=prdto.getProjectRequestMilestoneDTO();
        if(dtos!=null){
        	 Set<ProjectRequestMilestone> milestones = new HashSet<ProjectRequestMilestone>();
             
             for(ProjectRequestMilestoneDTO dto:dtos)
             {
             	ProjectRequestMilestone milestone=null;
             	if(dto.getId()!=null){
             		milestone= dao.findBy(ProjectRequestMilestone.class, dto.getId());
             	}else{
             		milestone = new ProjectRequestMilestone();
             	}
             	
             	milestone.setMilestoneTitle(dto.getTitle());
             	milestone.setEffort(dto.getEffort());
             	milestone.setComments(dto.getComments());
             	   try {
                        Date fromDate = null;
                        Date toDate = Date.parse(dto.getToDate(), "dd/MM/yyyy");
                        
                        	fromDate = Date.parse(dto.getFromDate(), "dd/MM/yyyy");
                       
                        milestone.setPeriod(new DateRange(fromDate, toDate));
                        milestone.setBillable(dto.getBillable());
                        if(dto.getMilestonePercentage()!=null)
                        {
                        	milestone.setMilestonePercentage(dto.getMilestonePercentage());
                        }
                    } catch (ParseException parseException) {
                        logger.error("parse exception came while converitng String into Date");
                    }
             	   milestones.add(milestone);
             }
             projectRequest.setProjectRequestMilestone(milestones);
        	
        }
       
        try {
            Date toDate = null;
            Date fromDate = Date.parse(prdto.getStartdate(), "dd/MM/yyyy");
            if (prdto.getEnddate() != null) {
                toDate = Date.parse(prdto.getEnddate(), "dd/MM/yyyy");
            }
            projectRequest.setPeriod(new DateRange(fromDate, toDate));
        } catch (ParseException parseException) {
            logger.error("parse exception came while converitng String into Date");
        }

        return projectRequest;

    }
    
    public List<ProjectRequestDTO> getAllProjectRequestsDTO(List<ProjectRequest> requests){
        
        List<ProjectRequestDTO> requestDTOs = new ArrayList<ProjectRequestDTO>();
        
        
        if(requests!=null){
            for(ProjectRequest pr:requests){
                requestDTOs.add(getProjectRequestDTO(pr));
            }
        }
          return requestDTOs;
    }
    
    public ProjectRequestDTO getProjectRequestDTO(ProjectRequest projectRequest){
        ProjectRequestDTO dTO=new ProjectRequestDTO();
        dTO.setId(projectRequest.getId());
        dTO.setDescription(projectRequest.getDescription());
        dTO.setRequiredResources(projectRequest.getRequiredResources());
        dTO.setProjectName(projectRequest.getProjectName());
        dTO.setManagerName(projectRequest.getProjectManager().getFullName());
        dTO.setManagerId(projectRequest.getProjectManager().getEmployeeId());
        dTO.setStartdate(projectRequest.getPeriod().getMinimum().toString("dd/MM/yyyy"));
        dTO.setNewClient(projectRequest.getNewClient());
        dTO.setStatus(projectRequest.getStatus());
        dTO.setTechnology(projectRequest.getTechnology());
        dTO.setRequestedBy(projectRequest.getRequestedBy().getFullName());
        dTO.setIntrnalOrNot(projectRequest.getIntrnalOrNot());
        if(projectRequest.getPeriod().getMaximum()!=null)
            dTO.setEnddate(projectRequest.getPeriod().getMaximum().toString("dd/MM/yyyy"));
        if(projectRequest.getClient()!=null)
            dTO.setClient(projectRequest.getClient().getOrganization());
        if(projectRequest.getType()!=null)
            dTO.setType(projectRequest.getType().name());
        if(projectRequest.getModel()!=null)
        	dTO.setModel(projectRequest.getModel().name().toString());
        dTO.setOrganization(projectRequest.getOrganization());
        dTO.setPersonName(projectRequest.getPersonName());
        dTO.setEmail(projectRequest.getEmail());
        dTO.setAddress(projectRequest.getAddress());
        dTO.setCountry(projectRequest.getCountry()!=null?projectRequest.getCountry().getName():"");
        dTO.setProjectContactPerson(projectRequest.getProjectContactPerson());
        dTO.setProjectContactEmail(projectRequest.getProjectContactEmail());
        dTO.setBillingContactPerson(projectRequest.getBillingContactPerson());
        dTO.setBillingContactPersonEmail(projectRequest.getBillingContactPersonEmail());
        dTO.setPlatform(projectRequest.getPlatform());
        dTO.setDomain(projectRequest.getDomain());
        if(projectRequest.getChecklist().size()>0)
        {
        	
        	dTO.setCheckListExist(true);
        }
        else
        {
        	dTO.setCheckListExist(false);
        }
        Set<ProjectInitiationChecklistDTO> checklistDTOs=new HashSet<ProjectInitiationChecklistDTO>();
        for(ProjectInitiationChecklist initiationChecklist:projectRequest.getChecklist()){
        	ProjectInitiationChecklistDTO checklistDTO=new ProjectInitiationChecklistDTO();
        	checklistDTO.setId(initiationChecklist.getId());
        	checklistDTO.setName(initiationChecklist.getChecklist().getName());
        	checklistDTO.setChecklistId(initiationChecklist.getChecklist().getId());
        	checklistDTO.setAnswer(initiationChecklist.getAnswer());
        	checklistDTO.setComments(initiationChecklist.getComments());
        	checklistDTOs.add(checklistDTO);
        }
        dTO.setChelist(checklistDTOs);
        dTO.setCc(projectRequest.getCc());
        dTO.setBcc(projectRequest.getBcc());
        
        dTO.setProjectRequestMilestoneDTO(getProjReqMileStones(projectRequest.getProjectRequestMilestone()));
        
        Long empId = securityUtils
				.getLoggedEmployeeIdforSecurityContextHolder();
		Employee employee = dao.findBy(Employee.class, empId);
		
		List<Long> employeeIds = null;
		Boolean adminFlag = false;
		
		if(employee.getRole().contains("admin"))
		{
			adminFlag = true;	
		}else {
			 employeeIds = projectService.mangerUnderManager(empId);
		}
		
		if(!adminFlag)
		{
			logger.warn("Employee size "+employeeIds.size());
		}
		
		
		if(adminFlag){
			dTO.setAccess(true);
		}else if (employeeIds.size()>1 &&  (!(projectRequest.getProjectManager().getFullName().
				equals(employee.getFullName())))){
			dTO.setAccess(true);
		}
        return dTO;
        
    }
    
    public List<ProjectInitiationChecklistDTO> getProjectChecklistDTO(List<ProjectCheckList> checklist){
    	List<ProjectInitiationChecklistDTO> listDTO = new ArrayList<ProjectInitiationChecklistDTO>();
    	
    	for(ProjectCheckList list : checklist){
    		ProjectInitiationChecklistDTO dto = new ProjectInitiationChecklistDTO();
    		dto.setName(list.getName());
    		dto.setChecklistId(list.getId());
    		listDTO.add(dto);
    	}
    	return listDTO;
    }
    public Set<ProjectInitiationChecklist> checkListToentity(Set<ProjectInitiationChecklistDTO> checklistDTOs){
    	Set<ProjectInitiationChecklist> checklists = new HashSet<ProjectInitiationChecklist>();
    	if(checklistDTOs != null){
    		for(ProjectInitiationChecklistDTO dto : checklistDTOs){
    			ProjectInitiationChecklist checklist=null;
    			if(dto.getId()!=null){
    				checklist=dao.findBy(ProjectInitiationChecklist.class, dto.getId());
    			}else{
    				checklist = new ProjectInitiationChecklist();
    			}
    			
    			checklist.setAnswer(dto.getAnswer());
    			checklist.setComments(dto.getComments());
    			checklist.setChecklist(dao.findBy(ProjectCheckList.class, dto.getChecklistId()));
    			checklists.add(checklist);
    		}
    	}
    	return checklists;
    }
    public Set<ProjectRequestMilestoneDTO> getProjReqMileStones(Set<ProjectRequestMilestone> milestones) {
		Set<ProjectRequestMilestoneDTO> setDTO = new HashSet<ProjectRequestMilestoneDTO>();
		
		for(ProjectRequestMilestone projReqMileStone : milestones){
			ProjectRequestMilestoneDTO projectRequestMilestoneDTO = new ProjectRequestMilestoneDTO();
			projectRequestMilestoneDTO.setComments(projReqMileStone.getComments());
			projectRequestMilestoneDTO.setEffort(projReqMileStone.getEffort());
			projectRequestMilestoneDTO.setFromDate(projReqMileStone.getPeriod().getMinimum().toString("dd/MM/yyyy"));
			projectRequestMilestoneDTO.setToDate(projReqMileStone.getPeriod().getMaximum().toString("dd/MM/yyyy"));
			projectRequestMilestoneDTO.setTitle(projReqMileStone.getMilestoneTitle());
			projectRequestMilestoneDTO.setId(projReqMileStone.getId());
			if(projReqMileStone.getBillable()!=null)
			{
			projectRequestMilestoneDTO.setBillable(projReqMileStone.getBillable());
			}
			if(projReqMileStone.getMilestonePercentage()!=null)
			{
			projectRequestMilestoneDTO.setMilestonePercentage(projReqMileStone.getMilestonePercentage());
			}
			setDTO.add(projectRequestMilestoneDTO);
			
		}
		return setDTO;
	}
    
    
}
