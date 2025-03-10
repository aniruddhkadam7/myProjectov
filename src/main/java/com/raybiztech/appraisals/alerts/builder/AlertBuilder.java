/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.raybiztech.appraisals.alerts.builder;

import org.springframework.stereotype.Component;

import com.raybiztech.appraisals.alerts.business.Alert;
import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.date.Second;
import com.raybiztech.leavemanagement.business.LeaveDebit;
import com.raybiztech.projectmanagement.business.Milestone;

/**
 *
 * @author naresh
 */
@Component("alertBuilder")
public class AlertBuilder {

	public Alert createApplayLeaveAlert(LeaveDebit leaveDebit) {
		Alert alert = new Alert();
		alert.setAlertType("LeaveApply");
		alert.setAlertStatus(Boolean.FALSE);
		alert.setLatestSatatus(Boolean.FALSE);
		alert.setEmployee(leaveDebit.getEmployee().getManager());
		alert.setInsertOn(new Second());
		alert.setMsg(leaveDebit.getEmployee().getFullName()
				+ " applied for leave");
		alert.setMsgDate(new Second());
		return alert;
	}

	public Alert createLeaveApprovedAlert(LeaveDebit leave) {
		Alert alert = new Alert();
		alert.setAlertType("LeaveApprove");
		alert.setAlertStatus(Boolean.FALSE);
		alert.setLatestSatatus(Boolean.FALSE);
		alert.setEmployee(leave.getEmployee());
		alert.setInsertOn(new Second());
		alert.setMsg("Your leave is approved");
		alert.setMsgDate(new Second());
		return alert;
	}

	public Alert createLeaveCancelledAlert(LeaveDebit leave) {
		Alert alert = new Alert();
		alert.setAlertType("LeaveCancel");
		alert.setAlertStatus(Boolean.FALSE);
		alert.setLatestSatatus(Boolean.FALSE);
		alert.setEmployee(leave.getEmployee().getManager());
		alert.setInsertOn(new Second());
		alert.setMsg(leave.getEmployee().getFullName() + " cancelled the leave");
		alert.setMsgDate(new Second());
		return alert;
	}

	public Alert createLeaveRejectedAlert(LeaveDebit leave) {
		Alert alert = new Alert();
		alert.setAlertType("LeaveReject");
		alert.setAlertStatus(Boolean.FALSE);
		alert.setLatestSatatus(Boolean.FALSE);
		alert.setEmployee(leave.getEmployee());
		alert.setInsertOn(new Second());
		alert.setMsg(" Your leave has been rejected");
		alert.setMsgDate(new Second());
		return alert;
	}

	public Alert createObservationAlert(Employee employee, Employee addedBy) {
		Alert alert = new Alert();
		alert.setAlertType("Observation");
		alert.setAlertStatus(Boolean.FALSE);
		alert.setLatestSatatus(Boolean.FALSE);
		alert.setEmployee(employee);
		alert.setInsertOn(new Second());
		alert.setMsg(" You have been given observation by "
				+ addedBy.getFullName());
		alert.setMsgDate(new Second());
		return alert;
	}

	public Alert createScheduleInterviewAlert(Employee employee,
			String candidateName) {
		Alert alert = new Alert();
		alert.setAlertType("Schedule");
		alert.setAlertStatus(Boolean.FALSE);
		alert.setLatestSatatus(Boolean.FALSE);
		alert.setEmployee(employee);
		alert.setInsertOn(new Second());
		alert.setMsg("Interview has been scheduled with " + candidateName);
		alert.setMsgDate(new Second());
		return alert;
	}

	public Alert createReScheduleInterviewAlert(Employee employee,
			String candidateName) {
		Alert alert = new Alert();
		alert.setAlertType("Re-Schedule");
		alert.setAlertStatus(Boolean.FALSE);
		alert.setLatestSatatus(Boolean.FALSE);
		alert.setEmployee(employee);
		alert.setInsertOn(new Second());
		alert.setMsg("Interview has been scheduled with " + candidateName);
		alert.setMsgDate(new Second());
		return alert;
	}

	public Alert createMilestoneDelayAlert(Milestone milestone,
			Employee employee, String message) {
		Alert alert = new Alert();
		alert.setAlertType("MilestoneDelay");
		alert.setAlertStatus(Boolean.FALSE);
		alert.setLatestSatatus(Boolean.FALSE);
		alert.setEmployee(employee);
		alert.setInsertOn(new Second());
		alert.setMsg(message);
		alert.setMsgDate(new Second());
		return alert;
	}

	public Alert createMilestoneAlert(Milestone milestone,
			Employee employee, String message) {
		Alert alert = new Alert();
		alert.setAlertType("MilestoneDelay");
		alert.setAlertStatus(Boolean.FALSE);
		alert.setLatestSatatus(Boolean.FALSE);
		alert.setEmployee(employee);
		alert.setInsertOn(new Second());
		alert.setMsg(message);
		alert.setMsgDate(new Second());
		return alert;
	}

}
