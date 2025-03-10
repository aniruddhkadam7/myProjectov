package com.raybiztech.recruitment.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.raybiztech.appraisals.dao.DAO;
import com.raybiztech.projectmanagement.business.Audit;
import com.raybiztech.recruitment.business.JobVacancy;
import com.raybiztech.recruitment.dto.JobVacancyDTO;

public interface JobVacancyDAO extends DAO {
	 <T extends Serializable> T findByPositionVacant(Class<T> clazz,
				Serializable name) ;
	 
	Map<String,List<Audit>> getJobVacancyAudit(Long id);
	 //List<JobVacancy> getAllJobVacancies( String searchJobTitle);
	 Map<String, Object> getOpenJobVacancies(Integer startIndex,Integer endIndex,String status,String searchJobTitle);
}
