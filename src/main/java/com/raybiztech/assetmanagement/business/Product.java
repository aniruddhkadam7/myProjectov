package com.raybiztech.assetmanagement.business;

import com.raybiztech.appraisals.business.EmpDepartment;
import com.raybiztech.date.Second;
import java.io.Serializable;
/**
*
* @author Aprajita
*/
public class Product implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2952271533858315241L;
	
	private Long productId;
	private String productName;
        private AssetType assetType;
       // private Role roleId;
       // private String departmentName;
        private EmpDepartment empDepartment;
        private Long createdBy;
        private Long updatedBy;
        private Second createdDate;
        private Second updatedDate;
	
	public Long getProductId() {
		return productId;
	}
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}

    public AssetType getAssetType() {
        return assetType;
    }

    public void setAssetType(AssetType assetType) {
        this.assetType = assetType;
    }

//    public Role getRoleId() {
//        return roleId;
//    }
//
//    public void setRoleId(Role roleId) {
//        this.roleId = roleId;
//    }
//    public String getDepartmentName() {
//        return departmentName;
//    }
//
//    public void setDepartmentName(String departmentName) {
//        this.departmentName = departmentName;
//    }
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
		return "Product [productId=" + productId + ", productName="
				+ productName + "]";
	}
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((productId == null) ? 0 : productId.hashCode());
		result = prime * result
				+ ((productName == null) ? 0 : productName.hashCode());
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
		Product other = (Product) obj;
		if (productId == null) {
			if (other.productId != null)
				return false;
		} else if (!productId.equals(other.productId))
			return false;
		if (productName == null) {
			if (other.productName != null)
				return false;
		} else if (!productName.equals(other.productName))
			return false;
		return true;
	}
	
	
	

}
