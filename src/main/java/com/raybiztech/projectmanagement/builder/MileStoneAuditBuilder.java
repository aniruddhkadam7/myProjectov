package com.raybiztech.projectmanagement.builder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.dao.DAO;
import com.raybiztech.date.Second;
import com.raybiztech.leavemanagement.business.algorithm.DateParser;
import com.raybiztech.projectmanagement.business.Milestone;
import com.raybiztech.projectmanagement.business.MilestoneAudit;
import com.raybiztech.projectmanagement.business.MilestonePeople;
import com.raybiztech.projectmanagement.business.MilestonePeopleAudit;
import com.raybiztech.projectmanagement.business.Project;
import com.raybiztech.projectmanagement.dto.MileStoneAuditDTO;
import com.raybiztech.projectmanagement.dto.MilestoneAuditSortComparator;
import com.raybiztech.projectmanagement.dto.MilestonePeopleDTO;

@Component("mileStoneBuilder")
public class MileStoneAuditBuilder {

	@Autowired
	DAO dao;

	public List<MileStoneAuditDTO> convertToMileStoneDTOList(
			List<MilestoneAudit> mileStoneauditList) {

		List<MileStoneAuditDTO> mileStoneauditDtoList = null;
		if (mileStoneauditList != null) {

			Collections.sort(mileStoneauditList,
					new MilestoneAuditSortComparator());

			mileStoneauditDtoList = new ArrayList<MileStoneAuditDTO>();
			for (MilestoneAudit milestone : mileStoneauditList) {
				Milestone milestoneData = dao.findBy(Milestone.class,
						milestone.getMilestoneId());
				Project project = milestoneData.getProject();
				MileStoneAuditDTO milestoneauditdto = new MileStoneAuditDTO();

				milestoneauditdto.setActualDate(DateParser.toString(milestone
						.getActualDate()));
				milestoneauditdto.setBillable(milestone.getBillable());
				milestoneauditdto.setComments(milestone.getComments());
				milestoneauditdto.setId(milestone.getId());
				milestoneauditdto.setMilestoneId(milestone.getMilestoneId());
				milestoneauditdto.setModifiedBy(milestone.getModifiedBy());
				milestoneauditdto.setModifiedDate(milestone.getModifiedDate()
						.toString("dd-MMM-yyyy hh:mm a"));
				milestoneauditdto.setPersistType(milestone.getPersistType());
				milestoneauditdto.setPlanedDate(DateParser.toString(milestone
						.getPlanedDate()));
				milestoneauditdto.setTitle(milestone.getTitle());
				milestoneauditdto.setProjectManager(project.getProjectManager()
						.getFullName());
				milestoneauditdto.setProjectName(project.getProjectName());
				milestoneauditdto.setProjectType(String.valueOf(project
						.getType()));
				milestoneauditdto.setProjectStatus(String.valueOf(project
						.getStatus()));
				milestoneauditdto
						.setPeopleDTOs(convertToMilestonePeopleDto(milestone
								.getMilestonePeopleAudit()));

				milestoneauditdto.setMilestonePercentage(milestone
						.getMilestonePercentage() != null ? milestone
						.getMilestonePercentage() : null);
				mileStoneauditDtoList.add(milestoneauditdto);

			}
		}

		return mileStoneauditDtoList;
	}

	public MilestoneAudit convertMileStoneToMilestoneAudit(Milestone milestone,
			Long milestoneId, String persistType) {

		String userName = SecurityContextHolder.getContext()
				.getAuthentication().getName();
		Employee employee = dao.findByEmployeeName(Employee.class, userName);

		MilestoneAudit milAudit = null;
		if (milestone != null) {
			milAudit = new MilestoneAudit();
			milAudit.setActualDate(milestone.getActualDate());
			milAudit.setBillable(milestone.isBillable());
			milAudit.setComments(milestone.getComments());
			milAudit.setMilestoneId(milestoneId);
			milAudit.setModifiedBy(employee.getFullName());
			milAudit.setModifiedDate(new Second());
			milAudit.setPersistType(persistType);
			milAudit.setPlanedDate(milestone.getPlanedDate());
			milAudit.setTitle(milestone.getTitle());
			milAudit.setMilestonePercentage(milestone.getMilestonePercentage() != null ? milestone
					.getMilestonePercentage() : null);
			milAudit.setMilestonePeopleAudit(milestone.getMilestonePeople() != null ? convertTomilestonePeopleAudit(milestone
					.getMilestonePeople()) : null);
			milAudit.setMilestoneTypeFlag(milestone.getMilestoneTypeFlag() != null ? milestone
					.getMilestoneTypeFlag() : null);

		}
		return milAudit;
	}

	public Set<MilestonePeopleAudit> convertTomilestonePeopleAudit(
			Set<MilestonePeople> people) {
		Set<MilestonePeopleAudit> peopleAudit = new HashSet<MilestonePeopleAudit>();
		for (MilestonePeople audit : people) {
			MilestonePeopleAudit milestonePeopleAudit = new MilestonePeopleAudit();
			milestonePeopleAudit.setEmployee(audit.getEmployee());
			milestonePeopleAudit.setEndDate(audit.getEndDate());
			milestonePeopleAudit.setStartDate(audit.getStartDate());
			milestonePeopleAudit.setIsBillable(audit.getIsBillable());
			milestonePeopleAudit.setCount(audit.getCount());
			milestonePeopleAudit.setComments(audit.getComments());
			milestonePeopleAudit.setMonthWorkingDays(audit
					.getMonthWorkingDays());
			milestonePeopleAudit.setLeaves(audit.getLeaves());
			// milestonePeopleAudit.setLeaves(audit.getLeaves());
			milestonePeopleAudit.setTotalDays(audit.getTotalDays());
			milestonePeopleAudit.setTotalValue(audit.getTotalValue());
			milestonePeopleAudit.setHours(audit.getHours());
			milestonePeopleAudit.setHolidays(audit.getHolidays());
			peopleAudit.add(milestonePeopleAudit);
		}
		return peopleAudit;
	}

	public Set<MilestonePeopleDTO> convertToMilestonePeopleDto(
			Set<MilestonePeopleAudit> audits) {
		Set<MilestonePeopleDTO> dtos = new HashSet<MilestonePeopleDTO>();
		for (MilestonePeopleAudit audit : audits) {
			MilestonePeopleDTO dto = new MilestonePeopleDTO();
			dto.setEmployeeName((audit.getEmployee() != null) ? audit
					.getEmployee().getFullName() : null);
			dto.setEmployeeId((audit.getEmployee() != null) ? audit
					.getEmployee().getEmployeeId() : null);
			dto.setEmployeeDesignation((audit.getEmployee() != null) ? audit
					.getEmployee().getDesignation() : null);
			dto.setFromDate((audit.getStartDate() != null) ? audit
					.getStartDate().toString("dd/MM/yyyy") : null);
			dto.setEndDate((audit.getEndDate() != null) ? audit.getEndDate()
					.toString("dd/MM/yyyy") : null);
			dto.setIsBillable(audit.getIsBillable());
			dto.setCount(audit.getCount() != null ? audit.getCount() : null);
			dto.setComments(audit.getComments() != null ? audit.getComments()
					: null);
			dto.setMonthWorkingDays(audit.getMonthWorkingDays() != null ? audit
					.getMonthWorkingDays() : null);
			dto.setHolidays(audit.getHolidays() != null ? audit.getHolidays()
					: null);
			dto.setLeaves(audit.getLeaves() != null ? audit.getLeaves() : null);
			dto.setTotalDays(audit.getTotalDays() != null ? audit
					.getTotalDays() : null);
			dto.setHours(audit.getHours() != null ? audit.getHours() : null);
			dto.setTotalValue(audit.getTotalValue() != null ? audit
					.getTotalValue() : null);
			dtos.add(dto);
		}
		return dtos;
	}

}
