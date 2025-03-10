/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.raybiztech.projectmanagement.mailnotifcation;

import javax.jms.JMSException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.properties.PropBean;
import com.raybiztech.date.Date;
import com.raybiztech.mail.sender.MessageSender;
import com.raybiztech.projectmanagement.business.AllocationDetails;
import com.raybiztech.projectmanagement.business.Project;

/**
 *
 * @author anil
 */
@Component("projectAllocationAcknowledgement")
public class ProjectAllocationAcknowledgement {

	@Autowired
	MessageSender messageSender;

	@Autowired
	PropBean propBean;

	public static Logger logger = Logger
			.getLogger(ProjectAllocationAcknowledgement.class);

	public PropBean getPropBean() {
		return propBean;
	}

	public void setPropBean(PropBean propBean) {
		this.propBean = propBean;
	}

	public void sendProjectAllocationAcknowledgement(
			AllocationDetails allocationDetails, Employee employee,
			Project project) {
		// Employee employee=allocationDetails.getEmployee();
		// Project project = allocationDetails.getProject();
		String to = employee.getEmail();
	//	System.out.println("to email:" + to);
		String cc = (String) propBean.getPropData().get("mail.hrMailId");

		if (employee.getManager() != null
				&& !employee.getManager().getEmployeeId().equals(1001L)) {
			cc = cc + " ," + employee.getManager().getEmail();
		}
		if (!employee.getManager().getEmail()
				.equalsIgnoreCase(project.getProjectManager().getEmail())
				&& !employee.getEmail().equalsIgnoreCase(
						project.getProjectManager().getEmail())) {

			cc = cc + " ," + project.getProjectManager().getEmail();
		}
		// System.out.println("cc mail:"+cc);
	//	System.out.println("cc  mails:" + cc);
		String bcc = " ";
		String comments = "";
		// Map<Project, AllocationDetails> allocationDetails=
		// employee.getAllocations();
		if (allocationDetails.getComments() != null) {
			comments = ", " + allocationDetails.getComments();
		}
		// System.out.println("coments:"+comments);

		String subject = "<![CDATA[ Project Allocation ]]>";
		StringBuffer buffer = new StringBuffer();

		buffer.append("<![CDATA[Dear " + employee.getFirstName())
				// .append(employee.getFullName())
				.append(", <br><br> We would like to inform you that you have been allocated to a project in OVH with the following details : ")
				.append("<br> <br> Project Name : " + project.getProjectName())
				.append("<br> Project Start Date : "
						+ allocationDetails.getPeriod().getMinimum().toString())
				.append("<br> Project End Date : "
						+ allocationDetails.getPeriod().getMaximum().toString());
		// .append(allocationDetails.getPeriod().getMinimum().toString())
		// .append(" to ")
		// .append(allocationDetails.getPeriod().getMaximum().toString())
		// .append(comments)
		if (project.getProjectManager().getEmployeeId() != employee
				.getEmployeeId()) {
			buffer.append(" <br> Project Manager : "
					+ project.getProjectManager().getFullName());
		}
		buffer.append(
				".<br> <br>For further queries, feel free to contact concern department ")
				.append(". <br><br><br> Regards,<br>")
				.append(" HR <br><br> Raybiztech is a CMMI Level 3, ISO 27001:2013 and ISO 9001:2008 Certified Company and The Member of NASSCOM and HYSEA.]]>");

		String body = buffer.toString();
		// System.out.println("body:"+body);
		// System.out.println("sending......");
	//	logger.warn("Sending Mail.....");

		try {
			messageSender.sendMessage("<email><to>" + to + "</to>" + "<cc>"
					+ cc + "</cc>" + "<bcc>" + bcc + "</bcc>" + "<subject>"
					+ subject + "</subject>" + "<body>" + body
					+ "</body></email>");
		//	logger.warn("mail sent successfully");
		} catch (JMSException ex) {
			logger.error(ex.getMessage());
		}

	}

	public void sendProjectDeAllocationAcknowledgement(AllocationDetails ad) {

		String to = ad.getEmployee().getEmail();
		// System.out.println("to email:"+to);
		String cc = (String) propBean.getPropData().get("mail.hrMailId");

		if (ad.getEmployee().getManager() != null
				&& !ad.getEmployee().getManager().getEmployeeId().equals(1001L)) {
			cc = cc + " ," + ad.getEmployee().getManager().getEmail();
		}
		if (!ad.getEmployee()
				.getManager()
				.getEmail()
				.equalsIgnoreCase(
						ad.getProject().getProjectManager().getEmail())
				&& !ad.getEmployee()
						.getEmail()
						.equalsIgnoreCase(
								ad.getProject().getProjectManager().getEmail())) {
			cc = cc + " ," + ad.getProject().getProjectManager().getEmail();
		}
		//System.out.println("cc mail:" + cc);
		// System.out.println("cc manager mail:"+ad.getEmployee().getManager().getEmail());
		String bcc = " ";

		String subject = "<![CDATA[ Project De-Allocation ]]>";
		StringBuffer buffer = new StringBuffer();

		buffer.append("<![CDATA[Dear " + ad.getEmployee().getFirstName())
				// .append(ad.getEmployee().getFullName())
				.append(", <br><br> We would like to inform you that you have been released from the ")
				.append(ad.getProject().getProjectName())
				.append(" Project with effect from ")
				.append(new Date())
				.append(".<br> <br>For further queries, feel free to contact concern department ")

				.append(". <br><br><br> Regards,<br>")
				.append("HR <br><br> Raybiztech is a CMMI Level 3, ISO 27001:2013 and ISO 9001:2008 Certified Company and The Member of NASSCOM and HYSEA.]]>");

		String body = buffer.toString();
		// System.out.println("body:"+body);
		// System.out.println("sending......");
		//logger.warn("Sending Mail.....");

		try {
			messageSender.sendMessage("<email><to>" + to + "</to>" + "<cc>"
					+ cc + "</cc>" + "<bcc>" + bcc + "</bcc>" + "<subject>"
					+ subject + "</subject>" + "<body>" + body
					+ "</body></email>");
		//	logger.warn("mail sent successfully");
		} catch (JMSException ex) {
			logger.error(ex.getMessage());
		}

	}

	public void sendProjectUpdationAcknowledgement(
			AllocationDetails allocationDetails, Employee employee,
			Project project) {
		String to = employee.getEmail();
	//	System.out.println("to email:" + to);
		String cc = (String) propBean.getPropData().get("mail.hrMailId");

		if (employee.getManager() != null
				&& !employee.getManager().getEmployeeId().equals(1001L)) {
			cc = cc + " ," + employee.getManager().getEmail();
		}
		if (!employee.getManager().getEmail()
				.equalsIgnoreCase(project.getProjectManager().getEmail())
				&& !employee.getEmail().equalsIgnoreCase(
						project.getProjectManager().getEmail())) {
			cc = cc + " ," + project.getProjectManager().getEmail();
		}
		//System.out.println("cc mails: " + cc);
		String bcc = " ";
		String comments = "";
		if (allocationDetails.getComments() != null) {
			comments = ", " + allocationDetails.getComments();
		}
		String subject = "<![CDATA[ Project ]]>";
		StringBuffer buffer = new StringBuffer();
		buffer.append("<![CDATA[Dear " + employee.getFirstName())
				.append(", <br><br> We would like to inform you that your project has been updated in OVH with following details : ")
				.append("<br> <br> Project Name : " + project.getProjectName())
				.append("<br> Allocation Date : "
						+ allocationDetails.getPeriod().getMinimum().toString())
				.append("<br> End Date : "
						+ allocationDetails.getPeriod().getMaximum().toString());
		if (project.getProjectManager().getEmployeeId() != employee
				.getEmployeeId()) {
			buffer.append(" <br> Project Manager : "
					+ project.getProjectManager().getFullName());
		}
		buffer.append(
				".<br> <br>For further queries, feel free to contact concern department ")
				.append(". <br><br><br> Regards,<br>")
				.append(" HR <br><br> Raybiztech is a CMMI Level 3, ISO 27001:2013 and ISO 9001:2008 Certified Company and The Member of NASSCOM and HYSEA.]]>");

		String body = buffer.toString();
	//	logger.warn("Sending Update Project Mail.......");

		try {
			messageSender.sendMessage("<email><to>" + to + "</to>" + "<cc>"
					+ cc + "</cc>" + "<bcc>" + bcc + "</bcc>" + "<subject>"
					+ subject + "</subject>" + "<body>" + body
					+ "</body></email>");
	//		logger.warn("mail sent successfully");
		} catch (JMSException ex) {
			logger.error(ex.getMessage());
		}

	}
}
