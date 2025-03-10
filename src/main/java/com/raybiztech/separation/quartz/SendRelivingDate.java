package com.raybiztech.separation.quartz;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.raybiztech.appraisals.properties.PropBean;
import com.raybiztech.mailtemplates.util.SeparationMailConfiguration;
import com.raybiztech.separation.business.Separation;
import com.raybiztech.separation.dao.SeparationDao;

@Component("sendRelivingDate")
public class SendRelivingDate {

	@Autowired
	PropBean propBean;

	@Autowired
	SeparationDao separationDaoImpl;

	@Autowired
	SeparationMailConfiguration separationMailConfiguration;

	Logger logger = Logger.getLogger(SendRelivingDate.class);

	public void sendAlert() {
		List<Separation> separationEmployee = separationDaoImpl.getRelivingEndDateEmployees();
		if (!separationEmployee.isEmpty()) {
			for (Separation separation : separationEmployee) {
				separationMailConfiguration.sendRemainderNotificationToHr(separation);
			}
		}
	}

}
