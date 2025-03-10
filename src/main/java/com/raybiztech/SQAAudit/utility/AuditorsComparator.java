package com.raybiztech.SQAAudit.utility;

import java.util.Comparator;

import com.raybiztech.SQAAudit.business.SQAAuditors;

public class AuditorsComparator implements Comparator {
	
	public int compare(Object auditors1, Object auditors2) {
		SQAAuditors a1 = (SQAAuditors) auditors1;
		SQAAuditors a2 = (SQAAuditors) auditors2;
		
		Long id1 = a1.getAuditor().getEmployeeId();
		Long id2 = a2.getAuditor().getEmployeeId();
		return id1.compareTo(id2);
	}

}
