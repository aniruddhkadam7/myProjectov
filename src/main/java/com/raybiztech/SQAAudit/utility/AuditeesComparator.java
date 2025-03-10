package com.raybiztech.SQAAudit.utility;

import java.util.Comparator;

import com.raybiztech.SQAAudit.business.SQAAuditees;

public class AuditeesComparator implements Comparator {
	
	@Override
	public int compare(Object auditees1, Object auditees2) {
		SQAAuditees a1 = (SQAAuditees) auditees1;
		SQAAuditees a2 = (SQAAuditees) auditees2;
		
		Long id1 = a1.getAuditee().getEmployeeId();
		Long id2 = a2.getAuditee().getEmployeeId();
		return id1.compareTo(id2);
	}

}
