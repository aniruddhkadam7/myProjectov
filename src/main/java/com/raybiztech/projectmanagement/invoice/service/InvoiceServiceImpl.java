package com.raybiztech.projectmanagement.invoice.service;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.Console;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Level;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRException;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.hibernate.envers.query.criteria.AuditDisjunction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pdfcrowd.PdfcrowdError;
import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.dao.DAO;
import com.raybiztech.appraisals.dao.DAOImpl;
import com.raybiztech.appraisals.properties.PropBean;
import com.raybiztech.appraisals.security.utils.SecurityUtils;
import com.raybiztech.date.Date;
import com.raybiztech.date.DateRange;
import com.raybiztech.date.Second;
import com.raybiztech.leavemanagement.utils.LeaveManagementUtils;
import com.raybiztech.mailtemplates.dao.MailTemplatesDao;
import com.raybiztech.mailtemplates.util.MailContentParser;
import com.raybiztech.payslip.utility.AES256Encryption;
import com.raybiztech.projectmanagement.builder.MilstoneBuilder;
import com.raybiztech.projectmanagement.builder.ProjectNumbersBuilder;
import com.raybiztech.projectmanagement.business.ChangeRequest;
import com.raybiztech.projectmanagement.business.Client;
import com.raybiztech.projectmanagement.business.Milestone;
import com.raybiztech.projectmanagement.business.Project;
import com.raybiztech.projectmanagement.business.ProjectNumbers;
import com.raybiztech.projectmanagement.business.ProjectType;
import com.raybiztech.projectmanagement.dao.ResourceManagementDAO;
import com.raybiztech.projectmanagement.dto.MilestoneDTO;
import com.raybiztech.projectmanagement.dto.MilestonePeopleDTO;
import com.raybiztech.projectmanagement.dto.ProjectNumbersDTO;
import com.raybiztech.projectmanagement.dto.ReportDTO;
import com.raybiztech.projectmanagement.exceptions.DuplicateProjectException;
import com.raybiztech.projectmanagement.invoice.MailAcknowledgement.InvoiceReminderAcknowledgement;
import com.raybiztech.projectmanagement.invoice.builder.CountryAddressBuilder;
import com.raybiztech.projectmanagement.invoice.builder.InvoiceBuilder;
import com.raybiztech.projectmanagement.invoice.builder.LineItemBuilder;
import com.raybiztech.projectmanagement.invoice.builder.RemittanceBuilder;
import com.raybiztech.projectmanagement.invoice.builder.TaxBuilder;
import com.raybiztech.projectmanagement.invoice.business.CountryAddress;
import com.raybiztech.projectmanagement.invoice.business.Invoice;
import com.raybiztech.projectmanagement.invoice.business.InvoiceAudit;
import com.raybiztech.projectmanagement.invoice.business.InvoiceReminderLog;
import com.raybiztech.projectmanagement.invoice.business.InvoiceTracker;
import com.raybiztech.projectmanagement.invoice.business.LineItem;
import com.raybiztech.projectmanagement.invoice.business.PDFCrowdToken;
import com.raybiztech.projectmanagement.invoice.business.ReceivedInvoiceAmount;
import com.raybiztech.projectmanagement.invoice.business.Remittance;
import com.raybiztech.projectmanagement.invoice.business.Tax;
import com.raybiztech.projectmanagement.invoice.dao.InvoiceDao;
import com.raybiztech.projectmanagement.invoice.dto.CountryAddressDTO;
import com.raybiztech.projectmanagement.invoice.dto.CountryLookUpDTO;
import com.raybiztech.projectmanagement.invoice.dto.InvoiceAuditComparator;
import com.raybiztech.projectmanagement.invoice.dto.InvoiceAuditDto;
import com.raybiztech.projectmanagement.invoice.dto.InvoiceAuditReportDto;
import com.raybiztech.projectmanagement.invoice.dto.InvoiceDTO;
import com.raybiztech.projectmanagement.invoice.dto.InvoiceQueryDTO;
import com.raybiztech.projectmanagement.invoice.dto.InvoiceReminderLogDTO;
import com.raybiztech.projectmanagement.invoice.dto.LineItemDTO;
import com.raybiztech.projectmanagement.invoice.dto.PDFCrowdTokenDao;
import com.raybiztech.projectmanagement.invoice.dto.RemittanceDTO;
import com.raybiztech.projectmanagement.invoice.dto.ResourceRoleDto;
import com.raybiztech.projectmanagement.invoice.dto.TaxDTO;
import com.raybiztech.projectmanagement.invoice.dto.TaxTypeLookupDTO;
import com.raybiztech.projectmanagement.invoice.exception.DuplicateCountryException;
import com.raybiztech.projectmanagement.invoice.lookup.CountryLookUp;
import com.raybiztech.projectmanagement.invoice.lookup.Currency;
import com.raybiztech.projectmanagement.invoice.lookup.CurrencyToINR;
import com.raybiztech.projectmanagement.invoice.lookup.TaxTypeLookup;
import com.raybiztech.projectmanagement.invoice.quartz.InvoiceReminder;
import com.raybiztech.projectmanagement.invoice.utility.EntityAlreadyExistsException;
import com.raybiztech.projectmanagement.invoice.utility.GenerateInvoice;
import com.raybiztech.projectmanagement.invoice.utility.ProjectNotification;
import com.raybiztech.projectmanagement.invoicesummary.builder.InvoiceSummaryBuilder;
import com.raybiztech.projectmanagement.service.AllocationDetailsService;
import com.raybiztech.projectmanagement.service.ProjectService;
import com.raybiztech.projectmanagement.service.ProjectServiceImpl;
import com.raybiztech.recruitment.utils.DateParser;
import com.raybiztech.rolefeature.business.Permission;

@Service("invoiceServiceImpl")
public class InvoiceServiceImpl implements InvoiceService {

	private final InvoiceDao invoicedaoImpl;
	private final MilstoneBuilder milestonebuilder;
	private final InvoiceBuilder invoiceBuilder;
	private final TaxBuilder taxBuilder;
	private final LineItemBuilder lineItemBuilder;
	private final ProjectNumbersBuilder projectNumbersBuilder;
	private final InvoiceSummaryBuilder summaryBuilder;
	private final CountryAddressBuilder countryAddressBuilder;
	private final DAO dao;

	Logger logger = Logger.getLogger(InvoiceServiceImpl.class);
	@Autowired
	PropBean propBean;
	@Autowired
	InvoiceReminder invoiceReminder;
	@Autowired
	ProjectNotification projectNotification;
	@Autowired
	AllocationDetailsService allocationDetailsService;
	@Autowired
	RemittanceBuilder remittancebuilder;
	@Autowired
	SecurityUtils securityUtils;
	@Autowired
	ProjectService projectServiceImpl;
	@Autowired
	InvoiceReminderAcknowledgement invoiceReminderAcknowledgement;
	@Autowired
	MailTemplatesDao mailTemplatesDaoImpl;
	@Autowired
	MailContentParser mailContentParser;
	@Autowired
	ResourceManagementDAO resourceManagementDAO;
	@Autowired
	LeaveManagementUtils leaveManagementUtils;

	@Autowired
	public InvoiceServiceImpl(InvoiceDao invoicedaoImpl,
			MilstoneBuilder milestonebuilder, InvoiceBuilder invoiceBuilder,
			TaxBuilder taxBuilder, LineItemBuilder lineItemBuilder,
			ProjectNumbersBuilder projectNumbersBuilder,
			InvoiceSummaryBuilder summarybuilder,
			CountryAddressBuilder countryAddressBuilder, DAO dao) {
		this.invoicedaoImpl = invoicedaoImpl;
		this.milestonebuilder = milestonebuilder;
		this.invoiceBuilder = invoiceBuilder;
		this.taxBuilder = taxBuilder;
		this.lineItemBuilder = lineItemBuilder;
		this.projectNumbersBuilder = projectNumbersBuilder;
		this.summaryBuilder = summarybuilder;
		this.countryAddressBuilder = countryAddressBuilder;
		this.dao = dao;

	}

	@Transactional
	@Override
	public void addInvoice(InvoiceDTO invoicedto, Long id) {

		logger.warn("in service");

		try {
			// Convert InvoiceDTO to Entity
			Invoice invoice = invoiceBuilder.convertDtoToEntity(invoicedto, id);
			logger.warn("in backend service");
			invoice.setInvoiceStatus("RAISED");
			invoice.setInvoiceNumberFlag(Boolean.TRUE);
			// invoice.setInvoiceReopenFlag(Boolean.FALSE);
			System.out.println("Beforre save");
			//invoicedaoImpl.getSessionFactory().getCurrentSession().clear();
			Long invoiceId = (Long) invoicedaoImpl.save(invoice);
			System.out.println("Invoice Raised");
			invoicedaoImpl.getSessionFactory().getCurrentSession().flush();
			// here we are sending already saved Invoice data and encrypting it
			// and updating it

			invoiceBuilder.encryptInvoiceDataAndSave(invoiceId, invoice);
			addInvoiceAudit(invoiceId, "Created");

		} catch (Exception e) {

			e.printStackTrace();
		}

	}

	@Transactional
	@Override
	public void updateInvoice(InvoiceDTO invoicedto) {

		Invoice invoice = invoicedaoImpl.findBy(Invoice.class,
				invoicedto.getId());

		// HERE WE ARE CLONING INVOICE OBJECT THIS IS USED IN BULIDER AND
		// MOST
		// IMPORTANT
		Invoice clonedInvoice = null;
		try {
			clonedInvoice = (Invoice) invoice.clone();
		} catch (CloneNotSupportedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		Milestone milestone = invoicedaoImpl.findBy(Milestone.class,
				invoicedto.getMilestoneId());

		try {
			invoiceBuilder.ConvertUpdatedInvoiceToEntity(invoicedto, invoice,
					milestone);
			invoicedaoImpl.update(invoice);

			invoicedaoImpl.getSessionFactory().getCurrentSession().flush();
			Invoice invoice1 = invoicedaoImpl.findBy(Invoice.class,
					invoicedto.getId());
			// here while updating invoice we are sending tax details for
			// encryption

			Set<LineItem> lineItemForEncryption = invoice1.getLineItems();
			Set<LineItem> lineItems = lineItemBuilder
					.encryptLineItem(lineItemForEncryption);

			Set<Tax> taxForEncryption = invoice1.getTax();
			Set<Tax> encryptedTax = taxBuilder.encryptTax(taxForEncryption);

			invoice1.setTax(null);
			invoice1.setTax(encryptedTax);

			invoice1.setLineItems(null);
			invoice1.setLineItems(lineItems);

			invoicedaoImpl.update(invoice1);

			// invoice.setGenarateType(Boolean.FALSE);
			addInvoiceAudit(invoice1.getId(), "Updated");

			// FROM HERE INVOICE SUMMARY CODE BEGINS
			String invoiceStatus = invoicedto.getInvoiceStatus();
			if (invoiceStatus.equalsIgnoreCase("SENT")
					|| invoiceStatus.equalsIgnoreCase("RECEIVED")
					|| invoiceStatus.equalsIgnoreCase("PARTIALLY RECEIVED")
					|| invoiceStatus.equalsIgnoreCase("WRITE OFF")) {
				// IF invoice is proforma Invoice 
				if(!invoice.getProformaInvoiceFlag()){
					summaryBuilder.saveInvoiceSummary(milestone, invoice,
							clonedInvoice);
				}
			}
			// HERE INVOICE SUMMARY CODE ENDS//

		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	@Transactional
	@Override
	public void deleteInvoice(Long Id) {

		Invoice invoice = invoicedaoImpl.findBy(Invoice.class, Id);
		String status = invoice.getInvoiceStatus();

		// if status are following invoice can't be deleted
		if (!status.equalsIgnoreCase("RECEIVED")
				|| !status.equalsIgnoreCase("WRITE OFF")
				|| !status.equalsIgnoreCase("PARTIALLY RECEIVED")) {

			addInvoiceAudit(Id, "Deleted");

			Milestone milestone = invoicedaoImpl.findBy(Milestone.class,
					invoice.getMilsestone().getId());
			milestone.setInvoiceStatus(Boolean.FALSE);

			// here before deleting invoice we are removing it from invoice
			// summary
			// if invoice status is sent
			if (invoice.getInvoiceStatus().equalsIgnoreCase("SENT")
					|| invoice.getInvoiceStatus().equalsIgnoreCase(
							"PARTIALLY RECEIVED")) {
				summaryBuilder.deleteInvoiceAmountFromSummary(invoice,
						milestone);
			}
			invoicedaoImpl.delete(invoice);
		}

	}

	@Transactional
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getAllLookUps() {

		Map<String, Object> lookUpMap = invoicedaoImpl.getAllLookUps();

		return lookUpMap;
	}

	@Transactional
	@Override
	public InvoiceDTO getInitProjectDetails(Long id) {

		Project project = invoicedaoImpl.findBy(Project.class, id);

		InvoiceDTO inDto = new InvoiceDTO();
		inDto.setProjectId(project.getId());
		inDto.setProjectName(project.getProjectName());
		Client client = project.getClient();
		inDto.setClientId(client.getId());
		inDto.setClient(client.getName());
		// here we are loading the client GST code
		inDto.setGstCode(client.getGstCode());
		inDto.setClientContactPerson(project.getClient().getPersonName());
		inDto.setMilestoneDTOs(milestonebuilder.getDTOList(project
				.getClosedBillableMileStones()));
		inDto.setOrganization(project.getClient().getOrganization());
		inDto.setCompanyAdress(project.getClient().getAddress());
		inDto.setProjectType(project.getType().toString());
		if (project.getBillingContactPerson() != null) {
			inDto.setBillingContactPerson(project.getBillingContactPerson());
		}
		if (project.getBillingContactPersonEmail() != null) {
			inDto.setBillingContactPersonEmail(project
					.getBillingContactPersonEmail());
		}
		if(project.getProformaInvoiceFlag() != null){
			inDto.setProformaInvoiceFlag(project.getProformaInvoiceFlag());
		}
		inDto.setClientCountry(client.getCountry().getName().toString());
		return inDto;

	}

	// PREVIOUSLY BELOW CODE USED TO CHECK INVOICE DUE DATE AND USED TO
	// UPDATE STATUS ACCORDINGLY NOW SEPERATE SCHEDULER IS WRITTEN WHICH WILL
	// TAKE CARE OF THIS THINGS (IN getProjectInvoices METHOD) AT /***/ PLACE
	/*
	 * Date date = new Date(); List<Invoice> newlist = new ArrayList<Invoice>();
	 * for (Invoice invoice : invoList) {
	 * 
	 * if (date.isAfter(invoice.getDueDate()) &&
	 * (invoice.getInvoiceStatus().equalsIgnoreCase("RAISED") ||
	 * invoice.getInvoiceStatus().equalsIgnoreCase( "SENT") ||
	 * invoice.getInvoiceStatus() .equalsIgnoreCase("NOT SENT"))) {
	 * invoice.setInvoiceStatus("OVER DUE");
	 * invoicedaoImpl.saveOrUpdate(invoice); } newlist.add(invoice); }
	 */
	@SuppressWarnings("unchecked")
	@Transactional
	@Override
	public Map<String, Object> getInvoiceList(String projectType,String invoiceType, 
			List<String> status, String fromDate, String toDate,
			String multiText, Integer fromIndex, Integer toIndex,
			String datePeriod, String invoiceCountry, Boolean intrnalOrNot) {

		Employee employee = (Employee) securityUtils
				.getLoggedEmployeeDetailsSecurityContextHolder()
				.get("employee");

		Permission totalInvoiceList = invoicedaoImpl.checkForPermission(
				"Invoices", employee);
		Permission hierarchyInvoiceList = invoicedaoImpl.checkForPermission(
				"Hierarchy Invoice List", employee);

		Map<String, Object> invoicesMap = new HashMap<String, Object>();

		if (totalInvoiceList.getView() && !hierarchyInvoiceList.getView()) {
			// logger.warn("Getting Total Invoice List");
			invoicesMap = invoicedaoImpl.getListOfInvoice(projectType,invoiceType, status,
					fromDate, toDate, multiText, fromIndex, toIndex,
					datePeriod, new HashSet<Long>(), invoiceCountry,
					intrnalOrNot);
		} else if (totalInvoiceList.getView() && hierarchyInvoiceList.getView()) {
			// logger.warn("Getting Hierarchy Invoice List");
			List<Long> managersList = projectServiceImpl
					.mangerUnderManager(employee.getEmployeeId());

			// Here we are getting all projects allocated to logged in employee
			// as well as(manager) reportees of logged in employee
			/*
			 * Set<Long> projectIds = new HashSet<Long>(
			 * invoicedaoImpl.getProjectsFor(employee, managersList));
			 */

			invoicesMap = invoicedaoImpl.getListOfInvoice(projectType, invoiceType, status,
					fromDate, toDate, multiText, fromIndex, toIndex,
					datePeriod, null, invoiceCountry, intrnalOrNot);
		}

		/***/
		List<InvoiceQueryDTO> invoiceQueryDtoList = invoiceBuilder
				.InvoiceEntityToDToList((List<Invoice>) invoicesMap
						.get("InvoiceList"));

		Map<String, Object> ListofInvoiceMap = new HashMap<>();
		ListofInvoiceMap.put("InvoiceListSize",
				invoicesMap.get("InvoiceListSize"));
		ListofInvoiceMap.put("InvoiceList", invoiceQueryDtoList);
		return ListofInvoiceMap;
	}

	@Override
	public Map<String, Object> getInvoiceAuditList(String projectType,
			List<String> status, String fromDate, String toDate,
			String multiText, Integer fromIndex, Integer toIndex,
			String datePeriod, String invoiceCountry, Boolean intrnalOrNot) {

		Map<String, Object> datemap = this.getConvertedDatesForAudit(
				datePeriod, fromDate, toDate);

		Date startDate = (Date) datemap.get("startDate");
		Date endDate = (Date) datemap.get("endDate");

		Map<String, Object> map = invoicedaoImpl.getInvoiceAuditReport(status,
				startDate.toString("yyyy-MM-dd"),
				endDate.toString("yyyy-MM-dd"), multiText, datePeriod,
				invoiceCountry, intrnalOrNot);

		List<InvoiceAudit> audits = (List<InvoiceAudit>) map.get("InvoiceList");

		List<InvoiceAuditDto> auditDtos = new ArrayList<InvoiceAuditDto>();
		for (InvoiceAudit audit : audits) {
			// auditDtos.add(invoiceBuilder.getAuditReportDtos(audit));
			Long refNo = audit.getInvoiceId();
			List<InvoiceAudit> refInvoice = invoicedaoImpl.getRefrenceInvoice(refNo);
			if(refInvoice.isEmpty()){
				auditDtos.add(invoiceBuilder.getAuditReportDtos(audit));
			}
		}
		if(auditDtos != null){
			Collections.sort(auditDtos, new InvoiceAuditComparator());
		}
		

		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put("InvoiceList", auditDtos);
		returnMap.put("InvoiceListSize", auditDtos.size());
		return returnMap;

		/*
		 * Map<String, Object> map = new HashMap<String, Object>();
		 * 
		 * @SuppressWarnings("unchecked") List<InvoiceAudit> audits =
		 * (List<InvoiceAudit>) invoicedaoImpl .getInvoiceAuditList(status,
		 * startDate, endDate, multiText, datePeriod,
		 * invoiceCountry).get("InvoiceList");
		 * 
		 * List<String> numberList = new ArrayList<String>();
		 * 
		 * List<InvoiceAudit> auditList = new ArrayList<InvoiceAudit>();
		 * 
		 * if (!audits.isEmpty()) {
		 * 
		 * for (InvoiceAudit audit : audits) {
		 * 
		 * if (numberList.isEmpty()) { if (checkInvoiceHaveValidDate(audit,
		 * startDate, endDate)) { numberList.add(audit.getInvoiceId() + " " +
		 * audit.getInvoiceStatus()); auditList.add(audit); } } else { if
		 * (!numberList.contains(audit.getInvoiceId() + " " +
		 * audit.getInvoiceStatus())) { if (checkInvoiceHaveValidDate(audit,
		 * startDate, endDate)) { auditList.add(audit);
		 * numberList.add(audit.getInvoiceId() + " " +
		 * audit.getInvoiceStatus()); } }
		 * 
		 * }
		 * 
		 * } }
		 * 
		 * List<InvoiceAuditDto> auditDtos =
		 * invoiceBuilder.auditToDtoList(audits);
		 * 
		 * map.put("InvoiceListSize", auditList.size()); map.put("InvoiceList",
		 * auditDtos); return map;
		 */
	}

	@Override
	public Map<String, Object> yearlyInvoiceAuditReport(List<String> status,
			String fromDate, String toDate, String multiText,
			String invoiceCountry, Boolean intrnalOrNot) {

		Map<String, Object> returnObject = new HashMap<String, Object>();

		Map<String, Object> datemap = this.getConvertedDates(fromDate, toDate);

		Date startDate = (Date) datemap.get("startDate");
		Date endDate = (Date) datemap.get("endDate");

		Map<String, Object> map = invoicedaoImpl.getInvoiceAuditReport(status,
				startDate.toString("yyyy-MM-dd"),
				endDate.toString("yyyy-MM-dd"), multiText, null,
				invoiceCountry, intrnalOrNot);

		// Here we are getting yearly invoice Audits and need to convert to
		// month wise
		List<InvoiceAudit> audits = (List<InvoiceAudit>) map.get("InvoiceList");

		// logger.warn("size of an audits"+audits.size());

		// This will convert to month wise
		List<InvoiceAuditReportDto> auditReportDtos = this.getMap(audits,
				status);

		returnObject.put("displayStartDate", startDate.toString("dd/MM/yyyy"));
		returnObject.put("displayEndDate", endDate.toString("dd/MM/yyyy"));
		returnObject.put("result", auditReportDtos);

		return returnObject;

	}

	public List<InvoiceAuditReportDto> getMap(List<InvoiceAudit> audits,
			List<String> status) {

		// Using map for gathering all invoice audits according to month & year
		// wise key will be month and year combination and value will be
		// invoiceAuditReportDto

		// look into InvoiceAuditReportDto class

		Map<Integer, InvoiceAuditReportDto> yearlyAudit = new TreeMap<Integer, InvoiceAuditReportDto>();

		Map<String, Long> converter = this.getConverterAmounts();

		List<String> months = new ArrayList<String>(
				Arrays.asList(new DateFormatSymbols().getMonths()));

		for (InvoiceAudit audit : audits) {
			Long invoiceRefNo = audit.getInvoiceId();
			List<InvoiceAudit> invoiceRef = invoicedaoImpl.getRefrenceInvoice(invoiceRefNo);
			if(invoiceRef.isEmpty()){
				
		
			Long totalTaxAmount = 0L;
			InvoiceAuditDto auditDto = invoiceBuilder.getAuditReportDtos(audit);

			Long amountInr = converter.get(auditDto.getCurrencyType());

			if (auditDto.getTaxDTOs() != null) {
				// Either proforma or Invoice will add in taxtotal
				if(auditDto.getProformaReferenceNo() == null){
					for (TaxDTO tax : auditDto.getTaxDTOs()) {
						if (!auditDto.getCurrencyType().equalsIgnoreCase("INR")) {
							totalTaxAmount += (Long.valueOf(tax.getTax()) * (amountInr != null ? amountInr
									: 1));
						} else {
							totalTaxAmount += Long.valueOf(tax.getTax());
						}
					}
				}
				
			}

			Date date = (status.contains("SENT") ? audit.getInvoiceSentDate()
					: audit.getInvoiceAmountReceviedDate());

			if (date != null) {

				Integer month = date.getMonthOfYear().getValue();

				Integer year = date.getYearOfEra().getValue();

				Integer monthValue = Integer.parseInt(String.valueOf(year)
						+ String.valueOf(month));

				// logger.warn("month"+monthValue);

				String projectType = auditDto.getProjectType();

				projectType = (projectType != null) ? projectType : "fixedbid";

				Long invoiceAmount = Math
						.round((projectType.equalsIgnoreCase("fixedbid") ? Double
								.valueOf(auditDto.getAmount()) : Double
								.valueOf(auditDto.getSubTotal())));

				String reportStatus = auditDto.getInvoiceStatus();

				Long totalAmount = Math.round(Double.valueOf(auditDto
						.getTotalAmount()));

				Long receivedAmount = Math.round(Double.valueOf(auditDto
						.getReceivedAmount()));

				Long balanceAmount = Math.round(Double.valueOf(auditDto
						.getBalanceAmount()));

				Long tdsAmount = 0L;
				if (auditDto.getTdsAmount() != null) {
					tdsAmount = Math.round(Double.valueOf(auditDto
							.getTdsAmount()));
				}

				Long netAmount = 0L;
				if (auditDto.getNetAmount() != null) {
					netAmount = Math.round(Double.valueOf(auditDto
							.getNetAmount()));
				}

				Long convertedInvoiceAmount = 0L;
				Long convertedTotalAmount = 0L;
				Long convertedReceivedAmount = 0L;
				Long convertedBalanceAmount = 0L;
				Long convertedTdsamount = 0L;
				Long convertedNetAmount = 0L;
				if (!auditDto.getCurrencyType().equalsIgnoreCase("INR")) {
					
					// Either Proforma or Invoice will add in total
					if(auditDto.getProformaReferenceNo() == null){
					convertedInvoiceAmount = invoiceAmount
							* (amountInr != null ? amountInr : 1);

					convertedTotalAmount = totalAmount
							* (amountInr != null ? amountInr : 1);

					convertedReceivedAmount = receivedAmount
							* (amountInr != null ? amountInr : 1);

					convertedBalanceAmount = balanceAmount
							* (amountInr != null ? amountInr : 1);

					if (tdsAmount != null) {
						convertedTdsamount = tdsAmount
								* (amountInr != null ? amountInr : 1);
					}

					if (netAmount != null) {
						convertedNetAmount = netAmount
								* (amountInr != null ? amountInr : 1);
					}
					}

					/*
					 * convertedInvoiceAmount = invoiceAmount
					 * converter.get(auditDto.getCurrencyType());
					 * 
					 * convertedTotalAmount = totalAmount
					 * converter.get(auditDto.getCurrencyType());
					 */

				} else {
					// Either Proforma or Invoice will add in total amount
					if(auditDto.getProformaReferenceNo() == null){
						convertedInvoiceAmount = invoiceAmount;
						convertedTotalAmount = totalAmount;
						convertedReceivedAmount = receivedAmount;
						convertedBalanceAmount = balanceAmount;
						if (tdsAmount != null) {
							convertedTdsamount = tdsAmount;
						}
						if (netAmount != null) {
							convertedNetAmount = netAmount;
						}
					}
					
				}

				if (yearlyAudit.containsKey(monthValue)) {
					// logger.warn("in if");
					
					InvoiceAuditReportDto invoiceAuditReportDto = yearlyAudit
							.get(monthValue);
					
					invoiceAuditReportDto.getAuditDtos().add(auditDto);
					invoiceAuditReportDto
							.setInvoiceAmountTotal(invoiceAuditReportDto
									.getInvoiceAmountTotal()
									+ convertedInvoiceAmount);
					invoiceAuditReportDto
							.setFinalAmountTotal(invoiceAuditReportDto
									.getFinalAmountTotal()
									+ convertedTotalAmount);
					invoiceAuditReportDto
							.setReceivedAmountTotal(invoiceAuditReportDto
									.getReceivedAmountTotal()
									+ convertedReceivedAmount);
					invoiceAuditReportDto
							.setBalanceAmountTotal(invoiceAuditReportDto
									.getBalanceAmountTotal()
									+ convertedBalanceAmount);
					invoiceAuditReportDto.setInvoiceStatus(reportStatus);

					invoiceAuditReportDto
							.setTotalTaxAmount(invoiceAuditReportDto
									.getTotalTaxAmount() + totalTaxAmount);

					invoiceAuditReportDto
							.setTdsAmountTotal(invoiceAuditReportDto
									.getTdsAmountTotal() != null ? invoiceAuditReportDto
									.getTdsAmountTotal() + convertedTdsamount
									: null);
					invoiceAuditReportDto
							.setNetAmountTotal(invoiceAuditReportDto
									.getNetAmountTotal() != null ? invoiceAuditReportDto
									.getNetAmountTotal() + convertedNetAmount
									: null);

				} else {
					// logger.warn("in else");
					InvoiceAuditReportDto auditReportDto = new InvoiceAuditReportDto();

					List<InvoiceAuditDto> auditDtos = new ArrayList<InvoiceAuditDto>();
					auditDtos.add(auditDto);
					auditReportDto.setAuditDtos(auditDtos);

					auditReportDto
							.setInvoiceAmountTotal(convertedInvoiceAmount);
					auditReportDto.setFinalAmountTotal(convertedTotalAmount);
					auditReportDto
							.setReceivedAmountTotal(convertedReceivedAmount);
					auditReportDto
							.setBalanceAmountTotal(convertedBalanceAmount);
					auditReportDto.setMonthName(months.get(month));
					auditReportDto.setYear(year.toString());
					auditReportDto.setKey(monthValue);
					auditReportDto.setInvoiceStatus(reportStatus);
					auditReportDto.setTotalTaxAmount(totalTaxAmount);
					auditReportDto
							.setTdsAmountTotal(convertedTdsamount != null ? convertedTdsamount
									: null);
					auditReportDto
							.setNetAmountTotal(convertedNetAmount != null ? convertedNetAmount
									: null);

					yearlyAudit.put(monthValue, auditReportDto);
				}
				}
			}// If
		}// for

		return new ArrayList<InvoiceAuditReportDto>(yearlyAudit.values());

	}

	public Map<String, Long> getConverterAmounts() {

		Map<String, Long> map = new HashMap<String, Long>();

		for (CurrencyToINR currencyToINR : invoicedaoImpl
				.get(CurrencyToINR.class)) {
			map.put(currencyToINR.getCurrenyType(),
					currencyToINR.getInrAmount());
		}

		return map;

	}

	// Used in previously now we are not using this keeping for future
	// references

	// This was used in getInvoiceAuditList method
	public Boolean checkInvoiceHaveValidDate(InvoiceAudit audit,
			Date startDate, Date endDate) {

		Date date = null;

		Invoice invoice = invoicedaoImpl.findBy(Invoice.class,
				audit.getInvoiceId());

		if (invoice != null && startDate != null && endDate != null) {

			String auditStatus = audit.getInvoiceStatus();

			if (auditStatus.equalsIgnoreCase("SENT")
					|| auditStatus.equalsIgnoreCase("WRITE OFF")) {
				date = invoice.getInvoiceAmountSentDate();
			}
			if (auditStatus.equalsIgnoreCase("RECEIVED")
					|| auditStatus.equalsIgnoreCase("PARTIALLY RECEIVED")) {
				date = invoice.getInvoiceAmountReceviedDate();
			}

			if (date != null) {
				return ((startDate.equals(date) || startDate.isBefore(date)) && (endDate
						.equals(date) || endDate.isAfter(date))) ? Boolean.TRUE
						: Boolean.FALSE;
			}
		}

		return Boolean.FALSE;

	}

	public Map<String, Object> getConvertedDates(String fromDate, String toDate) {

		Map<String, Object> map = new HashMap<String, Object>();

		Date startDate = null;
		Date endDate = null;

		try {
			startDate = DateParser.toDate(fromDate);
			endDate = DateParser.toDate(toDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		map.put("startDate", startDate);
		map.put("endDate", endDate);
		return map;

	}

	public Map<String, Object> getConvertedDatesForAudit(String datePeriod,
			String fromDate, String toDate) {

		Map<String, Object> map = new HashMap<String, Object>();

		Date startDate = null;
		Date endDate = null;

		if (datePeriod.equalsIgnoreCase("custom")) {
			try {
				startDate = DateParser.toDate(fromDate);
				endDate = DateParser.toDate(toDate);
			} catch (ParseException e) {
				e.printStackTrace();
			}

		} else {
			Map<String, Date> parsedDates = invoicedaoImpl
					.getCustomDates(datePeriod);
			startDate = parsedDates.get("startDate");
			endDate = parsedDates.get("endDate");

		}

		map.put("startDate", startDate);
		map.put("endDate", endDate);
		return map;

	}

	@Override
	public Boolean checkInvoiceNumberAlreadyExists(String invoiceNumber) {

		Invoice invoice = invoicedaoImpl.findByUniqueProperty(Invoice.class,
				"number", invoiceNumber);
		return (invoice != null) ? true : false;
	}

	// PREVIOUSLY BELOW CODE USED TO CHECK INVOICE DUE DATE AND USED TO
	// UPDATE STATUS ACCORDINGLY NOW SEPERATE SCHEDULER IS WRITTEN WHICH WILL
	// TAKE CARE OF THIS THINGS (IN getProjectInvoices METHOD) AT /***/ PLACE
	/*
	 * Date date = new Date(); List<Invoice> newlist = new ArrayList<Invoice>();
	 * for (Invoice invoice : invoList) {
	 * 
	 * if (date.isAfter(invoice.getDueDate()) &&
	 * (invoice.getInvoiceStatus().equalsIgnoreCase("RAISED") ||
	 * invoice.getInvoiceStatus().equalsIgnoreCase( "SENT") ||
	 * invoice.getInvoiceStatus() .equalsIgnoreCase("NOT SENT"))) {
	 * invoice.setInvoiceStatus("OVER DUE");
	 * invoicedaoImpl.saveOrUpdate(invoice); } newlist.add(invoice); }
	 */

	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	@Override
	public Map<String, Object> getProjectInvoices(Long id, Integer startIndex,
			Integer endIndex) {
		Map<String, Object> map = invoicedaoImpl.getProjectInvoices(id,
				startIndex, endIndex);

		List<Invoice> invoList = (List<Invoice>) map.get("list");
		Integer size = (Integer) map.get("listSize");
		/***/
		List<InvoiceQueryDTO> invoiceQueryDtoList = invoiceBuilder
				.InvoiceEntityToDToList(invoList);
		map.put("list", invoiceQueryDtoList);
		map.put("listSize", size);
		return map;
	}

	@Transactional
	@Override
	public InvoiceDTO getEditInitInvoiceDetails(Long id) {

		Invoice invoice1 = invoicedaoImpl.findBy(Invoice.class, id);
		Date date = new Date();
		if (date.isAfter(invoice1.getDueDate())
				&& (invoice1.getInvoiceStatus().equalsIgnoreCase("RAISED") || invoice1
						.getInvoiceStatus().equalsIgnoreCase("NOT SENT"))) {
			invoice1.setInvoiceStatus("OVER DUE");
			invoicedaoImpl.saveOrUpdate(invoice1);

		}

		InvoiceDTO dto = invoiceBuilder.entityToDto(invoice1);

		dto.setMilestoneDTOs(milestonebuilder.getDTOList(invoice1
				.getMilsestone().getProject()
				.getClosedBillableMileStonesForEdit()));

		return dto;
	}

	@Transactional
	@Override
	public void emailInvoice(Long invoiceId) {
		String getPDFPath = "";
		Invoice invoice = invoicedaoImpl.findBy(Invoice.class, invoiceId);

		SimpleDateFormat dateFormatMonth = new SimpleDateFormat("MMMMM");
		String month = dateFormatMonth.format(java.util.Date.parse(invoice
				.getInvoiceDate().toString()));
		SimpleDateFormat dateFormatYear = new SimpleDateFormat("yyyy");
		String year = dateFormatYear.format(java.util.Date.parse(invoice
				.getInvoiceDate().toString()));

		String cc = "";

		getPDFPath = (String) propBean.getPropData().get("fixedbidinvoices")
				+ invoice.getInvoiceFileName();

		/*
		 * getPDFPath = (String) propBean.getPropData() .get("retainerinvoices")
		 * + invoice.getInvoiceFileName();
		 */
		projectNotification.emailInoviceToClient(invoice, cc, getPDFPath,
				month, year);
	}

	@Transactional
	@Override
	public void downLoadInvoice(String filename, Long id,
			HttpServletResponse response) throws IOException {

		Invoice invoice = invoicedaoImpl.findBy(Invoice.class, id);
		ProjectType projectType = (invoice.getMilsestone() == null) ? (null)
				: (invoice.getMilsestone().getProject() == null) ? (null)
						: invoice.getMilsestone().getProject().getType();
		String filePath = null;
		filePath = (String) propBean.getPropData().get("fixedbidinvoices");
		String fileNameWithPath = filePath + filename;

		InputStream fileInputStream = null;
		PrintWriter printWriter = null;
		try {

			response.setHeader("Content-Disposition", "attachment;filename="
					+ "\"" + filename + "\"");
			response.setContentType("application/pdf");
			File invoiceFile = new File(fileNameWithPath);

			fileInputStream = new FileInputStream(invoiceFile);
			OutputStream outputStream = response.getOutputStream();

			IOUtils.copy(fileInputStream, outputStream);

		} catch (FileNotFoundException exception) {

		} finally {
			if (fileInputStream != null) {
				fileInputStream.close();
			}
			if (printWriter != null) {
				printWriter.flush();
				printWriter.close();
			}
		}

	}

	@Transactional
	@Override
	public List<MilestonePeopleDTO> getBillableForRetainer(Long InvoiceId,
			Long id) {
		InvoiceDTO invoiceDetail = getEditInitInvoiceDetails(InvoiceId);

		List<MilestonePeopleDTO> milestonePeopleDTOs = new ArrayList<MilestonePeopleDTO>();

		List<ReportDTO> dtos = new ArrayList<ReportDTO>();

		for (MilestonePeopleDTO milestonePeopleDTO : allocationDetailsService
				.getmilestonebillableresources(id)) {
			Boolean peopleAllocatedForMilestone = false;
			for (LineItemDTO itemDTO : invoiceDetail.getLineitem()) {
				if (milestonePeopleDTO.getEmployeeId().equals(
						itemDTO.getEmpId())) {

					MilestonePeopleDTO milestonePeopleDTO1 = new MilestonePeopleDTO();
					milestonePeopleDTO1.setAmount(itemDTO.getAmount());
					milestonePeopleDTO1.setCount(itemDTO.getCount());
					milestonePeopleDTO1.setDuration(itemDTO.getDuration());
					milestonePeopleDTO1.setRole(itemDTO.getRole());
					milestonePeopleDTO1.setRate(itemDTO.getRate());
					milestonePeopleDTO1.setEmployeeName(itemDTO.getEmpName());
					milestonePeopleDTO1.setEmployeeId(itemDTO.getEmpId());
					milestonePeopleDTO1.setStatus(Boolean.TRUE);
					milestonePeopleDTO1.setFromDate(itemDTO.getFromDate());
					milestonePeopleDTO1.setEndDate(itemDTO.getEndDate());
					milestonePeopleDTO1.setMonthWorkingDays(itemDTO
							.getMonthWorkingDays());
					milestonePeopleDTO1.setHolidays(itemDTO.getHolidays());
					milestonePeopleDTO1.setLeaves(itemDTO.getLeaves());
					milestonePeopleDTO1.setHours(itemDTO.getHours());
					milestonePeopleDTO1.setTotalValue(itemDTO.getTotalValue());
					milestonePeopleDTO1.setBillableDays(itemDTO
							.getBillableDays());
					milestonePeopleDTO1.setComments(itemDTO.getComments());
					peopleAllocatedForMilestone = true;
					milestonePeopleDTOs.add(milestonePeopleDTO1);
					break;
				}

			}
			if (!peopleAllocatedForMilestone) {
				milestonePeopleDTO.setStatus(Boolean.FALSE);
				milestonePeopleDTOs.add(milestonePeopleDTO);
			}

		}
		return milestonePeopleDTOs;

	}

	@Transactional
	@Override
	public Boolean checkInvoiceNoAlreadyExists(String invoiceNumber) {

		Invoice invoicenumber = invoicedaoImpl.findByUniqueProperty(
				Invoice.class, "number", invoiceNumber);

		return (invoicenumber != null) ? (Boolean.TRUE) : (Boolean.FALSE);
	}

	@Transactional
	@Override
	public String checkEmpIdExixts(Long empid) {

		Employee emp = invoicedaoImpl.findByUniqueProperty(Employee.class,
				"employeeId", empid);

		String empName = "null";

		if (emp != null) {

			if (emp.getStatusName().equalsIgnoreCase("Active")) {
				empName = emp.getFirstName() + " " + emp.getLastName();
			}

		}

		return empName;
	}

	@Transactional
	@Override
	public String generatepdf(Long InvoiceId) {
		Invoice invoice = invoicedaoImpl.findBy(Invoice.class, InvoiceId);
		InvoiceDTO invoicedto = invoiceBuilder.entityToDto(invoice);
		ProjectType projectType = (invoice.getMilsestone() == null) ? (null)
				: (invoice.getMilsestone().getProject() == null) ? (null)
						: invoice.getMilsestone().getProject().getType();
		try {

			int size = 0;
			if (invoicedto.getLineitem() != null) {
				if (invoicedto.getLineitem().size() > 5
						&& invoicedto.getLineitem().size() <= 11) {
					size = 10;
				} else if (invoicedto.getLineitem().size() > 11) {
					size = 20;
				}
			}

			String path = (String) propBean.getPropData().get(
					"fixedBidInvoicesJasper");
			InputStream inputStream = null;
			if (size == 10) {
				inputStream = new FileInputStream(path
						+ "LineItemMoreThen5.jrxml");
			} else if (size == 20) {
				inputStream = new FileInputStream(path
						+ "LineItemMoreThen12.jrxml");
			} else {
				inputStream = new FileInputStream(path + "InvoicePDF.jrxml");
			}

			GenerateInvoice.generateInvoice(invoicedto, inputStream, propBean,
					projectType);
			invoice.setInvoiceFileName(invoicedto.getNumber().replaceAll(" ",
					".")
					+ "."
					+ invoicedto.getProjectName().replaceAll(" ", ".")
					+ "."
					+ invoicedto.getInvoiceDate().replaceAll("/", ".")
					+ ".pdf");
			// invoice.setGenarateType(Boolean.TRUE);
			invoicedaoImpl.saveOrUpdate(invoice);

		} catch (JRException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return invoice.getInvoiceFileName();
	}

	@Override
	public List<InvoiceDTO> getAllInvoices() {
		return null;
	}

	@Override
	public InvoiceQueryDTO getInvoice(Long invoiceId) {

		List<Invoice> invoiceList = new ArrayList<Invoice>();
		invoiceList.add(invoicedaoImpl.findBy(Invoice.class, invoiceId));

		List<InvoiceQueryDTO> invoiceQueryDtoList = invoiceBuilder
				.InvoiceEntityToDToList(invoiceList);

		return invoiceQueryDtoList.get(0);
	}

	@Override
	public List<RemittanceDTO> getwireTransferInstructions(String currency,
			String bankName) {
		List<RemittanceDTO> remilist = new ArrayList<RemittanceDTO>();
		if (currency != null && bankName != null) {
			List<Remittance> remmitaList = invoicedaoImpl.remitanceList(
					currency, bankName);
			for (Remittance remittance2 : remmitaList) {
				RemittanceDTO remittanceDTO = remittancebuilder
						.convertEntityToDTO(remittance2);
				remilist.add(remittanceDTO);
			}

		}
		return remilist;

	}

	@Override
	public void addInvoiceAudit(Long invoiceId, String persistType) {
		InvoiceAudit audit;
		System.out.println("In addinvoiceAudit");
		try {
			audit = invoiceBuilder.saveInvoiceAudit(invoiceId, persistType);
			invoicedaoImpl.save(audit);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public List<InvoiceAuditDto> getInvoiceAudit(Long id) {
		List<InvoiceAudit> list = invoicedaoImpl.getInvoiceAudit(id);
		List<InvoiceAuditDto> auditDtos = invoiceBuilder.auditToDtoList(list);
		Collections.sort(auditDtos, new InvoiceAuditComparator());

		return auditDtos;
	}

	@Override
	public Double getAllInvoicePercentageForMilestone(Long milestoneId) {

		Milestone milestone = invoicedaoImpl.findBy(Milestone.class,
				milestoneId);

		Double invoiceallPercentage = 0.0;
		List<Invoice> invoices = invoicedaoImpl.getAllOfProperty(Invoice.class,
				"milsestone", milestone);
		for (Invoice invoice : invoices) {
			if (invoice.getPercentage() != null) {
				if(invoice.getProformaReferenceNo() == null){
					invoiceallPercentage = invoiceallPercentage
							+ Double.valueOf(invoice.getPercentage());
				}
			}

		}
		return invoiceallPercentage;
	}

	@Override
	public void saveAllTax(TaxTypeLookupDTO dto) {
		TaxTypeLookup lookup = taxBuilder.masterTax(dto);
		invoicedaoImpl.save(lookup);
	}

	@Override
	public Set<TaxTypeLookupDTO> getAllTaxRelatedToCuntry(String country) {
		Set<TaxTypeLookup> lookups = invoicedaoImpl
				.getAllTaxRelatedToCuntry(country);
		Set<TaxTypeLookupDTO> lookupDTOs = taxBuilder.taxLookupList(lookups);
		return lookupDTOs;
	}

	@Override
	public void updateTax(TaxTypeLookupDTO dto) {
		TaxTypeLookup taxTypeLookup = invoicedaoImpl.findBy(
				TaxTypeLookup.class, dto.getId());
		taxTypeLookup.setTaxRate(dto.getTaxRate());
		invoicedaoImpl.update(taxTypeLookup);
	}

	@Override
	public void deleteTax(Long id) {
		TaxTypeLookup taxTypeLookup = invoicedaoImpl.findBy(
				TaxTypeLookup.class, id);
		List<Tax> taxs = invoicedaoImpl.getAllOfProperty(Tax.class, "taxType",
				taxTypeLookup.getName());
		if (taxs.isEmpty()) {
			invoicedaoImpl.delete(taxTypeLookup);
		} else {
			throw new EntityAlreadyExistsException("Tax "
					+ taxTypeLookup.getName()
					+ " is already used in Invoice You can't Delete ");
		}

	}

	@Override
	public String getallInvoiceAmountOfProject(Long projectId) {

		List<Invoice> invoices = invoicedaoImpl
				.getAllInvoicesAmountForProject(invoicedaoImpl.findBy(
						Project.class, projectId));
		Double invoiceAmount = 0.0;
		for (Invoice invoice : invoices) {
			if (invoice != null) {
				AES256Encryption aes256Encryption = new AES256Encryption(
						String.valueOf(invoice.getId()), invoice.getSaltKey());
				invoiceAmount += Double.parseDouble(aes256Encryption
						.decrypt(invoice.getAmount()));
			}

		}

		String invoiceTotalStringAmount = String.valueOf(invoiceAmount);

		return invoiceTotalStringAmount;
	}

	@Override
	public ProjectNumbersDTO getAmountOfCR(Long crId) {

		ChangeRequest changeRequest = invoicedaoImpl.findBy(
				ChangeRequest.class, crId);

		ProjectNumbers numbers = invoicedaoImpl.getAmountofCr(changeRequest);

		return projectNumbersBuilder.toDto(numbers);
	}

	@Override
	public void updatePDFCrowd(PDFCrowdTokenDao crowdTokenDao) {
		PDFCrowdToken crowdToken = invoicedaoImpl.findBy(PDFCrowdToken.class,
				crowdTokenDao.getId());
		if (crowdTokenDao != null) {

			crowdToken.setToken(crowdTokenDao.getToken());
			crowdToken.setUserName(crowdTokenDao.getUserName());
			invoicedaoImpl.saveOrUpdate(crowdToken);
		}
	}

	@Override
	public void getPDF(String htmlFile) throws IOException {
		PDFCrowdTokenDao crowdTokenDao = getPDFCrowd();

		String htmlStr = getCss();
		String body = htmlFile.trim();
		String bodyWithCss = htmlStr + body + "</body></html>";

		String filePath = (String) propBean.getPropData().get(
				"fixedbidinvoices");

		String header = "<div style=\"background:#17365d; min-height:40px;\"></div>"
				+ "<div class=\"col-xs-6 pull-right  logo-pdf \">"
				+ "<img src=\"http://www.raybiztech.com/App_Themes/Raybiztech/images/Raybiztech-logo.png\" style=\"padding-right: 15px;\" width=\"197\" height=\"65\" />"
				+ "</div>"
				+ "<div  class=\"col-xs-6 pull-left invoice\">"
				+ "<h2 style=\"font-size:20px; padding-left:15px\">INVOICE</h2>"
				+ "</div>";

		String headerWithcss = htmlStr + header + "</body></html>";

		String footer = "<div class=\"row\">"
				+ "<div style=\"padding-left: 15px; padding-right: 15px;\" class=\"queries col-xs-12\">"
				+ "<p>"
				+ "If you have any questions regarding the quotation, please contact"
				+ " Ajay Ray,<br /> <a style=\"color: #000; text-decoration: none;\"mailto:ajay@raybiztech.com\">ajay@raybiztech.com</a>,"
				+ "+1 818 937 4333/+91 9849 743 823." + "</p>" + "</div>"
				+ "</div>"
				+ "<div style=\"background:#17365d; min-height:40px;\"></div>";

		String footerWithCss = htmlStr + footer + "</body></html>";

		FileOutputStream fout = new FileOutputStream(filePath
				+ "FixedBidInvoice.html");
		BufferedWriter bw = new BufferedWriter(new FileWriter(filePath
				+ "FixedBidInvoice.html"));
		bw.write(bodyWithCss);
		bw.close();

		try {
			FileOutputStream fileStream;

			com.pdfcrowd.Client client = new com.pdfcrowd.Client(
					crowdTokenDao.getUserName(), crowdTokenDao.getToken());

			// convert an HTML string and store the PDF into a byte array
			ByteArrayOutputStream memStream = new ByteArrayOutputStream();
			String html = "<head></head><body>My HTML Layout</body>";
			client.convertHtml(html, memStream);
			// MARGINS ARE VERY IMPORTANT
			client.setPageMargins("4cm", "0cm", "3cm", "0cm");
			client.setHeaderHtml(headerWithcss);
			client.setFooterHtml(footerWithCss);
			// convert an HTML file
			fileStream = new FileOutputStream(filePath + "Invoice.pdf");
			client.convertFile(filePath + "FixedBidInvoice.html", fileStream);
			fileStream.close();

			// retrieve the number of tokens in your account
			Integer ntokens = client.numTokens();
			logger.warn("No of token remaining: " + ntokens);
		} catch (PdfcrowdError why) {
			System.err.println(why.getMessage());
		} catch (IOException exc) {
			// handle the exception
		}

	}

	@Override
	public void getInvoicePDF(String postString,HttpServletRequest request) throws Exception {
		
		logger.warn("tenantKey"+request.getHeader("tenantkey"));
		
		String tenantkey =request.getHeader("tenantkey");

		String htmlStr = getCss();
		String body = postString.trim();
		String bodyWithCss = htmlStr + body + "</body></html>";

		String filePath = (String) propBean.getPropData().get(
				"fixedbidinvoices");
		FileOutputStream fout = new FileOutputStream(filePath
				+ "FixedBidInvoice.html");
		BufferedWriter bw = new BufferedWriter(new FileWriter(filePath
				+ "FixedBidInvoice.html"));
		bw.write(bodyWithCss);
		bw.close();
		
		String headerPath = "";
		String footerPath ="";
		

		if(tenantkey.equalsIgnoreCase("RAYBIZTECH")){
		 headerPath = filePath + "header.html";
		 footerPath = filePath + "footer.html";
		}else if(tenantkey.equalsIgnoreCase("AIBRIDGEML")){
			 headerPath = filePath + "header1.html";
			 footerPath = filePath + "footer1.html";
		}
		String bodyPath = filePath + "FixedBidInvoice.html";
		String invoicePath = filePath + "Invoice.pdf";

		// This is very Imp line this will create invoice we need to install
		// wkhtmltopdf version 0.12.2.1 (with patched qt)
		// and execute this commands as per their API
		// this link(http://wkhtmltopdf.org/usage/wkhtmltopdf.txt) will help you
		// in this

		String cmd = "wkhtmltopdf  -L 0mm -R 0mm  --header-html " + headerPath
				+ " -B 25mm --footer-html " + footerPath
				+ " --header-spacing 30 -T 40mm " + bodyPath + " "
				+ invoicePath;

		// AND PROCESS HERE IS FOR EXECUTING LINUX COMMAND USING JAVA
		Process process = Runtime.getRuntime().exec(cmd);

		process.waitFor();

		logger.warn("Process Executed Successfully");

	}

	@Override
	public PDFCrowdTokenDao getPDFCrowd() {
		Long id = 1l;
		PDFCrowdToken crowdTokend = invoicedaoImpl.findBy(PDFCrowdToken.class,
				id);
		PDFCrowdTokenDao crowdTokenDao = new PDFCrowdTokenDao();
		crowdTokenDao.setId(crowdTokend.getId());
		crowdTokenDao.setToken(crowdTokend.getToken());
		crowdTokenDao.setUserName(crowdTokend.getUserName());
		return crowdTokenDao;
	}

	@Override
	public Set<ResourceRoleDto> getByRole(Set<LineItemDTO> items) {

		return invoiceBuilder.resourceRoleDto(items);
	}

	@Override
	public void addCountry(CountryLookUpDTO countryLookUpDTO) {

		CountryLookUp country = resourceManagementDAO.findByUniqueProperty(
				CountryLookUp.class, "name", countryLookUpDTO.getName());
		if (country != null) {

			throw new DuplicateCountryException();
		}
		CountryLookUp countryLookUp = null;
		if (countryLookUpDTO != null) {
			countryLookUp = new CountryLookUp();
			countryLookUp.setId(countryLookUpDTO.getId());
			countryLookUp.setName(countryLookUpDTO.getName().toUpperCase());
			invoicedaoImpl.save(countryLookUp);
		}

	}

	@Override
	public List<CountryLookUp> getCountryLookup() {
		return invoicedaoImpl.get(CountryLookUp.class);
	}

	@Override
	public void deleteCountry(Integer id) {
		CountryLookUp countryLookUp = invoicedaoImpl.findBy(
				CountryLookUp.class, id);
		List<Invoice> invoices = invoicedaoImpl.getAllOfProperty(Invoice.class,
				"country", countryLookUp.getName());
		if (invoices.isEmpty()) {
			invoicedaoImpl.delete(countryLookUp);
		} else {
			throw new EntityAlreadyExistsException("Can't Delete Country"
					+ countryLookUp.getName()
					+ " because it is used in invoices");
		}
	}

	@Override
	public void deletePartiallyReceivedAmount(Long amount, Long invoiceId) {

		ReceivedInvoiceAmount receivedInvoiceAmount = invoicedaoImpl.findBy(
				ReceivedInvoiceAmount.class, amount);

		summaryBuilder.deletepartiallyReceivedInvoiceAmountFromSummary(
				receivedInvoiceAmount,
				invoicedaoImpl.findBy(Invoice.class, invoiceId));

		invoicedaoImpl.delete(receivedInvoiceAmount);

		Invoice updatedInvoice = invoicedaoImpl
				.findBy(Invoice.class, invoiceId);
		// logger.warn(updatedInvoice.getReceivedAmountList().isEmpty());
		if (updatedInvoice.getReceivedAmountList().isEmpty()) {
			updatedInvoice.setInvoiceStatus("SENT");
			invoicedaoImpl.update(updatedInvoice);
		}

	}

	@Override
	public InvoiceDTO getInvoiceDetailsForDiscussion(Long id) {
		Invoice invoice = invoicedaoImpl.findBy(Invoice.class, id);
		InvoiceDTO dto = new InvoiceDTO();
		Milestone milestone = invoice.getMilsestone();

		dto.setClient(milestone.getProject().getClient().getName());
		dto.setProjectName(milestone.getProject().getProjectName());
		dto.setNumber(invoice.getNumber());
		dto.setPercentage(invoice.getPercentage());
		dto.setInvoiceStatus(invoice.getInvoiceStatus());
		dto.setProjectId(milestone.getProject().getId());
		dto.setInvoiceNumber(invoice.getInvoiceNumber() != null ? invoice
				.getInvoiceNumber() : null);

		return dto;
	}

	@Override
	public Map<String, String> createInvoiceNumber(String clientName,
			String country) {

		Client client = invoicedaoImpl.findByUniqueProperty(Client.class,
				"name", clientName);
		
		String patternforInvoice = invoicedaoImpl.getInvoicePattern();
		
		logger.warn("service"+patternforInvoice);

		StringBuilder builder = new StringBuilder();
		
		if(patternforInvoice != null){
			logger.warn("patternforInvoice"+patternforInvoice);
		builder.append(patternforInvoice);
		}
		
		int month = new Date().getMonthOfYear().getValue() + 1;

		builder.append(String.format("%02d", month)).append(
				String.valueOf(new Date().getYearOfEra().getValue()).substring(
						2, 4));

		builder.append((country != null) ? country.subSequence(0, 2) : "");

		if (client.getClientCode() != null) {
			builder.append(String.format("%03d",
					Integer.parseInt(client.getClientCode())));
		}

		/*
		 * int month = new Date().getMonthOfYear().getValue() + 1;
		 * 
		 * builder.append(String.format("%02d", month)).append(
		 * String.valueOf(new Date().getYearOfEra().getValue()).substring( 2,
		 * 4));
		 */

		String maxinvoiceNumber = invoicedaoImpl
				.getLatestInvoiceNumberForClient(client, country);

		String maxNumber;

		if (maxinvoiceNumber != null) {
			Long invoiceMaxNumber = Long.valueOf(maxinvoiceNumber);
			maxNumber = formatNumber(invoiceMaxNumber + 1,
					maxinvoiceNumber.length());
		} else {
			maxNumber = "001";
		}

		Map<String, String> map = new HashMap<String, String>();
		map.put("pattern", builder.toString());
		map.put("number", maxNumber);

		return map;
	}

	@Override
	public Boolean checkInvoiceNumber(String client, String referenceNumber,
			String country) {

		Client clientName = invoicedaoImpl.findByUniqueProperty(Client.class,
				"name", client);

		return invoicedaoImpl.checkInvoiceNumber(clientName, referenceNumber,
				country);
	}

	@Override
	public Boolean checkInvoiceNumberExits(String invoiceReferenceNumber,
			String country, String proformaInvoiceFlag) {

		return invoicedaoImpl.checkInvoiceNumberExits(invoiceReferenceNumber,
				country,proformaInvoiceFlag);
	}

	public String formatNumber(Long value, int length) {
		String pattern = "%0" + length + "d";
		return String.format(pattern, value);
	}

	@Override
	public List<InvoiceAuditDto> getInvoiceStatusHistoryById(Long id) {

		List<InvoiceAudit> statusList = invoicedaoImpl.getInvoiceAudit(id);
		List<InvoiceAuditDto> auditDtos = invoiceBuilder
				.auditToDtoList(statusList);
		Collections.sort(auditDtos, new InvoiceAuditComparator());

		return auditDtos;
	}

	// exporting invoice list
	@Override
	public ByteArrayOutputStream exportInvoiceList(String projectType,
			List<String> status, String fromDate, String toDate,
			String multiText, String datePeriod, String invoiceCountry,
			Boolean intrnalOrNot) throws IOException {
		Employee employee = (Employee) securityUtils
				.getLoggedEmployeeDetailsSecurityContextHolder()
				.get("employee");

		Permission totalInvoiceList = invoicedaoImpl.checkForPermission(
				"Invoices", employee);
		Permission hierarchyInvoiceList = invoicedaoImpl.checkForPermission(
				"Hierarchy Invoice List", employee);

		Map<String, Object> invoicesMap = new HashMap<String, Object>();

		if (totalInvoiceList.getView() && !hierarchyInvoiceList.getView()) {

			invoicesMap = invoicedaoImpl.getInvoiceList(projectType, status,
					fromDate, toDate, multiText, null, null, datePeriod,
					new HashSet<Long>(), invoiceCountry, intrnalOrNot);

		} else if (totalInvoiceList.getView() && hierarchyInvoiceList.getView()) {
			List<Long> managersList = projectServiceImpl
					.mangerUnderManager(employee.getEmployeeId());

			// Here we are getting all projects allocated to logged in employee
			// as well as(manager) reportees of logged in employee
			Set<Long> projectIds = new HashSet<Long>(
					invoicedaoImpl.getProjectsFor(employee, managersList));

			invoicesMap = invoicedaoImpl.getInvoiceList(projectType, status,
					fromDate, toDate, multiText, null, null, datePeriod, null,
					invoiceCountry, intrnalOrNot);

		}

		@SuppressWarnings("unchecked")
		List<Invoice> invoicesList = (List<Invoice>) invoicesMap
				.get("InvoiceList");

		List<InvoiceQueryDTO> invoiceQueryDtoList = invoiceBuilder
				.InvoiceEntityToDToList(invoicesList);

		if (invoiceQueryDtoList != null) {
			Collections.sort(invoiceQueryDtoList,
					new Comparator<InvoiceQueryDTO>() {
						@Override
						public int compare(InvoiceQueryDTO i1,
								InvoiceQueryDTO i2) {
							return i1
									.getClientName()
									.toUpperCase()
									.compareTo(i2.getClientName().toUpperCase());
						}
					});
		}
		// for the above dto List we are exporting through using work book
		// for exporting code start here
		ByteArrayOutputStream bos = new ByteArrayOutputStream();

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
		cell0.setCellValue("Serial Number");
		cell0.setCellStyle(style);

		Cell cell1 = row1.createCell(1);
		cell1.setCellValue("Invoice Number");
		cell1.setCellStyle(style);

		Cell cell2 = row1.createCell(2);
		cell2.setCellValue("Client");
		cell2.setCellStyle(style);

		Cell cell3 = row1.createCell(3);
		cell3.setCellValue("Project");
		cell3.setCellStyle(style);

		Cell cell4 = row1.createCell(4);
		cell4.setCellValue("Project Manager");
		cell4.setCellStyle(style);

		Cell cell5 = row1.createCell(5);
		cell5.setCellValue("Delivery Manager");
		cell5.setCellStyle(style);

		Cell cell6 = row1.createCell(6);
		cell6.setCellValue("Percentage");
		cell6.setCellStyle(style);

		Cell cell7 = row1.createCell(7);
		cell7.setCellValue("Status");
		cell7.setCellStyle(style);

		Cell cell8 = row1.createCell(8);
		cell8.setCellValue("Sent Date");
		cell8.setCellStyle(style);

		Cell cell9 = row1.createCell(9);
		cell9.setCellValue("Received Date");
		cell9.setCellStyle(style);

		Cell cell10 = row1.createCell(10);
		cell10.setCellValue("Total Amount");
		cell10.setCellStyle(style);

		Cell cell11 = row1.createCell(11);
		cell11.setCellValue("Currency");
		cell11.setCellStyle(style);

		Cell cell12 = row1.createCell(12);
		cell12.setCellValue("Rate");
		cell12.setCellStyle(style);

		Cell cell13 = row1.createCell(13);
		cell13.setCellValue("Total Amount(INR)");
		cell13.setCellStyle(style);

		Cell cell14 = row1.createCell(14);
		cell14.setCellValue("Pending Amount");
		cell14.setCellStyle(style);

		Cell cell15 = row1.createCell(15);
		cell15.setCellValue("Pending Amount(INR)");
		cell15.setCellStyle(style);

		for (InvoiceQueryDTO dto : invoiceQueryDtoList) {

			Row row = sheet.createRow(rowIndex++);

			Cell cel0 = row.createCell(0);
			cel0.setCellValue(dto.getInvoicNumber());

			Cell cel1 = row.createCell(1);
			cel1.setCellValue(dto.getNumber() != null ? dto.getNumber() : dto
					.getInvoicNumber());

			Cell cel2 = row.createCell(2);
			cel2.setCellValue(dto.getClientName());

			Cell cel3 = row.createCell(3);
			cel3.setCellValue(dto.getProjectName());

			Cell cel4 = row.createCell(4);
			cel4.setCellValue(dto.getProjectManager());

			Cell cel5 = row.createCell(5);
			cel5.setCellValue(dto.getDeliveryManager());

			Cell cel6 = row.createCell(6);
			cel6.setCellValue(dto.getPercentage());

			Cell cel7 = row.createCell(7);
			cel7.setCellValue(dto.getInvoiceStatus());

			Cell cel8 = row.createCell(8);
			cel8.setCellValue(dto.getInvoiceAmountSentDate());

			Cell cel9 = row.createCell(9);
			cel9.setCellValue(dto.getInvoiceAmountReceviedDate());

			Cell cel10 = row.createCell(10);
			cel10.setCellValue((dto.getTotalAmount().longValue()));

			Long receviedAmount = Long.valueOf(dto.getTotalReceivedAmount());

			Long totalAmount = Long.valueOf(dto.getTotalAmount().toString());

			Cell cel11 = row.createCell(11);
			cel11.setCellValue(dto.getCurrencyType());

			Cell cel12 = row.createCell(12);
			cel12.setCellValue(dto.getRate() != null ? dto.getRate() : 1);

			Cell cel13 = row.createCell(13);
			cel13.setCellValue(dto.getFinalTotalAmount());

			Long finalTotalAmount = Long.valueOf(dto.getFinalTotalAmount());

			Cell cel14 = row.createCell(14);
			cel14.setCellValue(String.valueOf(totalAmount - receviedAmount));

			Cell cel15 = row.createCell(15);
			cel15.setCellValue(dto.getPendingAmount());

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
			sheet.autoSizeColumn(14);
			sheet.autoSizeColumn(15);
		}

		workbook.write(bos);
		bos.flush();
		bos.close();
		// for exporting code end here
		return bos;
	}

	@Override
	public InvoiceDTO getInvoiceSummary(Long invoiceId) {
		return invoiceBuilder.entityToDto(invoicedaoImpl.findBy(Invoice.class,
				invoiceId));
	}

	@Override
	public void trackInvoice(Long invoiceId, String dataString,HttpServletRequest request) {

		String header = "<div style=\"background:#17365d; min-height:40px;\"></div>"
				+ "<div class=\"col-xs-6 pull-right  logo-pdf \">"
				+ "<img src=\"http://www.raybiztech.com/App_Themes/Raybiztech/images/Raybiztech-logo.png\" style=\"padding-right: 15px;\" width=\"197\" height=\"65\" />"
				+ "</div>"
				+ "<div  class=\"col-xs-6 pull-left invoice\">"
				+ "<h2 style=\"font-size:20px; padding-left:15px\">INVOICE</h2>"
				+ "</div>";

		String footer = "<div class=\"row\">"
				+ "<div style=\"padding-left: 15px; padding-right: 15px;\" class=\"queries col-xs-12\">"
				+ "<p>"
				+ "If you have any questions regarding the quotation, please contact"
				+ " Ajay Ray,<br /> <a style=\"color: #000; text-decoration: none;\"mailto:ajay@raybiztech.com\">ajay@raybiztech.com</a>,"
				+ "+1 818 937 4333/+91 9849 743 823." + "</p>" + "</div>"
				+ "</div>"
				+ "<div style=\"background:#17365d; min-height:40px;\"></div>";
		
		String headeraibridge = "<div style=\"background:#577b37; min-height:40px;\"></div>"
				+ "<div class=\"col-xs-6 pull-right  logo-pdf \">"
				+ "<img src=\"http://www.aibridgeml.com/images/logo-default-130x42.png\" style=\"padding-right: 15px;\" width=\"197\" height=\"65\" />"
				+ "</div>"
				+ "<div  class=\"col-xs-6 pull-left invoice\">"
				+ "<h2 style=\"font-size:20px; padding-left:15px\">INVOICE</h2>"
				+ "</div>";

		String footeraibridge = "<div class=\"row\">"
				+ "<div style=\"padding-left: 15px; padding-right: 15px;\" class=\"queries col-xs-12\">"
				+ "<p>"
				+ "If you have any questions regarding the quotation, please contact"
				+ " Ajay Ray,<br /> <a style=\"color: #000; text-decoration: none;\"mailto:ajay@raybiztech.com\">ajay@aibridgeml.com</a>,"
				+ "+1 818 937 4333/+91 9849 743 823." + "</p>" + "</div>"
				+ "</div>"
				+ "<div style=\"background:#577b37; min-height:40px;\"></div>";
		
		String tenantkey =request.getHeader("tenantkey");

		String htmlStr = getCss();
		String body = dataString.trim();
		
		String totalinvoicecontent  = "";
		
		if(tenantkey.equalsIgnoreCase("RAYBIZTECH")){
			 totalinvoicecontent = htmlStr + header + body + footer
					+ "</body></html>";
			}else if(tenantkey.equalsIgnoreCase("AIBRIDGEML")){
				 totalinvoicecontent = htmlStr + headeraibridge + body + footeraibridge
							+ "</body></html>";
			}
/*
		String totalinvoicecontent = htmlStr + header + body + footer
				+ "</body></html>";*/

		String onlyBodyContent = htmlStr + body + "</body></html>";

		InvoiceTracker invoiceTracker = new InvoiceTracker();
		invoiceTracker.setInvoiceId(invoiceId);
		invoiceTracker.setTotalInvoiceContent(totalinvoicecontent);
		invoiceTracker.setOnlybodyContent(onlyBodyContent);
		invoiceTracker.setVersion(invoicedaoImpl
				.getInvoiceTrackerVersion(invoiceId));
		invoiceTracker.setCreatedBy(securityUtils
				.getLoggedEmployeeIdforSecurityContextHolder());
		invoiceTracker.setCreatedTime(new Second());
		invoicedaoImpl.save(invoiceTracker);

	}

	public String getCss() {

		return "<html style='margin:0; padding:0'>"
				+ "<meta charset='UTF-8' />"
				+ "<head>"
				+ "<style>"
				+ "body		{ font-family:Arial, Helvetica, sans-serif; color:#000; font-size:14px; line-height: 1.42857;  }"
				+ "h1,h2,h3,h4,h5,h6 {	font-size:100%;	font-weight:bold; margin:0; }"
				+ "p { margin:0; }"
				+ ".wrapper	{ background:#fff; padding:0;  margin:0 auto; }"
				+ ".container	{ margin:0 auto; float:none; padding-left: 15px; padding-right: 15px; }"
				+ ".invoice	{ float:left;  }"
				+ ".invoice h2	{ font-size:30px; padding:20px 0;   }"
				+ ".logo-pdf		{ text-align:right; padding-top:15px; padding-bottom:15px; float:right; }"
				+ ".logo a		{ display:inline-block; }"

				+ ".address	{ padding-top:15px; padding-bottom:15px; float:left;}"
				+ ".address h2	{ font-size:16px; line-height:20px;  }"
				+ ".address p	{ font-size:16px; line-height:20px;   }"
				+ ".address p span	{ display:block; }"
				+ ".address2	{ float:right; }"
				+ ".address table tr td {"
				+ " padding: 0px 2px;"
				+ "}"

				+ ".details-sec table{"
				+ "	border: none !important;"
				+ "}"
				+ ".details .notes_table td,th{ border:solid 1px #000 !important;padding:8px !important;font-size: 14px !important;}"
				+ ".details-sec table td{padding: 0px !important; }"
				+ ".details td,th	{ border:0 none !important; }"
				+ ".details thead	{ background:#c6d9f1; }"
				+ ".details td b		{ color:#275388; }"

				/*
				 * +
				 * ".details .table-striped > tbody > tr:nth-child(2) { background:#eeece1; }"
				 * +
				 * ".details .table-striped > tbody > tr:nth-child(4) { background:#dbe5f1; }"
				 */
				+ ".details .table-striped>tbody>tr.sub-total td{"
				+ "	background: #eeece1;"
				+ "}"
				+ ".details .table-striped>tbody>tr.grand-total td{"
				+ "	background: #dbe5f1;"
				+ "}"
				+ ".in-words	{ text-align:center; border-bottom:solid 1px #000; padding:2px; }"
				+ ".details h4	{ text-align:center; padding:20px 0 10px; font-size:18px; }"

				+ ".instructions		{ margin-bottom:30px; }"
				+ ".instructions h2	{ padding:12px 0 15px 0; font-size:18px; margin-bottom:0;}"
				+ ".instructions p		{ padding:3px 0;  }"
				+ ".instructions span	{ padding:0; }"
				+ ".instructions td{padding-bottom: 2px; padding-left: 0; padding-top: 2px;}"
				+ ".instructions td { font-size:13px;}"
				+ ".queries			{ padding-bottom:15px; padding-top:15px; }"
				+ ".queries p			{ font-size:16px;  }"
				+ ".queries p a		{ text-decoration:underline; color:#0000ff;  }"

				+ ".header-strip		{ background:#17365d; min-height:40px; display:none; }"
				+ ".footer				{ background:#17365d; min-height:40px; display:none;}"
				+ ".footerHide { display:none;}"
				+ ".headerHide { display:none;}"

				+ ".details-keith table	{ border:solid 1px #000; }"
				+ ".details-keith tr		{ background:none; }"
				+ ".details-keith thead	{ background:none; }"
				+ ".details-keith td		{ border-color:#000 !important; color:#000; }"
				+ ".details-keith th		{  color:#000; }"
				+ ".details-keith thead	{ background:#c6d9f1; }"
				+ ".border-rht				{ border-right:solid 1px #000 !important; }"
				+ ".details-keith h4		{ text-align:center; padding:25px 0; font-size:18px; }"

				+ ".col-xs-6 {"
				+ "    width: 50%;"
				+ "}"
				+ ".clearfix{clear: both;}"

				+ ".pull-right {"
				+ "    float: right !important;"
				+ "}"
				+ ".text-right {"
				+ "  text-align: right;"
				+ "}"
				+ ".text-center {"
				+ "text-align: center;"
				+ "	}"
				+ ".col-sm-9 {"
				+ "	  width: 70%;"
				+ "  float: left; "
				+ "	  box-sizing: border-box;"
				+ "	}"
				+ "	.col-sm-3 {"
				+ "	  width: 25%;"
				+ "  float: left; "
				+ "	  box-sizing: border-box;"
				+ "	}"
				+ ".col-sm-5 {"
				+ "	  width: 41.6667%;"
				+ "	}"
				+ "	.col-sm-1 {"
				+ "	  width: 8.33333%;"
				+ "	}"
				+ "	.col-sm-2 {"
				+ "	  width: 16.6667%;"
				+ "	}"
				+ "	*, *::before, *::after {"
				+ "	  box-sizing: border-box;"
				+ "	}"
				+ ".table {"
				+ "  margin-bottom: 20px;"
				+ "  max-width: 100%;"
				+ "  width: 100%;"
				+ " background-color: transparent;"
				+ "  border-collapse: collapse;"
				+ "border-spacing: 0;"
				+ "}"
				+ ".table > tbody > tr > td, .table > tbody > tr > th, .table > tfoot > tr > td, .table > tfoot > tr > th, .table > thead > tr > td, .table > thead > tr > th {"
				+ "  border-top: 1px solid #ddd;" + " line-height: 1.42857;"
				+ " padding: 8px;" + "  vertical-align: top;" + "}"
				+ ".table-striped > tbody > tr:nth-of-type(2n+1) {"
				+ "  background-color: #f9f9f9;" + "}" + ".col-sm-2 {"
				+ "  width: 25%;" + "  float: left;" + "}" + ".pad-lft-rht{"
				+ "	padding:0 15px;" + "}"
				+ ".pad-lft-rht { padding-left:0; padding-right:0; }"

				+ "@page {" + "        size: A4;"
				+ "        margin: 0 auto; border: 1px solid red;" + "}"

				+ "@media print {" + "        html, body {"
				+ "           width: 210mm;" + "           height: 297mm; "
				+ "       }" + "        .container {" + "			max-width: 210mm; "
				+ "			min-height: 297mm;" + "            margin: 0 auto;"
				+ "            border: initial;"
				+ "            border-radius: initial;"
				+ "            width: initial;"
				+ "            min-height: initial;"
				+ "           box-shadow: initial;"
				+ "           background: initial;"
				+ "            page-break-after: always;" + "        }"
				+ "		.wrapper {" + "			width: 210mm; " + "			}"

				+ "}" + "</style></head>" + "<body>";

	}

	@Override
	public List<InvoiceTracker> getInvoiceVersions(Long invoiceId) {
		return invoicedaoImpl.getInvoiceTrackers(invoiceId);
	}

	@Override
	public void invoiceReminderlog(InvoiceReminderLogDTO dto,HttpServletRequest request) {
		InvoiceReminderLog invoiceReminderLog = new InvoiceReminderLog();
		if (dto != null) {
			String[] invoiceno = dto.getInvoiceId().split(",");
			for (String ing : invoiceno) {
				invoiceReminderLog = invoiceBuilder.reminderLogToentity(dto);
				invoiceReminderLog.setInvoiceId(ing);
				invoicedaoImpl.save(invoiceReminderLog);
			}
		}

		invoiceReminderAcknowledgement.sendInvoiceReminder(dto,request);
	}

	@Override
	public List<InvoiceReminderLogDTO> getReminderLogs(Long invoiceId) {
		List<InvoiceReminderLogDTO> logs = invoiceBuilder
				.invoicereminderLogentityListToDTOList(invoicedaoImpl
						.getReminderLogs(invoiceId.toString()));
		return logs;
	}

	/*
	 * @Override public List<String> getReminderDescription() {
	 * 
	 * List<String> resultList = new ArrayList<String>(); String
	 * result=mailTemplatesDaoImpl.getMailContent("Invoice Reminder");
	 * resultList.add(result); return resultList; }
	 */

	@Override
	public String getReminderDescription() {

		return mailTemplatesDaoImpl.getMailContent("Invoice Reminder");
	}

	@Override
	public void allowToAdjustInvoice(Long invoiceId) {
		Invoice invoice = invoicedaoImpl.findBy(Invoice.class, invoiceId);
		invoice.setCanBeAdjusted(Boolean.TRUE);
		invoicedaoImpl.update(invoice);
	}

	@Override
	public void restrictToAdjustInvoice(Long invoiceId) {
		Invoice invoice = invoicedaoImpl.findBy(Invoice.class, invoiceId);
		invoice.setCanBeAdjusted(Boolean.FALSE);
		invoicedaoImpl.update(invoice);
	}

	public String getMonthForInt(int num) {
		String month = "wrong";
		DateFormatSymbols dfs = new DateFormatSymbols();
		String[] months = dfs.getMonths();
		if (num >= 0 && num <= 11) {
			month = months[num];
		}
		return month;
	}

	@Override
	public Map<String, Object> getInitData() {

		Map<String, Object> map = new HashMap<String, Object>();

		map.put("lookup", invoicedaoImpl.get(Currency.class));
		map.put("data", invoicedaoImpl.get(CurrencyToINR.class));

		return map;
	}

	@Override
	public void addInrToCurrency(String currencyType, Long inrAmount) {
		CurrencyToINR currencyToINR2 = new CurrencyToINR();
		currencyToINR2.setInrAmount(inrAmount);

		CurrencyToINR currency = invoicedaoImpl.findByUniqueProperty(
				CurrencyToINR.class, "currenyType", currencyType);

		if (currency == null) {
			currencyToINR2.setCurrenyType(currencyType);
			currencyToINR2.setCreatedBy(securityUtils
					.getLoggedEmployeeIdforSecurityContextHolder());
			currencyToINR2.setCreatedDate(new Second());

			invoicedaoImpl.save(currencyToINR2);
		} else {
			throw new RuntimeException();
		}
	}

	@Override
	public void updateInrToCurrency(Long id, Long amount) {
		CurrencyToINR persitObject = invoicedaoImpl.findBy(CurrencyToINR.class,
				id);
		persitObject.setInrAmount(amount);
		persitObject.setUpdatedBy(securityUtils
				.getLoggedEmployeeIdforSecurityContextHolder());
		persitObject.setUpdatedDate(new Second());
		invoicedaoImpl.update(persitObject);

	}

	@Override
	public ByteArrayOutputStream exportProjectFinancialList(String fromDate,
			String toDate) throws IOException {

		Map<String, Object> datemap = this.getConvertedDates(fromDate, toDate);

		Map<String, Long> converter = this.getConverterAmounts();

		Date startDate = (Date) datemap.get("startDate");
		Date endDate = (Date) datemap.get("endDate");

		List<Invoice> invoices = invoicedaoImpl.getInvoicesRaisedBetween(
				startDate, endDate);

		ByteArrayOutputStream bos = new ByteArrayOutputStream();

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
		cell0.setCellValue("Client Code");
		cell0.setCellStyle(style);

		Cell cell1 = row1.createCell(1);
		cell1.setCellValue("Client");
		cell1.setCellStyle(style);

		Cell cell2 = row1.createCell(2);
		cell2.setCellValue("Project");
		cell2.setCellStyle(style);

		Cell cell3 = row1.createCell(3);
		cell3.setCellValue("Project Manager");
		cell3.setCellStyle(style);

		Cell cell4 = row1.createCell(4);
		cell4.setCellValue("Delivery Manager");
		cell4.setCellStyle(style);

		Cell cell5 = row1.createCell(5);
		cell5.setCellValue("Type");
		cell5.setCellStyle(style);

		Cell cell6 = row1.createCell(6);
		cell6.setCellValue("Pricing Model");
		cell6.setCellStyle(style);

		Cell cell7 = row1.createCell(7);
		cell7.setCellValue("Milestone");
		cell7.setCellStyle(style);

		Cell cell8 = row1.createCell(8);
		cell8.setCellValue("Serial Number");
		cell8.setCellStyle(style);

		Cell cell9 = row1.createCell(9);
		cell9.setCellValue("Invoice Number");
		cell9.setCellStyle(style);

		Cell cell10 = row1.createCell(10);
		cell10.setCellValue("Invoice Status");
		cell10.setCellStyle(style);

		Cell cell11 = row1.createCell(11);
		cell11.setCellValue("Raised Date");
		cell11.setCellStyle(style);

		Cell cell12 = row1.createCell(12);
		cell12.setCellValue("Due Date");
		cell12.setCellStyle(style);

		Cell cell13 = row1.createCell(13);
		cell13.setCellValue("Sent Date");
		cell13.setCellStyle(style);

		Cell cell14 = row1.createCell(14);
		cell14.setCellValue("Received Date");
		cell14.setCellStyle(style);

		Cell cell15 = row1.createCell(15);
		cell15.setCellValue("Amount 1");
		cell15.setCellStyle(style);

		Cell cell16 = row1.createCell(16);
		cell16.setCellValue("Exch");
		cell16.setCellStyle(style);

		Cell cell17 = row1.createCell(17);
		cell17.setCellValue("Amount 2");
		cell17.setCellStyle(style);

		Cell cell18 = row1.createCell(18);
		cell18.setCellValue("Currency");
		cell18.setCellStyle(style);

		for (Invoice invoice : invoices) {

			Row row = sheet.createRow(rowIndex++);

			AES256Encryption aes256Encryption = new AES256Encryption(
					String.valueOf(invoice.getId()), invoice.getSaltKey());

			Milestone milestone = invoice.getMilsestone();
			Project project = (milestone != null) ? milestone.getProject()
					: null;
			Client client = (project != null) ? project.getClient() : null;

			Employee deliveryManager = (project != null) ? resourceManagementDAO
					.getDeliveryManagerofProject(project) : null;

			Cell cel0 = row.createCell(0);
			cel0.setCellValue(client != null ? client.getClientCode() : "N/A");

			Cell cel1 = row.createCell(1);
			cel1.setCellValue(client != null ? client.getPersonName() : "N/A");

			Cell cel2 = row.createCell(2);
			cel2.setCellValue(project != null ? project.getProjectName()
					: "N/A");

			Cell cel3 = row.createCell(3);
			cel3.setCellValue(project != null ? project.getProjectManager()
					.getFullName() : "N/A");

			Cell cel4 = row.createCell(4);
			cel4.setCellValue(deliveryManager != null ? deliveryManager
					.getFullName() : "N/A");

			Cell cel5 = row.createCell(5);
			cel5.setCellValue(project != null ? project.getType().toString()
					: "N/A");

			Cell cel6 = row.createCell(6);
			cel6.setCellValue(project != null ? project.getType().toString()
					: "N/A");

			Cell cel7 = row.createCell(7);
			cel7.setCellValue(milestone != null ? milestone.getTitle() : "N/A");

			Cell cel8 = row.createCell(8);
			cel8.setCellValue(invoice.getNumber());

			Cell cel9 = row.createCell(9);
			cel9.setCellValue(invoice.getInvoiceNumber() != null ? invoice
					.getInvoiceNumber() : invoice.getNumber());

			Cell cel10 = row.createCell(10);
			cel10.setCellValue(invoice.getInvoiceStatus());

			Cell cel11 = row.createCell(11);
			cel11.setCellValue(invoice.getInvoiceDate() != null ? invoice
					.getInvoiceDate().toString("dd/MM/yyyy") : "N/A");

			Cell cel12 = row.createCell(12);
			cel12.setCellValue(invoice.getDueDate() != null ? invoice
					.getDueDate().toString("dd/MM/yyyy") : "N/A");

			Cell cel13 = row.createCell(13);
			cel13.setCellValue(invoice.getInvoiceAmountSentDate() != null ? invoice
					.getInvoiceAmountSentDate().toString("dd/MM/yyyy") : "N/A");

			Cell cel14 = row.createCell(14);
			cel14.setCellValue(invoice.getInvoiceAmountReceviedDate() != null ? invoice
					.getInvoiceAmountReceviedDate().toString("dd/MM/yyyy")
					: "N/A");

			Double totalAmount = Double.valueOf(aes256Encryption
					.decrypt(invoice.getTotalAmount()));

			Cell cel15 = row.createCell(15);
			cel15.setCellValue(totalAmount);

			String currency = invoice.getRemittance().getCurrencyType();

			Long currencyValue = (!currency.equalsIgnoreCase("INR")) ? (converter
					.get(currency) != null ? converter.get(currency) : 1) : 1;

			/*
			 * Long currencyValue = (!currency.equalsIgnoreCase("INR")) ?
			 * converter .get(currency) : 1;
			 */

			Cell cel16 = row.createCell(16);
			cel16.setCellValue(currencyValue);

			Cell cel17 = row.createCell(17);
			cel17.setCellValue(Double.valueOf(totalAmount * currencyValue));

			Cell cel18 = row.createCell(18);
			cel18.setCellValue(invoice.getRemittance().getCurrencyType());

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
			sheet.autoSizeColumn(14);
			sheet.autoSizeColumn(15);
			sheet.autoSizeColumn(16);
			sheet.autoSizeColumn(17);
			sheet.autoSizeColumn(18);

		}

		workbook.write(bos);
		bos.flush();
		bos.close();
		// for exporting code end here
		return bos;

	}

	@Override
	public InvoiceQueryDTO getLatestInvoiceVersion(Long invoiceId) {
		InvoiceTracker tracker = invoicedaoImpl.latestInvoiceVersion(invoiceId);
		InvoiceQueryDTO dto = getInvoice(invoiceId);
		if (tracker != null) {

			if (tracker.getOnlybodyContent().contains("?")) {
				if (dto.getCurrencyType().equalsIgnoreCase("INR")) {
					String bodycontent = tracker.getOnlybodyContent().replace(
							"?", "₹");
					dto.setOnlybodyContent(bodycontent);
				}
			} else {
				dto.setOnlybodyContent(tracker.getOnlybodyContent());
			}
		}
		return dto;
	}

	@Override
	public void addAddress(CountryAddressDTO dto) {
		invoicedaoImpl.addAddressDetails(countryAddressBuilder
				.convertDTOToEntity(dto));
	}

	@Override
	public List<CountryAddressDTO> getAddressDetailsList() {
		return countryAddressBuilder.convertEntityListToDTOList(invoicedaoImpl
				.getAddressDetailsList());
	}

	@Override
	public void updateAddressDetails(CountryAddressDTO countryAddressDTO) {
		CountryAddress countryAddress = countryAddressBuilder
				.toEditEntity(countryAddressDTO);
		dao.update(countryAddress);
	}

	@Override
	public void deleteAddress(Long addressId) {

		dao.delete(dao.findBy(CountryAddress.class, addressId));

	}

	@Override
	public Map<String, Object> invoiceAuditLog(String startdate,
			String enddate, Integer firstIndex, Integer endIndex,
			String invoiceDatePeriod) {

		Employee employee = (Employee) securityUtils
				.getLoggedEmployeeDetailsSecurityContextHolder()
				.get("employee");

		Permission totalInvoiceList = invoicedaoImpl.checkForPermission(
				"Invoices", employee);
		Map<String, Object> ListofInvoiceMap = new HashMap<>();

		if (totalInvoiceList.getView()) {
			Date fromDate = null;
			Date toDate = null;

			Map<String, Date> mapValue = new HashMap<String, Date>();
			if (invoiceDatePeriod.equalsIgnoreCase("custom")) {
				try {
					fromDate = DateParser.toDate(startdate);
					toDate = DateParser.toDate(enddate);
				} catch (ParseException ex) {
					java.util.logging.Logger.getLogger(
							ProjectServiceImpl.class.getName()).log(
							Level.SEVERE, null, ex);
				}
			} else {
				mapValue = resourceManagementDAO
						.getCustomDates(invoiceDatePeriod);
				fromDate = mapValue.get("startDate");
				toDate = mapValue.get("endDate");
			}

			Map<String, Object> invoiceAuditList = invoicedaoImpl
					.invoiceAuditLog(fromDate, toDate, firstIndex, endIndex);

			List<InvoiceAuditDto> invoiceAuditListDto = invoiceBuilder
					.invoiceAuditLog((List<InvoiceAudit>) invoiceAuditList
							.get("list"));

			ListofInvoiceMap.put("InvoiceAuditSize",
					invoiceAuditList.get("listSize"));
			ListofInvoiceMap.put("InvoiceAudit", invoiceAuditListDto);

		}

		return ListofInvoiceMap;

	}

	@Override
	public Map<String, String> createInvoiceSerialNumber(String country,String proformaInvoiceFlag) {

		StringBuilder builder = new StringBuilder();
		
     String patternforInvoice = invoicedaoImpl.getInvoicePattern();
		
		logger.warn("service"+patternforInvoice);
		
		if(patternforInvoice != null){
			logger.warn("patternforInvoice"+patternforInvoice);
			if(proformaInvoiceFlag.equalsIgnoreCase("true")){
				builder.append("P").append(patternforInvoice);
			}
			else{
			builder.append(patternforInvoice);
			}
		}
	//	builder.append("RB");

		/*
		 * int month = new Date().getMonthOfYear().getValue() + 1;
		 * 
		 * builder.append(String.format("%02d", month)).append(
		 * String.valueOf(new Date().getYearOfEra().getValue()).substring( 2,
		 * 4));
		 */

		if (country.equalsIgnoreCase("INDIA")) {
			builder.append((country != null) ? country.subSequence(0, 2) : "");
		} else {
			builder.append("FC");
		}
		// builder.append((country != null) ? country.subSequence(0, 2) : "");

		int month = new Date().getMonthOfYear().getValue() + 1;

		builder.append(String.valueOf(new Date().getYearOfEra().getValue())
				.substring(2, 4));
		
		String maxinvoiceNumber=null,maxNumber;
		
		if(proformaInvoiceFlag.equalsIgnoreCase("true")){
			maxinvoiceNumber = invoicedaoImpl.getLatestProformaInvoice(country);
			
		}
		else{
			 maxinvoiceNumber = invoicedaoImpl
					.getLatestInvoiceNumberForCountry(country);
			 logger.warn("max ref no:"+maxinvoiceNumber);
			
		}

		if (maxinvoiceNumber != null) {
			Long invoiceMaxNumber = Long.valueOf(maxinvoiceNumber);
			maxNumber = formatNumber(invoiceMaxNumber + 1,
					maxinvoiceNumber.length());
		} else {
			maxNumber = "001";
		}

		Map<String, String> map = new HashMap<String, String>();
		map.put("invoicePattern", builder.toString());
		map.put("invoiceNumber", maxNumber);

		return map;
	}

	@Override
	public Map<String, Object> getInvoicesOfMilestone(Long milestoneId) {
		Map<String, Object> map = new HashMap<>();
		Milestone milestone = dao.findBy(Milestone.class, milestoneId);
		List<Invoice> invoList = invoicedaoImpl
				.getinvoiceForMilestone(milestone);
		List<InvoiceQueryDTO> invoiceQueryDtoList = invoiceBuilder
				.InvoiceEntityToDToList(invoList);
		map.put("list", invoiceQueryDtoList);
		map.put("listSize", invoList.size());
		return map;
	}

	@Override
	public Boolean checkMilestoneExits(Long milestoneid) {
		// invoicedaoImpl
		return invoicedaoImpl.checkMilestoneExits(milestoneid);
	}

	// Below method is used to get client audit report

	@Override
	public Map<String, Object> clientInvoiceAuditReport(String fromDate,
			String toDate, String multiText, String selectionStatus,Boolean pendingAmountFlag) {

		Map<String, Object> returnObject = new HashMap<String, Object>();
		Map<String, Object> datemap = this.getConvertedDates(fromDate, toDate);

		Date startDate = (Date) datemap.get("startDate");
		Date endDate = (Date) datemap.get("endDate");

		Map<String, Object> map = invoicedaoImpl.getClientsAuditReport(
				startDate.toString("yyyy-MM-dd"),
				endDate.toString("yyyy-MM-dd"), multiText, selectionStatus ,pendingAmountFlag);

		List<InvoiceAudit> audits = (List<InvoiceAudit>) map.get("InvoiceList");

		// This will convert to month wise
		List<InvoiceAuditReportDto> auditReportDtos = this.getClients(audits);

		returnObject.put("displayStartDate", startDate.toString("dd/MM/yyyy"));
		returnObject.put("displayEndDate", endDate.toString("dd/MM/yyyy"));
		returnObject.put("result", auditReportDtos);

		return returnObject;
	}

	public List<InvoiceAuditReportDto> getClients(List<InvoiceAudit> audits) {

		Map<Integer, InvoiceAuditReportDto> yearlyAudit = new TreeMap<Integer, InvoiceAuditReportDto>();

		Map<Long, InvoiceAuditReportDto> clientsList = new TreeMap<Long, InvoiceAuditReportDto>();

		Map<String, Long> converter = this.getConverterAmounts();

		List<String> months = new ArrayList<String>(
				Arrays.asList(new DateFormatSymbols().getMonths()));

		for (InvoiceAudit audit : audits) {
			Long refNo = audit.getInvoiceId();
			List invoiceRef = invoicedaoImpl.getRefrenceInvoice(refNo);
			if(invoiceRef.isEmpty()){
			Long totalTaxAmount = 0L;
			InvoiceAuditDto auditDto = invoiceBuilder.getAuditReportDtos(audit);

			Long amountInr = converter.get(auditDto.getCurrencyType());
			
			Date date = null;

			if (audit.getInvoiceSentDate() != null
					&& audit.getInvoiceAmountReceviedDate() == null) {
				date = audit.getInvoiceSentDate();
			} else {
				date = audit.getInvoiceAmountReceviedDate();
			}

			String clientName = audit.getClientname();

			Invoice invoice = dao.findBy(Invoice.class, audit.getInvoiceId());

			Milestone milestone = dao.findBy(Milestone.class, invoice
					.getMilsestone().getId());

			Project project = dao.findBy(Project.class, milestone.getProject()
					.getId());

			Client client = dao.findBy(Client.class, project.getClient()
					.getId());

			Long clientId = client.getId();


			String projectType = auditDto.getProjectType();

			projectType = (projectType != null) ? projectType : "fixedbid";

			Long invoiceAmount = Math.round((projectType
					.equalsIgnoreCase("fixedbid") ? Double.valueOf(auditDto
					.getAmount()) : Double.valueOf(auditDto.getSubTotal())));

			String reportStatus = auditDto.getInvoiceStatus();

			Long totalAmount = Math.round(Double.valueOf(auditDto
					.getTotalAmount()));

			Long receivedAmount = Math.round(Double.valueOf(auditDto
					.getReceivedAmount()));

			Long balanceAmount = Math.round(Double.valueOf(auditDto
					.getBalanceAmount()));

			Long tdsAmount = 0L;
			if (auditDto.getTdsAmount() != null) {
				tdsAmount = Math.round(Double.valueOf(auditDto.getTdsAmount()));
			}

			Long netAmount = 0L;
			if (auditDto.getNetAmount() != null) {
				netAmount = Math.round(Double.valueOf(auditDto.getNetAmount()));
			}

			Long convertedInvoiceAmount = 0L;
			Long convertedTotalAmount = 0L;
			Long convertedReceivedAmount = 0L;
			Long convertedBalanceAmount = 0L;
			Long convertedTdsamount = 0L;
			Long convertedNetAmount = 0L;
			/*// Either Invoice or Proforma will add in total
			if(auditDto.getProformaReferenceNo() == null){*/
				if (!auditDto.getCurrencyType().equalsIgnoreCase("INR")) {

					convertedInvoiceAmount = invoiceAmount
							* (amountInr != null ? amountInr : 1);

					convertedTotalAmount = totalAmount
							* (amountInr != null ? amountInr : 1);

					convertedReceivedAmount = receivedAmount
							* (amountInr != null ? amountInr : 1);

					convertedBalanceAmount = balanceAmount
							* (amountInr != null ? amountInr : 1);

					if (tdsAmount != null) {
						convertedTdsamount = tdsAmount
								* (amountInr != null ? amountInr : 1);
					}

					if (netAmount != null) {
						convertedNetAmount = netAmount
								* (amountInr != null ? amountInr : 1);
					}

				} else {
					convertedInvoiceAmount = invoiceAmount;
					convertedTotalAmount = totalAmount;
					convertedReceivedAmount = receivedAmount;
					convertedBalanceAmount = balanceAmount;
					if (tdsAmount != null) {
						convertedTdsamount = tdsAmount;
					}
					if (netAmount != null) {
						convertedNetAmount = netAmount;
					}
				}

			//}
			

			if (clientsList.containsKey(clientId)) {
				
				InvoiceAuditReportDto invoiceAuditReportDto = clientsList
						.get(clientId);
				//invoiceAuditReportDto.getAuditDtos().add(auditDto);
				invoiceAuditReportDto.setClientName(clientName);
				invoiceAuditReportDto.setClientId(clientId);
				invoiceAuditReportDto
						.setInvoiceAmountTotal(invoiceAuditReportDto
								.getInvoiceAmountTotal()
								+ convertedInvoiceAmount);
				invoiceAuditReportDto.setFinalAmountTotal(invoiceAuditReportDto
						.getFinalAmountTotal() + convertedTotalAmount);
				invoiceAuditReportDto
						.setReceivedAmountTotal(invoiceAuditReportDto
								.getReceivedAmountTotal()
								+ convertedReceivedAmount);
				invoiceAuditReportDto
						.setBalanceAmountTotal(invoiceAuditReportDto
								.getBalanceAmountTotal()
								+ convertedBalanceAmount);
				invoiceAuditReportDto.setInvoiceStatus(reportStatus);

				invoiceAuditReportDto.setTotalTaxAmount(invoiceAuditReportDto
						.getTotalTaxAmount() + totalTaxAmount);

				invoiceAuditReportDto.setTdsAmountTotal(invoiceAuditReportDto
						.getTdsAmountTotal() != null ? invoiceAuditReportDto
						.getTdsAmountTotal() + convertedTdsamount : null);
				invoiceAuditReportDto.setNetAmountTotal(invoiceAuditReportDto
						.getNetAmountTotal() != null ? invoiceAuditReportDto
						.getNetAmountTotal() + convertedNetAmount : null);

			} else {
				// logger.warn("in else");
				InvoiceAuditReportDto auditReportDto = new InvoiceAuditReportDto();

				List<InvoiceAuditDto> auditDtos = new ArrayList<InvoiceAuditDto>();
				//auditDtos.add(auditDto);
				auditReportDto.setAuditDtos(auditDtos);
				auditReportDto.setClientName(clientName);
				auditReportDto.setClientId(clientId);

				auditReportDto.setInvoiceAmountTotal(convertedInvoiceAmount);
				auditReportDto.setFinalAmountTotal(convertedTotalAmount);
				auditReportDto.setReceivedAmountTotal(convertedReceivedAmount);
				auditReportDto.setBalanceAmountTotal(convertedBalanceAmount);
				auditReportDto.setInvoiceStatus(reportStatus);
				auditReportDto.setTotalTaxAmount(totalTaxAmount);
				auditReportDto
						.setTdsAmountTotal(convertedTdsamount != null ? convertedTdsamount
								: null);
				auditReportDto
						.setNetAmountTotal(convertedNetAmount != null ? convertedNetAmount
								: null);

				clientsList.put(clientId, auditReportDto);
			}
		}// If block
		}// For loop
		

		return new ArrayList<InvoiceAuditReportDto>(clientsList.values());

	}

	@Override
	public Map<String, Object> mopnthlyInvoiceAudit(String monthName,
			String year, String invoiceStatus ,String country) {

		Map<String, Object> returnObject = new HashMap<String, Object>();

		Integer yearValue = Integer.parseInt(year);

		Integer monthvalue;

		java.util.Date date = null;

		try {
			date = new SimpleDateFormat("MMMM").parse(monthName);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		monthvalue = cal.get(Calendar.MONTH);


		DateRange monthPeriod = leaveManagementUtils.constructMonthPeriod(
				monthvalue, yearValue);


		Map<String, Object> map = invoicedaoImpl.getMonthlyAuditReport(
				monthPeriod.getMinimum().toString("yyyy-MM-dd"), monthPeriod
						.getMaximum().toString("yyyy-MM-dd"), invoiceStatus ,country);

		List<InvoiceAudit> audits = (List<InvoiceAudit>) map.get("InvoiceList");

		List<InvoiceAuditDto> auditReportDtos = (List<InvoiceAuditDto>) invoiceBuilder
				.auditToDtoLists(audits);

		returnObject.put("result", auditReportDtos);

		return returnObject;
	}

	@Override
	public Map<String, Object> clientsProjectsAudit(Long clientId,
			String displayStartDate, String displayEndDate ,Boolean pendingAmountFlag) {

		Map<String, Object> returnObject = new HashMap<String, Object>();

		Date formateddStringstartdate = null;

		Date formattedEnddate = null;

		try {
			formateddStringstartdate = DateParser.toDate(displayStartDate);
			formattedEnddate = DateParser.toDate(displayEndDate);

		} catch (ParseException e) {
			e.printStackTrace();
		}


		Map<String, Object> map = invoicedaoImpl.getClientsProjectAuditReport(
				clientId, formateddStringstartdate.toString("yyyy-MM-dd"),
				formattedEnddate.toString("yyyy-MM-dd") ,pendingAmountFlag);

		List<InvoiceAudit> audits = (List<InvoiceAudit>) map.get("InvoiceList");

		List<InvoiceAuditReportDto> auditReportDtos = this
				.getClientsProjectsReport(audits);

		returnObject.put("result", auditReportDtos);

		return returnObject;
	}

	public List<InvoiceAuditReportDto> getClientsProjectsReport(
			List<InvoiceAudit> audits) {

		Map<Integer, InvoiceAuditReportDto> yearlyAudit = new TreeMap<Integer, InvoiceAuditReportDto>();

		Map<Long, InvoiceAuditReportDto> projectsList = new TreeMap<Long, InvoiceAuditReportDto>();

		Map<String, Long> converter = this.getConverterAmounts();

		List<String> months = new ArrayList<String>(
				Arrays.asList(new DateFormatSymbols().getMonths()));

		for (InvoiceAudit audit : audits) {
			
			Long refNo = audit.getInvoiceId();
			List<InvoiceAudit> invoiceRef = invoicedaoImpl.getRefrenceInvoice(refNo);
			if(invoiceRef.isEmpty()){
				
			
			Long totalTaxAmount = 0L;	
			InvoiceAuditDto auditDto = invoiceBuilder.getAuditReportDtos(audit);

			Long amountInr = converter.get(auditDto.getCurrencyType());

			Date date = null;

			if (audit.getInvoiceSentDate() != null
					&& audit.getInvoiceAmountReceviedDate() == null) {
				date = audit.getInvoiceSentDate();
			} else {
				date = audit.getInvoiceAmountReceviedDate();
			}

			String clientName = audit.getClientname();

			Invoice invoice = dao.findBy(Invoice.class, audit.getInvoiceId());

			Milestone milestone = dao.findBy(Milestone.class, invoice
					.getMilsestone().getId());

			Project project = dao.findBy(Project.class, milestone.getProject()
					.getId());

			Client client = dao.findBy(Client.class, project.getClient()
					.getId());

			Long clientId = client.getId();

			Long projectId = project.getId();


			String projectType = auditDto.getProjectType();

			projectType = (projectType != null) ? projectType : "fixedbid";

			Long invoiceAmount = Math.round((projectType
					.equalsIgnoreCase("fixedbid") ? Double.valueOf(auditDto
					.getAmount()) : Double.valueOf(auditDto.getSubTotal())));

			String reportStatus = auditDto.getInvoiceStatus();

			Long totalAmount = Math.round(Double.valueOf(auditDto
					.getTotalAmount()));

			Long receivedAmount = Math.round(Double.valueOf(auditDto
					.getReceivedAmount()));

			Long balanceAmount = Math.round(Double.valueOf(auditDto
					.getBalanceAmount()));

			Long tdsAmount = 0L;
			if (auditDto.getTdsAmount() != null) {
				tdsAmount = Math.round(Double.valueOf(auditDto.getTdsAmount()));
			}

			Long netAmount = 0L;
			if (auditDto.getNetAmount() != null) {
				netAmount = Math.round(Double.valueOf(auditDto.getNetAmount()));
			}

			Long convertedInvoiceAmount = 0L;
			Long convertedTotalAmount = 0L;
			Long convertedReceivedAmount = 0L;
			Long convertedBalanceAmount = 0L;
			Long convertedTdsamount = 0L;
			Long convertedNetAmount = 0L;
			if (!auditDto.getCurrencyType().equalsIgnoreCase("INR")) {

				convertedInvoiceAmount = invoiceAmount
						* (amountInr != null ? amountInr : 1);

				convertedTotalAmount = totalAmount
						* (amountInr != null ? amountInr : 1);

				convertedReceivedAmount = receivedAmount
						* (amountInr != null ? amountInr : 1);

				convertedBalanceAmount = balanceAmount
						* (amountInr != null ? amountInr : 1);

				if (tdsAmount != null) {
					convertedTdsamount = tdsAmount
							* (amountInr != null ? amountInr : 1);
				}

				if (netAmount != null) {
					convertedNetAmount = netAmount
							* (amountInr != null ? amountInr : 1);
				}

			} else {
				convertedInvoiceAmount = invoiceAmount;
				convertedTotalAmount = totalAmount;
				convertedReceivedAmount = receivedAmount;
				convertedBalanceAmount = balanceAmount;
				if (tdsAmount != null) {
					convertedTdsamount = tdsAmount;
				}
				if (netAmount != null) {
					convertedNetAmount = netAmount;
				}
			}

			

			if (projectsList.containsKey(projectId)) {

				InvoiceAuditReportDto invoiceAuditReportDto = projectsList
						.get(projectId);
				//invoiceAuditReportDto.getAuditDtos().add(auditDto);
				invoiceAuditReportDto.setClientName(clientName);
				invoiceAuditReportDto.setClientId(clientId);
				invoiceAuditReportDto.setProjectId(projectId);
				invoiceAuditReportDto.setProjectName(audit.getProjectName());
				invoiceAuditReportDto
						.setInvoiceAmountTotal(invoiceAuditReportDto
								.getInvoiceAmountTotal()
								+ convertedInvoiceAmount);
				invoiceAuditReportDto.setFinalAmountTotal(invoiceAuditReportDto
						.getFinalAmountTotal() + convertedTotalAmount);
				invoiceAuditReportDto
						.setReceivedAmountTotal(invoiceAuditReportDto
								.getReceivedAmountTotal()
								+ convertedReceivedAmount);
				invoiceAuditReportDto
						.setBalanceAmountTotal(invoiceAuditReportDto
								.getBalanceAmountTotal()
								+ convertedBalanceAmount);
				invoiceAuditReportDto.setInvoiceStatus(reportStatus);

				invoiceAuditReportDto.setTotalTaxAmount(invoiceAuditReportDto
						.getTotalTaxAmount() + totalTaxAmount);

				invoiceAuditReportDto.setTdsAmountTotal(invoiceAuditReportDto
						.getTdsAmountTotal() != null ? invoiceAuditReportDto
						.getTdsAmountTotal() + convertedTdsamount : null);
				invoiceAuditReportDto.setNetAmountTotal(invoiceAuditReportDto
						.getNetAmountTotal() != null ? invoiceAuditReportDto
						.getNetAmountTotal() + convertedNetAmount : null);

			} else {
				InvoiceAuditReportDto auditReportDto = new InvoiceAuditReportDto();

				List<InvoiceAuditDto> auditDtos = new ArrayList<InvoiceAuditDto>();
				//auditDtos.add(auditDto);
				auditReportDto.setAuditDtos(auditDtos);
				auditReportDto.setClientName(clientName);
				auditReportDto.setClientId(clientId);
				auditReportDto.setProjectId(projectId);
				auditReportDto.setProjectName(audit.getProjectName());
				auditReportDto.setInvoiceAmountTotal(convertedInvoiceAmount);
				auditReportDto.setFinalAmountTotal(convertedTotalAmount);
				auditReportDto.setReceivedAmountTotal(convertedReceivedAmount);
				auditReportDto.setBalanceAmountTotal(convertedBalanceAmount);
				auditReportDto.setInvoiceStatus(reportStatus);
				auditReportDto.setTotalTaxAmount(totalTaxAmount);
				auditReportDto
						.setTdsAmountTotal(convertedTdsamount != null ? convertedTdsamount
								: null);
				auditReportDto
						.setNetAmountTotal(convertedNetAmount != null ? convertedNetAmount
								: null);

				projectsList.put(projectId, auditReportDto);
			}
			}
		}
		

		return new ArrayList<InvoiceAuditReportDto>(projectsList.values());

	}

	@Override
	public Map<String, Object> clientsProjectInvoicesAudit(Long projectId,
			String displayStartDate, String displayEndDate ,Boolean pendingAmountFlag) {

		Map<String, Object> returnObject = new HashMap<String, Object>();

		Date formateddStringstartdate = null;

		Date formattedEnddate = null;

		try {
			formateddStringstartdate = DateParser.toDate(displayStartDate);
			formattedEnddate = DateParser.toDate(displayEndDate);

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Map<String, Object> map = invoicedaoImpl.getClientProjectInvoice(
				projectId, formateddStringstartdate.toString("yyyy-MM-dd"),
				formattedEnddate.toString("yyyy-MM-dd"),pendingAmountFlag);

		List<InvoiceAudit> audits = (List<InvoiceAudit>) map.get("InvoiceList");

		List<InvoiceAuditDto> auditReportDtos = (List<InvoiceAuditDto>) invoiceBuilder
				.auditToDtoLists(audits);

		returnObject.put("result", auditReportDtos);

		return returnObject;

	}

	
	@Override
	public MilestoneDTO getMilestone(Long milestoneId) {
		// TODO Auto-generated method stub
		Milestone mile = invoicedaoImpl.findBy(Milestone.class, milestoneId);
		MilestoneDTO milestoneDTO = new MilestoneDTO();
		milestoneDTO = milestonebuilder.covertToDto(mile);
		System.out.println("Milestone is = "+mile);
		return milestoneDTO;
	}
	
	@Override
	public InvoiceDTO getInvoiceDetails(long invoiceId) {
		// TODO Auto-generated method stub
		InvoiceDTO invoiceDTO = new InvoiceDTO();
		invoiceDTO = invoiceBuilder.entityToDto(invoicedaoImpl.findBy(Invoice.class, invoiceId));
		return invoiceDTO;
	}

	@Override
	public Boolean getInvoiceRef(long invoiceId) {
		List<Invoice> invoice = invoicedaoImpl.getInvoiceRefrence(invoiceId);
		System.out.println("Getting refrence invoice for proforma");
		System.out.println("Invoice object is = "+ invoice);
		Boolean flag = true;
		
		if(invoice.isEmpty()){
			flag = false;
		}
		return flag;
	}
	/*
	 * @Override public void reOpenInvoice(Long invoiceId) {
	 * 
	 * Invoice invoice = dao.findBy(Invoice.class, invoiceId);
	 * 
	 * Milestone milestone = invoice.getMilsestone();
	 * milestone.setInvoiceReopenFlag(Boolean.TRUE);
	 * invoice.setInvoiceReopenFlag(Boolean.TRUE);
	 * invoicedaoImpl.update(invoice); invoicedaoImpl.update(milestone);
	 * 
	 * }
	 */

	/*
	 * @Override public Map<String, Object> getTotalPendingAmount(String
	 * projectType, List<String> status, String fromDate, String toDate, String
	 * multiText, String datePeriod, String invoiceCountry) {
	 * 
	 * Employee employee = (Employee) securityUtils
	 * .getLoggedEmployeeDetailsSecurityContextHolder() .get("employee");
	 * 
	 * Permission totalInvoiceList = invoicedaoImpl.checkForPermission(
	 * "Invoice List", employee); Permission hierarchyInvoiceList =
	 * invoicedaoImpl.checkForPermission( "Hierarchy Invoice List", employee);
	 * 
	 * Map<String, Object> invoicesMap = new HashMap<String, Object>();
	 * 
	 * if (totalInvoiceList.getView() && !hierarchyInvoiceList.getView()) { //
	 * logger.warn("Getting Total Invoice List"); invoicesMap =
	 * invoicedaoImpl.getInvoiceList(projectType, status, fromDate, toDate,
	 * multiText, null, null, datePeriod, new HashSet<Long>(), invoiceCountry);
	 * } else if (totalInvoiceList.getView() && hierarchyInvoiceList.getView())
	 * { // logger.warn("Getting Hierarchy Invoice List"); List<Long>
	 * managersList = projectServiceImpl
	 * .mangerUnderManager(employee.getEmployeeId());
	 * 
	 * // Here we are getting all projects allocated to logged in employee // as
	 * well as(manager) reportees of logged in employee Set<Long> projectIds =
	 * new HashSet<Long>( invoicedaoImpl.getProjectsFor(employee,
	 * managersList));
	 * 
	 * invoicesMap = invoicedaoImpl.getInvoiceList(projectType, status,
	 * fromDate, toDate, multiText, null, null, datePeriod, projectIds,
	 * invoiceCountry);
	 * 
	 * }
	 *//***/
	/*
	 * List<InvoiceQueryDTO> invoiceQueryDtoList = invoiceBuilder
	 * .InvoiceEntityToDToList((List<Invoice>) invoicesMap .get("InvoiceList"));
	 * 
	 * Map<String, Object> ListofInvoiceMap = new HashMap<>();
	 * ListofInvoiceMap.put("InvoiceListSize",
	 * invoicesMap.get("InvoiceListSize")); ListofInvoiceMap.put("InvoiceList",
	 * invoiceQueryDtoList); return ListofInvoiceMap; }
	 */
	/*
	 * public static void main(String[] args) { Date date = new Date();
	 * System.out.println(date.getYearOfEra().getValue());
	 * System.out.println(date
	 * .getYearOfEra().getValue()+""+date.getMonthOfYear().getValue()); String
	 * arr[] = new DateFormatSymbols().getMonths(); for(String str : arr){
	 * System.out.println(str); } System.out.println(1+"sdgsd"); }
	 */

}
