package com.raybiztech.recruitment.builder;

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

import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.projectmanagement.business.Audit;
import com.raybiztech.projectmanagement.dto.ProjectRequestAuditDTO;
import com.raybiztech.recruitment.business.JobVacancy;
import com.raybiztech.recruitment.dao.JobVacancyDAO;
import com.raybiztech.recruitment.dto.JobVacancyAuditDto;
import com.raybiztech.recruitment.dto.JobVacancyDTO;
import com.raybiztech.recruitment.utils.DateParser;
import com.raybiztech.supportmanagement.builder.SupportAuditBuilder;
import com.raybiztech.supportmanagement.dto.SupportAuditDTO;

@Component("jobVacancyBuilder")
public class JobVacancyBuilder {
	Logger logger = Logger.getLogger(JobVacancy.class);
	
	@Autowired
	JobVacancyDAO dao;

    public JobVacancyDTO createJobVacancyDTO(JobVacancy jobVacancy) {
        JobVacancyDTO jobVacancyDTO = null;
        if (jobVacancy != null) {
        	
            jobVacancyDTO = new JobVacancyDTO();
            jobVacancyDTO.setId(jobVacancy.getJobVacancyId());
            jobVacancyDTO.setDescription(jobVacancy.getDescription());
            jobVacancyDTO.setMinimumExperience(jobVacancy.getMinimumExperience());
            jobVacancyDTO.setNoOfRequirements(jobVacancy.getNoOfRequirements());
            jobVacancyDTO.setPositionVacant(jobVacancy.getPositionVacant());
            jobVacancyDTO.setOpendDate(DateParser.toString(jobVacancy.getOpenedDate()));
            jobVacancyDTO.setOffered(jobVacancy.getOffered());
            jobVacancyDTO.setJobCode(jobVacancy.getJobCode());
            jobVacancyDTO.setRemaining(jobVacancy.getNoOfRequirements() - jobVacancy.getOffered());
            jobVacancyDTO.setExpiryDate(jobVacancy.getExpiryDate()!=null?jobVacancy.getExpiryDate().toString("dd/MM/yyyy"):null);
            if(jobVacancy.getStatus()!=null) {
            	
            jobVacancyDTO.setStatus((jobVacancy.getStatus())?"open":"close");
        } else {
            	jobVacancyDTO.setStatus("");
            }
        }
        return jobVacancyDTO;
    }
    

    public JobVacancy createJobVacancy(JobVacancyDTO jobVacancyDTO) {
        JobVacancy jobVacancy = null;
        if (jobVacancyDTO != null) {
            jobVacancy = new JobVacancy();
            jobVacancy.setJobVacancyId(jobVacancyDTO.getId());
            jobVacancy.setDescription(jobVacancyDTO.getDescription());
            jobVacancy.setMinimumExperience(jobVacancyDTO.getMinimumExperience());
            jobVacancy.setPositionVacant(jobVacancyDTO.getPositionVacant());
            jobVacancy.setNoOfRequirements(jobVacancyDTO.getNoOfRequirements());
            jobVacancy.setJobCode(jobVacancyDTO.getJobCode());
            if(jobVacancyDTO.getExpiryDate()!=null){
                try{	
                jobVacancy.setExpiryDate(DateParser.toDate(jobVacancyDTO.getExpiryDate()));
                }catch(ParseException pe){
                	
                }
                }
            jobVacancy.setStatus((jobVacancyDTO.getStatus().equalsIgnoreCase("open"))?Boolean.TRUE:Boolean.FALSE);
        }
        return jobVacancy;
    }
    
    public JobVacancy createJobVacancy(JobVacancy jobVacancy,JobVacancyDTO jobVacancyDTO) {
       
        if (jobVacancyDTO != null) {
        	jobVacancy.setJobCode(jobVacancyDTO.getJobCode());
        	jobVacancy.setPositionVacant(jobVacancyDTO.getPositionVacant());
            jobVacancy.setDescription(jobVacancyDTO.getDescription());
            jobVacancy.setMinimumExperience(jobVacancyDTO.getMinimumExperience());
            jobVacancy.setNoOfRequirements(jobVacancyDTO.getNoOfRequirements());
            if(jobVacancyDTO.getExpiryDate()!=null)
            	try{
            jobVacancy.setExpiryDate(DateParser.toDate(jobVacancyDTO.getExpiryDate()));
            	}catch(ParseException pe){
                	
                }
            jobVacancy.setStatus((jobVacancyDTO.getStatus().equalsIgnoreCase("open"))?Boolean.TRUE:Boolean.FALSE);
        }
        return jobVacancy;
    }

    public List<JobVacancyDTO> createJobVacancyDTOList(List<JobVacancy> jobvacancyList) {
        List<JobVacancyDTO> jobVacancyDTOs = null;

        if (jobvacancyList != null) {

            jobVacancyDTOs = new ArrayList<JobVacancyDTO>();
            for (JobVacancy jobVacancy : jobvacancyList) {
                jobVacancyDTOs.add(createJobVacancyDTO(jobVacancy));

            }
        }
        return jobVacancyDTOs;
    }
    
  public Map<String, Object> ToJobVacancyAuditDTO(Map<String, List<Audit>> map) {
		Map<String, Object> map1 = new HashMap<String, Object>();
		List<JobVacancyAuditDto> AuditDTOs = new ArrayList<JobVacancyAuditDto>();

		if (map != null) {
			for (Map.Entry<String, List<Audit>> entry : map.entrySet()) {
				JobVacancyAuditDto auditDTO = new JobVacancyAuditDto();
				for (Audit audit : entry.getValue()) {

					Employee employee = dao.findBy(Employee.class,
							audit.getModifiedBy());
					auditDTO.setModifiedBy(employee.getFullName());
					auditDTO.setModifiedDate(audit.getModifiedDate().toString(
							"dd-MMM-yyyy hh:mm:ss a"));
					auditDTO.setPersistType(audit.getPersistType());

					switch (audit.getColumnName()) {

					case "jobCode":
						auditDTO.setJobCode(audit.getNewValue());
						auditDTO.setOldjobCode(audit.getOldValue());
						break;
					case "positionVacant":
						auditDTO.setPositionVacant(audit.getNewValue());
						auditDTO.setOldpositionVacant(audit.getOldValue());
						break;
					case "minimumExperience":
						auditDTO.setMinimumExperience(audit.getNewValue());
						auditDTO.setOldminimumExperience(audit.getOldValue());
						break;
						
					case "description":

						auditDTO.setDescription(audit.getNewValue());
						auditDTO.setOlddescription(audit.getOldValue());
						break;
					case "expiryDate":
						auditDTO.setExpiryDate(audit.getNewValue() != null ? audit
								.getNewValue().toString() : null);
						auditDTO.setOldexpiryDate(audit.getOldValue() != null ? audit
								.getOldValue().toString() : null);
						break;
					case "openedDate":
						auditDTO.setOpendDate(audit.getNewValue() != null ? audit
								.getNewValue().toString() : null);
						auditDTO.setOldopendDate(audit.getOldValue() != null ? audit
								.getOldValue().toString() : null);
						break;
					case "noOfRequirements":
						auditDTO.setNoOfRequirements(audit.getNewValue() != null ? audit
								.getNewValue(): null);
						auditDTO.setOldnoOfRequirements(audit.getOldValue() != null ? audit
								.getOldValue().toString() : null);
						break;
					case "offered":
						auditDTO.setOffered(audit.getNewValue()
								.toString());
						auditDTO.setOldoffered(audit.getOldValue() != null ? audit
								.getOldValue().toString() : null);
						break;
					case "status":
						auditDTO.setStatus(audit.getNewValue()
								.toString());
						auditDTO.setOldStatus(audit.getOldValue() != null ? audit
								.getOldValue().toString() : null);
						break;	
					}
				}
				AuditDTOs.add(auditDTO);

			}

		}

		if (AuditDTOs != null) {
			Collections.sort(AuditDTOs, new Comparator<JobVacancyAuditDto>() {

				@Override
				public int compare(JobVacancyAuditDto audit1,
						JobVacancyAuditDto audit2) {
					int k = 0;
					try {
						SimpleDateFormat sdf = new SimpleDateFormat(
								"dd-MMM-yyyy hh:mm:ss a");
						java.util.Date date1 = sdf.parse(audit1
								.getModifiedDate());
						java.util.Date date2 = sdf.parse(audit2
								.getModifiedDate());

						if (date1.after(date2)) {
							k = -1;
						}
						if (date1.before(date2)) {
							k = 1;
						}

					} catch (ParseException ex) {
						java.util.logging.Logger.getLogger(
								JobVacancyBuilder.class.getName()).log(
								Level.SEVERE, null, ex);
					}
					return k;
				}
			});
		}

		map1.put("list", AuditDTOs != null ? AuditDTOs : "");
		map1.put("size", AuditDTOs != null ? AuditDTOs.size() : "");
		return map1;
	}
  }


