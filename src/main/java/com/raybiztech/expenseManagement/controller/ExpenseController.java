package com.raybiztech.expenseManagement.controller;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.raybiztech.assetmanagement.dto.VendorDto;
import com.raybiztech.expenseManagement.business.PaymentMode;
import com.raybiztech.expenseManagement.dto.CreditCardDto;
import com.raybiztech.expenseManagement.dto.ExpenseCategoryDto;
import com.raybiztech.expenseManagement.dto.ExpenseFormAuditDto;
import com.raybiztech.expenseManagement.dto.ExpenseFormDto;
import com.raybiztech.expenseManagement.dto.ExpenseSubCategoryDto;
import com.raybiztech.expenseManagement.dto.PaymentFormDto;
import com.raybiztech.expenseManagement.service.ExpenseService;
import com.raybiztech.projectmanagement.dto.ProjectDTO;
import com.raybiztech.projectmanagement.invoice.lookup.CountryLookUp;
import com.raybiztech.projectmanagement.invoice.lookup.Currency;

@Controller
@RequestMapping("/ExpenseManagement")
public class ExpenseController {

	@Autowired
	ExpenseService expenseServiceImpl;

	@RequestMapping(value = "/addCategory", method = RequestMethod.POST)
	public @ResponseBody void addCategory(@RequestBody ExpenseCategoryDto categoryDto) {
		expenseServiceImpl.addCategory(categoryDto);
	}

	@RequestMapping(value = "/getCategoryList", method = RequestMethod.GET)
	public @ResponseBody List<ExpenseCategoryDto> getCategoryList(HttpServletResponse httpServletResponse) {
		return expenseServiceImpl.getCategoryList();
	}

	@RequestMapping(value = "/editCategory", params = { "categoryId" }, method = RequestMethod.GET)
	public @ResponseBody ExpenseCategoryDto editCategory(@RequestParam Long categoryId) {
		return expenseServiceImpl.editCategory(categoryId);
	}

	@RequestMapping(value = "/updateCategory", method = RequestMethod.PUT)
	public @ResponseBody void updateCategory(@RequestBody ExpenseCategoryDto categoryDto) {
		expenseServiceImpl.updateCategory(categoryDto);
	}

	@RequestMapping(value = "/deleteCategory", params = { "categoryId" }, method = RequestMethod.DELETE)
	public @ResponseBody void deleteCategory(@RequestParam Long categoryId) {
		expenseServiceImpl.deleteCategory(categoryId);
	}

	@RequestMapping(value = "/addSubCategory", method = RequestMethod.POST)
	public @ResponseBody void addSubCategory(@RequestBody ExpenseSubCategoryDto subCategoryDto) {
		expenseServiceImpl.addSubCategory(subCategoryDto);
	}

	@RequestMapping(value = "/getSubCategoryList", method = RequestMethod.GET)
	public @ResponseBody List<ExpenseSubCategoryDto> getSubCategoryList(HttpServletResponse httpServletResponse) {
		return expenseServiceImpl.getSubCategoryList();
	}

	@RequestMapping(value = "/editSubCategory", params = { "subCategoryId" }, method = RequestMethod.GET)
	public @ResponseBody ExpenseSubCategoryDto editSubCategory(@RequestParam Long subCategoryId) {
		return expenseServiceImpl.editSubCategory(subCategoryId);
	}

	@RequestMapping(value = "/updateSubCategory", method = RequestMethod.PUT)
	public @ResponseBody void updateSubCategory(@RequestBody ExpenseSubCategoryDto subCategoryDto) {
		expenseServiceImpl.updateSubCategory(subCategoryDto);
	}

	@RequestMapping(value = "/deleteSubCategory", params = { "subCategoryId" }, method = RequestMethod.DELETE)
	public @ResponseBody void deleteSubCategory(@RequestParam Long subCategoryId) {
		expenseServiceImpl.deleteSubCategory(subCategoryId);
	}

	@RequestMapping(value = "/getCurrencyList", method = RequestMethod.GET)
	public @ResponseBody List<Currency> getCurrencyList() {
		return expenseServiceImpl.getCurrencyList();
	}

	@RequestMapping(value = "/getSubCategories", params = { "categoryId" }, method = RequestMethod.GET)
	public @ResponseBody List<ExpenseSubCategoryDto> getSubCategories(Long categoryId) {
		return expenseServiceImpl.getSubCategories(categoryId);
	}

	@RequestMapping(value = "/addExpenses", method = RequestMethod.POST)
	public @ResponseBody void addExpenses(@RequestBody ExpenseFormDto expenseFormDto) {
		expenseServiceImpl.addExpenses(expenseFormDto);
	}

	@RequestMapping(value = "/getExpensesList", params = { "startIndex", "endIndex", "from", "to", "dateSelection",
			"multipleSearch", "departmentId", "categoryId","subCategoryId", "country", "paymentMode"}, method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> getExpensesList(@RequestParam Integer startIndex,
			@RequestParam Integer endIndex, @RequestParam String from, @RequestParam String to,
			@RequestParam String dateSelection, @RequestParam String multipleSearch, @RequestParam Long departmentId, 
			@RequestParam List<Long> categoryId,@RequestParam List<Long> subCategoryId , @RequestParam String country,@RequestParam String paymentMode,
			HttpServletResponse httpServletResponse) {
		
		return expenseServiceImpl.getExpensesList(startIndex, endIndex, from, to, dateSelection, multipleSearch,
				departmentId, categoryId, subCategoryId,country, paymentMode);
	}
	
	@RequestMapping(value = "/updateExpenses",method = RequestMethod.PUT)
	public @ResponseBody void updateExpenses(@RequestBody ExpenseFormDto expenseFormDto) {
		expenseServiceImpl.updateExpenses(expenseFormDto);
	}
	
	@RequestMapping(value = "/getExpenseDetails", params = {"formId"}, method = RequestMethod.GET)
	public @ResponseBody ExpenseFormDto getExpenseDetails(@RequestParam Long formId,HttpServletResponse httpServletResponse) {
		return expenseServiceImpl.getExpenseDetails(formId);
	}
	
	@RequestMapping(value = "/getPaymentList", method = RequestMethod.GET)
	public @ResponseBody List<PaymentMode> getPaymentList(){
		return expenseServiceImpl.getPaymentList();
	}
		
	@RequestMapping(value = "/getMatchedProjects", params = {"searchStr"}, method = RequestMethod.GET)
	public @ResponseBody List<ProjectDTO> getMatchedProjects(@RequestParam String searchStr){
		return expenseServiceImpl.getMatchedProjects(searchStr);
	}
	
	@RequestMapping(value = "/getVendorList", params = {"searchStr"}, method = RequestMethod.GET)
	public @ResponseBody List<VendorDto> getVendorList(@RequestParam String searchStr){
		return expenseServiceImpl.getVendorList(searchStr);
	}
	
	@RequestMapping(value="/getExpensesYearlyReport", params = {"fromDate", "toDate", 
			 "departmentId", "categoryId","country"}, method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> getExpensesYearlyReport(@RequestParam String fromDate, @RequestParam String toDate,
			 @RequestParam Long departmentId, @RequestParam Long categoryId, @RequestParam String country,HttpServletResponse httpServletResponse) {
				return expenseServiceImpl.getExpensesYearlyReport(fromDate, toDate,	departmentId, categoryId,country);
	}
	
	@RequestMapping(value="/getMonthWiseExpenseList", params = {"key", 
			 "departmentId", "categoryId","country"}, method = RequestMethod.GET)
	public @ResponseBody List<ExpenseFormDto> getExpensesYearlyReport(@RequestParam String key, 
			 @RequestParam Long departmentId, @RequestParam Long categoryId,@RequestParam String country) {
				return expenseServiceImpl.getMonthWiseExpenseList(key,departmentId, categoryId,country);
	}
	
	@RequestMapping(value = "/checkForDuplicateCategory", params = {"categoryName"}, method = RequestMethod.GET)
	public @ResponseBody Boolean checkForDuplicateCategory(@RequestParam String categoryName){
		return expenseServiceImpl.checkForDuplicateCategory(categoryName);
	}
	
	@RequestMapping(value = "/checkForDuplicateSubCategory", params = {"categoryId","subCategoryName"}, method = RequestMethod.GET)
	public @ResponseBody Boolean checkForDuplicateSubCategory(@RequestParam Long categoryId,@RequestParam String subCategoryName) {
		return expenseServiceImpl.checkForDuplicateSubCategory(categoryId,subCategoryName);
	}
	
	@RequestMapping(value = "/exportExpenseList", params = {"from", "to", "dateSelection",
			"multipleSearch", "departmentId", "categoryId","subCategoryId", "country", "paymentMode"}, method = RequestMethod.GET)
	public @ResponseBody void exportExpenseList(@RequestParam String from, @RequestParam String to,
			@RequestParam String dateSelection, @RequestParam String multipleSearch, @RequestParam Long departmentId, 
			@RequestParam List<Long> categoryId ,@RequestParam List<Long> subCategoryId,@RequestParam String country, @RequestParam String paymentMode, 
			HttpServletResponse httpServletResponse) throws Exception{
		
		httpServletResponse.setContentType("text/csv");
		httpServletResponse.setHeader("Content-Disposition",
				"attachment; filename=\"ExpenseList.csv\"");
		
		ByteArrayOutputStream os = expenseServiceImpl.exportExpenseList(from, to, dateSelection, multipleSearch,
				departmentId, categoryId,subCategoryId, country, paymentMode);
		
		httpServletResponse.getOutputStream().write(os.toByteArray());
		
	}
	
	@RequestMapping(value="/getExpenseAudit", params = {"formId"}, method = RequestMethod.GET)
	public @ResponseBody List<ExpenseFormAuditDto> getExpenseAudit(Long formId, HttpServletResponse httpServletResponse){
		return expenseServiceImpl.getExpenseAudit(formId);
	}
	
	@RequestMapping(value="/getCountries", method = RequestMethod.GET)
	public @ResponseBody List<CountryLookUp> getCountries(){
		return expenseServiceImpl.getCountries();
	}
	
	@RequestMapping(value="/addCardDetails", method = RequestMethod.POST)
	public @ResponseBody void addCardDetails(@RequestBody CreditCardDto cardDto) {
		expenseServiceImpl.addCardDetails(cardDto);
	}
	
	@RequestMapping(value = "/editCardDetials", params = { "cardId" }, method = RequestMethod.GET)
	public @ResponseBody CreditCardDto editCardDetials(@RequestParam Long cardId) {
		return expenseServiceImpl.editCardDetials(cardId);
	}

	@RequestMapping(value = "/updateCardDetails", method = RequestMethod.PUT)
	public @ResponseBody void updateCardDetails(@RequestBody CreditCardDto cardDto) {
		expenseServiceImpl.updateCardDetails(cardDto);
	}
	
		
	@RequestMapping(value = "/deleteCardData", params = { "cardId" }, method = RequestMethod.DELETE)
	public @ResponseBody void deleteCardData(@RequestParam Long cardId) {
		expenseServiceImpl.deleteCardData(cardId);
	}
	
	@RequestMapping(value = "/getCardsList", method = RequestMethod.GET)
	public @ResponseBody List<CreditCardDto> getCardsList(HttpServletResponse httpServletResponse) {
		return expenseServiceImpl.getCardsList();
	}
	
	@RequestMapping(value = "/checkDuplicateCardNumber", params = {"cardNumber"}, method = RequestMethod.GET)
	public @ResponseBody Boolean checkDuplicateCardNumber(@RequestParam String cardNumber) {
		return expenseServiceImpl.checkDuplicateCardNumber(cardNumber);
	}
	
	@RequestMapping(value = "/getReimbursementExpenseList", params = { "startIndex", "endIndex", "from", "to", "dateSelection",
			"multipleSearch", "departmentId", "categoryId", "country", "paymentMode", "paymentStatus", "creditCardId"}, method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> getReimbursementExpenseList(@RequestParam Integer startIndex,
			@RequestParam Integer endIndex, @RequestParam String from, @RequestParam String to,
			@RequestParam String dateSelection, @RequestParam String multipleSearch, @RequestParam Long departmentId, 
			@RequestParam Long categoryId, @RequestParam String country,@RequestParam String paymentMode,@RequestParam String paymentStatus,
			HttpServletResponse httpServletResponse,@RequestParam Long creditCardId) {

		return expenseServiceImpl.getReimbursementExpenseList(startIndex, endIndex, from, to, dateSelection, multipleSearch,
				departmentId, categoryId, country, paymentMode, paymentStatus, creditCardId);
	}
	
	@RequestMapping(value = "/savePaymentForm", method = RequestMethod.POST)
	public @ResponseBody void savePaymentForm(@RequestBody PaymentFormDto paymentFormDto) {
		expenseServiceImpl.savePaymentForm(paymentFormDto);
	}
	
	@RequestMapping(value = "/getExpensePaymentList", params = {"expenseId"}, method = RequestMethod.GET)
	public @ResponseBody List<PaymentFormDto> getExpensePaymentList(@RequestParam Long expenseId,HttpServletResponse httpServletResponse) {
		return expenseServiceImpl.getExpensePaymentList(expenseId);
	}
	
	@RequestMapping(value = "/payAllAtOnce", params = {"formIds"}, method = RequestMethod.POST)
	public @ResponseBody void payAllAtOnce(@RequestParam List<Long> formIds, @RequestBody PaymentFormDto paymentFormDto,
			HttpServletResponse httpServletResponse) {
		expenseServiceImpl.payAllAtOnce(formIds,paymentFormDto);

	}
	
	@RequestMapping(value = "/getCountryWiseExpenseReport", params = {"fromDate", "toDate"},  method = RequestMethod.GET)
		public @ResponseBody Map<String, Object> getCountryWiseExpenseReport(@RequestParam String fromDate, @RequestParam String toDate,HttpServletResponse httpServletResponse) {
					return expenseServiceImpl.getCountryWiseExpenseReport(fromDate, toDate);	
	}
	@RequestMapping(value="/getCountryWiseExpenseList", params = {"key","fromDate", "toDate"}, method = RequestMethod.GET)
	public @ResponseBody List<ExpenseFormDto> getCountryWiseExpenseList(@RequestParam String key,@RequestParam String fromDate, @RequestParam String toDate,HttpServletResponse httpServletResponse) {
				return expenseServiceImpl.getCountryWiseExpenseList(key, fromDate, toDate);
	}
	
	@RequestMapping(value="/getCountryCategoryWiseExpenseList",params= {"fromDate","toDate","country","category"}, method=RequestMethod.GET)
	public @ResponseBody List<ExpenseFormDto> getCountryCategoryWiseExpenseList(@RequestParam String fromDate, @RequestParam String toDate, 
			@RequestParam String country, @RequestParam String category){
				return expenseServiceImpl.getCountryCategoryWiseExpenseList(fromDate,toDate,country,category);
		
	}
	
	@RequestMapping(value = "/addCopiedExpensesList",params= {"formIds"}, method = RequestMethod.POST)
	public @ResponseBody void addCopiedExpensesList(@RequestParam List<Long> formIds,
			HttpServletResponse httpServletResponse ) {
	    expenseServiceImpl.addCopiedExpensesList(formIds);
	}
	
	@RequestMapping(value = "/deleteExpense", params = { "formId" }, method = RequestMethod.DELETE)
	public @ResponseBody void deleteExpense (@RequestParam Long formId) {
		
		expenseServiceImpl.deleteExpense(formId);
	}
	@RequestMapping(value = "/getSelectedSubCategories", params = {"categoryId"}, method = RequestMethod.GET)
	public @ResponseBody List<ExpenseSubCategoryDto> getSelectedSubCategories(@RequestParam List<Long> categoryId) {
	
		return expenseServiceImpl.getSelectedSubCategories(categoryId);
	}
}
