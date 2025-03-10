package com.raybiztech.assetmanagement.business;

import com.raybiztech.appraisals.business.EmpDepartment;
import com.raybiztech.date.Second;
import java.io.Serializable;
/**
*
* @author Aprajita
*/
public class Manufacturer implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3908197907995291965L;
	
	private Long manufacturerId;
	private String manufacturerName;
        private Product product;
//        private Role roleId;
//        private String departmentName;
        private EmpDepartment empDepartment;
        private Long createdBy;
        private Long updatedBy;
        private Second createdDate;
        private Second updatedDate;
	
	
	public Long getManufacturerId() {
		return manufacturerId;
	}
	public void setManufacturerId(Long manufacturerId) {
		this.manufacturerId = manufacturerId;
	}
	public String getManufacturerName() {
		return manufacturerName;
	}
	public void setManufacturerName(String manufacturerName) {
		this.manufacturerName = manufacturerName;
	}

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

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
    
    
    
    
        
	@Override
	public String toString() {
		return "Manufacturer [manufacturerId=" + manufacturerId
				+ ", manufacturerName=" + manufacturerName + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((manufacturerId == null) ? 0 : manufacturerId.hashCode());
		result = prime
				* result
				+ ((manufacturerName == null) ? 0 : manufacturerName.hashCode());
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
		Manufacturer other = (Manufacturer) obj;
		if (manufacturerId == null) {
			if (other.manufacturerId != null)
				return false;
		} else if (!manufacturerId.equals(other.manufacturerId))
			return false;
		if (manufacturerName == null) {
			if (other.manufacturerName != null)
				return false;
		} else if (!manufacturerName.equals(other.manufacturerName))
			return false;
		return true;
	}
	
	
	
	

}
