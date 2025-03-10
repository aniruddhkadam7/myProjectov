package com.raybiztech.recruitment.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.date.Date;
import com.raybiztech.lookup.business.VacancyLookUp;
import com.raybiztech.projectmanagement.builder.AuditBuilder;
import com.raybiztech.projectmanagement.business.Audit;
import com.raybiztech.recruitment.builder.JobVacancyBuilder;
import com.raybiztech.recruitment.builder.VacancyLookUpBuilder;
import com.raybiztech.recruitment.business.JobVacancy;
import com.raybiztech.recruitment.dao.JobVacancyDAO;
import com.raybiztech.recruitment.dto.JobVacancyAuditDto;
import com.raybiztech.recruitment.dto.JobVacancyDTO;
import com.raybiztech.recruitment.dto.VacancyLookUpDTO;

@Service("jobVacancyServieImpl")
@Transactional
public class JobVacancyServieImpl implements JobVacancyService {

	@Autowired
	JobVacancyDAO dao;
	@Autowired
	JobVacancyBuilder jobVacancyBuilder;
	@Autowired
	VacancyLookUpBuilder vacancyLookUpBuilder;
	@Autowired
	AuditBuilder auditBuilder;
	
	Logger logger = Logger.getLogger(JobVacancyServieImpl.class);

	@CacheEvict(value = "jobVacancies", allEntries = true)
	@Override
	public void addJobVacancy(JobVacancyDTO jobVacancyDTO) {
		JobVacancy jobVacancy = jobVacancyBuilder
				.createJobVacancy(jobVacancyDTO);
		jobVacancy.setOffered(0);
		jobVacancy.setOpenedDate(new Date());
		Long id= (Long) dao.save(jobVacancy);
		List<Audit> audits = (List<Audit>) auditBuilder.createAuditToJobvacancy(jobVacancy, id, "JOBVACANCY", "CREATED");
	    for(Audit audit:audits)
	    {
	    	dao.save(audit);
	    }
	}

	@CacheEvict(value = "jobVacancies", allEntries = true)
	@Override
	public void updateJobVacancy(JobVacancyDTO jobVacancyDTO) {

		JobVacancy jbvcncy = dao
				.findBy(JobVacancy.class, jobVacancyDTO.getId());
		JobVacancy oldjobVacancy=new JobVacancy();
	    
		try {
			oldjobVacancy=(JobVacancy) jbvcncy.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		JobVacancy jobVacancy = jobVacancyBuilder.createJobVacancy(jbvcncy,
				jobVacancyDTO);
		dao.update(jobVacancy);
		
	List<Audit> audits=	auditBuilder.updateAuditToJobVacancy(jbvcncy,jobVacancyDTO.getId(), oldjobVacancy, "JOBVACANCY", "UPDATED");
		for(Audit audit:audits)
		{
			dao.save(audit);
		}
	}

	//@Cacheable(value = "jobVacancies")
//	@Override
//	public List<JobVacancyDTO> getOpenJobVacancies(String status) {
//		List<JobVacancy> jobVacancies = dao.getOpenJobVacancies(status);
//		Collections.sort(jobVacancies, new VacancyListComparator());
//		return jobVacancyBuilder.createJobVacancyDTOList(jobVacancies);
//	}
//	@Override
//	public List<JobVacancyDTO> getAllJobVacancies(String searchJobTitle) {
//		List<JobVacancy> jobVacancies = dao.getAllJobVacancies(searchJobTitle);
//		Collections.sort(jobVacancies, new VacancyListComparator());
//		return jobVacancyBuilder.createJobVacancyDTOList(jobVacancies);
//	}

	@CacheEvict(value = "jobVacancies", allEntries = true)
	@Override
	public void deleteJobVacancy(Long id) {
		JobVacancy jobVacancy = dao.findBy(JobVacancy.class, id);
		dao.delete(jobVacancy);
	}

	@Cacheable(value = "vacanciesLookup")
	@Override
	public List<VacancyLookUpDTO> getAllVacancyLookUp() {
		List<VacancyLookUp> vacancyLookUpList = dao.get(VacancyLookUp.class);
		return vacancyLookUpBuilder
				.createVacancyLookUpDTOList(vacancyLookUpList);
	}

	@Override
	public Boolean jobVacantAlreadyCheck(String vacantposition) {
		return dao.findByPositionVacant(JobVacancy.class, vacantposition) != null;
	}

	@CacheEvict(value = "vacanciesLookup", allEntries = true)
	@Override
	public List<VacancyLookUpDTO> addPositionVacantName(
			String positionVacantName, String jobCode) {
		VacancyLookUp lookUp = new VacancyLookUp();
		lookUp.setName(positionVacantName);
		lookUp.setJobCode(jobCode);
		dao.save(lookUp);
		return getAllVacancyLookUp();

	}

	@CacheEvict(value = "vacanciesLookup", allEntries = true)
	@Override
	public void deletePositionVacancy(long positionvacancyLookUpId) {
		VacancyLookUp vacancyLookUp = dao.findBy(VacancyLookUp.class,
				positionvacancyLookUpId);
		dao.delete(vacancyLookUp);
	}

	@CacheEvict(value = "jobVacancies", allEntries = true)
	@Override
	public JobVacancyDTO getEditJobvacancy(Long id) {

		JobVacancy jobVacancy = dao.findBy(JobVacancy.class, id);
		return jobVacancyBuilder.createJobVacancyDTO(jobVacancy);
	}

	@Override
	public Map<String,Object> getJobVacancyAudit(Long id) {
		Map<String,List<Audit>> jobvacancyAudit=dao.getJobVacancyAudit(id);
		
		return jobVacancyBuilder.ToJobVacancyAuditDTO(jobvacancyAudit);
	}
	
	public Map<String, Object> getOpenJobVacancies(Integer startIndex, Integer endIndex, String status, String searchJobTitle) {
		Map<String, Object> jobVacanciesMap  = dao.getOpenJobVacancies(startIndex,endIndex,status,searchJobTitle);
		List<JobVacancy> jobVacanciesList = (List<JobVacancy>) jobVacanciesMap.get("list");
		Integer noOfRecords = (Integer) jobVacanciesMap.get("size");
		List<JobVacancyDTO> jobVacanciesDTOList = jobVacancyBuilder.createJobVacancyDTOList(jobVacanciesList);
		Map<String, Object> jobVacanciesDTOMap = new HashMap<String, Object>();
		jobVacanciesMap.put("list", jobVacanciesDTOList);
		jobVacanciesMap.put("size", noOfRecords);
		return jobVacanciesMap;
	}

}
