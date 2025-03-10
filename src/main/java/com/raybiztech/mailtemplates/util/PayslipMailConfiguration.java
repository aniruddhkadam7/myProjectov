package com.raybiztech.mailtemplates.util;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.leavemanagement.exceptions.MailCantSendException;
import com.raybiztech.mailtemplates.dao.MailTemplatesDao;
import com.raybiztech.payslip.dto.PayslipDto;

@Component("payslipMailConfiguration")
public class PayslipMailConfiguration {

	/**
	 * @author shashank
	 */

	@Autowired
	MailTemplatesDao mailTemplatesDaoImpl;

	@Autowired
	MailContentParser mailContentParser;

	public void sendPayslipGeneratedMailNotificationToEmployee(PayslipDto dto,
			String month, String year) {

		Employee employee = mailTemplatesDaoImpl.findBy(Employee.class,
				dto.getEmployeeId());

		Map<String, String> details = new HashMap<String, String>();
		details.put("regarding", "<![CDATA[Payslip]]>" + "-" + month + "-" + year);
		details.put("toName", employee.getFullName());
		details.put("cc", "");
		details.put("to", employee.getEmail());
		details.put("bcc", "");
		details.put("month", month);
		details.put("year", year);

		String content = mailTemplatesDaoImpl.getMailContent("Payslip");

		if (content != null) {
			mailContentParser.parseAndSendMail(details, content);
		} else {
			throw new MailCantSendException(
					"Mail Content is not available for 'Payslip' template Type");
		}

	}
}
