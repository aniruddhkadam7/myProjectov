package com.raybiztech.projectmanagement.dto;

import java.util.Comparator;

import com.raybiztech.projectmanagement.business.MilestoneAudit;

public class MilestoneAuditSortComparator implements Comparator<MilestoneAudit> {

	@Override
	public int compare(MilestoneAudit o1, MilestoneAudit o2) {
		return o2.getId().compareTo(o1.getId());
	}
}
