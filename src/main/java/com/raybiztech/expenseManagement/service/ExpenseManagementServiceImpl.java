package com.raybiztech.expenseManagement.service;

import java.io.ByteArrayOutputStream;
import java.math.RoundingMode;
import java.text.DateFormatSymbols;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.raybiztech.appraisals.business.EmpDepartment;
import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.security.utils.SecurityUtils;
import com.raybiztech.assetmanagement.builder.VendorBuilder;
import com.raybiztech.assetmanagement.business.VendorDetails;
import com.raybiztech.assetmanagement.dto.VendorDto;
import com.raybiztech.date.Date;
import com.raybiztech.date.DayOfMonth;
import com.raybiztech.date.MonthOfYear;
import com.raybiztech.date.YearOfEra;
import com.raybiztech.expenseManagement.DAO.ExpenseDAO;
import com.raybiztech.expenseManagement.builder.ExpenseBuilder;
import com.raybiztech.expenseManagement.business.CreditCard;
import com.raybiztech.expenseManagement.business.ExpenseCategory;
import com.raybiztech.expenseManagement.business.ExpenseForm;
import com.raybiztech.expenseManagement.business.ExpenseFormAudit;
import com.raybiztech.expenseManagement.business.ExpenseSubCategory;
import com.raybiztech.expenseManagement.business.PaymentForm;
import com.raybiztech.expenseManagement.business.PaymentMode;
import com.raybiztech.expenseManagement.dto.ExpenseAuditComparator;
import com.raybiztech.expenseManagement.dto.CreditCardDto;
import com.raybiztech.expenseManagement.dto.ExpenseCategoryDto;
import com.raybiztech.expenseManagement.dto.ExpenseFormAuditDto;
import com.raybiztech.expenseManagement.dto.ExpenseFormDto;
import com.raybiztech.expenseManagement.dto.ExpenseSubCategoryDto;
import com.raybiztech.expenseManagement.dto.ExpenseYearlyReportDto;
import com.raybiztech.expenseManagement.dto.PaymentFormDto;
import com.raybiztech.payslip.utility.AES256Encryption;
import com.raybiztech.projectmanagement.builder.ProjectBuilder;
import com.raybiztech.projectmanagement.business.Project;
import com.raybiztech.projectmanagement.dto.ProjectDTO;
import com.raybiztech.projectmanagement.invoice.lookup.CountryLookUp;
import com.raybiztech.projectmanagement.invoice.lookup.Currency;
import com.raybiztech.projectmanagement.invoice.lookup.CurrencyToINR;
import com.raybiztech.recruitment.utils.DateParser;
import com.raybiztech.rolefeature.business.Permission;

@Service("expenseServiceImpl")
@Transactional
public class ExpenseManagementServiceImpl implements ExpenseService{
	
	Logger logger = Logger.getLogger(ExpenseManagementServiceImpl.class);
	
	@Autowired
	ExpenseDAO expenseDaoImpl;
	
	@Autowired
	ExpenseBuilder expenseManagementBuilder;
	
	@Autowired
	SecurityUtils securityUtils;
	
	@Autowired
	ProjectBuilder projectBuilder;
	
	@Autowired
	VendorBuilder vendorBuilder;

	@Override
	public void addCategory(ExpenseCategoryDto categoryDto) {
		ExpenseCategory category = expenseManagementBuilder.toCategoryEntity(categoryDto);
		expenseDaoImpl.save(category);
		
	}

	@Override
	public List<ExpenseCategoryDto> getCategoryList() {
		List<ExpenseCategory> categoryList = expenseDaoImpl.getCategoryList();
		List<ExpenseCategoryDto> categoryDTOList = expenseManagementBuilder.toCategoryDTOList(categoryList);
		return categoryDTOList;
	}

	@Override
	public ExpenseCategoryDto editCategory(Long categoryId) {
		ExpenseCategory category = expenseDaoImpl.findBy(ExpenseCategory.class, categoryId);
		ExpenseCategoryDto categoryDto = expenseManagementBuilder.toCategoryDto(category);
		return categoryDto;
	}
	
	@Override
	public void updateCategory(ExpenseCategoryDto categoryDto) {
		ExpenseCategory category = expenseManagementBuilder.toCategoryEntity(categoryDto);
		expenseDaoImpl.update(category);
		
	}

	@Override
	public void deleteCategory(Long categoryId) {
		expenseDaoImpl.delete(expenseDaoImpl.findBy(ExpenseCategory.class, categoryId));
		
	}

	@Override
	public void addSubCategory(ExpenseSubCategoryDto subCategoryDto) {
		ExpenseSubCategory subCategory = expenseManagementBuilder.toSubCategoryEntity(subCategoryDto);
		expenseDaoImpl.save(subCategory);
		
	}

	@Override
	public List<ExpenseSubCategoryDto> getSubCategoryList() {
		List<ExpenseSubCategory> subCategoryList = expenseDaoImpl.getsubCategoryList();
		List<ExpenseSubCategoryDto> subCategoryDtoList = expenseManagementBuilder.toSubCategoryDTOList(subCategoryList);
		return subCategoryDtoList;
	}

	@Override
	public ExpenseSubCategoryDto editSubCategory(Long subCategoryId) {
		ExpenseSubCategory subCategory = expenseDaoImpl.findBy(ExpenseSubCategory.class, subCategoryId);
		ExpenseSubCategoryDto subCategoryDto = expenseManagementBuilder.toSubCategoryDto(subCategory);
		return subCategoryDto;
	}

	@Override
	public void updateSubCategory(ExpenseSubCategoryDto subCategoryDto) {
		ExpenseSubCategory subCategory = expenseManagementBuilder.toSubCategoryEntity(subCategoryDto);
		expenseDaoImpl.update(subCategory);
		
	}

	@Override
	public void deleteSubCategory(Long subCategoryId) {
		expenseDaoImpl.delete(expenseDaoImpl.findBy(ExpenseSubCategory.class, subCategoryId));
		
	}

	@Override
	public List<Currency> getCurrencyList() {
		return expenseDaoImpl.get(Currency.class);
	}

	@Override
	public List<ExpenseSubCategoryDto> getSubCategories(Long categoryId) {
		List<ExpenseSubCategory> subCategoryList = expenseDaoImpl.getSubCategories(categoryId);
		List<ExpenseSubCategoryDto> subCategoryDtoList = expenseManagementBuilder.toSubCategoryDTOList(subCategoryList);
		return subCategoryDtoList;
	}

	@Override
	public void addExpenses(ExpenseFormDto expenseFormDto) {
		ExpenseForm expenseForm = expenseManagementBuilder.toExpenseFormEntity(expenseFormDto);
		//generating expense number
		String expenseNo = getExpenseNumber(expenseForm.getCountry());
		//logger.warn("expenseNo:"+expenseNo);
		expenseForm.setExpenseNumber(expenseNo);
		Long id =   (Long) expenseDaoImpl.save(expenseForm);
		
		ExpenseForm form = expenseDaoImpl.findBy(ExpenseForm.class, id);
		//encrypting amount and updating already saved data
		String saltKey = KeyGenerators.string().generateKey();
		form.setSaltKey(saltKey);
		AES256Encryption encryption = new AES256Encryption(String.valueOf(form.getId()), saltKey);
		form.setAmount(encryption.encrypt(expenseFormDto.getAmount()));
		expenseDaoImpl.update(form);
		
		ExpenseFormAudit audit = expenseManagementBuilder.toExpenseformAuditEntity(id,"Created");
		expenseDaoImpl.save(audit);
	}

	@Override
	public Map<String, Object> getExpensesList(Integer startIndex, Integer endIndex, String from, String to,
			String dateSelection, String multipleSearch, Long departmentId, List<Long> categoryId,
			List<Long> subCategoryId,String country, String paymentMode) {
	   
		ObjectMapper mapper = new ObjectMapper();
		List<Long> categoryIds =
				mapper.convertValue(categoryId, new TypeReference<List<Long>>(){});
		List<Long> subCategoryIds =
				mapper.convertValue(subCategoryId, new TypeReference<List<Long>>(){});
		Date fromDate = null;
		Date toDate = null;
		if(dateSelection.equalsIgnoreCase("Custom")) {
			try {
				fromDate = DateParser.toDate(from);
				toDate = DateParser.toDate(to);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
		}
		else {
			Map<String, Date> dateMap = expenseDaoImpl.getCustomDates(dateSelection);
			fromDate = dateMap.get("startDate");
			toDate = dateMap.get("endDate");
		}
		
		Employee employee = (Employee) securityUtils.getLoggedEmployeeDetailsSecurityContextHolder()
									.get("employee");
		
		Permission totalExpensesList = expenseDaoImpl.checkForPermission("TotalExpensesList", employee);
		
		Permission departmentWiseList = expenseDaoImpl.checkForPermission("DepartmentWiseList", employee);
		
		Map<String, Object> expenseMap = null;
		
		if(totalExpensesList.getView()) {
		expenseMap = expenseDaoImpl.getExpensesList(startIndex, endIndex, fromDate, 
				toDate, multipleSearch,departmentId, categoryIds ,subCategoryIds, country, paymentMode);
		}
		
		EmpDepartment department = new EmpDepartment();
		if(departmentWiseList.getView()) {
			List<EmpDepartment> deptList = expenseDaoImpl.get(EmpDepartment.class);
			for(EmpDepartment dept : deptList) {
				if(dept.getDepartmentName().equals(employee.getDepartmentName())) {
					department = dept;
				}
			}
			departmentId = department.getDepartmentId();
			expenseMap = expenseDaoImpl.getExpensesList(startIndex, endIndex, fromDate, 
					toDate, multipleSearch,departmentId, categoryIds,subCategoryIds, country, paymentMode);
		}
		
		Map<String, Object> map = new HashMap<>();
		
		List<ExpenseFormDto> expensesList = (expenseManagementBuilder
				.toExpenseFormDtoList( (List)expenseMap.get("list")));
		String totalExpenseAmount = this.getTotalExpenseAmount(fromDate, 
				toDate, multipleSearch,departmentId, categoryIds ,subCategoryIds, country, paymentMode);
		map.put("list", expensesList);
		map.put("size", expenseMap.get("size"));
		map.put("totalExpenseAmount", totalExpenseAmount);
		
		return map;
	}

	@Override
	public void updateExpenses(ExpenseFormDto expenseFormDto)  {
			ExpenseForm	cloneForm = expenseDaoImpl.findBy(ExpenseForm.class, expenseFormDto.getFormId());
			
			ExpenseForm oldForm = null;
			try {
				oldForm  = (ExpenseForm) cloneForm.clone();
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}
		ExpenseForm expenseForm = expenseManagementBuilder.toExpenseFormEntity(expenseFormDto);
		//generating expense number
		if((!oldForm.getCountry().equalsIgnoreCase(expenseFormDto.getCountry())) || oldForm.getExpenseNumber()==null) {	
		String expenseNo = getExpenseNumber(expenseForm.getCountry());
		expenseForm.setExpenseNumber(expenseNo);
		
		}
		if(expenseForm.getExpenseNumber()!=null) {
			expenseForm.setIsAutogenerated(Boolean.FALSE);
		}
	
		expenseDaoImpl.update(expenseForm);
		
		ExpenseFormAudit audit = expenseManagementBuilder.toExpenseformAuditEntity(expenseFormDto.getFormId(),"Updated");
		expenseDaoImpl.save(audit);
		
	}

	@Override
	public ExpenseFormDto getExpenseDetails(Long formId) {
		ExpenseForm form = expenseDaoImpl.findBy(ExpenseForm.class, formId);
		ExpenseFormDto dto = expenseManagementBuilder.toExpenseFormDto(form);
		return dto;
	}

	@Override
	public List<PaymentMode> getPaymentList() {
		return expenseDaoImpl.get(PaymentMode.class);
	}

	@Override
	public List<ProjectDTO> getMatchedProjects(String searchStr) {
		List<Project> projects = expenseDaoImpl
				.getMatchedProjects(searchStr);
		List<ProjectDTO> dTOs = projectBuilder
				.createProjectActiveList(projects);
		return dTOs;
		
	}

	@Override
	public List<VendorDto> getVendorList(String searchStr) {
		List<VendorDetails> vendorDetails = expenseDaoImpl.getVendorList(searchStr);
		List<VendorDto> vendorDto = vendorBuilder.convertEntityToDtoList(vendorDetails);
		return vendorDto;
	}

	@Override
	public Map<String, Object> getExpensesYearlyReport(String fromDate, String toDate, Long departmentId,
			Long categoryId,String country) {
		Date startDate = null;
		Date endDate = null;
		
		try {
			startDate = DateParser.toDate(fromDate);
			endDate = DateParser.toDate(toDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		Map<String, Object> expensesMap = expenseDaoImpl.getExpensesYearlyReport(startDate.toString("yyyy-MM-dd"),
				endDate.toString("yyyy-MM-dd"),departmentId,categoryId,country);
		
		List<ExpenseForm> expensesList = (List<ExpenseForm>) expensesMap.get("list");
		
		Map<String, Object> reportMap = this.getExpenseDtosList(expensesList);
		
		List<ExpenseYearlyReportDto> expenseFormDtoList = (List<ExpenseYearlyReportDto>) reportMap.get("reportList");
		
		Map<String, Object> yearlyReportMap = new HashMap<String, Object>();
		
		yearlyReportMap.put("displayStartDate", startDate);
		yearlyReportMap.put("displayEndDate", endDate);
		yearlyReportMap.put("result", expenseFormDtoList);
		yearlyReportMap.put("finalTotalAmount", reportMap.get("finalTotalAmount"));
		yearlyReportMap.put("yearsList", reportMap.get("yearsList"));
		
		return yearlyReportMap;
	}

	private Map<String, Object> getExpenseDtosList(List<ExpenseForm> expensesList) {
		
		
		Map<Integer, ExpenseYearlyReportDto> yearlyReport = new TreeMap<Integer, ExpenseYearlyReportDto>();
		
		List<ExpenseFormDto> dtoList = new ArrayList<>();
		
		Map<String, Long> converter =  this.getConvertedAmount();
		
		Long totalAmount = 0L;
		
		List<String> months = new ArrayList<String>(Arrays.asList(new DateFormatSymbols().getMonths()));
		
		List<Integer> years = new ArrayList<>();
		
		for(ExpenseForm form : expensesList) {
			AES256Encryption aes256Encryption = new AES256Encryption(String.valueOf(form.getId()), 
					form.getSaltKey());
			
			Long amount = 0L;
			Date date = form.getExpenditureDate();
			
			if(date != null) {
			
			Integer month = date.getMonthOfYear().getValue();
			
			Integer year = date.getYearOfEra().getValue();
			
			if(!years.contains(year)) {
				years.add(year);
			}
			
			Integer keyValue = Integer.parseInt(String.valueOf(year) + String.valueOf(month + 1));
			
			//logger.warn("keyVAlue:"+keyValue);
			
			Long amountInr = converter.get(form.getCurrency().getType());
			
			
			if(!form.getCurrency().getType().equalsIgnoreCase("INR")) {
				amount = Math.round((Double.valueOf(aes256Encryption.decrypt(form.getAmount())))*(amountInr != null ? amountInr
						: 1));
				//logger.warn("in if amount:"+amount);
			}
			else {
				amount = Math.round((Double.valueOf(aes256Encryption.decrypt(form.getAmount()))));
				//logger.warn("in else amount:"+amount);
			}
			
			totalAmount += amount;
			
			if(yearlyReport.containsKey(keyValue)) {
				ExpenseYearlyReportDto dto = yearlyReport.get(keyValue);
				dto.setTotalAmount(dto.getTotalAmount() + amount);
			}
			else {
				
				ExpenseYearlyReportDto dto = new ExpenseYearlyReportDto();
				
				dto.setKey(keyValue);
				dto.setMonth(months.get(month));
				dto.setYear(year.toString());
				dto.setTotalAmount(amount);
				
				yearlyReport.put(keyValue, dto);
			}
			
			}
			
		}
	
		Collections.sort(years, Collections.reverseOrder()); 
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("reportList", new ArrayList<ExpenseYearlyReportDto>(yearlyReport.values()));
		map.put("finalTotalAmount", totalAmount);
		map.put("yearsList", years);
		return map;
	}
	
	public Map<String, Long> getConvertedAmount() {

		Map<String, Long> map = new HashMap<String, Long>();

		for (CurrencyToINR currencyToINR : expenseDaoImpl
				.get(CurrencyToINR.class)) {
			map.put(currencyToINR.getCurrenyType(),
					currencyToINR.getInrAmount());
		}

		return map;

	}

	@Override
	public List<ExpenseFormDto> getMonthWiseExpenseList(String key, Long departmentId, Long categoryId,String country) {
		
		/*List<String> months = new ArrayList<String>(Arrays.asList(new DateFormatSymbols().getMonths()));
		String month = String.valueOf((months.indexOf("December") + 1));*/
		String year = key.substring(0, 4);
		String month = key.substring(4);
		
		List<ExpenseForm> expenseList = expenseDaoImpl.getMonthWiseExpenseList(month,year,departmentId,categoryId,country);
		List<ExpenseFormDto> dtoList = expenseManagementBuilder.toExpenseFormDtoList(expenseList);
		return dtoList;
	}

	@Override
	public Boolean checkForDuplicateCategory(String categoryName) {
		return expenseDaoImpl.checkForDuplicateCategory(categoryName);
	}

	@Override
	public Boolean checkForDuplicateSubCategory(Long categoryId, String subCategoryName) {
		return expenseDaoImpl.checkForDuplicateSubCategory(categoryId,subCategoryName);
	}

	@Override
	public ByteArrayOutputStream exportExpenseList(String from, String to, String dateSelection, String multipleSearch,
			Long departmentId, List<Long> categoryId , List<Long> subCategoryId,String country, String paymentMode) throws Exception{
		
		Date fromDate = null;
		Date toDate = null;
		if(dateSelection.equalsIgnoreCase("Custom")) {
			try {
				fromDate = DateParser.toDate(from);
				toDate = DateParser.toDate(to);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
		}
		else {
			Map<String, Date> dateMap = expenseDaoImpl.getCustomDates(dateSelection);
			fromDate = dateMap.get("startDate");
			toDate = dateMap.get("endDate");
		}
		
		Employee employee = (Employee) securityUtils.getLoggedEmployeeDetailsSecurityContextHolder()
									.get("employee");
		
		Permission totalExpensesList = expenseDaoImpl.checkForPermission("TotalExpensesList", employee);
		
		Permission departmentWiseList = expenseDaoImpl.checkForPermission("DepartmentWiseList", employee);
		
		Map<String, Object> expenseMap = null;
		
		if(totalExpensesList.getView()) {
		expenseMap = expenseDaoImpl.getExpensesList(null, null, fromDate, 
				toDate, multipleSearch,departmentId, categoryId,subCategoryId, country, paymentMode);
		}
		
		EmpDepartment department = new EmpDepartment();
		if(departmentWiseList.getView()) {
			List<EmpDepartment> deptList = expenseDaoImpl.get(EmpDepartment.class);
			for(EmpDepartment dept : deptList) {
				if(dept.getDepartmentName().equals(employee.getDepartmentName())) {
					department = dept;
				}
			}
			departmentId = department.getDepartmentId();
			expenseMap = expenseDaoImpl.getExpensesList(null, null, fromDate, 
					toDate, multipleSearch,departmentId, categoryId, subCategoryId,country, paymentMode);
		}
		
		Map<String, Object> map = new HashMap<>();
		
		List<ExpenseForm> expensesList = ((List)expenseMap.get("list"));

		ByteArrayOutputStream os = new ByteArrayOutputStream();
		
		int rowIndex = 1;
		
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
		cell0.setCellValue("S.No");
		cell0.setCellStyle(style);
		
		Cell cell1 = row1.createCell(1);
		cell1.setCellValue("ExpenseNo");
		cell1.setCellStyle(style);
		
		Cell cell2 = row1.createCell(2);
		cell2.setCellValue("Employee Name");
		cell2.setCellStyle(style);
		
		Cell cell3 = row1.createCell(3);
		cell3.setCellValue("Category");
		cell3.setCellStyle(style);
		
		Cell cell4 = row1.createCell(4);
		cell4.setCellValue("Sub-Category");
		cell4.setCellStyle(style);
		
		Cell cell5 = row1.createCell(5);
		cell5.setCellValue("Department");
		cell5.setCellStyle(style);
		
		Cell cell6 = row1.createCell(6);
		cell6.setCellValue("Project");
		cell6.setCellStyle(style);
		
		Cell cell7 = row1.createCell(7);
		cell7.setCellValue("Purpose");
		cell7.setCellStyle(style);
		
		Cell cell8 = row1.createCell(8);
		cell8.setCellValue("Mode of Payment");
		cell8.setCellStyle(style);
		
		Cell cell9 = row1.createCell(9);
		cell9.setCellValue("Amount");
		cell9.setCellStyle(style);
		
		Cell cell10 = row1.createCell(10);
		cell10.setCellValue("Amount in (INR)");
		cell10.setCellStyle(style);
		
		Cell cell11 = row1.createCell(11);
		cell11.setCellValue("Country");
		cell11.setCellStyle(style);
		
		Cell cell12 = row1.createCell(12);
		cell12.setCellValue("Currency");
		cell12.setCellStyle(style);
		
		Cell cell13 = row1.createCell(13);
		cell13.setCellValue("Expenditure Date");
		cell13.setCellStyle(style);
		
		int index = 1;
		for(ExpenseForm form : expensesList) {
			
			AES256Encryption aes256Encryption = new AES256Encryption(String.valueOf(form.getId()), 
					form.getSaltKey());
			
			Row row = sheet.createRow(rowIndex++);
			
			Cell cel0 = row.createCell(0);
			cel0.setCellValue(index++);
			
			Cell cel1 = row.createCell(1);
			cel1.setCellValue(form.getExpenseNumber());
			
			Cell cel2 = row.createCell(2);
			cel2.setCellValue(form.getToEmployee() != null?form.getToEmployee().getFullName():"N/A");
			
			Cell cel3 = row.createCell(3);
			cel3.setCellValue(form.getCategory().getCategoryName());
			
			Cell cel4 = row.createCell(4);
			cel4.setCellValue(form.getSubCategory().getSubCategoryName());
			
			Cell cel5 = row.createCell(5);
			cel5.setCellValue(form.getDepartment() != null?form.getDepartment().getDepartmentName():"N/A");
			
			Cell cel6 = row.createCell(6);
			cel6.setCellValue(form.getProject() != null?form.getProject().getProjectName():"N/A");
			
			Cell cel7 = row.createCell(7);
			cel7.setCellValue(form.getPurpose());
			
			Cell cel8 = row.createCell(8);
			cel8.setCellValue(form.getPaymentMode());
			
			Cell cel9 = row.createCell(9);
			DecimalFormat df = new DecimalFormat("#.###");
			df.setRoundingMode(RoundingMode.CEILING);
			Double amount = Double.valueOf(aes256Encryption.decrypt(form.getAmount()));
			cel9.setCellValue(df.format(amount));
			
			Cell cel10 = row.createCell(10);
			
			Map<String, Long> converter =  this.getConvertedAmount();
			Long amountInr = converter.get(form.getCurrency().getType());
			
			
			if(!form.getCurrency().getType().equalsIgnoreCase("INR")) {
				amount = (amount)*(amountInr != null ? amountInr: 1);
				cel10.setCellValue(df.format(amount));
			}
			else {
				cel10.setCellValue(df.format(amount));
			}
			
			Cell cel11 = row.createCell(11);
			cel11.setCellValue(form.getCountry());
			
			Cell cel12 = row.createCell(12);
			cel12.setCellValue(form.getCurrency().getType());
			
			
			Cell cel13 = row.createCell(13);
			cel13.setCellValue(form.getExpenditureDate().toString("dd/MM/yyyy"));
			
			
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
			sheet.autoSizeColumn(11);
			sheet.autoSizeColumn(12);
			sheet.autoSizeColumn(13);
			
		}
		
		workbook.write(os);
		os.flush();
		os.close();
		return os;
	}

	@Override
	public List<ExpenseFormAuditDto> getExpenseAudit(Long formId) {
		List<ExpenseFormAudit> auditList = expenseDaoImpl.getExpenseAudit(formId);
		List<ExpenseFormAuditDto> auditDtos = expenseManagementBuilder.toExpenseFormAuditDtoList(auditList);
		Collections.sort(auditDtos, new ExpenseAuditComparator());
		return auditDtos;
	}
	
	public String getExpenseNumber(String country) {
		
		StringBuilder builder = new StringBuilder();
		
   String patternforInvoice = expenseDaoImpl.getExpensePattern();
		
       if(patternforInvoice != null){
			logger.warn("patternforInvoice"+patternforInvoice);
		builder.append(patternforInvoice);
		}
	
		
		int year = new Date().getYearOfEra().getValue();
		int month = new Date().getMonthOfYear().getValue() +1;
		
		builder.append(String.format("%02d", month)).append(String.valueOf(year).substring(2));
		
		builder.append(country.subSequence(0, 2));
		
		String maxNumber = expenseDaoImpl.getMaxExpenseNo(String.valueOf(year),country);
		
		logger.warn("maxno:"+ maxNumber);
		String expenseNo;
		String pattern;
		if(maxNumber != null) {
	
		 Long nextExpenseNo = Long.valueOf(maxNumber.substring(8));
		 if(nextExpenseNo.toString().length()>3) {
		 pattern = "%0"+ nextExpenseNo.toString().length()+"d";
		 }
		 else {
			pattern = "%03d"; 
		 }
		 logger.warn("next number :" + nextExpenseNo);
		 expenseNo = String.format(pattern,(nextExpenseNo+1));
		
			
		}
		else {
			expenseNo = "001";
		}
		builder.append(expenseNo);
		return builder.toString();
		
	}

	@Override
	public List<CountryLookUp> getCountries() {
		
		return expenseDaoImpl.getCountries();
	}

	@Override
	public void addCardDetails(CreditCardDto cardDto) {
		CreditCard cardInfo = expenseManagementBuilder.toCardDetailEntity(cardDto);
		Long cardId = (Long) expenseDaoImpl.save(cardInfo);
		
		
		//encrypting card details
		CreditCard cardData = expenseDaoImpl.findBy(CreditCard.class, cardId);
		String saltKey = KeyGenerators.string().generateKey();
		AES256Encryption encryption = new AES256Encryption(String.valueOf(cardId), saltKey);
		
		cardData.setSaltKey(saltKey);
		cardData.setCardName(encryption.encrypt(cardDto.getCardName()));
		cardData.setCardNumber(encryption.encrypt(cardDto.getCardNumber()));
		expenseDaoImpl.update(cardData);
		
	}

	@Override
	public CreditCardDto editCardDetials(Long cardId) {
		CreditCard cardInfo= expenseDaoImpl.findBy(CreditCard.class, cardId);
		CreditCardDto cardsDto = expenseManagementBuilder.toCardDetailDto(cardInfo);
		return cardsDto;
	}

	@Override
	public void updateCardDetails(CreditCardDto cardDto) {
		CreditCard cardInfo = expenseManagementBuilder.toCardDetailEntity(cardDto);
		expenseDaoImpl.update(cardInfo);
	}


	@Override
	public void deleteCardData(Long cardId) {
		expenseDaoImpl.delete(expenseDaoImpl.findBy(CreditCard.class, cardId));
		
	}

	@Override
	public List<CreditCardDto> getCardsList() {
		List<CreditCard> cardsList = expenseDaoImpl.getCardsList();
		return expenseManagementBuilder.toCardsListDto(cardsList);
	}

	@Override
	public Boolean checkDuplicateCardNumber(String cardNumber) {
		Boolean flag = Boolean.FALSE;
		List<CreditCardDto> cardsList = getCardsList();
		
		for (CreditCardDto expenseCardsDto : cardsList) {
			if(expenseCardsDto.getCardNumber().equals(cardNumber)) {
				flag = Boolean.TRUE;
				break;
			}
		}
		
		return flag;
	}

	@Override
	public Map<String, Object> getReimbursementExpenseList(Integer startIndex, Integer endIndex, String from, String to,
			String dateSelection, String multipleSearch, Long departmentId, Long categoryId, String country,
			String paymentMode, String paymentStatus, Long creditCardId) {
		Date fromDate = null;
		Date toDate = null;
		if(dateSelection.equalsIgnoreCase("Custom")) {
			try {
				fromDate = DateParser.toDate(from);
				toDate = DateParser.toDate(to);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
		}
		else {
			Map<String, Date> dateMap = expenseDaoImpl.getCustomDates(dateSelection);
			fromDate = dateMap.get("startDate");
			toDate = dateMap.get("endDate");
		}
		
		Employee employee = (Employee) securityUtils.getLoggedEmployeeDetailsSecurityContextHolder()
									.get("employee");
		Map<String, Object> expenseMap = null;
		
		expenseMap = expenseDaoImpl.getReimbursementExpenseList(startIndex, endIndex, fromDate, 
				toDate, multipleSearch,departmentId, categoryId, country, paymentMode, paymentStatus, creditCardId);
		
		Map<String, Object> map = new HashMap<>();
		
		List<ExpenseFormDto> expensesList = (expenseManagementBuilder
				.toExpenseFormDtoList( (List)expenseMap.get("list")));
		
		map.put("list", expensesList);
		map.put("size", expenseMap.get("size"));
		
		return map;
	}

	@Override
	public void savePaymentForm(PaymentFormDto paymentFormDto) {
		PaymentForm paymentForm = expenseManagementBuilder.toPaymentFormEntity(paymentFormDto);
		
		ExpenseForm expense = expenseDaoImpl.findBy(ExpenseForm.class, paymentFormDto.getExpenseId());
		
		paymentForm.setExpenseId(expense);
		AES256Encryption decryption = new AES256Encryption(String.valueOf(expense.getId()), expense.getSaltKey());
		Map<String, Long> converter =  this.getConvertedAmount();
		Long amountInr = converter.get(expense.getCurrency().getType());
		Double actualAmount = Double.valueOf(decryption.decrypt(expense.getAmount()));
		
		if(!expense.getCurrency().getType().equalsIgnoreCase("INR")) {
			actualAmount = (actualAmount)*(amountInr != null ? amountInr: 1);	
		}
		
		Double newAmount = Double.valueOf(paymentFormDto.getPaidAmount());
		paymentForm.setPaidAmount(paymentFormDto.getPaidAmount());
		
		if(expense.getPaymentStatus().equalsIgnoreCase("Not Paid") && newAmount.equals(actualAmount)) {
			logger.warn("in if");
			paymentForm.setStatus("Paid");
		}
		else if(expense.getPaymentStatus().equalsIgnoreCase("Partially Paid")) {
			logger.warn("in else if");
			List<PaymentForm> paymentList = expenseDaoImpl.getPaymentList(expense.getId());
			Double totalPaidAmount = 0.0;
			for(PaymentForm payment : paymentList) {
				AES256Encryption decryptPayment = new AES256Encryption(String.valueOf(payment.getId()), payment.getSaltKey());
				Double amountPaid = Double.valueOf(decryptPayment.decrypt(payment.getPaidAmount()));
				totalPaidAmount = totalPaidAmount + amountPaid;
			}
			
			Double remainingAmount = actualAmount - totalPaidAmount;
			if(newAmount.equals(remainingAmount)) {
				paymentForm.setStatus("Paid");
			}
			else {
				paymentForm.setStatus("Partially Paid");
			}
		}
		else {
			logger.warn("in else");
			paymentForm.setStatus("Partially Paid");
		}
		
		
		
		Long id = (Long) expenseDaoImpl.save(paymentForm);
		//encrypting the amount
		PaymentForm form = expenseDaoImpl.findBy(PaymentForm.class, id);
		
		expense.setPaymentStatus(form.getStatus());
		expenseDaoImpl.update(expense);
		
		ExpenseFormAudit audit = expenseDaoImpl.getExpenseAudit(expense.getId()).get(0);
		audit.setPaymentStatus(form.getStatus());
		expenseDaoImpl.update(audit);
		
		String saltKey = KeyGenerators.string().generateKey();
		form.setSaltKey(saltKey);
		AES256Encryption encryption = new AES256Encryption(String.valueOf(id), saltKey);
		form.setPaidAmount(encryption.encrypt(form.getPaidAmount()));
		expenseDaoImpl.update(form);
	}

	@Override
	public List<PaymentFormDto> getExpensePaymentList(Long expenseId) {
		List<PaymentForm> paymentList = expenseDaoImpl.getPaymentList(expenseId);
		return expenseManagementBuilder.getPaymentFormDtoList(paymentList);
	}

	@Override
	public void payAllAtOnce(List<Long> formIds, PaymentFormDto paymentFormDto) {
		
		//System.out.println("totalAmount:"+paymentFormDto.getPaidAmount());
		/*List<ExpenseForm> expensesList = expenseDaoImpl.getListOfExpenses(formIds);
		List<PaymentForm> paymentForms = new ArrayList<>();
		for(ExpenseForm expense : expensesList) {
			PaymentForm form  = expenseManagementBuilder.toPaymentFormEntity(paymentFormDto);
			ExpenseForm expenseForm = expenseDaoImpl.findBy(ExpenseForm.class, expense.getId());
			form.setExpenseId(expenseForm);
			form.setSaltKey(expense.getSaltKey());
			form.setPaidAmount(expenseForm.getAmount());
			form.setStatus("Paid");
			expenseDaoImpl.save(form);
			paymentForms.add(form);
			
		}*/
		
		for(Long expenseId : formIds) {
			//System.out.println(expenseId);
			ExpenseForm expense = expenseDaoImpl.findBy(ExpenseForm.class, expenseId);
			AES256Encryption decryption = new AES256Encryption(String.valueOf(expense.getId()), expense.getSaltKey());
			Map<String, Long> converter =  this.getConvertedAmount();
			Long amountInr = converter.get(expense.getCurrency().getType());
			Double actualAmount = Double.valueOf(decryption.decrypt(expense.getAmount()));
			
			if(!expense.getCurrency().getType().equalsIgnoreCase("INR")) {
				actualAmount = (actualAmount)*(amountInr != null ? amountInr: 1);	
			}
			paymentFormDto.setExpenseId(expenseId);
			paymentFormDto.setPaidAmount(actualAmount.toString());
			this.savePaymentForm(paymentFormDto);
		}
	}

	
	private String getTotalExpenseAmount(Date fromDate, Date toDate, String multipleSearch, Long departmentId,
			List<Long> categoryIds,List<Long> subCategoryIds, String country, String paymentMode) {

		List<ExpenseForm> expensesList = expenseDaoImpl.getTotalExpenseAmount(fromDate,toDate,multipleSearch,
				departmentId,categoryIds,subCategoryIds,country,paymentMode);
		Double totalAmount = 0.0;
		Map<String, Long> converter =  this.getConvertedAmount();
		for(ExpenseForm form : expensesList) {
			AES256Encryption decryption = new AES256Encryption(String.valueOf(form.getId()), form.getSaltKey());
			Long amountInr = converter.get(form.getCurrency().getType());
			Double amount = Double.valueOf(decryption.decrypt(form.getAmount()));
		
			
			if((!form.getCurrency().getType().equalsIgnoreCase("INR")) && form.getExpenseNumber()!=null) {
				
		        Double amount1= (amount)*(amountInr != null ? amountInr: 1);
			     totalAmount += amount1;
			}
			if((form.getCurrency().getType().equalsIgnoreCase("INR")) && form.getExpenseNumber()!=null) {
				
		        Double amount1= (amount)*(amountInr != null ? amountInr: 1);
			     totalAmount += amount1;
			}
			
		}
	    DecimalFormat df = new DecimalFormat("#.###");
		df.setRoundingMode(RoundingMode.CEILING);
		return df.format(totalAmount);
	}

	@Override
	public Map<String, Object> getCountryWiseExpenseReport(String fromDate, String toDate) {
		Date startDate = null;
		Date endDate = null;
		
		try {
			startDate = DateParser.toDate(fromDate);
			endDate = DateParser.toDate(toDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		List<ExpenseForm> expensesList = expenseDaoImpl.getCountryWiseExpenseReport(startDate,endDate);
		
		//List<ExpenseForm> expensesList = (List<ExpenseForm>) expensesMap.get("list");
		
		Map<String, Object> reportMap = this.getCountryWiseExpenseDto(expensesList);
		
		//List<ExpenseYearlyReportDto> expenseFormDtoList = (List<ExpenseYearlyReportDto>) reportMap.get("reportList");
		
		Map<String, Object> countryExpenseReportMap = new HashMap<String, Object>();
		
		countryExpenseReportMap.put("result", reportMap.get("countryWiseExpenseAmount"));
		countryExpenseReportMap.put("finalTotalAmount", reportMap.get("finalTotalAmount"));

		
		return countryExpenseReportMap;
	}

	private Map<String, Object> getCountryWiseExpenseDto(List<ExpenseForm> expensesList) {
		

		Map<String, Long> countryReport = new TreeMap<String, Long>();
		
		List<ExpenseFormDto> dtoList = new ArrayList<>();
		
		Map<String, Long> converter =  this.getConvertedAmount();
		
		Long totalAmount = 0L;
		
		List<CountryLookUp> countries = expenseDaoImpl.getCountries();
		
		
		for(ExpenseForm form : expensesList) {
			AES256Encryption aes256Encryption = new AES256Encryption(String.valueOf(form.getId()), 
					form.getSaltKey());
			
			Long amount = 0L;
			
			Long amountInr = converter.get(form.getCurrency().getType());
			
			
			if(!form.getCurrency().getType().equalsIgnoreCase("INR")) {
				amount = Math.round((Double.valueOf(aes256Encryption.decrypt(form.getAmount())))*(amountInr != null ? amountInr
						: 1));
			//logger.warn("in if amount:"+amount);
			}
			else {
				amount = Math.round((Double.valueOf(aes256Encryption.decrypt(form.getAmount()))));
				//logger.warn("in else amount:"+amount);
			}
			
			totalAmount += amount;
			
			if(!countryReport.containsKey(form.getCountry())) {
			countryReport.put(form.getCountry(), amount);
			//System.out.println("first amount:"+amount);
			}
			else {
				Long existingAmount = countryReport.get(form.getCountry());
			
				countryReport.put(form.getCountry(), (existingAmount+amount));
				
			}
		}
		
		Map<String, Object> map = new HashMap<>();
		map.put("countryWiseExpenseAmount", countryReport);
		map.put("finalTotalAmount", totalAmount);
		return map;
		
	}

	@Override
	public List<ExpenseFormDto> getCountryWiseExpenseList(String key, String fromDate, String toDate) {
		Date startDate = null;
		Date endDate = null;
		
		try {
			startDate = DateParser.toDate(fromDate);
			endDate = DateParser.toDate(toDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		List<ExpenseForm> expensesList = expenseDaoImpl.getCountryWiseExpenseList(key,startDate,endDate);
		Map<String, Long> categoryWiseAmount = new TreeMap<String, Long>();
		List<ExpenseFormDto> expensesDto = new ArrayList<>();
		for(ExpenseForm form : expensesList) {
			AES256Encryption aes256Encryption = new AES256Encryption(String.valueOf(form.getId()), 
					form.getSaltKey());
			Long amount = Math.round((Double.valueOf(aes256Encryption.decrypt(form.getAmount()))));
			if(!categoryWiseAmount.containsKey(form.getCategory().getCategoryName())) {
				categoryWiseAmount.put(form.getCategory().getCategoryName(), amount);
			}
			else {
				Long existingAmount = categoryWiseAmount.get(form.getCategory().getCategoryName());
				categoryWiseAmount.put(form.getCategory().getCategoryName(), (existingAmount+amount));
			}
		}
		for(Map.Entry<String, Long> map : categoryWiseAmount.entrySet()) {
			ExpenseFormDto dto = new ExpenseFormDto();
			dto.setCategoryName(map.getKey());
			dto.setAmount(map.getValue().toString());
			dto.setCurrencyType(expensesList.get(0).getCurrency().getType());
			expensesDto.add(dto);
		}
		return expensesDto;
	}

	@Override
	public List<ExpenseFormDto> getCountryCategoryWiseExpenseList(String fromDate, String toDate, String country,
			String category) {
		Date startDate = null;
		Date endDate = null;
		
		try {
			startDate = DateParser.toDate(fromDate);
			endDate = DateParser.toDate(toDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		List<ExpenseForm> expenseList = expenseDaoImpl.getCountryCategoryWiseExpenseList(startDate,endDate,country,category);
		List<ExpenseFormDto> dtoList = expenseManagementBuilder.toExpenseFormDtoList(expenseList);
		return dtoList;
	}
	@Override
	public void addCopiedExpensesList(List<Long> formIds) {

		List<ExpenseForm> list = null;
		ObjectMapper mapper = new ObjectMapper();
		List<Long> myObjects =
				mapper.convertValue(formIds, new TypeReference<List<Long>>(){});
		List<ExpenseForm> expensesList = expenseDaoImpl.getCopiedExpensesList(myObjects);
	
        
		for(ExpenseForm ex:expensesList)
		{
			
			ExpenseFormDto dto = expenseManagementBuilder.toExpenseFormDto(ex);
			dto.setFormId(null);
			
			ExpenseForm exp = expenseManagementBuilder.toExpenseFormEntity(dto);
			
			exp.setExpenditureDate(new Date(DayOfMonth.valueOf(1),
					  MonthOfYear.valueOf(new Date().getMonthOfYear().getValue()),
					  YearOfEra.valueOf(new Date().getYearOfEra().getValue())));
			exp.setIsAutogenerated(Boolean.TRUE);
			
			Long id =   (Long) expenseDaoImpl.save(exp);
			
			ExpenseForm form = expenseDaoImpl.findBy(ExpenseForm.class, id);
			//encrypting amount and updating already saved data
			String saltKey = KeyGenerators.string().generateKey();
			form.setSaltKey(saltKey);
			AES256Encryption encryption = new AES256Encryption(String.valueOf(form.getId()), saltKey);
			form.setAmount(encryption.encrypt(dto.getAmount()));
			form.setExpenseNumber(null);
			expenseDaoImpl.update(form);
			
			
			
		}
		
	}

	@Override
	public void deleteExpense(Long formId) {
		 {
			
			expenseDaoImpl.delete(expenseDaoImpl.findBy(ExpenseForm.class, formId));
			
		}
	}

	@Override
	public List<ExpenseSubCategoryDto> getSelectedSubCategories(List<Long> categoryId) {
		ObjectMapper mapper = new ObjectMapper();
		List<Long> categoryIds =
				mapper.convertValue(categoryId, new TypeReference<List<Long>>(){});
		List<ExpenseSubCategory> subCategoryList = expenseDaoImpl.getSelectedSubCategories(categoryIds);
		List<ExpenseSubCategoryDto> subCategoryDtoList = expenseManagementBuilder.toSubCategoryDTOList(subCategoryList);
		return subCategoryDtoList;
	}
}

	
	

