/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.raybiztech.appraisals.business;

import java.io.Serializable;

/**
 *
 * @author anil
 */
public class EmpDepartment implements Serializable {

	private static final long serialVersionUID = -88877456338601638L;
        private Long departmentId;
        private String departmentName;
        private Boolean supportManagementFlag;
        private Boolean allocationSupportFlag;

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

	public Boolean getSupportManagementFlag() {
		return supportManagementFlag;
	}

	public void setSupportManagementFlag(Boolean supportManagementFlag) {
		this.supportManagementFlag = supportManagementFlag;
	}

	public Boolean getAllocationSupportFlag() {
			return allocationSupportFlag;
		}

	public void setAllocationSupportFlag(Boolean allocationSupportFlag) {
			this.allocationSupportFlag = allocationSupportFlag;
		}    
        
}
