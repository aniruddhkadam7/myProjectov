package com.raybiztech.itdeclaration.business;

import java.io.Serializable;

public class ITDeclarationFormInvestments implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long formInvestmentId;
	private Investment investment;
	//private Long investmentId;
	private Long customAmount;

	public Long getFormInvestmentId() {
		return formInvestmentId;
	}

	public void setFormInvestmentId(Long formInvestmentId) {
		this.formInvestmentId = formInvestmentId;
	}

	

	public Long getCustomAmount() {
		return customAmount;
	}

	public void setCustomAmount(Long customAmount) {
		this.customAmount = customAmount;
	}

	public Investment getInvestment() {
		return investment;
	}

	public void setInvestment(Investment investment) {
		this.investment = investment;
	}





	

}
