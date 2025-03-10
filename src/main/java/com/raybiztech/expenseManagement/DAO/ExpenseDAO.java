package com.raybiztech.expenseManagement.DAO;

import java.util.List;
import java.util.Map;

import com.raybiztech.appraisals.dao.DAO;
import com.raybiztech.assetmanagement.business.VendorDetails;
import com.raybiztech.date.Date;
import com.raybiztech.expenseManagement.business.CreditCard;
import com.raybiztech.expenseManagement.business.ExpenseCategory;
import com.raybiztech.expenseManagement.business.ExpenseForm;
import com.raybiztech.expenseManagement.business.ExpenseFormAudit;
import com.raybiztech.expenseManagement.business.ExpenseSubCategory;
import com.raybiztech.expenseManagement.business.PaymentForm;
import com.raybiztech.projectmanagement.business.Project;
import com.raybiztech.projectmanagement.invoice.lookup.CountryLookUp;


public interface ExpenseDAO extends DAO{
	
	List<ExpenseCategory> getCategoryList();
	
	List<ExpenseSubCategory> getsubCategoryList();
	
	List<ExpenseSubCategory> getSubCategories(Long categoryId);
	
	Map<String, Object> getExpensesList( Integer startIndex,
			 Integer endIndex,  Date fromDate,  Date toDate,
			 String multipleSearch,  Long departmentId, List<Long> categoryIds , 
			 List<Long> subCategoryIds, String country, String paymentMode);
	
	List<Project> getMatchedProjects(String searchStr);
	
	List<VendorDetails> getVendorList(String searchStr);

	Map<String, Object> getExpensesYearlyReport(String fromDate, String toDate, Long departmentId, Long categoryId,String country);

	List<ExpenseForm> getMonthWiseExpenseList(String month, String year, Long departmentId, Long categoryId,String country);

	Boolean checkForDuplicateCategory(String categoryName);

	Boolean checkForDuplicateSubCategory(Long categoryId, String subCategoryName);

	List<ExpenseFormAudit> getExpenseAudit(Long formId);

	List<CountryLookUp> getCountries();

	String getMaxExpenseNo(String year, String country);
	
	List<CreditCard> getCardsList();

	Map<String, Object> getReimbursementExpenseList(Integer startIndex, Integer endIndex, Date fromDate, Date toDate,
			String multipleSearch, Long departmentId, Long categoryId, String country, String paymentMode, String paymentStatus, Long creditCardId);

	List<PaymentForm> getPaymentList(Long id);

	
	List<ExpenseForm> getTotalExpenseAmount(Date fromDate, Date toDate, String multipleSearch, Long departmentId,
			List<Long> categoryIds,List<Long> subCategoryIds, String country, String paymentMode);

	/*List<ExpenseForm> getListOfExpenses(List<Long> formIds);*/
	
	String getExpensePattern();

	List<ExpenseForm> getCountryWiseExpenseReport(Date startDate, Date endDate);

	List<ExpenseForm> getCountryWiseExpenseList(String key, Date startDate, Date endDate);

	List<ExpenseForm> getCountryCategoryWiseExpenseList(Date startDate, Date endDate, String country, String category);

	List<ExpenseForm> getCopiedExpensesList(List<Long> formIds);
	List<ExpenseSubCategory> getSelectedSubCategories(List<Long> categoryIds);
	
}
