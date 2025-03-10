package com.raybiztech.supportmanagement.mailNotification;

/**
*
* @author aprajita
*/

import javax.jms.JMSException;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.dao.DAO;
import com.raybiztech.appraisals.properties.PropBean;
import com.raybiztech.appraisals.security.utils.SecurityUtils;
import com.raybiztech.mail.sender.MessageSender;
import com.raybiztech.supportmanagement.builder.SupportTicketsBuilder;
import com.raybiztech.supportmanagement.business.SupportTickets;
import com.raybiztech.supportmanagement.dao.SupportManagementDAO;

@Component("supportTicketMailNotification")
public class SupportTicketMailNotification {

	@Autowired
	MessageSender messageSender;

	@Autowired
	PropBean propBean;

	@Autowired
	SecurityUtils securityUtils;

	@Autowired
	DAO dao;

	@Autowired
	SupportManagementDAO supportManagementDAO;

	@Autowired
	SupportTicketsBuilder supportTicketsBuilder;

	@Autowired
	SessionFactory sessionFactory;

	public static Logger logger = Logger
			.getLogger(SupportTicketMailNotification.class);

	public PropBean getPropBean() {
		return propBean;
	}

	public void setPropBean(PropBean propBean) {
		this.propBean = propBean;
	}

	// Employee Cancel
	public void sendEmployeeCancelTicketNotification(
			SupportTickets supportTickets) {

		String subjectName = supportTickets.getSubject();
		Long employeeId = supportTickets.getCreatedBy();
		Employee employee = dao.findBy(Employee.class, employeeId);
		String employeeName = employee.getFullName();
		String cc = employee.getEmail();

		String to = "";
		String bcc = "";

		String subject = "<![CDATA[ Ticket status for ]]>" + subjectName;
		StringBuffer buffer = new StringBuffer();
		to = employee.getManager().getEmail();
		buffer.append("<![CDATA[Dear " + employee.getManager().getFullName())
				.append(", <br><br><br> The <b>" + subjectName
						+ "</b> ticket has been cancelled by Mr./Ms. "
						+ employeeName + ". ")
				.append(" <br><br><br> Regards,<br>")
				.append(employeeName
						+ " <br><br> Raybiztech is a CMMI Level 3, ISO 27001:2013 and ISO 9001:2008 Certified Company and The Member of NASSCOM and HYSEA."
						+ "]]>");
		String body = buffer.toString();

		try {
			messageSender.sendMessage("<email><to>" + to + "</to>" + "<cc>"
					+ cc + "</cc>" + "<bcc>" + bcc + "</bcc>" + "<subject>"
					+ subject + "</subject>" + "<body>" + body
					+ "</body></email>");
		} catch (JMSException ex) {
			logger.error(ex.getMessage());
		}
	}

	// Raise Ticket
	public void sendRaiseTicketMailNotification(SupportTickets supTickets) {

		String subjectName = supTickets.getSubject();
		//Long empId = supTickets.getCreatedBy();
		
		
		/*String ccval=manager.getEmail();
		
		for(String emp:empid){
			if(!empId.equals(Long.parseLong(emp))){
			Long tempEmpId=Long.parseLong(emp);
			Employee tempemp=dao.findBy(Employee.class, tempEmpId);
			ccval=ccval+","+tempemp.getEmail();
			}
			
			
		}*/
		Employee employee=dao.findBy(Employee.class, supTickets.getCreatedBy());
		
		String empName = employee.getFullName();
		String cc = employee.getEmail();

		String to = "";
		String bcc = "";

		String subject = "<![CDATA[ Ticket status for ]]>" + subjectName;
		StringBuffer buffer = new StringBuffer();

		// If WorkFLow is there than manager should get mail first
		if (supTickets.getTicketsSubCategory().getWorkFlow()) {
			String[] empid=supTickets.getManagesList().split(",");
			Long empId=Long.parseLong(empid[empid.length-1]);
			Employee manager = dao.findBy(Employee.class, empId);
			to = manager/*.getManager()*/.getEmail();
			//cc=ccval;
			buffer.append(
					"<![CDATA[Dear " + manager.getFullName())
					.append(", <br><br><br> The <b>"
							+ subjectName
							+ "</b> ticket has been raised by Mr./Ms. "
							+ empName
							+ " on "
							+ supTickets.getCreatedDate()
									.toString("dd/MM/yyyy") + ". ")
					.append(" <br><br><br> Regards,<br>")
					.append(empName
							+ " <br><br> Raybiztech is a CMMI Level 3, ISO 27001:2013 and ISO 9001:2008 Certified Company and The Member of NASSCOM and HYSEA."
							+ "]]>");
		}
		// if work flow is not there than mail will go to support Team
		else {
			if (supTickets.getTicketsSubCategory().getTicketsCategory()
					.getEmpDepartment().getDepartmentName()
					.equalsIgnoreCase("Administrative")) {
				to = (String) propBean.getPropData().get("mail.supportMailId");
			}else if(supTickets.getTicketsSubCategory().getTicketsCategory()
					.getEmpDepartment().getDepartmentName()
					.equalsIgnoreCase("Networking")){
				to = (String) propBean.getPropData().get("mail.itAdminMailId");
			}
			else if(supTickets.getTicketsSubCategory().getTicketsCategory()
					.getEmpDepartment().getDepartmentName()
					.equalsIgnoreCase("HR")){
				to = (String) propBean.getPropData().get("mail.hrMailId");
			}
			else if(supTickets.getTicketsSubCategory().getTicketsCategory()
					.getEmpDepartment().getDepartmentName()
					.equalsIgnoreCase("Accounts")){
				to = (String) propBean.getPropData().get("mail.accountsMailId");
			}
			//to = (String) propBean.getPropData().get("mail.supportMailId");
			buffer.append("<![CDATA[Dear Team")
					.append(", <br><br><br> The <b>"
							+ subjectName
							+ "</b> ticket has been raised by Mr./Ms. "
							+ empName
							+ " on "
							+ supTickets.getCreatedDate()
									.toString("dd/MM/yyyy") + ". ")
					.append(" <br><br><br> Regards,<br>")
					.append(empName
							+ " <br><br> Raybiztech is a CMMI Level 3, ISO 27001:2013 and ISO 9001:2008 Certified Company and The Member of NASSCOM and HYSEA."
							+ "]]>");
		}
		String body = buffer.toString();

		try {
			messageSender.sendMessage("<email><to>" + to + "</to>" + "<cc>"
					+ cc + "</cc>" + "<bcc>" + bcc + "</bcc>" + "<subject>"
					+ subject + "</subject>" + "<body>" + body
					+ "</body></email>");

		} catch (JMSException ex) {
			logger.error(ex.getMessage());
		}

	}

	// Update Ticket
	public void sendUpdateTicketMailNotification(SupportTickets suTickets) {

		String subjectName = suTickets.getSubject();
		Long ticketId= suTickets.getId();
		Long updatedBy = suTickets.getUpdatedBy();
		Long createdBy=suTickets.getCreatedBy();
		Employee createdEmployee=dao.findBy(Employee.class, createdBy);
		Employee updatedEmployee = dao.findBy(Employee.class, updatedBy);
		String successfully=null;
		//String empName = employee.getFullName();
		String cc ="";

		String to = "";
		String bcc = "";

		String subject = "<![CDATA[ Ticket status for ]]>" + subjectName;
		StringBuffer buffer = new StringBuffer();
		to=createdEmployee.getEmail();
		// If WorkFLow is there than manager should get mail first
		if (suTickets.getTicketsSubCategory().getWorkFlow()) {
			if(createdBy.equals(updatedBy)){
				if(suTickets.getManagesList()!=null){
					String[] manaString=suTickets.getManagesList().split(",");
					successfully="successfully";
					for(String mId:manaString){
						Employee ma =dao.findBy(Employee.class, Long.parseLong(mId));
						if(cc==null)
							cc=ma.getEmail();
						else
						cc=cc+","+ma.getEmail();
					}
				}
			}else{
				successfully="by Mr./Ms. "+updatedEmployee.getFullName();
				cc=updatedEmployee.getEmail();
			}
		//	to = employee.getManager().getEmail();
			buffer.append(
					"<![CDATA[Dear " + createdEmployee.getFullName())
					.append(", <br><br><br> The ticket no <b>" +ticketId+"("+subjectName+")"
							+ "</b> has been updated "+successfully 
							+ " on "
							+ suTickets.getUpdatedDate().toString("dd/MM/yyyy")
							+ ". ")
					.append(" <br><br><br> Regards,<br>")
					.append(updatedEmployee.getFullName()
							+ " <br><br> Raybiztech is a CMMI Level 3, ISO 27001:2013 and ISO 9001:2008 Certified Company and The Member of NASSCOM and HYSEA."
							+ "]]>");

		}
		// if work flow is not there than mail will go to respective Team
		else {
			cc=createdEmployee.getEmail();
			if(!createdBy.equals(updatedBy)){
				cc=cc+","+updatedEmployee.getEmail();
			}
			if (suTickets.getTicketsSubCategory().getTicketsCategory()
					.getEmpDepartment().getDepartmentName()
					.equalsIgnoreCase("Administrative")) {
				to = (String) propBean.getPropData().get("mail.supportMailId");
			}else if(suTickets.getTicketsSubCategory().getTicketsCategory()
					.getEmpDepartment().getDepartmentName()
					.equalsIgnoreCase("Networking")){
				to = (String) propBean.getPropData().get("mail.itAdminMailId");
			}
			else if(suTickets.getTicketsSubCategory().getTicketsCategory()
					.getEmpDepartment().getDepartmentName()
					.equalsIgnoreCase("HR")){
				to = (String) propBean.getPropData().get("mail.hrMailId");
			}
			else if(suTickets.getTicketsSubCategory().getTicketsCategory()
					.getEmpDepartment().getDepartmentName()
					.equalsIgnoreCase("Accounts")){
				to = (String) propBean.getPropData().get("mail.accountsMailId");
			}
		//	to = (String) propBean.getPropData().get("mail.supportMailId");
			buffer.append("<![CDATA[Dear Team")
					.append(", <br><br><br> The <b>" + subjectName
							+ "</b> ticket has been updates by Mr./Ms. "
							+ updatedEmployee.getFullName() + " on "
							+ suTickets.getUpdatedDate().toString("dd/MM/yyyy")
							+ ". ")
					.append(" <br><br><br> Regards,<br>")
					.append(updatedEmployee.getFullName()
							+ " <br><br> Raybiztech is a CMMI Level 3, ISO 27001:2013 and ISO 9001:2008 Certified Company and The Member of NASSCOM and HYSEA."
							+ "]]>");
		}
		String body = buffer.toString();

		try {
			messageSender.sendMessage("<email><to>" + to + "</to>" + "<cc>"
					+ cc + "</cc>" + "<bcc>" + bcc + "</bcc>" + "<subject>"
					+ subject + "</subject>" + "<body>" + body
					+ "</body></email>");

		} catch (JMSException ex) {
			logger.error(ex.getMessage());
		}
	}

	// Manager Approval
	public void sendManagerApprovedTicketMailNotification(
			SupportTickets suppTickets) {

		Long id = securityUtils.getLoggedEmployeeIdforSecurityContextHolder();
		Employee loggedInEmp = dao.findBy(Employee.class, id);
		String approvedBy = loggedInEmp.getFullName();
		String subjectName = suppTickets.getSubject();
		Employee emp = dao.findBy(Employee.class, suppTickets.getCreatedBy());
		String cc = emp.getEmail();
		if(suppTickets.getManagesList()!=null){
			String[] manaString=suppTickets.getManagesList().split(",");
			for(String mId:manaString){
				Employee ma =dao.findBy(Employee.class, Long.parseLong(mId));
				cc=cc+","+ma.getEmail();
			}
		}
		else
		cc=cc + " ," + emp.getManager().getEmail();

		String to = " ";
		String bcc = " ";

		String subject = "<![CDATA[ Ticket status for ]]>" + subjectName;
		StringBuffer buffer = new StringBuffer();

		if (suppTickets.getTicketsSubCategory().getTicketsCategory()
				.getEmpDepartment().getDepartmentName()
				.equalsIgnoreCase("Administrative")) {
			to = (String) propBean.getPropData().get("mail.supportMailId");
			buffer.append("<![CDATA[Dear Administrative Team")
					.append(", <br><br><br> The <b>" + subjectName
							+ "</b> ticket has been approved by Mr./Ms. "
							+ approvedBy + ". ")
					.append(" <br><br><br> Regards,<br>")
					.append(approvedBy
							+ " <br><br> Raybiztech is a CMMI Level 3, ISO 27001:2013 and ISO 9001:2008 Certified Company and The Member of NASSCOM and HYSEA."
							+ "]]>");

		} else {
			to = (String) propBean.getPropData().get("mail.itAdminMailId");
			buffer.append("<![CDATA[Dear Networking Team")
					.append(", <br><br><br> The <b>" + subjectName
							+ "</b> ticket has been approved by Mr./Ms. "
							+ approvedBy + ". ")
					.append(" <br><br><br> Regards,<br>")
					.append(approvedBy
							+ " <br><br> Raybiztech is a CMMI Level 3, ISO 27001:2013 and ISO 9001:2008 Certified Company and The Member of NASSCOM and HYSEA."
							+ "]]>");

		}
		String body = buffer.toString();

		try {
			messageSender.sendMessage("<email><to>" + to + "</to>" + "<cc>"
					+ cc + "</cc>" + "<bcc>" + bcc + "</bcc>" + "<subject>"
					+ subject + "</subject>" + "<body>" + body
					+ "</body></email>");

		} catch (JMSException ex) {
			logger.error(ex.getMessage());
		}
	}

	// Manager Reject
	public void sendManagerRejectTicketMailNotification(SupportTickets sTickets) {

		Long id = securityUtils.getLoggedEmployeeIdforSecurityContextHolder();
		Employee loggedInEmp = dao.findBy(Employee.class, id);
		String rejectedBy = loggedInEmp.getFullName();
		String subjectName = sTickets.getSubject();
		Employee emp = dao.findBy(Employee.class, sTickets.getCreatedBy());
		String cc = null;//emp.getManager().getEmail();
		if(sTickets.getManagesList()!=null){
			String[] manaString=sTickets.getManagesList().split(",");
			for(String mId:manaString){
				Employee ma =dao.findBy(Employee.class, Long.parseLong(mId));
				if(cc==null)
					cc=ma.getEmail();
				else
				cc=cc+","+ma.getEmail();
			}
		}
		else
		cc=emp.getManager().getEmail();
		String raisedBy = emp.getFirstName();

		String to = " ";
		String bcc = " ";

		String subject = "<![CDATA[ Ticket status for ]]>" + subjectName;
		StringBuffer buffer = new StringBuffer();

		to = emp.getEmail();
		buffer.append("<![CDATA[Dear " + raisedBy)
				.append(", <br><br><br> The <b>" + subjectName
						+ "</b> ticket has been rejected by Mr./Ms. "
						+ rejectedBy + ". ")
				.append(" <br><br><br> Regards,<br>")
				.append(rejectedBy
						+ " <br><br> Raybiztech is a CMMI Level 3, ISO 27001:2013 and ISO 9001:2008 Certified Company and The Member of NASSCOM and HYSEA."
						+ "]]>");
		String body = buffer.toString();

		try {
			messageSender.sendMessage("<email><to>" + to + "</to>" + "<cc>"
					+ cc + "</cc>" + "<bcc>" + bcc + "</bcc>" + "<subject>"
					+ subject + "</subject>" + "<body>" + body
					+ "</body></email>");

		} catch (JMSException ex) {
			logger.error(ex.getMessage());
		}
	}

}
