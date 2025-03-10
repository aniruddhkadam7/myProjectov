package com.raybiztech.projectmanagement.invoice.dao;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.dao.DAO;
import com.raybiztech.date.Date;
import com.raybiztech.projectmanagement.business.ChangeRequest;
import com.raybiztech.projectmanagement.business.Client;
import com.raybiztech.projectmanagement.business.Milestone;
import com.raybiztech.projectmanagement.business.Project;
import com.raybiztech.projectmanagement.business.ProjectNumbers;
import com.raybiztech.projectmanagement.invoice.business.CountryAddress;
import com.raybiztech.projectmanagement.invoice.business.Invoice;
import com.raybiztech.projectmanagement.invoice.business.InvoiceAudit;
import com.raybiztech.projectmanagement.invoice.business.InvoiceReminderLog;
import com.raybiztech.projectmanagement.invoice.business.InvoiceSummary;
import com.raybiztech.projectmanagement.invoice.business.InvoiceTracker;
import com.raybiztech.projectmanagement.invoice.business.Remittance;
import com.raybiztech.projectmanagement.invoice.dto.CountryAddressDTO;
import com.raybiztech.projectmanagement.invoice.lookup.TaxTypeLookup;

public interface InvoiceDao extends DAO {

	Map<String, Object> getAllLookUps();

	Map<String, Object> getInvoiceList(String projectType, List<String> status,
			String fromDate, String toDate, String multiText,
			Integer fromIndex, Integer toIndex, String datePeriod,
			Set<Long> projectids, String invoiceCountry ,Boolean intrnalOrNot);
	
	Map<String, Object> getListOfInvoice(String projectType, String invoiceType, List<String> status,
			String fromDate, String toDate, String multiText,
			Integer fromIndex, Integer toIndex, String datePeriod,
			Set<Long> projectids, String invoiceCountry ,Boolean intrnalOrNot);

	Map<String, Object> getInvoiceAuditList(List<String> status, Date fromDate,
			Date toDate, String multiText, String datePeriod,
			String invoiceCountry);

	Map<String, Object> getInvoiceAuditReport(List<String> status,
			String fromDate, String toDate, String multiText,String datePeriod,
			 String invoiceCountry, Boolean inintrnalOrNot);
	

	int getInvoicesCountOf(Long clientId);

	Map<String, Object> getProjectInvoices(Long projectId, Integer startIndex,
			Integer endIndex);

	List<Remittance> remitanceList(String currencyType, String bankName);

	List<InvoiceAudit> getInvoiceAudit(Long addInvoiceAudit);

	public Set<TaxTypeLookup> getAllTaxRelatedToCuntry(String country);

	public Set<TaxTypeLookup> getAllTaxes();

	public List<Invoice> getAllInvoicesAmountForProject(Project projectId);

	public ProjectNumbers getAmountofCr(ChangeRequest changeRequestid);

	public Double risedPercentage(Long id, Long invoiceId);

	public ProjectNumbers getProjectNumbers(Project project);

	public InvoiceSummary getSummaryOfChangeRequest(Project project,
			Client client, ChangeRequest changeRequest);

	public InvoiceSummary getInvoiceSummary(Project project, Client client);

	List<Project> getAllProjects();

	List<InvoiceSummary> getInvoiceSummaryList(Project project, Client client);

	List<Invoice> getInvoicesInCollection(List<Long> collection);

	List<Project> getFixedAndRetainerTypeProjects();

	List<Invoice> getinvoiceForMilestone(Milestone milestone);

	List<Milestone> getclosedBillableMilestoneOfProject(Project project);

	List<ChangeRequest> getChangeRequestOfProject(Long projectId);

	List<Milestone> getMilestoneOfCR(ChangeRequest changeRequest);

	String getLatestInvoiceNumberForClient(Client client,String country);

	Boolean checkInvoiceNumber(Client client, String referenceNumber ,String country);
	
	Boolean checkInvoiceNumberExits(String invoiceReferenceNumber ,String country, String proformaInvoiceFlag);

	// Used to get employee allocated projects as well as employee reportee
	// projects
	List<Long> getProjectsFor(Employee loggedEmployee, List<Long> managerList);

	// Used in Invoice Quartz

	List<Invoice> checkInvoiceStatus();

	Long getInvoiceTrackerVersion(Long invoiceId);

	List<InvoiceTracker> getInvoiceTrackers(Long invoiceId);

	List<InvoiceReminderLog> getReminderLogs(String invoiceId);

	List<Invoice> getInvoicesRaisedBetween(Date fromDate, Date toDate);

	public InvoiceTracker latestInvoiceVersion(Long invoiceId);
	
	void addAddressDetails(CountryAddress dto);
	
	List<CountryAddress> getAddressDetailsList();

	Map<String, Object> invoiceAuditLog(Date fromDate, Date toDate,
			Integer firstIndex, Integer endIndex);
	
	
	String getLatestInvoiceNumberForCountry(String country);
	
	
	List<Invoice> getInvoicesUnderMilestone(Long projectId);
	
	List<Invoice> getRaisedInvoices(Long milestoneId);
	
	Boolean checkMilestoneExits(Long  milestoneid);
	
	
	
	Map<String, Object> getClientsAuditReport(
			String fromDate, String toDate ,String multiText ,String selectionStatus ,Boolean pendingAmountFlag );
	
	Map<String, Object> getMonthlyAuditReport(
			String fromDate, String toDate ,String  invoiceStatus ,String country);
	
	Map<String, Object> getClientsProjectAuditReport(Long clientId,String formateddStringstartdate, String formattedEnddate, Boolean pendingAmountFlag) ;
	
	Map<String, Object> getClientProjectInvoice(Long projectId,String formateddStringstartdate, String formattedEnddate , Boolean pendingAmountFlag);

			
	
	String getInvoicePattern();

	String getLatestProformaInvoice(String country);

	List<Invoice> getInvoiceRefrence(long invoiceId);

	List<InvoiceAudit> getRefrenceInvoice(Long invoiceNum);

	
	
	
	
	

}
