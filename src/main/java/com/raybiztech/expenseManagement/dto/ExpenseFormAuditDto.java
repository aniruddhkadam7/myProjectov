package com.raybiztech.expenseManagement.dto;


public class ExpenseFormAuditDto {
	
	private Long id;
	private Long formId;
	private String expenseNumber;
	private Long toEmployeeId;
	private String toEmployeeName;
	private Long categoryId;
	private String categoryName;
	private Long subCategoryId;
	private String subCategoryName;
	private Long deptId;
	private String deptName;
	private Long projectId;
	private String projectName;
	private Long vendorId;
	private String vendorName;
	private String purpose;
	private String expenditureDate;
	private String country;
	private Long currencyId;
	private String currencyType;
	private String paymentMode;
	private CreditCardDto creditCardDetails;
	private String chequeNumber;
	private String chequeDate;
	private String voucherNumber;
	private String amount;
	private String description;
	private Boolean isReimbursable;
	private String paymentStatus;
	private Long empId;
	private String saltKey;
	private String modifiedBy;
	private String modifiedDate;
	private String persistType;
	private String invoiceNumber;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getFormId() {
		return formId;
	}
	public void setFormId(Long formId) {
		this.formId = formId;
	}
	public Long getToEmployeeId() {
		return toEmployeeId;
	}
	public void setToEmployeeId(Long toEmployeeId) {
		this.toEmployeeId = toEmployeeId;
	}
	public String getToEmployeeName() {
		return toEmployeeName;
	}
	public void setToEmployeeName(String toEmployeeName) {
		this.toEmployeeName = toEmployeeName;
	}
	public Long getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public Long getSubCategoryId() {
		return subCategoryId;
	}
	public void setSubCategoryId(Long subCategoryId) {
		this.subCategoryId = subCategoryId;
	}
	public String getSubCategoryName() {
		return subCategoryName;
	}
	public void setSubCategoryName(String subCategoryName) {
		this.subCategoryName = subCategoryName;
	}
	public Long getDeptId() {
		return deptId;
	}
	public void setDeptId(Long deptId) {
		this.deptId = deptId;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
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
	public Long getVendorId() {
		return vendorId;
	}
	public void setVendorId(Long vendorId) {
		this.vendorId = vendorId;
	}
	public String getVendorName() {
		return vendorName;
	}
	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}
	public String getPurpose() {
		return purpose;
	}
	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}
	public String getExpenditureDate() {
		return expenditureDate;
	}
	public void setExpenditureDate(String expenditureDate) {
		this.expenditureDate = expenditureDate;
	}
	public Long getCurrencyId() {
		return currencyId;
	}
	public void setCurrencyId(Long currencyId) {
		this.currencyId = currencyId;
	}
	public String getCurrencyType() {
		return currencyType;
	}
	public void setCurrencyType(String currencyType) {
		this.currencyType = currencyType;
	}
	public String getPaymentMode() {
		return paymentMode;
	}
	public void setPaymentMode(String paymentMode) {
		this.paymentMode = paymentMode;
	}
	public String getChequeNumber() {
		return chequeNumber;
	}
	public void setChequeNumber(String chequeNumber) {
		this.chequeNumber = chequeNumber;
	}
	public String getChequeDate() {
		return chequeDate;
	}
	public void setChequeDate(String chequeDate) {
		this.chequeDate = chequeDate;
	}
	public String getVoucherNumber() {
		return voucherNumber;
	}
	public void setVoucherNumber(String voucherNumber) {
		this.voucherNumber = voucherNumber;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Boolean getIsReimbursable() {
		return isReimbursable;
	}
	public void setIsReimbursable(Boolean isReimbursable) {
		this.isReimbursable = isReimbursable;
	}
	public Long getEmpId() {
		return empId;
	}
	public void setEmpId(Long empId) {
		this.empId = empId;
	}
	public String getSaltKey() {
		return saltKey;
	}
	public void setSaltKey(String saltKey) {
		this.saltKey = saltKey;
	}
	public String getModifiedBy() {
		return modifiedBy;
	}
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	public String getModifiedDate() {
		return modifiedDate;
	}
	public void setModifiedDate(String modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
	public String getPersistType() {
		return persistType;
	}
	public void setPersistType(String persistType) {
		this.persistType = persistType;
	}
	public String getExpenseNumber() {
		return expenseNumber;
	}
	public void setExpenseNumber(String expenseNumber) {
		this.expenseNumber = expenseNumber;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
	this.country = country;
	}
	public CreditCardDto getCreditCardDetails() {
		return creditCardDetails;
	}
	public void setCreditCardDetails(CreditCardDto creditCardDetails) {
		this.creditCardDetails = creditCardDetails;
	}
	public String getPaymentStatus() {
		return paymentStatus;
	}
	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}
	public String getInvoiceNumber() {
		return invoiceNumber;
	}
	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}
	

}
