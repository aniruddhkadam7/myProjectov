package com.raybiztech.supportmanagement.quartz;

import java.text.ParseException;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.raybiztech.mailtemplates.util.SupportManagementMailConfiguration;
import com.raybiztech.supportmanagement.business.SupportTickets;
import com.raybiztech.supportmanagement.dao.SupportManagementDAO;

@Component("accessTicketEndDateAlert")
public class AccessTicketEndDateAlert {

	@Autowired
	SupportManagementDAO supportManagementDAOImpl;

	@Autowired
	SupportManagementMailConfiguration supportManagementMailConfiguration;

	Logger logger = Logger.getLogger(AccessTicketEndDateAlert.class);

	public void accessTicketDetails() throws ParseException {

		List<SupportTickets> ticketsList = supportManagementDAOImpl
				.getAccessTicketsWhoseEndDateisInNextFiveDays();
		if (!ticketsList.isEmpty()) {
			for (SupportTickets supportTickets : ticketsList) {

				supportManagementMailConfiguration
						.sendAccessEndDateMailAlert(supportTickets);

			}
		}

	}

}
