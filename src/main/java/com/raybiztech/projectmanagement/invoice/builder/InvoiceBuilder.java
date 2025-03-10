package com.raybiztech.projectmanagement.invoice.builder;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.stereotype.Component;

import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.security.utils.SecurityUtils;
import com.raybiztech.date.Second;
import com.raybiztech.leavemanagement.business.algorithm.DateParser;
import com.raybiztech.payslip.utility.AES256Encryption;
import com.raybiztech.projectmanagement.business.ChangeRequest;
import com.raybiztech.projectmanagement.business.Client;
import com.raybiztech.projectmanagement.business.Milestone;
import com.raybiztech.projectmanagement.business.Project;
import com.raybiztech.projectmanagement.business.ProjectNumbers;
import com.raybiztech.projectmanagement.dao.ResourceManagementDAO;
import com.raybiztech.projectmanagement.invoice.business.CountryAddress;
import com.raybiztech.projectmanagement.invoice.business.Invoice;
import com.raybiztech.projectmanagement.invoice.business.InvoiceAudit;
import com.raybiztech.projectmanagement.invoice.business.InvoiceReminderLog;
import com.raybiztech.projectmanagement.invoice.business.LineItem;
import com.raybiztech.projectmanagement.invoice.business.LineItemAudit;
import com.raybiztech.projectmanagement.invoice.business.ReceivedInvoiceAmount;
import com.raybiztech.projectmanagement.invoice.business.ReceivedInvoiceAmountAudit;
import com.raybiztech.projectmanagement.invoice.business.Remittance;
import com.raybiztech.projectmanagement.invoice.business.Tax;
import com.raybiztech.projectmanagement.invoice.dao.InvoiceDao;
import com.raybiztech.projectmanagement.invoice.dao.InvoiceDaoImpl;
import com.raybiztech.projectmanagement.invoice.dto.InvoiceAuditDto;
import com.raybiztech.projectmanagement.invoice.dto.InvoiceDTO;
import com.raybiztech.projectmanagement.invoice.dto.InvoiceQueryDTO;
import com.raybiztech.projectmanagement.invoice.dto.InvoiceReminderLogDTO;
import com.raybiztech.projectmanagement.invoice.dto.LineItemDTO;
import com.raybiztech.projectmanagement.invoice.dto.ReceivedInvoiceAmountDTO;
import com.raybiztech.projectmanagement.invoice.dto.ResourceRoleDto;
import com.raybiztech.projectmanagement.invoice.dto.TaxDTO;
import com.raybiztech.projectmanagement.invoice.lookup.CurrencyToINR;
import com.raybiztech.projectmanagement.invoice.utility.NumberToWordConversion;
import com.raybiztech.projectmanagement.invoice.utility.NumberToWordUSCurrency;

@Component("invoiceBuilder")
public class InvoiceBuilder {

	private final InvoiceDao invoiceDao;
	private final ResourceManagementDAO resourceManagementDAO;
	private final LineItemBuilder lineItemBuilder;
	private final RemittanceBuilder remittanceBuilder;
	private final TaxBuilder taxBuilder;
	private final SecurityUtils securityUtils;
	private final ReceivedInvoiceAmountBuilder receivedAmountBuilder;
	private final CountryAddressBuilder countryAddressBuilder;

	int i = 0;

	Logger logger = Logger.getLogger(InvoiceBuilder.class);

	@Autowired
	public InvoiceBuilder(DiscountBuilder discountBuilder,
			InvoiceDao invoiceDao, LineItemBuilder lineItemBuilder,
			RemittanceBuilder remittanceBuilder, TaxBuilder taxBuilder,
			SecurityUtils securityUtils,
			ResourceManagementDAO resourceManagementDAO,
			ReceivedInvoiceAmountBuilder receivedAmountBuilder,
			CountryAddressBuilder countryAddressBuilder) {

		this.invoiceDao = invoiceDao;
		this.lineItemBuilder = lineItemBuilder;
		this.remittanceBuilder = remittanceBuilder;
		this.taxBuilder = taxBuilder;
		this.securityUtils = securityUtils;
		this.resourceManagementDAO = resourceManagementDAO;
		this.receivedAmountBuilder = receivedAmountBuilder;
		this.countryAddressBuilder = countryAddressBuilder;
		// this.invoiceServiceImpl=invoiceServiceImpl;

	}

	/*public Invoice convertDtoToEntity(InvoiceDTO invoicedto, Long id)
			throws ParseException {
		logger.warn("bulider");

		Invoice invoice = null;
		if (invoicedto != null) {
			
			
				// Loading Milestone from DB through milestoneID
				Milestone milestone = invoiceDao.findBy(Milestone.class,
						invoicedto.getMilestoneId());
				// Milestone Type
				String type = milestone.getProject().getType().toString();
				
					if (invoicedto.getMilestoneTypeFlag().equalsIgnoreCase("true")) {
					
					milestone.setMilestonePercentage(invoicedto
							.getMilestonePercentage() != null ? invoicedto
							.getMilestonePercentage() : null);
				
					
					if(milestone.getChangeRequest() != null){
						ChangeRequest changeRequest = invoiceDao.findBy(ChangeRequest.class,
								milestone.getChangeRequest().getId());
						
					
					Double CRAlreadyRaisedPercentage = resourceManagementDAO
							.getRaisedCrPercentage(changeRequest, null);
					Double raisingCRPercentage = Double.valueOf(invoicedto
							.getMilestonePercentage());
	               
					Double allOverpercentage = CRAlreadyRaisedPercentage
							+ raisingCRPercentage;

					if (allOverpercentage >= 100) {
						changeRequest.setMilestoneStatus(Boolean.TRUE);
						invoiceDao.update(changeRequest);
					}
					
					}
					
	               
				}

				invoice = new Invoice();
				if (invoicedto.getMilestoneId() != null) {

					if (type.equalsIgnoreCase("retainer")
							|| type.equalsIgnoreCase("Support")) {
						milestone.setInvoiceStatus(Boolean.TRUE);
					} else if (type.equalsIgnoreCase("FIXEDBID")) {
						
						//.warn("falg"+milestone.getMilestoneTypeFlag());
						//logger.warn("condition"+milestone.getMilestoneTypeFlag().equals(Boolean.FALSE));

						if (milestone.getMilestoneTypeFlag().equals(Boolean.FALSE)) {
							//logger.warn("in percentage if");
							Double risedPercentage = invoiceDao.risedPercentage(
									invoicedto.getMilestoneId(), null);

							risedPercentage = (invoicedto.getPercentage() != null) ? risedPercentage
									+ Double.valueOf(invoicedto.getPercentage())
									: risedPercentage;

							if (Double.valueOf(milestone.getMilestonePercentage())
									.equals(risedPercentage)) {
								milestone.setInvoiceStatus(Boolean.TRUE);
							}
						} else {
							//logger.warn("in milestone amount");
							Double risedPercentageForMilestone = invoiceDao
									.risedPercentage(invoicedto.getMilestoneId(),
											null);
							

							if (invoicedto.getPercentage() != null
									&& invoicedto.getMilestonePercentage() != null) {
								if (Double.valueOf(invoicedto.getPercentage())
										.equals(Double.valueOf(invoicedto
												.getMilestonePercentage()))) {
									milestone.setInvoiceStatus(Boolean.TRUE);
								} else if (risedPercentageForMilestone != 0.0) {
									risedPercentageForMilestone = (invoicedto
											.getPercentage() != null) ? risedPercentageForMilestone
											+ Double.valueOf(invoicedto
													.getPercentage())
											: risedPercentageForMilestone;
											
									

									if (Double.valueOf(
											milestone.getMilestonePercentage())
											.equals(risedPercentageForMilestone)) {
										
										milestone.setInvoiceStatus(Boolean.TRUE);
									}
									
								}
								else {
									milestone.setInvoiceStatus(Boolean.FALSE);
								}
							} else if (invoicedto.getMilestonePercentage() != null
									&& invoicedto.getPercentage() == null) {
								milestone.setInvoiceStatus(Boolean.TRUE);
							}
							// logger.warn("in milestone amount");
							// milestone.setInvoiceStatus(Boolean.TRUE);
						}
					}
					invoice.setMilsestone(milestone);
				}
			
		

			invoice.setDiscountType(invoicedto.getDiscountType());
			invoice.setDiscountRate(invoicedto.getDiscountRate());
			invoice.setDiscount(invoicedto.getDiscount());
			System.out.println("Tax Dto ="+invoicedto.getTaxDTO());
		invoice.setTax(taxBuilder.toEntityList(invoicedto.getTaxDTO()));
				System.out.println("Tax in invoice = "+invoice.getTax());
			
			
			invoice.setDueDate(DateParser.toDate(invoicedto.getDueDate()));
			invoice.setLineItems(lineItemBuilder.toEnityList(invoicedto
					.getLineitem()));

			invoice.setInvoiceDate(DateParser.toDate(invoicedto
					.getInvoiceDate()));
			if (invoicedto.getInvoiceAmountReceviedDate() != null) {
				invoice.setInvoiceAmountReceviedDate(DateParser
						.toDate(invoicedto.getInvoiceAmountReceviedDate()));
			}
			if (invoicedto.getInvoiceAmountSentDate() != null) {
				invoice.setInvoiceAmountSentDate(DateParser.toDate(invoicedto
						.getInvoiceAmountSentDate()));
			}
			invoice.setConversionRate((invoicedto.getConversionRate() != null) ? invoicedto
					.getConversionRate() : null);

			invoice.setNotes(invoicedto.getNotes());
			invoice.setShowNotesOnInvoice((invoicedto.getShowNotesOnInvoice() != null) ? invoicedto
					.getShowNotesOnInvoice() : Boolean.FALSE);
			// below number is for serial number
			invoice.setNumber(invoicedto.getNumber());
			invoice.setInvoiceNumber(invoicedto.getInvoiceNumber());
			invoice.setPaymentTerm(invoicedto.getPaymentTerm());
			
			Remittance remittance = invoiceDao.findBy(Remittance.class,
					invoicedto.getRemittance().getId());
			invoice.setRemittance(remittance);
			
			invoice.setSubTotal(invoicedto.getSubTotal());
			invoice.setAmount(invoicedto.getAmount());
			invoice.setTotalAmount(invoicedto.getTotalAmount());
			invoice.setTdsAmount(invoicedto.getTdsAmount() != null ? invoicedto
					.getTdsAmount() : null);
			invoice.setNetAmount(invoicedto.getNetAmount() != null ? invoicedto
					.getNetAmount() : null);

			invoice.setCountry(invoicedto.getCountry());

			CountryAddress companyAddress = invoiceDao.findByUniqueProperty(
					CountryAddress.class, "country", invoicedto.getCountry());

			if (companyAddress.getAddress() != null)
				invoice.setCompanyAddress(companyAddress.getAddress());

			CountryAddress companyName = invoiceDao.findByUniqueProperty(
					CountryAddress.class, "country", invoicedto.getCountry());

			if (companyName.getCompanyName() != null)
				invoice.setCompanyName(companyName.getCompanyName());

			invoice.setCountTypeToDisplay(invoicedto.getCountTypeToDisplay());
			if (type.equalsIgnoreCase("Retainer")) {
				invoice.setPercentage("100");
			} else {
				invoice.setPercentage(invoicedto.getPercentage() != null ? invoicedto
						.getPercentage() : null);
			}
			invoice.setAmountAfterDiscount((invoicedto.getAmountAfterDiscount() != null) ? invoicedto
					.getAmountAfterDiscount() : null);
			invoice.setPoNumber((invoicedto.getPoNumber() != null) ? invoicedto
					.getPoNumber() : null);
			invoice.setStatusNotes(invoicedto.getStatusNotes());
			invoice.setWriteoffAmount(invoicedto.getWriteoffAmount());
			invoice.setReceivedAmount(invoicedto.getReceivedAmount());
			invoice.setReferenceNumber(invoicedto.getReferenceNumber());
			invoice.setInvoiceReferenceNumber(invoicedto
					.getInvoiceReferenceNumber());
			invoice.setIsAdjusted(Boolean.FALSE);
			invoice.setCanBeAdjusted(Boolean.FALSE);
			invoice.setShowTaxDetailsOnInvoice(invoicedto
					.getShowTaxDetailsOnInvoice());
			invoice.setShowNotesOnInvoice((invoicedto.getShowNotesOnInvoice() != null) ? invoicedto
					.getShowNotesOnInvoice() : Boolean.FALSE);

			invoice.setModifiedMilestoneName(invoicedto
					.getModifiedMilestoneName() != null ? invoicedto
					.getModifiedMilestoneName() : null);
			System.out.println("ProformaInvoiceFlag = "+invoicedto.getProformaInvoiceFlag());
			if(invoicedto.getProformaInvoiceFlag() == false){
				invoice.setProformaInvoiceFlag(Boolean.FALSE);
			}
			else{
				invoice.setProformaInvoiceFlag(Boolean.TRUE);
				
			}
			
			 * invoice.setInvoiceReopenFlag(invoicedto.getInvoiceReopenFlag() !=
			 * null ? invoicedto .getInvoiceReopenFlag() : Boolean.FALSE);
			 
			// invoice.setInvoiceReopenFlag(invoicedto.getInvoiceReopenFlag() !=
			// null ? invoicedto.getInvoiceReopenFlag() : Boolean.FALSE);

			
			 * invoice.setInvoiceNumberFlag((invoicedto.getInvoiceNumberFlag()
			 * != null) ? invoicedto.getInvoiceNumberFlag() : Boolean.FALSE );
			 
		//	System.out.println("Proferma invoivce number"+invoicedto.getProformaReferenceNo());
			if(invoicedto.getProformaReferenceNo()!=null)
			{
				Invoice refInvoice = invoiceDao.findBy(Invoice.class,Long.parseLong(invoicedto.getProformaReferenceNo()));
				System.out.println("Ref Invoice object is = "+ refInvoice.getId());
				 invoice.setProformaReferenceNo(refInvoice);
				 System.out.println("After setting invoice ref");
			}
			
			
		}

		return invoice;

	}*/
	
	public Invoice convertDtoToEntity(InvoiceDTO invoicedto, Long id)
			throws ParseException {
		logger.warn("bulider");

		Invoice invoice = null;
		if (invoicedto != null) {
			
			
				// Loading Milestone from DB through milestoneID
				Milestone milestone = invoiceDao.findBy(Milestone.class,
						invoicedto.getMilestoneId());
				// Milestone Type
				String type = milestone.getProject().getType().toString();
				
					if (invoicedto.getMilestoneTypeFlag().equalsIgnoreCase("true")) {
					
					// Invoice raised from Milestone
					if(invoicedto.getProformaReferenceNo() == null || invoicedto.getProformaReferenceNo().isEmpty()){
						logger.warn("In if");
						milestone.setMilestonePercentage(invoicedto
								.getMilestonePercentage() != null ? invoicedto
								.getMilestonePercentage() : null);
					}
					// Invoice raised from Proforma
					else{
						logger.warn("In else");
						milestone.setMilestonePercentage(null);
					}
					
					if(milestone.getChangeRequest() != null){
						ChangeRequest changeRequest = invoiceDao.findBy(ChangeRequest.class,
								milestone.getChangeRequest().getId());
						
					
					Double CRAlreadyRaisedPercentage = resourceManagementDAO
							.getRaisedCrPercentage(changeRequest, null);
					System.out.println("CRAlreadyRaisedPercentage:" + CRAlreadyRaisedPercentage);
					Double raisingCRPercentage =0.0;
					// Invoice raised from Milestone
					System.out.println("proforma:" + invoicedto.getProformaReferenceNo());
					if(invoicedto.getProformaReferenceNo() == null){
						logger.warn("In If");
						 raisingCRPercentage = Double.valueOf(invoicedto
								.getMilestonePercentage());
						 System.out.println("raisingCRPercentage:"+raisingCRPercentage);
					}
					// Invoice raised from Proforma
					else{
						logger.warn("In else");
						raisingCRPercentage = raisingCRPercentage;
					}
					System.out.println("raisingCRPercentage:"+raisingCRPercentage);
					
	            	   Double  allOverpercentage = CRAlreadyRaisedPercentage + raisingCRPercentage;
	               

					if (allOverpercentage >= 100) {
						System.out.println("in allOverpercentage");
						changeRequest.setMilestoneStatus(Boolean.TRUE);
						invoiceDao.update(changeRequest);
					}
					/*if(allOverpercentage == 0.0 && (milestone.getMilestonePercentage().isEmpty()|| milestone.getMilestonePercentage()==null)){
						changeRequest.setMilestoneStatus(Boolean.TRUE);
						invoiceDao.update(changeRequest);
					}*/
					}
					
	               
				}

				invoice = new Invoice();
				logger.warn("Milestone id = "+invoicedto.getMilestoneId());
				logger.warn("Project Type = "+type);
				if (invoicedto.getMilestoneId() != null) {
					logger.warn("In If Milestone ID is not null");

					if (type.equalsIgnoreCase("retainer")
							|| type.equalsIgnoreCase("Support")) {
						logger.warn("Project is Retainer or Support");
						milestone.setInvoiceStatus(Boolean.TRUE);
					} else if (type.equalsIgnoreCase("FIXEDBID")) {
						

						if (milestone.getMilestoneTypeFlag().equals(Boolean.FALSE)) {
							logger.warn("in percentage if");
							Double risedPercentage = invoiceDao.risedPercentage(
									invoicedto.getMilestoneId(), null);
							
							//Invoice raised from Milestone
							if(invoicedto.getProformaReferenceNo() == null){
								logger.warn("In If");
								risedPercentage = (invoicedto.getPercentage() != null) ? risedPercentage
										+ Double.valueOf(invoicedto.getPercentage())
										: risedPercentage;
							}
							//Invoice raised from Proforma
							else{
								logger.warn("In else");
								risedPercentage = risedPercentage;
							}
							

							if (Double.valueOf(milestone.getMilestonePercentage())
									.equals(risedPercentage)) {
								milestone.setInvoiceStatus(Boolean.TRUE);
							}
						} else {
							logger.warn("in milestone amount");
							Double risedPercentageForMilestone = invoiceDao
									.risedPercentage(invoicedto.getMilestoneId(),
											null);
							

							if (invoicedto.getPercentage() != null
									&& invoicedto.getMilestonePercentage() != null) {
								if(!invoicedto.getMilestonePercentage().isEmpty()){
								if (Double.valueOf(invoicedto.getPercentage())
										.equals(Double.valueOf(invoicedto
												.getMilestonePercentage()))) {
									milestone.setInvoiceStatus(Boolean.TRUE);
								}} else if (risedPercentageForMilestone != 0.0) {
									// Invoice Raised from Milestone
									if(invoicedto.getProformaReferenceNo() == null){
										logger.warn("In If last");
										risedPercentageForMilestone = (invoicedto
												.getPercentage() != null) ? risedPercentageForMilestone
												+ Double.valueOf(invoicedto
														.getPercentage())
												: risedPercentageForMilestone;
									}
									// Invocie raised from Proforma
									else{
										logger.warn("In else last");
										risedPercentageForMilestone = risedPercentageForMilestone;
									}
									
											
									
                                    if(milestone.getMilestonePercentage()!=null && !milestone.getMilestonePercentage().isEmpty()){
									if (Double.valueOf(
											milestone.getMilestonePercentage())
											.equals(risedPercentageForMilestone)) {
										
										milestone.setInvoiceStatus(Boolean.TRUE);
									}
                                    }
								}
								else {
									milestone.setInvoiceStatus(Boolean.FALSE);
								}
							} else if (invoicedto.getMilestonePercentage() != null
									&& invoicedto.getPercentage() == null) {
								if(!invoicedto.getMilestonePercentage().isEmpty()){
								milestone.setInvoiceStatus(Boolean.TRUE);
								}
							}
							// logger.warn("in milestone amount");
							// milestone.setInvoiceStatus(Boolean.TRUE);
						}
					}
					logger.warn("Before Setting Milestone Invoice Status= "+ milestone.getInvoiceStatus());
					invoice.setMilsestone(milestone);
				}
			
		

			invoice.setDiscountType(invoicedto.getDiscountType());
			invoice.setDiscountRate(invoicedto.getDiscountRate());
			invoice.setDiscount(invoicedto.getDiscount());
			System.out.println("Tax Dto ="+invoicedto.getTaxDTO());
		invoice.setTax(taxBuilder.toEntityList(invoicedto.getTaxDTO()));
				System.out.println("Tax in invoice = "+invoice.getTax());
			
			
			invoice.setDueDate(DateParser.toDate(invoicedto.getDueDate()));
			invoice.setLineItems(lineItemBuilder.toEnityList(invoicedto
					.getLineitem()));

			invoice.setInvoiceDate(DateParser.toDate(invoicedto
					.getInvoiceDate()));
			if (invoicedto.getInvoiceAmountReceviedDate() != null) {
				invoice.setInvoiceAmountReceviedDate(DateParser
						.toDate(invoicedto.getInvoiceAmountReceviedDate()));
			}
			if (invoicedto.getInvoiceAmountSentDate() != null) {
				invoice.setInvoiceAmountSentDate(DateParser.toDate(invoicedto
						.getInvoiceAmountSentDate()));
			}
			invoice.setConversionRate((invoicedto.getConversionRate() != null) ? invoicedto
					.getConversionRate() : null);

			invoice.setNotes(invoicedto.getNotes());
			invoice.setShowNotesOnInvoice((invoicedto.getShowNotesOnInvoice() != null) ? invoicedto
					.getShowNotesOnInvoice() : Boolean.FALSE);
			// below number is for serial number
			invoice.setNumber(invoicedto.getNumber());
			invoice.setInvoiceNumber(invoicedto.getInvoiceNumber());
			invoice.setPaymentTerm(invoicedto.getPaymentTerm());
			
			Remittance remittance = invoiceDao.findBy(Remittance.class,
					invoicedto.getRemittance().getId());
			invoice.setRemittance(remittance);
			
			invoice.setSubTotal(invoicedto.getSubTotal());
			invoice.setAmount(invoicedto.getAmount());
			invoice.setTotalAmount(invoicedto.getTotalAmount());
			invoice.setTdsAmount(invoicedto.getTdsAmount() != null ? invoicedto
					.getTdsAmount() : null);
			invoice.setNetAmount(invoicedto.getNetAmount() != null ? invoicedto
					.getNetAmount() : null);

			invoice.setCountry(invoicedto.getCountry());

			CountryAddress companyAddress = invoiceDao.findByUniqueProperty(
					CountryAddress.class, "country", invoicedto.getCountry());

			if (companyAddress.getAddress() != null)
				invoice.setCompanyAddress(companyAddress.getAddress());

			CountryAddress companyName = invoiceDao.findByUniqueProperty(
					CountryAddress.class, "country", invoicedto.getCountry());

			if (companyName.getCompanyName() != null)
				invoice.setCompanyName(companyName.getCompanyName());

			invoice.setCountTypeToDisplay(invoicedto.getCountTypeToDisplay());
			if (type.equalsIgnoreCase("Retainer")) {
				invoice.setPercentage("100");
			} else {
				invoice.setPercentage(invoicedto.getPercentage() != null ? invoicedto
						.getPercentage() : null);
			}
			invoice.setAmountAfterDiscount((invoicedto.getAmountAfterDiscount() != null) ? invoicedto
					.getAmountAfterDiscount() : null);
			invoice.setPoNumber((invoicedto.getPoNumber() != null) ? invoicedto
					.getPoNumber() : null);
			invoice.setStatusNotes(invoicedto.getStatusNotes());
			invoice.setWriteoffAmount(invoicedto.getWriteoffAmount());
			invoice.setReceivedAmount(invoicedto.getReceivedAmount());
			invoice.setReferenceNumber(invoicedto.getReferenceNumber());
			invoice.setInvoiceReferenceNumber(invoicedto
					.getInvoiceReferenceNumber());
			invoice.setIsAdjusted(Boolean.FALSE);
			invoice.setCanBeAdjusted(Boolean.FALSE);
			invoice.setShowTaxDetailsOnInvoice(invoicedto
					.getShowTaxDetailsOnInvoice());
			invoice.setShowNotesOnInvoice((invoicedto.getShowNotesOnInvoice() != null) ? invoicedto
					.getShowNotesOnInvoice() : Boolean.FALSE);

			invoice.setModifiedMilestoneName(invoicedto
					.getModifiedMilestoneName() != null ? invoicedto
					.getModifiedMilestoneName() : null);
			System.out.println("ProformaInvoiceFlag = "+invoicedto.getProformaInvoiceFlag());
			if(invoicedto.getProformaInvoiceFlag() == false){
				invoice.setProformaInvoiceFlag(Boolean.FALSE);
			}
			else{
				invoice.setProformaInvoiceFlag(Boolean.TRUE);
				
			}
			/*
			 * invoice.setInvoiceReopenFlag(invoicedto.getInvoiceReopenFlag() !=
			 * null ? invoicedto .getInvoiceReopenFlag() : Boolean.FALSE);
			 */
			// invoice.setInvoiceReopenFlag(invoicedto.getInvoiceReopenFlag() !=
			// null ? invoicedto.getInvoiceReopenFlag() : Boolean.FALSE);

			/*
			 * invoice.setInvoiceNumberFlag((invoicedto.getInvoiceNumberFlag()
			 * != null) ? invoicedto.getInvoiceNumberFlag() : Boolean.FALSE );
			 */
			System.out.println("Proferma invoivce number"+invoicedto.getProformaReferenceNo());
			if(invoicedto.getProformaReferenceNo()!=null)
			{
				Invoice refInvoice = invoiceDao.findBy(Invoice.class,Long.parseLong(invoicedto.getProformaReferenceNo()));
				System.out.println("Ref Invoice object is = "+ refInvoice.getId());
				 invoice.setProformaReferenceNo(refInvoice);
				 System.out.println("After setting invoice ref");
			}
			
			
		}

		return invoice;

	}

	@SuppressWarnings("unchecked")
	public InvoiceDTO entityToDto(Invoice invoice) {
		InvoiceDTO invoiceDTO = null;
		if (invoice != null) {

			Employee loggedEmployee = (Employee) securityUtils
					.getLoggedEmployeeDetailsSecurityContextHolder().get(
							"employee");

			AES256Encryption aes256Encryption = new AES256Encryption(
					String.valueOf(invoice.getId()), invoice.getSaltKey());

			invoiceDTO = new InvoiceDTO();

			invoiceDTO.setId(invoice.getId());

			invoiceDTO.setAmount(invoice.getAmount() != null ? aes256Encryption
					.decrypt(invoice.getAmount().toString()) : null);

			if (invoice.getDiscountType() == null
					|| invoice.getDiscountType().isEmpty()) {
				invoiceDTO.setDiscountType(null);
			} else {
				invoiceDTO
						.setDiscountType(invoice.getDiscountType() != null ? invoice
								.getDiscountType() : null);
			}
			invoiceDTO
					.setDiscountRate(invoice.getDiscountRate() != null ? invoice
							.getDiscountRate() : null);

			invoiceDTO.setDiscount(invoice.getDiscount() != null ? invoice
					.getDiscount() : null);

			invoiceDTO.setDueDate(invoice.getDueDate().toString("dd/MM/yyyy"));
			invoiceDTO.setInvoiceDate(invoice.getInvoiceDate().toString(
					"dd/MM/yyyy"));

			if (invoice.getInvoiceAmountReceviedDate() != null) {
				invoiceDTO.setInvoiceAmountReceviedDate(invoice
						.getInvoiceAmountReceviedDate().toString("dd/MM/yyyy"));
			}

			if (invoice.getInvoiceAmountSentDate() != null) {
				invoiceDTO.setInvoiceAmountSentDate(invoice
						.getInvoiceAmountSentDate().toString("dd/MM/yyyy"));
			}
			invoiceDTO
					.setConversionRate((invoice.getConversionRate() != null) ? invoice
							.getConversionRate() : null);
			invoiceDTO.setInvoiceStatus(invoice.getInvoiceStatus().toString());
			invoiceDTO
					.setLineitem(lineItemBuilder.toDto(invoice.getLineItems()));

			invoiceDTO.setMilestoneId(invoice.getMilsestone().getId());
			invoiceDTO.setNotes(aes256Encryption.decrypt(invoice.getNotes()));
			invoiceDTO.setShowNotesOnInvoice(invoice.getShowNotesOnInvoice());
			invoiceDTO.setNumber(invoice.getNumber());
			invoiceDTO
					.setInvoiceNumber((invoice.getInvoiceNumber() != null) ? invoice
							.getInvoiceNumber() : null);
			invoiceDTO.setPaymentTerm(invoice.getPaymentTerm());
			invoiceDTO.setCountry(invoice.getCountry());
			Milestone milestone = invoice.getMilsestone();
			if(invoice.getProformaInvoiceFlag() == null){
				invoiceDTO.setProformaInvoiceFlag(Boolean.FALSE);
			}
			else{
				invoiceDTO.setProformaInvoiceFlag(invoice.getProformaInvoiceFlag());
			}

			if (milestone != null) {
				
				logger.warn("milestoneId"+milestone.getId());
				invoiceDTO
						.setCrid(milestone.getChangeRequest() != null ? milestone
								.getChangeRequest().getId() : null);
				invoiceDTO
						.setCrduration(milestone.getChangeRequest() != null ? milestone
								.getChangeRequest().getDuration() : null);
				invoiceDTO.setMileStoneName(milestone.getTitle());
				//logger.warn("milestoneFFFFlag"+milestone.getMilestoneTypeFlag());
				//logger.warn("connnn"+milestone.getMil19=;2estoneTypeFlag().equals(Boolean.TRUE));
				if(milestone.getMilestoneTypeFlag() != null){
					logger.warn("milestone flag if"+milestone.getMilestoneTypeFlag());
				invoiceDTO.setMilestoneTypeFlag(milestone.getMilestoneTypeFlag().equals(Boolean.TRUE) ? "true" : "false");
				}
				invoiceDTO.setMilestonePercentage(milestone
						.getMilestonePercentage());
				Project project = milestone.getProject();
				logger.warn("project Id"+project.getId());
				if (project != null) {
					invoiceDTO.setProjectName(milestone.getProject()
							.getProjectName());
					invoiceDTO.setClient(milestone.getProject().getClient()
							.getName());
					invoiceDTO.setClientId(project.getClient().getId());
					invoiceDTO.setGstCode(project.getClient().getGstCode());
					if (project.getClient().getCountry() != null) {
						invoiceDTO.setClientCountry(project.getClient()
								.getCountry().getName());
					}
				}
			}
			invoiceDTO.setRemittance(remittanceBuilder
					.convertEntityToDTO(invoice.getRemittance()));
			/*
			 * CountryAddress companyAddress = invoiceDao.
			 * findByUniqueProperty(CountryAddress.class, "country",
			 * invoice.getCountry());
			 * 
			 * invoiceDTO.setCompanyName(companyAddress.getCompanyName());
			 * 
			 * invoiceDTO.setCompanyAddress(companyAddress.getAddress());
			 */

			invoiceDTO.setCompanyAddress(invoice.getCompanyAddress());
			invoiceDTO.setCompanyName(invoice.getCompanyName());

			String invoicestatus = invoice.getInvoiceStatus();
			invoiceDTO.setInvoiceStatus(invoicestatus);

			invoiceDTO.setModifiedMilestoneName(invoice
					.getModifiedMilestoneName() != null ? invoice
					.getModifiedMilestoneName() : null);

			invoiceDTO
					.setInvoiceUpdatable((invoicestatus
							.equalsIgnoreCase("RECEIVED") ? Boolean.FALSE
							: invoicestatus.equalsIgnoreCase("WRITE OFF") ? Boolean.FALSE
									: Boolean.TRUE));

			if (invoicestatus.equalsIgnoreCase("RECEIVED")
					|| invoicestatus.equalsIgnoreCase("PARTIALLY RECEIVED")
					|| invoicestatus.equalsIgnoreCase("WRITE OFF")) {
				invoiceDTO.setInvoiceMutable(Boolean.FALSE);
			} else {
				invoiceDTO.setInvoiceMutable(Boolean.TRUE);
			}

			invoiceDTO.setTaxDTO(taxBuilder.toDtoList(invoice.getTax()));
			invoiceDTO.setSubTotal(aes256Encryption.decrypt(invoice
					.getSubTotal().toString()));
			invoiceDTO.setTdsAmount(aes256Encryption.decrypt(invoice
					.getTdsAmount()));
			invoiceDTO.setNetAmount(aes256Encryption.decrypt(invoice
					.getNetAmount()));

			Map<String, Object> receivedInvoiceAmountDetails = receivedAmountBuilder
					.decryptReceivedInvoiceAmountList(invoice.getId(),
							invoice.getReceivedAmountList());

			invoiceDTO
					.setReceivedAmountList((Set<ReceivedInvoiceAmountDTO>) receivedInvoiceAmountDetails
							.get("list"));

			invoiceDTO.setTotalReceivedAmount(String
					.valueOf(receivedInvoiceAmountDetails
							.get("totalReceivedAmount")))

			;
			String currency = invoice.getRemittance().getCurrencyType();

			invoiceDTO.setCurrencyType(currency);

			if (invoice.getTotalAmount() != null) {
				// Here totalamount is set with comma and round value.
				String totalAmount = aes256Encryption.decrypt(invoice
						.getTotalAmount().toString());
				invoiceDTO.setTotalAmount(totalAmount);

				Double amt = new Double(aes256Encryption.decrypt(invoice
						.getTotalAmount().toString()));

				BigDecimal bigDecimal = new BigDecimal(Math.round(amt));

				if (currency.equalsIgnoreCase("INR")) {
					invoiceDTO.setNumberInWords(new NumberToWordConversion()
							.convertNumberToWords(bigDecimal.intValue()));
				} else {
					invoiceDTO.setNumberInWords(new NumberToWordUSCurrency()
							.convert(bigDecimal.longValue()));
				}

			}

			invoiceDTO.setOrganization(invoice.getMilsestone().getProject()
					.getClient().getOrganization());
			invoiceDTO.setCompanyAdress(invoice.getMilsestone().getProject()
					.getClient().getAddress());
			invoiceDTO.setClientContactPerson(invoice.getMilsestone()
					.getProject().getClient().getPersonName());

			if (invoice.getMilsestone().getProject().getBillingContactPerson() != null) {
				invoiceDTO.setBillingContactPerson(invoice.getMilsestone()
						.getProject().getBillingContactPerson());
			}

			if (invoice.getMilsestone().getProject().getBillingContactPerson() != null) {
				invoiceDTO.setBillingContactPersonEmail(invoice.getMilsestone()
						.getProject().getBillingContactPersonEmail());
			}

			invoiceDTO.setProjectId(invoice.getMilsestone().getProject()
					.getId());
			invoiceDTO.setProjectType(invoice.getMilsestone().getProject()
					.getType().toString());
			invoiceDTO.setStatusNotes(invoice.getStatusNotes());

			invoiceDTO.setInvoiceStatusList(getInvoiceStatusByStatus(invoice
					.getInvoiceStatus()));

			String writeOffAmount = aes256Encryption.decrypt(invoice
					.getWriteoffAmount());
			invoiceDTO
					.setWriteoffAmount((writeOffAmount != null) ? writeOffAmount
							: "0");
			invoiceDTO.setReceivedAmount(aes256Encryption.decrypt(invoice
					.getReceivedAmount()));

			if (invoice.getAmountAfterDiscount() != null) {

				invoiceDTO.setAmountAfterDiscount(aes256Encryption
						.decrypt(invoice.getAmountAfterDiscount()));
			}

			invoiceDTO.setCountTypeToDisplay(invoice.getCountTypeToDisplay());

			invoiceDTO.setPercentage(invoice.getPercentage());
			if (invoice.getPoNumber() != null) {
				invoiceDTO.setPoNumber(invoice.getPoNumber());
			}
			invoiceDTO.setReferenceNumber(invoice.getReferenceNumber());
			if(invoice.getReferenceNumber()!=null){
			invoiceDTO.setPattern(invoice.getNumber().replace(
					invoice.getReferenceNumber(), ""));
			}
			invoiceDTO.setInvoiceReferenceNumber((invoice
					.getInvoiceReferenceNumber() != null) ? invoice
					.getInvoiceReferenceNumber() : null);

			if (invoice.getInvoiceNumber() != null) {
				invoiceDTO
						.setInvoicePattern(invoice
								.getInvoiceNumber()
								.replace(
										(invoice.getInvoiceReferenceNumber() != null) ? invoice
												.getInvoiceReferenceNumber()
												: null, ""));
			}
			invoiceDTO.setIsAdjusted(invoice.getIsAdjusted());
			invoiceDTO.setCanBeAdjusted(invoice.getCanBeAdjusted());

			String loggedEmployeeRole = loggedEmployee.getRole();

			// this will be set after status is Received.
			invoiceDTO.setShowCanBeAdjusted((loggedEmployeeRole
					.equalsIgnoreCase("admin") && invoice.getInvoiceStatus()
					.equalsIgnoreCase("Received")) ? Boolean.TRUE
					: Boolean.FALSE);

			invoiceDTO.setShowRestrictAdjusting((loggedEmployeeRole
					.equalsIgnoreCase("admin") && invoice.getCanBeAdjusted()));
			invoiceDTO.setShowTaxDetailsOnInvoice(invoice
					.getShowTaxDetailsOnInvoice());

			/* for resource name */
			List<String> resourceNames = new ArrayList<String>();
			StringBuffer namesbuffer = new StringBuffer();
			Set<LineItem> lineItems = invoice.getLineItems();

			if (lineItems != null) {
				for (LineItem item : lineItems) {
					if (item != null) {
						// If the item is an instance of an employee
						if (item.getItem() instanceof Employee) {
							Employee employee = (Employee) item.getItem();
							resourceNames.add((employee == null) ? (null)
									: employee.getFullName());
						}
					}
				}
			}

			// after adding all Names remove all nulls and sort
			resourceNames.removeAll(Collections.singleton(null));
			Collections.sort(resourceNames);

			for (String names : resourceNames) {

				namesbuffer.append(names).append(
						System.getProperty("line.separator"));
			}

			/* invoiceDTO.setResourceNamesForPdf(new String(namesbuffer)); */
			invoiceDTO.setResourcesName(resourceNames);

			/* for roles */

			Set<LineItem> lineitem = invoice.getLineItems();
			String lineItemAmout;
			StringBuilder employeebuffer = new StringBuilder();
			StringBuilder employeeamount = new StringBuilder();
			StringBuilder lineItembuffer = new StringBuilder();
			StringBuilder lineItemAmount = new StringBuilder();

			// for LineItems in Jasper report

			if (lineitem != null) {

				for (LineItem ldto : lineitem) {
					// For line item is not an object of Employee
					if (!(ldto.getItem() instanceof Employee)) {

						AES256Encryption encryption = new AES256Encryption(
								String.valueOf(ldto.getId()), ldto.getSaltkey());

						if (ldto.getDuration() != null) {
							lineItembuffer.append(
									ldto.getDescription().toString() + "  -  "
											+ ldto.getCount() + " "
											+ ldto.getDuration().getDuration()
											+ "  @  " + ldto.getRate() + " "
											+ invoiceDTO.getCurrencyType()
											+ "\n").append(
									System.getProperty("line.separator"));
							lineItemAmout = null;

							lineItemAmout = setLength(String.valueOf(Double
									.valueOf(ldto.getRate())
									* Double.valueOf(ldto.getCount())));

							lineItemAmount.append(lineItemAmout + "\n").append(
									System.getProperty("line.separator"));
						} else {
							lineItembuffer.append(
									ldto.getDescription().toString() + "\n")
									.append(System
											.getProperty("line.separator"));
							lineItemAmout = null;

							lineItemAmout = setLength(encryption.decrypt(ldto
									.getLineItemAmount()));

							lineItemAmount.append(lineItemAmout + "\n").append(
									System.getProperty("line.separator"));

						}
					}
					// When Line ITEM is an Employee
					else {
						Employee employee = (Employee) ldto.getItem();
						if (employee != null) {
							AES256Encryption encryption = new AES256Encryption(
									String.valueOf(ldto.getId()),
									ldto.getSaltkey());

							if (ldto.getCount() != null
									&& !(ldto.getRate() == null || ldto
											.getRate().isEmpty())) {
								// Jasper for show by role with fixed rate for
								// Employee
								if (invoiceDTO.getCountTypeToDisplay()
										.equalsIgnoreCase("roles")) {
									employeebuffer
											.append("1 X "
													+ ldto.getDescription()
													+ " : "
													+ ldto.getBillableDays()
													+ " * "
													+ ldto.getDuration()
															.getDuration()
													+ " @ "
													+ ldto.getRate()
													+ " "
													+ invoiceDTO
															.getCurrencyType()
													+ "\n")
											.append(System
													.getProperty("line.separator"));
								}
								// Jasper for show by Name with fixed rate for
								// Employee
								else {
									employeebuffer
											.append(employee.getFullName()
													+ " - "
													+ ldto.getBillableDays()
													+ " "
													+ ldto.getDuration()
															.getDuration()
													+ " @ "
													+ ldto.getRate()
													+ " "
													+ invoiceDTO
															.getCurrencyType()
													+ "\n")
											.append(System
													.getProperty("line.separator"));
								}

								lineItemAmout = null;

								lineItemAmout = setLength(String
										.valueOf(Double.valueOf(ldto.getRate())
												* Double.valueOf(ldto
														.getBillableDays())));

								employeeamount.append(lineItemAmout + "\n")
										.append(System
												.getProperty("line.separator"));
							} else {
								// Jasper for show by role with no rate for
								// Employee
								if (invoiceDTO.getCountTypeToDisplay()
										.equalsIgnoreCase("roles")) {

									employeebuffer
											.append("1 X "
													+ ldto.getDescription()
													+ " : "
													+ ldto.getBillableDays()
													+ "*"
													+ ldto.getDuration()
															.getDuration()
													+ "\n")
											.append(System
													.getProperty("line.separator"));

								}
								// Jasper for show by name with no rate for
								// Employee
								else {
									employeebuffer
											.append(employee.getFullName()
													+ " - "
													+ ldto.getBillableDays()
													+ " "
													+ ldto.getDuration()
															.getDuration()
													+ "\n")
											.append(System
													.getProperty("line.separator"));
								}
								lineItemAmout = null;
								lineItemAmout = setLength(encryption
										.decrypt(ldto.getAmount()));

								employeeamount.append(lineItemAmout

								+ "\n").append(
										System.getProperty("line.separator"));
							}
						}
					}
					String totalItem = lineItembuffer.toString().concat(
							employeebuffer.toString());
					String Individualamount = employeeamount.toString().concat(
							lineItemAmount.toString());

					/*
					 * invoiceDTO.setTotalItemsforPDF(totalItem);
					 * invoiceDTO.setItemamounteForPDF(Individualamount);
					 */

				}
			}
		}
		return invoiceDTO;
	}

	public List<InvoiceQueryDTO> InvoiceEntityToDToList(
			List<Invoice> invoicesList) {

		List<InvoiceQueryDTO> invoiceQueryDTOList = null;

		if (invoicesList != null) {
			invoiceQueryDTOList = new ArrayList<>();

			for (Invoice invoice : invoicesList) {

				if (invoice != null) {

					AES256Encryption aes256Encryption = new AES256Encryption(
							String.valueOf(invoice.getId()),
							invoice.getSaltKey());

					Milestone milestone = invoice.getMilsestone();
					Project project = (milestone != null) ? milestone
							.getProject() : null;
					Client client = (project != null) ? project.getClient()
							: null;

					Employee deliveryManager = (project != null) ? resourceManagementDAO
							.getDeliveryManagerofProject(project) : null;

					InvoiceQueryDTO invoiceQueryDTO = new InvoiceQueryDTO();
					invoiceQueryDTO.setReminderSize(invoiceDao.getReminderLogs(
							invoice.getId().toString()).size());
					// invoice serial number
					invoiceQueryDTO.setInvoicNumber(invoice.getNumber());
					// invoice Number
					invoiceQueryDTO
							.setNumber((invoice.getInvoiceNumber() != null) ? invoice
									.getInvoiceNumber() : null);
					invoiceQueryDTO.setInvoiceId(invoice.getId());
					invoiceQueryDTO
							.setDueDate(invoice.getDueDate() != null ? invoice
									.getDueDate().toString("dd/MM/yyyy") : null);
					invoiceQueryDTO
							.setRaisedDate(invoice.getInvoiceDate() != null ? invoice
									.getInvoiceDate().toString("dd/MM/yyyy")
									: null);

					invoiceQueryDTO.setInvoiceAmountReceviedDate(invoice
							.getInvoiceAmountReceviedDate() != null ? invoice
							.getInvoiceAmountReceviedDate().toString(
									"dd/MM/yyyy") : "N/A");

					invoiceQueryDTO.setInvoiceAmountSentDate(invoice
							.getInvoiceAmountSentDate() != null ? invoice
							.getInvoiceAmountSentDate().toString("dd/MM/yyyy")
							: "N/A");
					invoiceQueryDTO.setConversionRate(invoice
							.getConversionRate() != null ? invoice
							.getConversionRate() : null);

					invoiceQueryDTO
							.setTotalAmount(new BigDecimal(aes256Encryption
									.decrypt(invoice.getTotalAmount())));
					invoiceQueryDTO.setTdsAmount(aes256Encryption
							.decrypt(invoice.getTdsAmount()));
					invoiceQueryDTO.setNetAmount(aes256Encryption
							.decrypt(invoice.getNetAmount()));

					Map<String, Long> converter = this.getConverterAmounts();

					Long convertedTotalAmount = 0L;
					if (!invoice.getRemittance().getCurrencyType()
							.equalsIgnoreCase("INR")) {

						BigDecimal finalTotal = (new BigDecimal(
								aes256Encryption.decrypt(invoice
										.getTotalAmount())));
						Long inrAmount = converter.get(invoice.getRemittance()
								.getCurrencyType());
						convertedTotalAmount = finalTotal.longValue()
								* (inrAmount != null ? inrAmount : 1);

						invoiceQueryDTO
								.setFinalTotalAmount(convertedTotalAmount);
					} else {

						BigDecimal finalTotal = (new BigDecimal(
								aes256Encryption.decrypt(invoice
										.getTotalAmount())));

						invoiceQueryDTO.setFinalTotalAmount(finalTotal
								.longValue());

					}

					Long totalReceivedAmount1 = (Long) receivedAmountBuilder
							.decryptReceivedInvoiceAmountList(invoice.getId(),
									invoice.getReceivedAmountList()).get(
									"totalReceivedAmount");

					Long convertedPendingAmount = 0L;
					if (!invoice.getRemittance().getCurrencyType()
							.equalsIgnoreCase("INR")) {

						BigDecimal finalTotal = (new BigDecimal(
								aes256Encryption.decrypt(invoice
										.getTotalAmount())));

						Long pendingTotal = finalTotal.longValue()
								- totalReceivedAmount1;

						Long inrAmount = converter.get(invoice.getRemittance()
								.getCurrencyType());
						convertedTotalAmount = pendingTotal.longValue()
								* (inrAmount != null ? inrAmount : 1);

						invoiceQueryDTO.setPendingAmount(convertedTotalAmount);
					} else {

						BigDecimal finalTotal = (new BigDecimal(
								aes256Encryption.decrypt(invoice
										.getTotalAmount())));

						Long pendingTotal = finalTotal.longValue()
								- totalReceivedAmount1;

						invoiceQueryDTO.setPendingAmount(pendingTotal
								.longValue());

					}

					invoiceQueryDTO.setSubtotal(aes256Encryption
							.decrypt(invoice.getSubTotal()));

					invoiceQueryDTO
							.setCountry((invoice.getCountry() != null) ? invoice
									.getCountry() : null);
					invoiceQueryDTO.setClientCountry(invoice.getMilsestone().getProject().getClient().getCountry().getName().toString());

					CountryAddress companyAddress = invoiceDao
							.findByUniqueProperty(CountryAddress.class,
									"country", invoiceQueryDTO.getCountry());

					/* logger.warn("at line 707"+companyAddress); */

					if (companyAddress != null)
						invoiceQueryDTO.setCompAddress(companyAddress
								.getAddress());

					CountryAddress companyName = invoiceDao
							.findByUniqueProperty(CountryAddress.class,
									"country", invoiceQueryDTO.getCountry());
					if (companyName != null)
						invoiceQueryDTO.setCompanyName(companyName
								.getCompanyName());

					invoiceQueryDTO.setClientName(client != null ? client
							.getName() : null);

					invoiceQueryDTO.setClientId(client != null ? client.getId()
							: null);
					invoiceQueryDTO.setGstCode(client != null ? client
							.getGstCode() : null);
					invoiceQueryDTO
							.setContactPersonName(client != null ? client
									.getPersonName() : null);
					invoiceQueryDTO.setBillingContactPerson(project
							.getBillingContactPerson() != null ? project
							.getBillingContactPerson() : null);
					invoiceQueryDTO.setBillingContactPersonEmail(project
							.getBillingContactPersonEmail() != null ? project
							.getBillingContactPersonEmail() : null);

					if (milestone != null) {
						invoiceQueryDTO.setMilestoneId(milestone.getId());
						invoiceQueryDTO.setMilestoneNumber(milestone
								.getMilestoneNumber());
						invoiceQueryDTO.setMilestonePercentage(milestone
								.getMilestonePercentage());
						invoiceQueryDTO.setMilestoneName(milestone.getTitle());
						invoiceQueryDTO.setMilestonePlannedEndDate((milestone
								.getPlanedDate() != null) ? milestone
								.getPlanedDate().toString("dd/MM/yyyy") : null);
						invoiceQueryDTO.setMilestoneActualEndDate((milestone
								.getActualDate() != null) ? milestone
								.getActualDate().toString("dd/MM/yyyy") : null);
						invoiceQueryDTO.setMilestoneComments(milestone
								.getComments());
						invoiceQueryDTO.setMilestoneTypeFlag(milestone.getMilestoneTypeFlag() != null ? milestone.getMilestoneTypeFlag() : null);

					}
					ChangeRequest changeRequest = milestone.getChangeRequest();
					if (changeRequest != null) {
						invoiceQueryDTO.setCrId(changeRequest.getId());
						invoiceQueryDTO.setCrName(changeRequest.getName());
						invoiceQueryDTO.setCrDuration(changeRequest
								.getDuration());
					}

					invoiceQueryDTO
							.setAmount((invoice.getAmount() != null) ? (aes256Encryption
									.decrypt(invoice.getAmount())) : null);

					invoiceQueryDTO.setProjectId(project != null ? project
							.getId() : null);

					invoiceQueryDTO.setProjectName(project != null ? project
							.getProjectName() : null);

					invoiceQueryDTO.setProjectManager(project != null ? project
							.getProjectManager().getFullName() : "N/A");

					invoiceQueryDTO
							.setDeliveryManager(deliveryManager != null ? deliveryManager
									.getFullName() : "N/A");

					String invoicestatus = invoice.getInvoiceStatus();
					invoiceQueryDTO.setInvoiceStatus(invoicestatus);

					if (invoicestatus.equalsIgnoreCase("RECEIVED")
							|| invoicestatus
									.equalsIgnoreCase("PARTIALLY RECEIVED")
							|| invoicestatus.equalsIgnoreCase("WRITE OFF")) {
						invoiceQueryDTO.setInvoiceDeletable(Boolean.FALSE);
					} else {
						invoiceQueryDTO.setInvoiceDeletable(Boolean.TRUE);
					}

					invoiceQueryDTO.setCompanyAddress(client != null ? client
							.getAddress() : null);

					invoiceQueryDTO.setOrganization(client != null ? client
							.getOrganization() : null);

					// this variable is used below important
					String currencyType = invoice.getRemittance()
							.getCurrencyType();

					CurrencyToINR currencyToINR = invoiceDao
							.findByUniqueProperty(CurrencyToINR.class,
									"currenyType", invoice.getRemittance()
											.getCurrencyType());
					if (currencyToINR != null)
						invoiceQueryDTO.setRate(currencyToINR.getInrAmount());

					invoiceQueryDTO.setCurrencyType(currencyType);
					invoiceQueryDTO.setBankName(invoice.getRemittance()
							.getBankName());
					invoiceQueryDTO.setLocation(invoice.getRemittance()
							.getLocation());
					invoiceQueryDTO.setWireTransferInstructions(invoice
							.getRemittance().getWireTransferInstructions());

					invoiceQueryDTO.setProjectType(project != null ? project
							.getType().toString() : null);

					invoiceQueryDTO.setStatusNotes(invoice.getStatusNotes());

					invoiceQueryDTO.setInvoiceFileName(invoice
							.getInvoiceFileName());
					invoiceQueryDTO.setActualDate(DateParser.toString(invoice
							.getMilsestone().getActualDate()));
					invoiceQueryDTO.setPlannedDate(DateParser.toString(invoice
							.getMilsestone().getPlanedDate()));

					invoiceQueryDTO.setNotes(aes256Encryption.decrypt(invoice
							.getNotes()));

					invoiceQueryDTO.setShowNotesOnInvoice(invoice
							.getShowNotesOnInvoice());

					invoiceQueryDTO.setShowTaxDetailsOnInvoice(invoice
							.getShowTaxDetailsOnInvoice());

					invoiceQueryDTO.setCountTypeToDisplay(invoice
							.getCountTypeToDisplay());
					
						invoiceQueryDTO.setPercentage(invoice.getPercentage());

					invoiceQueryDTO.setModifiedMilestoneName(invoice
							.getModifiedMilestoneName() != null ? invoice
							.getModifiedMilestoneName() : null);

					invoiceQueryDTO.setTaxDTO(taxBuilder.toDtoList(invoice
							.getTax()));

					if (invoice.getAmountAfterDiscount() != null) {
						invoiceQueryDTO.setAmountAfterDiscount(aes256Encryption
								.decrypt(invoice.getAmountAfterDiscount()
										.toString()));
					}

					invoiceQueryDTO
							.setDiscountType((invoice.getDiscountType() != null) ? invoice
									.getDiscountType() : null);
					invoiceQueryDTO
							.setDiscountRate((invoice.getDiscountRate() != null) ? invoice
									.getDiscountRate() : null);
					invoiceQueryDTO
							.setDiscount((invoice.getDiscount() != null) ? invoice
									.getDiscount() : null);

					invoiceQueryDTO
							.setPoNumber((invoice.getPoNumber() != null) ? invoice
									.getPoNumber() : null);

					invoiceQueryDTO.setStatusNotes(invoice.getStatusNotes());

					invoiceQueryDTO
							.setLineitem((invoice.getLineItems() != null) ? lineItemBuilder
									.toDto(invoice.getLineItems()) : null);
					invoiceQueryDTO
							.setRoleDtos(invoice.getLineItems() != null ? resourceRole(invoice
									.getLineItems()) : null);

					Long totalReceivedAmount = (Long) receivedAmountBuilder
							.decryptReceivedInvoiceAmountList(invoice.getId(),
									invoice.getReceivedAmountList()).get(
									"totalReceivedAmount");

					invoiceQueryDTO.setOnlybodyContent(invoiceQueryDTO
							.getOnlybodyContent());

					invoiceQueryDTO.setTotalReceivedAmount(String
							.valueOf(totalReceivedAmount));
					invoiceQueryDTO.setProformaInvoiceFlag(invoice.getProformaInvoiceFlag());

					List<ProjectNumbers> projectNumbers = resourceManagementDAO
							.getProjectNumbers(resourceManagementDAO.findBy(
									Project.class, invoice.getMilsestone()
											.getProject().getId()));

					ChangeRequest changeRequest2 = invoice.getMilsestone()
							.getChangeRequest();

					for (ProjectNumbers numbers : projectNumbers) {

						if (changeRequest2 != null
								&& numbers.getChangeRequest() != null) {

							if (numbers.getChangeRequest().getId()
									.equals(changeRequest2.getId())) {
								AES256Encryption aes256Encryption2 = new AES256Encryption(
										String.valueOf(invoice.getMilsestone()
												.getProject().getId()),
										numbers.getSaltKey());

								invoiceQueryDTO
										.setProjectOrCRNumbers(aes256Encryption2
												.decrypt(numbers
														.getProjectAmount()));

							}

						} else {

							if (numbers.getChangeRequest() == null) {
								AES256Encryption aes256Encryption2 = new AES256Encryption(
										String.valueOf(invoice.getMilsestone()
												.getProject().getId()),
										numbers.getSaltKey());
								invoiceQueryDTO
										.setProjectOrCRNumbers(aes256Encryption2
												.decrypt((numbers
														.getProjectAmount())));
							}
						}

					}

					if (invoice.getTotalAmount() != null) {

						Double totamt = new Double(
								aes256Encryption.decrypt(invoice
										.getTotalAmount().toString()));
						BigDecimal bigDecimal = new BigDecimal(
								Math.round(totamt));
						invoiceQueryDTO.setTotalAmount(bigDecimal);
						if (totamt >= 0) {

							if (currencyType.equalsIgnoreCase("INR")) {
								invoiceQueryDTO
										.setNumberInWords(new NumberToWordConversion()
												.convertNumberToWords(bigDecimal
														.intValue()));
							} else {
								/*
								 * invoiceQueryDTO
								 * .setNumberInWords(EnglishNumberstoWords
								 * .convert(bigDecimal.intValue()));
								 */
								invoiceQueryDTO
										.setNumberInWords(new NumberToWordUSCurrency()
												.convert(bigDecimal.longValue()));

							}

						}

					}

					Set<LineItem> lineitem = invoice.getLineItems();

					Map<String, List<String>> empDisMap = new HashMap<String, List<String>>();

					if (lineitem != null) {

						for (LineItem ldto : lineitem) {

							// AES256Encryption aes256Encryption2 = new
							// AES256Encryption(
							// String.valueOf(ldto.getId()),
							// ldto.getSaltkey());

							if (empDisMap.get(ldto.getDescription()) == null) {

								List<String> tlist = new ArrayList<String>();

								if (ldto.getItem() instanceof Employee) {
									Employee employee = (Employee) ldto
											.getItem();
									if (employee != null
											&& ldto.getDescription() != null) {

										tlist.add(employee.getFirstName());

										empDisMap.put(ldto.getDescription(),
												tlist);
									}
								}
							} else {
								if (ldto.getItem() instanceof Employee) {
									Employee employee = (Employee) ldto
											.getItem();
									if (employee != null
											&& ldto.getDescription() != null) {

										empDisMap.get(ldto.getDescription())
												.add(employee.getFirstName());

									}
								}

							}
						}
					}
					List<String> roleAndNameCount = new ArrayList<String>();

					for (Entry<String, List<String>> entry : empDisMap
							.entrySet()) {

						roleAndNameCount.add(entry.getValue().size() + "X"
								+ entry.getKey());
					}

					if (roleAndNameCount != null) {
						invoiceQueryDTO.setRoleAndNameCount(roleAndNameCount);
					}

					List<String> resourceNames = new ArrayList<String>();
					if (lineitem != null) {
						for (LineItem ldto : lineitem) {
							if (ldto.getItem() instanceof Employee) {
								Employee employee = (Employee) ldto.getItem();
								if (employee != null) {
									resourceNames.add(employee.getFullName());
								}
							}
						}
					}

					if (resourceNames != null) {
						resourceNames.removeAll(Collections.singleton(null));
						Collections.sort(resourceNames);
						invoiceQueryDTO.setResourceNames(resourceNames);
					}
					invoiceQueryDTOList.add(invoiceQueryDTO);
				}
			}

		}
		return invoiceQueryDTOList;
	}

	public Invoice ConvertUpdatedInvoiceToEntity(InvoiceDTO invoicedto,
			Invoice invoice, Milestone milestone) throws ParseException {

		invoice.setDueDate(DateParser.toDate(invoicedto.getDueDate()));

		// Here while updating Invoice if line item is removed we are making
		// that line item status as "deleted" in DB

		List<Long> dtoLineItemIds = new ArrayList<Long>();
		Set<LineItemDTO> dtoLineItems = invoicedto.getLineitem();
		for (LineItemDTO dto : dtoLineItems) {
			dtoLineItemIds.add(dto.getId());
		}

		Invoice retreviedInvoice = invoiceDao.findBy(Invoice.class,
				invoicedto.getId());

		Set<LineItem> databaseLineItems = retreviedInvoice.getLineItems();

		for (LineItem lineItem : databaseLineItems) {
			if (!dtoLineItemIds.contains(lineItem.getId())) {
				lineItem.setStatus("DELETED");
				invoiceDao.update(lineItem);
			}
		}

		// line item status updation ends here//

		Set<LineItem> lineItems = new HashSet<LineItem>();
		for (LineItemDTO lineItemDTO : invoicedto.getLineitem()) {
			lineItems.add(lineItemBuilder.getLineItemEntity(lineItemDTO));
		}

		invoice.setLineItems(lineItems);

		Set<Tax> tax = new HashSet<Tax>();
		for (TaxDTO dto : invoicedto.getTaxDTO()) {
			tax.add(taxBuilder.toEditEntity(dto));
		}
		invoice.setTax(tax);

		invoice.setInvoiceDate(DateParser.toDate(invoicedto.getInvoiceDate()));
		invoice.setPercentage(invoicedto.getPercentage());

		if (invoicedto.getInvoiceAmountReceviedDate() != null) {
			invoice.setInvoiceAmountReceviedDate(DateParser.toDate(invoicedto
					.getInvoiceAmountReceviedDate()));
		}
		if (invoicedto.getInvoiceAmountSentDate() != null) {
			invoice.setInvoiceAmountSentDate(DateParser.toDate(invoicedto
					.getInvoiceAmountSentDate()));
		}
		invoice.setConversionRate((invoicedto.getConversionRate() != null) ? invoicedto
				.getConversionRate() : null);
		if (invoicedto != null) {
			AES256Encryption aes256Encryption = new AES256Encryption(
					String.valueOf(invoice.getId()), invoice.getSaltKey());

			invoice.setNotes(aes256Encryption.encrypt(invoicedto.getNotes()));
			invoice.setShowNotesOnInvoice(invoicedto.getShowNotesOnInvoice());
			// below number is serial number
			invoice.setNumber(invoicedto.getNumber());
			//logger.warn("serial number" + invoicedto.getNumber());

			// below one is invoice number
			invoice.setInvoiceNumber(invoicedto.getInvoiceNumber());

			//logger.warn("invoice number" + invoicedto.getInvoiceNumber());
			//logger.warn("invoice" + invoice.getInvoiceNumber());

			invoice.setPaymentTerm(invoicedto.getPaymentTerm());
			Remittance remittance = invoiceDao.findBy(Remittance.class,
					invoicedto.getRemittance().getId());
			invoice.setRemittance(remittance);
			invoice.setSubTotal(aes256Encryption.encrypt(invoicedto
					.getSubTotal()));
			invoice.setAmount(aes256Encryption.encrypt(invoicedto.getAmount()));
			invoice.setTotalAmount(aes256Encryption.encrypt(invoicedto
					.getTotalAmount()));
			invoice.setCountry(invoicedto.getCountry());
			invoice.setInvoiceStatus(invoicedto.getInvoiceStatus());

			invoice.setAmountAfterDiscount((invoicedto.getAmountAfterDiscount() != null) ? aes256Encryption
					.encrypt(invoicedto.getAmountAfterDiscount()) : null);

			invoice.setCountTypeToDisplay(invoicedto.getCountTypeToDisplay());

			invoice.setDiscount(invoicedto.getDiscount());
			invoice.setConversionRate(invoicedto.getConversionRate());
			invoice.setDiscountRate(invoicedto.getDiscountRate());
			invoice.setDiscountType(invoicedto.getDiscountType());
			invoice.setPoNumber(invoicedto.getPoNumber());
			invoice.setStatusNotes(invoicedto.getStatusNotes());
			invoice.setWriteoffAmount(aes256Encryption.encrypt(invoicedto
					.getWriteoffAmount()));
			invoice.setReceivedAmount(aes256Encryption.encrypt(invoicedto
					.getReceivedAmount()));
			invoice.setReferenceNumber(invoicedto.getReferenceNumber());
			invoice.setInvoiceReferenceNumber((invoicedto
					.getInvoiceReferenceNumber() != null) ? invoicedto
					.getInvoiceReferenceNumber() : null);
			invoice.setIsAdjusted(invoicedto.getIsAdjusted());
			invoice.setShowTaxDetailsOnInvoice(invoicedto
					.getShowTaxDetailsOnInvoice());
			invoice.setTdsAmount(invoicedto.getTdsAmount() != null ? aes256Encryption
					.encrypt(invoicedto.getTdsAmount()) : null);
			invoice.setNetAmount(invoicedto.getNetAmount() != null ? aes256Encryption
					.encrypt(invoicedto.getNetAmount()) : null);

			String status = invoicedto.getInvoiceStatus();

			if (status.equalsIgnoreCase("RECEIVED")
					|| status.equalsIgnoreCase("PARTIALLY RECEIVED")) {

				String saltKey = KeyGenerators.string().generateKey();
				AES256Encryption partialReceivedAmountEncryption = new AES256Encryption(
						String.valueOf(invoice.getId()), saltKey);

				Set<ReceivedInvoiceAmount> amounts = null;
				ReceivedInvoiceAmount amount = new ReceivedInvoiceAmount();

				amount.setReceivedAmount(partialReceivedAmountEncryption
						.encrypt(invoicedto.getReceivedAmount()));

				amount.setReceivedDate(DateParser.toDate(invoicedto
						.getInvoiceAmountReceviedDate()));

				amount.setTdsAmount(invoicedto.getTdsAmount() != null ? partialReceivedAmountEncryption
						.encrypt(invoicedto.getTdsAmount()) : null);

				amount.setNetAmount(invoicedto.getNetAmount() != null ? partialReceivedAmountEncryption
						.encrypt(invoicedto.getNetAmount()) : null);

				amount.setSaltkey(saltKey);

				if (invoice.getReceivedAmountList().isEmpty()) {
					amounts = new HashSet<ReceivedInvoiceAmount>();
					amounts.add(amount);
					invoice.setReceivedAmountList(amounts);
				} else {
					Map<String, Object> receivedInvoiceAmountDetails = receivedAmountBuilder
							.decryptReceivedInvoiceAmountList(invoice.getId(),
									invoice.getReceivedAmountList());

					Long totalReceivedAmount = (Long) receivedInvoiceAmountDetails
							.get("totalReceivedAmount");

					if (!invoicedto.getTotalAmount().equalsIgnoreCase(
							totalReceivedAmount.toString()))
						invoice.getReceivedAmountList().add(amount);
				}

			}

			if (milestone != null) {

				invoice.setMilsestone(milestone);

				// here we are checking wheather invoice for milestone are
				// raised completly or not if completly raised we are making
				// flag true then this milestone will not populate again
				// while
				// raising Invoice

				String type = milestone.getProject().getType().toString();
				/*Long refNo = invoicedto.getId();
				List invoiceRef = invoiceDao.getRefrenceInvoice(refNo);*/
				// If Invoice don't have any proforma Invoice.
				if(invoice.getProformaReferenceNo() == null){
					if (type.equalsIgnoreCase("FIXEDBID")) {
						Double risedPercentage = 0.0;

						 risedPercentage = invoiceDao.risedPercentage(
								invoicedto.getMilestoneId(), invoice.getId());

						risedPercentage = risedPercentage
								+ Double.valueOf(invoicedto.getPercentage());
						System.out.println("rised:" + risedPercentage + "milestone:" + invoicedto.getMilestonePercentage());
						if(invoicedto.getMilestonePercentage()!=null && !invoicedto.getMilestonePercentage().isEmpty()){
						if (Double.valueOf(milestone.getMilestonePercentage())
								.equals(risedPercentage)) {
							// || milestone.getChangeRequest() != null) {
							milestone.setInvoiceStatus(Boolean.TRUE);

						} else {
							//logger.warn("in else of status is false");
							milestone.setInvoiceStatus(Boolean.FALSE);
						}
						}else{
							milestone.setInvoiceStatus(Boolean.TRUE);
						}
						milestone.setInvoiceReopenFlag(Boolean.FALSE);
					}

				}
			
			}
		}

		return invoice;

	}

	public void encryptInvoiceDataAndSave(Long id, Invoice invoice) {

		Invoice encryptingInvoice = invoiceDao.findBy(Invoice.class, id);

		if (encryptingInvoice != null) {
			String saltkey = KeyGenerators.string().generateKey();
			AES256Encryption aes256Encryption = new AES256Encryption(
					String.valueOf(encryptingInvoice.getId()), saltkey);

			invoice.setSaltKey(saltkey);
			encryptingInvoice.setNotes(aes256Encryption.encrypt(invoice
					.getNotes()));
			encryptingInvoice.setSubTotal(aes256Encryption.encrypt(invoice
					.getSubTotal()));
			encryptingInvoice
					.setTdsAmount(invoice.getTdsAmount() != null ? aes256Encryption
							.encrypt(invoice.getTdsAmount()) : null);
			encryptingInvoice
					.setNetAmount(invoice.getNetAmount() != null ? aes256Encryption
							.encrypt(invoice.getNetAmount()) : null);
			encryptingInvoice.setAmount(aes256Encryption.encrypt(invoice
					.getAmount()));
			encryptingInvoice.setAmountAfterDiscount(aes256Encryption
					.encrypt(invoice.getAmountAfterDiscount()));
			encryptingInvoice.setTotalAmount(aes256Encryption.encrypt(invoice
					.getTotalAmount()));
			encryptingInvoice.setPaymentTerm(invoice.getPaymentTerm());
			encryptingInvoice.setCountry(invoice.getCountry());
			encryptingInvoice.setInvoiceFileName(invoice.getInvoiceFileName());

			encryptingInvoice.setCountTypeToDisplay(invoice
					.getCountTypeToDisplay());

			/* Tax tax = encryptingInvoice.getTax(); */
			Set<LineItem> lineitem = encryptingInvoice.getLineItems();

			// this methods to encrypt discount,tax,line items we are sending
			// those to
			// respective and getting encrypted data and setting it to
			// encryptingInvoice

			/* Tax encryptedTax = taxBuilder.encryptAndAddTax(tax, invoice); */

			Set<LineItem> encryptedLineItems = lineItemBuilder
					.encryptLineItem(lineitem);

			Set<Tax> tax = encryptingInvoice.getTax();
			Set<Tax> encryptedTax = taxBuilder.encryptTax(tax);

			encryptingInvoice.setTax(encryptedTax);
			encryptingInvoice.setLineItems(encryptedLineItems);

			invoiceDao.update(encryptingInvoice);
		}
	}

	public InvoiceAudit saveInvoiceAudit(Long invoiceId, String persistType)
			throws ParseException {

		Invoice invoice = invoiceDao.findBy(Invoice.class, invoiceId);
		InvoiceAudit invoiceAudit = null;

		Map<String, Object> employeeDetails = securityUtils
				.getLoggedEmployeeDetailsSecurityContextHolder();
		Employee employee = (Employee) employeeDetails.get("employee");

		if (invoice != null) {
			invoiceAudit = new InvoiceAudit();
			invoiceAudit.setSaltKey(invoice.getSaltKey());
			invoiceAudit.setInvoiceAmount(invoice.getAmount());
			invoiceAudit.setAmountAfterDiscount(invoice
					.getAmountAfterDiscount());
			invoiceAudit.setBankName(invoice.getRemittance().getBankName());
			invoiceAudit.setClientname(invoice.getMilsestone().getProject()
					.getClient().getName());
			invoiceAudit.setCountry(invoice.getCountry());
			invoiceAudit.setCurrencyType(invoice.getRemittance()
					.getCurrencyType());

			invoiceAudit.setDueDate(invoice.getDueDate());
			invoiceAudit.setInvoiceAmountReceviedDate(invoice
					.getInvoiceAmountReceviedDate());
			invoiceAudit.setInvoiceSentDate(invoice.getInvoiceAmountSentDate());
			invoiceAudit.setInvoiceDate(invoice.getInvoiceDate());
			invoiceAudit.setInvoiceId(invoiceId);
			invoiceAudit.setInvoiceStatus(invoice.getInvoiceStatus());
			invoiceAudit.setMilstoneTitle(invoice.getMilsestone().getTitle());
			invoiceAudit.setModifiedBy(employee.getFullName());
			invoiceAudit.setModifiedDate(new Second());
			invoiceAudit.setNotes(invoice.getNotes());
			// invoice serial number
			invoiceAudit.setInvoiceNumber(invoice.getNumber());
			// invoice number
			invoiceAudit.setNumber(invoice.getInvoiceNumber());
			invoiceAudit.setProjectName(invoice.getMilsestone().getProject()
					.getProjectName());
			invoiceAudit.setPersistType(persistType);
			invoiceAudit.setSubTotal(invoice.getSubTotal());
			invoiceAudit.setTdsAmount(invoice.getTdsAmount() != null ? invoice
					.getTdsAmount() : null);
			invoiceAudit.setNetAmount(invoice.getNetAmount() != null ? invoice
					.getNetAmount() : null);
			invoiceAudit.setDiscountType(invoice.getDiscountType());
			invoiceAudit.setDiscountRate(invoice.getDiscountRate());
			invoiceAudit.setDiscountInAmount(invoice.getDiscount());
			invoiceAudit.setTotalAmount(invoice.getTotalAmount());
			invoiceAudit.setWireTransferInstructions(invoice.getRemittance()
					.getWireTransferInstructions());
			invoiceAudit
					.setConversionRate((invoice.getConversionRate() != null) ? invoice
							.getConversionRate() : null);

			invoiceAudit.setStatusNotes(invoice.getStatusNotes());
			invoiceAudit.setAdjustedInvoice(invoice.getIsAdjusted());
			invoiceAudit.setModifiedMilestoneName(invoice
					.getModifiedMilestoneName() != null ? invoice
					.getModifiedMilestoneName() : null);

			if (invoice.getLineItems() != null) {
				Set<LineItem> lineItems = invoice.getLineItems();
				Set<LineItemAudit> itemAudits = new HashSet<LineItemAudit>();

				for (LineItem item : lineItems) {
					LineItemAudit audit = new LineItemAudit();
					audit = lineItemBuilder.getLineItemAudit(item);

					itemAudits.add(audit);
				}
				invoiceAudit.setLineitems(itemAudits);
			}
			if (invoice.getTax() != null) {
				invoiceAudit.setTaxAudits(taxBuilder
						.getTaxDetailsForAudit(invoice.getTax()));
			}

			if (invoice.getReceivedAmountList() != null) {
				invoiceAudit.setAmountAudits(receivedAmountBuilder
						.getReceivedAmountAudit(
								invoice.getReceivedAmountList(), invoiceId));
			}

			/*
			 * String status = invoice.getInvoiceStatus();
			 * 
			 * if (status.equalsIgnoreCase("RECEIVED") ||
			 * status.equalsIgnoreCase("PARTIALLY RECEIVED")) {
			 * 
			 * String saltKey = KeyGenerators.string().generateKey();
			 * AES256Encryption partialReceivedAmountEncryption = new
			 * AES256Encryption( String.valueOf(invoice.getId()), saltKey);
			 * 
			 * Set<ReceivedInvoiceAmountAudit> amounts = null;
			 * ReceivedInvoiceAmountAudit amount = new
			 * ReceivedInvoiceAmountAudit();
			 * 
			 * amount.setReceivedAmount(partialReceivedAmountEncryption
			 * .encrypt(invoice.getReceivedAmount()));
			 * 
			 * amount.setReceivedDate(DateParser.toDate(invoice
			 * .getInvoiceAmountReceviedDate().toString()));
			 * 
			 * amount.setSaltkey(saltKey);
			 * 
			 * if (invoice.getReceivedAmountList() != null) { amounts = new
			 * HashSet<ReceivedInvoiceAmountAudit>(); amounts.add(amount);
			 * invoiceAudit.setAmountAudits(amounts); } else { Map<String,
			 * Object> receivedInvoiceAmountDetails = receivedAmountBuilder
			 * .decryptReceivedInvoiceAmountList(invoice.getId(),
			 * invoice.getReceivedAmountList());
			 * 
			 * Long totalReceivedAmount = (Long) receivedInvoiceAmountDetails
			 * .get("totalReceivedAmount");
			 * 
			 * if (!invoice.getTotalAmount().equalsIgnoreCase(
			 * totalReceivedAmount.toString()))
			 * invoiceAudit.getAmountAudits().add(amount); }
			 * 
			 * }
			 */
			invoiceAudit.setProformaInvoiceFlag(invoice.getProformaInvoiceFlag());
			invoiceAudit.setProformaReferenceNo(invoice.getProformaReferenceNo());
			if(invoice.getProformaReferenceNo()!=null)
			{
				Invoice refInvoice = invoiceDao.findBy(Invoice.class,(invoice.getProformaReferenceNo().getId()));
				System.out.println("Ref Invoice object is = "+ refInvoice.getId());
				 invoice.setProformaReferenceNo(refInvoice);
				 System.out.println("After setting invoice ref");
			}

		}
		return invoiceAudit;
	}

	public List<InvoiceAuditDto> auditToDtoList(List<InvoiceAudit> audits) {
		List<InvoiceAuditDto> auditDtos = new ArrayList<InvoiceAuditDto>();
		if (audits != null) {
			for (InvoiceAudit audit : audits) {
				InvoiceAuditDto auditDto = auditEntityToDto(audit);
				auditDtos.add(auditDto);
			}
		}

		return auditDtos;
	}

	public InvoiceAuditDto auditEntityToDto(InvoiceAudit audit) {
		InvoiceAuditDto auditDto = null;
		if (audit != null) {
			AES256Encryption aes256Encryption = new AES256Encryption(
					String.valueOf(audit.getInvoiceId()), audit.getSaltKey());
			auditDto = new InvoiceAuditDto();
			auditDto.setId(audit.getId());
			auditDto.setAmount(audit.getInvoiceAmount() != null ? aes256Encryption
					.decrypt(audit.getInvoiceAmount()) : null);
			auditDto.setAmountAfterDiscount(audit.getAmountAfterDiscount() != null ? aes256Encryption
					.decrypt(audit.getAmountAfterDiscount()) : null);
			auditDto.setBankName(audit.getBankName());
			auditDto.setClientname(audit.getClientname());
			auditDto.setCountry(audit.getCountry());
			auditDto.setCurrencyType(audit.getCurrencyType());
			auditDto.setDiscount(audit.getDiscountInAmount());
			auditDto.setDiscountRate(audit.getDiscountRate());
			auditDto.setDiscountType(audit.getDiscountType());
			auditDto.setDueDate(audit.getDueDate().toString("dd/MM/yyyy"));
			auditDto.setId(audit.getId());// need to check again
			if (audit.getInvoiceAmountReceviedDate() != null) {
				auditDto.setInvoiceAmountReceviedDate(audit
						.getInvoiceAmountReceviedDate().toString("dd/MM/yyyy"));
			}

			auditDto.setInvoiceSentDate((audit.getInvoiceSentDate() != null) ? audit
					.getInvoiceSentDate().toString("dd/MM/yyyy") : null);
			// auditDto.setInvoiceCount(audit.getInvoiceCount());
			auditDto.setConversionRate((audit.getConversionRate() != null) ? audit
					.getConversionRate() : null);
			auditDto.setInvoiceDate(audit.getInvoiceDate().toString(
					"dd/MM/yyyy"));
			// auditDto.setInvoiceDuration(audit.getInvoiceDuration());
			auditDto.setInvoiceId(audit.getInvoiceId());// no need as no where
														// to use this
			// auditDto.setInvoiceRate(audit.getInvoiceRate());
			auditDto.setInvoiceStatus(audit.getInvoiceStatus());

			auditDto.setAdjustedInvoice(audit.getAdjustedInvoice());

			if (audit.getLineitems() != null) {
				Set<LineItemDTO> lineItems = lineIteimAuditToLineItemDto(audit
						.getLineitems());
				auditDto.setLineitems(lineItems);// need to write one more
													// method
			}
			if (audit.getTaxAudits() != null) {
				auditDto.setTaxDTOs(taxBuilder.getTaxAuditDTO(audit
						.getTaxAudits()));
			}
			if (audit.getAmountAudits() != null) {
				auditDto.setAmountDTOs(receivedAmountBuilder
						.getReceivedAmountAuditDTOs(audit.getAmountAudits(),
								audit.getInvoiceId()));
				System.out.println("audit invoice id" + audit.getInvoiceId());
			}

			auditDto.setMilestoneName(audit.getMilstoneTitle());
			auditDto.setModifiedBy(audit.getModifiedBy());
			auditDto.setModifiedDate(audit.getModifiedDate().toString(
					"dd-MM-yyyy hh:mm a"));
			auditDto.setNotes((audit.getNotes() != null) ? aes256Encryption
					.decrypt(audit.getNotes()) : null);
			auditDto.setNumber(audit.getInvoiceNumber());
			auditDto.setInvoiceNumber(audit.getNumber() != null ? audit
					.getNumber() : null);
			// auditDto.setPaymentTerm(audit.getPaymentTerm());
			auditDto.setPersistType(audit.getPersistType());

			Project project = invoiceDao.findByUniqueProperty(Project.class,
					"projectName", audit.getProjectName());

			if (project != null) {

				// logger.warn(project.getId());
				// need to create project object
				Employee employee = project.getProjectManager();
				auditDto.setProjectManager(employee.getFirstName() + " "
						+ employee.getLastName());
				auditDto.setProjectStatus(project.getStatus().name());
				auditDto.setProjectType(project.getType().name());
			}
			auditDto.setProjectName(audit.getProjectName());
			// auditDto.setSaltKey(audit);
			auditDto.setSubTotal(audit.getSubTotal() != null ? aes256Encryption
					.decrypt(audit.getSubTotal()) : null);
			auditDto.setTdsAmount(audit.getTdsAmount() != null ? audit
					.getTdsAmount() : null);
			auditDto.setNetAmount(audit.getNetAmount() != null ? audit
					.getNetAmount() : null);
			auditDto.setTax(audit.getTaxAmount());
			auditDto.setTaxRate(audit.getTaxRate());
			auditDto.setTaxType(audit.getTaxType());
			auditDto.setTotalAmount(audit.getTotalAmount() != null ? aes256Encryption
					.decrypt(audit.getTotalAmount()) : null);
			auditDto.setWireTransferInstructions(audit
					.getWireTransferInstructions());

			auditDto.setStatusNotes(audit.getStatusNotes());

		}
		return auditDto;
	}

	public Set<LineItemDTO> lineIteimAuditToLineItemDto(
			Set<LineItemAudit> lineItemAudits) {
		Set<LineItemDTO> itemDTOs = new HashSet<LineItemDTO>();
		if (lineItemAudits != null) {
			for (LineItemAudit audit : lineItemAudits) {
				AES256Encryption aes256Encryption = new AES256Encryption(
						String.valueOf(audit.getLineItemId()),
						audit.getItemSaltkey());
				LineItemDTO dto = new LineItemDTO();
				dto.setCount(audit.getCount());
				dto.setDescription(audit.getDescription());
				dto.setDuration(audit.getDuration());
				dto.setAmount(audit.getAmount() != null ? aes256Encryption
						.decrypt(audit.getAmount()) : null);

				dto.setLineItemAmount(audit.getLineItemAmount() != null ? aes256Encryption
						.decrypt(audit.getLineItemAmount()) : null);

				dto.setFromDate((audit.getFromDate() != null) ? audit
						.getFromDate() : null);
				dto.setEndDate((audit.getEndDate() != null) ? audit
						.getEndDate() : null);
				dto.setItem(audit.getItem().toString());
				dto.setMonthWorkingDays((audit.getMonthWorkingDays() != null) ? audit
						.getMonthWorkingDays() : null);
				dto.setHolidays((audit.getHolidays() != null) ? audit
						.getHolidays() : null);
				dto.setLeaves((audit.getLeaves() != null) ? audit.getLeaves()
						: null);

				dto.setHours((audit.getHours() != null) ? audit.getHours()
						: null);

				dto.setTotalValue((audit.getTotalValue() != null) ? audit
						.getTotalValue() : null);

				dto.setBillableDays((audit.getBillableDays() != null) ? audit
						.getBillableDays() : null);

				dto.setId(audit.getId());
				// dto.setItem(audit.getItem());
				dto.setRate(audit.getRate());
				// dto.setRole(audit.getRole());
				if (audit.getItem() instanceof Employee) {
					Employee empName = (Employee) audit.getItem();
					dto.setEmpName(empName.getFullName());
					dto.setEmpId(empName.getEmployeeId());
				}
				itemDTOs.add(dto);
			}
		}
		return itemDTOs;
	}

	public String roundNumber(String value) {
		double amount = Double.parseDouble(value);
		DecimalFormat formatter = new DecimalFormat("#,###");
		String roundvalue = formatter.format(amount);
		return roundvalue;
	}

	String setLength(String taxlength) {
		for (int i = taxlength.length(); i < 8; i++) {
			taxlength = " " + taxlength;
		}
		return taxlength;
	}

	public Set<ResourceRoleDto> resourceRole(Set<LineItem> items) {

		List<LineItem> lineItems = new ArrayList<LineItem>(items);
		Set<ResourceRoleDto> dtos = new HashSet<ResourceRoleDto>();
		List<ResourceRoleDto> roleList = new ArrayList<ResourceRoleDto>();
		ResourceRoleDto dto = null;
		Boolean resorceFlag = null;
		for (LineItem item : lineItems) {
			resorceFlag = false;
			if (item.getItem() instanceof Employee) {
				// logger.warn("Employee" + item.getItem());
				if (roleList.size() > 0) {
					int index = -1;
					for (ResourceRoleDto role : roleList) {
						index++;
						if (role.getRate() != null) {
							if (role.getRole().equalsIgnoreCase(
									item.getDescription())
									&& role.getRate().equalsIgnoreCase(
											item.getRate())
									&& role.getDuration().equalsIgnoreCase(
											item.getDuration().getDuration())) {
								resorceFlag = true;
								break;
							}
						}
					}
					if (resorceFlag) {
						AES256Encryption aes256Encryption = new AES256Encryption(
								String.valueOf(item.getId()), item.getSaltkey());
						dto = roleList.get(index);
						if (item.getDuration().getDuration()
								.equalsIgnoreCase("hours")) {
							dto.setDurationcount(dto.getDurationcount()
									+ Double.parseDouble(item.getTotalValue()));
						} else {
							dto.setDurationcount(dto.getDurationcount()
									+ Double.parseDouble(item.getBillableDays()));
						}
						dto.setAmount(String.valueOf(Double.parseDouble(dto
								.getAmount())
								+ Double.parseDouble(aes256Encryption
										.decrypt(item.getAmount()))));
						dto.setNumberOfResources(dto.getNumberOfResources() + 1);
						// dto.setDurationcount(dto.getDurationcount()
						// + Integer.parseInt(item.getCount()));
						roleList.set(index, dto);
					} else {
						AES256Encryption aes256Encryption = new AES256Encryption(
								String.valueOf(item.getId()), item.getSaltkey());
						dto = new ResourceRoleDto();
						dto.setRole(item.getDescription());
						dto.setAmount(aes256Encryption.decrypt(item.getAmount()));
						dto.setNumberOfResources(1);
						if (item.getDuration().getDuration()
								.equalsIgnoreCase("hours")) {
							dto.setDurationcount(Double.parseDouble(item
									.getTotalValue()));
						} else {
							dto.setDurationcount(Double.parseDouble(item
									.getBillableDays()));
						}
						dto.setRate(item.getRate());
						dto.setDuration(item.getDuration().getDuration());
						dtos.add(dto);
						roleList.add(dto);
					}

				} else {
					AES256Encryption aes256Encryption = new AES256Encryption(
							String.valueOf(item.getId()), item.getSaltkey());

					dto = new ResourceRoleDto();
					dto.setRole(item.getDescription());
					dto.setNumberOfResources(1);
					dto.setAmount(item.getAmount() != null ? aes256Encryption
							.decrypt(item.getAmount()) : null);
					if (item.getDuration().getDuration()
							.equalsIgnoreCase("hours")) {
						dto.setDurationcount(Double.parseDouble(item
								.getTotalValue()));
					} else {
						dto.setDurationcount(Double.parseDouble(item
								.getBillableDays()));
					}
					dto.setRate(item.getRate());
					dto.setDuration(item.getDuration().getDuration());
					dtos.add(dto);
					roleList.add(dto);
				}

			}
		}

		return new HashSet<ResourceRoleDto>(roleList);
	}

	public Set<ResourceRoleDto> resourceRoleDto(Set<LineItemDTO> items) {

		List<LineItemDTO> lineItems = new ArrayList<LineItemDTO>(items);
		Set<ResourceRoleDto> dtos = new HashSet<ResourceRoleDto>();
		List<ResourceRoleDto> roleList = new ArrayList<ResourceRoleDto>();
		ResourceRoleDto dto = null;
		Boolean resorceFlag = null;
		for (LineItemDTO item : lineItems) {
			resorceFlag = false;
			if (item.getEmpId() != null) {
				// logger.warn("Employee" + item.getItem());
				if (roleList.size() > 0) {
					int index = -1;
					for (ResourceRoleDto role : roleList) {
						index++;
						if (role.getRate() != null) {
							if (role.getRole().equalsIgnoreCase(item.getRole())
									&& role.getRate().equalsIgnoreCase(
											item.getRate())
									&& role.getDuration().equalsIgnoreCase(
											item.getDuration())) {
								resorceFlag = true;
								break;
							}
						}
					}
					if (resorceFlag) {

						dto = roleList.get(index);
						if (item.getDuration().equalsIgnoreCase("hours")) {
							dto.setDurationcount(dto.getDurationcount()
									+ Double.parseDouble(item.getTotalValue()));
						} else {
							dto.setDurationcount(dto.getDurationcount()
									+ Double.parseDouble(item.getBillableDays()));
						}
						dto.setAmount(String.valueOf(Double.parseDouble(dto
								.getAmount())
								+ Integer.valueOf(item.getAmount())));
						dto.setNumberOfResources(dto.getNumberOfResources() + 1);
						// dto.setDurationcount(dto.getDurationcount()
						// + Integer.parseInt(item.getCount()));
						roleList.set(index, dto);
					} else {

						dto = new ResourceRoleDto();
						dto.setRole(item.getRole());
						dto.setAmount(item.getAmount());
						dto.setNumberOfResources(1);
						if (item.getDuration().equalsIgnoreCase("hours")) {
							dto.setDurationcount(Double.parseDouble(item
									.getTotalValue()));
						} else {
							dto.setDurationcount(Double.parseDouble(item
									.getBillableDays()));
						}
						dto.setRate(item.getRate());
						dto.setDuration(item.getDuration());
						dtos.add(dto);
						roleList.add(dto);
					}

				} else {

					dto = new ResourceRoleDto();
					dto.setRole(item.getRole());
					dto.setNumberOfResources(1);
					dto.setAmount(item.getAmount());
					if (item.getDuration().equalsIgnoreCase("hours")) {
						dto.setDurationcount(Double.parseDouble(item
								.getTotalValue()));
					} else {
						dto.setDurationcount(Double.parseDouble(item
								.getBillableDays()));
					}
					dto.setRate(item.getRate());
					dto.setDuration(item.getDuration());
					dtos.add(dto);
					roleList.add(dto);
				}

			}
		}

		return new HashSet<ResourceRoleDto>(roleList);
	}

	public List<String> getInvoiceStatusByStatus(String invoicestatus) {
		List<String> status = null;
		if (invoicestatus != null) {
			if (invoicestatus.equalsIgnoreCase("RAISED")) {
				status = new ArrayList<String>(Arrays.asList("RAISED", "SENT",
						"OVER DUE", "NOT SENT"));
			} else if (invoicestatus.equalsIgnoreCase("NOT SENT")) {
				status = new ArrayList<String>(
						Arrays.asList("SENT", "NOT SENT"));
			} else if (invoicestatus.equalsIgnoreCase("SENT")) {
				status = new ArrayList<String>(Arrays.asList(
						"PARTIALLY RECEIVED", "RECEIVED", "WRITE OFF", "SENT"));
			} else if (invoicestatus.equalsIgnoreCase("RECEIVED")) {
				status = new ArrayList<String>(Arrays.asList("RECEIVED"));
			} else if (invoicestatus.equalsIgnoreCase("PARTIALLY RECEIVED")
					|| invoicestatus.equalsIgnoreCase("WRITE OFF")) {
				status = new ArrayList<String>(Arrays.asList(
						"PARTIALLY RECEIVED", "RECEIVED", "WRITE OFF"));
			} else if (invoicestatus.equalsIgnoreCase("OVER DUE")) {
				status = new ArrayList<String>(Arrays.asList(
						"PARTIALLY RECEIVED", "RECEIVED", "WRITE OFF",
						"OVER DUE", "SENT"));
			}

		}
		return status;
	}

	public InvoiceReminderLog reminderLogToentity(InvoiceReminderLogDTO dto) {
		InvoiceReminderLog invoiceReminderLog = new InvoiceReminderLog();
		if (dto != null) {
			invoiceReminderLog.setCreatedBy(securityUtils
					.getLoggedEmployeeIdforSecurityContextHolder());
			invoiceReminderLog.setCreatedDate(new Second());
			invoiceReminderLog
					.setDescription(dto.getDescription() != null ? dto
							.getDescription() : null);
			invoiceReminderLog.setTo(dto.getTo());
			invoiceReminderLog.setFrom(dto.getFrom());
			invoiceReminderLog.setDetails(dto.getDetails());
			invoiceReminderLog.setSubject(dto.getSubject());
			invoiceReminderLog.setInvoiceId(dto.getInvoiceId());
			invoiceReminderLog.setBcc(dto.getBcc());
			invoiceReminderLog.setCc(dto.getCc());

		}
		return invoiceReminderLog;
	}

	public List<InvoiceReminderLogDTO> invoicereminderLogentityListToDTOList(
			List<InvoiceReminderLog> invoiceReminderLogs) {
		List<InvoiceReminderLogDTO> dtos = new ArrayList<InvoiceReminderLogDTO>();
		if (invoiceReminderLogs != null) {
			for (InvoiceReminderLog invoiceReminderLog : invoiceReminderLogs) {
				InvoiceReminderLogDTO dto = new InvoiceReminderLogDTO();
				dto = logentityToDTO(invoiceReminderLog);
				dtos.add(dto);
			}
		}
		return dtos;
	}

	public InvoiceReminderLogDTO logentityToDTO(InvoiceReminderLog log) {
		InvoiceReminderLogDTO dto = new InvoiceReminderLogDTO();
		if (log != null) {
			dto.setId(log.getId());
			Employee employee = invoiceDao.findBy(Employee.class,
					log.getCreatedBy());
			dto.setCreatedBy(employee.getFullName());
			dto.setCreatedDate(log.getCreatedDate().toString());
			dto.setDescription(log.getDescription());
			dto.setSubject(log.getSubject());
			dto.setTo(log.getTo());
			dto.setFrom(log.getFrom());
			dto.setDetails(log.getDetails());
			dto.setBcc(log.getBcc());
			dto.setCc(log.getCc());
		}
		return dto;
	}

	public List<InvoiceAuditDto> auditToDtoLists(List<InvoiceAudit> audits) {
		List<InvoiceAuditDto> auditDtos = new ArrayList<InvoiceAuditDto>();
		if (audits != null) {
			for (InvoiceAudit audit : audits) {
				Long refNo = audit.getInvoiceId();
				List<InvoiceAudit> refInvoice = invoiceDao.getRefrenceInvoice(refNo);
				if(refInvoice.isEmpty()){
					InvoiceAuditDto auditDto = getAuditReportDtos(audit);
					auditDtos.add(auditDto);
				}
			}
		}

		return auditDtos;
	}

	public InvoiceAuditDto getAuditReportDtos(InvoiceAudit audit) {
		InvoiceAuditDto auditDto = null;
		if (audit != null) {
		/*	Long invoiceRefNo = audit.getInvoiceId();
			List<InvoiceAudit> invoiceRef = invoiceDao.getRefrenceInvoice(invoiceRefNo);
			
			// Either proforma or Invoice will calculate in total
			if(invoiceRef.isEmpty()){*/
				
			auditDto = new InvoiceAuditDto();

			AES256Encryption aes256Encryption = new AES256Encryption(
					String.valueOf(audit.getInvoiceId()), audit.getSaltKey());

			auditDto.setId(audit.getId());
			auditDto.setAmount(audit.getInvoiceAmount() != null ? aes256Encryption
					.decrypt(audit.getInvoiceAmount()) : null);

			auditDto.setClientname(audit.getClientname());
			auditDto.setCountry(audit.getCountry());
			auditDto.setCurrencyType(audit.getCurrencyType());
			CurrencyToINR currencyToINR = invoiceDao
					.findByUniqueProperty(CurrencyToINR.class, "currenyType",
							audit.getCurrencyType());

			auditDto.setRate(currencyToINR == null ? 1 : currencyToINR
					.getInrAmount());

			if (audit.getInvoiceAmountReceviedDate() != null) {
				auditDto.setInvoiceAmountReceviedDate(audit
						.getInvoiceAmountReceviedDate().toString("dd/MM/yyyy"));
			}
			if (audit.getTaxAudits() != null) {
				auditDto.setTaxDTOs(taxBuilder.getTaxAuditDTO(audit
						.getTaxAudits()));
				//logger.warn("size:" + auditDto.getTaxDTOs().size());
			}

			
			Double totalTaxAmount = 0d;
			if (taxBuilder.getTaxAuditDTO(audit.getTaxAudits()) != null) {
				for (TaxDTO tax : taxBuilder.getTaxAuditDTO(audit
						.getTaxAudits())) {
					totalTaxAmount += (Double.valueOf(tax.getTax()));
				}
			}

			auditDto.setTotalTaxAmount(totalTaxAmount != null ? totalTaxAmount
					: null);

			// invoiceDTO.setTaxDTO(taxBuilder.toDtoList(invoice.getTax()));

			/*
			 * auditDto.setInvoiceDate((audit.getInvoiceDate() !=null) ?
			 * audit.getInvoiceDate().toString("dd/MM/yyy"): null);
			 */
			auditDto.setInvoiceSentDate((audit.getInvoiceSentDate() != null) ? audit
					.getInvoiceSentDate().toString("dd/MM/yyyy") : null);
			auditDto.setConversionRate((audit.getConversionRate() != null) ? audit
					.getConversionRate() : null);

			auditDto.setInvoiceId(audit.getInvoiceId());
			auditDto.setInvoiceStatus(audit.getInvoiceStatus());
			auditDto.setNumber(audit.getInvoiceNumber());
			auditDto.setInvoiceNumber(audit.getNumber() != null ? audit
					.getNumber() : null);

			auditDto.setModifiedMilestoneName(audit.getModifiedMilestoneName() != null ? audit
					.getModifiedMilestoneName() : null);

			/*
			 * Project project = invoiceDao.findByUniqueProperty(Project.class,
			 * "projectName", audit.getProjectName());
			 */

			/*
			 * ProjectAudit projectAudit
			 * =invoiceDao.findByUniqueProperty(ProjectAudit.class,
			 * "projectName", audit.getProjectName()); Project project =
			 * invoiceDao.findBy(Project.class, projectAudit.getProjectId());
			 */

			Invoice invoice = invoiceDao.findBy(Invoice.class,
					audit.getInvoiceId());
			Project project = invoice.getMilsestone().getProject();

			if (project != null) {
				Employee employee = project.getProjectManager();
				auditDto.setProjectManager(employee.getFirstName() + " "
						+ employee.getLastName());
				auditDto.setProjectStatus(project.getStatus().name());
				auditDto.setProjectType(project.getType().name());
			}
			auditDto.setProjectName(audit.getProjectName());
			if (project.getClient() != null) {
				auditDto.setClientCountry(project.getClient().getCountry()
						.getName() != null ? project.getClient().getCountry()
						.getName() : null);
			}
			// auditDto.setSaltKey(audit);
			auditDto.setSubTotal(audit.getSubTotal() != null ? aes256Encryption
					.decrypt(audit.getSubTotal()) : null);

			auditDto.setTotalAmount(audit.getTotalAmount() != null ? aes256Encryption
					.decrypt(audit.getTotalAmount()) : null);

			/*
			 * auditDto.setTdsAmount(audit.getTdsAmount() != null ?
			 * aes256Encryption .decrypt(audit.getTdsAmount()) : null);
			 * auditDto.setNetAmount(audit.getNetAmount() != null ?
			 * aes256Encryption .decrypt(audit.getNetAmount()) : null);
			 */
			/*
			 * Map<String, Object> receivedInvoiceAmountDetails1 =
			 * receivedAmountBuilder
			 * .decryptReceivedInvoiceAmountList(audit.getInvoiceId(),
			 * invoice.getReceivedAmountList());
			 */

			if (audit.getAmountAudits() != null) {
				auditDto.setAmountDTOs(receivedAmountBuilder
						.getReceivedAmountAuditDTOs(audit.getAmountAudits(),
								audit.getInvoiceId()));
			}

			Map<String, Object> receivedInvoiceAmountDetails2 = receivedAmountBuilder
					.decryptReceivedInvoiceAmountAuditListt(
							audit.getInvoiceId(), audit.getAmountAudits());

			/*
			 * auditDto.setAmountDTOs((Set<ReceivedInvoiceAmountDTO>)
			 * receivedInvoiceAmountDetails2 .get("list"));
			 */

			auditDto.setReceivedAmount(String
					.valueOf(receivedInvoiceAmountDetails2
							.get("totalReceivedAmount")));

			auditDto.setTdsAmount(String.valueOf(receivedInvoiceAmountDetails2
					.get("totalTdsAmount")));

			auditDto.setNetAmount(String.valueOf(receivedInvoiceAmountDetails2
					.get("totalNetAmount")));

			Map<String, Long> converter = this.getConverterAmounts();

			Long convertedTotalAmount = 0L;
			if (!audit.getCurrencyType().equalsIgnoreCase("INR")) {
				Double finalTotal = Double.valueOf((aes256Encryption
						.decrypt(audit.getTotalAmount())));

				Long amount = Math.round(finalTotal);

				Long amountInr = converter.get(audit.getCurrencyType());

				convertedTotalAmount = amount
						* (amountInr != null ? amountInr : 1);

				auditDto.setFinalAmount(String.valueOf(convertedTotalAmount));

				/*
				 * Long inrAmount=converter.get(invoice
				 * .getRemittance().getCurrencyType()); convertedTotalAmount =
				 * finalTotal.longValue()*(inrAmount!=null?inrAmount:1);
				 * 
				 * invoiceQueryDTO.setFinalTotalAmount(convertedTotalAmount);
				 */

			} else {
				auditDto.setFinalAmount(audit.getTotalAmount() != null ? aes256Encryption
						.decrypt(audit.getTotalAmount()) : null);
			}

			Long balanceAmount = 0L;

			BigDecimal finalTotal = (new BigDecimal(
					aes256Encryption.decrypt(audit.getTotalAmount())));

			BigDecimal receivedAmount = (new BigDecimal(
					String.valueOf(receivedInvoiceAmountDetails2
							.get("totalReceivedAmount"))));

			balanceAmount = finalTotal.longValue() - receivedAmount.longValue();

			auditDto.setBalanceAmount(balanceAmount.toString());
			
			auditDto.setProformaInvoiceFlag(audit.getProformaInvoiceFlag());
			//auditDto.setProformaReferenceNo(audit.getProformaReferenceNo());
			//}// cheking proforma ref
		}
		return auditDto;

	}

	public Map<String, Long> getConverterAmounts() {

		Map<String, Long> map = new HashMap<String, Long>();

		for (CurrencyToINR currencyToINR : invoiceDao.get(CurrencyToINR.class)) {
			map.put(currencyToINR.getCurrenyType(),
					currencyToINR.getInrAmount());
		}

		return map;

	}

	public List<InvoiceAuditDto> invoiceAuditLog(List<InvoiceAudit> list) {

		List<InvoiceAuditDto> auditDtos = new ArrayList<InvoiceAuditDto>();
		if (list != null) {
			for (InvoiceAudit audit : list) {
				InvoiceAuditDto auditDto = convertAudittoAuditDto(audit);
				auditDtos.add(auditDto);
			}
		}

		return auditDtos;
	}

	private InvoiceAuditDto convertAudittoAuditDto(InvoiceAudit audit) {

		InvoiceAuditDto auditDto = null;
		if (audit != null) {
			auditDto = new InvoiceAuditDto();

			AES256Encryption aes256Encryption = new AES256Encryption(
					String.valueOf(audit.getInvoiceId()), audit.getSaltKey());

			auditDto.setId(audit.getId());

			auditDto.setClientname(audit.getClientname());

			auditDto.setInvoiceSentDate((audit.getInvoiceSentDate() != null) ? audit
					.getInvoiceSentDate().toString("dd/MM/yyyy") : null);
			auditDto.setConversionRate((audit.getConversionRate() != null) ? audit
					.getConversionRate() : null);

			auditDto.setInvoiceId(audit.getInvoiceId());
			auditDto.setNumber(audit.getInvoiceNumber());
			auditDto.setInvoiceNumber(audit.getNumber() != null ? audit
					.getNumber() : null);

			auditDto.setProjectName(audit.getProjectName());
			auditDto.setModifiedBy(audit.getModifiedBy());
			auditDto.setPersistType(audit.getPersistType());
			auditDto.setModifiedDate(audit.getModifiedDate().toString(
					"dd-MM-yyyy hh:mm a"));

		}
		return auditDto;

	}
	
	
}
