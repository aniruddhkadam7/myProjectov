package com.raybiztech.leavemanagement.quartz;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.date.DateRange;
import com.raybiztech.leavemanagement.business.LeaveDebit;
import com.raybiztech.leavemanagement.dao.LeaveDAO;
import com.raybiztech.leavemanagement.mailNotification.LeaveApplicationAcknowledgement;
import com.raybiztech.mailtemplates.util.LeaveManagementMailConfiguration;

@Transactional
@Component("pendingApprovalMailAlert")
public class PendingApprovalMailAlert {

	@Autowired
	LeaveDAO leaveDAO;
	@Autowired
	LeaveApplicationAcknowledgement leaveApplicationAcknowledgement;
	@Autowired
	LeaveManagementMailConfiguration leaveManagementMailConfiguration;

	public static Logger logger = Logger
			.getLogger(PendingApprovalMailAlert.class);

	public void alertPendingApprovals(DateRange monthPeriod) {

		logger.warn("Scheduler Sending Mail Alert to APPROVE ALL PENDING LEAVES");
		

		/*
		 * List<Employee> managers = leaveDAO.findByManagerName(Employee.class);
		 * 
		 * String to = "";
		 * 
		 * for (Employee manager : managers) {
		 * 
		 * List<LeaveDebit> reportees = leaveDAO
		 * .getReporteesPendingLeavesOfManager(manager, monthPeriod);
		 * 
		 * List<Employee> empReportees = new ArrayList<Employee>();
		 * 
		 * if (!reportees.isEmpty()) {
		 * 
		 * for (LeaveDebit reportee : reportees) { to = to + "," +
		 * reportee.getEmployee().getEmail();
		 * //empReportees.add(reportee.getEmployee()); }
		 * 
		 * leaveManagementMailConfiguration.sendLeavePendingApprovalAlert(
		 * manager, removeDuplicatemails(to));
		 * 
		 * to = ""; }
		 * 
		 * }
		 */
		
		List<Employee> managers = leaveDAO.findByManagerName(Employee.class);

		String to = "";
		String cc = "";
		String toName = "";
				 

		/*for (Employee manager : managers) {

			List<LeaveDebit> reportees = leaveDAO
					.getReporteesPendingLeavesOfManager(manager, monthPeriod);

			List<Employee> empReportees = new ArrayList<Employee>();

			if (!reportees.isEmpty()) {

				for (LeaveDebit reportee : reportees) {
					to = to + "," + reportee.getEmployee().getEmail();
					//empReportees.add(reportee.getEmployee());
				}

				leaveManagementMailConfiguration.sendLeavePendingApprovalAlert(
						manager, removeDuplicatemails(to));

				to = "";
			}

		}*/
		
		List<LeaveDebit> pendingLeaves = leaveDAO
				.getAllEmployeeMonthlyPendingLeaves(monthPeriod);
		
			for (LeaveDebit leaveDebits : pendingLeaves){
				
				if(leaveDebits.getEmployee().getProjectManager() == null){
					to = leaveDebits.getEmployee().getManager().getEmail();
					toName =leaveDebits.getEmployee().getManager().getFullName();
				}else{
					to = leaveDebits.getEmployee().getProjectManager().getEmail();
					toName =leaveDebits.getEmployee().getProjectManager().getFullName();
					cc =leaveDebits.getEmployee().getManager().getEmail();
				}
				
				if(cc.isEmpty()){
					cc =leaveDebits.getEmployee().getEmail();
				}else{
					cc =cc+","+leaveDebits.getEmployee().getEmail();
				}
				
				/*logger.warn("ccfff"+cc);
				logger.warn("togg"+to);
				logger.warn("tonamefggg"+toName);*/
				
				leaveManagementMailConfiguration.sendLeavePendingApprovalAlert(
						removeDuplicatemails(cc),to ,toName);
				
				to ="";
				cc ="";
				toName ="";
				
				
				
				
			}
		

		/*logger.info("In alertPendingApprovals ");
		Set<Employee> managaers = new HashSet<Employee>();
		Map<Long, Object> managersMap = new HashMap<Long, Object>();
		Set<Employee> projectManagers = new HashSet<Employee>();

		List<Employee> reportees;
		List<LeaveDebit> pendingLeaves = leaveDAO
				.getAllEmployeeMonthlyPendingLeaves(monthPeriod);
		String employees = " ";
		for (LeaveDebit leaveDebits : pendingLeaves) {
			Employee employee = leaveDebits.getEmployee();
			managaers.add(employee.getManager());
			if (leaveDebits.getEmployee().getProjectManager() == null) {
				
				System.out.println("in reporting manager");
				if (managersMap.containsKey(employee.getManager()
						.getEmployeeId())) {
					System.out.println("containes manager");
					reportees = (List<Employee>) managersMap.get(employee
							.getManager().getEmployeeId());
					reportees.add(leaveDebits.getEmployee());
				} else {
					System.out.println("Noncontaines manager");
					reportees = new ArrayList<Employee>();
					reportees.add(leaveDebits.getEmployee());
					managersMap.put(employee.getManager().getEmployeeId(),
							reportees);
				}
			} else {
				
				System.out.println("in projectManger");
				if (managersMap.containsKey(employee.getProjectManager()
						.getEmployeeId())) {
					System.out.println("containes Projectmanager");
					reportees = (List<Employee>) managersMap.get(employee
							.getProjectManager().getEmployeeId());
					reportees = (List<Employee>) managersMap.get(employee
							.getProjectManager().getEmployeeId());
					reportees.add(leaveDebits.getEmployee());
				} else {
					System.out.println("Noncontaines Projectmanager");
					reportees = new ArrayList<Employee>();
					reportees.add(leaveDebits.getEmployee());
					managersMap.put(employee.getProjectManager()
							.getEmployeeId(), reportees);
				}
			}
		}
		
		for(Map.Entry<Long, Object> entry :managersMap.entrySet()){
			System.out.println(entry.getKey());
			System.out.println(entry.getValue());
		}
		for (Employee manager : managaers) {

			for (LeaveDebit leaveDebits : pendingLeaves) {

				if (leaveDebits.getEmployee().getManager().equals(manager)
						&& !leaveDebits.getEmployee().getManager()
								.getEmployeeId().equals(1001)) {
					employees = employees + ","
							+ leaveDebits.getEmployee().getEmail();
				}

			}
			leaveManagementMailConfiguration.sendLeavePendingApprovalAlert(
					manager, employees);

			leaveApplicationAcknowledgement.sendLeavePendingApprovalAlert(
					manager, employees);

			logger.info("Mail sent for a Manager..........");

		}*/

	}

	// Removes Duplicates from String //
	public String removeDuplicatemails(String s) {
		return new LinkedHashSet<String>(Arrays.asList(s.split(",")))
				.toString().replaceAll("(^\\[|\\]$)", "");
	}

	// This was old Code used in alertPendingApprovals Method//

}
