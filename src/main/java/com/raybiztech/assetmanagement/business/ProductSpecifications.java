package com.raybiztech.assetmanagement.business;

import com.raybiztech.appraisals.business.EmpDepartment;
import com.raybiztech.date.Second;
import java.io.Serializable;
/**
*
* @author anil
*/
public class ProductSpecifications implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5467760424401918239L;
	
	private Long specificationId;
        private AssetType assetType;
	private Product product;
	private Manufacturer manufacturer;
	private String productSpecification;
       // private Role roleId;    
       // private String departmentName;
        private EmpDepartment empDepartment;
         private Long createdBy;
        private Long updatedBy;
        private Second createdDate;
        private Second updatedDate;
	
	
	public Long getSpecificationId() {
		return specificationId;
	}
	public void setSpecificationId(Long specificationId) {
		this.specificationId = specificationId;
	}
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	public Manufacturer getManufacturer() {
		return manufacturer;
	}
	public void setManufacturer(Manufacturer manufacturer) {
		this.manufacturer = manufacturer;
	}
	public String getProductSpecification() {
		return productSpecification;
	}
	public void setProductSpecification(String productSpecification) {
		this.productSpecification = productSpecification;
	}

    public AssetType getAssetType() {
        return assetType;
    }

    public void setAssetType(AssetType assetType) {
        this.assetType = assetType;
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
		return "ProductSpecifications [specificationId=" + specificationId
				+ ", product=" + product + ", manufacturer=" + manufacturer
				+ ", productSpecification=" + productSpecification + "]";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((manufacturer == null) ? 0 : manufacturer.hashCode());
		result = prime * result + ((product == null) ? 0 : product.hashCode());
		result = prime
				* result
				+ ((productSpecification == null) ? 0 : productSpecification
						.hashCode());
		result = prime * result
				+ ((specificationId == null) ? 0 : specificationId.hashCode());
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
		ProductSpecifications other = (ProductSpecifications) obj;
		if (manufacturer == null) {
			if (other.manufacturer != null)
				return false;
		} else if (!manufacturer.equals(other.manufacturer))
			return false;
		if (product == null) {
			if (other.product != null)
				return false;
		} else if (!product.equals(other.product))
			return false;
		if (productSpecification == null) {
			if (other.productSpecification != null)
				return false;
		} else if (!productSpecification.equals(other.productSpecification))
			return false;
		if (specificationId == null) {
			if (other.specificationId != null)
				return false;
		} else if (!specificationId.equals(other.specificationId))
			return false;
		return true;
	}
	
	
	
	

}