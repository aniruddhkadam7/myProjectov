package com.raybiztech.expenseManagement.service;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

import com.raybiztech.assetmanagement.dto.VendorDto;
import com.raybiztech.expenseManagement.business.PaymentMode;
import com.raybiztech.expenseManagement.dto.CreditCardDto;
import com.raybiztech.expenseManagement.dto.ExpenseCategoryDto;
import com.raybiztech.expenseManagement.dto.ExpenseFormAuditDto;
import com.raybiztech.expenseManagement.dto.ExpenseFormDto;
import com.raybiztech.expenseManagement.dto.ExpenseSubCategoryDto;
import com.raybiztech.expenseManagement.dto.PaymentFormDto;
import com.raybiztech.projectmanagement.dto.ProjectDTO;
import com.raybiztech.projectmanagement.invoice.lookup.CountryLookUp;
import com.raybiztech.projectmanagement.invoice.lookup.Currency;

public interface ExpenseService {
	
	void addCategory(ExpenseCategoryDto categoryDto);
	
	List<ExpenseCategoryDto> getCategoryList();
	
	ExpenseCategoryDto editCategory(Long categoryId);
	
	void updateCategory(ExpenseCategoryDto categoryDto);
	
	void deleteCategory(Long categoryId);
	
	void addSubCategory(ExpenseSubCategoryDto subCategoryDto);
	
	List<ExpenseSubCategoryDto> getSubCategoryList();
	
	ExpenseSubCategoryDto editSubCategory(Long subCategoryId);
	
	void updateSubCategory(ExpenseSubCategoryDto subCategoryDto);
	
	void deleteSubCategory(Long subCategoryId);
	
	List<Currency> getCurrencyList();
	
	List<ExpenseSubCategoryDto> getSubCategories(Long categoryId);
	
	void addExpenses(ExpenseFormDto expenseFormDto);
	
	Map<String, Object> getExpensesList( Integer startIndex,
			 Integer endIndex,  String from,  String to,
			 String dateSelection,  String multipleSearch,  Long departmentId, List<Long> categoryId , List<Long> subCategoryId ,
			 String country, String paymentMode);
	
	void updateExpenses(ExpenseFormDto expenseFormDto);
	
	ExpenseFormDto getExpenseDetails(Long formId);
	
	List<PaymentMode> getPaymentList();
	
	List<ProjectDTO> getMatchedProjects(String searchStr);
	
	List<VendorDto> getVendorList(String searchStr);

	Map<String, Object> getExpensesYearlyReport(String fromDate, String toDate, Long departmentId, Long categoryId,String country);

	List<ExpenseFormDto> getMonthWiseExpenseList(String key, Long departmentId, Long categoryId,String country);

	Boolean checkForDuplicateCategory(String categoryName);

	Boolean checkForDuplicateSubCategory(Long categoryId, String subCategoryName);

	ByteArrayOutputStream exportExpenseList(String from, String to, String dateSelection, String multipleSearch,
			Long departmentId, List<Long> categoryId,List<Long> subCategoryId,String country, String paymentMode) throws Exception;

	List<ExpenseFormAuditDto> getExpenseAudit(Long formId);

	List<CountryLookUp> getCountries();

	void addCardDetails(CreditCardDto dto);

	CreditCardDto editCardDetials(Long cardId);

	void updateCardDetails(CreditCardDto cardDto);

	Boolean checkDuplicateCardNumber(String cardNumber);

	void deleteCardData(Long cardId);

	List<CreditCardDto> getCardsList();

	Map<String, Object> getReimbursementExpenseList(Integer startIndex, Integer endIndex, String from, String to,
			String dateSelection, String multipleSearch, Long departmentId, Long categoryId, String country,
			String paymentMode, String paymentStatus, Long creditCardId);

	void savePaymentForm(PaymentFormDto paymentFormDto);

	List<PaymentFormDto> getExpensePaymentList(Long expenseId);

	void payAllAtOnce(List<Long> formIds, PaymentFormDto paymentFormDto);

	Map<String, Object> getCountryWiseExpenseReport(String fromDate, String toDate);

	List<ExpenseFormDto> getCountryWiseExpenseList(String key, String fromDate, String toDate);

	List<ExpenseFormDto> getCountryCategoryWiseExpenseList(String fromDate, String toDate, String country,
			String category);

	void addCopiedExpensesList(List<Long> formIds);
	
	void deleteExpense(Long formId);

	List<ExpenseSubCategoryDto> getSelectedSubCategories(List<Long> categoryId);
	}
