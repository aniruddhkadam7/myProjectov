package com.raybiztech.mailtemplates.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.properties.PropBean;
import com.raybiztech.date.Date;
import com.raybiztech.leavemanagement.exceptions.MailCantSendException;
import com.raybiztech.mailtemplates.dao.MailTemplatesDao;
import com.raybiztech.separation.business.Separation;
import com.raybiztech.separation.dto.SeparationDTO;

@Component("separationMailConfiguration")
public class SeparationMailConfiguration {
	@Autowired
	MailTemplatesDao mailTemplateDao;
	@Autowired
	MailContentParser mailContentParser;
	@Autowired
	PropBean propBean;

	Logger logger = Logger.getLogger(SeparationMailConfiguration.class);

	public void sendSeparationMailNotificationToEmployee(Long separationid) {

		Separation separation = mailTemplateDao.findBy(Separation.class,
				separationid);

		Employee employee = separation.getEmployee();

		Map<String, String> details = new HashMap<String, String>();
		details.put("regarding", "<![CDATA[Resignation]]>");
		details.put("toName", employee.getEmployeeFullName());
		details.put("cc", "");
		details.put("to", employee.getEmail());
		details.put("bcc", "");

		String content = mailTemplateDao.getMailContent("Resignation");

		if (content != null) {

			mailContentParser.parseAndSendMail(details, content);
		} else {
			throw new MailCantSendException(
					"Mail Content is not available for 'Resignation' template Type");
		}

	}

	public void sendSeparationMailNotificationToManager(Long separationid) {

		String cc = (String) propBean.getPropData().get("mail.hrMailId");

		Separation separation = mailTemplateDao.findBy(Separation.class,
				separationid);

		Employee employee = separation.getEmployee();

		Employee manager = mailTemplateDao.findBy(Employee.class, employee
				.getManager().getEmployeeId());

		Employee deliveryManager = manager;
		while (!deliveryManager.getRole().equalsIgnoreCase("Delivery Manager")) {

			if (deliveryManager.getRole().equalsIgnoreCase("admin")) {
				deliveryManager = mailTemplateDao.findBy(Employee.class,
						deliveryManager.getManager().getEmployeeId());
				break;
			} else {
				deliveryManager = mailTemplateDao.findBy(Employee.class,
						deliveryManager.getManager().getEmployeeId());
			}

		}

		/*
		 * Employee deliveryManager = mailTemplateDao.findBy(Employee.class,
		 * manager.getManager().getEmployeeId());
		 */

		// cc = cc + ',' + deliveryManager.getEmail();

		if (deliveryManager.getManager().getEmployeeId() != 1001L
				&& deliveryManager.getManager().getEmployeeId() != 1002L
				&& deliveryManager.getManager().getEmployeeId() != 1007L) {
			cc = cc + ',' + deliveryManager.getEmail();
		}

		Map<String, String> details = new HashMap<String, String>();
		details.put("regarding",
				"<![CDATA[Resignation -" + employee.getFullName() + "]]>");
		details.put("toName", manager.getFullName());
		details.put("cc", cc);
		details.put("to", manager.getEmail());
		details.put("name", employee.getFullName());
		details.put("date",
				separation.getResignationDate().toString("dd/MM/yyyy"));
		details.put("bcc", "");

		String content = mailTemplateDao.getMailContent("Manager Mail");
		if (content != null) {
			mailContentParser.parseAndSendMail(details, content);

		} else {
			throw new MailCantSendException(
					"Mail Content is not available for 'Resignation Employee' template Type");
		}
	}

	public void updateMailNotificationToEmployee(SeparationDTO separationDto) {

		String cc = (String) propBean.getPropData().get("mail.hrMailId");

		Employee employee = mailTemplateDao.findBy(Employee.class,
				separationDto.getEmployeeId());

		Employee manager = mailTemplateDao.findBy(Employee.class, employee
				.getManager().getEmployeeId());

		Employee deliveryManager = manager;
		while (!deliveryManager.getRole().equalsIgnoreCase("Delivery Manager")) {

			if (deliveryManager.getRole().equalsIgnoreCase("admin")) {
				deliveryManager = mailTemplateDao.findBy(Employee.class,
						deliveryManager.getManager().getEmployeeId());
				break;
			} else {
				deliveryManager = mailTemplateDao.findBy(Employee.class,
						deliveryManager.getManager().getEmployeeId());
			}

		}

		/*
		 * Employee deliveryManager = mailTemplateDao.findBy(Employee.class,
		 * manager.getManager().getEmployeeId());
		 */
		// cc = cc + ',' + manager.getEmail() + ',' +
		// deliveryManager.getEmail();

		if (deliveryManager.getManager().getEmployeeId() != 1001L
				&& deliveryManager.getManager().getEmployeeId() != 1002L
				&& deliveryManager.getManager().getEmployeeId() != 1007L) {
			cc = cc + ',' + manager.getEmail() + ','
					+ deliveryManager.getEmail();
		} else {
			cc = cc + ',' + manager.getEmail();
		}

		Map<String, String> details = new HashMap<String, String>();
		details.put("regarding",
				"<![CDATA[Resignation - " + employee.getFullName() + "]]>");
		details.put("toName", employee.getEmployeeFullName());
		details.put("cc", cc);
		details.put("to", employee.getEmail());
		details.put("bcc", "");

		String content = mailTemplateDao.getMailContent("Manager Comments");

		if (content != null) {

			mailContentParser.parseAndSendMail(details, content);
		} else {
			throw new MailCantSendException(
					"Mail Content is not available for 'Manager Comments' template Type");
		}
	}

	public void revokeMailNotificationToManager(SeparationDTO separationDTO) {

		String cc = (String) propBean.getPropData().get("mail.hrMailId");

		Separation separation = mailTemplateDao.findBy(Separation.class,
				separationDTO.getSeparationId());

		Employee employee = separation.getEmployee();

		Employee manager = mailTemplateDao.findBy(Employee.class, employee
				.getManager().getEmployeeId());

		Employee deliveryManager = manager;

		while (!deliveryManager.getRole().equalsIgnoreCase("Delivery Manager")) {

			if (deliveryManager.getRole().equalsIgnoreCase("admin")) {

				deliveryManager = mailTemplateDao.findBy(Employee.class,
						deliveryManager.getManager().getEmployeeId());
				break;
			} else {
				deliveryManager = mailTemplateDao.findBy(Employee.class,
						deliveryManager.getManager().getEmployeeId());
			}

		}

		if (deliveryManager.getManager().getEmployeeId() != 1001L
				&& deliveryManager.getManager().getEmployeeId() != 1002L
				&& deliveryManager.getManager().getEmployeeId() != 1007L) {
			cc = cc + ',' + deliveryManager.getEmail();
		}

		/*
		 * Employee deliveryManager = mailTemplateDao.findBy(Employee.class,
		 * manager.getManager().getEmployeeId());
		 */
		// cc = cc + ',' + deliveryManager.getEmail();

		Map<String, String> details = new HashMap<String, String>();
		details.put("regarding",
				"<![CDATA[Resignation - " + employee.getFullName() + "]]>");
		details.put("toName", manager.getFullName());
		details.put("cc", cc);
		details.put("to", manager.getEmail());
		details.put("name", employee.getFullName());
		details.put("bcc", "");
		String content = mailTemplateDao.getMailContent("Resignation Revoke");
		if (content != null) {
			mailContentParser.parseAndSendMail(details, content);
		} else {
			throw new MailCantSendException(
					"Mail Content is not available for 'Resignation Revoke' template Type");
		}

	}

	public void InitiateCcMailNotification(Long separationId) {

		String to = (String) propBean.getPropData().get("mail.supportMailId");

		String cc = "";

		Separation separation = mailTemplateDao.findBy(Separation.class,
				separationId);

		Employee employee = separation.getEmployee();

		Employee manager = mailTemplateDao.findBy(Employee.class, employee
				.getManager().getEmployeeId());

		Employee deliveryManager = manager;
		while (!deliveryManager.getRole().equalsIgnoreCase("Delivery Manager")) {

			if (deliveryManager.getRole().equalsIgnoreCase("admin")) {
				deliveryManager = mailTemplateDao.findBy(Employee.class,
						deliveryManager.getManager().getEmployeeId());
				break;
			} else {
				deliveryManager = mailTemplateDao.findBy(Employee.class,
						deliveryManager.getManager().getEmployeeId());
			}

		}

		if (deliveryManager.getManager().getEmployeeId() != 1001L
				&& deliveryManager.getManager().getEmployeeId() != 1002L
				&& deliveryManager.getManager().getEmployeeId() != 1007L) {
			cc = deliveryManager.getEmail();
		}

		/*
		 * Employee deliveryManager = mailTemplateDao.findBy(Employee.class,
		 * manager.getManager().getEmployeeId());
		 */

		to = to + ',' + manager.getEmail() + ","
				+ propBean.getPropData().get("mail.itAdminMailId") + ","
				+ propBean.getPropData().get("mail.financeMailId") + ","
				+ propBean.getPropData().get("mail.hrMailId");

		Map<String, String> details = new HashMap<String, String>();
		details.put("regarding", "<![CDATA[Initiate Resignation Process]]>");
		details.put("toName", manager.getFullName());
		details.put("cc", cc);
		details.put("to", to);
		details.put("name", employee.getFullName());
		details.put("bcc", "");
		String content = mailTemplateDao
				.getMailContent("Initiate Resignation Process");
		if (content != null) {
			mailContentParser.parseAndSendMail(details, content);
		} else {
			throw new MailCantSendException(
					"Mail Content is not available for 'Initiate Resignation Process' template Type");
		}

	}

	public void sendRemainderNotificationToHr(Separation separation) {
		String to = (String) propBean.getPropData().get("mail.hrMailId");
		Employee employee = separation.getEmployee();
		Map<String, String> details = new HashMap<String, String>();
		details.put("regarding", "<![CDATA[Reminder -" + employee.getFullName()
				+ "]]>");
		details.put("cc", "");
		details.put("to", to);
		details.put("name", employee.getFullName());
		details.put("bcc", "");
		String content = mailTemplateDao.getMailContent("Reminder");
		if (content != null) {
			mailContentParser.parseAndSendMail(details, content);
		} else {
			throw new MailCantSendException(
					"Mail Content is not available for 'Reminder' template Type");
		}

	}

	public void sendAcceptanceToEmployee(Separation separation) {
		String cc = (String) propBean.getPropData().get("mail.hrMailId");
		Employee employee = separation.getEmployee();

		Employee manager = mailTemplateDao.findBy(Employee.class, employee
				.getManager().getEmployeeId());

		Employee deliveryManager = manager;
		while (!deliveryManager.getRole().equalsIgnoreCase("Delivery Manager")) {

			if (deliveryManager.getRole().equalsIgnoreCase("admin")) {
				deliveryManager = mailTemplateDao.findBy(Employee.class,
						deliveryManager.getManager().getEmployeeId());
				break;
			} else {
				deliveryManager = mailTemplateDao.findBy(Employee.class,
						deliveryManager.getManager().getEmployeeId());
			}

		}

		/*
		 * Employee deliveryManager = mailTemplateDao.findBy(Employee.class,
		 * manager.getManager().getEmployeeId());
		 */

		if (deliveryManager.getManager().getEmployeeId() != 1001L
				&& deliveryManager.getManager().getEmployeeId() != 1002L
				&& deliveryManager.getManager().getEmployeeId() != 1007L) {
			cc = cc + ',' + manager.getEmail() + ','
					+ deliveryManager.getEmail();
		} else {

			cc = cc + ',' + manager.getEmail();
		}

		// cc = cc + ',' + manager.getEmail() + ',' +
		// deliveryManager.getEmail();

		Map<String, String> details = new HashMap<String, String>();
		details.put("regarding", "<![CDATA[Resignation Acceptance]]>");
		details.put("cc", cc);
		details.put("to", employee.getEmail());
		details.put("toName", employee.getFullName());
		details.put("date", separation.getRelievingDate()
				.toString("dd/MM/yyyy"));
		details.put("bcc", "");
		String content = mailTemplateDao
				.getMailContent("Resignation Acceptance");
		if (content != null) {
			mailContentParser.parseAndSendMail(details, content);
		} else {
			throw new MailCantSendException(
					"Mail Content is not available for 'Separation Acceptance' template Type");
		}

	}

	/*public void sendSeparationMailNotificationToAccounts(Long separationid) {

		logger.warn("Resignation submitted");

		
		  Separation separation = mailTemplateDao.findBy(Separation.class,
		  separationid);
		  
		  Employee employee = separation.getEmployee();
		  
		  String cc = (String) propBean.getPropData().get("mail.hrMailId");
		 
		  cc = cc + ',' + (String)
		  propBean.getPropData().get("mail.financeMailId"); 
		 
		  Map<String, String> details = new HashMap<String, String>();
		  details.put("regarding", "<![CDATA[Accounts Mail]]>");
		  details.put("toName", employee.getEmployeeFullName());
		 details.put("cc", cc); details.put("to", employee.getEmail());
		  details.put("bcc", "");
		 
		  
		  logger.warn(cc);
		  
		  logger.warn(employee.getEmail());
		 
		  String content = mailTemplateDao.getMailContent("Accounts Mail");
		  
		 if (content != null) { mailContentParser.parseAndSendMail(details,
		 content); } else { throw new MailCantSendException(
		 "Mail Content is not available for 'Resignation' template Type"); }
		 

	}*/
	
	//Below functionality is for sending mail to employee personal email.

	public void mailToemployeePersonalEmail(Long separationId) {

		String cc = "";

		Separation separation = mailTemplateDao.findBy(Separation.class,
				separationId);

		Employee employee = separation.getEmployee();

		//logger.warn("personal mail" + employee.getPersonalEmail());
		
		logger.warn("initiated date"+separation.getInitiatedDate().toString());

		Map<String, String> details = new HashMap<String, String>();
		details.put("regarding", "<![CDATA[Relieved from Services]]>");
		details.put("toName", employee.getFullName());
		details.put("cc", cc);
		details.put("date", separation.getRelievingDate()
				.toString("dd/MM/yyyy"));
         details.put("to", employee.getPersonalEmail());
		details.put("bcc", "");
		String content = mailTemplateDao.getMailContent("Personal Mail");
		if (content != null) {
			mailContentParser.parseAndSendMail(details, content);
		} else {
			throw new MailCantSendException(
					"Mail Content is not available for 'Initiate Resignation Process' template Type");
		}

		logger.warn("mail sent successfullly");

	}

}
