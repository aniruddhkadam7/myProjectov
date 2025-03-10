/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.raybiztech.assetmanagement.builder;

import com.raybiztech.appraisals.business.EmpDepartment;
import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.dao.DAO;
import com.raybiztech.appraisals.security.utils.SecurityUtils;
import com.raybiztech.assetmanagement.business.Manufacturer;
import com.raybiztech.assetmanagement.business.Product;
import com.raybiztech.assetmanagement.dto.ManufacturerDto;
import com.raybiztech.date.Second;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author anil
 */
@Component("manufacturerBuilder")
public class ManufacturerBuilder {
    @Autowired
     DAO dao;
    @Autowired
    SecurityUtils securityUtils;
    
    Logger logger=Logger.getLogger(ManufacturerBuilder.class);
    
    public Manufacturer toEntity(ManufacturerDto dto){
        Long loggedInEmpId = securityUtils.getLoggedEmployeeIdforSecurityContextHolder();
            Employee employee = dao.findBy(Employee.class, loggedInEmpId);
        Manufacturer manufacturer=null;
        if(dto!=null){
            manufacturer=new Manufacturer();
            manufacturer.setManufacturerName(dto.getManufacturerName());
            Product product=dao.findBy(Product.class, dto.getProductId());
            manufacturer.setProduct(product);
             manufacturer.setEmpDepartment(dao.findBy(EmpDepartment.class, product.getEmpDepartment().getDepartmentId()));
             manufacturer.setCreatedBy(employee.getEmployeeId());
             manufacturer.setCreatedDate(new Second());
           // manufacturer.setRoleId(employee.getEmpRole());
          //  manufacturer.setDepartmentName(employee.getEmpDepartment().getName());
           // manufacturer.setEmpDepartment(employee.getEmpDepartment());
            
        }
        return manufacturer;
        
    }
    public ManufacturerDto toDTO(Manufacturer manufacturer){
        Employee createdBy = null,updatedBy = null;
          createdBy  =manufacturer.getCreatedBy()!=null?dao.findBy(Employee.class, manufacturer.getCreatedBy()):null;
              updatedBy=manufacturer.getUpdatedBy()!=null?dao.findBy(Employee.class, manufacturer.getUpdatedBy()):null;
        ManufacturerDto dto=null;
        if(manufacturer!=null){
        	dto=new ManufacturerDto();
        	dto.setManufacturerId(manufacturer.getManufacturerId());
            dto.setManufacturerName(manufacturer.getManufacturerName());
            dto.setProductId(manufacturer.getProduct().getProductId());
            dto.setProductName(manufacturer.getProduct().getProductName());
            dto.setCreatedBy(createdBy!=null?createdBy.getFullName():"");
            dto.setUpdatedBy(updatedBy!=null?updatedBy.getFullName():"");
            dto.setCreatedDate(manufacturer.getCreatedDate()!=null?manufacturer.getCreatedDate().toString("dd/MM/yyyy"):"");
            dto.setUpdatedDate(manufacturer.getUpdatedDate()!=null?manufacturer.getUpdatedDate().toString("dd/MM/yyyy"):"");
            //dto.setDepartmentId(manufacturer.getEmpDepartment().getDepartmentId());
           // dto.setDepartmentName(manufacturer.getEmpDepartment().getDepartmentName());
        }
        return dto;
        
    }
    public Manufacturer toEditEntity(ManufacturerDto dto){
         Long loggedInEmpId = securityUtils.getLoggedEmployeeIdforSecurityContextHolder();
            Employee employee = dao.findBy(Employee.class, loggedInEmpId);
        
        Manufacturer manufacturer=null;
        if(dto!=null){
            manufacturer=dao.findBy(Manufacturer.class, dto.getManufacturerId());
            manufacturer.setManufacturerName(dto.getManufacturerName());
            Product product=dao.findBy(Product.class, dto.getProductId());
            manufacturer.setProduct(product);
            manufacturer.setEmpDepartment(dao.findBy(EmpDepartment.class, product.getEmpDepartment().getDepartmentId()));
            manufacturer.setUpdatedBy(employee.getEmployeeId());
            manufacturer.setUpdatedDate(new Second());
        }
        
        return manufacturer;
        
    }
    public List<ManufacturerDto> toDTOList(List<Manufacturer> manufacturerList){
        List<ManufacturerDto> dtos=new ArrayList<ManufacturerDto>();
        if(manufacturerList!=null){
            for(Manufacturer manufacturer:manufacturerList){
                dtos.add(toDTO(manufacturer));
            }
        }
        return dtos;
    }
}
