package com.raybiztech.TimeActivity.quartz;

import java.text.ParseException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.raybiztech.TimeActivity.dao.TimeActivityDAO;
import com.raybiztech.TimeActivity.mailNotification.TimeActivityMailNotification;
import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.projectmanagement.business.AllocationDetails;
import com.raybiztech.projectmanagement.business.Project;


// TimeSheetAleart
@Component("timeSheetAleart")
public class TimeSheetAleart {
	
	@Autowired
	TimeActivityDAO timeActivityDAOimpl;
	
	@Autowired
	TimeActivityMailNotification mailNotification;
	
	/*private List<Project> project;

	private List<AllocationDetails> activeAllocation;
	
	private List<Long> employeeId;*/
	
	
	public void sendTimeSheetAleart() throws ParseException {
			// Getting all the Project which is INPROGRESS
			List<Project> project = timeActivityDAOimpl.getAllActiveProjectList() ;
			
			Set<String> projectAddedList = new HashSet<String>();
			
			
			
			// Taking one by one all the project and performing opretion
			for(Project proj : project){
				if(!projectAddedList.contains(proj.getProjectName()))
				{
					projectAddedList.add(proj.getProjectName());
					// Getting Project Manager object by calling getProjectManager() from Project object
					Employee pm = proj.getProjectManager();
					// From Employee object we are taking his/her mail ID
					System.out.println("Project Name = "+proj.getProjectName()+"   Project id = "+proj.getId());
					System.out.println("Manager Name = "+proj.getProjectManager().getFullName());
					String pmMailId = pm.getEmail();
					String empMailId ="";
					// Based on Project Id we are loading AllocationDetails date which have active Employee and Employee allocation is true. 
					List<AllocationDetails> details = timeActivityDAOimpl.getAllAllocationDetails(proj.getId());
					// From AllocationDetails Record one by one we are loading Employee object and from this we are getting his/her mail ID
					for(AllocationDetails list : details){
						Employee employee = list.getEmployee();
						System.out.println("Employee name in "+proj.getProjectName()+" is "+employee.getFullName());
						
						if(employee.getEmail() != pmMailId){
							empMailId = empMailId+","+employee.getEmail();
							
						}
					
				}
					mailNotification.sendTimeSheetAlert(empMailId, pmMailId);
				}
				
				// We are Sending mail to all Employees as well project manager which is allocated in project 
				
			}
	}
}
		
		
	


