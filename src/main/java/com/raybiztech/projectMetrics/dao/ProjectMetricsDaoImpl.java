package com.raybiztech.projectMetrics.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.raybiztech.TimeActivity.business.EmpoloyeeHiveActivity;
import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.dao.DAOImpl;
import com.raybiztech.projectMetrics.business.EffortVariance;
import com.raybiztech.projectMetrics.business.ProjectSprints;
import com.raybiztech.projectMetrics.business.ProjectSprintsAudit;
import com.raybiztech.projectMetrics.business.ScheduleVariance;

@Repository("projectMetricsDao")
public class ProjectMetricsDaoImpl extends DAOImpl implements ProjectMetricsDao {

	@Override
	public List<ScheduleVariance> getOverAllScheduleVariance(Long projectId) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				ScheduleVariance.class);
		criteria.createAlias("project", "project");
		criteria.add(Restrictions.eq("project.id", projectId));
		criteria.addOrder(Order.desc("id"));
		criteria.setResultTransformer(criteria.DISTINCT_ROOT_ENTITY);
		return criteria.list();
	}

	
	@Override
	public Map<String, Object> getSprintWiseTimeSheet(String hiveProjectName, String sprintName) {
		
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(EmpoloyeeHiveActivity.class);
		criteria.add(Restrictions.and(Restrictions.eq("projectName", hiveProjectName),
				Restrictions.eq("sprintName", sprintName)));
		
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("list", criteria.list());
		map.put("size",criteria.list().size());
		
		return map;
	}

	

	@Override
	public List<ProjectSprintsAudit> getAuditForMetrics(String hiveProjectName) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				ProjectSprintsAudit.class);
		criteria.add(Restrictions.eq("projectName", hiveProjectName));
		return criteria.list();
	}

	
	@Override
	public List<ProjectSprints> getScheduleVariance(String hiveProjectName) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				ProjectSprints.class);
		criteria.add(Restrictions.eq("projectName", hiveProjectName));
		criteria.add(Restrictions.not(Restrictions.
				ilike("versionName", "Backlog", MatchMode.ANYWHERE)));
		return criteria.list();
				
	}
	
	@Override
	public List<EffortVariance> getEffortVarianceList(Long projectId) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				EffortVariance.class);
		criteria.createAlias("project", "project");
		criteria.add(Restrictions.eq("project.id", projectId));
		criteria.addOrder(Order.desc("id"));
		criteria.setResultTransformer(criteria.DISTINCT_ROOT_ENTITY);
		return criteria.list();
	}

	@Override
	public List<ProjectSprints> getProjectEffortVariance(String hiveProjectName) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				ProjectSprints.class);
		criteria.add(Restrictions.eq("projectName", hiveProjectName));
		criteria.add(Restrictions.not(Restrictions.
				ilike("versionName", "Backlog", MatchMode.ANYWHERE)));
		return criteria.list();
	}


	@Override
	public int checkingWorkpages(String hiveProjectName) {
		/*System.out.println("In workapackage criteria");
		Criteria criteria=sessionFactory.getCurrentSession().createCriteria(EmpoloyeeHiveActivity.class);
		//criteria.add(Restrictions.and(Restrictions.eq("projectName", hiveProjectName), Restrictions.isNotNull("taskId")));
		Criterion criterion=Restrictions.eq("projectName", hiveProjectName);
		Criterion criterion2=Restrictions.isNotEmpty("taskId");
		Criterion finalCriteria=Restrictions.and(criterion,criterion2);
		criteria.add(finalCriteria);*/
		/*ProjectionList projections = Projections.projectionList();
		projections.add(Projections.property("taskId"));
		criteria.setProjection(projections);*/
		String sql="SELECT * FROM `EmpoloyeeHiveActivity` WHERE `projectName` LIKE '"+hiveProjectName+"' AND `hive_TaskId` IS NOT NULL ";
		SQLQuery query = getSessionFactory().getCurrentSession().createSQLQuery(sql);
		query.addEntity(EmpoloyeeHiveActivity.class);
		int size=query.list().size();
		System.out.println("list size:"+size);
		return size;
	}


	@Override
	public Boolean checkingForProjectNameMatching(String hiveProjectName) {
		System.out.println("in name matching criteria");
		Criteria criteria =sessionFactory.getCurrentSession().createCriteria(ProjectSprints.class);
		criteria.add(Restrictions.eq("projectName", hiveProjectName));
		Boolean nameExist=criteria.list().size()!=0?true:false;
		System.out.println("name exist:"+nameExist);
		return nameExist;
	}
}
