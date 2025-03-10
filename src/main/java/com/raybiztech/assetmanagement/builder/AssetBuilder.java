/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.raybiztech.assetmanagement.builder;

import com.raybiztech.appraisals.business.EmpDepartment;
import com.raybiztech.appraisals.business.Employee;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.raybiztech.appraisals.dao.DAO;
import com.raybiztech.appraisals.security.utils.SecurityUtils;
import com.raybiztech.assetmanagement.business.Asset;
import com.raybiztech.assetmanagement.business.AssetType;
import com.raybiztech.assetmanagement.business.ProductSpecifications;
import com.raybiztech.assetmanagement.business.VendorDetails;
import com.raybiztech.assetmanagement.dao.AssetManagementDAO;
import com.raybiztech.assetmanagement.dto.AssetDto;
import com.raybiztech.date.Second;
import com.raybiztech.projectmanagement.invoice.lookup.CountryLookUp;
import com.raybiztech.recruitment.utils.DateParser;

/**
 *
 * @author anil
 */
@Component("assetBuilder")
public class AssetBuilder {
    @Autowired
    DAO dao;
    @Autowired
    AssetManagementDAO assetManagementDAOImpl;
    @Autowired
    SecurityUtils securityUtils;
    public Asset convertDTOToEntity(AssetDto assetDto){
        Long loggedInEmpId = securityUtils.getLoggedEmployeeIdforSecurityContextHolder();
            Employee employee = dao.findBy(Employee.class, loggedInEmpId);
        Asset asset=null;
        if(assetDto!=null){
            asset=new Asset();
            asset.setPoNumber(assetDto.getPoNumber());
            asset.setInvoiceNumber(assetDto.getInvoiceNumber());
            asset.setAssetNumber(assetDto.getAssetNumber());
            asset.setOtherAssetNumber(assetDto.getOtherAssetNumber());
            asset.setNotes(assetDto.getNotes());
            asset.setStatus(assetDto.getStatus());
            asset.setLocation(assetDto.getLocation());
            ProductSpecifications ps2= assetManagementDAOImpl.specificationId(assetDto.getProductId(),assetDto.getManufacturerId(),assetDto.getpSpecification());
            ProductSpecifications ps=dao.findBy(ProductSpecifications.class, ps2.getSpecificationId());
            asset.setProductSpecifications(ps);
            VendorDetails details=dao.findBy(VendorDetails.class, assetDto.getVendorId());
            asset.setVendorDetails(details);
            AssetType assetType=dao.findBy(AssetType.class, assetDto.getAssetTypeId());
            asset.setAssetType(assetType);
            asset.setAllocatedStatus("Unassigned");
            try {
                asset.setPurchasedDate(DateParser.toDate(assetDto.getPurchasedDate()));
                asset.setReceivedDate(DateParser.toDate(assetDto.getReceivedDate()));
                asset.setWarrantyStartDate(DateParser.toDate(assetDto.getWarrantyStartDate()));
                asset.setWarrantyEndDate(DateParser.toDate(assetDto.getWarrantyEndDate()));
            } catch (ParseException ex) {
                Logger.getLogger(AssetBuilder.class.getName()).log(Level.SEVERE, null, ex);
            }
            asset.setEmpDepartment(dao.findBy(EmpDepartment.class, assetType.getEmpDepartment().getDepartmentId()));
            asset.setCreatedBy(employee.getEmployeeId());
            asset.setCreatedDate(new Second());
            asset.setReferenceNumber(assetDto.getReferenceNumber());
            asset.setAmount(assetDto.getAmount()!=null?assetDto.getAmount():null);
            if(assetDto.getCountryId()!=null){
    			CountryLookUp con = dao.findBy(CountryLookUp.class, assetDto.getCountryId());
    			asset.setCountry(con);
    			}
            
        }
        return asset;
        
    }
    public AssetDto ToDTO(Asset asset){
        Employee createdBy = null,updatedBy = null;
          createdBy  =asset.getCreatedBy()!=null?dao.findBy(Employee.class, asset.getCreatedBy()):null;
              updatedBy=asset.getUpdatedBy()!=null?dao.findBy(Employee.class, asset.getUpdatedBy()):null;
        AssetDto assetDto=null;
        if(asset!=null){
            assetDto=new AssetDto();
            assetDto.setId(asset.getId());
            assetDto.setPoNumber(asset.getPoNumber());
            assetDto.setAssetNumber(asset.getAssetNumber());
            assetDto.setOtherAssetNumber(asset.getOtherAssetNumber());
            assetDto.setInvoiceNumber(asset.getInvoiceNumber());
            assetDto.setProductSpecificationId(asset.getProductSpecifications().getSpecificationId());
            assetDto.setVendorId(asset.getVendorDetails().getVendorId());
            assetDto.setVendorName(asset.getVendorDetails().getVendorName());
            assetDto.setNotes(asset.getNotes());
            assetDto.setLocation(asset.getLocation());
            assetDto.setStatus(asset.getStatus());
            assetDto.setpSpecification(asset.getProductSpecifications().getProductSpecification().replace("&#10;", ""));
            assetDto.setManufacturerName(asset.getProductSpecifications().getManufacturer().getManufacturerName());
            assetDto.setProductName(asset.getProductSpecifications().getProduct().getProductName());
            assetDto.setProductId(asset.getProductSpecifications().getProduct().getProductId());
            assetDto.setManufacturerId(asset.getProductSpecifications().getManufacturer().getManufacturerId());
            if(asset.getEmployee()!=null){
            assetDto.setEmployeeName(asset.getEmployee().getFullName());
            assetDto.setEmployeeId(asset.getEmployee().getEmployeeId());
            }
            assetDto.setPurchasedDate(asset.getPurchasedDate().toString("dd/MM/yyyy"));
            assetDto.setReceivedDate(asset.getReceivedDate().toString("dd/MM/yyyy"));
            if(asset.getWarrantyStartDate()!=null)
            assetDto.setWarrantyStartDate(asset.getWarrantyStartDate().toString("dd/MM/yyyy"));
            if(asset.getWarrantyEndDate()!=null)
            assetDto.setWarrantyEndDate(asset.getWarrantyEndDate().toString("dd/MM/yyyy"));
            assetDto.setAssetTypeId(asset.getAssetType().getId());
            assetDto.setAssetType(asset.getAssetType().getAssetType());
            assetDto.setCreatedBy(createdBy!=null?createdBy.getFullName():"");
            assetDto.setUpdatedBy(updatedBy!=null?updatedBy.getFullName():"");
            assetDto.setCreatedDate(asset.getCreatedDate()!=null?asset.getCreatedDate().toString("dd/MM/yyyy"):"");
            assetDto.setUpdatedDate(asset.getUpdatedDate()!=null?asset.getUpdatedDate().toString("dd/MM/yyyy"):"");
            assetDto.setReferenceNumber(asset.getReferenceNumber());
            assetDto.setAmount(asset.getAmount()!=null?asset.getAmount():null);
            if(asset.getCountry()!=null){
            assetDto.setCountryId(asset.getCountry()!=null?asset.getCountry().getId():null);
            }
           
        }
        return assetDto;
        
    }
    public List<AssetDto> ToDTOList(List<Asset> assetList)
    {
        List<AssetDto> assetDtosList=null;
        if(assetList!=null){
            assetDtosList=new ArrayList<AssetDto>();
            for(Asset asset:assetList){
                assetDtosList.add(ToDTO(asset));
            }
        }
        
        return assetDtosList;
        
    }
    
    public Asset toEditEntity(AssetDto dto){
        Long loggedInEmpId = securityUtils.getLoggedEmployeeIdforSecurityContextHolder();
            Employee employee = dao.findBy(Employee.class, loggedInEmpId);
        Asset details=null;
        if(dto!=null){
        	details= dao.findBy(Asset.class, dto.getId());
            details.setPoNumber(dto.getPoNumber());
            details.setInvoiceNumber(dto.getInvoiceNumber());
            details.setAssetNumber(dto.getAssetNumber());
            details.setOtherAssetNumber(dto.getOtherAssetNumber());
            details.setNotes(dto.getNotes());
            details.setStatus(dto.getStatus());
            details.setLocation(dto.getLocation());
            ProductSpecifications ps2= assetManagementDAOImpl.specificationId(dto.getProductId(),dto.getManufacturerId(),dto.getpSpecification());
            ProductSpecifications ps=dao.findBy(ProductSpecifications.class, ps2.getSpecificationId());
            details.setProductSpecifications(ps);
            VendorDetails vdetails=dao.findBy(VendorDetails.class, dto.getVendorId());
            details.setVendorDetails(vdetails);
            AssetType assetType=dao.findBy(AssetType.class, dto.getAssetTypeId());
            details.setAssetType(assetType);
            try {
            	details.setPurchasedDate(DateParser.toDate(dto.getPurchasedDate()));
            	details.setReceivedDate(DateParser.toDate(dto.getReceivedDate()));
                details.setWarrantyStartDate(DateParser.toDate(dto.getWarrantyStartDate()));
                details.setWarrantyEndDate(DateParser.toDate(dto.getWarrantyEndDate()));
            } catch (ParseException ex) {
                Logger.getLogger(AssetBuilder.class.getName()).log(Level.SEVERE, null, ex);
            }
            details.setEmpDepartment(dao.findBy(EmpDepartment.class, assetType.getEmpDepartment().getDepartmentId()));
            details.setUpdatedBy(employee.getEmployeeId());
            details.setUpdatedDate(new Second());
            details.setReferenceNumber(dto.getReferenceNumber());
            details.setAmount(dto.getAmount()!=null?dto.getAmount():null);
            System.out.println("id:" + dto.getCountryId());
            if(dto.getCountryId()!=null){
    			CountryLookUp con = dao.findBy(CountryLookUp.class, dto.getCountryId());
    			details.setCountry(con);
    			System.out.println("name:"+ con.getName());
    			}
            
            
        }
        return details;
        
    }
    
    
}
