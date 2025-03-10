package com.raybiztech.assetmanagement.builder;
/**
*
* @author Aprajita
*/

import com.raybiztech.appraisals.business.EmpDepartment;
import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.dao.DAO;
import com.raybiztech.appraisals.security.utils.SecurityUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.raybiztech.assetmanagement.business.VendorDetails;
import com.raybiztech.assetmanagement.dto.VendorDto;
import com.raybiztech.date.Second;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

@Component("vendorBuilder")
public class VendorBuilder {
    
    @Autowired
     DAO dao;
    @Autowired
     SecurityUtils securityUtils;
	
	Logger logger = Logger.getLogger(VendorBuilder.class);
	
	public VendorDetails toEntity(VendorDto vendorDto){
		Long loggedInEmpId = securityUtils.getLoggedEmployeeIdforSecurityContextHolder();
            Employee employee = dao.findBy(Employee.class, loggedInEmpId);
        
		VendorDetails vendorDetails = null;
		if (vendorDto != null) {
			vendorDetails = new VendorDetails();
//			vendorDetails.setVendorId(vendorDto.getVendorId());
			vendorDetails.setVendorName(vendorDto.getVendorName());
			vendorDetails.setVendorPhoneNumber(vendorDto.getVendorPhoneNumber());
			vendorDetails.setVendorFaxNumber(vendorDto.getVendorFaxNumber());
			vendorDetails.setVendorAddress(vendorDto.getVendorAddress());
			vendorDetails.setVendorCity(vendorDto.getVendorCity());
			vendorDetails.setVendorState(vendorDto.getVendorState());
			vendorDetails.setVendorCountry(vendorDto.getVendorCountry());
			vendorDetails.setVendorPincode(vendorDto.getVendorPincode());
			vendorDetails.setVendorEmailId(vendorDto.getVendorEmailId());
            vendorDetails.setEmpDepartment(vendorDto.getDepartmentId()!=null?dao.findBy(EmpDepartment.class, vendorDto.getDepartmentId()):dao.findByUniqueProperty(EmpDepartment.class, "departmentName",employee.getDepartmentName()));
            vendorDetails.setCreatedBy(employee.getEmployeeId());
            vendorDetails.setCreatedDate(new Second());
            if(vendorDto.getIsExpenseVendor() != null && vendorDto.getIsExpenseVendor()) {
            vendorDetails.setIsExpenseVendor(Boolean.TRUE);
            }
            else {
            	vendorDetails.setIsExpenseVendor(Boolean.FALSE);
            }
            vendorDetails.setVendorBankDetails(vendorDto.getVendorBankDetails());
            vendorDetails.setvendorGSTNumber(vendorDto.getvendorGSTNumber());
		}
		
	return vendorDetails;	
	}

	public VendorDto toDto(VendorDetails vendorDetails){
            Employee createdBy = null,updatedBy = null;
          //  if(vendorDetails.getCreatedBy()!=null)
          createdBy  =vendorDetails.getCreatedBy()!=null?dao.findBy(Employee.class, vendorDetails.getCreatedBy()):null;
          //   if(vendorDetails.getUpdatedBy()!=null)
              updatedBy=vendorDetails.getUpdatedBy()!=null?dao.findBy(Employee.class, vendorDetails.getUpdatedBy()):null;
            VendorDto dto=null;
            if(vendorDetails!=null){
                dto=new VendorDto();
                dto.setVendorId(vendorDetails.getVendorId());
                dto.setVendorName(vendorDetails.getVendorName());
                dto.setVendorPhoneNumber(vendorDetails.getVendorPhoneNumber());
                dto.setVendorFaxNumber(vendorDetails.getVendorFaxNumber());
                dto.setVendorAddress(vendorDetails.getVendorAddress());
                dto.setVendorCity(vendorDetails.getVendorCity());
                dto.setVendorState(vendorDetails.getVendorState());
                dto.setVendorCountry(vendorDetails.getVendorCountry());
                dto.setVendorPincode(vendorDetails.getVendorPincode());
                dto.setVendorEmailId(vendorDetails.getVendorEmailId());
                dto.setDepartmentName(vendorDetails.getEmpDepartment().getDepartmentName());
                dto.setDepartmentId(vendorDetails.getEmpDepartment().getDepartmentId());
                dto.setCreatedBy(createdBy!=null?createdBy.getFullName():"");
                dto.setUpdatedBy(updatedBy!=null?updatedBy.getFullName():"");
                dto.setCreatedDate(vendorDetails.getCreatedDate()!=null?vendorDetails.getCreatedDate().toString("dd/MM/yyyy"):"");
                dto.setUpdatedDate(vendorDetails.getUpdatedDate()!=null?vendorDetails.getUpdatedDate().toString("dd/MM/yyyy"):"");
                dto.setIsExpenseVendor(vendorDetails.getIsExpenseVendor());
                dto.setVendorBankDetails(vendorDetails.getVendorBankDetails());
                dto.setvendorGSTNumber(vendorDetails.getvendorGSTNumber());
            }
            
            return dto;
		
	}
        
        public VendorDetails toEditEntity(VendorDto dto){
            Long loggedInEmpId = securityUtils.getLoggedEmployeeIdforSecurityContextHolder();
            Employee employee = dao.findBy(Employee.class, loggedInEmpId);
            
            VendorDetails details=null;
            if(dto!=null){
                details=dao.findBy(VendorDetails.class, dto.getVendorId());
                details.setVendorName(dto.getVendorName());
                details.setVendorEmailId(dto.getVendorEmailId());
                details.setVendorPhoneNumber(dto.getVendorPhoneNumber());
                details.setVendorAddress(dto.getVendorAddress());
                details.setVendorCity(dto.getVendorCity());
                details.setVendorCountry(dto.getVendorCountry());
                details.setVendorState(dto.getVendorState());
                details.setVendorFaxNumber(dto.getVendorFaxNumber());
                details.setVendorPincode(dto.getVendorPincode());
                details.setEmpDepartment(dao.findBy(EmpDepartment.class, dto.getDepartmentId()));
                details.setUpdatedBy(employee.getEmployeeId());
                details.setUpdatedDate(new Second());
                if(dto.getIsExpenseVendor() != null && dto.getIsExpenseVendor()) {
                	details.setIsExpenseVendor(Boolean.TRUE);
                    }
                    else {
                    	details.setIsExpenseVendor(Boolean.FALSE);
                    }
                details.setVendorBankDetails(dto.getVendorBankDetails());
               
                details.setvendorGSTNumber(dto.getvendorGSTNumber());
                
            }
            
            return details;
            
        }
        
        public List<VendorDto> convertEntityToDtoList(List<VendorDetails> vendorDetailsList){
            List<VendorDto> vendorDtoList=new ArrayList<VendorDto>();
            if(vendorDetailsList!=null){
                for(VendorDetails ven:vendorDetailsList){
                    vendorDtoList.add(toDto(ven));
                }
            
            }
        
        return vendorDtoList;
        
        }	
	
}
