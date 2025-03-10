package com.raybiztech.payslip.mailNotification;

import javax.jms.JMSException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.raybiztech.appraisals.business.Employee;

import com.raybiztech.appraisals.dao.DAO;
import com.raybiztech.appraisals.properties.PropBean;
import com.raybiztech.mail.sender.MessageSender;
import com.raybiztech.payslip.dto.PayslipDto;

@Component("payslipGeneratedMailNotification")
public class PayslipGeneratedMailNotification {

	@Autowired
	MessageSender messageSender;

	@Autowired
	DAO dao;

	@Autowired
	PropBean propbean;

	public static Logger logger = Logger
			.getLogger(PayslipGeneratedMailNotification.class);

	public void sendPayslipGeneratedMailNotificationToEmployee(PayslipDto dto,
			String month, String year) {

		Employee employee = dao.findBy(Employee.class, dto.getEmployeeId());

		String to = employee.getEmail();

		String name = employee.getFirstName();

		String cc = "";
		String bcc = "";

		String subject = "<![CDATA[Payslip]]>" + "_" + month + "-" + year;
		StringBuffer buffer = new StringBuffer();

		buffer.append("<![CDATA[Hi")
				.append(" ")
				.append(name)
				.append(",")
				.append("<br><br> Payslip for the Month of  ")
				.append(month)
				.append("-")
				.append(year)
				.append(" ")
				.append("has been generated,You can Download it from OVH(My Profile --> Payslips). <br><br> Password for downloading payslip will be (Last three digits of PAN)+(Last three digits of Bank Account Number)+(Day and Month of Official Birthday(in OVH).<br><br>Example:<br>PAN : ABCDE1234F <br> BAN : 5589526646<br> DOB : 10-05-1990 <br> Password will be :34F6461005.<br><br> If PAN card details are not updated in OVH,Please ignore those details and use other details as credentials to view payslip.<br><br>Example:<br> BAN : 5589526646<br> DOB : 10-05-1990 <br> Password will be :6461005.<br><br> Feel free to contact Finance & HR department for further queries.."
						+ "]]>");

		String body = buffer.toString();

		try {
			messageSender.sendMessage("<email><to>" + to + "</to>" + "<cc>"
					+ cc + "</cc>" + "<bcc>" + bcc + "</bcc>" + "<subject>"
					+ subject + "</subject>" + "<body>" + body
					+ "</body></email>");

		} catch (JMSException e) {
			e.printStackTrace();
		}

	}

}
