package com.raybiztech.mailtemplates.util;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.properties.PropBean;
import com.raybiztech.appraisals.security.utils.SecurityUtils;
import com.raybiztech.leavemanagement.exceptions.MailCantSendException;
import com.raybiztech.mailtemplates.dao.MailTemplatesDao;
import com.raybiztech.supportmanagement.business.SupportTickets;
import com.raybiztech.supportmanagement.dao.SupportManagementDAOImpl;

@Component("supportManagementMailConfiguration")
public class SupportManagementMailConfiguration {

	@Autowired
	PropBean propBean;
	@Autowired
	MailTemplatesDao mailTemplatesDaoImpl;
	@Autowired
	MailContentParser mailContentParser;
	@Autowired
	SecurityUtils securityUtils;

	// When Employee Cancel's his ticket
	public void sendTicketCancellationMail(SupportTickets supportTickets) {

		Employee employee = mailTemplatesDaoImpl.findBy(Employee.class, supportTickets.getCreatedBy());

		String regarding = "<![CDATA[ Ticket status for ]]>" + supportTickets.getSubject();

		Map<String, String> details = new HashMap<String, String>();
		details.put("regarding", regarding);
		details.put("name", employee.getFullName());
		details.put("toName", employee.getManager().getFullName());
		details.put("subject", supportTickets.getSubject());
		details.put("cc", employee.getEmail());
		details.put("to", employee.getManager().getEmail());
		details.put("bcc", "");

		String content = mailTemplatesDaoImpl.getMailContent("Ticket Cancellation");

		if (content != null) {
			mailContentParser.parseAndSendMail(details, content);
		} else {
			throw new MailCantSendException("Mail Content is not available for 'Ticket Cancellation' template Type");
		}

	}

	// When Employee Raise New Ticket//
	public void sendNewTicketMail(SupportTickets supportTickets) {

		Employee employee = mailTemplatesDaoImpl.findBy(Employee.class, supportTickets.getCreatedBy());

		String regarding = "<![CDATA[ Ticket status for ]]>" + supportTickets.getSubject();

		String cc = employee.getEmail();
		String to = null;
		String managerName = null;

		// If work flow is there then ticket should go to manager//
		if (supportTickets.getTicketsSubCategory().getWorkFlow() && supportTickets.getManagesList() != null) {
			String[] empid = supportTickets.getManagesList().split(",");
			Long empId = Long.parseLong(empid[empid.length - 1]);
			Employee manager = mailTemplatesDaoImpl.findBy(Employee.class, empId);
			to = manager.getEmail();
			managerName = manager.getFullName();
		} else {
			if (supportTickets.getTicketsSubCategory().getTicketsCategory().getEmpDepartment().getDepartmentName()
					.equalsIgnoreCase("Administrative")) {
				to = (String) propBean.getPropData().get("mail.supportMailId");
			} else if (supportTickets.getTicketsSubCategory().getTicketsCategory().getEmpDepartment()
					.getDepartmentName().equalsIgnoreCase("Networking")) {
				to = (String) propBean.getPropData().get("mail.itAdminMailId");
			} else if (supportTickets.getTicketsSubCategory().getTicketsCategory().getEmpDepartment()
					.getDepartmentName().equalsIgnoreCase("HR")) {
				to = (String) propBean.getPropData().get("mail.hrMailId");
			} else if (supportTickets.getTicketsSubCategory().getTicketsCategory().getEmpDepartment()
					.getDepartmentName().equalsIgnoreCase("Accounts")) {
				to = (String) propBean.getPropData().get("mail.accountsMailId");
			}
			managerName = "Team";
		}

		Map<String, String> details = new HashMap<String, String>();
		details.put("regarding", regarding);
		details.put("name", employee.getFullName());
		details.put("toName", managerName);
		details.put("subject", supportTickets.getSubject());
		details.put("fromDate", supportTickets.getCreatedDate().toString("dd/MM/yyyy"));
		details.put("cc", cc);
		details.put("to", to);
		details.put("bcc", "");

		String content = mailTemplatesDaoImpl.getMailContent("New Support Ticket");

		if (content != null) {
			mailContentParser.parseAndSendMail(details, content);
		} else {
			throw new MailCantSendException("Mail Content is not available for 'New Support Ticket' template Type");
		}

	}

	// on updating ticket//
	public void sendTicketUpdationMail(SupportTickets supportTickets) {

		String regarding = "<![CDATA[ Ticket status for ]]>" + supportTickets.getSubject();
		String cc = null;
		String updatedBy = null;

		Employee createdEmployee = mailTemplatesDaoImpl.findBy(Employee.class, supportTickets.getCreatedBy());
		Employee updatedEmployee = mailTemplatesDaoImpl.findBy(Employee.class, supportTickets.getUpdatedBy());
		String to = createdEmployee.getEmail();

		if (supportTickets.getTicketsSubCategory().getWorkFlow() && supportTickets.getManagesList() != null) {
			if (createdEmployee.equals(updatedEmployee)) {
				updatedBy = "you";
				if (!supportTickets.getManagesList().isEmpty()) {
					for (String employeeid : supportTickets.getManagesList().split(",")) {
						Employee manager = mailTemplatesDaoImpl.findBy(Employee.class, Long.parseLong(employeeid));
						cc = (cc == null) ? manager.getEmail() : cc + manager.getEmail();
					}

				}
			} else {
				updatedBy = "by Mr./Ms. " + updatedEmployee.getFullName();
				cc = updatedEmployee.getEmail();
			}
		} else {
			cc = createdEmployee.getEmail();
			if (!createdEmployee.equals(updatedEmployee)) {
				cc = cc + "," + updatedEmployee.getEmail();
			}
			if (supportTickets.getTicketsSubCategory().getTicketsCategory().getEmpDepartment().getDepartmentName()
					.equalsIgnoreCase("Administrative")) {
				to = (String) propBean.getPropData().get("mail.supportMailId");
			} else if (supportTickets.getTicketsSubCategory().getTicketsCategory().getEmpDepartment()
					.getDepartmentName().equalsIgnoreCase("Networking")) {
				to = (String) propBean.getPropData().get("mail.itAdminMailId");
			} else if (supportTickets.getTicketsSubCategory().getTicketsCategory().getEmpDepartment()
					.getDepartmentName().equalsIgnoreCase("HR")) {
				to = (String) propBean.getPropData().get("mail.hrMailId");
			} else if (supportTickets.getTicketsSubCategory().getTicketsCategory().getEmpDepartment()
					.getDepartmentName().equalsIgnoreCase("Accounts")) {
				to = (String) propBean.getPropData().get("mail.accountsMailId");
			}

			updatedBy = updatedEmployee.getFullName();
		}

		Map<String, String> details = new HashMap<String, String>();
		details.put("regarding", regarding);
		details.put("toName", createdEmployee.getFullName());
		details.put("name", updatedBy);
		details.put("subject", supportTickets.getSubject());
		details.put("fromDate", supportTickets.getUpdatedDate().toString("dd/MM/yyyy"));
		details.put("cc", cc);
		details.put("to", to);
		details.put("bcc", "");

		String content = mailTemplatesDaoImpl.getMailContent("Ticket Updation");

		if (content != null) {
			mailContentParser.parseAndSendMail(details, content);
		} else {
			throw new MailCantSendException("Mail Content is not available for 'New Support Ticket' template Type");
		}

	}

	// Ticket Approval//
	public void sendTicketApproval(SupportTickets supportTickets) {

		Employee loggedInEmployee = mailTemplatesDaoImpl.findBy(Employee.class,
				securityUtils.getLoggedEmployeeIdforSecurityContextHolder());

		Employee ticketCreatedEmployee = mailTemplatesDaoImpl.findBy(Employee.class, supportTickets.getCreatedBy());

		String cc = ticketCreatedEmployee.getEmail();
		String toName;
		String to;

		if (supportTickets.getManagesList() != null) {
			for (String managerId : supportTickets.getManagesList().split(",")) {
				Employee manager = mailTemplatesDaoImpl.findBy(Employee.class, Long.parseLong(managerId));
				cc = cc + "," + manager.getEmail();
			}
		} else {
			cc = cc + "," + ticketCreatedEmployee.getEmail();
		}

		if (supportTickets.getTicketsSubCategory().getTicketsCategory().getEmpDepartment().getDepartmentName()
				.equalsIgnoreCase("Administrative")) {
			to = (String) propBean.getPropData().get("mail.supportMailId");
			toName = "Administrative Team";
		} else {
			to = (String) propBean.getPropData().get("mail.itAdminMailId");
			toName = "Networking Team";
		}

		Map<String, String> details = new HashMap<String, String>();
		details.put("regarding", "<![CDATA[ Ticket status for ]]>" + supportTickets.getSubject());
		details.put("name", loggedInEmployee.getFullName());
		details.put("subject", supportTickets.getSubject());
		details.put("toName", toName);
		details.put("cc", cc);
		details.put("to", to);
		details.put("bcc", "");

		String content = mailTemplatesDaoImpl.getMailContent("Ticket Approval");

		if (content != null) {
			mailContentParser.parseAndSendMail(details, content);
		} else {
			throw new MailCantSendException("Mail Content is not available for 'Ticket Approval' template Type");
		}

	}

	public void sendTicketRejectionMail(SupportTickets supportTickets) {

		Employee loggedInEmployee = mailTemplatesDaoImpl.findBy(Employee.class,
				securityUtils.getLoggedEmployeeIdforSecurityContextHolder());

		Employee ticketCreatedEmployee = mailTemplatesDaoImpl.findBy(Employee.class, supportTickets.getCreatedBy());

		String cc = null;

		String to = ticketCreatedEmployee.getEmail();

		if (supportTickets.getManagesList() != null) {
			for (String managerId : supportTickets.getManagesList().split(",")) {
				Employee manager = mailTemplatesDaoImpl.findBy(Employee.class, Long.parseLong(managerId));
				cc = (cc == null) ? manager.getEmail() : cc + "," + manager.getEmail();
			}
		} else {
			cc = ticketCreatedEmployee.getManager().getEmail();
		}

		Map<String, String> details = new HashMap<String, String>();
		details.put("regarding", "<![CDATA[ Ticket status for ]]>" + supportTickets.getSubject());
		details.put("toName", ticketCreatedEmployee.getFullName());
		details.put("subject", supportTickets.getSubject());
		details.put("name", loggedInEmployee.getFullName());
		details.put("cc", cc);
		details.put("to", to);
		details.put("bcc", "");

		String content = mailTemplatesDaoImpl.getMailContent("Ticket Rejection");

		if (content != null) {
			mailContentParser.parseAndSendMail(details, content);
		} else {
			throw new MailCantSendException("Mail Content is not available for 'Ticket Rejection' template Type");
		}

	}

	public void sendAccessEndDateMailAlert(SupportTickets supportTickets) {

		String cc = (String) propBean.getPropData().get("mail.itAdminMailId");

		String to;
		String toName;
		if (supportTickets.getAssignee() != null) {
			to = supportTickets.getAssignee().getEmail();
			toName = supportTickets.getAssignee().getFullName();
		} else {
			to = (String) propBean.getPropData().get("mail.itAdminMailId");
			toName = "Team";
		}

		Map<String, String> details = new HashMap<String, String>();
		details.put("regarding", "<![CDATA[ Access completion Alert ]]>");
		details.put("toName", toName);
		details.put("subject", supportTickets.getSubject());
		details.put("fromDate", supportTickets.getAccessStartDate().toString("dd/MM/yyyy"));
		details.put("toDate", supportTickets.getAccessEndDate().toString("dd/MM/yyyy"));
		details.put("cc", cc);
		details.put("to", to);
		details.put("bcc", "");
		String content = mailTemplatesDaoImpl.getMailContent("Access End Date Alert");
		if (content != null) {
			mailContentParser.parseAndSendMail(details, content);
		} else {
			throw new MailCantSendException("Mail Content is not available for 'Access End Date Alert' template Type");
		}
	}

	public void sendFoodTicketApprovalAlert(String supportTickets) {

		//String cc = (String) propBean.getPropData().get("mail.supportMailId");
		String to = null;
		String toName = null;

		String subject = "Food Ticket Approval Remainder";

		if (supportTickets != null) {
			for (String managerId : supportTickets.split(",")) {

				Employee manager = mailTemplatesDaoImpl.findBy(Employee.class, Long.parseLong(managerId));
				to = manager.getEmail();
				toName = manager.getEmployeeFullName();
			}
		} else {
			to = (String) propBean.getPropData().get("mail.supportMailId");
			toName = "Team";
		}
		Map<String, String> details = new HashMap<String, String>();
		details.put("regarding", "<![CDATA[ Food Ticket Approval Alert ]]>");
		details.put("toName", toName);
		details.put("subject", subject);
		details.put("cc", "");
		details.put("to", to);
		details.put("bcc", "");
		String content = mailTemplatesDaoImpl.getMailContent("Food Ticket Approval Alert");
		if (content != null) {
			mailContentParser.parseAndSendMail(details, content);
		} else {
			throw new MailCantSendException("Mail Content is not available for 'Access End Date Alert' template Type");
		}

	}

}
