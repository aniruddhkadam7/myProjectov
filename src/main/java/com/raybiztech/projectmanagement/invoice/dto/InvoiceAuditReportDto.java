package com.raybiztech.projectmanagement.invoice.dto;

import java.util.List;

public class InvoiceAuditReportDto {

	private Integer key;// used in html for orderby in ng-repeat
	private String monthName;
	private String year;
	private Long invoiceAmountTotal;
	private Long finalAmountTotal;
	private Long receivedAmountTotal;
	private Long balanceAmountTotal;
	private Long totalTaxAmount;
	private String invoiceStatus;
	private Long tdsAmountTotal;
	private Long netAmountTotal;
	private List<InvoiceAuditDto> auditDtos;
	private String clientName;
	private Long clientId;
	private Long projectId;
	private String projectName;
	

	public Integer getKey() {
		return key;
	}

	public void setKey(Integer key) {
		this.key = key;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getMonthName() {
		return monthName;
	}

	public void setMonthName(String monthName) {
		this.monthName = monthName;
	}

	public Long getInvoiceAmountTotal() {
		return invoiceAmountTotal;
	}

	public void setInvoiceAmountTotal(Long invoiceAmountTotal) {
		this.invoiceAmountTotal = invoiceAmountTotal;
	}

	public Long getFinalAmountTotal() {
		return finalAmountTotal;
	}

	public void setFinalAmountTotal(Long finalAmountTotal) {
		this.finalAmountTotal = finalAmountTotal;
	}

	public List<InvoiceAuditDto> getAuditDtos() {
		return auditDtos;
	}

	public void setAuditDtos(List<InvoiceAuditDto> auditDtos) {
		this.auditDtos = auditDtos;
	}

	public Long getReceivedAmountTotal() {
		return receivedAmountTotal;
	}

	public void setReceivedAmountTotal(Long receivedAmountTotal) {
		this.receivedAmountTotal = receivedAmountTotal;
	}

	public Long getBalanceAmountTotal() {
		return balanceAmountTotal;
	}

	public void setBalanceAmountTotal(Long balanceAmountTotal) {
		this.balanceAmountTotal = balanceAmountTotal;
	}

	public String getInvoiceStatus() {
		return invoiceStatus;
	}

	public void setInvoiceStatus(String invoiceStatus) {
		this.invoiceStatus = invoiceStatus;
	}

	public Long getTotalTaxAmount() {
		return totalTaxAmount;
	}

	public void setTotalTaxAmount(Long totalTaxAmount) {
		this.totalTaxAmount = totalTaxAmount;
	}

	public Long getTdsAmountTotal() {
		return tdsAmountTotal;
	}

	public void setTdsAmountTotal(Long tdsAmountTotal) {
		this.tdsAmountTotal = tdsAmountTotal;
	}

	public Long getNetAmountTotal() {
		return netAmountTotal;
	}

	public void setNetAmountTotal(Long netAmountTotal) {
		this.netAmountTotal = netAmountTotal;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public Long getClientId() {
		return clientId;
	}

	public void setClientId(Long clientId) {
		this.clientId = clientId;
	}

	public Long getProjectId() {
		return projectId;
	}

	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	
	
	
	
	

}
