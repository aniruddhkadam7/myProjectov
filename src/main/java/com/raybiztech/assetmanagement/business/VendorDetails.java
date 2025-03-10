package com.raybiztech.assetmanagement.business;

import com.raybiztech.appraisals.business.EmpDepartment;
import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.date.Date;
import com.raybiztech.date.Second;
import java.io.Serializable;

/**
 *
 * @author Aprajita
 */
// @Audited
public class VendorDetails implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -871367402175183800L;

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
	private EmpDepartment empDepartment;
	private Long createdBy;
	private Long updatedBy;
	private Second createdDate;
	private Second updatedDate;
	private Boolean isExpenseVendor;
	private String vendorBankDetails;
	private String vendorGSTNumber;
	// private String departmentName;
	// private Role roleId;

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

	public String getVendorPincode() {
		return vendorPincode;
	}

	public void setVendorPincode(String vendorPincode) {
		this.vendorPincode = vendorPincode;
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

	// public Role getRoleId() {
	// return roleId;
	// }
	//
	// public void setRoleId(Role roleId) {
	// this.roleId = roleId;
	// }
	// public String getDepartmentName() {
	// return departmentName;
	// }
	//
	// public void setDepartmentName(String departmentName) {
	// this.departmentName = departmentName;
	// }
	public EmpDepartment getEmpDepartment() {
		return empDepartment;
	}

	public void setEmpDepartment(EmpDepartment empDepartment) {
		this.empDepartment = empDepartment;
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

	@Override
	public String toString() {
		return "VendorDetails [vendorId=" + vendorId + ", vendorName=" + vendorName + ", vendorAddress=" + vendorAddress
				+ ", vendorCity=" + vendorCity + ", vendorState=" + vendorState + ", vendorPincode=" + vendorPincode
				+ ", vendorCountry=" + vendorCountry + ", vendorEmailId=" + vendorEmailId + ", vendorPhoneNumber="
				+ vendorPhoneNumber + ", vendorFaxNumber=" + vendorFaxNumber + ", empDepartment=" + empDepartment
				+ ", createdBy=" + createdBy + ", updatedBy=" + updatedBy + ", createdDate=" + createdDate
				+ ", updatedDate=" + updatedDate + ", isExpenseVendor=" + isExpenseVendor + ", vendorBankDetails="
				+ vendorBankDetails + ", vendorGSTNo=" + vendorGSTNumber + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((createdBy == null) ? 0 : createdBy.hashCode());
		result = prime * result + ((createdDate == null) ? 0 : createdDate.hashCode());
		result = prime * result + ((empDepartment == null) ? 0 : empDepartment.hashCode());
		result = prime * result + ((isExpenseVendor == null) ? 0 : isExpenseVendor.hashCode());
		result = prime * result + ((updatedBy == null) ? 0 : updatedBy.hashCode());
		result = prime * result + ((updatedDate == null) ? 0 : updatedDate.hashCode());
		result = prime * result + ((vendorAddress == null) ? 0 : vendorAddress.hashCode());
		result = prime * result + ((vendorBankDetails == null) ? 0 : vendorBankDetails.hashCode());
		result = prime * result + ((vendorCity == null) ? 0 : vendorCity.hashCode());
		result = prime * result + ((vendorCountry == null) ? 0 : vendorCountry.hashCode());
		result = prime * result + ((vendorEmailId == null) ? 0 : vendorEmailId.hashCode());
		result = prime * result + ((vendorFaxNumber == null) ? 0 : vendorFaxNumber.hashCode());
		result = prime * result + ((vendorGSTNumber == null) ? 0 : vendorGSTNumber.hashCode());
		result = prime * result + ((vendorId == null) ? 0 : vendorId.hashCode());
		result = prime * result + ((vendorName == null) ? 0 : vendorName.hashCode());
		result = prime * result + ((vendorPhoneNumber == null) ? 0 : vendorPhoneNumber.hashCode());
		result = prime * result + ((vendorPincode == null) ? 0 : vendorPincode.hashCode());
		result = prime * result + ((vendorState == null) ? 0 : vendorState.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		VendorDetails other = (VendorDetails) obj;
		if (createdBy == null) {
			if (other.createdBy != null)
				return false;
		} else if (!createdBy.equals(other.createdBy))
			return false;
		if (createdDate == null) {
			if (other.createdDate != null)
				return false;
		} else if (!createdDate.equals(other.createdDate))
			return false;
		if (empDepartment == null) {
			if (other.empDepartment != null)
				return false;
		} else if (!empDepartment.equals(other.empDepartment))
			return false;
		if (isExpenseVendor == null) {
			if (other.isExpenseVendor != null)
				return false;
		} else if (!isExpenseVendor.equals(other.isExpenseVendor))
			return false;
		if (updatedBy == null) {
			if (other.updatedBy != null)
				return false;
		} else if (!updatedBy.equals(other.updatedBy))
			return false;
		if (updatedDate == null) {
			if (other.updatedDate != null)
				return false;
		} else if (!updatedDate.equals(other.updatedDate))
			return false;
		if (vendorAddress == null) {
			if (other.vendorAddress != null)
				return false;
		} else if (!vendorAddress.equals(other.vendorAddress))
			return false;
		if (vendorBankDetails == null) {
			if (other.vendorBankDetails != null)
				return false;
		} else if (!vendorBankDetails.equals(other.vendorBankDetails))
			return false;
		if (vendorCity == null) {
			if (other.vendorCity != null)
				return false;
		} else if (!vendorCity.equals(other.vendorCity))
			return false;
		if (vendorCountry == null) {
			if (other.vendorCountry != null)
				return false;
		} else if (!vendorCountry.equals(other.vendorCountry))
			return false;
		if (vendorEmailId == null) {
			if (other.vendorEmailId != null)
				return false;
		} else if (!vendorEmailId.equals(other.vendorEmailId))
			return false;
		if (vendorFaxNumber == null) {
			if (other.vendorFaxNumber != null)
				return false;
		} else if (!vendorFaxNumber.equals(other.vendorFaxNumber))
			return false;
		if (vendorGSTNumber == null) {
			if (other.vendorGSTNumber != null)
				return false;
		} else if (!vendorGSTNumber.equals(other.vendorGSTNumber))
			return false;
		if (vendorId == null) {
			if (other.vendorId != null)
				return false;
		} else if (!vendorId.equals(other.vendorId))
			return false;
		if (vendorName == null) {
			if (other.vendorName != null)
				return false;
		} else if (!vendorName.equals(other.vendorName))
			return false;
		if (vendorPhoneNumber == null) {
			if (other.vendorPhoneNumber != null)
				return false;
		} else if (!vendorPhoneNumber.equals(other.vendorPhoneNumber))
			return false;
		if (vendorPincode == null) {
			if (other.vendorPincode != null)
				return false;
		} else if (!vendorPincode.equals(other.vendorPincode))
			return false;
		if (vendorState == null) {
			if (other.vendorState != null)
				return false;
		} else if (!vendorState.equals(other.vendorState))
			return false;
		return true;
	}

	

	
}
