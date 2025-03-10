package com.raybiztech.projectmanagement.dto;

import java.io.Serializable;

public class AllocationDTO  implements Serializable{ 

	private Long id;
	
   private Float allocation;
	
	private String fromDate ;
	
	private String toDate ;
	
	private String commnets;

	private Boolean billable;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Float getAllocation() {
		return allocation;
	}

	public void setAllocation(Float allocation) {
		this.allocation = allocation;
	}

	public String getFromDate() {
		return fromDate;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public String getToDate() {
		return toDate;
	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}

	public String getCommnets() {
		return commnets;
	}

	public void setCommnets(String commnets) {
		this.commnets = commnets;
	}

	public Boolean getBillable() {
		return billable;
	}

	public void setBillable(Boolean billable) {
		this.billable = billable;
	}
		
}
