/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.raybiztech.assetmanagement.service;

import com.raybiztech.appraisals.business.EmpDepartment;
import com.raybiztech.appraisals.dto.EmpDepartmentDTO;
import com.raybiztech.appraisals.dto.SearchEmpDetailsDTO;
import com.raybiztech.assetmanagement.dto.AssetAuditDto;
import com.raybiztech.assetmanagement.dto.AssetDto;
import com.raybiztech.assetmanagement.dto.ManufacturerDto;
import com.raybiztech.assetmanagement.dto.ProductDto;
import com.raybiztech.assetmanagement.dto.ProductSpecificationDto;
import com.raybiztech.assetmanagement.dto.SearchQueryParamsInAssetList;
import com.raybiztech.assetmanagement.dto.VendorDto;
import com.raybiztech.date.Date;
import com.raybiztech.projectmanagement.dto.ReportDTO;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder.In;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author anil
 */
public interface AssetManagementService {

	void addVendorDetails(VendorDto vendorDto);

	void deleteVendorDetails(Long vendorId);

	void updateVendorDetails(VendorDto vendorDto);

	Map<String, Object> getAllVendorDetails(String vendorName,
			Integer startIndex, Integer endIndex);

	void addProduct(ProductDto productDto);

	Map<String, Object> getAllProductTypes(String productName,
			Integer startIndex, Integer endIndex);

	void updateProduct(ProductDto productDto);

	void deleteProduct(Long productId);

	void addManufacturer(ManufacturerDto manufacturerDto);

	Map<String, Object> getAllManufacturerName(String manufacturerName,
			Integer startIndex, Integer endIndex);

	void deleteManufacturer(Long manufacturerId);

	void updateManufacturer(ManufacturerDto manufacturerDto);

	void addProductSpecifications(ProductSpecificationDto specificationDto);

	Map<String, Object> getAllProductSpecifications(String productName,
			Integer startIndex, Integer endIndex);

	void deleteProductSpecification(Long specificationId);

	void updateProductSpecification(ProductSpecificationDto dto);

	// List<ProductDto> searchByProductType(String productName);

	// List<VendorDto> serachVendor(String vendorName);

	// List<ManufacturerDto> serachManufacturer(String manufacturerName);

	List<ProductSpecificationDto> typeChangeSpecifications(Long productId,
			Long manufacturerId);

	// List<ProductSpecificationDto> searchBySpecificationProductType(String
	// productName);

	void addAsset(AssetDto assetDto);

	Map<String, Object> getAllAssets(
			SearchQueryParamsInAssetList paramsInAssetList);

	Boolean checkAssetNumberUniqueness(String assetNumber);

	void saveEmployee(AssetAuditDto assetAuditDto);

	void deassignAsset(Long assetId);

	Map<String, Object> getAllLookUps();

	List<AssetAuditDto> getAssetHistory(Long assetId,
			String searchAssetReference);

	List<ReportDTO> getActiveEmployeesList(Long id);

	List<ProductDto> getAssetTypeChangeList(Long id);

	List<ManufacturerDto> getProductTypeChangeList(Long productId);

	void updateAssetDetails(AssetDto assetDto);

	public SearchEmpDetailsDTO getUpdateByName(Long employeeId);

	List<EmpDepartment> getEmpDepartments();

	Map<String, Object> getTransactionalHistory(AssetAuditDto assetAuditDtos,
			Integer startIndex, Integer endIndex, String from, String to,
			String dateSelection);

	List<EmpDepartmentDTO> getDepartmentNameList();

	List<AssetDto> getEmployeeAssets(Long employeeId);

	ByteArrayOutputStream exportVendorData(String vendorNameSearch)
			throws IOException;

	ByteArrayOutputStream exportManufacturerListData(
			String manufacturerNameSearch) throws IOException;

	ByteArrayOutputStream exportProductSpecificationListData(
			String specificationSearch) throws IOException;

	ByteArrayOutputStream exportProductData(String productSearch)
			throws IOException;

	Map<String, Object> getWarrantyAssetsList(Integer startIndex,
			Integer endIndex, String from, String to, String dateSelection);

	ByteArrayOutputStream exportAssetWarrantyList(Integer startIndex,
			Integer endIndex, String from, String to, String dateSelection,
			HttpServletResponse response) throws IOException;

	ByteArrayOutputStream exportAssetList(String assetss, Long assetTypeId,
			Long productId, String status, String multiSearch,
			Boolean searchByEmpName,String dateSelection,String fromDate,String toDate, HttpServletResponse response)
			throws IOException;
}
