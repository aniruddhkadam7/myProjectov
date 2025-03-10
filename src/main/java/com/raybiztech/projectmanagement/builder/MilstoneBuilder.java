package com.raybiztech.projectmanagement.builder;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.raybiztech.leavemanagement.business.algorithm.DateParser;
import com.raybiztech.projectmanagement.business.ChangeRequest;
import com.raybiztech.projectmanagement.business.Milestone;
import com.raybiztech.projectmanagement.dto.MilestoneDTO;

@Component("milstoneBuilder")
public class MilstoneBuilder {

	public MilestoneDTO covertToDto(Milestone milestone) {
		MilestoneDTO milestonedto = null;

		if (milestone != null) {

			milestonedto = new MilestoneDTO();

			milestonedto.setId(milestone.getId());
			milestonedto.setTitle(milestone.getTitle());
			milestonedto.setProject(milestone.getProject().getProjectName());
			milestonedto
					.setClient(milestone.getProject().getClient().getName());
			milestonedto.setActualDate(DateParser.toString(milestone
					.getActualDate()));
			milestonedto.setPlanedDate(DateParser.toString(milestone
					.getPlanedDate()));
			milestonedto.setMilestonePercentage(milestone
					.getMilestonePercentage());
			milestonedto.setComments(milestone.getComments());
			milestonedto
					.setMilestoneTypeFlag(milestone.getMilestoneTypeFlag() != null ? milestone
							.getMilestoneTypeFlag() : null);
		
			if (milestone.getEffort() != null) {
				milestonedto.setEffort(milestone.getEffort());
			}

			ChangeRequest changeRequest = milestone.getChangeRequest();
			if (changeRequest != null) {
				milestonedto.setCrId(changeRequest.getId());
				milestonedto.setCrName(changeRequest.getName());
				milestonedto.setCrDuration(changeRequest.getDuration());
			}
			milestonedto.setInvoiceStatus(milestone.getInvoiceStatus());

		}

		return milestonedto;
	}

	public Set<MilestoneDTO> getDTOList(Set<Milestone> milstoneset) {

		Set<MilestoneDTO> milestoneDTOSet = null;
		if (milstoneset != null) {
			milestoneDTOSet = new HashSet<MilestoneDTO>();
			for (Milestone milestone : milstoneset) {
				milestoneDTOSet.add(covertToDto(milestone));
			}
		}
		return milestoneDTOSet;

	}
}
