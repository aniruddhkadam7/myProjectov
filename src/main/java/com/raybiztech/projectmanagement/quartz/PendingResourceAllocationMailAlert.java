package com.raybiztech.projectmanagement.quartz;

import java.text.ParseException;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.raybiztech.mailtemplates.util.ProjectManagementMailConfiguration;
import com.raybiztech.projectmanagement.business.AllocationDetails;
import com.raybiztech.projectmanagement.dao.ResourceManagementDAO;

@Transactional
@Component("PendingResourceAllocationMailAlert")
public class PendingResourceAllocationMailAlert {
	@Autowired
	ResourceManagementDAO resourceManagementDAOImpl;
	@Autowired
	ProjectManagementMailConfiguration projectManagementMailConfiguration;

	Logger logger = Logger.getLogger(PendingResourceAllocationMailAlert.class);

	// getting allocation resource details
	public void alertpendingresourceallocations() throws ParseException {

		List<AllocationDetails> allocationdetails = resourceManagementDAOImpl
				.getallocateresources();
		if (!allocationdetails.isEmpty()) {
			for (AllocationDetails allocationresources : allocationdetails) {
				projectManagementMailConfiguration
						.allocationDateExpiryAlert(allocationresources);
			}
		}

	}
}
