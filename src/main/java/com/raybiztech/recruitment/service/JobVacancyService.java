package com.raybiztech.recruitment.service;

import java.util.List;
import java.util.Map;

import com.raybiztech.projectmanagement.business.Audit;
import com.raybiztech.recruitment.dto.JobVacancyAuditDto;
import com.raybiztech.recruitment.dto.JobVacancyDTO;
import com.raybiztech.recruitment.dto.VacancyLookUpDTO;

public interface JobVacancyService {

    void addJobVacancy(JobVacancyDTO jobVacancyDTO);

    Map<String, Object> getOpenJobVacancies(Integer startIndex,Integer endIndex,String status,String searchJobTitle);
    
   // List<JobVacancyDTO> getAllJobVacancies( String searchJobTitle);

    void deleteJobVacancy(Long id);

    List<VacancyLookUpDTO> getAllVacancyLookUp();

    Boolean jobVacantAlreadyCheck(String vacantposition);

    List<VacancyLookUpDTO> addPositionVacantName(String positionVacantName,String jobCode);

    void deletePositionVacancy(long positionvacancyLookUpId);

    JobVacancyDTO getEditJobvacancy(Long id);

    void updateJobVacancy(JobVacancyDTO jobVacancyDTO);
    
    Map<String,Object> getJobVacancyAudit(Long id);
    

}
