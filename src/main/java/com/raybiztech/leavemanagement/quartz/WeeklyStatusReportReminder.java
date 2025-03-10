package com.raybiztech.leavemanagement.quartz;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.raybiztech.appraisals.builder.EmployeeBuilder;
import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.dao.DAO;
import com.raybiztech.appraisals.dto.EmployeeDTO;
import com.raybiztech.mailtemplates.util.ProjectManagementMailConfiguration;

@Component("weeklyStatusReportReminder")
public class WeeklyStatusReportReminder {

	@Autowired
	EmployeeBuilder employeeBuilder;
	@Autowired
	DAO dao;
	@Autowired
	ProjectManagementMailConfiguration projectManagementMailConfiguration;

	@Transactional
	public void sendStatusMailReminder() {

		List<EmployeeDTO> leads = employeeBuilder.getemployeeDTOList(dao
				.findByManagerName(Employee.class));

		String leadsMails = " ";

		for (EmployeeDTO lead : leads) {

			if (!lead.getId().equals(1001L)) {

				leadsMails = leadsMails + "," + lead.getEmailId();

			}
		}

		projectManagementMailConfiguration
				.weeklyStatusReportReminderMail(leadsMails);

		/*
		 * leaveApplicationAcknowledgement
		 * .weeklyStatusReportReminderMail(leadsMails);
		 */
	}

}
