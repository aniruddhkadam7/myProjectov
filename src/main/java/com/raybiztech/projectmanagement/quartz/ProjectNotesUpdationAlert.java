package com.raybiztech.projectmanagement.quartz;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.mailtemplates.util.ProjectManagementMailConfiguration;
import com.raybiztech.projectmanagement.business.Project;
import com.raybiztech.projectmanagement.dao.ResourceManagementDAO;

@Component("projectNotesUpdationAlert")
public class ProjectNotesUpdationAlert {

	@Autowired
	ResourceManagementDAO resourceManagementDAO;

	@Autowired
	ProjectManagementMailConfiguration projectManagementMailConfiguration;

	Logger logger = Logger.getLogger(ProjectNotesUpdationAlert.class);

	public void sendProjectNotesAlerts() {

		List<Project> projects = resourceManagementDAO
				.getProjectsWhoseStartDateInLastFiveDays();

		if (!projects.isEmpty()) {
			for (Project project : projects) {
				Employee deliverymanager = resourceManagementDAO
						.getDeliveryManagerofProject(project);
				if (deliverymanager != null) {
					logger.warn("Send Project Notes Update Alert for project "
							+ project.getProjectName() + " and Delivery Manager "
							+ deliverymanager.getFullName());
					projectManagementMailConfiguration
							.SendProjectNotesUpdationMail(deliverymanager,
									project);
				}

			}

		}

	}
}
