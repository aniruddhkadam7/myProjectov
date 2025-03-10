package com.raybiztech.leavemanagement.builder;

import java.text.ParseException;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.date.DateRange;
import com.raybiztech.leavemanagement.business.LeaveCategory;
import com.raybiztech.leavemanagement.business.LeaveDebit;
import com.raybiztech.leavemanagement.business.LeaveStatus;
import com.raybiztech.leavemanagement.business.algorithm.DateParser;
import com.raybiztech.leavemanagement.dao.LeaveDAO;
import com.raybiztech.leavemanagement.dto.LeaveApplicationDTO;

@Component("leaveApplicationLeaveBuilder")
public class LeaveApplicationBuilder {

	Logger logger = Logger.getLogger(LeaveApplicationBuilder.class);

	LeaveDAO leaveDAO;

	public LeaveApplicationBuilder() {

	}

	public LeaveApplicationBuilder(LeaveDAO leaveDAO) {

		this.leaveDAO = leaveDAO;
	}

	public LeaveDebit createLeaveEntity(LeaveApplicationDTO leaveDTO) {

		logger.info("in apply leave builder apply leave dto object is : "
				+ leaveDTO.getEmployeeId());

		LeaveDebit leave = new LeaveDebit();
		leave.setId(leaveDTO.getId());

		leave.setEmployee(leaveDAO.findBy(Employee.class,
				leaveDTO.getEmployeeId()));

		leave.setEmployeeComments(leaveDTO.getEmployeeComments());

		LeaveCategory leaveCategory = leaveDAO.getLeaveCategoryByName(leaveDTO
				.getLeaveCategoryName());
		leave.setLeaveCategory(leaveCategory);
		String from = leaveDTO.getFromDate();
		String to = leaveDTO.getToDate();

		logger.info("form is " + from);
		logger.info("to is " + to);
		
		leave.setStatus(LeaveStatus.PendingApproval);

		try {
			leave.setLeaveAppliedOn(DateParser.toDate(leaveDTO.getLeaveAppliedOn()));
			leave.setPeriod(new DateRange(DateParser.toDate(from), DateParser
					.toDate(to)));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage());
		}


		/*Double days = leave.getPeriod().getDuration().getMagnitude();
		days = days + 1;
		leave.setNumberOfDays(days);*/
		logger.info("leave builder leave period is :" + leave.getStatus());
		return leave;
	}

}
