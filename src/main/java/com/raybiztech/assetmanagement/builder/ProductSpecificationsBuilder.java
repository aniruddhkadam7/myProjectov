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
import com.raybiztech.assetmanagement.business.AssetType;
import com.raybiztech.assetmanagement.business.Manufacturer;
import com.raybiztech.assetmanagement.business.Product;
import com.raybiztech.assetmanagement.business.ProductSpecifications;
import com.raybiztech.assetmanagement.dto.ProductSpecificationDto;
import com.raybiztech.date.Second;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author anil
 */
@Component("productSpecificationsBuilder")
public class ProductSpecificationsBuilder {
    
    @Autowired
    DAO dao;
    @Autowired
    SecurityUtils securityUtils;
    
    public ProductSpecifications convertDTOToEntity(ProductSpecificationDto specificationDto)
    {
        Long loggedInEmpId = securityUtils.getLoggedEmployeeIdforSecurityContextHolder();
            Employee employee = dao.findBy(Employee.class, loggedInEmpId);
        ProductSpecifications specifications=null;
        if(specificationDto!=null){
            specifications=new ProductSpecifications();
            Product p=dao.findBy(Product.class, specificationDto.getProductId());
            Manufacturer m=dao.findBy(Manufacturer.class, specificationDto.getManufacturerId());
            AssetType assetType=dao.findBy(AssetType.class, specificationDto.getAssetTypeId());
            specifications.setAssetType(assetType);
            specifications.setProduct(p);
            specifications.setManufacturer(m);
            specifications.setProductSpecification(specificationDto.getProductSpecification());
            //AssetType assetType1=dao.findBy(AssetType.class, specificationDto.getAssetTypeId());
            specifications.setEmpDepartment(dao.findBy(EmpDepartment.class, assetType.getEmpDepartment().getDepartmentId()));
            specifications.setCreatedBy(employee.getEmployeeId());
            specifications.setCreatedDate(new Second());
           
        }
        
        return specifications;
        
    }
    public List<ProductSpecificationDto> EntityToDTO(List<ProductSpecifications> specificationses){
         Employee createdBy = null,updatedBy = null;
         
        
        List<ProductSpecificationDto> dtoList=null;
        if(specificationses!=null){
            dtoList=new ArrayList<ProductSpecificationDto>();
            for(ProductSpecifications ps:specificationses){
              createdBy  =ps.getCreatedBy()!=null?dao.findBy(Employee.class, ps.getCreatedBy()):null;
              updatedBy=ps.getUpdatedBy()!=null?dao.findBy(Employee.class, ps.getUpdatedBy()):null;
                ProductSpecificationDto dto1=new ProductSpecificationDto();
                dto1.setId(ps.getSpecificationId());
                dto1.setProductId(ps.getProduct().getProductId());
                dto1.setManufacturerId(ps.getManufacturer().getManufacturerId());
                dto1.setProductSpecification(ps.getProductSpecification().replace("&#10;", ""));
                dto1.setManufacturerName(ps.getManufacturer().getManufacturerName());
                dto1.setProductName(ps.getProduct().getProductName());
                dto1.setAssetTypeId(ps.getAssetType().getId());
                dto1.setAssetType(ps.getAssetType().getAssetType());
                dto1.setCreatedBy(createdBy!=null?createdBy.getFullName():"");
                dto1.setUpdatedBy(updatedBy!=null?updatedBy.getFullName():"");
                dto1.setCreatedDate(ps.getCreatedDate()!=null?ps.getCreatedDate().toString("dd/MM/yyyy"):"");
                dto1.setUpdatedDate(ps.getUpdatedDate()!=null?ps.getUpdatedDate().toString("dd/MM/yyyy"):"");
                //dto1.setDepartmentId(ps.getEmpDepartment().getDepartmentId());
                //dto1.setDepartmentName(ps.getEmpDepartment().getDepartmentName());
                dtoList.add(dto1);
            }
        }
        
        
        return dtoList;
        
    }
    
    public ProductSpecifications toEditEntity(ProductSpecificationDto dto){
        Long loggedInEmpId = securityUtils.getLoggedEmployeeIdforSecurityContextHolder();
            Employee employee = dao.findBy(Employee.class, loggedInEmpId);
        ProductSpecifications specifications=null;
        if(dto!=null){
            specifications=dao.findBy(ProductSpecifications.class, dto.getId());
            Product p=dao.findBy(Product.class, dto.getProductId());
            Manufacturer m=dao.findBy(Manufacturer.class, dto.getManufacturerId());
            AssetType assetType=dao.findBy(AssetType.class, dto.getAssetTypeId());
            specifications.setAssetType(assetType);
            specifications.setProduct(p);
            specifications.setManufacturer(m);
            specifications.setProductSpecification(dto.getProductSpecification());
            specifications.setEmpDepartment(dao.findBy(EmpDepartment.class, assetType.getEmpDepartment().getDepartmentId()));
            specifications.setUpdatedBy(employee.getEmployeeId());
            specifications.setUpdatedDate(new Second());
        }
        
        return specifications;
    }
    

}
