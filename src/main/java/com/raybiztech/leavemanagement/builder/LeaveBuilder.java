package com.raybiztech.leavemanagement.builder;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.raybiztech.appraisals.builder.EmployeeBuilder;
import com.raybiztech.date.DateRange;
import com.raybiztech.leavemanagement.business.LeaveDebit;
import com.raybiztech.leavemanagement.dto.LeaveDTO;

@Component("leaveBuilder")
public class LeaveBuilder {

	@Autowired
	LeaveCategoryBuilder leaveCategoryBuilder;
	@Autowired
	EmployeeBuilder employeeBuilder;

	Logger logger = Logger.getLogger(LeaveBuilder.class);

	public LeaveDebit createLeaveEntity(LeaveDTO leaveDTO) {
		// TODO Auto-generated method stub

		LeaveDebit leave = new LeaveDebit();
		if (leaveDTO != null) {
			leave.setId(leaveDTO.getId());
			leave.setPeriod(leaveDTO.getPeriod());
			leave.setEmployeeComments(leaveDTO.getEmployeeComments());
			leave.setManagerComments(leaveDTO.getManagerComments());

			leave.setLeaveCategory(leaveCategoryBuilder
					.createLeaveCategoryEntity(leaveDTO.getLeaveCategoryDTO()));

			leave.setStatus(leaveDTO.getStatus());

			leave.setEmployee(employeeBuilder.createEmployeeEntity(leaveDTO
					.getEmployeeDTO()));

			leave.setStatus(leaveDTO.getStatus());
		}

		return leave;
	}

	public LeaveDTO createLeaveDTO(LeaveDebit leave) {
		// TODO Auto-generated method stub

		LeaveDTO leaveDTO = new LeaveDTO();
		if (leave != null) {
			leaveDTO.setId(leave.getId());
			leaveDTO.setPeriod(leave.getPeriod());
			leaveDTO.setEmployeeComments(leave.getEmployeeComments());
			leaveDTO.setManagerComments(leave.getManagerComments());
			leaveDTO.setLeaveCategoryDTO(leaveCategoryBuilder
					.createLeaveCategoryDTO(leave.getLeaveCategory()));
			leaveDTO.setStatus(leave.getStatus());
			leaveDTO.setEmployeeDTO(employeeBuilder.createEmployeeDTO(leave
					.getEmployee()));
			leaveDTO.setNumberOfDays(leave.getNumberOfDays());
			leaveDTO.setLeaveAppliedOn(leave.getLeaveAppliedOn());
			leaveDTO.setAppliedDate(leave.getLeaveAppliedOn().toString(
					"dd MMM yyyy"));

			leaveDTO.setApprovedBy(leave.getApprovedBy() != null ? leave
					.getApprovedBy() : "N/A");

			if (leave.getStatus().toString().equalsIgnoreCase("Approved")) {
				DateRange dateRange = leave.getPeriod();
				if (dateIsInCurrentMonth(dateRange.getMinimum().getJavaDate())
						&& dateIsInCurrentMonth(dateRange.getMaximum()
								.getJavaDate())) {
					leaveDTO.setCanBeCancelledAfterApproval(Boolean.TRUE);

				} else {
					leaveDTO.setCanBeCancelledAfterApproval(Boolean.FALSE);
				}

			} else {
				leaveDTO.setCanBeCancelledAfterApproval(Boolean.FALSE);
			}

		}

		return leaveDTO;
	}

	public List<LeaveDTO> createLeaveDTOEntityList(List<LeaveDebit> leaveSet) {
		// TODO Auto-generated method stub

		List<LeaveDTO> leavesDTOSet = new ArrayList<LeaveDTO>();

		for (LeaveDebit leave : leaveSet) {

			leavesDTOSet.add(createLeaveDTO(leave));
		}

		return leavesDTOSet;
	}

	Boolean dateIsInCurrentMonth(Date date) {

		Calendar calendar = GregorianCalendar.getInstance();
		calendar.setTime(new Date());
		calendar.set(Calendar.DAY_OF_MONTH,
				calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
		Date startDate = calendar.getTime();

		calendar.set(Calendar.DAY_OF_MONTH,
				calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		Date endDate = calendar.getTime();

		if (startDate.before(date) && endDate.after(date)) {
			return Boolean.TRUE;
		} else {
			return Boolean.FALSE;
		}

	}

}
