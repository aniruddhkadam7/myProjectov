package com.raybiztech.projectmanagement.invoice.lookup;

import java.io.Serializable;

import com.raybiztech.date.Second;

public class CurrencyToINR implements Serializable {

	/**
	 * shashank
	 */
	private static final long serialVersionUID = 2979708407161210677L;

	private Long id;
	private String currenyType;
	private Long inrAmount;
	private Second createdDate;
	private Long createdBy;
	private Second updatedDate;
	private Long updatedBy;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCurrenyType() {
		return currenyType;
	}

	public void setCurrenyType(String currenyType) {
		this.currenyType = currenyType;
	}

	public Long getInrAmount() {
		return inrAmount;
	}

	public void setInrAmount(Long inrAmount) {
		this.inrAmount = inrAmount;
	}

	public Second getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Second createdDate) {
		this.createdDate = createdDate;
	}

	public Long getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}

	public Second getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Second updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Long getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Long updatedBy) {
		this.updatedBy = updatedBy;
	}

}
