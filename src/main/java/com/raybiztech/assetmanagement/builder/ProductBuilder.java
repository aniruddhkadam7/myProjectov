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
import com.raybiztech.assetmanagement.business.Product;
import com.raybiztech.assetmanagement.dto.ProductDto;
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
@Component("productBuilder")
public class ProductBuilder {

    @Autowired
    DAO dao;
    @Autowired
     SecurityUtils securityUtils;

    Logger logger = Logger.getLogger(ProductBuilder.class);

    public Product toEntity(ProductDto productDto) {
        Long loggedInEmpId = securityUtils.getLoggedEmployeeIdforSecurityContextHolder();
            Employee employee = dao.findBy(Employee.class, loggedInEmpId);

        Product product = null;
        if (productDto != null) {
            product = new Product();
            product.setProductName(productDto.getProductName());
            AssetType assetType=dao.findBy(AssetType.class, productDto.getAssetTypeId());
            product.setAssetType(assetType);
             product.setEmpDepartment(dao.findBy(EmpDepartment.class,assetType.getEmpDepartment().getDepartmentId()));
             product.setCreatedBy(employee.getEmployeeId());
             product.setCreatedDate(new Second());
           // product.setRoleId(employee.getEmpRole());
           // product.setDepartmentName(employee.getEmpDepartment().getName());
           // product.setEmpDepartment(employee.getEmpDepartment());
        }

        return product;

    }

    public ProductDto toDTO(Product product) {

        Employee createdBy = null, updatedBy = null;
        //  if(vendorDetails.getCreatedBy()!=null)
        createdBy = product.getCreatedBy() != null ? dao.findBy(Employee.class, product.getCreatedBy()) : null;
        //   if(vendorDetails.getUpdatedBy()!=null)
        updatedBy = product.getUpdatedBy() != null ? dao.findBy(Employee.class, product.getUpdatedBy()) : null;
        ProductDto dto = null;
        if (product != null) {
            dto = new ProductDto();
            dto.setProductId(product.getProductId());
            dto.setProductName(product.getProductName());
            dto.setAssetTypeId(product.getAssetType().getId());
            dto.setAssetType(product.getAssetType().getAssetType());
            dto.setCreatedBy(createdBy!=null?createdBy.getFullName():"");
            dto.setUpdatedBy(updatedBy!=null?updatedBy.getFullName():"");
            dto.setCreatedDate(product.getCreatedDate()!=null?product.getCreatedDate().toString("dd/MM/yyyy"):"");
            dto.setUpdatedDate(product.getUpdatedDate()!=null?product.getUpdatedDate().toString("dd/MM/yyyy"):"");
           // dto.setDepartmentName(product.getEmpDepartment().getDepartmentName());
           // dto.setDepartmentId(product.getEmpDepartment().getDepartmentId());
        }

        return dto;

    }

    public Product toEditEntity(ProductDto dto) {
         Long loggedInEmpId = securityUtils.getLoggedEmployeeIdforSecurityContextHolder();
            Employee employee = dao.findBy(Employee.class, loggedInEmpId);
        Product product = null;
        if (dto != null) {
            product = dao.findBy(Product.class, dto.getProductId());
            product.setProductName(dto.getProductName());
            AssetType assetType=dao.findBy(AssetType.class, dto.getAssetTypeId());
            product.setAssetType(assetType);
            product.setEmpDepartment(dao.findBy(EmpDepartment.class, assetType.getEmpDepartment().getDepartmentId()));
            product.setUpdatedBy(employee.getEmployeeId());
            product.setUpdatedDate(new Second());
        }
        return product;

    }
    
    public List<ProductDto> toDTOList(List<Product> productList)
    {
        List<ProductDto> productDtos=new ArrayList<ProductDto>();
        if(productList!=null){
            for(Product product:productList){
                productDtos.add(toDTO(product));
            }
        }
        return productDtos;
        
    }

}
