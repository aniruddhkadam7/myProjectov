/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.raybiztech.assetmanagement.builder;

import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.dao.DAO;
import com.raybiztech.appraisals.security.utils.SecurityUtils;
import com.raybiztech.assetmanagement.business.AssetAudit;
import com.raybiztech.assetmanagement.business.VendorDetails;
import com.raybiztech.assetmanagement.dto.AssetAuditDto;
import com.raybiztech.assetmanagement.dto.AssetDto;
import com.raybiztech.date.Second;
import com.raybiztech.leavemanagement.business.algorithm.DateParser;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author anil
 */
@Component("assetAuditBuilder")
public class AssetAuditBuilder {
	@Autowired
	DAO dao;
	@Autowired
	SecurityUtils securityUtils;

	public AssetAudit ToEntity(AssetDto assetDto) {
		AssetAudit audit = null;
		if (assetDto != null) {
			audit = new AssetAudit();
			audit.setAsset(null);
		}
		return null;

	}

	public AssetAuditDto ToDTO(AssetAudit assetAudit) {
		AssetAuditDto auditDto = null;
		if (assetAudit != null) {
			auditDto = new AssetAuditDto();
			auditDto.setId(assetAudit.getId());
			AssetAudit audit = dao.findBy(AssetAudit.class, assetAudit.getId());
			auditDto.setAssetNumber(audit.getAsset().getAssetNumber());
			auditDto.setProductName(audit.getAsset().getProductSpecifications()
					.getProduct().getProductName());
			auditDto.setpSpecification(audit.getAsset()
					.getProductSpecifications().getProductSpecification());
			if (audit.getEmployee() != null)
				auditDto.setEmployeeName(audit.getEmployee().getFullName());
			auditDto.setDescription(audit.getDescription());
//			if (audit.getAssinedDate() != null)
//				auditDto.setAssignedDate(audit.getAssinedDate().toString(
//						"dd-MMM-yyyy hh:mm a"));
			auditDto.setStatus(audit.getStatus());
			if (audit.getDate() != null)
				auditDto.setDate(audit.getDate().toString("dd-MMM-yyyy"));
			if (audit.getUpdatedBy() != null)
				auditDto.setUpdatedBy(audit.getUpdatedBy().getFullName());
			if (audit.getLocation() != null)
				auditDto.setLocation(audit.getLocation());
			//if (audit.getReferenceNumber() != null)
				auditDto.setReferenceNumber(audit.getReferenceNumber());
		//	if(audit.getVendorDetails() != null)
			auditDto.setVendorName(audit.getVendorName());
			auditDto.setAssetId(audit.getAsset().getId());
			auditDto.setInvoiceNumber(audit.getAsset().getInvoiceNumber()!=null?audit.getAsset().getInvoiceNumber():null);
			auditDto.setAmount(audit.getAsset().getAmount()!=null?audit.getAsset().getAmount():null);
			

		}
		return auditDto;

	}

	public List<AssetAuditDto> ToDTOList(List<AssetAudit> assetAudit) {
		List<AssetAuditDto> auditDtosList = null;
		if (assetAudit != null) {
			auditDtosList = new ArrayList<AssetAuditDto>();
			for (AssetAudit aa : assetAudit) {
				auditDtosList.add(ToDTO(aa));
			}
		}
		return auditDtosList;
	}

	public AssetAudit toEntityAudit(AssetAuditDto assetAuditDto) {
		Long logedInEmpId = securityUtils
				.getLoggedEmployeeIdforSecurityContextHolder();
		AssetAudit assetAudit = null;
		if (assetAuditDto != null) {
			assetAudit = new AssetAudit();
			//assetAudit.setId(assetAuditDto.getId());
			// Asset asset2 = dao.findBy(Asset.class, assetAuditDto.getId());
			// asset2.setAllocatedStatus("");
			// assetAudit.setAsset(asset2);
			assetAudit.setStatus(assetAuditDto.getStatus());
			assetAudit.setDescription(assetAuditDto.getDescription());
			try {
				assetAudit.setDate(DateParser.toDate(assetAuditDto.getDate()));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			assetAudit.setLocation(assetAuditDto.getLocation());
			assetAudit.setReferenceNumber(assetAuditDto.getReferenceNumber());
			VendorDetails vendorDetails = dao.findBy(VendorDetails.class,
					assetAuditDto.getVendorId());
			assetAudit.setVendorName(vendorDetails.getVendorName());

			Employee employee = dao.findBy(Employee.class, logedInEmpId);
			assetAudit.setUpdatedBy(employee);
                        assetAudit.setUpdatedDate(new Second());

		}

		return assetAudit;
	}
	
	


}
