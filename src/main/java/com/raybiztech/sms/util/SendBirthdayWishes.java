package com.raybiztech.sms.util;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.properties.PropBean;
import com.raybiztech.employeeprofile.dao.EmployeeProfileDAOI;

@Component("sendBirthdayWishes")
public class SendBirthdayWishes {

	@Autowired
	EmployeeProfileDAOI employeeDaoimpl;

	@Autowired
	PropBean propBean;

	@Autowired
	SMSUtil smsUtil;

	Logger logger = Logger.getLogger(SendBirthdayWishes.class);

	public void sendSMS() {

		List<Employee> employees = employeeDaoimpl.getBirthdayEmployees();
		logger.warn("Employee " + employees.size());
		if (!employees.isEmpty()) {
			for (Employee employee : employees) {
				if (employee.getPhone() != null) {
					String url = (String) propBean.getPropData().get("SMSAPI");
					if (url != null) {

						url = url.replace("{number(s)}", employee.getPhone());

						String message = employeeDaoimpl.getSMSTemplate();
						message = message.replace(" ", "%20").replace("{Name}",
								employee.getFirstName());

						url = url.replace("{message}", message);

						try {
							smsUtil.sendMessage(url);
						} catch (Exception e) {
							e.printStackTrace();
						}

					}

				}
			}

		}

	}

}
