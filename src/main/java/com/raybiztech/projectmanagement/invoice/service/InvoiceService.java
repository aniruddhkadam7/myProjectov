package com.raybiztech.projectmanagement.invoice.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.raybiztech.projectmanagement.business.Milestone;
import com.raybiztech.projectmanagement.dto.MilestoneDTO;
import com.raybiztech.projectmanagement.dto.MilestonePeopleDTO;
import com.raybiztech.projectmanagement.dto.ProjectNumbersDTO;
import com.raybiztech.projectmanagement.invoice.business.Invoice;
import com.raybiztech.projectmanagement.invoice.business.InvoiceTracker;
import com.raybiztech.projectmanagement.invoice.business.Remittance;
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
import com.raybiztech.projectmanagement.invoice.lookup.CountryLookUp;
import com.raybiztech.projectmanagement.invoice.lookup.CurrencyToINR;

public interface InvoiceService {

	/**
	 * This method is used to raise invoice *
	 *
	 * @param Invoicedto
	 */
	void addInvoice(InvoiceDTO invoicedto, Long id);

	/**
	 * This method is used to get all invoices
	 */
	List<InvoiceDTO> getAllInvoices();

	/**
	 * This method is used to update invoice *
	 *
	 * @param Invoicedto
	 */
	void updateInvoice(InvoiceDTO invoicedto);

	/**
	 * This method is used to delete invoice *
	 *
	 * @param Id
	 */
	void deleteInvoice(Long Id);

	/**
	 * This method is used to email invoice *
	 *
	 * @param Invoicedto
	 */
	InvoiceQueryDTO getInvoice(Long invoiceId);

	Map<String, Object> getAllLookUps();

	InvoiceDTO getInitProjectDetails(Long id);

	Map<String, Object> getInvoiceList(String projectType,String invoiceType, List<String> status,
			String fromDate, String toDate, String multiText,
			Integer fromIndex, Integer toIndex, String datePeriod,
			String invoiceCountry,Boolean intrnalOrNot);

	Map<String, Object> getInvoiceAuditList(String projectType,
			List<String> status, String fromDate, String toDate,
			String multiText, Integer fromIndex, Integer toIndex,
			String datePeriod, String invoiceCountry, Boolean intrnalOrNot);

	Map<String, Object> yearlyInvoiceAuditReport(List<String> status,
			String fromDate, String toDate, String multiText,
			 String invoiceCountry, Boolean intrnalOrNot);

	Map<String, Object> getProjectInvoices(Long id, Integer startIndex,
			Integer endIndex);

	InvoiceDTO getEditInitInvoiceDetails(Long id);

	InvoiceDTO getInvoiceDetailsForDiscussion(Long id);

	String generatepdf(Long InvoiceId);

	void emailInvoice(Long invoiceId);

	void downLoadInvoice(String filename, Long id, HttpServletResponse response)
			throws IOException;

	List<MilestonePeopleDTO> getBillableForRetainer(Long InvoiceId, Long id);

	Boolean checkInvoiceNoAlreadyExists(String invoiceNumber);

	String checkEmpIdExixts(Long empid);

	public List<RemittanceDTO> getwireTransferInstructions(String currency,
			String bankName);

	void addInvoiceAudit(Long invoiceId, String persistType);

	List<InvoiceAuditDto> getInvoiceAudit(Long id);

	public Double getAllInvoicePercentageForMilestone(Long milestoneId);

	public void saveAllTax(TaxTypeLookupDTO dto);

	public Set<TaxTypeLookupDTO> getAllTaxRelatedToCuntry(String country);

	public void updateTax(TaxTypeLookupDTO dto);

	public void deleteTax(Long id);

	public String getallInvoiceAmountOfProject(Long projectId);

	public ProjectNumbersDTO getAmountOfCR(Long crId);

	Boolean checkInvoiceNumberAlreadyExists(String invoiceNumber);

	public void updatePDFCrowd(PDFCrowdTokenDao crowdTokenDao);

	public void getPDF(String html) throws IOException;

	public PDFCrowdTokenDao getPDFCrowd();

	public Set<ResourceRoleDto> getByRole(Set<LineItemDTO> items);

	public List<CountryLookUp> getCountryLookup();

	void addCountry(CountryLookUpDTO countryLookUpDTO);

	void deleteCountry(Integer id);

	void deletePartiallyReceivedAmount(Long amount, Long invoiceId);

	public Map<String, String> createInvoiceNumber(String clientName,
			String country);
	
	public Map<String, String> createInvoiceSerialNumber(
			String country, String proformaInvoiceFlag);

	Boolean checkInvoiceNumber(String client, String referenceNumber,String country);
	
	Boolean checkInvoiceNumberExits( String invoiceReferenceNumber,String country, String proformaInvoiceFlag);

	List<InvoiceAuditDto> getInvoiceStatusHistoryById(Long id);

	ByteArrayOutputStream exportInvoiceList(String projectType,
			List<String> status, String fromDate, String toDate,
			String multiText, String datePeriod, String invoiceCountry,Boolean intrnalOrNot)
			throws IOException;

	InvoiceDTO getInvoiceSummary(Long invoiceId);

	void trackInvoice(Long invoiceId, String dataString,HttpServletRequest request);

	List<InvoiceTracker> getInvoiceVersions(Long invoiceId);

	void invoiceReminderlog(InvoiceReminderLogDTO dto,HttpServletRequest request);

	List<InvoiceReminderLogDTO> getReminderLogs(Long invoiceId);

	public void getInvoicePDF(String postString,HttpServletRequest request) throws Exception;

	String getReminderDescription();

	public void allowToAdjustInvoice(Long invoiceId);

	public void restrictToAdjustInvoice(Long invoiceId);

	public Map<String, Object> getInitData();

	public void addInrToCurrency(String currencyType,Long inrAmount);

	public void updateInrToCurrency(Long id, Long amount);

	ByteArrayOutputStream exportProjectFinancialList(String fromDate,
			String toDate) throws IOException;

	public InvoiceQueryDTO getLatestInvoiceVersion(Long invoiceId);
	
	void addAddress(CountryAddressDTO dto);
	
	public List<CountryAddressDTO> getAddressDetailsList();
	
	void updateAddressDetails(CountryAddressDTO countryAddressDTO);
	
	void deleteAddress(Long addressId);

	Map<String, Object> invoiceAuditLog(String startdate, String enddate,
			Integer firstIndex, Integer endIndex,
			String invoiceDatePeriod);
	
	/*public Map<String, Object> getTotalPendingAmount(String projectType, List<String> status,
			String fromDate, String toDate, String multiText,
			String datePeriod,
			String invoiceCountry);*/
	
	/*void  reOpenInvoice(Long invoiceId);
*/
	
	Map<String, Object> getInvoicesOfMilestone(Long milestoneId);
	
	Boolean checkMilestoneExits(Long milestoneid);
	
	Map<String, Object> clientInvoiceAuditReport(
			String fromDate, String toDate ,String multiText ,String selectionStatus, Boolean pendingAmountFlag);
	

	Map<String, Object> mopnthlyInvoiceAudit(
			String monthName, String year ,String invoiceStatus,String country);
	
	Map<String, Object> clientsProjectsAudit(
			Long clientId, String displayStartDate ,String displayEndDate ,Boolean pendingAmountFlag);
	
	Map<String, Object> clientsProjectInvoicesAudit(
			Long projectId, String displayStartDate ,String displayEndDate, Boolean pendingAmountFlag);

	MilestoneDTO getMilestone(Long milestoneId);

	InvoiceDTO getInvoiceDetails(long invoiceId);

	Boolean getInvoiceRef(long invoiceId);


	

}
