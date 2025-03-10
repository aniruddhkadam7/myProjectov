package com.raybiztech.projectmanagement.invoice.dto;

import java.io.Serializable;

public class DiscountDTO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4798464404812598803L;
	/**
	 * 
	 */
	
	private Long id;
	private String discountType;
	private String discountRate;
	private String discount;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	

	public String getDiscountType() {
		return discountType;
	}

	public void setDiscountType(String discountType) {
		this.discountType = discountType;
	}

	public String getDiscountRate() {
		return discountRate;
	}

	public void setDiscountRate(String discountRate) {
		this.discountRate = discountRate;
	}

	public String getDiscount() {
		return discount;
	}

	public void setDiscount(String discount) {
		this.discount = discount;
	}

	
}
