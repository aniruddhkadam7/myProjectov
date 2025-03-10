/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.raybiztech.assetmanagement.dao;

import java.util.List;

import com.raybiztech.appraisals.business.EmpDepartment;
import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.dao.DAO;
import com.raybiztech.assetmanagement.business.Asset;
import com.raybiztech.assetmanagement.business.AssetAudit;
import com.raybiztech.assetmanagement.business.AssetType;
import com.raybiztech.assetmanagement.business.Manufacturer;
import com.raybiztech.assetmanagement.business.Product;
import com.raybiztech.assetmanagement.business.ProductSpecifications;
import com.raybiztech.assetmanagement.business.VendorDetails;
import com.raybiztech.assetmanagement.dto.AssetAuditDto;
import com.raybiztech.assetmanagement.dto.SearchQueryParamsInAssetList;
import com.raybiztech.date.Date;
import com.raybiztech.date.DateRange;
import com.raybiztech.projectmanagement.business.Project;

import java.util.Map;

import org.hibernate.Criteria;

/**
 *
 * @author anil
 */
public interface AssetManagementDAO extends DAO {

	void addProductSpecifications(ProductSpecifications specifications);

	List<ProductSpecifications> typeChangeSpecifications(Long productId,
			Long manufacturerId);

	// List<VendorDetails> searchByVendorName(String vendorName);

	// List<Manufacturer> searchByManufacturerName(String manufacturerName);

	// List<Product> searchByProductType(String productName);

	// List<ProductSpecifications> searchBySpecificationProductType(
	// String productName);

	public ProductSpecifications specificationId(Long productId,
			Long manufacturerId, String pSpecification);

	// Map<String,Object> getAllAssetsForAdmin(SearchQueryParamsInAssetList
	// paramsInAssetList);

	Map<String, Object> getAllAssets(
			SearchQueryParamsInAssetList paramsInAssetList, Employee employee,
			Boolean seperateListFalg,Date fromDate,Date toDate);

	/*Criteria getAllAssetsLiseSize(
			SearchQueryParamsInAssetList paramsInAssetList, Employee employee,
			Boolean seperateListFalg,Boolean sess);*/

	Map<String, Object> getAllProductSpecifications(String multipleSearch,
			Integer startIndex, Integer endIndex);

	Map<String, Object> getAllVendorDetails(String multipleSearch,
			Integer startIndex, Integer endIndex);

	Map<String, Object> getAllManufacturerName(String multipleSearch,
			Integer startIndex, Integer endIndex);

	Map<String, Object> getAllProducts(String multipleSearch,
			Integer startIndex, Integer endIndex);

	Map<String, Object> getAllLookUps();

	List<AssetAudit> getAssetHistory(Long assetId, String searchAssetReference);

	List<Employee> getactiveEmployeeList(Employee employee);

	List<Product> getAssetTypeChangeList(Long id);

	List<Manufacturer> getProductTypeChangeList(Long productId);

	Map<String, Object> getTransactionalHistory(AssetAuditDto assetAuditDtos,
			Integer startIndex, Integer endIndex, DateRange dateRange,
			Boolean seperateListFalg);

	List<Asset> getEmpAssets(Asset asset);

	List<EmpDepartment> getDepartmentNameList();

	List<Asset> getEmployeeAssets(Long employeeId);

	Map<String, Object> getWarrantyAssetsList(Integer startIndex,
			Integer endIndex, DateRange dateRange, Boolean seperateListFalg);

	Map<AssetType, Object> getAssetWhoseWarrantyEndDateisInNextFifteennDays();

	Map<String, Object> getAllAssetsForExport(String assetss, Long assetTypeId,
			Long productId, String status, String multiSearch,Date fromDate,Date toDate,
			Boolean searchByEmpName, Employee employee, Boolean seperateListFalg);
}
