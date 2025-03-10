package com.raybiztech.projectmanagement.quartz;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.raybiztech.date.Date;
import com.raybiztech.leavemanagement.business.algorithm.DateParser;
import com.raybiztech.mailtemplates.util.ProjectManagementMailConfiguration;
import com.raybiztech.projectmanagement.builder.AuditBuilder;
import com.raybiztech.projectmanagement.business.Audit;
import com.raybiztech.projectmanagement.business.Project;
import com.raybiztech.projectmanagement.business.ProjectStatus;
import com.raybiztech.projectmanagement.dao.ResourceManagementDAO;
import com.raybiztech.projectmanagement.service.ProjectServiceImpl;

@Transactional
@Component("closeProjectAlert")
public class CloseProjectAlert {
	
	@Autowired
	SessionFactory sessionFactory;
	
	@Autowired
	ResourceManagementDAO resourceDao;
	
	@Autowired
	AuditBuilder auditBuilder;
	
	@Autowired
	ProjectManagementMailConfiguration projectManagementMailConfiguration;
	
	
	public void getAllInprogressProjects() {
		
		List<Project> projects = resourceDao.getAllInprogressProjects();
		if(projects.size() > 0) {
		Date currentDate = new Date();
		for(Project project : projects) {
			
			Project oldProject = null;
			try {
				oldProject = (Project) project.clone();
			} catch (CloneNotSupportedException ex) {
				java.util.logging.Logger.getLogger(
						ProjectServiceImpl.class.getName()).log(Level.SEVERE, null,
						ex);
			}
			//System.out.println(project.getId()+":"+project.getProjectName());
			Date projectEndDate = project.getPeriod().getMaximum();
			//System.out.println("projectenddate:"+projectEndDate);
			//System.out.println("javaDate:"+projectEndDate.getJavaDate());
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(projectEndDate.getJavaDate());
			calendar.add(Calendar.MONTH, 3);
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			Date dateAfterThreeMonths = null;
			try {
				 dateAfterThreeMonths = DateParser.toDate(sdf.format(calendar.getTime()));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//System.out.println("currentDate:"+currentDate);
			//System.out.println("dateAfterThreeMonths:"+dateAfterThreeMonths);
			
			if(dateAfterThreeMonths.equals(currentDate)) {
				project.setStatus(ProjectStatus.CLOSED);
				resourceDao.update(project);
				
				projectManagementMailConfiguration.closeProject(project.getId());
			}
			
		}
		
		
	}

	}

}
