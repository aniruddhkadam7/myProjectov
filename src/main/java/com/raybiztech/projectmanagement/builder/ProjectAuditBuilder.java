/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.raybiztech.projectmanagement.builder;

import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.dao.DAO;
import com.raybiztech.projectmanagement.business.AllocationDetailsAudit;
import com.raybiztech.projectmanagement.business.Audit;
import com.raybiztech.projectmanagement.business.ProjectAudit;
import com.raybiztech.projectmanagement.dto.ProjectAuditDTO;
import com.raybiztech.projectmanagement.dto.ProjectRequestAuditDTO;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author anil
 */
@Component("projectAuditBuilder")
public class ProjectAuditBuilder {

    @Autowired
    DAO dao;

    Logger logger = Logger.getLogger(ProjectAuditBuilder.class);

    public Map<String, Object> ToProjectAuditDTO(Map<String, List<Audit>> map) {
        Map<String, Object> map1 = new HashMap<String, Object>();
        List<ProjectAuditDTO> projectAuditDTOs = new ArrayList<ProjectAuditDTO>();
        ProjectAuditDTO auditDTO = null;
        List<String>  allocatedEmpNames=null;
        if (map != null) {
            for (Map.Entry<String, List<Audit>> entry : map.entrySet()) {
                allocatedEmpNames  = new ArrayList<String>();
                auditDTO = new ProjectAuditDTO();
                for (Audit audit : entry.getValue()) {
                    Employee employee = dao.findBy(Employee.class, audit.getModifiedBy());
                    auditDTO.setModifiedBy(employee.getFullName());
                    auditDTO.setModifiedDate(audit.getModifiedDate().toString("dd-MMM-yyyy hh:mm:ss a"));
                    auditDTO.setPersistType(audit.getPersistType());

                    switch (audit.getColumnName()) {

                        case "projectName":
                            auditDTO.setProjectName(audit.getNewValue());
                            auditDTO.setOldProjectName(audit.getOldValue());
                            break;
                        case "projectManager":
                            Long newManagerId = Long.parseLong(audit.getNewValue());
                            Employee newManagerName = dao.findBy(Employee.class, newManagerId);
                            auditDTO.setManagerName(newManagerName.getFullName());
                            if(audit.getOldValue()!=null){
                            Long oldManagerId = Long.parseLong(audit.getOldValue());
                            Employee oldManagerName = dao.findBy(Employee.class, oldManagerId);
                            auditDTO.setOldManagerName(oldManagerName.getFullName());
                            }
                            break;
                        case "projectStartDate":
                            auditDTO.setStartdate(audit.getNewValue());
                            auditDTO.setOldStartDate(audit.getOldValue());
                            break;
                        case "projectEndDate":
                            auditDTO.setEnddate(audit.getNewValue());
                            auditDTO.setOldEndDate(audit.getOldValue());
                            break;
                        case "status":
                            auditDTO.setStatus(audit.getNewValue());
                            auditDTO.setOldStatus(audit.getOldValue());
                            break;
                        case "client":
                            auditDTO.setClient(audit.getNewValue());
                            auditDTO.setOldClient(audit.getOldValue());
                            break;
                        case "type":
                            auditDTO.setType(audit.getNewValue());
                            auditDTO.setOldType(audit.getOldValue());
                            break;
                        case "health":
                            auditDTO.setHealth(audit.getNewValue());
                            auditDTO.setOldHealth(audit.getOldValue());
                            break;
                        case "description":
                            auditDTO.setDescription(audit.getNewValue());
                            auditDTO.setOldDescription(audit.getOldValue());
                            break;
                        case "employee":
                            Long newEmployeeId = Long.parseLong(audit.getNewValue());
                            Employee newEmployeeName = dao.findBy(Employee.class, newEmployeeId);
                            allocatedEmpNames.add(newEmployeeName.getFullName());
                            
                            break;
                        case "billable":
                            auditDTO.setBillable(Boolean.parseBoolean(audit.getNewValue()));
                              if(audit.getOldValue()!=null)
                            auditDTO.setOldBillable(Boolean.parseBoolean(audit.getOldValue()));
                           if(audit.getAdditionalInfo()!=null){
                            Long billableEmpId = Long.parseLong(audit.getAdditionalInfo());
                            Employee billableEmpName = dao.findBy(Employee.class, billableEmpId);
                            auditDTO.setAdditionalInfo(billableEmpName.getFullName());
                           }
                            
                            break;
                        case "percentage":
                            auditDTO.setAllocation(audit.getNewValue());
                            auditDTO.setOldAllocation(audit.getOldValue());
                            if(audit.getAdditionalInfo()!=null){
                            Long percentageEmpId = Long.parseLong(audit.getAdditionalInfo());
                            Employee percentageEmpName = dao.findBy(Employee.class, percentageEmpId);
                            auditDTO.setAdditionalInfo(percentageEmpName.getFullName());
                            }
                            break;
                        case "allocationStartDate":
                            auditDTO.setFromDate(audit.getNewValue());
                            auditDTO.setOldFromDate(audit.getOldValue());
                            if(audit.getAdditionalInfo()!=null){
                            Long fromDateEmpId = Long.parseLong(audit.getAdditionalInfo());
                            Employee fromDateEmpName = dao.findBy(Employee.class, fromDateEmpId);
                            auditDTO.setAdditionalInfo(fromDateEmpName.getFullName());
                            }
                            break;
                        case "allocationEndDate":
                            auditDTO.setToDate(audit.getNewValue());
                            auditDTO.setOldToDate(audit.getOldValue());
                            if(audit.getAdditionalInfo()!=null){
                            Long toDateEmpId = Long.parseLong(audit.getAdditionalInfo());
                            Employee toDateEmpName = dao.findBy(Employee.class, toDateEmpId);
                            auditDTO.setAdditionalInfo(toDateEmpName.getFullName());
                            }
                            break;
                        case "isAllocated":
                            auditDTO.setIsAllocated(Boolean.parseBoolean(audit.getNewValue()));
                            if(audit.getOldValue()!=null)
                            auditDTO.setOldIsAllocated(Boolean.parseBoolean(audit.getOldValue()));
                            if(audit.getAdditionalInfo()!=null){
                            Long isAllocatedEmpId = Long.parseLong(audit.getAdditionalInfo());
                            Employee isAllocatedEmpName = dao.findBy(Employee.class, isAllocatedEmpId);
                            auditDTO.setAdditionalInfo(isAllocatedEmpName.getFullName());
                            }
                            break;

                    }
                    
                    
                    String allocateEmpNames=null;
                    for(String empName:allocatedEmpNames){
                        if(allocateEmpNames!=null)
                            allocateEmpNames=allocateEmpNames+","+empName;
                        else
                            allocateEmpNames=empName;
                    }
                    auditDTO.setEmployeeName(allocateEmpNames);

//                if (audit.getColumnName().equalsIgnoreCase("projectName")) {
//                    auditDTO.setProjectName(audit.getNewValue());
//                    auditDTO.setOldProjectName(audit.getOldValue());
//                } else if (audit.getColumnName().equalsIgnoreCase("projectManager")) {
//                    Long newManagerId = Long.parseLong(audit.getNewValue());
//                    Employee newManagerName = dao.findBy(Employee.class, newManagerId);
//                    Long oldManagerId = Long.parseLong(audit.getOldValue());
//                    Employee oldManagerName = dao.findBy(Employee.class, oldManagerId);
//
//                    auditDTO.setManagerName(newManagerName.getFullName());
//                    auditDTO.setOldManagerName(oldManagerName.getFullName());
//                } else if (audit.getColumnName().equalsIgnoreCase("projectStartDate")) {
//                    auditDTO.setStartdate(audit.getNewValue());
//                    auditDTO.setOldStartDate(audit.getOldValue());
//                } else if (audit.getColumnName().equalsIgnoreCase("projectEndDate")) {
//                    auditDTO.setEnddate(audit.getNewValue());
//                    auditDTO.setOldEndDate(audit.getOldValue());
//                } else if (audit.getColumnName().equalsIgnoreCase("status")) {
//                    auditDTO.setStatus(audit.getNewValue());
//                    auditDTO.setOldStatus(audit.getOldValue());
//                } else if (audit.getColumnName().equalsIgnoreCase("client")) {
//                    auditDTO.setClient(audit.getNewValue());
//                    auditDTO.setOldClient(audit.getOldValue());
//                } else if (audit.getColumnName().equalsIgnoreCase("type")) {
//                    auditDTO.setType(audit.getNewValue());
//                    auditDTO.setOldType(audit.getOldValue());
//                } else if (audit.getColumnName().equalsIgnoreCase("health")) {
//                    auditDTO.setHealth(audit.getNewValue());
//                    auditDTO.setOldHealth(audit.getOldValue());
//                } else if (audit.getColumnName().equalsIgnoreCase("description")) {
//                    auditDTO.setDescription(audit.getNewValue());
//                    auditDTO.setOldDescription(audit.getOldValue());
//                } else if (audit.getColumnName().equalsIgnoreCase("employee")) {
//                    Long newEmployeeId = Long.parseLong(audit.getNewValue());
//                    Employee newEmployeeName = dao.findBy(Employee.class, newEmployeeId);
//                    Long oldEmployeeId = Long.parseLong(audit.getOldValue());
//                    Employee oldEmployeeName = dao.findBy(Employee.class, oldEmployeeId);
//                    auditDTO.setEmployeeName(newEmployeeName.getFullName());
//                    auditDTO.setOldEmployeeName(oldEmployeeName.getFullName());
//                } else if (audit.getColumnName().equalsIgnoreCase("billable")) {
//                    auditDTO.setBillable(Boolean.parseBoolean(audit.getNewValue()));
//                    auditDTO.setOldBillable(Boolean.parseBoolean(audit.getOldValue()));
//                } else if (audit.getColumnName().equalsIgnoreCase("percentage")) {
//                    auditDTO.setAllocation(audit.getNewValue());
//                    auditDTO.setOldAllocation(audit.getOldValue());
//                } else if (audit.getColumnName().equalsIgnoreCase("allocationStartDate")) {
//                    auditDTO.setFromDate(audit.getNewValue());
//                    auditDTO.setOldFromDate(audit.getOldValue());
//                } else if (audit.getColumnName().equalsIgnoreCase("allocationEndDate")) {
//                    auditDTO.setToDate(audit.getNewValue());
//                    auditDTO.setOldToDate(audit.getOldValue());
//                }
                }
                projectAuditDTOs.add(auditDTO);
            }
        }

        if (projectAuditDTOs != null) {
            Collections.sort(projectAuditDTOs, new Comparator<ProjectAuditDTO>() {

                @Override
                public int compare(ProjectAuditDTO pAudit1, ProjectAuditDTO pAudit2) {
                    int k = 0;
                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a");
                        java.util.Date date1 = sdf.parse(pAudit1.getModifiedDate());
                        java.util.Date date2 = sdf.parse(pAudit2.getModifiedDate());
                        // Date date1=DateParser.toDate(pAudit1.getModifiedDate());
                        //Date date2=DateParser.toDate(pAudit2.getModifiedDate());
                        // Date date1=Date.parse(pAudit1.getModifiedDate(), "dd-MMM-yyyy hh:mm:ss");
                        //  Date date2=Date.parse(pAudit2.getModifiedDate(), "dd-MMM-yyyy hh:mm:ss");

                        //logger.warn("modified date :" + date1 + ":date2:" + date2);
                        if (date1.after(date2)) {
                            k = -1;
                        }
                        if (date1.before(date2)) {
                            k = 1;
                        }

                    } catch (ParseException ex) {
                        java.util.logging.Logger.getLogger(ProjectAuditBuilder.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    return k;
                }
            });
        }
        map1.put("list", projectAuditDTOs != null ? projectAuditDTOs : "");
        map1.put("size", projectAuditDTOs != null ? projectAuditDTOs.size() : "");
        return map1;
    }

//    public List<ProjectAuditDTO> converTotProjectAuditDTO(List<Audit> projectAudits) {
//
//        List<ProjectAuditDTO> projectAuditDTOs = new ArrayList<ProjectAuditDTO>();
//        ProjectAuditDTO auditDTO = new ProjectAuditDTO();
//        if (projectAudits != null) {
//            for (Audit audit : projectAudits) {
//                logger.warn("builder :" + audit.getId());
//
//                Employee employee = dao.findBy(Employee.class, audit.getModifiedBy());
//                auditDTO.setModifiedBy(employee.getFullName());
//                auditDTO.setModifiedDate(audit.getModifiedDate().toString("dd-MMM-yyyy hh:mm a"));
//                auditDTO.setPersistType(audit.getPersistType());
//
//                if (audit.getColumnName().equalsIgnoreCase("projectName")) {
//                    auditDTO.setProjectName(audit.getNewValue());
//               // auditDTO.setOldvalue(audit.getOldValue());
//                    //auditDTO.setNewValue(audit.getNewValue());
//                } else if (audit.getColumnName().equalsIgnoreCase("projectManager")) {
//                    auditDTO.setManagerName(audit.getNewValue());
//                } else if (audit.getColumnName().equalsIgnoreCase("fromDate")) {
//                    auditDTO.setFromDate(audit.getNewValue());
//                } else if (audit.getColumnName().equalsIgnoreCase("status")) {
//                    auditDTO.setStatus(audit.getNewValue());
//                } else if (audit.getColumnName().equalsIgnoreCase("client")) {
//                    auditDTO.setClient(audit.getNewValue());
//
//                }
//
//            }
//            projectAuditDTOs.add(auditDTO);
//        }
//        return projectAuditDTOs;
//    }
//    public List<ProjectAuditDTO> convertToDTO(List<ProjectAudit> projectAudits, List<AllocationDetailsAudit> detailsAudits) {
//
//        List<ProjectAuditDTO> projectAuditDTOs = new ArrayList<ProjectAuditDTO>();
//        ProjectAuditDTO auditDTO = null;
//        if (projectAudits != null) {
//            for (ProjectAudit audit : projectAudits) {
//                auditDTO = new ProjectAuditDTO();
//                auditDTO.setProjectDetailsFlag(Boolean.TRUE);
//                auditDTO.setProjectName(audit.getProjectName());
//                // auditDTO.setManagerId(audit.getProjectManager().getEmployeeId());
//                auditDTO.setManagerName(audit.getProjectManager().getFullName());
//                auditDTO.setStatus(audit.getStatus().getProjectStatus());
//                auditDTO.setType(audit.getType().name());
//                auditDTO.setClient(audit.getClient().getName());
//                //auditDTO.setClientId(audit.getClient().getId());
//                auditDTO.setHealth(audit.getHealth());
//                auditDTO.setStartdate(audit.getPeriod().getMinimum().toString("dd/MM/yyyy"));
//                if (audit.getPeriod().getMaximum() != null) {
//                    auditDTO.setEnddate(audit.getPeriod().getMaximum().toString("dd/MM/yyyy"));
//                }
//                auditDTO.setModifiedBy(audit.getModifiedBy());
//                auditDTO.setModifiedDate(audit.getModifiedDate().toString("dd-MMM-yyyy hh:mm a"));
//                auditDTO.setPersistType(audit.getPersistType());
//                auditDTO.setDescription(audit.getDescription());
//                projectAuditDTOs.add(auditDTO);
//            }
//        }
//
//        if (detailsAudits != null) {
//            for (AllocationDetailsAudit detailsAudit : detailsAudits) {
//                auditDTO = new ProjectAuditDTO();
//                auditDTO.setProjectDetailsFlag(Boolean.FALSE);
//                if (detailsAudit.getEmployee() != null) {
//                    auditDTO.setEmployeeId(detailsAudit.getEmployee().getEmployeeId());
//                    auditDTO.setEmployeeName(detailsAudit.getEmployee().getFullName());
//                }
//                auditDTO.setModifiedBy(detailsAudit.getModifiedBy());
//                auditDTO.setModifiedDate(detailsAudit.getModifiedDate().toString("dd-MMM-yyyy hh:mm a"));
//                auditDTO.setPersistType(detailsAudit.getPersistType());
//                auditDTO.setAllocation(detailsAudit.getPercentage().toString("#0", false));
//                //auditDTO.setAllocationPercantage(detailsAudit.getPercentage());
//                auditDTO.setFromDate(detailsAudit.getPeriod().getMinimum().toString("dd/MM/yyyy"));
//                auditDTO.setToDate(detailsAudit.getPeriod().getMaximum().toString("dd/MM/yyyy"));
//                auditDTO.setIsAllocated(detailsAudit.getIsAllocated());
//                auditDTO.setBillable(detailsAudit.getBillable());
//                auditDTO.setCommnets(detailsAudit.getComments());
//                projectAuditDTOs.add(auditDTO);
//            }
//        }
//
//        if (projectAuditDTOs != null) {
//            Collections.sort(projectAuditDTOs, new Comparator<ProjectAuditDTO>() {
//
//                @Override
//                public int compare(ProjectAuditDTO pAudit1, ProjectAuditDTO pAudit2) {
//                    int k = 0;
//                    try {
//                        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy hh:mm a");
//                        java.util.Date date1 = sdf.parse(pAudit1.getModifiedDate());
//                        java.util.Date date2 = sdf.parse(pAudit2.getModifiedDate());
//                       // Date date1=DateParser.toDate(pAudit1.getModifiedDate());
//                        //Date date2=DateParser.toDate(pAudit2.getModifiedDate());
//                        // Date date1=Date.parse(pAudit1.getModifiedDate(), "dd-MMM-yyyy hh:mm:ss");
//                        //  Date date2=Date.parse(pAudit2.getModifiedDate(), "dd-MMM-yyyy hh:mm:ss");
//
//                        // logger.warn("modified date :"+date1+":date2:"+date2);
//                        if (date1.after(date2)) {
//                            k = -1;
//                        }
//                        if (date1.before(date2)) {
//                            k = 1;
//                        }
//
//                    } catch (ParseException ex) {
//                        java.util.logging.Logger.getLogger(ProjectAuditBuilder.class.getName()).log(Level.SEVERE, null, ex);
//                    }
//                    return k;
//                }
//            });
//        }
//
//        return projectAuditDTOs;
//    }
    
    public List<ProjectRequestAuditDTO> getProjReqAudits(List<Audit> list) {
		List<ProjectRequestAuditDTO> listDTO = new ArrayList<ProjectRequestAuditDTO>();  
    	for(Audit audit : list){
    		ProjectRequestAuditDTO dto = new ProjectRequestAuditDTO();
    		dto.setAuditId(audit.getId());
    		dto.setOldValue(audit.getOldValue());
    		dto.setNewValue(audit.getNewValue());
    		dto.setModifiedBy(audit.getModifiedBy());
    		dto.setPersistType(audit.getPersistType());
    		dto.setReferenceId(audit.getReferenceId());
    		dto.setAdditionalInfo(audit.getAdditionalInfo());
    		dto.setModifiedDate(audit.getModifiedDate().toString("dd-MMM-yyyy hh:mm:ss a"));
    		Employee employee = dao.findBy(Employee.class, audit.getModifiedBy());
    		dto.setEmployeeName(employee.getFullName());
    		listDTO.add(dto);
    	}
		return listDTO;
	}
    
}
