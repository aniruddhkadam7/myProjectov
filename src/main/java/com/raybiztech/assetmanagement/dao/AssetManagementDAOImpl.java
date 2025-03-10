/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.raybiztech.assetmanagement.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ibm.icu.util.Calendar;
import com.itextpdf.text.pdf.PdfStructTreeController.returnType;
import com.raybiztech.appraisals.business.EmpDepartment;
import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.dao.DAO;
import com.raybiztech.appraisals.dao.DAOImpl;
import com.raybiztech.appraisals.security.utils.SecurityUtils;
import com.raybiztech.assetmanagement.business.Asset;
import com.raybiztech.assetmanagement.business.AssetAudit;
import com.raybiztech.assetmanagement.business.AssetType;
import com.raybiztech.assetmanagement.business.Manufacturer;
import com.raybiztech.assetmanagement.business.Product;
import com.raybiztech.assetmanagement.business.ProductSpecifications;
import com.raybiztech.assetmanagement.business.VendorDetails;
import com.raybiztech.assetmanagement.dto.AssetAuditDto;
import com.raybiztech.assetmanagement.dto.AssetDto;
import com.raybiztech.assetmanagement.dto.SearchQueryParamsInAssetList;
import com.raybiztech.date.Date;
import com.raybiztech.date.DateRange;
import com.raybiztech.date.DayOfMonth;
import com.raybiztech.date.MonthOfYear;
import com.raybiztech.date.YearOfEra;
import com.raybiztech.projectmanagement.business.Project;
import com.raybiztech.projectmanagement.business.ProjectStatus;
import com.raybiztech.roleFeature.dao.UserDAO;
import com.raybiztech.rolefeature.business.Permission;

/**
 *
 * @author anil
 */
@Repository("assetManagementDAOImpl")
public class AssetManagementDAOImpl extends DAOImpl implements AssetManagementDAO {
	Logger logger = Logger.getLogger(AssetManagementDAOImpl.class);

	@Autowired
	SecurityUtils securityUtils;
	@Autowired
	DAO dao;
	@Autowired
	UserDAO userDAOImpl;

	@Override
	public void addProductSpecifications(ProductSpecifications specifications) {
		getSessionFactory().getCurrentSession().save(specifications);
	}

	@Override
	public List<ProductSpecifications> typeChangeSpecifications(Long productId, Long manufacturerId) {

		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ProductSpecifications.class)
				.createAlias("product", "product");
		criteria.createAlias("manufacturer", "manufacturer");
		criteria.add(Restrictions.eq("product.productId", productId));
		criteria.add(Restrictions.eq("manufacturer.manufacturerId", manufacturerId));
		return criteria.list();
	}

	@Override
	public ProductSpecifications specificationId(Long productId, Long manufacturerId, String pSpecification) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ProductSpecifications.class);
		criteria.createAlias("product", "product");
		criteria.createAlias("manufacturer", "manufacturer");
		criteria.add(Restrictions.eq("product.productId", productId));
		criteria.add(Restrictions.eq("manufacturer.manufacturerId", manufacturerId));
		criteria.add(Restrictions.eq("productSpecification", pSpecification));
		return (ProductSpecifications) criteria.uniqueResult();

	}

	@Override
	public Map<String, Object> getAllProductSpecifications(String multipleSearch, Integer startIndex,
			Integer endIndex) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ProductSpecifications.class);
		Long loggedInEmpId = securityUtils.getLoggedEmployeeIdforSecurityContextHolder();
		Employee employee = dao.findBy(Employee.class, loggedInEmpId);
		// Long roleId = employee.getEmpRole().getRoleId();

		Boolean totalListFalg = false;
		Boolean seperateListFalg = false;

		// Criteria permissionCriteria =
		// sessionFactory.getCurrentSession().createCriteria(Permission.class);
		// permissionCriteria.add(Restrictions.eq("role",
		// employee.getEmpRole()));
		// permissionCriteria.createAlias("feature", "feature");
		// permissionCriteria.add(Restrictions.eq("feature.name",
		// "Product Specification List"));
		//
		// Criteria permissionCriteria2 =
		// sessionFactory.getCurrentSession().createCriteria(Permission.class);
		// permissionCriteria2.add(Restrictions.eq("role",
		// employee.getEmpRole()));
		// permissionCriteria2.createAlias("feature", "feature");
		// permissionCriteria2.add(Restrictions.eq("feature.name",
		// "Department Product Specification List"));

		Permission totalList = dao.checkForPermission("Product Specification List", employee);
		Permission departmentList = dao.checkForPermission("Department Product Specification List", employee);

		if (totalList.getView() && !departmentList.getView())
			totalListFalg = true;
		else if (departmentList.getView() && totalList.getView())
			seperateListFalg = true;

		// List<Permission> permissionsList =
		// userDAOImpl.getAllFeatures_underRole(roleId);
		//
		// for (Permission permission : permissionsList) {
		// if
		// (permission.getFeature().getName().equalsIgnoreCase("Product Specification
		// List"))
		// {
		// if (permission.getView()) {
		// totalProductListFalg = true;
		// }
		// }
		//
		// if
		// (permission.getFeature().getName().equalsIgnoreCase("Role Based Product
		// Specification List"))
		// {
		// if (permission.getView()) {
		// seperateProductListFalg = true;
		// }
		// }
		// }
		if (totalListFalg) {
			criteria.list();
		} else if (seperateListFalg) {
			criteria.createAlias("empDepartment", "department");
			criteria.add(Restrictions.eq("department.departmentName", employee.getDepartmentName()));
		}

		criteria.createAlias("product", "product");
		criteria.createAlias("manufacturer", "manufacturer");
		Criterion productName = Restrictions.ilike("product.productName", multipleSearch, MatchMode.ANYWHERE);
		Criterion manufacturerName = Restrictions.ilike("manufacturer.manufacturerName", multipleSearch,
				MatchMode.ANYWHERE);
		Criterion productSpecification = Restrictions.ilike("productSpecification", multipleSearch, MatchMode.ANYWHERE);
		criteria.add(Restrictions.or(Restrictions.or(productName, manufacturerName), productSpecification));
		criteria.addOrder(Order.desc("specificationId"));
		/*
		 * Integer noOfRecords = criteria.list().size(); if (startIndex != null &&
		 * endIndex != null) { criteria.setFirstResult(startIndex);
		 * criteria.setMaxResults(endIndex - startIndex); } List<ProductSpecifications>
		 * specificationses = criteria.list();
		 */

		Map<String, Object> criteriaMap = getPaginationList(criteria, startIndex, endIndex);

		Map<String, Object> allSpecificationsMap = new HashMap<String, Object>();
		allSpecificationsMap.put("list", criteriaMap.get("list"));
		allSpecificationsMap.put("size", Integer.parseInt((criteriaMap.get("listSize").toString())));

		return allSpecificationsMap;
		/*
		 * Map<String,Object> allSpecificationsMap =getPaginationList(criteria,
		 * startIndex, endIndex); Long size =(Long)
		 * allSpecificationsMap.remove("listSize"); allSpecificationsMap.put("size",
		 * Integer.parseInt(size.toString())); return allSpecificationsMap;
		 */
	}

	@Override
	public Map<String, Object> getAllVendorDetails(String multipleSearch, Integer startIndex, Integer endIndex) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(VendorDetails.class);
		Map<String, Object> map = new HashMap<String, Object>();
		Long loggedInEmpId = securityUtils.getLoggedEmployeeIdforSecurityContextHolder();
		Employee employee = dao.findBy(Employee.class, loggedInEmpId);
		// Long roleId = employee.getEmpRole().getRoleId();
		Boolean totalVendorListFalg = false;
		Boolean seperateVendorListFalg = false;

		Permission totalList = dao.checkForPermission("Vendor List", employee);
		Permission departmentList = dao.checkForPermission("Department Vendor List", employee);

		if (totalList.getView() && !departmentList.getView())
			totalVendorListFalg = true;
		else if (departmentList.getView() && totalList.getView())
			seperateVendorListFalg = true;

		if (totalVendorListFalg) {
			criteria.list();
		} else if (seperateVendorListFalg) {
			criteria.createAlias("empDepartment", "department");
			criteria.add(Restrictions.eq("department.departmentName", employee.getDepartmentName()));
		}

		Criterion vendorName = Restrictions.ilike("vendorName", multipleSearch, MatchMode.ANYWHERE);
		Criterion vendorAddress = Restrictions.ilike("vendorAddress", multipleSearch, MatchMode.ANYWHERE);
		Criterion vendorCity = Restrictions.ilike("vendorCity", multipleSearch, MatchMode.ANYWHERE);
		Criterion vendorPhoneNumber = Restrictions.ilike("vendorPhoneNumber", multipleSearch, MatchMode.ANYWHERE);
		Criterion vendorEmailId = Restrictions.ilike("vendorEmailId", multipleSearch, MatchMode.ANYWHERE);
		Criterion criterion = Restrictions.or(Restrictions.or(vendorName, vendorAddress),
				Restrictions.or(vendorPhoneNumber, vendorCity));
		criteria.add(Restrictions.or(criterion, vendorEmailId));
		criteria.addOrder(Order.desc("vendorId"));
		/*
		 * Integer noOfRecords = criteria.list().size();
		 * 
		 * if (startIndex != null && endIndex != null) {
		 * criteria.setFirstResult(startIndex); criteria.setMaxResults(endIndex -
		 * startIndex); }
		 * 
		 * List<VendorDetails> detailses = criteria.list();
		 */
		Map<String, Object> criteriaMap = getPaginationList(criteria, startIndex, endIndex);
		map.put("list", criteriaMap.get("list"));
		map.put("size", Integer.parseInt((criteriaMap.get("listSize").toString())));

		return map;

		/*
		 * Map<String,Object> allVendorsMap =getPaginationList(criteria, startIndex,
		 * endIndex); Long size =(Long) allVendorsMap.remove("listSize");
		 * allVendorsMap.put("size", Integer.parseInt(size.toString())); return
		 * allVendorsMap;
		 */
	}

	@Override
	public Map<String, Object> getAllManufacturerName(String multipleSearch, Integer startIndex, Integer endIndex) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Manufacturer.class);
		Long loggedInEmpId = securityUtils.getLoggedEmployeeIdforSecurityContextHolder();
		Employee employee = dao.findBy(Employee.class, loggedInEmpId);
		// Long roleId = employee.getEmpRole().getRoleId();
		// if (!(employee.getRole().equalsIgnoreCase("Admin") ||
		// employee.getRole().equalsIgnoreCase("Finance"))) {
		// criteria.createAlias("roleId", "role");
		// criteria.add(Restrictions.eq("role.roleId", roleId));
		// }

		Boolean totalListFalg = false;
		Boolean seperateListFalg = false;

		Permission totalList = dao.checkForPermission("Manufacturer List", employee);
		Permission departmentList = dao.checkForPermission("Department Manufacturer List", employee);

		if (totalList.getView() && !departmentList.getView())
			totalListFalg = true;
		else if (departmentList.getView() && totalList.getView())
			seperateListFalg = true;

		if (totalListFalg) {
			criteria.list();
		} else if (seperateListFalg) {
			criteria.createAlias("empDepartment", "department");
			criteria.add(Restrictions.eq("department.departmentName", employee.getDepartmentName()));
		}

		criteria.createAlias("product", "product");
		Criterion manufacturerName = Restrictions.ilike("manufacturerName", multipleSearch, MatchMode.ANYWHERE);
		Criterion productName = Restrictions.ilike("product.productName", multipleSearch, MatchMode.ANYWHERE);
		criteria.add(Restrictions.or(manufacturerName, productName));
		criteria.addOrder(Order.desc("manufacturerId"));
		/*
		 * Integer noOfRecords = criteria.list().size(); if (startIndex != null &&
		 * endIndex != null) { criteria.setFirstResult(startIndex);
		 * criteria.setMaxResults(endIndex - startIndex); } List<Manufacturer> list =
		 * criteria.list();
		 */
		Map<String, Object> criteriaMap = getPaginationList(criteria, startIndex, endIndex);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", criteriaMap.get("list"));
		map.put("size", Integer.parseInt((criteriaMap.get("listSize").toString())));
		return map;

		/*
		 * Map<String,Object> allManufactureNames =getPaginationList(criteria,
		 * startIndex, endIndex); Long size =(Long)
		 * allManufactureNames.remove("listSize"); allManufactureNames.put("size",
		 * Integer.parseInt(size.toString())); return allManufactureNames;
		 */
	}

	@Override
	public Map<String, Object> getAllProducts(String multipleSearch, Integer startIndex, Integer endIndex) {
		Long loggedInEmpId = securityUtils.getLoggedEmployeeIdforSecurityContextHolder();
		Employee employee = dao.findBy(Employee.class, loggedInEmpId);
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Product.class);
		// Long roleId = employee.getEmpRole().getRoleId();

		Boolean totalListFalg = false;
		Boolean seperateListFalg = false;

		Permission totalList = dao.checkForPermission("Product Type List", employee);
		Permission departmentList = dao.checkForPermission("Department Product Type List", employee);

		if (totalList.getView() && !departmentList.getView())
			totalListFalg = true;
		else if (departmentList.getView() && totalList.getView())
			seperateListFalg = true;

		if (totalListFalg) {
			criteria.list();
		} else if (seperateListFalg) {
			criteria.createAlias("empDepartment", "department");
			criteria.add(Restrictions.eq("department.departmentName", employee.getDepartmentName()));
		}
		criteria.createAlias("assetType", "assetType");
		Criterion assetType = Restrictions.ilike("assetType.assetType", multipleSearch, MatchMode.ANYWHERE);
		Criterion productName = Restrictions.ilike("productName", multipleSearch, MatchMode.ANYWHERE);
		criteria.add(Restrictions.or(assetType, productName));
		criteria.addOrder(Order.desc("productId"));
		criteria.setProjection(Projections.rowCount());
		criteria.uniqueResult();
		// Integer noOfRecords = criteria.list().size();

		/*
		 * if (startIndex != null && endIndex != null) {
		 * criteria.setFirstResult(startIndex); criteria.setMaxResults(endIndex -
		 * startIndex); } List<Product> list = criteria.list();
		 */

		Map<String, Object> criteriaMap = getPaginationList(criteria, startIndex, endIndex);

		Map<String, Object> map = new HashMap<String, Object>();
		/*
		 * Long noOfRecords = (Long) criteria.uniqueResult(); Integer norec =
		 * Integer.parseInt(noOfRecords.toString());
		 */
		map.put("list", criteriaMap.get("list"));
		map.put("size", Integer.parseInt((criteriaMap.get("listSize").toString())));
		return map;

		/*
		 * Map<String,Object> allProductsMap = getPaginationList(criteria, startIndex,
		 * endIndex); Long size =(Long) allProductsMap.remove("listSize");
		 * allProductsMap.put("size", Integer.parseInt(size.toString())); return
		 * allProductsMap;
		 */
	}

	@Override
	public Map<String, Object> getAllLookUps() {
		Long loggedInEmpId = securityUtils.getLoggedEmployeeIdforSecurityContextHolder();
		Employee employee = dao.findBy(Employee.class, loggedInEmpId);
		Criteria assetTypeCriteria = sessionFactory.getCurrentSession().createCriteria(AssetType.class);
		Criteria productCriteria = sessionFactory.getCurrentSession().createCriteria(Product.class);
		Criteria manufacturerCriteria = sessionFactory.getCurrentSession().createCriteria(Manufacturer.class);
		Criteria vendorCriteria = sessionFactory.getCurrentSession().createCriteria(VendorDetails.class);

		// Long roleId = employee.getEmpRole().getRoleId();
		Boolean totalListFalg = false;
		Boolean seperateListFalg = false;
        System.out.println("emp:" + employee);
		Permission totalList = dao.checkForPermission("Asset List", employee);
		Permission departmentList = dao.checkForPermission("Department Asset List", employee);
		if(totalList.getView()){
			System.out.println("in totallist if");
			totalListFalg = true;
		}

		if (totalList.getView() && !departmentList.getView())
			totalListFalg = true;
		else if (departmentList.getView() && totalList.getView())
			seperateListFalg = true;

		Map<String, Object> map = new HashMap<String, Object>();
		if (totalListFalg) {
			assetTypeCriteria.list();
			productCriteria.list();
			manufacturerCriteria.list();
			vendorCriteria.list();
		} else if (seperateListFalg) {
			//assetTypeCriteria.createAlias("empDepartment", "department");
			//assetTypeCriteria.add(Restrictions.eq("department.departmentName", employee.getDepartmentName()));
			productCriteria.createAlias("empDepartment", "department");
			productCriteria.add(Restrictions.eq("department.departmentName", employee.getDepartmentName()));
			manufacturerCriteria.createAlias("empDepartment", "department");
			manufacturerCriteria.add(Restrictions.eq("department.departmentName", employee.getDepartmentName()));
			vendorCriteria.createAlias("empDepartment", "department");
			vendorCriteria.add(Restrictions.eq("department.departmentName", employee.getDepartmentName()));
		}
		map.put("assetTypeList", assetTypeCriteria.list());
		map.put("productList", productCriteria.list());
		map.put("manufacturerList", manufacturerCriteria.list());
		map.put("vendorList", vendorCriteria.list());
		return map;
	}

	@Override
	public List<AssetAudit> getAssetHistory(Long assetId, String searchAssetReference) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(AssetAudit.class);
		criteria.createAlias("asset", "asset");
		// if(assetId != null)
		criteria.add(Restrictions.eq("asset.id", assetId));
		if (!searchAssetReference.isEmpty())
			criteria.add(Restrictions.ilike("referenceNumber", searchAssetReference, MatchMode.ANYWHERE));
		criteria.addOrder(Order.desc("id"));

		return criteria.list();
	}

	@Override
	public List<Employee> getactiveEmployeeList(Employee employee) {
		List<Employee> employees = new ArrayList<Employee>();
		if (!employee.getRole().equalsIgnoreCase("Employee")) {
			Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(Employee.class);
			criteria.add(Restrictions.eq("statusName", "Active"));
			employees = criteria.list();
		}

		return employees;
	}

	@Override
	public List<Product> getAssetTypeChangeList(Long id) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Product.class);
		criteria.createAlias("assetType", "assetType");
		criteria.add(Restrictions.eq("assetType.id", id));
		return criteria.list();
	}

	@Override
	public List<Manufacturer> getProductTypeChangeList(Long productId) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Manufacturer.class);
		criteria.createAlias("product", "product");
		criteria.add(Restrictions.eq("product.productId", productId));
		return criteria.list();
	}

	/*
	 * public Map<String, Object> getAllAssets( SearchQueryParamsInAssetList
	 * paramsInAssetList, Employee employee, Boolean seperateListFalg) { <<<<<<<
	 * HEAD long var1 = System.currentTimeMillis();
	 * System.out.println("Before method:" + var1); Criteria criteria1 =
	 * getAllAssetsListSize(paramsInAssetList, employee, seperateListFalg); long
	 * Var2 = System.currentTimeMillis(); System.out.println("aftermethod:" + Var2);
	 * System.out.println("total time for for this method:" + (Var2 - var1)); //
	 * Integer noOfRecords = criteria1.list().size();
	 * //System.out.println("start index" + paramsInAssetList.getStartIndex());
	 * //System.out.println("end index:" + paramsInAssetList.getEndIndex());
	 * 
	 * criteria1.setFirstResult(paramsInAssetList.getStartIndex());
	 * criteria1.setMaxResults(paramsInAssetList.getEndIndex() ======= Criteria
	 * criteria = sessionFactory.getCurrentSession().createCriteria( Asset.class);
	 * String multipleSearch = paramsInAssetList.getMultipleSearch(); if
	 * (seperateListFalg) { criteria.createAlias("empDepartment", "department");
	 * criteria.add(Restrictions.eq("department.departmentName",
	 * employee.getDepartmentName())); }
	 * criteria.createAlias("productSpecifications", "productSpecifications");
	 * criteria.createAlias("productSpecifications.product", "product");
	 * criteria.createAlias("assetType", "assetType"); if
	 * (paramsInAssetList.getAssetTypeId() != null &&
	 * paramsInAssetList.getProductId() == null) {
	 * criteria.add(Restrictions.eq("assetType.id",
	 * paramsInAssetList.getAssetTypeId())); } else if
	 * (paramsInAssetList.getAssetTypeId() != null &&
	 * paramsInAssetList.getProductId() != null) {
	 * criteria.add(Restrictions.eq("product.productId",
	 * paramsInAssetList.getProductId())); } if (paramsInAssetList.getStatus() !=
	 * "") { criteria.add(Restrictions.eq("status", paramsInAssetList.getStatus()));
	 * } if (paramsInAssetList.isSearchByEmpName() == false) {
	 * 
	 * Criterion assetNumber = Restrictions.ilike("assetNumber", multipleSearch,
	 * MatchMode.ANYWHERE);
	 * 
	 * Criterion location = Restrictions.ilike("location", multipleSearch,
	 * MatchMode.ANYWHERE);
	 * 
	 * Criterion productSpecifications = Restrictions.ilike(
	 * "productSpecifications.productSpecification", multipleSearch,
	 * MatchMode.ANYWHERE);
	 * 
	 * Criterion licenseNumber = Restrictions.ilike("otherAssetNumber",
	 * multipleSearch, MatchMode.ANYWHERE);
	 * 
	 * Criterion assetReferenceNumber = Restrictions.ilike( "referenceNumber",
	 * multipleSearch, MatchMode.ANYWHERE); Criterion criterion1 =
	 * Restrictions.or(assetNumber, location); Criterion criterion2 =
	 * Restrictions.or(assetReferenceNumber, criterion1); Criterion criterion =
	 * Restrictions.or(criterion2, Restrictions.or(licenseNumber,
	 * productSpecifications));
	 * 
	 * criteria.add(criterion); } else { criteria.createAlias("employee",
	 * "employee"); criteria.add(Restrictions.ilike("employee.employeeFullName",
	 * multipleSearch, MatchMode.ANYWHERE));
	 * 
	 * } if (paramsInAssetList.getSelectionStatus().equalsIgnoreCase(
	 * "not_assigned")) { criteria.add(Restrictions.ilike("allocatedStatus",
	 * "Unassigned")); } else if
	 * (paramsInAssetList.getSelectionStatus().equalsIgnoreCase( "Assigned")) {
	 * criteria.add(Restrictions.ilike("allocatedStatus", "Assigned")); }
	 * 
	 * criteria.addOrder(Order.desc("id")); Integer noOfRecords =
	 * criteria.list().size();
	 * criteria.setFirstResult(paramsInAssetList.getStartIndex());
	 * criteria.setMaxResults(paramsInAssetList.getEndIndex() >>>>>>> branch '5.6.4'
	 * of ssh://git@gitlab.raybiztech.com/root/OneViewHRM.git -
	 * paramsInAssetList.getStartIndex()); List<Product> list = criteria.list();
	 * Map<String, Object> map = new HashMap<String, Object>(); map.put("list",
	 * list); map.put("size", noOfRecords); return map;
	 * 
	 * Map<String,Object> allAssertsMap = getPaginationList (criteria1,
	 * paramsInAssetList.getStartIndex(), paramsInAssetList.getEndIndex()); Long
	 * size =(Long) allAssertsMap.remove("listSize"); allAssertsMap.put("size",
	 * Integer.parseInt(size.toString())); return allAssertsMap;
	 * 
	 * }
	 */

	@Override
	public Map<String, Object> getAllAssets(SearchQueryParamsInAssetList paramsInAssetList, Employee employee,
			Boolean totalListFalg,Date fromDate,Date toDate) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Asset.class);
		String multipleSearch = paramsInAssetList.getMultipleSearch();
		/*if (seperateListFalg) {
			criteria.createAlias("empDepartment", "department");
			criteria.add(Restrictions.eq("department.departmentName", employee.getDepartmentName()));
		}*/
		criteria.createAlias("productSpecifications", "productSpecifications");
		criteria.createAlias("productSpecifications.product", "product");
		criteria.createAlias("assetType", "assetType");
		if (paramsInAssetList.getAssetTypeId() != null && paramsInAssetList.getProductId() == null) {
			criteria.add(Restrictions.eq("assetType.id", paramsInAssetList.getAssetTypeId()));
		} else if (paramsInAssetList.getAssetTypeId() != null && paramsInAssetList.getProductId() != null) {
			criteria.add(Restrictions.eq("product.productId", paramsInAssetList.getProductId()));
		}
		if (paramsInAssetList.getStatus() != "") {
			criteria.add(Restrictions.eq("status", paramsInAssetList.getStatus()));
		}
		if (paramsInAssetList.isSearchByEmpName() == false) {

			Criterion assetNumber = Restrictions.ilike("assetNumber", multipleSearch, MatchMode.ANYWHERE);

			Criterion location = Restrictions.ilike("location", multipleSearch, MatchMode.ANYWHERE);

			Criterion productSpecifications = Restrictions.ilike("productSpecifications.productSpecification",
					multipleSearch, MatchMode.ANYWHERE);

			Criterion licenseNumber = Restrictions.ilike("otherAssetNumber", multipleSearch, MatchMode.ANYWHERE);

			Criterion assetReferenceNumber = Restrictions.ilike("referenceNumber", multipleSearch, MatchMode.ANYWHERE);
			Criterion criterion1 = Restrictions.or(assetNumber, location);
			Criterion criterion2 = Restrictions.or(assetReferenceNumber, criterion1);
			Criterion criterion = Restrictions.or(criterion2, Restrictions.or(licenseNumber, productSpecifications));

			criteria.add(criterion);
		} else {
			criteria.createAlias("employee", "employee");
			criteria.add(Restrictions.ilike("employee.employeeFullName", multipleSearch, MatchMode.ANYWHERE));

		}
		if (paramsInAssetList.getSelectionStatus().equalsIgnoreCase("not_assigned")) {
			criteria.add(Restrictions.ilike("allocatedStatus", "Unassigned"));
		} else if (paramsInAssetList.getSelectionStatus().equalsIgnoreCase("Assigned")) {
			criteria.add(Restrictions.ilike("allocatedStatus", "Assigned"));
		}
		
		if(fromDate!=null&&toDate!=null)
		{
			criteria.add(Restrictions.between("purchasedDate", fromDate, toDate));
		}
		criteria.addOrder(Order.desc("id"));
		/*
		 * Integer noOfRecords = criteria.list().size();
		 * criteria.setFirstResult(paramsInAssetList.getStartIndex());
		 * criteria.setMaxResults(paramsInAssetList.getEndIndex() -
		 * paramsInAssetList.getStartIndex()); List<Product> list = criteria.list();
		 */

		Map<String, Object> criteriaMap = getPaginationList(criteria, paramsInAssetList.getStartIndex(),
				paramsInAssetList.getEndIndex());

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", criteriaMap.get("list"));
		map.put("size", Integer.parseInt((criteriaMap.get("listSize").toString())));
		return map;

	}

	@Override
	public Map<String, Object> getTransactionalHistory(AssetAuditDto assetAuditDtos, Integer startIndex,
			Integer endIndex, DateRange dateRange, Boolean seperateListFalg) {

		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(AssetAudit.class);

		Employee employee = (Employee) securityUtils.getLoggedEmployeeDetailsSecurityContextHolder().get("employee");
		if (seperateListFalg) {
			criteria.createAlias("empDepartment", "department");
			criteria.add(Restrictions.eq("department.departmentName", employee.getDepartmentName()));
		}

		criteria.createAlias("asset", "asset");
		criteria.createAlias("updatedBy", "updatedBy");
		if (assetAuditDtos.getAssetId() != null) {
			criteria.createAlias("asset.assetType", "assetType");
			criteria.add(Restrictions.eq("assetType.id", assetAuditDtos.getAssetId()));
		}
		if (assetAuditDtos.getProductId() != null) {
			criteria.createAlias("asset.productSpecifications", "productSpecifications");
			criteria.createAlias("productSpecifications.product", "product");
			criteria.add(Restrictions.eq("product.productId", assetAuditDtos.getProductId()));
		}
		if (assetAuditDtos.getStatus() != "") {
			criteria.add(Restrictions.eq("status", assetAuditDtos.getStatus()));
		}
		String multiSearch = assetAuditDtos.getMultipleSearch();

		if (multiSearch != null && !assetAuditDtos.isSearchByEmpName()) {

			Criterion assetNum = Restrictions.ilike("asset.assetNumber", assetAuditDtos.getMultipleSearch(),
					MatchMode.ANYWHERE);

			Criterion assetRefNum = Restrictions.ilike("referenceNumber", multiSearch, MatchMode.ANYWHERE);

			Criterion location = Restrictions.ilike("location", multiSearch, MatchMode.ANYWHERE);

			Criterion updatedBy = Restrictions.ilike("updatedBy.employeeFullName", multiSearch, MatchMode.ANYWHERE);
			Criterion vendorCriteria = Restrictions.ilike("vendorName", multiSearch, MatchMode.ANYWHERE);

			Criterion criterion = Restrictions.or(Restrictions.or(assetNum, updatedBy),
					Restrictions.or(assetRefNum, location));
			criteria.add(Restrictions.or(criterion, vendorCriteria));
		}

		if (assetAuditDtos.isSearchByEmpName()) {

			criteria.createAlias("employee", "employee");

			criteria.add(Restrictions.ilike("employee.employeeFullName", multiSearch, MatchMode.ANYWHERE));
		}
		if (dateRange.getMinimum() != null && dateRange.getMaximum() != null) {

			criteria.add(Restrictions.or(
					Restrictions.or(Restrictions.between("date", dateRange.getMinimum(), dateRange.getMaximum()),
							Restrictions.between("date", dateRange.getMinimum(), dateRange.getMaximum())),
					Restrictions.or(
							Restrictions.and(Restrictions.ge("date", dateRange.getMinimum()),
									Restrictions.le("date", dateRange.getMaximum())),
							Restrictions.or(
									Restrictions.and(Restrictions.ge("date", dateRange.getMinimum()),
											Restrictions.le("date", dateRange.getMaximum())),
									Restrictions.and(Restrictions.le("date", dateRange.getMinimum()),
											Restrictions.ge("date", dateRange.getMaximum()))))));

		} else if (dateRange.getMinimum() != null && dateRange.getMaximum() == null) {

			criteria.add(Restrictions.or(
					Restrictions.and(Restrictions.ge("date", dateRange.getMinimum()), Restrictions.isNull("date")),
					Restrictions.ge("date", dateRange.getMinimum())));

		} else if (dateRange.getMinimum() == null && dateRange.getMaximum() != null) {

			criteria.add(Restrictions.or(
					Restrictions.and(Restrictions.le("date", dateRange.getMaximum()), Restrictions.isNull("date")),
					Restrictions.le("date", dateRange.getMaximum())));
		}
		criteria.addOrder(Order.desc("id"));
		/*
		 * Integer noOfRecords = criteria.list().size();
		 * criteria.setFirstResult(startIndex); criteria.setMaxResults(endIndex -
		 * startIndex); List<AssetAuditDto> list = criteria.list();
		 */
		Map<String, Object> criteriaMap = getPaginationList(criteria, startIndex, endIndex);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", criteriaMap.get("list"));
		map.put("size", Integer.parseInt((criteriaMap.get("listSize").toString())));
		return map;

		/*
		 * Map<String,Object> allTransactionalHistoryMap =getPaginationList(criteria,
		 * startIndex, endIndex); Long size =(Long)
		 * allTransactionalHistoryMap.remove("listSize");
		 * allTransactionalHistoryMap.put("size", Integer.parseInt(size.toString()));
		 * return allTransactionalHistoryMap;
		 */

		// return criteria.list();
	}

	@Override
	public List<Asset> getEmpAssets(Asset asset) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Asset.class);
		criteria.createAlias("employee", "employee");
		criteria.createAlias("empDepartment", "empDepartment");
		criteria.add(Restrictions.eq("employee.employeeId", asset.getEmployee().getEmployeeId()));
		criteria.add(Restrictions.eq("empDepartment.departmentId", asset.getEmpDepartment().getDepartmentId()));
		return criteria.list();
	}

	@Override
	public List<EmpDepartment> getDepartmentNameList() {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(EmpDepartment.class);
		criteria.add(Restrictions.eq("supportManagementFlag", Boolean.TRUE));
		return criteria.list();
	}

	@Override
	public List<Asset> getEmployeeAssets(Long employeeId) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Asset.class);
		criteria.createAlias("employee", "employee");
		criteria.add(Restrictions.eq("employee.employeeId", employeeId));
		return criteria.list();
	}

	@Override
	public Map<String, Object> getWarrantyAssetsList(Integer startIndex, Integer endIndex, DateRange dateRange,
			Boolean seperateListFalg) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Asset.class);

		Employee employee = (Employee) securityUtils.getLoggedEmployeeDetailsSecurityContextHolder().get("employee");
		if (seperateListFalg) {
			criteria.createAlias("empDepartment", "department");
			criteria.add(Restrictions.eq("department.departmentName", employee.getDepartmentName()));
		}

		if (dateRange.getMinimum() != null && dateRange.getMaximum() != null) {

			criteria.add(Restrictions.between("warrantyEndDate", dateRange.getMinimum(), dateRange.getMaximum()));
		} else if (dateRange.getMinimum() == null && dateRange.getMaximum() != null) {

			criteria.add(Restrictions.or(
					Restrictions.and(Restrictions.le("warrantyEndDate", dateRange.getMaximum()),
							Restrictions.isNull("warrantyEndDate")),
					Restrictions.le("warrantyEndDate", dateRange.getMaximum())));
		}

		criteria.add(Restrictions.isNotNull("warrantyStartDate"));
		criteria.add(Restrictions.isNotNull("warrantyEndDate"));
		criteria.addOrder(Order.asc("warrantyEndDate"));

		/*
		 * Integer noOfRecords = criteria.list().size();
		 * 
		 * if (startIndex != null && endIndex != null) {
		 * criteria.setFirstResult(startIndex); criteria.setMaxResults(endIndex -
		 * startIndex); }
		 * 
		 * List<AssetDto> list = criteria.list();
		 */
		Map<String, Object> criteriaMap = getPaginationList(criteria, startIndex, endIndex);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", criteriaMap.get("list"));
		map.put("size", Integer.parseInt((criteriaMap.get("listSize").toString())));
		return map;

		/*
		 * Map<String,Object> allWarrantyAssetsListMap = getPaginationList(criteria,
		 * startIndex, endIndex); Long size=(Long)
		 * allWarrantyAssetsListMap.remove("listSize");
		 * allWarrantyAssetsListMap.put("size", Integer.parseInt(size.toString()));
		 * return allWarrantyAssetsListMap;
		 */
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<AssetType, Object> getAssetWhoseWarrantyEndDateisInNextFifteennDays() {

		Map<AssetType, Object> map = new HashMap<AssetType, Object>();

		Date date = new Date(DayOfMonth.valueOf(new Date().getDayOfMonth().getValue() + 15),
				MonthOfYear.valueOf(new Date().getMonthOfYear().getValue()),
				YearOfEra.valueOf(new Date().getYearOfEra().getValue()));

		List<AssetType> assetList = sessionFactory.getCurrentSession().createCriteria(AssetType.class).list();
		sessionFactory.getCurrentSession().flush();

		for (AssetType assetType : assetList) {

			Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Asset.class);
			criteria.createAlias("assetType", "assetType");
			criteria.add(Restrictions.eq("assetType", assetType));
			criteria.add(Restrictions.between("warrantyEndDate", new Date(), date));
			criteria.addOrder(Order.asc("warrantyEndDate"));

			map.put(assetType, criteria.list());
			sessionFactory.getCurrentSession().flush();
		}

		return map;

	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getAllAssetsForExport(String assetss, Long assetTypeId, Long productId, String status,
			String multiSearch,Date fromDate,Date toDate, Boolean searchByEmpName, Employee employee, Boolean seperateListFalg) {

		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Asset.class);
		if (seperateListFalg) {
			criteria.createAlias("empDepartment", "department");
			criteria.add(Restrictions.eq("department.departmentName", employee.getDepartmentName()));
		}
		criteria.createAlias("productSpecifications", "productSpecifications");
		criteria.createAlias("productSpecifications.product", "product");
		criteria.createAlias("assetType", "assetType");
		if (assetTypeId != null && productId == null) {
			criteria.add(Restrictions.eq("assetType.id", assetTypeId));
		} else if (assetTypeId != null && productId != null) {
			criteria.add(Restrictions.eq("product.productId", productId));
		}
		if (!status.isEmpty()) {
			criteria.add(Restrictions.eq("status", status));
		}
		if (searchByEmpName == false) {

			Criterion assetNumber = Restrictions.ilike("assetNumber", multiSearch, MatchMode.ANYWHERE);

			Criterion location = Restrictions.ilike("location", multiSearch, MatchMode.ANYWHERE);

			Criterion productSpecifications = Restrictions.ilike("productSpecifications.productSpecification",
					multiSearch, MatchMode.ANYWHERE);

			Criterion licenseNumber = Restrictions.ilike("otherAssetNumber", multiSearch, MatchMode.ANYWHERE);

			Criterion assetReferenceNumber = Restrictions.ilike("referenceNumber", multiSearch, MatchMode.ANYWHERE);
			Criterion criterion1 = Restrictions.or(assetNumber, location);
			Criterion criterion2 = Restrictions.or(assetReferenceNumber, criterion1);
			Criterion criterion = Restrictions.or(criterion2, Restrictions.or(licenseNumber, productSpecifications));

			criteria.add(criterion);
		} else {
			criteria.createAlias("employee", "employee");
			criteria.add(Restrictions.ilike("employee.employeeFullName", multiSearch, MatchMode.ANYWHERE));

		}
		if (assetss.equalsIgnoreCase("not_assigned")) {
			criteria.add(Restrictions.ilike("allocatedStatus", "Unassigned"));
		} else if (assetss.equalsIgnoreCase("Assigned")) {
			criteria.add(Restrictions.ilike("allocatedStatus", "Assigned"));
		}
		
		if(fromDate!=null&&toDate!=null)
		{
			criteria.add(Restrictions.between("purchasedDate", fromDate, toDate));
		}

		criteria.addOrder(Order.desc("id"));
		Integer noOfRecords = criteria.list().size();

		List<Product> list = criteria.list();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", list);
		map.put("size", noOfRecords);
		return map;

	}
}
