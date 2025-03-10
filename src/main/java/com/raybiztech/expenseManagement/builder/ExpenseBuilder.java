package com.raybiztech.expenseManagement.builder;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.stereotype.Component;

import com.raybiztech.appraisals.business.EmpDepartment;
import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.dao.DAO;
import com.raybiztech.appraisals.dto.EmployeeDTO;
import com.raybiztech.appraisals.security.utils.SecurityUtils;
import com.raybiztech.assetmanagement.business.VendorDetails;
import com.raybiztech.assetmanagement.dto.VendorDto;
import com.raybiztech.date.Date;
import com.raybiztech.date.DayOfMonth;
import com.raybiztech.date.MonthOfYear;
import com.raybiztech.date.Second;
import com.raybiztech.date.YearOfEra;
import com.raybiztech.expenseManagement.DAO.ExpenseDAO;
import com.raybiztech.expenseManagement.business.CreditCard;
import com.raybiztech.expenseManagement.business.ExpenseCategory;
import com.raybiztech.expenseManagement.business.ExpenseForm;
import com.raybiztech.expenseManagement.business.ExpenseFormAudit;
import com.raybiztech.expenseManagement.business.ExpenseSubCategory;
import com.raybiztech.expenseManagement.business.PaymentForm;
import com.raybiztech.expenseManagement.dto.CreditCardDto;
import com.raybiztech.expenseManagement.dto.ExpenseCategoryDto;
import com.raybiztech.expenseManagement.dto.ExpenseFormAuditDto;
import com.raybiztech.expenseManagement.dto.ExpenseFormDto;
import com.raybiztech.expenseManagement.dto.ExpenseSubCategoryDto;
import com.raybiztech.expenseManagement.dto.PaymentFormDto;
import com.raybiztech.expenseManagement.service.ExpenseManagementServiceImpl;
import com.raybiztech.expenseManagement.service.ExpenseService;
import com.raybiztech.payslip.utility.AES256Encryption;
import com.raybiztech.projectmanagement.business.Project;
import com.raybiztech.projectmanagement.dto.ProjectDTO;
import com.raybiztech.projectmanagement.invoice.lookup.Currency;
import com.raybiztech.projectmanagement.invoice.lookup.CurrencyToINR;
import com.raybiztech.recruitment.utils.DateParser;

@Component("expenseManagementBuilder")
public class ExpenseBuilder {

	@Autowired
	SecurityUtils securityUtils;

	@Autowired
	DAO dao;
	
	@Autowired
	ExpenseDAO expenseDaoImpl;
	

	public ExpenseCategory toCategoryEntity(ExpenseCategoryDto dto) {
		Long empId = securityUtils.getLoggedEmployeeIdforSecurityContextHolder();

		ExpenseCategory category = null;
		if (dto != null) {
			if (dto.getId() == null) {
				category = new ExpenseCategory();
				category.setCreatedBy(empId);
				category.setCreatedDate(new Date());
			} else {
				category = dao.findBy(ExpenseCategory.class, dto.getId());
				category.setUpdatedBy(empId);
				category.setUpdatedDate(new Date());
			}
			category.setCategoryName(dto.getCategoryName());

		}

		return category;

	}

	public ExpenseCategoryDto toCategoryDto(ExpenseCategory category) {
		ExpenseCategoryDto dto = null;
		if (category != null) {
			dto = new ExpenseCategoryDto();
			dto.setId(category.getId());
			dto.setCategoryName(category.getCategoryName());
			Employee createdEmp = dao.findBy(Employee.class, category.getCreatedBy());
			dto.setCreatedBy(createdEmp.getFullName());
			dto.setCreatedDate(category.getCreatedDate().toString("dd/MM/yyyy"));
			if (category.getUpdatedBy() != null) {
				Employee updatedEmp = dao.findBy(Employee.class, category.getUpdatedBy());
				dto.setUpdatedBy(updatedEmp.getFullName());
			}
			if (category.getUpdatedDate() != null) {
				dto.setUpdatedDate(category.getUpdatedDate().toString("dd/MM/yyyy"));
			}
		}
		return dto;

	}

	public List<ExpenseCategoryDto> toCategoryDTOList(List<ExpenseCategory> categoryList) {
		List<ExpenseCategoryDto> dtoList = null;
		if (categoryList != null) {
			dtoList = new ArrayList<>();
			for (ExpenseCategory category : categoryList) {
				dtoList.add(toCategoryDto(category));
			}
		}
		return dtoList;
	}

	public ExpenseSubCategory toSubCategoryEntity(ExpenseSubCategoryDto dto) {
		Long empId = securityUtils.getLoggedEmployeeIdforSecurityContextHolder();

		ExpenseSubCategory subCategory = null;
		if (dto != null) {
			if (dto.getId() == null) {
				subCategory = new ExpenseSubCategory();
				subCategory.setCreatedBy(empId);
				subCategory.setCreatedDate(new Date());
			} else {
				subCategory = dao.findBy(ExpenseSubCategory.class, dto.getId());
				subCategory.setUpdatedBy(empId);
				subCategory.setUpdatedDate(new Date());
			}
			subCategory.setCategory(dao.findBy(ExpenseCategory.class, dto.getCategoryId()));
			subCategory.setSubCategoryName(dto.getSubCategoryName());

		}

		return subCategory;

	}

	public ExpenseSubCategoryDto toSubCategoryDto(ExpenseSubCategory subCategory) {
		ExpenseSubCategoryDto dto = null;
		if (subCategory != null) {
			dto = new ExpenseSubCategoryDto();
			dto.setId(subCategory.getId());
			dto.setCategoryId(subCategory.getCategory().getId());
			dto.setCategoryName(subCategory.getCategory().getCategoryName());
			dto.setSubCategoryName(subCategory.getSubCategoryName());
			Employee createdEmp = dao.findBy(Employee.class, subCategory.getCreatedBy());
			dto.setCreatedBy(createdEmp.getFullName());
			dto.setCreatedDate(subCategory.getCreatedDate().toString("dd/MM/yyyy"));
			if (subCategory.getUpdatedBy() != null) {
				Employee updatedEmp = dao.findBy(Employee.class, subCategory.getUpdatedBy());
				dto.setUpdatedBy(updatedEmp.getFullName());
			}
			if (subCategory.getUpdatedDate() != null) {
				dto.setUpdatedDate(subCategory.getUpdatedDate().toString("dd/MM/yyyy"));
			}
		}
		return dto;

	}

	public List<ExpenseSubCategoryDto> toSubCategoryDTOList(List<ExpenseSubCategory> subCategoryList) {
		List<ExpenseSubCategoryDto> dtoList = null;
		if (subCategoryList != null) {
			dtoList = new ArrayList<>();
			for (ExpenseSubCategory subCategory : subCategoryList) {
				dtoList.add(toSubCategoryDto(subCategory));
			}
		}
		return dtoList;
	}

	public ExpenseForm toExpenseFormEntity(ExpenseFormDto expenseFormDto)  {
		ExpenseForm expenseForm = null;
		if (expenseFormDto != null) {
			Employee employee = (Employee) securityUtils.getLoggedEmployeeDetailsSecurityContextHolder()
					.get("employee");

			// if formId exists then update operation
			if (expenseFormDto.getFormId() != null) {
				expenseForm = dao.findBy(ExpenseForm.class, expenseFormDto.getFormId());
				expenseForm.setUpdatedBy(employee);
				expenseForm.setUpdatedDate(new Date());
				// encrypting amount
				String saltKey = KeyGenerators.string().generateKey();
				expenseForm.setSaltKey(saltKey);
				AES256Encryption encryption = new AES256Encryption(String.valueOf(expenseForm.getId()), saltKey);
				expenseForm.setAmount(encryption.encrypt(expenseFormDto.getAmount()));

			}
			// if formId does not exists then add operation
			else {
				expenseForm = new ExpenseForm();
				expenseForm.setCreatedBy(employee);
				expenseForm.setCreatedDate(new Date());
			}

			if (expenseFormDto.getToEmployee() != null) {
				Employee toEmployee = dao.findBy(Employee.class, expenseFormDto.getToEmployee().getId());
				expenseForm.setToEmployee(toEmployee);
			} else {
				expenseForm.setToEmployee(null);
			}
			ExpenseCategory category = dao.findBy(ExpenseCategory.class, expenseFormDto.getCategoryId());
			expenseForm.setCategory(category);
			if (expenseFormDto.getSubCategoryId() != null) {
				ExpenseSubCategory subCategory = dao.findBy(ExpenseSubCategory.class,
						expenseFormDto.getSubCategoryId());
				expenseForm.setSubCategory(subCategory);
			} else {
				expenseForm.setSubCategory(null);
			}
			EmpDepartment dept = dao.findBy(EmpDepartment.class, expenseFormDto.getDeptId());
			expenseForm.setDepartment(dept);

			if (expenseFormDto.getProject() != null) {
				Project project = dao.findBy(Project.class, expenseFormDto.getProject().getId());
				expenseForm.setProject(project);
			} else {
				expenseForm.setProject(null);
			}

			if (expenseFormDto.getVendor() != null) {
				VendorDetails vendor = dao.findBy(VendorDetails.class, expenseFormDto.getVendor().getVendorId());
				expenseForm.setVendor(vendor);
			} else {
				expenseForm.setVendor(null);
			}
			expenseForm.setPurpose(expenseFormDto.getPurpose());
		
			/*
			 * try { String date = expenseFormDto.getExpenditureDate();
			 * System.out.println("date:"+ date);
			 * 
			 * SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd"); SimpleDateFormat
			 * sdf2 = new SimpleDateFormat("dd/MM/yyyy");
			 * 
			 * date = sdf2.format(sdf1.parse(date)); System.out.println("dtae :" + date);
			 * expenseForm.setExpenditureDate(DateParser.toDate(date).next()); }catch
			 * (ParseException e) { e.printStackTrace(); }
			 */
			
			  try {
				  
				  expenseForm.setExpenditureDate(DateParser.toDate(expenseFormDto.getExpenditureDate())); 
			
				  }catch (ParseException e){
					  
			       e.printStackTrace();
			       }
			 
			expenseForm.setCountry(expenseFormDto.getCountry());
			Currency currency = dao.findBy(Currency.class, expenseFormDto.getCurrencyId());
			expenseForm.setCurrency(currency);
			expenseForm.setPaymentMode(expenseFormDto.getPaymentMode());
			expenseForm.setChequeNumber(expenseFormDto.getChequeNumber());
			try {
				expenseForm.setChequeDate(DateParser.toDate(expenseFormDto.getChequeDate()));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			expenseForm.setVoucherNumber(expenseFormDto.getVoucherNumber());
			expenseForm.setDescription(expenseFormDto.getDescription());
			if (expenseFormDto.getIsReimbursable() != null && expenseFormDto.getIsReimbursable()) {
				expenseForm.setIsReimbursable(Boolean.TRUE);
				expenseForm.setPaymentStatus("Not Paid");
			} else {
				expenseForm.setIsReimbursable(Boolean.FALSE);
				expenseForm.setPaymentStatus(null);
			}

			if (expenseFormDto.getPaymentMode().equalsIgnoreCase("Credit Card")) {
				expenseForm.setCreditCardDetails(toCardDetailEntity(expenseFormDto.getCreditCardDetails()));
			} else {
				expenseForm.setCreditCardDetails(null);
			}

			expenseForm.setInvoiceNumber(
					expenseFormDto.getInvoiceNumber() != null ? expenseFormDto.getInvoiceNumber() : null);
			expenseForm.setExpenseNumber(
					expenseFormDto.getExpenseNumber() != null ? expenseFormDto.getExpenseNumber() : null);
			expenseForm.setIsAutogenerated(Boolean.FALSE);

		}
		return expenseForm;

	}

	public ExpenseFormDto toExpenseFormDto(ExpenseForm form) {
		ExpenseFormDto dto = null;

		if (form != null) {
			dto = new ExpenseFormDto();

			AES256Encryption aes256Encryption = new AES256Encryption(String.valueOf(form.getId()), form.getSaltKey());
			dto.setFormId(form.getId());
			if (form.getToEmployee() != null) {
				EmployeeDTO toEmployee = new EmployeeDTO();
				toEmployee.setId(form.getToEmployee().getEmployeeId());
				toEmployee.setFullName(form.getToEmployee().getFullName());
				dto.setToEmployee(toEmployee);
			}
			if (form.getExpenseNumber() != null) {
				dto.setExpenseNumber(form.getExpenseNumber());
			}

			dto.setCategoryId(form.getCategory().getId());
			dto.setCategoryName(form.getCategory().getCategoryName());
			if (form.getSubCategory() != null) {
				dto.setSubCategoryId(form.getSubCategory().getId());
				dto.setSubCategoryName(form.getSubCategory().getSubCategoryName());
			}
			if (form.getProject() != null) {
				ProjectDTO project = new ProjectDTO();
				project.setId(form.getProject().getId());
				project.setProjectName(form.getProject().getProjectName());
				dto.setProject(project);
			}
			if (form.getVendor() != null) {
				VendorDto vendor = new VendorDto();
				vendor.setVendorId(form.getVendor().getVendorId());
				vendor.setVendorName(form.getVendor().getVendorName());
				dto.setVendor(vendor);
			}
			dto.setDeptId(form.getDepartment().getDepartmentId());
			dto.setDeptName(form.getDepartment().getDepartmentName());
			dto.setPurpose(form.getPurpose());
			dto.setExpenditureDate(form.getExpenditureDate().toString("dd/MM/yyyy"));
			dto.setCountry(form.getCountry());
			dto.setCurrencyId(form.getCurrency().getId());
			dto.setCurrencyType(form.getCurrency().getType());
			dto.setPaymentMode(form.getPaymentMode());
			dto.setChequeNumber(form.getChequeNumber());
			if (form.getChequeDate() != null) {
				dto.setChequeDate(form.getChequeDate().toString("dd/MM/yyyy"));
			}
			dto.setVoucherNumber(form.getVoucherNumber());
			dto.setDescription(form.getDescription());
			dto.setIsReimbursable(form.getIsReimbursable());
			dto.setEmpId(form.getCreatedBy().getEmployeeId());
			dto.setCreatedBy(form.getCreatedBy().getFullName());
			dto.setCreatedDate(form.getCreatedDate().toString("dd/MM/yyyy"));
			if (form.getUpdatedBy() != null) {
				dto.setUpdatedBy(form.getUpdatedBy().getFullName());
			}
			if (form.getUpdatedDate() != null) {
				dto.setUpdatedDate(form.getUpdatedDate().toString("dd/MM/yyyy"));
			}
			DecimalFormat df = new DecimalFormat("#.###");
			df.setRoundingMode(RoundingMode.CEILING);
			Double amount = Double.valueOf(
					aes256Encryption.decrypt(form.getAmount()) != null ? aes256Encryption.decrypt(form.getAmount())
							: null);
			dto.setAmount(df.format(amount));

			if (form.getPaymentMode().equalsIgnoreCase("Credit Card")) {
				dto.setCreditCardDetails(toCardDetailDto(form.getCreditCardDetails()));
			}
			dto.setPaymentStatus(form.getPaymentStatus());

			Map<String, Long> converter = this.getConvertedAmount();
			Long amountInr = converter.get(form.getCurrency().getType());
			Double actualAmount = Double.valueOf(aes256Encryption.decrypt(form.getAmount()));

			if ((!form.getCurrency().getType().equalsIgnoreCase("INR")) || form.getExpenseNumber() != null) {
				actualAmount = (actualAmount) * (amountInr != null ? amountInr : 1);
			}

			dto.setAmountInINR(df.format(actualAmount));

			if (form.getPaymentStatus() != null && form.getPaymentStatus().equalsIgnoreCase("Partially Paid")) {

				List<PaymentForm> paymentList = expenseDaoImpl.getPaymentList(form.getId());
				Double totalPaidAmount = 0.0;
				for (PaymentForm payment : paymentList) {
					AES256Encryption decryptPayment = new AES256Encryption(String.valueOf(payment.getId()),
							payment.getSaltKey());
					Double amountPaid = Double.valueOf(decryptPayment.decrypt(payment.getPaidAmount()));
					totalPaidAmount = totalPaidAmount + amountPaid;
				}
				Double remainingAmount = actualAmount - totalPaidAmount;
				dto.setRemainingAmount(df.format(remainingAmount));
			}

			dto.setInvoiceNumber(form.getInvoiceNumber() != null ? form.getInvoiceNumber() : null);

		}
		return dto;
	}

	public Map<String, Long> getConvertedAmount() {

		Map<String, Long> map = new HashMap<String, Long>();

		for (CurrencyToINR currencyToINR : expenseDaoImpl.get(CurrencyToINR.class)) {
			map.put(currencyToINR.getCurrenyType(), currencyToINR.getInrAmount());
		}

		return map;

	}

	public List<ExpenseFormDto> toExpenseFormDtoList(List<ExpenseForm> expenseFormList) {
		List<ExpenseFormDto> dtoList = null;
		if (expenseFormList != null) {
			dtoList = new ArrayList<>();
			for (ExpenseForm form : expenseFormList) {
				dtoList.add(toExpenseFormDto(form));
			}
		}

		return dtoList;
	}

	public ExpenseFormAudit toExpenseformAuditEntity(Long formId, String persistType) {

		ExpenseForm form = dao.findBy(ExpenseForm.class, formId);
		ExpenseFormAudit expenseFormAudit = null;

		if (form != null) {
			Employee employee = (Employee) securityUtils.getLoggedEmployeeDetailsSecurityContextHolder()
					.get("employee");
			expenseFormAudit = new ExpenseFormAudit();
			expenseFormAudit.setFormId(form.getId());
			if (form.getToEmployee() != null) {
				expenseFormAudit.setToEmployee(form.getToEmployee());
			} else {
				expenseFormAudit.setToEmployee(null);
			}
			expenseFormAudit.setExpenseNumber(form.getExpenseNumber());
			expenseFormAudit.setCategory(form.getCategory());
			if (form.getSubCategory() != null) {
				expenseFormAudit.setSubCategory(form.getSubCategory());
			} else {
				expenseFormAudit.setSubCategory(null);
			}
			expenseFormAudit.setDepartment(form.getDepartment());
			if (form.getProject() != null) {
				expenseFormAudit.setProject(form.getProject());
			}

			if (form.getVendor() != null) {
				expenseFormAudit.setVendor(form.getVendor());
			}
			expenseFormAudit.setPurpose(form.getPurpose());

			expenseFormAudit.setExpenditureDate(form.getExpenditureDate());
			expenseFormAudit.setCountry(form.getCountry());
			expenseFormAudit.setCurrency(form.getCurrency());
			expenseFormAudit.setPaymentMode(form.getPaymentMode());
			expenseFormAudit.setChequeNumber(form.getChequeNumber());

			expenseFormAudit.setChequeDate(form.getChequeDate());

			expenseFormAudit.setAmount(form.getAmount());
			expenseFormAudit.setSaltKey(form.getSaltKey());

			expenseFormAudit.setVoucherNumber(form.getVoucherNumber());
			expenseFormAudit.setDescription(form.getDescription());
			expenseFormAudit.setIsReimbursable(form.getIsReimbursable());
			expenseFormAudit.setPaymentStatus(form.getPaymentStatus());

			expenseFormAudit.setModifiedBy(employee);
			expenseFormAudit.setModifiedDate(new Second());
			expenseFormAudit.setPersistType(persistType);

			if (form.getPaymentMode().equalsIgnoreCase("Credit Card")) {
				expenseFormAudit.setCreditCardDetails(form.getCreditCardDetails());
			}
			expenseFormAudit.setInvoiceNumber(form.getInvoiceNumber() != null ? form.getInvoiceNumber() : null);
			expenseFormAudit.setIsAutogenerated(form.getIsAutogenerated() != null ? form.getIsAutogenerated() : null);

		}
		return expenseFormAudit;

	}

	public ExpenseFormAuditDto toExpenseFormAuditDto(ExpenseFormAudit auditForm) {
		ExpenseFormAuditDto dto = null;

		if (auditForm != null) {
			dto = new ExpenseFormAuditDto();

			AES256Encryption aes256Encryption = new AES256Encryption(String.valueOf(auditForm.getFormId()),
					auditForm.getSaltKey());
			dto.setId(auditForm.getId());
			dto.setFormId(auditForm.getFormId());
			dto.setExpenseNumber(auditForm.getExpenseNumber());
			if (auditForm.getToEmployee() != null) {
				dto.setToEmployeeId(auditForm.getToEmployee().getEmployeeId());
				dto.setToEmployeeName(auditForm.getToEmployee().getFullName());
			}
			dto.setCategoryId(auditForm.getCategory().getId());
			dto.setCategoryName(auditForm.getCategory().getCategoryName());
			if (auditForm.getSubCategory() != null) {
				dto.setSubCategoryId(auditForm.getSubCategory().getId());
				dto.setSubCategoryName(auditForm.getSubCategory().getSubCategoryName());
			}
			if (auditForm.getProject() != null) {
				dto.setProjectId(auditForm.getProject().getId());
				dto.setProjectName(auditForm.getProject().getProjectName());
			}
			if (auditForm.getVendor() != null) {
				dto.setVendorId(auditForm.getVendor().getVendorId());
				dto.setVendorName(auditForm.getVendor().getVendorName());
			}
			dto.setDeptId(auditForm.getDepartment().getDepartmentId());
			dto.setDeptName(auditForm.getDepartment().getDepartmentName());
			dto.setPurpose(auditForm.getPurpose());
			dto.setExpenditureDate(auditForm.getExpenditureDate().toString("dd/MM/yyyy"));
			dto.setCountry(auditForm.getCountry());
			dto.setCurrencyId(auditForm.getCurrency().getId());
			dto.setCurrencyType(auditForm.getCurrency().getType());
			dto.setPaymentMode(auditForm.getPaymentMode());
			dto.setChequeNumber(auditForm.getChequeNumber());
			if (auditForm.getChequeDate() != null) {
				dto.setChequeDate(auditForm.getChequeDate().toString("dd/MM/yyyy"));
			}
			dto.setVoucherNumber(auditForm.getVoucherNumber());
			dto.setDescription(auditForm.getDescription());
			dto.setIsReimbursable(auditForm.getIsReimbursable());
			dto.setPaymentStatus(auditForm.getPaymentStatus());
			dto.setEmpId(auditForm.getModifiedBy().getEmployeeId());
			dto.setModifiedBy(auditForm.getModifiedBy().getFullName());
			dto.setModifiedDate(auditForm.getModifiedDate().toString("dd-MM-yyyy hh:mm a"));

			DecimalFormat df = new DecimalFormat("#.###");
			df.setRoundingMode(RoundingMode.CEILING);
			Double amount = Double.valueOf(aes256Encryption.decrypt(auditForm.getAmount()));
			dto.setAmount(df.format(amount));
			dto.setPersistType(auditForm.getPersistType());
			if (auditForm.getPaymentMode().equalsIgnoreCase("Credit card")) {
				dto.setCreditCardDetails(toCardDetailDto(auditForm.getCreditCardDetails()));
			}
		}
		return dto;

	}

	public List<ExpenseFormAuditDto> toExpenseFormAuditDtoList(List<ExpenseFormAudit> auditList) {
		List<ExpenseFormAuditDto> auditDtos = new ArrayList<ExpenseFormAuditDto>();
		if (auditDtos != null) {
			for (ExpenseFormAudit audit : auditList) {
				auditDtos.add(toExpenseFormAuditDto(audit));
			}
		}
		return auditDtos;
	}

	public CreditCard toCardDetailEntity(CreditCardDto cardDto) {
		Long empId = securityUtils.getLoggedEmployeeIdforSecurityContextHolder();
		CreditCard cardInfo = null;
		if (cardDto != null) {

			if (cardDto.getCardId() == null) {
				cardInfo = new CreditCard();
				cardInfo.setCardName(cardDto.getCardName());
				cardInfo.setCardNumber(cardDto.getCardNumber());
				cardInfo.setCreatedBy(empId);
				cardInfo.setCreatedDate(new Date());
			}

			else {
				cardInfo = dao.findBy(CreditCard.class, cardDto.getCardId());
				// encrypt the details
				String saltKey = KeyGenerators.string().generateKey();
				AES256Encryption encryption = new AES256Encryption(String.valueOf(cardDto.getCardId()), saltKey);
				cardInfo.setSaltKey(saltKey);
				cardInfo.setCardName(encryption.encrypt(cardDto.getCardName()));
				cardInfo.setCardNumber(encryption.encrypt(cardDto.getCardNumber()));
				cardInfo.setUpdatedBy(empId);
				cardInfo.setUpdatedDate(new Date());
			}

		}

		return cardInfo;
	}

	public CreditCardDto toCardDetailDto(CreditCard cardInfo) {
		CreditCardDto dto = null;
		if (cardInfo != null) {
			dto = new CreditCardDto();
			dto.setCardId(cardInfo.getCardId());

			AES256Encryption decryption = new AES256Encryption(String.valueOf(cardInfo.getCardId()),
					cardInfo.getSaltKey());

			dto.setCardName(decryption.decrypt(cardInfo.getCardName()));
			dto.setCardNumber(decryption.decrypt(cardInfo.getCardNumber()));
			Employee createdEmp = dao.findBy(Employee.class, cardInfo.getCreatedBy());
			dto.setCreatedBy(createdEmp.getFullName());
			dto.setCreatedDate(cardInfo.getCreatedDate().toString("dd/MM/yyyy"));
			if (cardInfo.getUpdatedBy() != null) {
				Employee updatedEmp = dao.findBy(Employee.class, cardInfo.getUpdatedBy());
				dto.setUpdatedBy(updatedEmp.getFullName());
			}
			if (cardInfo.getUpdatedDate() != null) {
				dto.setUpdatedDate(cardInfo.getUpdatedDate().toString("dd/MM/yyyy"));
			}
		}
		return dto;

	}

	public List<CreditCardDto> toCardsListDto(List<CreditCard> cardsList) {
		List<CreditCardDto> cardDtoList = new ArrayList<CreditCardDto>();
		if (cardDtoList != null) {
			for (CreditCard expenseCards : cardsList) {
				cardDtoList.add(toCardDetailDto(expenseCards));
			}
		}
		return cardDtoList;
	}

	public PaymentForm toPaymentFormEntity(PaymentFormDto Dto) {
		PaymentForm form = null;
		if (Dto != null) {
			form = new PaymentForm();
			Employee loggedInEmployee = (Employee) securityUtils.getLoggedEmployeeDetailsSecurityContextHolder()
					.get("employee");

			System.out.println("paymentMode:" + Dto.getModeOfPayment());
			form.setModeOfPayment(Dto.getModeOfPayment());

			form.setChequeNumber(Dto.getChequeNumber());
			System.out.println("chequeNumber:" + Dto.getChequeDate());
			try {
				form.setChequeDate(DateParser.toDate(Dto.getChequeDate()));
				System.out.println("chequeDate:" + Dto.getChequeDate());
				form.setRtgsDate(DateParser.toDate(Dto.getRtgsDate()));
				System.out.println("rtgsDate:" + Dto.getRtgsDate());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			form.setCreatedBy(loggedInEmployee);
			form.setCreatedDate(new Second());
		}

		return form;
	}

	public PaymentFormDto toPaymentFormDto(PaymentForm form) {
		PaymentFormDto dto = null;
		if (form != null) {
			dto = new PaymentFormDto();
			dto.setId(form.getId());
			AES256Encryption decryption = new AES256Encryption(String.valueOf(form.getId()), form.getSaltKey());
			dto.setExpenseId(form.getExpenseId().getId());
			dto.setModeOfPayment(form.getModeOfPayment());

			DecimalFormat df = new DecimalFormat("#.###");
			df.setRoundingMode(RoundingMode.CEILING);
			Double amount = Double.valueOf(decryption.decrypt(form.getPaidAmount()));
			dto.setPaidAmount(df.format(amount));
			if (form.getChequeNumber() != null) {
				dto.setChequeNumber(form.getChequeNumber());
			}
			if (form.getChequeDate() != null) {
				dto.setChequeDate(form.getChequeDate().toString("dd/MM/yyyy"));
			}
			if (form.getRtgsDate() != null) {
				dto.setRtgsDate(form.getRtgsDate().toString("dd/MM/yyyy"));
			}

			dto.setStatus(form.getStatus());
			Employee employee = dao.findBy(Employee.class, form.getCreatedBy().getEmployeeId());
			dto.setCreatedBy(employee.getEmployeeFullName());
			dto.setCreatedDate(form.getCreatedDate().toString("dd-MM-yyyy hh:mm a"));
		}
		return dto;
	}

	public List<PaymentFormDto> getPaymentFormDtoList(List<PaymentForm> formList) {
		List<PaymentFormDto> dtoList = new ArrayList<PaymentFormDto>();
		if (formList != null) {
			for (PaymentForm form : formList) {
				dtoList.add(toPaymentFormDto(form));
			}
		}

		return dtoList;

	
	}
}
