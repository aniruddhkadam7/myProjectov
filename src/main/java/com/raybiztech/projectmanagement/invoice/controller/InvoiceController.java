package com.raybiztech.projectmanagement.invoice.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.raybiztech.appraisals.properties.PropBean;
import com.raybiztech.projectmanagement.business.Milestone;
import com.raybiztech.projectmanagement.dto.MilestoneDTO;
import com.raybiztech.projectmanagement.dto.MilestonePeopleDTO;
import com.raybiztech.projectmanagement.dto.ProjectDTO;
import com.raybiztech.projectmanagement.dto.ProjectNumbersDTO;
import com.raybiztech.projectmanagement.exceptions.DuplicateProjectException;
import com.raybiztech.projectmanagement.invoice.business.Invoice;
import com.raybiztech.projectmanagement.invoice.business.InvoiceTracker;
import com.raybiztech.projectmanagement.invoice.business.Remittance;
import com.raybiztech.projectmanagement.invoice.dao.InvoiceDao;
import com.raybiztech.projectmanagement.invoice.dao.InvoiceDaoImpl;
import com.raybiztech.projectmanagement.invoice.dto.CountryAddressDTO;
import com.raybiztech.projectmanagement.invoice.dto.CountryLookUpDTO;
import com.raybiztech.projectmanagement.invoice.dto.InvoiceAuditDto;
import com.raybiztech.projectmanagement.invoice.dto.InvoiceDTO;
import com.raybiztech.projectmanagement.invoice.dto.InvoiceQueryDTO;
import com.raybiztech.projectmanagement.invoice.dto.InvoiceReminderLogDTO;
import com.raybiztech.projectmanagement.invoice.dto.LineItemDTO;
import com.raybiztech.projectmanagement.invoice.dto.PDFCrowdTokenDao;
import com.raybiztech.projectmanagement.invoice.dto.RemittanceDTO;
import com.raybiztech.projectmanagement.invoice.dto.ResourceRoleDto;
import com.raybiztech.projectmanagement.invoice.dto.TaxTypeLookupDTO;
import com.raybiztech.projectmanagement.invoice.exception.DuplicateCountryException;
import com.raybiztech.projectmanagement.invoice.lookup.CountryLookUp;
import com.raybiztech.projectmanagement.invoice.lookup.CurrencyToINR;
import com.raybiztech.projectmanagement.invoice.service.InvoiceService;

@Controller
@RequestMapping("/invoice")
public class InvoiceController {

	Logger logger = Logger.getLogger(InvoiceController.class);

	@Autowired
	PropBean propBean;

	private final InvoiceService invoiceServiceImpl;

	@Autowired
	public InvoiceController(InvoiceService invoiceServiceImpl) {
		this.invoiceServiceImpl = invoiceServiceImpl;
	}

	@RequestMapping(value = "/", params = { "projectid" }, method = RequestMethod.POST)
	public @ResponseBody void addInvoice(@RequestBody InvoiceDTO invoicedto,
			@RequestParam Long projectid) {
		//logger.warn("in backend controller");
		System.out.println("in addInvoiceController "+projectid);
		invoiceServiceImpl.addInvoice(invoicedto, projectid);
	}

	@RequestMapping(value = "/", method = RequestMethod.PUT)
	public @ResponseBody void updateInvoice(@RequestBody InvoiceDTO invoicedto) {

		invoiceServiceImpl.updateInvoice(invoicedto);
	}

	@RequestMapping(value = "/deleteInvoice", params = { "id" }, method = RequestMethod.DELETE)
	public @ResponseBody void deleteInvoice(@RequestParam Long id) {
		invoiceServiceImpl.deleteInvoice(id);
	}

	@RequestMapping(value = "/getInvoiceForDownload", params = { "invoiceId" }, method = RequestMethod.GET)
	public @ResponseBody InvoiceQueryDTO getInvoice(@RequestParam Long invoiceId) {

		return invoiceServiceImpl.getInvoice(invoiceId);
	}

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public @ResponseBody List<InvoiceDTO> getAllInvoices() {

		return invoiceServiceImpl.getAllInvoices();
	}

	@RequestMapping(value = "/lookup", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getAllLookUps() {

		return invoiceServiceImpl.getAllLookUps();
	}

	@RequestMapping(value = "/projectData", params = { "projectId" }, method = RequestMethod.GET)
	@ResponseBody
	public InvoiceDTO getInitProjectDetails(@RequestParam Long projectId) {

		return invoiceServiceImpl.getInitProjectDetails(projectId);

	}

	@RequestMapping(value = "/invoiceList", params = { "projectType","invoiceType", "status",
			"fromDate", "toDate", "multiText", "fromIndex", "toIndex",
			"invoiceDateSelection", "country", "intrnalOrNot" }, method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getInvoiceList(@RequestParam String projectType,
			@RequestParam String invoiceType,
			@RequestParam List<String> status, @RequestParam String fromDate,
			@RequestParam String toDate, @RequestParam String multiText,
			@RequestParam Integer fromIndex, @RequestParam Integer toIndex,
			@RequestParam String invoiceDateSelection,
			@RequestParam String country, @RequestParam Boolean intrnalOrNot) {
		return invoiceServiceImpl.getInvoiceList(projectType,invoiceType, status, fromDate,
				toDate, multiText, fromIndex, toIndex, invoiceDateSelection,
				country, intrnalOrNot);
	}

	// To get Invoice Report From Invoice Audit
	@RequestMapping(value = "/invoiceAuditList", params = { "projectType",
			"status", "fromDate", "toDate", "multiText", "fromIndex",
			"toIndex", "invoiceDateSelection", "country", "intrnalOrNot" }, method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> invoiceAuditList(
			@RequestParam String projectType,
			@RequestParam List<String> status, @RequestParam String fromDate,
			@RequestParam String toDate, @RequestParam String multiText,
			@RequestParam Integer fromIndex, @RequestParam Integer toIndex,
			@RequestParam String invoiceDateSelection,
			@RequestParam String country, @RequestParam Boolean intrnalOrNot) {
		return invoiceServiceImpl.getInvoiceAuditList(projectType, status,
				fromDate, toDate, multiText, fromIndex, toIndex,
				invoiceDateSelection, country, intrnalOrNot);
	}

	// To get Annual Report of Invoices From Invoice Audit
	@RequestMapping(value = "/yearlyInvoiceAudit", params = { "status",
			"fromDate", "toDate", "multiText", "country", "intrnalOrNot" }, method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> yearlyInvoiceAudit(
			@RequestParam List<String> status, @RequestParam String fromDate,
			@RequestParam String toDate, @RequestParam String multiText,
			@RequestParam String country, @RequestParam Boolean intrnalOrNot) {
		return invoiceServiceImpl.yearlyInvoiceAuditReport(status, fromDate,
				toDate, multiText, country, intrnalOrNot);
	}

	@RequestMapping(value = "/project", params = { "id", "startIndex",
			"endIndex" }, method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getProjectInvoices(@RequestParam Long id,
			@RequestParam Integer startIndex, @RequestParam Integer endIndex) {

		return invoiceServiceImpl.getProjectInvoices(id, startIndex, endIndex);
	}

	@RequestMapping(value = "/invoiceEdit", params = { "id" }, method = RequestMethod.GET)
	@ResponseBody
	public InvoiceDTO getEditInitInvoiceDetails(@RequestParam Long id) {

		return invoiceServiceImpl.getEditInitInvoiceDetails(id);
	}

	@RequestMapping(value = "/invoiceDetailsForDiscussion", params = { "id" }, method = RequestMethod.GET)
	@ResponseBody
	public InvoiceDTO getInvoiceDetailsForDiscussion(@RequestParam Long id) {

		return invoiceServiceImpl.getInvoiceDetailsForDiscussion(id);
	}

	@RequestMapping(value = "/pdf/", params = { "invoiceId" }, method = RequestMethod.GET)
	@ResponseBody
	public String generatepdf(@RequestParam Long invoiceId) {
		return invoiceServiceImpl.generatepdf(invoiceId);
	}

	@RequestMapping(value = "/downloadInvoice", params = { "filename", "id" }, method = RequestMethod.GET)
	public @ResponseBody void downLoadInvoice(HttpServletResponse response,
			String filename, Long id) throws IOException {

		invoiceServiceImpl.downLoadInvoice(filename, id, response);

	}

	@RequestMapping(value = "/email", params = { "invoiceId" }, method = RequestMethod.GET)
	@ResponseBody
	public void email(@RequestParam("invoiceId") Long invoiceId) {

		invoiceServiceImpl.emailInvoice(invoiceId);
	}

	@RequestMapping(value = "/getBillableForRetainer/{invoiceId}/{id}", method = RequestMethod.GET)
	@ResponseBody
	public List<MilestonePeopleDTO> getBillableForRetainer(
			@PathVariable("invoiceId") Long InvoiceId,
			@PathVariable("id") Long id) {

		return invoiceServiceImpl.getBillableForRetainer(InvoiceId, id);
	}

	@RequestMapping(value = "/CheckEmpIdExixts/{empId}", method = RequestMethod.GET)
	public @ResponseBody String checkEmpIdExixts(
			@PathVariable("empId") Long empId) {

		return invoiceServiceImpl.checkEmpIdExixts(empId);
	}

	@RequestMapping(value = "/CheckNoExixts/{InvoiceNumber}", method = RequestMethod.GET)
	public @ResponseBody Boolean checkInvoiceNoAlreadyExists(
			@PathVariable("InvoiceNumber") String InvoiceNumber) {

		return invoiceServiceImpl.checkInvoiceNoAlreadyExists(InvoiceNumber);
	}

	@RequestMapping(value = "/getwireTransferInstructions/{currency}/{bankName}", method = RequestMethod.GET)
	@ResponseBody
	public List<RemittanceDTO> getwireTransferInstructions(
			@PathVariable("currency") String currency,
			@PathVariable("bankName") String bankName) {
		return invoiceServiceImpl.getwireTransferInstructions(currency,
				bankName);

	}

	// changed type of getAllInvoicePercentageForMilestone method from Integer
	// to Double due to
	// milestone percentage field allows decimal values.

	@RequestMapping(value = "/getAllInvoicePercentageForMilestone", params = { "milestoneId" }, method = RequestMethod.GET)
	@ResponseBody
	public Double getAllInvoicePercentageForMilestone(
			@RequestParam Long milestoneId) {
		return invoiceServiceImpl
				.getAllInvoicePercentageForMilestone(milestoneId);

	}

	@RequestMapping(value = "/Audit", params = { "invoiceId" }, method = RequestMethod.GET)
	public @ResponseBody List<InvoiceAuditDto> getInvoiceAudit(
			@RequestParam Long invoiceId) {

		return invoiceServiceImpl.getInvoiceAudit(invoiceId);
	}

	@RequestMapping(value = "/saveAllTax", method = RequestMethod.POST)
	public @ResponseBody void saveAllTax(@RequestBody TaxTypeLookupDTO dto) {
		invoiceServiceImpl.saveAllTax(dto);
	}

	@RequestMapping(value = "/getAllTaxRelatedToCuntry/{country}", method = RequestMethod.GET)
	public @ResponseBody Set<TaxTypeLookupDTO> getAllTaxRelatedToCuntry(
			@PathVariable("country") String country) {
		return invoiceServiceImpl.getAllTaxRelatedToCuntry(country);
	}

	@RequestMapping(value = "/updateTax", method = RequestMethod.PUT)
	public @ResponseBody void updateTax(
			@RequestBody TaxTypeLookupDTO taxTypeLookupDTO) {

		invoiceServiceImpl.updateTax(taxTypeLookupDTO);
	}

	@RequestMapping(value = "deleteTax/{id}", method = RequestMethod.DELETE)
	public @ResponseBody void deleteTax(@PathVariable("id") Long id) {
		invoiceServiceImpl.deleteTax(id);
	}

	@RequestMapping(value = "/getAllInvoiceAmount", params = { "projectId" }, method = RequestMethod.GET)
	@ResponseBody
	public String getTotalInvoicesAmountofProject(@RequestParam Long projectId) {
		return invoiceServiceImpl.getallInvoiceAmountOfProject(projectId);

	}

	@RequestMapping(value = "/getAmountOfCR", params = { "crId" }, method = RequestMethod.GET)
	@ResponseBody
	public ProjectNumbersDTO getAmountOfCR(@RequestParam Long crId) {
		return invoiceServiceImpl.getAmountOfCR(crId);
	}

	/*
	 * @RequestMapping(value = "/getPDF", method = RequestMethod.POST) public
	 * 
	 * @ResponseBody void getPDF(@RequestBody String postString,
	 * HttpServletResponse httpServletResponse) throws IOException {
	 * invoiceServiceImpl.getPDF(postString);
	 * httpServletResponse.setStatus(HttpServletResponse.SC_OK); }
	 */

	@RequestMapping(value = "/getInvoicePDF", method = RequestMethod.POST)
	public @ResponseBody void getInvoicePDF(@RequestBody String postString,
			HttpServletResponse httpServletResponse,HttpServletRequest request) throws Exception {
		invoiceServiceImpl.getInvoicePDF(postString,request);
		httpServletResponse.setStatus(HttpServletResponse.SC_OK);
	}

	@RequestMapping(value = "/invoiceTracker", method = RequestMethod.POST)
	public @ResponseBody void trackInvoice(@RequestParam Long invoiceId,
			@RequestBody String postString,HttpServletRequest request) {
		invoiceServiceImpl.trackInvoice(invoiceId, postString,request);
	}

	@RequestMapping(value = "/getInvoiceVersions", method = RequestMethod.GET)
	public @ResponseBody List<InvoiceTracker> getInvoiceVersions(
			@RequestParam Long invoiceId) {
		return invoiceServiceImpl.getInvoiceVersions(invoiceId);
	}

	@RequestMapping(value = "/updatePDFCrowd", method = RequestMethod.PUT)
	public @ResponseBody void updatePDFCrowd(
			@RequestBody PDFCrowdTokenDao crowdTokenDao) {
		invoiceServiceImpl.updatePDFCrowd(crowdTokenDao);
	}

	@RequestMapping(value = "/getPDFCrowd", method = RequestMethod.GET)
	public @ResponseBody PDFCrowdTokenDao getPDFCrowd() {

		return invoiceServiceImpl.getPDFCrowd();
	}

	@RequestMapping(value = "/getByRole", method = RequestMethod.POST)
	public @ResponseBody Set<ResourceRoleDto> getByRole(
			@RequestBody InvoiceDTO invoiceDTO) {

		Set<LineItemDTO> item = invoiceDTO.getLineitem();
		return invoiceServiceImpl.getByRole(item);
	}

	@RequestMapping(value = "/getCountryLookup", method = RequestMethod.GET)
	@ResponseBody
	public List<CountryLookUp> getCountryLookup() {

		return invoiceServiceImpl.getCountryLookup();
	}

	@RequestMapping(value = "/addCountry", method = RequestMethod.POST)
	public @ResponseBody void addCountry(@RequestBody CountryLookUpDTO dto,
			HttpServletResponse httpServletResponse) {
		try {
			invoiceServiceImpl.addCountry(dto);
		} catch (DuplicateCountryException e) {
			httpServletResponse
					.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);

		}
	}

	@RequestMapping(value = "/deleteCountry", params = { "id" }, method = RequestMethod.DELETE)
	public @ResponseBody void deleteCountry(@RequestParam Integer id) {
		invoiceServiceImpl.deleteCountry(id);
	}

	@RequestMapping(value = "/deletePartiallyReceivedAmount", params = {
			"amountId", "invoiceId" }, method = RequestMethod.DELETE)
	public @ResponseBody void deletePartiallyReceivedAmount(
			@RequestParam Long amountId, @RequestParam Long invoiceId) {

		invoiceServiceImpl.deletePartiallyReceivedAmount(amountId, invoiceId);
	}

	// creation of invoice serial number

	@RequestMapping(value = "/invoiceNumber/{country}/{clientName}", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, String> getInvoiceNumber(
			@PathVariable("country") String country,
			@PathVariable("clientName") String clientName) {
		return invoiceServiceImpl.createInvoiceNumber(clientName, country);
	}

	// creation of invoice number

	@RequestMapping(value = "/getInvoiceSerialNumber/{country}/{proformaInvoiceFlag}", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, String> getInvoiceSerialNumber(
			@PathVariable("country") String country,
			@PathVariable("proformaInvoiceFlag") String proformaInvoiceFlag) {
		return invoiceServiceImpl.createInvoiceSerialNumber(country,proformaInvoiceFlag);
	}

	@RequestMapping(value = "/checkInvoiceNumber/{client}/{referenceNumber}/{country}", method = RequestMethod.GET)
	@ResponseBody
	private Boolean checkInvoiceNumber(@PathVariable("client") String client,
			@PathVariable("referenceNumber") String referenceNumber,
			@PathVariable("country") String country) {
		return invoiceServiceImpl.checkInvoiceNumber(client, referenceNumber,
				country);
	}

	@RequestMapping(value = "/checkInvoiceNumberExits/{invoiceReferenceNumber}/{country}/{proformaInvoiceFlag}", method = RequestMethod.GET)
	@ResponseBody
	private Boolean checkInvoiceNumberExits(
			@PathVariable("invoiceReferenceNumber") String invoiceReferenceNumber,
			@PathVariable("country") String country,
			@PathVariable("proformaInvoiceFlag") String proformaInvoiceFlag) {
		return invoiceServiceImpl.checkInvoiceNumberExits(
				invoiceReferenceNumber, country, proformaInvoiceFlag);
	}

	@RequestMapping(value = "/getInvoiceStatusHistoryById", params = { "invoiceId" }, method = RequestMethod.GET)
	public @ResponseBody List<InvoiceAuditDto> getInvoiceStatusHistoryById(
			@RequestParam Long invoiceId) {
		return invoiceServiceImpl.getInvoiceStatusHistoryById(invoiceId);
	}

	// exporting invoice list
	@RequestMapping(value = "/exportInvoiceList", params = { "projectType",
			"status", "fromDate", "toDate", "multiText",
			"invoiceDateSelection", "country", "intrnalOrNot" }, method = RequestMethod.GET)
	@ResponseBody
	public void exportInvoiceList(@RequestParam String projectType,
			@RequestParam List<String> status, @RequestParam String fromDate,
			@RequestParam String toDate, @RequestParam String multiText,
			@RequestParam String invoiceDateSelection,
			@RequestParam String country, @RequestParam Boolean intrnalOrNot,
			HttpServletResponse httpServletResponse) throws IOException {
		httpServletResponse.setContentType("text/csv");
		httpServletResponse.setHeader("Content-Disposition",
				"attachment; filename=\"InvoiceList.csv\"");
		ByteArrayOutputStream os = invoiceServiceImpl.exportInvoiceList(
				projectType, status, fromDate, toDate, multiText,
				invoiceDateSelection, country, intrnalOrNot);
		httpServletResponse.getOutputStream().write(os.toByteArray());

	}

	// To Export Project Financial List
	@RequestMapping(value = "/projectFinancialList", params = { "fromDate",
			"toDate", }, method = RequestMethod.GET)
	@ResponseBody
	public void projectFinancialList(@RequestParam String fromDate,
			@RequestParam String toDate, HttpServletResponse httpServletResponse)
			throws IOException {

		httpServletResponse.setContentType("text/csv");
		httpServletResponse.setHeader("Content-Disposition",
				"attachment; filename=\"Project Financial List.csv\"");
		ByteArrayOutputStream os = invoiceServiceImpl
				.exportProjectFinancialList(fromDate, toDate);
		httpServletResponse.getOutputStream().write(os.toByteArray());

	}

	@RequestMapping(value = "/getInvoiceSummary", params = { "invoiceId" }, method = RequestMethod.GET)
	public @ResponseBody InvoiceDTO getInvoiceSummary(
			@RequestParam Long invoiceId) {
		return invoiceServiceImpl.getInvoiceSummary(invoiceId);
	}

	@RequestMapping(value = "/invoiceReminderlog", method = RequestMethod.POST)
	public @ResponseBody void invoiceReminderlog(
			@RequestBody InvoiceReminderLogDTO dto ,HttpServletRequest request) {
		invoiceServiceImpl.invoiceReminderlog(dto,request);
	}

	@RequestMapping(value = "/getReminderLogs", params = { "invoiceId" }, method = RequestMethod.GET)
	public @ResponseBody List<InvoiceReminderLogDTO> getReminderLogs(
			@RequestParam Long invoiceId) {
		return invoiceServiceImpl.getReminderLogs(invoiceId);
	}

	@RequestMapping(value = "/getReminderDescription", method = RequestMethod.GET)
	public @ResponseBody String getReminderDescription() {

		return invoiceServiceImpl.getReminderDescription();
	}

	@RequestMapping(value = "/allowToAdjustInvoice", params = { "invoiceId" }, method = RequestMethod.PUT)
	public @ResponseBody void allowToAdjustInvoice(@RequestParam Long invoiceId) {
		invoiceServiceImpl.allowToAdjustInvoice(invoiceId);
	}

	@RequestMapping(value = "/restrictToAdjustInvoice", params = { "invoiceId" }, method = RequestMethod.PUT)
	public @ResponseBody void restrictToAdjustInvoice(
			@RequestParam Long invoiceId) {
		invoiceServiceImpl.restrictToAdjustInvoice(invoiceId);
	}

	@RequestMapping(value = "/getInitData", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> getInitData() {
		return invoiceServiceImpl.getInitData();
	}

	@RequestMapping(value = "/addInrToCurrency", params = { "currenyType",
			"inrAmount" }, method = RequestMethod.POST)
	public @ResponseBody void addInrToCurrency(
			@RequestParam String currenyType, @RequestParam Long inrAmount) {
		invoiceServiceImpl.addInrToCurrency(currenyType, inrAmount);
	}

	@RequestMapping(value = "/updateInrToCurrency", params = { "id", "amount" }, method = RequestMethod.PUT)
	public @ResponseBody void updateInrToCurrency(@RequestParam Long id,
			@RequestParam Long amount) {
		invoiceServiceImpl.updateInrToCurrency(id, amount);
	}

	@RequestMapping(value = "/latestInvoiceVersion", params = { "invoiceId" }, method = RequestMethod.GET)
	@ResponseBody
	public InvoiceQueryDTO getLatestInvoiceVersion(@RequestParam Long invoiceId) {
		return invoiceServiceImpl.getLatestInvoiceVersion(invoiceId);
	}

	@RequestMapping(value = "/addAddress", method = RequestMethod.POST)
	public @ResponseBody void addAddress(@RequestBody CountryAddressDTO dto) {
		invoiceServiceImpl.addAddress(dto);
	}

	@RequestMapping(value = "/getAddressDetailsList", method = RequestMethod.GET)
	@ResponseBody
	public List<CountryAddressDTO> getAddressDetailsList() {
		return invoiceServiceImpl.getAddressDetailsList();
	}

	@RequestMapping(value = "/updateAddress", method = RequestMethod.PUT)
	@ResponseBody
	public void updateAddressDetails(
			@RequestBody CountryAddressDTO countryAddressDTO) {
		invoiceServiceImpl.updateAddressDetails(countryAddressDTO);
	}

	@RequestMapping(value = "/deleteAddressDetails", params = { "addressId" }, method = RequestMethod.DELETE)
	public @ResponseBody void deleteAddress(@RequestParam("addressId") Long addressId) {
		invoiceServiceImpl.deleteAddress(addressId);
	}
	
	@RequestMapping(value = "/getMilestone", params = { "milestoneId"}, method = RequestMethod.GET)
	@ResponseBody
	public MilestoneDTO getMilestone(@RequestParam Long milestoneId){
		System.out.println("In getting milestone "+ milestoneId);
		
	
		return invoiceServiceImpl.getMilestone(milestoneId);
	}
	
	
	@RequestMapping(value = "/getinvoiceDetails", params = { "invoiceId"}, method = RequestMethod.GET)
	@ResponseBody
	public InvoiceDTO getinvoiceDetails(@RequestParam long invoiceId){
		System.out.println("In getting getinvoiceDetails "+ invoiceId);
		return invoiceServiceImpl.getInvoiceDetails(invoiceId);
	}
	

	
	@RequestMapping(value = "/isProformaRefrece", params = { "invoiceId"}, method = RequestMethod.GET)
	@ResponseBody
	public Boolean isProformaRefrece(@RequestParam long invoiceId){
		System.out.println("In getting getinvoiceDetails "+ invoiceId);
		Boolean flag = invoiceServiceImpl.getInvoiceRef(invoiceId);
		
		return flag;
	}


	/*
	 * @RequestMapping(value ="/getTotalPendingAmount",method
	 * =RequestMethod.GET)
	 * 
	 * @ResponseBody public Map<String, Object>
	 * getTotalPendingAmount(@RequestParam String projectType,
	 * 
	 * @RequestParam List<String> status, @RequestParam String fromDate,
	 * 
	 * @RequestParam String toDate, @RequestParam String multiText,
	 * 
	 * @RequestParam String invoiceDateSelection,
	 * 
	 * @RequestParam String country) { return
	 * invoiceServiceImpl.getTotalPendingAmount(projectType, status, fromDate,
	 * toDate, multiText, invoiceDateSelection, country);
	 * 
	 * }
	 */

	/*
	 * public static void main(String[] args) throws Exception {
	 * 
	 * String command = "wkhtmltopdf -T 10mm" +
	 * " --header-html /home/user/Desktop/wildfly-8.0.0.Final/standalone/deployments/hrm-newUI.war/fixedbidinvoices/header.html"
	 * + "-B 10mm " +
	 * "--footer-html /home/user/Desktop/wildfly-8.0.0.Final/standalone/deployments/hrm-newUI.war/fixedbidinvoices/footer.html"
	 * +
	 * " /home/user/Desktop/wildfly-8.0.0.Final/standalone/deployments/hrm-newUI.war/fixedbidinvoices/FixedBidInvoice.html"
	 * +
	 * " /home/user/Desktop/wildfly-8.0.0.Final/standalone/deployments/hrm-newUI.war/fixedbidinvoices/TEST.pdf"
	 * ;
	 * 
	 * Process process = Runtime .getRuntime() .exec(
	 * "wkhtmltopdf  -L 0mm -R 0mm -T 15mm --header-html /home/user/Desktop/wildfly-8.0.0.Final/standalone/deployments/hrm-newUI.war/fixedbidinvoices/header.html -B 20mm --footer-html /home/user/Desktop/wildfly-8.0.0.Final/standalone/deployments/hrm-newUI.war/fixedbidinvoices/footer.html --header-spacing 5 /home/user/Desktop/wildfly-8.0.0.Final/standalone/deployments/hrm-newUI.war/fixedbidinvoices/body.html /home/user/Desktop/wildfly-8.0.0.Final/standalone/deployments/hrm-newUI.war/fixedbidinvoices/FixedBidInvoiceVenk.pdf"
	 * ); process.waitFor();
	 * 
	 * }
	 */

	/* Complete Invoice Audit log */

	@RequestMapping(value = "/invoiceAuditLog", params = { "startdate",
			"enddate", "firstIndex", "endIndex", "invoiceDatePeriod" }, method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> InvoiceAuditLog(
			@RequestParam String startdate, @RequestParam String enddate,
			@RequestParam Integer firstIndex, @RequestParam Integer endIndex,
			@RequestParam String invoiceDatePeriod) {

		return invoiceServiceImpl.invoiceAuditLog(startdate, enddate,
				firstIndex, endIndex, invoiceDatePeriod);

	}

	/*
	 * @RequestMapping(value = "/reOpenInvoice", params = { "invoiceId" },
	 * method = RequestMethod.GET)
	 * 
	 * @ResponseBody public void reOpenInvoice(@RequestParam Long invoiceId) {
	 * invoiceServiceImpl.reOpenInvoice(invoiceId); }
	 */

	@RequestMapping(value = "/getInvoicesOfMilestone", params = { "milestoneId" }, method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> getInvoicesOfMilestone(
			@RequestParam Long milestoneId) {
		return invoiceServiceImpl.getInvoicesOfMilestone(milestoneId);
	}

	@RequestMapping(value = "/checkMilestoneExits", params = { "milestoneid" }, method = RequestMethod.GET)
	public @ResponseBody Boolean checkMilestoneExits(
			@RequestParam Long milestoneid) {
		return invoiceServiceImpl.checkMilestoneExits(milestoneid);
	}
	
	@RequestMapping(value = "/clientInvoiceAudit", params = { 
			"fromDate", "toDate","multiText","selectionStatus","pendingAmountFlag"}, method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> ClientInvoiceAudit(
			 @RequestParam String fromDate,
			@RequestParam String toDate,@RequestParam String multiText,@RequestParam String selectionStatus,@RequestParam Boolean pendingAmountFlag
			) {
		return invoiceServiceImpl.clientInvoiceAuditReport(fromDate,
				toDate,multiText,selectionStatus ,pendingAmountFlag);
	}
	
	// To get Annual Report of Invoices From Invoice Audit
		@RequestMapping(value = "/mopnthlyInvoiceAudit", params = { "monthName",
				"year", "invoiceStatus" ,"country"}, method = RequestMethod.GET)
		@ResponseBody
		public Map<String, Object> mopnthlyInvoiceAudit(
				@RequestParam String monthName, @RequestParam String year,
				@RequestParam String invoiceStatus ,@RequestParam String country) {
			return invoiceServiceImpl.mopnthlyInvoiceAudit(monthName, year,
					invoiceStatus,country);
		}
		
		@RequestMapping(value = "/clientsProjectsAudit", params = { "clientId",
				"displayStartDate", "displayEndDate" ,"pendingAmountFlag"}, method = RequestMethod.GET)
		@ResponseBody
		public Map<String, Object> clientsProjectsAudit(
				@RequestParam Long clientId, @RequestParam String displayStartDate,
				@RequestParam String displayEndDate ,@RequestParam Boolean pendingAmountFlag) {
			return invoiceServiceImpl.clientsProjectsAudit(clientId, displayStartDate,
					displayEndDate ,pendingAmountFlag);
		}
		
		@RequestMapping(value = "/clientsProjectInvoicesAudit", params = { "projectId",
				"displayStartDate", "displayEndDate" ,"pendingAmountFlag"}, method = RequestMethod.GET)
		@ResponseBody
		public Map<String, Object> clientsProjectInvoicesAudit(
				@RequestParam Long projectId, @RequestParam String displayStartDate,
				@RequestParam String displayEndDate ,@RequestParam Boolean pendingAmountFlag) {
			return invoiceServiceImpl.clientsProjectInvoicesAudit(projectId, displayStartDate,
					displayEndDate ,pendingAmountFlag);
		}
		

}
