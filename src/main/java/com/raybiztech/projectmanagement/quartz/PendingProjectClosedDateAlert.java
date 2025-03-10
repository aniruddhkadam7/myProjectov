package com.raybiztech.projectmanagement.quartz;

import java.text.ParseException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.raybiztech.mailtemplates.util.ProjectManagementMailConfiguration;
import com.raybiztech.projectmanagement.business.Project;
import com.raybiztech.projectmanagement.service.ProjectService;

@Service("pendingProjectClosedDateAlert")
public class PendingProjectClosedDateAlert {

	@Autowired
	ProjectService projectServiceImpl;
	@Autowired
	ProjectManagementMailConfiguration projectManagementMailConfiguration;

	// getting project details
	public void projectDetails() throws ParseException {

		List<Project> projectdetails = projectServiceImpl
				.getProjectWhoseEndDateisInNextFiveDays();
		if (!projectdetails.isEmpty()) {
			for (Project project : projectdetails) {
				projectManagementMailConfiguration.sendProjectEndDateAlert(project);
			}
		}

	}
}
