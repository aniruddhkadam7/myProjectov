/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.raybiztech.assetmanagement.business;

import com.raybiztech.appraisals.business.EmpDepartment;
import java.io.Serializable;

/**
 *
 * @author anil
 */
public class AssetType implements Serializable{
    
    private static final long serialVersionUID = -292733858315241L;
    
    private Long id;
    private String assetType;
   // private Role roleId;
   // private String departmentName;
    private EmpDepartment empDepartment;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAssetType() {
        return assetType;
    }

    public void setAssetType(String assetType) {
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
    


    
}
