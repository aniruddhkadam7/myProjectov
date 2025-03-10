package com.raybiztech.expenseManagement.business;

import java.io.Serializable;

public class PaymentMode implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1369421458932646831L;
	private Long id;
	private String paymentType;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getPaymentType() {
		return paymentType;
	}
	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}
	
	

}
