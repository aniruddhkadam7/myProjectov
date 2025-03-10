package com.raybiztech.itdeclaration.dto;

public class ITDeclarationFormInvestmentsDTO {
	private Long formInvestmentId;
	private Long investmentId;
	private String investmentName;
	private Long customAmount;

	public Long getFormInvestmentId() {
		return formInvestmentId;
	}

	public void setFormInvestmentId(Long formInvestmentId) {
		this.formInvestmentId = formInvestmentId;
	}

	
	public Long getInvestmentId() {
		return investmentId;
	}

	public void setInvestmentId(Long investmentId) {
		this.investmentId = investmentId;
	}

	public Long getCustomAmount() {
		return customAmount;
	}

	public void setCustomAmount(Long customAmount) {
		this.customAmount = customAmount;
	}

	public String getInvestmentName() {
		return investmentName;
	}

	public void setInvestmentName(String investmentName) {
		this.investmentName = investmentName;
	}

}
