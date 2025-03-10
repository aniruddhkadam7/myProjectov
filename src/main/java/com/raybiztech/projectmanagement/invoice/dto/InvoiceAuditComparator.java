package com.raybiztech.projectmanagement.invoice.dto;

import java.util.Comparator;

public class InvoiceAuditComparator implements Comparator<InvoiceAuditDto> {

	@Override
	public int compare(InvoiceAuditDto o1, InvoiceAuditDto o2) {
		return o2.getId().compareTo(o1.getId());
	}

}
