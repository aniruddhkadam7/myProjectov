package com.raybiztech.leavemanagement.builder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.raybiztech.appraisals.builder.EmployeeBuilder;
import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.dto.EmployeeDTO;
import com.raybiztech.date.DateRange;
import com.raybiztech.leavemanagement.business.CarryForwardLeave;
import com.raybiztech.leavemanagement.business.LeaveCategory;
import com.raybiztech.leavemanagement.business.LeaveDebit;
import com.raybiztech.leavemanagement.business.LeaveStatus;
import com.raybiztech.leavemanagement.business.LeaveType;
import com.raybiztech.leavemanagement.dto.LeaveCategorySummaryDTO;
import com.raybiztech.leavemanagement.dto.LeaveSummaryDTO;
import com.raybiztech.leavemanagement.utils.LeaveManagementUtils;

@Component("leaveSummaryBuilder")
public class LeaveSummaryBuilder {

	Logger logger = Logger.getLogger(LeaveSummaryBuilder.class);
	@Autowired
	EmployeeBuilder employeeBuilder;
	@Autowired
	LeaveCategoryBuilder leaveCategoryBuilder;
	@Autowired
	LeaveManagementUtils leaveManagementUtils;

	public LeaveSummaryBuilder() {

	}

	public LeaveSummaryDTO createLeaveSummaryDTO(EmployeeDTO employeeDTO,
			Double totalCreditedLeaves, List<LeaveDebit> leavedebits,
			Set<LeaveCategory> leaveCategories) {

		LeaveSummaryDTO leaveSummaryDTO = new LeaveSummaryDTO();
		leaveSummaryDTO.setEmployeeDTO(employeeDTO);
		List<LeaveCategorySummaryDTO> leaveCategorySummaryDtos = getListOfCategorySummaries(leaveCategories);

		leaveSummaryDTO.setLeaveCategorySummaries(leaveCategorySummaryDtos);
		leaveSummaryDTO.setAllCreditedLeaves(totalCreditedLeaves);

		try {

			Iterator resultIterator2 = leavedebits.iterator();
			while (resultIterator2.hasNext()) {

				Object[] obj = (Object[]) resultIterator2.next();

				for (LeaveCategorySummaryDTO leaveCategorySummaryDTO : leaveCategorySummaryDtos) {

					if (leaveCategorySummaryDTO.getLeaveCategoryDTO().getName()
							.equalsIgnoreCase(String.valueOf(obj[0]))) {
						if (LeaveStatus.PendingApproval.toString()
								.equalsIgnoreCase(String.valueOf(obj[1]))) {
							leaveCategorySummaryDTO.setDaysPending(Double
									.parseDouble(String.valueOf(obj[2])));
						} else if (LeaveStatus.Approved.toString()
								.equalsIgnoreCase(String.valueOf(obj[1]))) {
							leaveCategorySummaryDTO.setDaysTaken(Double
									.parseDouble(String.valueOf(obj[2])));
						} else if (LeaveStatus.CancelAfterApproval.toString()
								.equalsIgnoreCase(String.valueOf(obj[1]))) {
							leaveCategorySummaryDTO
									.setDaysCancelAfterApprovalPending(Double
											.parseDouble(String.valueOf(obj[2])));

							/*
							 * leaveCategorySummaryDTO.setDaysPending(Double.
							 * parseDouble(String.valueOf(obj[2])));
							 */
						}

					}

				}

			}
		} catch (ClassCastException e) {
			throw new ClassCastException(e.getMessage());
		}

		for (LeaveCategorySummaryDTO l : leaveCategorySummaryDtos) {
			if (l.getLeaveCategoryDTO().getLeaveType()
					.equalsIgnoreCase((LeaveType.EARNED.toString()))) {
				leaveSummaryDTO.setAllPendingLeaves(leaveSummaryDTO
						.getAllPendingLeaves() + l.getDaysPending());
			}
			if (l.getLeaveCategoryDTO().getLeaveType()
					.equalsIgnoreCase((LeaveType.EARNED.toString()))) {
				leaveSummaryDTO.setAllTakenLeaves(leaveSummaryDTO
						.getAllTakenLeaves() + l.getDaysTaken());
			}

			if (l.getLeaveCategoryDTO().getLeaveType()
					.equalsIgnoreCase((LeaveType.EARNED.toString()))) {

				leaveSummaryDTO.setAllCancelAfterApprovalLeaves(leaveSummaryDTO
						.getAllCancelAfterApprovalLeaves()
						+ l.getDaysCancelAfterApprovalPending());

			}

			if (l.getLeaveCategoryDTO().getLeaveType()
					.equalsIgnoreCase((LeaveType.LOP.toString()))) {
				leaveSummaryDTO.setAllLOPPendingLeaves(leaveSummaryDTO
						.getAllLOPPendingLeaves() + l.getDaysPending());
			}
			if (l.getLeaveCategoryDTO().getLeaveType()
					.equalsIgnoreCase((LeaveType.LOP.toString()))) {
				leaveSummaryDTO.setAllLOPTakenLeaves(leaveSummaryDTO
						.getAllLOPTakenLeaves() + l.getDaysTaken());
			}

		}

		leaveSummaryDTO.setAllAvailableLeaves(leaveSummaryDTO
				.getAllCreditedLeaves()
				- (leaveSummaryDTO.getAllPendingLeaves()
						+ leaveSummaryDTO.getAllTakenLeaves() + leaveSummaryDTO
							.getAllCancelAfterApprovalLeaves()));
		return leaveSummaryDTO;
	}

	public SortedSet<LeaveSummaryDTO> getLeaveSummaryDtoSet(
			List<LeaveDebit> leaveDebitsList, List<Employee> employees,
			Set<LeaveCategory> leaveCategories,
			List<CarryForwardLeave> leaveCreditsList,
			DateRange financialPeriod, Integer maxLeavesEarned) {

		SortedSet<LeaveSummaryDTO> leaveSumamryDtoSet = new TreeSet<LeaveSummaryDTO>();

		for (Employee employee : employees) {
			LeaveSummaryDTO leaveSummaryDTO = new LeaveSummaryDTO();
			leaveSummaryDTO.setId(employee.getEmployeeId());
			leaveSummaryDTO.setEmployeeDTO(employeeBuilder
					.leaveReportEmployeeDTO(employee));
			leaveSummaryDTO
					.setLeaveCategorySummaries(getListOfCategorySummaries(leaveCategories));
			leaveSumamryDtoSet.add(leaveSummaryDTO);
		}

		try {
			for (LeaveSummaryDTO leaveSummaryDTO : leaveSumamryDtoSet) {

				Iterator resultIterator = leaveCreditsList.iterator();
				while (resultIterator.hasNext()) {

					Object[] obj = (Object[]) resultIterator.next();

					if (leaveSummaryDTO.getEmployeeDTO().getUserName()
							.equalsIgnoreCase(String.valueOf(obj[0]))) {
						leaveSummaryDTO.setCarryForwardedLeaves(Double
								.parseDouble(String.valueOf(obj[1])));
						
						if(leaveSummaryDTO.getCarryForwardedLeaves()>5d){ 
							leaveSummaryDTO.setCarryForwardedLeaves(5d); 
							}

					}

				}

				leaveSummaryDTO
						.setCalculatedCreditedLeaves(leaveManagementUtils
								.getCreditedLeaves(leaveSummaryDTO
										.getEmployeeDTO().getId(),
										financialPeriod));
				
				if(leaveSummaryDTO.getCalculatedCreditedLeaves() > maxLeavesEarned){
					leaveSummaryDTO.setCalculatedCreditedLeaves(new Double(maxLeavesEarned));
				}

				if (leaveSummaryDTO.getCarryForwardedLeaves()
						+ leaveSummaryDTO.getCalculatedCreditedLeaves() >= maxLeavesEarned) {
					leaveSummaryDTO.setAllCreditedLeaves(new Double(
							maxLeavesEarned));
				} else {
					leaveSummaryDTO.setAllCreditedLeaves(leaveSummaryDTO
							.getCarryForwardedLeaves()
							+ leaveSummaryDTO.getCalculatedCreditedLeaves());
				}

				Iterator resultIterator2 = leaveDebitsList.iterator();
				while (resultIterator2.hasNext()) {

					Object[] obj = (Object[]) resultIterator2.next();

					if (leaveSummaryDTO.getEmployeeDTO().getUserName()
							.equalsIgnoreCase(String.valueOf(obj[0]))) {
						for (LeaveCategorySummaryDTO leaveCategorySummaryDTO : leaveSummaryDTO
								.getLeaveCategorySummaries()) {

							if (leaveCategorySummaryDTO.getLeaveCategoryDTO()
									.getName()
									.equalsIgnoreCase(String.valueOf(obj[1]))) {
								if (LeaveStatus.PendingApproval.toString()
										.equalsIgnoreCase(
												String.valueOf(obj[2]))) {
									leaveCategorySummaryDTO
											.setDaysPending(Double
													.parseDouble(String
															.valueOf(obj[3])));
								} else if (LeaveStatus.Approved.toString()
										.equalsIgnoreCase(
												String.valueOf(obj[2]))) {
									leaveCategorySummaryDTO
											.setDaysTaken(Double
													.parseDouble(String
															.valueOf(obj[3])));
								}

							}
						}

					}

				}

			}
			for (LeaveSummaryDTO leaveSummaryDTO : leaveSumamryDtoSet) {
				Double availedLeaves = 0.0;
				for (LeaveCategorySummaryDTO leaveCategorySummaryDTO : leaveSummaryDTO
						.getLeaveCategorySummaries()) {

					if (leaveCategorySummaryDTO.getLeaveCategoryDTO()
							.getLeaveType()
							.equalsIgnoreCase(LeaveType.EARNED.toString())) {
						availedLeaves = availedLeaves
								+ leaveCategorySummaryDTO.getDaysPending()
								+ leaveCategorySummaryDTO.getDaysScheduled()
								+ leaveCategorySummaryDTO.getDaysTaken();

					}

				}

				leaveSummaryDTO.setAllAvailableLeaves(leaveSummaryDTO
						.getAllCreditedLeaves() - availedLeaves);

			}

		} catch (ClassCastException e) {
			throw new ClassCastException(e.getMessage());
		}
		return leaveSumamryDtoSet;

	}

	public List<LeaveCategorySummaryDTO> getListOfCategorySummaries(
			Set<LeaveCategory> leaveCategories) {
		List<LeaveCategorySummaryDTO> leaveCategorySummaryDtos = new ArrayList<LeaveCategorySummaryDTO>();

		for (LeaveCategory leaveCategory : leaveCategories) {
			LeaveCategorySummaryDTO categorySummaryDTO = new LeaveCategorySummaryDTO();
			categorySummaryDTO.setId(leaveCategory.getId());
			categorySummaryDTO.setLeaveCategoryDTO(leaveCategoryBuilder
					.createLeaveCategoryDTO(leaveCategory));
			leaveCategorySummaryDtos.add(categorySummaryDTO);
		}
		return leaveCategorySummaryDtos;
	}

}
