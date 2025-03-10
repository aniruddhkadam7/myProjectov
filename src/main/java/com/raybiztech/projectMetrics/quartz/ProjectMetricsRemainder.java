package com.raybiztech.projectmetrics.quartz;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.raybiztech.mailtemplates.util.ProjectManagementMailConfiguration;
import com.raybiztech.projectMetrics.builder.ProjectSprintsBuilder;


import com.raybiztech.projectmanagement.business.Project;
import com.raybiztech.projectmanagement.dao.ResourceManagementDAO;



@Transactional

@Component("sendProjectMetricsRemainder")

public class ProjectMetricsRemainder {
	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	ProjectManagementMailConfiguration projectManagementMailConfiguration;

	@Autowired
	ResourceManagementDAO resourceManagementDAO;

	@Autowired
	ProjectSprintsBuilder projectSprintsBuilder;

	public void sendProjectMetricsAlert() {

		Logger logger = Logger.getLogger(ProjectMetricsRemainder.class);

		// logger.warn("in project metrics quartz");
		
		//SELECT * FROM `PROJECT` as pr INNER JOIN ProjectSprints as ps ON pr.HiveProjectName = ps.Hive_ProjectName where ps.Hive_ProjectName != '' and DATE(pr.FromDate) <= DATE_SUB(CURDATE(), INTERVAL 15 DAY) AND ps.Hive_ProjectName is not null and pr.STATUS = 'INPROGRESS' GROUP BY ps.Hive_ProjectName 
		
		//getting the list of projects which are in ProjectSprints table by performing inner join

		String query = "SELECT * FROM `PROJECT` as pr INNER JOIN ProjectSprints as ps "
				+ "ON pr.HiveProjectName = ps.Hive_ProjectName "
				+ "where ps.Hive_ProjectName != '' and and DATE(pr.FromDate) <= DATE_SUB(CURDATE(), INTERVAL 15 DAY) and ps.Hive_ProjectName is not null \n"
				+ "and  pr.STATUS = 'INPROGRESS' GROUP BY ps.Hive_ProjectName";

		SQLQuery sqlQuery = sessionFactory.getCurrentSession().createSQLQuery(query);

		sqlQuery.addEntity(Project.class);
		

		List<Project> projectList = sqlQuery.list();
		
		Set<Long> projectMangerIds=new HashSet<Long>();
		
		List<Long> projectListIds = new ArrayList<Long>();
		

		
		for (Project project : projectList) {
			projectListIds.add(project.getId());
			//projectMangerIds.add(project.getProjectManager().getEmployeeId());
		}

		System.out.println("projectListIds:" + projectListIds);
		
		String projectIds = getString(projectListIds);
		
		System.out.println("projectIds:" + projectIds);
		
		

		
		
	//getting the list of projects for which effort variance is submitted one week before of current date	
		String effortQuery = "SELECT ev.`Project_Id` FROM `Effort_Variance` as ev WHERE " 
				+ "ev.`Project_Id` IN("+ projectIds + ")" 
				+ "AND DATE(ev.`Created_Date`) BETWEEN DATE(NOW()-INTERVAL 14 DAY) AND DATE(NOW())";

		SQLQuery effortListQuery = sessionFactory.getCurrentSession().createSQLQuery(effortQuery);

		List<Long> effortListIds = effortListQuery.list();
		

		System.out.println("efffortlistIds:" + effortListIds);
		
		
	//getting the list of projects for which schedule variance is submitted one week before of current date		

		String scheduleQuery = "SELECT sv.`Project_Id` FROM `Schedule_Variance` as sv WHERE " 
				+ "sv.`Project_Id` IN("+ projectIds + ")" 
				+ "AND DATE(sv.`Created_Date`) BETWEEN DATE(NOW()-INTERVAL 14 DAY) AND DATE(NOW())";

		SQLQuery scheduleListQuery = sessionFactory.getCurrentSession().createSQLQuery(scheduleQuery);

		List<Long> scheduleListIds = scheduleListQuery.list();

		System.out.println("schedulelistIds:" + scheduleListIds);
		
		
		//getting non-submission effort variance list
		
		List<Long> nonSubmittedEffortIds = new ArrayList<Long>();
		
		
		for(Long pId:projectListIds)
		{
			
			if(!effortListIds.contains(BigInteger.valueOf(pId)))
			{
				
				nonSubmittedEffortIds.add(pId);
			}
			
			
		}
		
		//nonSubmittedEffortIds.removeAll(effortListIds);
		
		System.out.println("NonSubmittedEffortIDs:" + nonSubmittedEffortIds);
		
		
		//getting non-submission schedule variance list
		
		List<Long> nonSubmittedScheduleIds = new ArrayList<Long>();
		
		for(Long pId:projectListIds)
		{
			
			if(!scheduleListIds.contains(BigInteger.valueOf(pId)))
			{
				
				nonSubmittedScheduleIds.add(pId);
			}
			
			
		}
				
		//nonSubmittedScheduleIds.removeAll(scheduleListIds);
				
		System.out.println("NonSubmittedScheduleIDs:" + nonSubmittedScheduleIds);
		
    	
    	List<Long> commonIds = new ArrayList<Long>(nonSubmittedEffortIds);
    	commonIds.retainAll(nonSubmittedScheduleIds);
    	System.out.println("commonIds:"+commonIds);
    	
    	String commonIdsList = getString(commonIds);
    	System.out.println("commonIdsList:"+commonIdsList);
    	
    	List<Long> remainingEffortIds = new ArrayList<Long>(nonSubmittedEffortIds);
    	remainingEffortIds.removeAll(commonIds);
    	System.out.println("remainingEffortIds:"+remainingEffortIds);
    	
    	String remainingEffortIdsList = getString(remainingEffortIds);
    	System.out.println("remainingEffortIdsList:"+remainingEffortIdsList);
    	
    	List<Long> remainingScheduleIds = new ArrayList<Long>(nonSubmittedScheduleIds);
    	remainingScheduleIds.removeAll(commonIds);
    	System.out.println("remainingScheduleIds:"+remainingScheduleIds);
    	
    	String remainingScheduleIdsList = getString(remainingScheduleIds);
    	System.out.println("remainingScheduleIdsList:"+remainingScheduleIdsList);
    	
    	List<Project> scheduleEffortProjectList = new ArrayList<Project>();
    	List<Project> effortProjectList = new ArrayList<Project>();
    	List<Project> scheduleProjectList = new ArrayList<Project>();
    	
    	
    	/*getting both  effort projects*/
    	
    	if(!remainingEffortIdsList.isEmpty()) {
    	String effort = "SELECT * FROM `PROJECT` WHERE `ID` IN("+ remainingEffortIdsList +")";
        
    	SQLQuery effortQuery1 = sessionFactory.getCurrentSession().createSQLQuery(effort);
    	effortQuery1.addEntity(Project.class);
       
    	 effortProjectList = effortQuery1.list();
    	 
    	System.out.println("effortProjectListsize:" + effortProjectList.size());
    	}
    	
    	/*getting both schedule  projects*/
    	
    	if(!remainingScheduleIdsList.isEmpty() ) {
    	String schedule = "SELECT * FROM `PROJECT` WHERE `ID` IN("+ remainingScheduleIdsList +")";
        
    	SQLQuery scheduleQuery1 = sessionFactory.getCurrentSession().createSQLQuery(schedule);
    	scheduleQuery1.addEntity(Project.class);
       
    	 scheduleProjectList = scheduleQuery1.list();
    	System.out.println("scheduleProjectListsize:" + scheduleProjectList.size());
		
    	}
    
    	/*getting both schedule and effort projects*/
    	if(!commonIdsList.isEmpty()) {
    	String scheduleEffort = "SELECT * FROM `PROJECT` WHERE `ID` IN("+ commonIdsList +")";
        
    	SQLQuery scheduleEffortquery = sessionFactory.getCurrentSession().createSQLQuery(scheduleEffort);
    	
    	scheduleEffortquery.addEntity(Project.class);
       
     scheduleEffortProjectList = scheduleEffortquery.list();
    	System.out.println("scheduleEffortProjectListsize:" + scheduleEffortProjectList.size());
    	
    	}
		
		
    	for(Project p:effortProjectList)
    	{
    		projectMangerIds.add(p.getProjectManager().getEmployeeId());
    	}
    	
    	
    	for(Project p:scheduleProjectList)
    	{
    		projectMangerIds.add(p.getProjectManager().getEmployeeId());
    	}
    	
    	for(Project p:scheduleEffortProjectList)
    	{
    		projectMangerIds.add(p.getProjectManager().getEmployeeId());
    	}
    	
		
		if(projectList != null) {
			
			projectManagementMailConfiguration.sendMetricsRemainderToPM(scheduleEffortProjectList,effortProjectList,scheduleProjectList,projectMangerIds);
		}
		
		

		}
	



 public String getString(List<Long> list) {
	
	 String value = "";
		for (Long listValue : list) {
			
			if (value == "") {
				value = listValue.toString();
			} else {
				value = value + "," + listValue.toString();
			}

		}
	return value;
 }


}







