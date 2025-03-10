/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.raybiztech.assetmanagement.business;

import com.raybiztech.appraisals.business.EmpDepartment;
import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.dto.SearchEmpDetailsDTO;
import com.raybiztech.date.Date;
import com.raybiztech.date.Second;

import java.io.Serializable;

/**
 *
 * @author anil
 */
public class AssetAudit implements Serializable {
    
    private static final long serialVersionUID = 862714628055865079L;
    
    private Long id;
    private Asset asset;
    private Employee employee;
    private String description;
   // private Second assinedDate;
    private String status;
    private Date date;
    private Employee updatedBy;
    private String location;
    private String referenceNumber;
    private String vendorName;
    private Second updatedDate;
    private EmpDepartment empDepartment;
    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Asset getAsset() {
        return asset;
    }

    public void setAsset(Asset asset) {
        this.asset = asset;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

//    public Second getAssinedDate() {
//        return assinedDate;
//    }
//
//    public void setAssinedDate(Second assinedDate) {
//        this.assinedDate = assinedDate;
//    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

	public Employee getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Employee updatedBy) {
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

    public Second getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Second updatedDate) {
        this.updatedDate = updatedDate;
    }

    public EmpDepartment getEmpDepartment() {
        return empDepartment;
    }

    public void setEmpDepartment(EmpDepartment empDepartment) {
        this.empDepartment = empDepartment;
    }

	
	

    
    
}
