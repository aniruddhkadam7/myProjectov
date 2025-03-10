/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.raybiztech.assetmanagement.dto;

import java.io.Serializable;

import com.raybiztech.appraisals.dto.SearchEmpDetailsDTO;

/**
 *
 * @author anil
 */
public class AssetAuditDto implements Serializable {

	private static final long serialVersionUID = 81782295347743767L;

	private Long id;
	private String assetNumber;
	private String productName;
	private String pSpecification;
	private String employeeName;
	private Long employeeId;
	private String assignedDate;
	private String description;
	private String status;
	private String date;
	private String updatedBy;
	private String location;
	private String referenceNumber;
	private String vendorName;
	private Long vendorId;
	private Long assetId;
	private Long productId;
	private String multipleSearch;
	private Boolean searchByEmpName;
    private Boolean locationForEmpAssets;
    private String invoiceNumber;
    private String amount;
    
    
        

    public String getInvoiceNumber() {
		return invoiceNumber;
	}

	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public Boolean isLocationForEmpAssets() {
        return locationForEmpAssets;
    }

    public void setLocationForEmpAssets(Boolean locationForEmpAssets) {
        this.locationForEmpAssets = locationForEmpAssets;
    }
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAssetNumber() {
		return assetNumber;
	}

	public void setAssetNumber(String assetNumber) {
		this.assetNumber = assetNumber;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getpSpecification() {
		return pSpecification;
	}

	public void setpSpecification(String pSpecification) {
		this.pSpecification = pSpecification;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public String getAssignedDate() {
		return assignedDate;
	}

	public void setAssignedDate(String assignedDate) {
		this.assignedDate = assignedDate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getReferenceNumber() {
		return referenceNumber;
	}

	public void setReferenceNumber(String referenceNumber) {
		this.referenceNumber = referenceNumber;
	}

	public String getVendorName() {
		return vendorName;
	}

	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}

	public Long getVendorId() {
		return vendorId;
	}

	public void setVendorId(Long vendorId) {
		this.vendorId = vendorId;
	}

	public Long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}

	public Long getAssetId() {
		return assetId;
	}

	public void setAssetId(Long assetId) {
		this.assetId = assetId;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public String getMultipleSearch() {
		return multipleSearch;
	}

	public void setMultipleSearch(String multipleSearch) {
		this.multipleSearch = multipleSearch;
	}

	public Boolean isSearchByEmpName() {
		return searchByEmpName;
	}

	public void setSearchByEmpName(Boolean searchByEmpName) {
		this.searchByEmpName = searchByEmpName;
	}

}
