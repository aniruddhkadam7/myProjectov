package com.raybiztech.projectmetrics.quartz;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.raybiztech.appraisals.dao.DAO;
import com.raybiztech.projectMetrics.business.ProjectSprints;

@Transactional
@Component("sprintWiseActualEffort")
public class ProjectSprintActualEffort  {
	
	@Autowired
	public SessionFactory sessionFactory;
	
	@Autowired
	DAO dao;
	
	public void getSprintWiseAcutalEffort() {
		
		String hqlquery ="SELECT sum(e.hours),e.projectId,e.sprintName FROM EmpoloyeeHiveActivity e WHERE e.sprintName is not null GROUP BY projectId,sprintName";				
		
		Query query = sessionFactory.getCurrentSession().createQuery(hqlquery);
		
		List<Object[]> result = query.list();
		
		for (Object[] row1 : result) {
		
		//System.out.println("hours:" + row1[0]);
		System.out.println("projectId:" + row1[1]);
		System.out.println("sprintName:" + row1[2]);
		
		Criteria criteria2 = sessionFactory.getCurrentSession().
				createCriteria(ProjectSprints.class);	
		criteria2.add(Restrictions.and(Restrictions.eq("projectId", row1[1]),
				Restrictions.eq("versionName", row1[2])));
		
		ProjectSprints sprintExist = (ProjectSprints)criteria2.uniqueResult();
		if(sprintExist!=null) {
		//System.out.println("VersionName:" + sprintExist.getVersionName());
		//System.out.println(" before hours:" + sprintExist.getActuallEffort());
		
		sprintExist.setActuallEffort((double) Math.round((Double) row1[0]*100.0)/100.0);
		System.out.println("status before:" + sprintExist.getStatus());
		if(!sprintExist.getStatus().equalsIgnoreCase("closed") && !sprintExist.getStatus().equalsIgnoreCase("locked")) {
			System.out.println("in if");
			sprintExist.setStatus("In Progress");
			System.out.println("status after:"+sprintExist.getStatus());
		}
	
		//System.out.println(" After effort:" + sprintExist.getActuallEffort());
		dao.update(sprintExist);
		System.out.println("successfully updated");
		}
		}
		
		}
}
