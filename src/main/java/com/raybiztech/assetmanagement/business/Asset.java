/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.raybiztech.assetmanagement.business;

import com.raybiztech.appraisals.business.EmpDepartment;
import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.date.Date;
import com.raybiztech.date.Second;
import com.raybiztech.projectmanagement.invoice.lookup.CountryLookUp;

import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author anil
 */
public class Asset implements Serializable {

	private static final long serialVersionUID = -29271533858315241L;

	private Long id;
	private String poNumber;
	private VendorDetails vendorDetails;
	private ProductSpecifications productSpecifications;
	private String assetNumber;
	private String otherAssetNumber;
	private String invoiceNumber;
	private Date purchasedDate;
	private Date receivedDate;
	private String notes;
	private Employee employee;
	private String status;
	private AssetType assetType;
	private Date warrantyStartDate;
	private Date warrantyEndDate;
	private String allocatedStatus;
	// private Role roleId;
	// private String departmentName;
	private EmpDepartment empDepartment;
	private String location;
	private Long createdBy;
	private Long updatedBy;
	private Second createdDate;
	private Second updatedDate;
	private String referenceNumber;
	private String amount;
	private CountryLookUp country;
	
	
	
	public CountryLookUp getCountry() {
		return country;
	}

	public void setCountry(CountryLookUp country) {
		this.country = country;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPoNumber() {
		return poNumber;
	}

	public void setPoNumber(String poNumber) {
		this.poNumber = poNumber;
	}

	public ProductSpecifications getProductSpecifications() {
		return productSpecifications;
	}

	public void setProductSpecifications(
			ProductSpecifications productSpecifications) {
		this.productSpecifications = productSpecifications;
	}

	public String getAssetNumber() {
		return assetNumber;
	}

	public void setAssetNumber(String assetNumber) {
		this.assetNumber = assetNumber;
	}

	public String getOtherAssetNumber() {
		return otherAssetNumber;
	}

	public void setOtherAssetNumber(String otherAssetNumber) {
		this.otherAssetNumber = otherAssetNumber;
	}

	public String getInvoiceNumber() {
		return invoiceNumber;
	}

	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}

	public Date getPurchasedDate() {
		return purchasedDate;
	}

	public void setPurchasedDate(Date purchasedDate) {
		this.purchasedDate = purchasedDate;
	}

	public Date getReceivedDate() {
		return receivedDate;
	}

	public void setReceivedDate(Date receivedDate) {
		this.receivedDate = receivedDate;
	}

	public VendorDetails getVendorDetails() {
		return vendorDetails;
	}

	public void setVendorDetails(VendorDetails vendorDetails) {
		this.vendorDetails = vendorDetails;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public AssetType getAssetType() {
		return assetType;
	}

	public void setAssetType(AssetType assetType) {
		this.assetType = assetType;
	}

	public Date getWarrantyStartDate() {
		return warrantyStartDate;
	}

	public void setWarrantyStartDate(Date warrantyStartDate) {
		this.warrantyStartDate = warrantyStartDate;
	}

	public Date getWarrantyEndDate() {
		return warrantyEndDate;
	}

	public void setWarrantyEndDate(Date warrantyEndDate) {
		this.warrantyEndDate = warrantyEndDate;
	}

	public String getAllocatedStatus() {
		return allocatedStatus;
	}

	public void setAllocatedStatus(String allocatedStatus) {
		this.allocatedStatus = allocatedStatus;
	}

	public EmpDepartment getEmpDepartment() {
		return empDepartment;
	}

	public void setEmpDepartment(EmpDepartment empDepartment) {
		this.empDepartment = empDepartment;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Long getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}

	public Long getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Long updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Second getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Second createdDate) {
		this.createdDate = createdDate;
	}

	public Second getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Second updatedDate) {
		this.updatedDate = updatedDate;
	}

	public String getReferenceNumber() {
		return referenceNumber;
	}

	public void setReferenceNumber(String referenceNumber) {
		this.referenceNumber = referenceNumber;
	}

	
	
	@Override
	public int hashCode() {
		int hash = 7;
		hash = 67 * hash + Objects.hashCode(this.id);
		hash = 67 * hash + Objects.hashCode(this.poNumber);
		hash = 67 * hash + Objects.hashCode(this.vendorDetails);
		hash = 67 * hash + Objects.hashCode(this.productSpecifications);
		hash = 67 * hash + Objects.hashCode(this.assetNumber);
		hash = 67 * hash + Objects.hashCode(this.otherAssetNumber);
		hash = 67 * hash + Objects.hashCode(this.invoiceNumber);
		hash = 67 * hash + Objects.hashCode(this.purchasedDate);
		hash = 67 * hash + Objects.hashCode(this.receivedDate);
		hash = 67 * hash + Objects.hashCode(this.notes);
		hash = 67 * hash + Objects.hashCode(this.employee);
		hash = 67 * hash + Objects.hashCode(this.status);
		hash = 67 * hash + Objects.hashCode(this.assetType);
		hash = 67 * hash + Objects.hashCode(this.warrantyStartDate);
		hash = 67 * hash + Objects.hashCode(this.warrantyEndDate);
		hash = 67 * hash + Objects.hashCode(this.allocatedStatus);
		hash = 67 * hash + Objects.hashCode(this.empDepartment);
		hash = 67 * hash + Objects.hashCode(this.createdBy);
		hash = 67 * hash + Objects.hashCode(this.updatedBy);
		hash = 67 * hash + Objects.hashCode(this.createdDate);
		hash = 67 * hash + Objects.hashCode(this.updatedDate);
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Asset other = (Asset) obj;
		if (!Objects.equals(this.id, other.id)) {
			return false;
		}
		if (!Objects.equals(this.poNumber, other.poNumber)) {
			return false;
		}
		if (!Objects.equals(this.vendorDetails, other.vendorDetails)) {
			return false;
		}
		if (!Objects.equals(this.productSpecifications,
				other.productSpecifications)) {
			return false;
		}
		if (!Objects.equals(this.assetNumber, other.assetNumber)) {
			return false;
		}
		if (!Objects.equals(this.otherAssetNumber, other.otherAssetNumber)) {
			return false;
		}
		if (!Objects.equals(this.invoiceNumber, other.invoiceNumber)) {
			return false;
		}
		if (!Objects.equals(this.purchasedDate, other.purchasedDate)) {
			return false;
		}
		if (!Objects.equals(this.receivedDate, other.receivedDate)) {
			return false;
		}
		if (!Objects.equals(this.notes, other.notes)) {
			return false;
		}
		if (!Objects.equals(this.employee, other.employee)) {
			return false;
		}
		if (!Objects.equals(this.status, other.status)) {
			return false;
		}
		if (!Objects.equals(this.assetType, other.assetType)) {
			return false;
		}
		if (!Objects.equals(this.warrantyStartDate, other.warrantyStartDate)) {
			return false;
		}
		if (!Objects.equals(this.warrantyEndDate, other.warrantyEndDate)) {
			return false;
		}
		if (!Objects.equals(this.allocatedStatus, other.allocatedStatus)) {
			return false;
		}
		if (!Objects.equals(this.empDepartment, other.empDepartment)) {
			return false;
		}
		if (!Objects.equals(this.createdBy, other.createdBy)) {
			return false;
		}
		if (!Objects.equals(this.updatedBy, other.updatedBy)) {
			return false;
		}
		if (!Objects.equals(this.createdDate, other.createdDate)) {
			return false;
		}
		if (!Objects.equals(this.updatedDate, other.updatedDate)) {
			return false;
		}
		return true;
	}


}
