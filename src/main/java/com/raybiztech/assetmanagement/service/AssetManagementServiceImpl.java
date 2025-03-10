/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.raybiztech.assetmanagement.service;

import com.raybiztech.appraisals.builder.EmployeeBuilder;
import com.raybiztech.appraisals.business.EmpDepartment;
import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.business.Finance;
import com.raybiztech.appraisals.dao.DAO;
import com.raybiztech.appraisals.dto.EmpDepartmentDTO;
import com.raybiztech.appraisals.dto.SearchEmpDetailsDTO;
import com.raybiztech.appraisals.properties.PropBean;
import com.raybiztech.appraisals.security.utils.SecurityUtils;
import com.raybiztech.assetmanagement.builder.AssetAuditBuilder;
import com.raybiztech.assetmanagement.builder.AssetBuilder;
import com.raybiztech.assetmanagement.builder.ManufacturerBuilder;
import com.raybiztech.assetmanagement.builder.ProductBuilder;
import com.raybiztech.assetmanagement.builder.ProductSpecificationsBuilder;
import com.raybiztech.assetmanagement.builder.VendorBuilder;
import com.raybiztech.assetmanagement.business.Asset;
import com.raybiztech.assetmanagement.business.AssetAudit;
import com.raybiztech.assetmanagement.business.Manufacturer;
import com.raybiztech.assetmanagement.business.Product;
import com.raybiztech.assetmanagement.business.ProductSpecifications;
import com.raybiztech.assetmanagement.business.VendorDetails;
import com.raybiztech.assetmanagement.dao.AssetManagementDAO;
import com.raybiztech.assetmanagement.dto.AssetAuditDto;
import com.raybiztech.assetmanagement.dto.AssetDto;
import com.raybiztech.assetmanagement.dto.ManufacturerDto;
import com.raybiztech.assetmanagement.dto.ProductDto;
import com.raybiztech.assetmanagement.dto.ProductSpecificationDto;
import com.raybiztech.assetmanagement.dto.SearchQueryParamsInAssetList;
import com.raybiztech.assetmanagement.dto.VendorDto;
import com.raybiztech.date.Date;
import com.raybiztech.date.DateRange;
import com.raybiztech.date.Second;
import com.raybiztech.employeeprofile.dao.EmployeeProfileDAOI;
import com.raybiztech.itdeclaration.business.ITDeclarationForm;
import com.raybiztech.projectmanagement.builder.ReportBuilder;
import com.raybiztech.projectmanagement.business.Milestone;
import com.raybiztech.projectmanagement.dto.ReportDTO;
import com.raybiztech.projectmanagement.service.ProjectServiceImpl;
import com.raybiztech.recruitment.service.JobPortalServiceImpl;
import com.raybiztech.recruitment.utils.DateParser;
import com.raybiztech.recruitment.utils.FileUploaderUtility;
import com.raybiztech.rolefeature.business.Permission;
import com.raybiztech.supportmanagement.builder.TicketsCategoryBuilder;
import com.raybiztech.ticketmanagement.builder.TicketBuilder;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author anil
 */
@Service("assetManagementServiceImpl")
@Transactional
public class AssetManagementServiceImpl implements AssetManagementService {

	@Autowired
	DAO dao;
	@Autowired
	AssetManagementDAO assetManagementDAOImpl;
	@Autowired
	VendorBuilder vendorBuilder;
	@Autowired
	ProductSpecificationsBuilder productSpecificationsBuilder;
	@Autowired
	ProductBuilder productBuilder;
	@Autowired
	ManufacturerBuilder manufacturerBuilder;
	@Autowired
	AssetBuilder assetBuilder;
	@Autowired
	AssetAuditBuilder assetAuditBuilder;
	@Autowired
	ReportBuilder builder;
	@Autowired
	EmployeeBuilder employeeBuilder;
	@Autowired
	TicketsCategoryBuilder ticketsCategoryBuilder;
	@Autowired
	SecurityUtils securityUtils;

	Logger logger = Logger.getLogger(AssetManagementServiceImpl.class);

	@Override
	public void addVendorDetails(VendorDto vendorDto) {
		VendorDetails vendorDetails = vendorBuilder.toEntity(vendorDto);
		assetManagementDAOImpl.save(vendorDetails);
	}

	@Override
	public void deleteVendorDetails(Long vendorId) {
		dao.delete(dao.findBy(VendorDetails.class, vendorId));
	}

	@Override
	public Map<String, Object> getAllVendorDetails(String vendorName,
			Integer startIndex, Integer endIndex) {
		// return vendorBuilder.convertEntityToDtoList(
		Map<String, Object> detailsMap = assetManagementDAOImpl
				.getAllVendorDetails(vendorName, startIndex, endIndex);
		Integer noOfRecords = (Integer) detailsMap.get("size");
		List<VendorDetails> list = (List) detailsMap.get("list");
		List<VendorDto> dtosList = vendorBuilder.convertEntityToDtoList(list);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", dtosList);
		map.put("size", noOfRecords);

		return map;
	}

	@Override
	public void updateVendorDetails(VendorDto vendorDto) {
		VendorDetails vendorDetails = vendorBuilder.toEditEntity(vendorDto);
		assetManagementDAOImpl.update(vendorDetails);
	}

	@Override
	public void addProduct(ProductDto productDto) {
		Product product = productBuilder.toEntity(productDto);
		assetManagementDAOImpl.save(product);

	}

	@Override
	public Map<String, Object> getAllProductTypes(String productName,
			Integer startIndex, Integer endIndex) {
		// return productBuilder.toDTOList(
		Map<String, Object> productList = assetManagementDAOImpl
				.getAllProducts(productName, startIndex, endIndex);
		Integer noOfRecords = (Integer) productList.get("size");
		List<Product> list = (List) productList.get("list");
		List<ProductDto> dtosList = productBuilder.toDTOList(list);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", dtosList);
		map.put("size", noOfRecords);
		return map;
	}

	@Override
	public void updateProduct(ProductDto productDto) {
		Product product = productBuilder.toEditEntity(productDto);
		assetManagementDAOImpl.update(product);
	}

	@Override
	public void deleteProduct(Long productId) {
		dao.delete(dao.findBy(Product.class, productId));
	}

	@Override
	public void addManufacturer(ManufacturerDto manufactureDto) {
		Manufacturer manufacturer = manufacturerBuilder
				.toEntity(manufactureDto);
		assetManagementDAOImpl.save(manufacturer);
	}

	@Override
	public Map<String, Object> getAllManufacturerName(String manufacturerName,
			Integer startIndex, Integer endIndex) {
		Map<String, Object> manufacturerMap = assetManagementDAOImpl
				.getAllManufacturerName(manufacturerName, startIndex, endIndex);
		Integer noOfRecords = (Integer) manufacturerMap.get("size");
		List<Manufacturer> list = (List) manufacturerMap.get("list");
		List<ManufacturerDto> dtosList = manufacturerBuilder.toDTOList(list);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", dtosList);
		map.put("size", noOfRecords);
		return map;
	}

	@Override
	public void deleteManufacturer(Long manufacturerId) {
		dao.delete(dao.findBy(Manufacturer.class, manufacturerId));
	}

	@Override
	public void updateManufacturer(ManufacturerDto manufacturerDto) {
		Manufacturer manufacturer = manufacturerBuilder
				.toEditEntity(manufacturerDto);
		assetManagementDAOImpl.update(manufacturer);
	}

	@Override
	public void addProductSpecifications(
			ProductSpecificationDto specificationDto) {
		ProductSpecifications specifications = productSpecificationsBuilder
				.convertDTOToEntity(specificationDto);
		assetManagementDAOImpl.save(specifications);
	}

	@Override
	public Map<String, Object> getAllProductSpecifications(String productName,
			Integer startIndex, Integer endIndex) {
		Map<String, Object> map = assetManagementDAOImpl
				.getAllProductSpecifications(productName, startIndex, endIndex);
		List<ProductSpecifications> specificationsesList = (List) map
				.get("list");
		Integer noOfRecords = (Integer) map.get("size");
		List<ProductSpecificationDto> dtosList = productSpecificationsBuilder
				.EntityToDTO(specificationsesList);
		Map<String, Object> specificationsMap = new HashMap<String, Object>();
		specificationsMap.put("list", dtosList);
		specificationsMap.put("size", noOfRecords);

		return specificationsMap;

	}

	@Override
	public void deleteProductSpecification(Long specificationId) {
		dao.delete(dao.findBy(ProductSpecifications.class, specificationId));
	}

	@Override
	public void updateProductSpecification(ProductSpecificationDto dto) {
		ProductSpecifications ps = productSpecificationsBuilder
				.toEditEntity(dto);
		assetManagementDAOImpl.update(ps);
	}

	@Override
	public List<ProductSpecificationDto> typeChangeSpecifications(
			Long productId, Long manufacturerId) {
		return productSpecificationsBuilder.EntityToDTO(assetManagementDAOImpl
				.typeChangeSpecifications(productId, manufacturerId));

	}

	@Override
	public void addAsset(AssetDto assetDto) {
		Asset asset = assetBuilder.convertDTOToEntity(assetDto);
		// AssetAudit assetAudit = assetBuilder.convertDTOToEntity(assetDto);

		assetManagementDAOImpl.save(asset);

	}

	@Override
	public Map<String, Object> getAllAssets(
			SearchQueryParamsInAssetList paramsInAssetList) {
		Date from =null;
		Date to=null;
		if(paramsInAssetList.getDateSelection().equalsIgnoreCase("Custom"))
		{
			try {
				 from=DateParser.toDate(paramsInAssetList.getFromDate());
				 to=DateParser.toDate(paramsInAssetList.getToDate());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else
		{
			if(!paramsInAssetList.getDateSelection().equalsIgnoreCase(""))
			{
		Map<String, Date>dateMap=assetManagementDAOImpl.getCustomDates(paramsInAssetList.getDateSelection());
		from= dateMap.get("startDate");
		to= dateMap.get("endDate");
		
			}
		}
		Employee employee = (Employee) securityUtils
				.getLoggedEmployeeDetailsSecurityContextHolder()
				.get("employee");
		Permission totalList = dao.checkForPermission("Asset List", employee);
		Permission departmentList = dao.checkForPermission(
				"Department Asset List", employee);
		Boolean seperateListFalg = false;
		Boolean totalListFlag = false;
		if(totalList.getView()){
			totalListFlag= true;
		}

		if (departmentList.getView() && totalList.getView()) {
			seperateListFalg = true;
		}
		Map<String, Object> getAllAssetsMap = assetManagementDAOImpl
				.getAllAssets(paramsInAssetList, employee, totalListFlag,from,to);
		List<Asset> assetList = (List<Asset>) getAllAssetsMap.get("list");
		Integer noOfrecords = (Integer) getAllAssetsMap.get("size");
		List<AssetDto> assetDtoList = assetBuilder.ToDTOList(assetList);
		Map<String, Object> assetDtoMap = new HashMap<String, Object>();
		assetDtoMap.put("list", assetDtoList);
		assetDtoMap.put("size", noOfrecords);

		return assetDtoMap;
	}

	@Transactional
	@Override
	public Boolean checkAssetNumberUniqueness(String assetNumber) {

		Asset asset = assetManagementDAOImpl.findByUniqueProperty(Asset.class,
				"assetNumber", assetNumber);

		return (asset != null) ? (Boolean.TRUE) : (Boolean.FALSE);
	}

	/*
	 * public void saveEmployee(Long assetId,String status,Date date, Long
	 * employeeId,String description,Long empId, String location, String
	 * referenceNumber) { Asset asset = dao.findBy(Asset.class, assetId);
	 * AssetAudit assetAudit=new AssetAudit(); asset.setStatus(status);
	 * asset.setLocation(location); if(employeeId!=null){ Employee employee =
	 * dao.findBy(Employee.class, employeeId); asset.setEmployee(employee);
	 * asset.setAllocatedStatus("Assigned");
	 * assetAudit.setEmployee(asset.getEmployee());
	 * assetAudit.setAssinedDate(new Second()); } else{ asset.setEmployee(null);
	 * assetAudit.setAssinedDate(null); asset.setAllocatedStatus("Unassigned");
	 * } assetManagementDAOImpl.update(asset);
	 * 
	 * assetAudit.setAsset(asset); assetAudit.setStatus(status);
	 * assetAudit.setDate(date);
	 * 
	 * assetAudit.setDescription(description); assetAudit.setLocation(location);
	 * assetAudit.setReferenceNumber(referenceNumber);
	 * 
	 * // logger.warn("location is" +location); Employee
	 * employee=dao.findBy(Employee.class, empId);
	 * assetAudit.setUpdatedBy(employee);
	 * assetManagementDAOImpl.save(assetAudit); }
	 */
	public void saveEmployee(AssetAuditDto assetAuditDto) {
		Long logedInEmpId = securityUtils
				.getLoggedEmployeeIdforSecurityContextHolder();

		Asset asset = dao.findBy(Asset.class, assetAuditDto.getAssetId());
		AssetAudit assetAudit = assetAuditBuilder.toEntityAudit(assetAuditDto);
		if (assetAuditDto.getEmployeeId() != null) {
			Employee employee = dao.findBy(Employee.class,
					assetAuditDto.getEmployeeId());
			asset.setEmployee(employee);
			asset.setAllocatedStatus("Assigned");
			assetAudit.setEmployee(asset.getEmployee());
			// assetAudit.setAssinedDate(new Second());
		} else {
			asset.setEmployee(null);
			// assetAudit.setAssinedDate(null);
			asset.setAllocatedStatus("Unassigned");
		}
		asset.setStatus(assetAuditDto.getStatus());
		asset.setVendorDetails(dao.findBy(VendorDetails.class,
				assetAuditDto.getVendorId()));
		asset.setLocation(assetAuditDto.getLocation());
		asset.setReferenceNumber(assetAuditDto.getReferenceNumber());
		System.out.println(assetAuditDto.getInvoiceNumber() + assetAuditDto.getAmount());
		if(assetAuditDto.getInvoiceNumber()!=null && !assetAuditDto.getInvoiceNumber().isEmpty()){
		asset.setInvoiceNumber(assetAuditDto.getInvoiceNumber()!=null?assetAuditDto.getInvoiceNumber():null);
		}
		if(assetAuditDto.getAmount()!=null && !assetAuditDto.getAmount().isEmpty()){
		asset.setAmount(assetAuditDto.getAmount()!=null?assetAuditDto.getAmount():null);
		}
		assetManagementDAOImpl.update(asset);
		// same location for all the assets of same employee
		if (assetAuditDto.isLocationForEmpAssets()) {

			List<Asset> asset1 = assetManagementDAOImpl.getEmpAssets(asset);
			for (Asset a : asset1) {
				a.setLocation(assetAuditDto.getLocation());
				assetManagementDAOImpl.update(a);
				if (assetAuditDto.getAssetId().equals(a.getId())) {
					AssetAudit audit = assetAuditBuilder
							.toEntityAudit(assetAuditDto);
					audit.setAsset(a);
					audit.setEmployee(a.getEmployee());
					audit.setEmpDepartment(dao.findBy(EmpDepartment.class, a
							.getEmpDepartment().getDepartmentId()));
					assetManagementDAOImpl.save(audit);
				} else {
					AssetAudit audit = new AssetAudit();
					audit.setAsset(a);
					audit.setStatus(a.getStatus());
					audit.setVendorName(a.getVendorDetails().getVendorName());
					audit.setReferenceNumber(a.getReferenceNumber());
					audit.setLocation(assetAuditDto.getLocation());
					audit.setEmployee(a.getEmployee());
					audit.setEmpDepartment(dao.findBy(EmpDepartment.class, a
							.getEmpDepartment().getDepartmentId()));
					Employee employee = dao
							.findBy(Employee.class, logedInEmpId);
					audit.setUpdatedBy(employee);
					audit.setUpdatedDate(new Second());
					audit.setDescription(assetAuditDto.getDescription());
					try {
						audit.setDate(DateParser.toDate(assetAuditDto.getDate()));
					} catch (ParseException ex) {
						java.util.logging.Logger.getLogger(
								AssetManagementServiceImpl.class.getName())
								.log(Level.SEVERE, null, ex);
					}
					assetManagementDAOImpl.save(audit);
				}
			}

		} else {
			assetAudit.setAsset(asset);
			assetAudit.setEmpDepartment(dao.findBy(EmpDepartment.class, asset
					.getEmpDepartment().getDepartmentId()));
			assetManagementDAOImpl.save(assetAudit);
		}

	}

	@Override
	public void deassignAsset(Long assetId) {
		Asset asset = dao.findBy(Asset.class, assetId);
		asset.setEmployee(null);
		assetManagementDAOImpl.update(asset);
	}

	@Override
	public Map<String, Object> getAllLookUps() {
		return assetManagementDAOImpl.getAllLookUps();

	}

	@Override
	public List<AssetAuditDto> getAssetHistory(Long assetId,
			String searchAssetReference) {

		return assetAuditBuilder.ToDTOList(assetManagementDAOImpl
				.getAssetHistory(assetId, searchAssetReference));
	}

	@Override
	public List<ReportDTO> getActiveEmployeesList(Long id) {
		Employee employee = dao.findBy(Employee.class, id);
		List<Employee> masterlist = new ArrayList<Employee>();

		List<Employee> reportmanagerlist = assetManagementDAOImpl
				.getactiveEmployeeList(employee);

		masterlist.addAll(reportmanagerlist);
		List<ReportDTO> employees = builder.convertEmplyeeTOReport(masterlist);
		return employees;
	}

	@Override
	public List<ProductDto> getAssetTypeChangeList(Long id) {

		return productBuilder.toDTOList(assetManagementDAOImpl
				.getAssetTypeChangeList(id));
	}

	@Override
	public List<ManufacturerDto> getProductTypeChangeList(Long productId) {
		return manufacturerBuilder.toDTOList(assetManagementDAOImpl
				.getProductTypeChangeList(productId));
	}

	@Override
	public void updateAssetDetails(AssetDto assetDto) {
		Asset assetDetails = assetBuilder.toEditEntity(assetDto);
		assetManagementDAOImpl.update(assetDetails);
	}

	@Override
	public SearchEmpDetailsDTO getUpdateByName(Long employeeId) {
		Employee employee = assetManagementDAOImpl.findBy(Employee.class,
				employeeId);
		return employeeBuilder.createEmployeeDTOForSearch(employee);
	}

	@Override
	public List<EmpDepartment> getEmpDepartments() {
		return dao.get(EmpDepartment.class);
	}

	@Override
	public Map<String, Object> getTransactionalHistory(
			AssetAuditDto assetAuditDtos, Integer startIndex, Integer endIndex,
			String from, String to, String dateSelection) {

		Employee employee = (Employee) securityUtils
				.getLoggedEmployeeDetailsSecurityContextHolder()
				.get("employee");
		Boolean seperateListFalg = false;
		Permission totalList = dao.checkForPermission("Transactional List",
				employee);
		Permission departmentList = dao.checkForPermission(
				"Department Transactional List", employee);

		if (departmentList.getView() && totalList.getView()) {
			seperateListFalg = true;
		}

		Date fromDate = null;
		Date toDate = null;

		if (dateSelection.equalsIgnoreCase("Custom")) {
			try {
				fromDate = DateParser.toDate(from);
				toDate = DateParser.toDate(to);
			} catch (ParseException ex) {
				java.util.logging.Logger.getLogger(
						AssetManagementServiceImpl.class.getName()).log(
						Level.SEVERE, null, ex);
			}
		} else {
			Map<String, Date> dateMap = dao.getCustomDates(dateSelection);
			fromDate = dateMap.get("startDate");
			toDate = dateMap.get("endDate");
		}

		DateRange dateRange = new DateRange(fromDate, toDate);

		Map<String, Object> map = assetManagementDAOImpl
				.getTransactionalHistory(assetAuditDtos, startIndex, endIndex,
						dateRange, seperateListFalg);
		List<AssetAudit> list = (List<AssetAudit>) map.get("list");
		Integer noOfrecords = (Integer) map.get("size");
		List<AssetAuditDto> assetAuditDtoList = assetAuditBuilder
				.ToDTOList(list);
		Map<String, Object> assetAuditMap = new HashMap<String, Object>();
		assetAuditMap.put("list", assetAuditDtoList);
		assetAuditMap.put("size", noOfrecords);

		return assetAuditMap;

	}

	@Override
	public List<EmpDepartmentDTO> getDepartmentNameList() {
		return ticketsCategoryBuilder.getDepartmentList(assetManagementDAOImpl
				.getDepartmentNameList());
	}

	@Override
	public List<AssetDto> getEmployeeAssets(Long employeeId) {
		return assetBuilder.ToDTOList(assetManagementDAOImpl
				.getEmployeeAssets(employeeId));
	}

	// export vendor data
	@SuppressWarnings("unchecked")
	@Override
	public ByteArrayOutputStream exportVendorData(String vendorName)
			throws IOException {
		Map<String, Object> detailsMap = assetManagementDAOImpl
				.getAllVendorDetails(vendorName, null, null);
		List<VendorDetails> list = (List) detailsMap.get("list");
		ByteArrayOutputStream bos = new ByteArrayOutputStream();

		int rowindex = 1;
		Workbook workbook = new HSSFWorkbook();
		Sheet sheet = workbook.createSheet();
		Row row1 = sheet.createRow(0);
		Font font = workbook.createFont();
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		font.setFontHeightInPoints((short) 10);
		CellStyle style = workbook.createCellStyle();

		style.setAlignment(CellStyle.ALIGN_CENTER);
		style.setFont(font);

		Cell cell0 = row1.createCell(0);
		cell0.setCellStyle(style);
		cell0.setCellValue("vendorName");

		Cell cell1 = row1.createCell(1);
		cell1.setCellStyle(style);
		cell1.setCellValue("Address");

		Cell cell2 = row1.createCell(2);
		cell2.setCellStyle(style);
		cell2.setCellValue("City");

		Cell cell3 = row1.createCell(3);
		cell3.setCellStyle(style);
		cell3.setCellValue("Phone Number");

		Cell cell4 = row1.createCell(4);
		cell4.setCellStyle(style);
		cell4.setCellValue("Email ID");

		Cell cell5 = row1.createCell(5);
		cell5.setCellStyle(style);
		cell5.setCellValue("Last Updated by");

		for (VendorDetails dto : list) {

			Row row = sheet.createRow(rowindex++);

			Cell cel0 = row.createCell(0);
			cel0.setCellValue(dto.getVendorName());
			Cell cel1 = row.createCell(1);
			if (dto.getVendorAddress() == null) {
				cel1.setCellValue("N/A");
			} else {
				cel1.setCellValue(dto.getVendorAddress().replaceAll(
						"\\<[^>]*>", ""));
			}

			Cell cel2 = row.createCell(2);
			cel2.setCellValue(dto.getVendorCity());

			Cell cel3 = row.createCell(3);
			cel3.setCellValue(dto.getVendorPhoneNumber());

			Cell cel4 = row.createCell(4);
			cel4.setCellValue(dto.getVendorEmailId());

			Cell cel5 = row.createCell(5);
			if (dto.getUpdatedBy() == null) {
				cel5.setCellValue("N/A");
			} else {
				Employee employee = dao.findBy(Employee.class,
						dto.getUpdatedBy());
				cel5.setCellValue(employee.getFullName());
			}
			sheet.autoSizeColumn(0);
			sheet.autoSizeColumn(1);
			sheet.autoSizeColumn(2);
			sheet.autoSizeColumn(3);
			sheet.autoSizeColumn(4);
			sheet.autoSizeColumn(5);

		}
		workbook.write(bos);
		bos.flush();
		bos.close();
		return bos;
	}

	@Override
	public ByteArrayOutputStream exportProductData(String productName)
			throws IOException {

		Map<String, Object> productList = assetManagementDAOImpl
				.getAllProducts(productName, null, null);
		List<Product> list = (List) productList.get("list");
		logger.warn("at line 566:" + list.size());

		ByteArrayOutputStream bos = new ByteArrayOutputStream();

		int rowindex = 1;
		Workbook workbook = new HSSFWorkbook();
		Sheet sheet = workbook.createSheet();
		Row row1 = sheet.createRow(0);
		Font font = workbook.createFont();
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		font.setFontHeightInPoints((short) 10);
		CellStyle style = workbook.createCellStyle();

		style.setAlignment(CellStyle.ALIGN_CENTER);
		style.setFont(font);

		Cell cell0 = row1.createCell(0);
		cell0.setCellStyle(style);
		cell0.setCellValue("Product Type");

		Cell cell1 = row1.createCell(1);
		cell1.setCellStyle(style);
		cell1.setCellValue("Asset Type");

		Cell cell2 = row1.createCell(2);
		cell2.setCellStyle(style);
		cell2.setCellValue("Last Updated by");

		for (Product dto : list) {

			Row row = sheet.createRow(rowindex++);

			Cell cel0 = row.createCell(0);
			cel0.setCellValue(dto.getProductName());

			Cell cel1 = row.createCell(1);
			cel1.setCellValue(dto.getAssetType().getAssetType());

			Cell cel2 = row.createCell(2);
			if (dto.getUpdatedBy() == null) {
				if (dto.getCreatedBy() == null) {
					cel2.setCellValue("N/A");
				} else {
					Employee employee = dao.findBy(Employee.class,
							dto.getCreatedBy());
					cel2.setCellValue(employee.getFullName());
				}
			} else {
				Employee employee = dao.findBy(Employee.class,
						dto.getUpdatedBy());
				cel2.setCellValue(employee.getFullName());
			}

			sheet.autoSizeColumn(0);
			sheet.autoSizeColumn(1);
			sheet.autoSizeColumn(2);

		}
		workbook.write(bos);
		bos.flush();
		bos.close();
		return bos;
	}

	@Override
	public ByteArrayOutputStream exportManufacturerListData(
			String manufacturerName) throws IOException {
		Map<String, Object> manufacturerMap = assetManagementDAOImpl
				.getAllManufacturerName(manufacturerName, null, null);
		List<Manufacturer> list = (List) manufacturerMap.get("list");
		ByteArrayOutputStream bos = new ByteArrayOutputStream();

		int rowindex = 1;
		Workbook workbook = new HSSFWorkbook();
		Sheet sheet = workbook.createSheet();
		Row row1 = sheet.createRow(0);
		Font font = workbook.createFont();
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		font.setFontHeightInPoints((short) 10);
		CellStyle style = workbook.createCellStyle();

		style.setAlignment(CellStyle.ALIGN_CENTER);
		style.setFont(font);

		Cell cell0 = row1.createCell(0);
		cell0.setCellStyle(style);
		cell0.setCellValue("Manufacturer Name");

		Cell cell1 = row1.createCell(1);
		cell1.setCellStyle(style);
		cell1.setCellValue("Product Type");

		Cell cell2 = row1.createCell(2);
		cell2.setCellStyle(style);
		cell2.setCellValue("Last Updated by");

		for (Manufacturer obj : list) {
			Row row = sheet.createRow(rowindex++);
			Cell cel0 = row.createCell(0);
			cel0.setCellValue(obj.getManufacturerName());

			Cell cel1 = row.createCell(1);
			cel1.setCellValue(obj.getProduct().getProductName());

			Cell cel2 = row.createCell(2);
			if (obj.getUpdatedBy() == null) {
				if (obj.getCreatedBy() == null) {
					cel2.setCellValue("N/A");
				} else {
					Employee employee = dao.findBy(Employee.class,
							obj.getCreatedBy());
					cel2.setCellValue(employee.getFullName());
				}
			} else {
				Employee employee = dao.findBy(Employee.class,
						obj.getUpdatedBy());
				cel2.setCellValue(employee.getFullName());
			}
			sheet.autoSizeColumn(0);
			sheet.autoSizeColumn(1);
			sheet.autoSizeColumn(2);

		}
		workbook.write(bos);
		bos.flush();
		bos.close();
		return bos;
	}

	@Override
	public ByteArrayOutputStream exportProductSpecificationListData(
			String productName) throws IOException {
		Map<String, Object> map = assetManagementDAOImpl
				.getAllProductSpecifications(productName, null, null);
		List<ProductSpecifications> list = (List<ProductSpecifications>) map
				.get("list");
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		int rowindex = 1;
		Workbook workbook = new HSSFWorkbook();
		Sheet sheet = workbook.createSheet();
		Row row1 = sheet.createRow(0);
		Font font = workbook.createFont();
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		font.setFontHeightInPoints((short) 10);
		CellStyle style = workbook.createCellStyle();

		style.setAlignment(CellStyle.ALIGN_CENTER);
		style.setFont(font);

		Cell cell0 = row1.createCell(0);
		cell0.setCellStyle(style);
		cell0.setCellValue("Product Type");

		Cell cell1 = row1.createCell(1);
		cell1.setCellStyle(style);
		cell1.setCellValue("Manufacturer Name");

		Cell cell2 = row1.createCell(2);
		cell2.setCellStyle(style);
		cell2.setCellValue("Product Specification");

		Cell cell3 = row1.createCell(3);
		cell3.setCellStyle(style);
		cell3.setCellValue("Last Updated by");

		for (ProductSpecifications obj : list) {

			Row row = sheet.createRow(rowindex++);

			Cell cel0 = row.createCell(0);
			cel0.setCellValue(obj.getProduct().getProductName());

			Cell cel1 = row.createCell(1);
			cel1.setCellValue(obj.getManufacturer().getManufacturerName());

			Cell cel2 = row.createCell(2);
			cel2.setCellValue(obj.getProductSpecification().replaceAll(
					"\\<[^>]*>", ""));

			Cell cel3 = row.createCell(3);
			if (obj.getUpdatedBy() == null) {
				if (obj.getCreatedBy() == null) {
					cel3.setCellValue("N/A");
				} else {
					Employee employee = dao.findBy(Employee.class,
							obj.getCreatedBy());
					cel3.setCellValue(employee.getFullName());
				}
			} else {
				Employee employee = dao.findBy(Employee.class,
						obj.getUpdatedBy());
				cel3.setCellValue(employee.getFullName());
			}
			sheet.autoSizeColumn(0);
			sheet.autoSizeColumn(1);
			sheet.autoSizeColumn(2);
			sheet.autoSizeColumn(3);

		}
		workbook.write(bos);
		bos.flush();
		bos.close();
		return bos;
	}

	@Override
	public Map<String, Object> getWarrantyAssetsList(Integer startIndex,
			Integer endIndex, String from, String to, String dateSelection) {
		Employee employee = (Employee) securityUtils
				.getLoggedEmployeeDetailsSecurityContextHolder()
				.get("employee");
		Boolean seperateListFalg = false;
		Permission totalList = dao.checkForPermission("Asset Warranty Report",
				employee);
		Permission departmentList = dao.checkForPermission(
				"Department Warranty Asset List", employee);

		if (departmentList.getView() && totalList.getView()) {
			seperateListFalg = true;
		}

		Date fromDate = null;
		Date toDate = null;

		if (dateSelection.equalsIgnoreCase("Custom")) {
			try {
				fromDate = DateParser.toDate(from);
				toDate = DateParser.toDate(to);
			} catch (ParseException ex) {
				java.util.logging.Logger.getLogger(
						AssetManagementServiceImpl.class.getName()).log(
						Level.SEVERE, null, ex);
			}
		} else {
			Map<String, Date> dateMap = dao.getCustomDates(dateSelection);
			fromDate = dateMap.get("startDate");
			toDate = dateMap.get("endDate");
		}

		DateRange dateRange = new DateRange(fromDate, toDate);

		Map<String, Object> map = assetManagementDAOImpl.getWarrantyAssetsList(
				startIndex, endIndex, dateRange, seperateListFalg);
		List<Asset> list = (List<Asset>) map.get("list");
		Integer noOfrecords = (Integer) map.get("size");

		if (list != null) {
			Collections.sort(list, new Comparator<Asset>() {
				@Override
				public int compare(Asset a1, Asset a2) {
					int k = 0;

					if (a1.getWarrantyEndDate()
							.isAfter(a2.getWarrantyEndDate())) {
						k = 1;
					}
					if (a1.getWarrantyEndDate().isBefore(
							a2.getWarrantyEndDate())) {
						k = -1;
					}

					return k;
				}

			});
		}

		List<AssetDto> assetDtoList = assetBuilder.ToDTOList(list);
		Map<String, Object> assetDtoMap = new HashMap<String, Object>();
		assetDtoMap.put("list", assetDtoList);
		assetDtoMap.put("size", noOfrecords);

		return assetDtoMap;
	}

	@Override
	public ByteArrayOutputStream exportAssetWarrantyList(Integer startIndex,
			Integer endIndex, String from, String to, String dateSelection,
			HttpServletResponse response) throws IOException {

		Employee employee = (Employee) securityUtils
				.getLoggedEmployeeDetailsSecurityContextHolder()
				.get("employee");
		Boolean seperateListFalg = false;
		Permission totalList = dao.checkForPermission("Asset Warranty Report",
				employee);
		Permission departmentList = dao.checkForPermission(
				"Department Warranty Asset List", employee);

		if (departmentList.getView() && totalList.getView()) {
			seperateListFalg = true;
		}
		Date fromDate = null;
		Date toDate = null;

		if (dateSelection.equalsIgnoreCase("Custom")) {
			try {
				fromDate = DateParser.toDate(from);
				toDate = DateParser.toDate(to);
			} catch (ParseException ex) {
				java.util.logging.Logger.getLogger(
						AssetManagementServiceImpl.class.getName()).log(
						Level.SEVERE, null, ex);
			}
		} else {
			Map<String, Date> dateMap = dao.getCustomDates(dateSelection);
			fromDate = dateMap.get("startDate");
			toDate = dateMap.get("endDate");
		}

		DateRange dateRange = new DateRange(fromDate, toDate);

		Map<String, Object> map = assetManagementDAOImpl.getWarrantyAssetsList(
				null, null, dateRange, seperateListFalg);
		List<Asset> list = (List<Asset>) map.get("list");

		if (list != null) {
			Collections.sort(list, new Comparator<Asset>() {
				@Override
				public int compare(Asset a1, Asset a2) {
					int k = 0;

					if (a1.getWarrantyEndDate()
							.isAfter(a2.getWarrantyEndDate())) {
						k = 1;
					}
					if (a1.getWarrantyEndDate().isBefore(
							a2.getWarrantyEndDate())) {
						k = -1;
					}

					return k;
				}

			});
		}
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		int rowindex = 1;
		Workbook workbook = new HSSFWorkbook();
		Sheet sheet = workbook.createSheet();
		Row row1 = sheet.createRow(0);
		Font font = workbook.createFont();
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		font.setFontHeightInPoints((short) 10);
		CellStyle style = workbook.createCellStyle();

		style.setAlignment(CellStyle.ALIGN_CENTER);
		style.setFont(font);

		Cell cell0 = row1.createCell(0);
		cell0.setCellStyle(style);
		cell0.setCellValue("Asset Number");

		Cell cell1 = row1.createCell(1);
		cell1.setCellStyle(style);
		cell1.setCellValue("Asset Type");

		Cell cell2 = row1.createCell(2);
		cell2.setCellStyle(style);
		cell2.setCellValue("Asset Ref. Number");

		Cell cell3 = row1.createCell(3);
		cell3.setCellStyle(style);
		cell3.setCellValue("Vendor Name");

		Cell cell4 = row1.createCell(4);
		cell4.setCellStyle(style);
		cell4.setCellValue("Product Name");

		Cell cell5 = row1.createCell(5);
		cell5.setCellStyle(style);
		cell5.setCellValue("Product Specification");

		Cell cell6 = row1.createCell(6);
		cell6.setCellStyle(style);
		cell6.setCellValue("License Number");

		Cell cell7 = row1.createCell(7);
		cell7.setCellStyle(style);
		cell7.setCellValue("Warranty Start Date");

		Cell cell8 = row1.createCell(8);
		cell8.setCellStyle(style);
		cell8.setCellValue("Warranty End Date");

		Cell cell9 = row1.createCell(9);
		cell9.setCellStyle(style);
		cell9.setCellValue("Asset Status");

		for (Asset obj : list) {

			Row row = sheet.createRow(rowindex++);

			Cell cel0 = row.createCell(0);
			cel0.setCellValue(obj.getAssetNumber());

			Cell cel1 = row.createCell(1);
			cel1.setCellValue(obj.getAssetType().getAssetType());

			Cell cel2 = row.createCell(2);
			if (obj.getReferenceNumber() == null
					|| obj.getReferenceNumber().isEmpty()) {
				cel2.setCellValue("N/A");
			} else {
				cel2.setCellValue(obj.getReferenceNumber());
			}

			Cell cel3 = row.createCell(3);
			cel3.setCellValue(obj.getVendorDetails().getVendorName());

			Cell cel4 = row.createCell(4);
			cel4.setCellValue(obj.getProductSpecifications().getProduct()
					.getProductName());

			Cell cel5 = row.createCell(5);
			cel5.setCellValue(obj.getProductSpecifications()
					.getProductSpecification().replaceAll("\\<[^>]*>", ""));

			Cell cel6 = row.createCell(6);
			if (obj.getOtherAssetNumber() == null
					|| obj.getOtherAssetNumber().isEmpty()) {
				cel6.setCellValue("N/A");
			} else {
				cel6.setCellValue(obj.getOtherAssetNumber());
			}

			Cell cel7 = row.createCell(7);
			cel7.setCellValue(obj.getWarrantyStartDate().toString());

			Cell cel8 = row.createCell(8);
			cel8.setCellValue(obj.getWarrantyEndDate().toString());

			Cell cel9 = row.createCell(9);
			cel9.setCellValue(obj.getStatus());

			sheet.autoSizeColumn(0);
			sheet.autoSizeColumn(1);
			sheet.autoSizeColumn(2);
			sheet.autoSizeColumn(3);
			sheet.autoSizeColumn(4);
			sheet.autoSizeColumn(5);
			sheet.autoSizeColumn(6);
			sheet.autoSizeColumn(7);
			sheet.autoSizeColumn(8);
			sheet.autoSizeColumn(9);
		}

		workbook.write(bos);
		bos.flush();
		bos.close();

		return bos;

	}

	@SuppressWarnings("unchecked")
	@Override
	public ByteArrayOutputStream exportAssetList(String assetss,
			Long assetTypeId, Long productId, String status,
			String multiSearch, Boolean searchByEmpName,String dateSelection,String fromDate,String toDate,
			HttpServletResponse response) throws IOException {
		Date from =null;
		Date to=null;
		if(dateSelection.equalsIgnoreCase("Custom"))
		{
			try {
				 from=DateParser.toDate(fromDate);
				 to=DateParser.toDate(toDate);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else
		{
			if(!dateSelection.equalsIgnoreCase(""))
			{
		Map<String, Date>dateMap=assetManagementDAOImpl.getCustomDates(dateSelection);
		from= dateMap.get("startDate");
		to= dateMap.get("endDate");
		
			}
		}
	
		Employee employee = (Employee) securityUtils
				.getLoggedEmployeeDetailsSecurityContextHolder()
				.get("employee");
		Permission totalList = dao.checkForPermission("Asset List", employee);
		Permission departmentList = dao.checkForPermission(
				"Department Asset List", employee);
		Boolean seperateListFalg = false;

		if (departmentList.getView() && totalList.getView()) {
			seperateListFalg = true;
		}

		Map<String, Object> getAllAssetsMap = assetManagementDAOImpl
				.getAllAssetsForExport(assetss, assetTypeId, productId, status,
						multiSearch,from,to, searchByEmpName, employee,
						seperateListFalg);
		List<Asset> assetList = (List<Asset>) getAllAssetsMap.get("list");

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		int rowindex = 1;
		Workbook workbook = new HSSFWorkbook();
		Sheet sheet = workbook.createSheet();
		Row row1 = sheet.createRow(0);
		Font font = workbook.createFont();
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		font.setFontHeightInPoints((short) 10);
		CellStyle style = workbook.createCellStyle();

		style.setAlignment(CellStyle.ALIGN_CENTER);
		style.setFont(font);

		Cell cell0 = row1.createCell(0);
		cell0.setCellStyle(style);
		cell0.setCellValue("Asset Number");

		Cell cell1 = row1.createCell(1);
		cell1.setCellStyle(style);
		cell1.setCellValue("Asset Type");

		Cell cell2 = row1.createCell(2);
		cell2.setCellStyle(style);
		cell2.setCellValue("Product Name");

		Cell cell3 = row1.createCell(3);
		cell3.setCellStyle(style);
		cell3.setCellValue("Product Specification");

		Cell cell4 = row1.createCell(4);
		cell4.setCellStyle(style);
		cell4.setCellValue("License Number");

		Cell cell5 = row1.createCell(5);
		cell5.setCellStyle(style);
		cell5.setCellValue("Location");

		Cell cell6 = row1.createCell(6);
		cell6.setCellStyle(style);
		cell6.setCellValue("Asset Ref. Number");

		Cell cell7 = row1.createCell(7);
		cell7.setCellStyle(style);
		cell7.setCellValue("Asset Status");

		Cell cell8 = row1.createCell(8);
		cell8.setCellStyle(style);
		cell8.setCellValue("Employee Name");
		
		Cell cell9 = row1.createCell(9);
		cell9.setCellStyle(style);
		cell9.setCellValue("Date of Purchase");
		
		Cell cell10 = row1.createCell(10);
		cell10.setCellStyle(style);
		cell10.setCellValue("PO Number");

		for (Asset obj : assetList) {

			Row row = sheet.createRow(rowindex++);

			Cell cel0 = row.createCell(0);
			cel0.setCellValue(obj.getAssetNumber());

			Cell cel1 = row.createCell(1);
			cel1.setCellValue(obj.getAssetType().getAssetType());

			Cell cel2 = row.createCell(2);
			cel2.setCellValue(obj.getProductSpecifications().getProduct()
					.getProductName());

			Cell cel3 = row.createCell(3);
			cel3.setCellValue(obj.getProductSpecifications()
					.getProductSpecification().replaceAll("\\<[^>]*>", ""));

			Cell cel4 = row.createCell(4);
			if (obj.getOtherAssetNumber() == null
					|| obj.getOtherAssetNumber().isEmpty()) {
				cel4.setCellValue("N/A");
			} else {
				cel4.setCellValue(obj.getOtherAssetNumber());
			}

			Cell cel5 = row.createCell(5);
			if (obj.getLocation() == null || obj.getLocation().isEmpty()) {
				cel5.setCellValue("N/A");
			} else {
				cel5.setCellValue(obj.getLocation());
			}

			Cell cel6 = row.createCell(6);
			if (obj.getReferenceNumber() == null
					|| obj.getReferenceNumber().isEmpty()) {
				cel6.setCellValue("N/A");
			} else {
				cel6.setCellValue(obj.getReferenceNumber());
			}

			Cell cel7 = row.createCell(7);
			if (obj.getStatus() == null || obj.getStatus().isEmpty()) {
				cel7.setCellValue("N/A");
			} else {
				cel7.setCellValue(obj.getStatus());
			}

			Cell cel8 = row.createCell(8);
			if (obj.getEmployee() != null) {
				cel8.setCellValue(obj.getEmployee().getEmployeeFullName());
			} else {
				cel8.setCellValue("N/A");
			}
			
			Cell cel9 = row.createCell(9);
			if(obj.getPurchasedDate()!=null)
			{
				cel9.setCellValue(obj.getPurchasedDate().toString("dd/MM/yyyy"));
			}
			
			Cell cel10 = row.createCell(10);
			if(obj.getPoNumber()!=null)
			{
				cel10.setCellValue(obj.getPoNumber());
			}

			sheet.autoSizeColumn(0);
			sheet.autoSizeColumn(1);
			sheet.autoSizeColumn(2);
			sheet.autoSizeColumn(3);
			sheet.autoSizeColumn(4);
			sheet.autoSizeColumn(5);
			sheet.autoSizeColumn(6);
			sheet.autoSizeColumn(7);
			sheet.autoSizeColumn(8);
			sheet.autoSizeColumn(9);
			sheet.autoSizeColumn(10);
		}

		workbook.write(bos);
		bos.flush();
		bos.close();

		return bos;

	}

}
