package com.raybiztech.expenseManagement.dto;

import java.util.Comparator;

import com.raybiztech.expenseManagement.dto.ExpenseFormAuditDto;

public class ExpenseAuditComparator implements Comparator<ExpenseFormAuditDto> {

	@Override
	public int compare(ExpenseFormAuditDto o1, ExpenseFormAuditDto o2) {
		return o2.getId().compareTo(o1.getId());
		
	}

}
