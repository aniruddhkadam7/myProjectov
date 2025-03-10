package com.raybiztech.assetmanagement.dto;

import java.io.Serializable;

public class VendorDto implements Serializable {

	/**
	 * Aprajita
	 */
	private static final long serialVersionUID = -8916783566708005021L;

	private Long vendorId;
	private String vendorName;
	private String vendorAddress;
	private String vendorCity;
	private String vendorState;
	private String vendorPincode;
	private String vendorCountry;
	private String vendorEmailId;
	private String vendorPhoneNumber;
	private String vendorFaxNumber;
	private Long departmentId;
	private String departmentName;
	private String createdBy;
	private String updatedBy;
	private String createdDate;
	private String updatedDate;
	private Boolean isExpenseVendor;
	private String vendorBankDetails;
	private String vendorGSTNumber;

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

	public String getVendorAddress() {
		return vendorAddress;
	}

	public void setVendorAddress(String vendorAddress) {
		this.vendorAddress = vendorAddress;
	}

	public String getVendorCity() {
		return vendorCity;
	}

	public void setVendorCity(String vendorCity) {
		this.vendorCity = vendorCity;
	}

	public String getVendorState() {
		return vendorState;
	}

	public void setVendorState(String vendorState) {
		this.vendorState = vendorState;
	}

	public String getVendorPincode() {
		return vendorPincode;
	}

	public void setVendorPincode(String vendorPincode) {
		this.vendorPincode = vendorPincode;
	}

	public String getVendorCountry() {
		return vendorCountry;
	}

	public void setVendorCountry(String vendorCountry) {
		this.vendorCountry = vendorCountry;
	}

	public String getVendorEmailId() {
		return vendorEmailId;
	}

	public void setVendorEmailId(String vendorEmailId) {
		this.vendorEmailId = vendorEmailId;
	}

	public String getVendorPhoneNumber() {
		return vendorPhoneNumber;
	}

	public void setVendorPhoneNumber(String vendorPhoneNumber) {
		this.vendorPhoneNumber = vendorPhoneNumber;
	}

	public String getVendorFaxNumber() {
		return vendorFaxNumber;
	}

	public void setVendorFaxNumber(String vendorFaxNumber) {
		this.vendorFaxNumber = vendorFaxNumber;
	}

	public Long getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Long departmentId) {
		this.departmentId = departmentId;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public String getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Boolean getIsExpenseVendor() {
		return isExpenseVendor;
	}

	public void setIsExpenseVendor(Boolean isExpenseVendor) {
		this.isExpenseVendor = isExpenseVendor;
	}

	public String getVendorBankDetails() {
		return vendorBankDetails;
	}

	public void setVendorBankDetails(String vendorBankDetails) {
		this.vendorBankDetails = vendorBankDetails;
	}
	
	public String getvendorGSTNumber() {
		return vendorGSTNumber;
	}

	public void setvendorGSTNumber(String vendorGSTNumber) {
		this.vendorGSTNumber = vendorGSTNumber;
	}
}
