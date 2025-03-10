package com.raybiztech.projectmanagement.invoice.quartz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.raybiztech.appraisals.alerts.builder.AlertBuilder;
import com.raybiztech.appraisals.alerts.business.Alert;
import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.date.Date;
import com.raybiztech.mailtemplates.util.ProjectManagementMailConfiguration;
import com.raybiztech.projectmanagement.business.Milestone;
import com.raybiztech.projectmanagement.dao.ResourceManagementDAO;

@Component("milestoneReminder")
@Transactional
public class MilestoneReminder {
	
		@Autowired
		ResourceManagementDAO resourceManagementDAO;

		@Autowired
		AlertBuilder alertBuilder;

		@Autowired
		ProjectManagementMailConfiguration projectManagementMailConfiguration;

		public void sendReminder() {

			List<Milestone> milestones = resourceManagementDAO
					.getUnclosedBillableMilestones();
			//System.out.println("listSize:"+milestones.size());

			List<Employee> employees = (List<Employee>) (getOutput()
					.get("employees"));
			String cc = String.valueOf(getOutput().get("cc"));
			//System.out.println("cc:"+cc);
			for (Milestone milestone : milestones) {
				

				if (milestone.getActualDate() == null) {

					if (new Date().next().equals(milestone.getPlanedDate()) || new Date().equals(milestone.getPlanedDate())) {
						
						//System.out.println("milestone Planned Date:"+milestone.getPlanedDate());
						projectManagementMailConfiguration.milestoneClosureReminder(
								milestone.getProject(), cc, milestone.getTitle(),
								"Planned End Date",milestone.getPlanedDate());
					}

				} else {

					if (new Date().next().equals(milestone.getActualDate()) || new Date().equals(milestone.getActualDate())) {
						
						//System.out.println("milestone Planned Date:"+milestone.getPlanedDate());
						projectManagementMailConfiguration.milestoneClosureReminder(
								milestone.getProject(), cc, milestone.getTitle(),
								"Actual End Date",milestone.getActualDate());
					}

				}

			}

		}

		public Map<String, Object> getOutput() {
			String cc = " ";

			Map<String, Object> output = new HashMap<String, Object>();
			List<Employee> employees = new ArrayList<Employee>();
			List<Employee> admins = resourceManagementDAO.getAllOfProperty(
					Employee.class, "role", "admin");
			for (Employee employee : admins) {
				if (employee.getStatusName().equalsIgnoreCase("Active")
						&& (!employee.getEmail().equalsIgnoreCase(
								"hr1@raybiztech.com"))) {
					cc = cc + "," + employee.getEmail();
					employees.add(employee);
				}
			}

			List<Employee> financeTeam = resourceManagementDAO.getAllOfProperty(
					Employee.class, "role", "Finance");
			for (Employee employee : financeTeam) {
				if (employee.getStatusName().equalsIgnoreCase("Active")
						&& (!employee.getEmail().equalsIgnoreCase(
								"hr1@raybiztech.com"))) {
					cc = cc + "," + employee.getEmail();
					employees.add(employee);
				}
			}
			output.put("cc", cc);
			output.put("employees", employees);
			return output;
		}

}
