/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.raybiztech.assetmanagement.controller;

import com.raybiztech.appraisals.business.EmpDepartment;
import com.raybiztech.appraisals.dto.EmpDepartmentDTO;
import com.raybiztech.appraisals.security.utils.SecurityUtils;
import com.raybiztech.assetmanagement.dto.AssetAuditDto;
import com.raybiztech.assetmanagement.dto.AssetDto;
import com.raybiztech.assetmanagement.dto.ManufacturerDto;
import com.raybiztech.assetmanagement.dto.ProductDto;
import com.raybiztech.assetmanagement.dto.ProductSpecificationDto;
import com.raybiztech.assetmanagement.dto.SearchQueryParamsInAssetList;
import com.raybiztech.assetmanagement.dto.VendorDto;
import com.raybiztech.assetmanagement.service.AssetManagementService;
import com.raybiztech.date.Date;
import com.raybiztech.projectmanagement.dto.ReportDTO;
import com.raybiztech.recruitment.utils.DateParser;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author anil
 */
@Controller("assetManagementController")
@RequestMapping("/assetManagement")
public class AssetManagementController {

	Logger logger = Logger.getLogger(AssetManagementController.class);

	@Autowired
	AssetManagementService assetManagementServiceImpl;
	@Autowired
	SecurityUtils securityUtils;

	@RequestMapping(value = "/addVendorDetails", method = RequestMethod.POST)
	public @ResponseBody void addVendorDetails(@RequestBody VendorDto vendorDto) {

		assetManagementServiceImpl.addVendorDetails(vendorDto);

	}

	@RequestMapping(value = "/deleteVendorDetails", params = { "vendorId" }, method = RequestMethod.DELETE)
	public @ResponseBody void deleteVendorDetails(
			@RequestParam("vendorId") Long vendorId) {
		assetManagementServiceImpl.deleteVendorDetails(vendorId);
	}

	@RequestMapping(value = "/getAllVendorDetails", params = { "vendorName",
			"startIndex", "endIndex" }, method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> getAllVendorDetails(
			@RequestParam String vendorName, @RequestParam Integer startIndex,
			@RequestParam Integer endIndex) {
		return assetManagementServiceImpl.getAllVendorDetails(vendorName,
				startIndex, endIndex);

	}

	@RequestMapping(value = "/updateVendorDetails", method = RequestMethod.PUT)
	public @ResponseBody void updateVendorDetails(
			@RequestBody VendorDto vendorDto) {

		assetManagementServiceImpl.updateVendorDetails(vendorDto);
	}

	@RequestMapping(value = "/addProduct", method = RequestMethod.POST)
	public @ResponseBody void addProduct(@RequestBody ProductDto productDto) {
		assetManagementServiceImpl.addProduct(productDto);

	}

	@RequestMapping(value = "/getAllProductTypes", params = { "productName",
			"startIndex", "endIndex" }, method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> getAllProductTypes(
			@RequestParam String productName, @RequestParam Integer startIndex,
			@RequestParam Integer endIndex) {

		return assetManagementServiceImpl.getAllProductTypes(productName,
				startIndex, endIndex);
	}

	@RequestMapping(value = "/updateProduct", method = RequestMethod.PUT)
	public @ResponseBody void updateProduct(@RequestBody ProductDto productDto) {
		assetManagementServiceImpl.updateProduct(productDto);

	}

	@RequestMapping(value = "/deleteProduct", params = { "productId" }, method = RequestMethod.DELETE)
	public @ResponseBody void deleteProduct(
			@RequestParam("productId") Long productId) {
		assetManagementServiceImpl.deleteProduct(productId);
	}

	@RequestMapping(value = "/addManufacturer", method = RequestMethod.POST)
	public @ResponseBody void addManufacturer(
			@RequestBody ManufacturerDto manufacturerDto) {
		assetManagementServiceImpl.addManufacturer(manufacturerDto);
	}

	@RequestMapping(value = "/getAllManufacturerName", params = {
			"manufacturerName", "startIndex", "endIndex" }, method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> getAllManufacturerName(
			@RequestParam String manufacturerName,
			@RequestParam Integer startIndex, @RequestParam Integer endIndex) {
		return assetManagementServiceImpl.getAllManufacturerName(
				manufacturerName, startIndex, endIndex);
	}

	@RequestMapping(value = "/deleteManufacturerName", params = { "manufacturerId" }, method = RequestMethod.DELETE)
	public @ResponseBody void deleteManufacturerName(
			@RequestParam("manufacturerId") Long manufacturerId) {
		assetManagementServiceImpl.deleteManufacturer(manufacturerId);
	}

	@RequestMapping(value = "/updateManufacturerName", method = RequestMethod.PUT)
	public @ResponseBody void updateManufacturerName(
			@RequestBody ManufacturerDto manufacturerDto) {
		assetManagementServiceImpl.updateManufacturer(manufacturerDto);
	}

	@RequestMapping(value = "/addProductSpecifications", method = RequestMethod.POST)
	public @ResponseBody void addProductSpecifications(
			@RequestBody ProductSpecificationDto specificationDto) {
		assetManagementServiceImpl.addProductSpecifications(specificationDto);
	}

	@RequestMapping(value = "/getAllProductSpecifications", params = {
			"productName", "startIndex", "endIndex" }, method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> getAllProductSpecifications(
			@RequestParam String productName, @RequestParam Integer startIndex,
			@RequestParam Integer endIndex) {
		return assetManagementServiceImpl.getAllProductSpecifications(
				productName, startIndex, endIndex);

	}

	@RequestMapping(value = "/deleteProductSpecification", params = { "specificationId" }, method = RequestMethod.DELETE)
	public @ResponseBody void deleteProductSpecification(
			@RequestParam("specificationId") Long specificationId) {
		assetManagementServiceImpl.deleteProductSpecification(specificationId);
	}

	@RequestMapping(value = "/updateProductSpecification", method = RequestMethod.PUT)
	public @ResponseBody void updateProductSpecification(
			@RequestBody ProductSpecificationDto dto) {
		assetManagementServiceImpl.updateProductSpecification(dto);
	}

	@RequestMapping(value = "/typeChangeSpecifications", params = {
			"productId", "manufacturerId" }, method = RequestMethod.GET)
	public @ResponseBody List<ProductSpecificationDto> typeChangeSpecifications(
			@RequestParam("productId") Long productId,
			@RequestParam("manufacturerId") Long manufacturerId) {
		return assetManagementServiceImpl.typeChangeSpecifications(productId,
				manufacturerId);

	}

	@RequestMapping(value = "/addAsset", method = RequestMethod.POST)
	public @ResponseBody void addAsset(@RequestBody AssetDto assetDto) {
		assetManagementServiceImpl.addAsset(assetDto);
	}

	@RequestMapping(value = "/getAllAssets", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> getAllAssets(
			@RequestBody SearchQueryParamsInAssetList paramsInAssetList) {
		return assetManagementServiceImpl.getAllAssets(paramsInAssetList);

	}
	
	@RequestMapping(value = "/getAllAssets", method = RequestMethod.POST)
	public @ResponseBody List<AssetDto> getAllAssets(@RequestBody AssetRequestDto request) {
	    logger.info("Fetching all assets with request: " + request);
	    return assetManagementServiceImpl.getAllAssets(request);
	}

	
	
	

	@RequestMapping(value = "/checkAssetNumberExixts", params = { "AssetNumber" }, method = RequestMethod.GET)
	public @ResponseBody Boolean checkAssetNumberUniqueness(
			@RequestParam String AssetNumber) {
		return assetManagementServiceImpl
				.checkAssetNumberUniqueness(AssetNumber);
	}

	/*
	 * @RequestMapping(value = "/saveEmployee", params = {
	 * "assetId","status","date", "employeeId", "description","location",
	 * "referenceNumber" }, method = RequestMethod.PUT) public @ResponseBody
	 * void saveEmployee(@RequestParam Long assetId, @RequestParam String
	 * status,@RequestParam String date,
	 * 
	 * @RequestParam Long employeeId, @RequestParam String description,
	 * 
	 * @RequestParam String location, @RequestParam String referenceNumber) {
	 * Date parsedDate = null; try { parsedDate = DateParser.toDate(date); }
	 * catch (ParseException e) { e.printStackTrace(); } Long empId =
	 * securityUtils .getLoggedEmployeeIdforSecurityContextHolder();
	 * assetManagementServiceImpl.saveEmployee(assetId,
	 * status,parsedDate,employeeId, description, empId, location,
	 * referenceNumber); }
	 */

	@RequestMapping(value = "/saveEmployee", method = RequestMethod.PUT)
	public @ResponseBody void saveEmployee(
			@RequestBody AssetAuditDto assetAuditDto) {
		Date parsedDate = null;
		try {
			parsedDate = DateParser.toDate(assetAuditDto.getDate());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Long empId = securityUtils
				.getLoggedEmployeeIdforSecurityContextHolder();
		assetManagementServiceImpl.saveEmployee(assetAuditDto);
	}

	@RequestMapping(value = "/deassignAsset", params = { "assetId" }, method = RequestMethod.DELETE)
	public @ResponseBody void deassignAsset(@RequestParam Long assetId) {
		assetManagementServiceImpl.deassignAsset(assetId);
	}

	@RequestMapping(value = "/getAllLookUps", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> getAllLookUps() {

		return assetManagementServiceImpl.getAllLookUps();

	}

	@RequestMapping(value = "/getActiveEmployeeList", method = RequestMethod.GET)
	public @ResponseBody List<ReportDTO> getActiveEmployee() {
		Long id = securityUtils.getLoggedEmployeeIdforSecurityContextHolder();
		return assetManagementServiceImpl.getActiveEmployeesList(id);

	}

	@RequestMapping(value = "/getAssetHistory", params = { "assetId",
			"searchAssetReference" }, method = RequestMethod.GET)
	public @ResponseBody List<AssetAuditDto> getAssetHistory(
			@RequestParam Long assetId,
			@RequestParam String searchAssetReference) {
		return assetManagementServiceImpl.getAssetHistory(assetId,
				searchAssetReference);
	}

	@RequestMapping(value = "/getassetTypeChangeList", params = { "id" }, method = RequestMethod.GET)
	public @ResponseBody List<ProductDto> getassetTypeChangeList(
			@RequestParam Long id) {
		return assetManagementServiceImpl.getAssetTypeChangeList(id);

	}

	@RequestMapping(value = "/getProductTypeChangeList", params = { "productId" }, method = RequestMethod.GET)
	public @ResponseBody List<ManufacturerDto> getProductTypeChangeList(
			@RequestParam Long productId) {
		return assetManagementServiceImpl.getProductTypeChangeList(productId);

	}

	/*
	 * @RequestMapping(value = "/getLoggedEmployeeName", method =
	 * RequestMethod.GET) public @ResponseBody SearchEmpDetailsDTO
	 * getLoggedInEmployeeName() {
	 * 
	 * Long empId = securityUtils
	 * .getLoggedEmployeeIdforSecurityContextHolder(); return
	 * assetManagementServiceImpl.getUpdateByName(empId); }
	 */

	@RequestMapping(value = "/updateAssetDetails", method = RequestMethod.PUT)
	public @ResponseBody void updateAssetDetails(@RequestBody AssetDto assetDto) {
		assetManagementServiceImpl.updateAssetDetails(assetDto);
	}

	@RequestMapping(value = "/getEmpDepartments", method = RequestMethod.GET)
	public @ResponseBody List<EmpDepartment> getEmpDepartments() {
		return assetManagementServiceImpl.getEmpDepartments();
	}

	@RequestMapping(value = "/getTransactionalHistory", params = {
			"startIndex", "endIndex", "from", "to", "dateSelection" }, method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> getTransactionalHistory(
			@RequestBody AssetAuditDto assetAuditDtos,
			@RequestParam Integer startIndex, @RequestParam Integer endIndex,
			@RequestParam String from, @RequestParam String to,
			@RequestParam String dateSelection) {
		return assetManagementServiceImpl.getTransactionalHistory(
				assetAuditDtos, startIndex, endIndex, from, to, dateSelection);

	}

	@RequestMapping(value = "/getDepartmentNameList", method = RequestMethod.GET)
	public @ResponseBody List<EmpDepartmentDTO> getDepartmentNameList() {
		return assetManagementServiceImpl.getDepartmentNameList();
	}

	@RequestMapping(value = "/exportVendorList", params = { "vendorNameSearch" }, method = RequestMethod.GET)
	public @ResponseBody void exportVendorList(
			@RequestParam String vendorNameSearch,
			HttpServletResponse httpServletResponse) throws IOException {
		httpServletResponse.setContentType("text/csv");
		httpServletResponse.setHeader("Content-Disposition",
				"attachment; filename=\"VendorList.csv\"");
		ByteArrayOutputStream os = assetManagementServiceImpl
				.exportVendorData(vendorNameSearch);
		httpServletResponse.getOutputStream().write(os.toByteArray());

	}

	@RequestMapping(value = "/exportManufacturerList", params = { "manufacturerNameSearch" }, method = RequestMethod.GET)
	public @ResponseBody void exportManufacturerList(
			@RequestParam String manufacturerNameSearch,
			HttpServletResponse httpServletResponse) throws IOException {
		httpServletResponse.setContentType("text/csv");
		httpServletResponse.setHeader("Content-Disposition",
				"attachment; filename=\"ManufacturerList.csv\"");
		ByteArrayOutputStream os = assetManagementServiceImpl
				.exportManufacturerListData(manufacturerNameSearch);
		httpServletResponse.getOutputStream().write(os.toByteArray());

	}

	@RequestMapping(value = "/exportProductSpecificationList", params = { "specificationSearch" }, method = RequestMethod.GET)
	public @ResponseBody void exportProductSpecificationList(
			@RequestParam String specificationSearch,
			HttpServletResponse httpServletResponse) throws IOException {
		httpServletResponse.setContentType("text/csv");
		httpServletResponse.setHeader("Content-Disposition",
				"attachment; filename=\"ProductSpecificationList.csv\"");
		ByteArrayOutputStream os = assetManagementServiceImpl
				.exportProductSpecificationListData(specificationSearch);
		httpServletResponse.getOutputStream().write(os.toByteArray());

	}

	@RequestMapping(value = " /exportProductList", params = { "productSearch" }, method = RequestMethod.GET)
	public @ResponseBody void exportProductList(
			@RequestParam String productSearch,
			HttpServletResponse httpServletResponse) throws IOException {
		httpServletResponse.setContentType("text/csv");
		httpServletResponse.setHeader("Content-Disposition",
				"attachment; filename=\"ProductList.csv\"");
		ByteArrayOutputStream os = assetManagementServiceImpl
				.exportProductData(productSearch);
		httpServletResponse.getOutputStream().write(os.toByteArray());
	}

	@RequestMapping(value = "/getWarrantyAssetsList", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> getWarrantyAssetsList(
			@RequestParam Integer startIndex, @RequestParam Integer endIndex,
			@RequestParam String from, @RequestParam String to,
			@RequestParam String dateSelection) {
		return assetManagementServiceImpl.getWarrantyAssetsList(startIndex,
				endIndex, from, to, dateSelection);

	}

	@RequestMapping(value = "/exportAssetWarrantyList", params = {
			"startIndex", "endIndex", "from", "to", "dateSelection" }, method = RequestMethod.GET)
	public @ResponseBody void exportAssetWarrantyList(
			@RequestParam Integer startIndex, @RequestParam Integer endIndex,
			@RequestParam String from, @RequestParam String to,
			@RequestParam String dateSelection, HttpServletResponse response)
			throws IOException {

		response.setContentType("text/csv");
		response.setHeader("Content-Disposition",
				"attachment; filename=\"AssetWarrantyEndDateList.csv\"");
		ByteArrayOutputStream os = assetManagementServiceImpl
				.exportAssetWarrantyList(startIndex, endIndex, from, to,
						dateSelection, response);
		response.getOutputStream().write(os.toByteArray());

	}

	@RequestMapping(value = "/exportAssetList", params = { "assetss",
			"assetTypeId", "productId", "status", "multiSearch",
			"searchByEmpName","dateSelection","fromDate","toDate" }, method = RequestMethod.GET)
	public @ResponseBody void exportAssetList(@RequestParam String assetss,
			@RequestParam Long assetTypeId, @RequestParam Long productId,
			@RequestParam String status, @RequestParam String multiSearch,
			@RequestParam Boolean searchByEmpName,@RequestParam String dateSelection,@RequestParam String fromDate,@RequestParam String toDate, HttpServletResponse response)
			throws IOException {
		response.setContentType("text/csv");
		response.setHeader("Content-Disposition",
				"attachment; filename=\"AssetList.csv\"");
		ByteArrayOutputStream os = assetManagementServiceImpl.exportAssetList(
				assetss, assetTypeId, productId, status, multiSearch,
				searchByEmpName,dateSelection,fromDate,toDate, response);
		response.getOutputStream().write(os.toByteArray());

	}

}
