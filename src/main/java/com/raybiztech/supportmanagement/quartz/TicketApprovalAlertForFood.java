package com.raybiztech.supportmanagement.quartz;

import java.text.ParseException;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.raybiztech.mailtemplates.util.SupportManagementMailConfiguration;
import com.raybiztech.supportmanagement.dao.SupportManagementDAO;

@Component("ticketApprovalAlertForFood")
public class TicketApprovalAlertForFood {

	@Autowired
	SupportManagementDAO supportManagementDAOImpl;

	@Autowired
	SupportManagementMailConfiguration supportManagementMailConfiguration;

	Logger logger = Logger.getLogger(TicketApprovalAlertForFood.class);

	public void foodTicketApproval() throws ParseException {


		List<String> managerList = supportManagementDAOImpl.getManagersListforPendingFoodTicket();

		if (!managerList.isEmpty()) {
			for (String supportTickets : managerList) {
				supportManagementMailConfiguration.sendFoodTicketApprovalAlert(supportTickets);
			}
		}
	}

}
