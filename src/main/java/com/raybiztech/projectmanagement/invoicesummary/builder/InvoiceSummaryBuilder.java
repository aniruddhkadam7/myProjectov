package com.raybiztech.projectmanagement.invoicesummary.builder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.stereotype.Component;

import com.raybiztech.payslip.utility.AES256Encryption;
import com.raybiztech.projectmanagement.business.ChangeRequest;
import com.raybiztech.projectmanagement.business.Client;
import com.raybiztech.projectmanagement.business.Milestone;
import com.raybiztech.projectmanagement.business.Project;
import com.raybiztech.projectmanagement.business.ProjectNumbers;
import com.raybiztech.projectmanagement.invoice.builder.InvoiceBuilder;
import com.raybiztech.projectmanagement.invoice.business.Invoice;
import com.raybiztech.projectmanagement.invoice.business.InvoiceSummary;
import com.raybiztech.projectmanagement.invoice.business.ReceivedInvoiceAmount;
import com.raybiztech.projectmanagement.invoice.business.ReceivedInvoiceSummary;
import com.raybiztech.projectmanagement.invoice.business.SentInvoiceSummary;
import com.raybiztech.projectmanagement.invoice.dao.InvoiceDao;
import com.raybiztech.projectmanagement.invoice.dto.InvoiceDTO;
import com.raybiztech.projectmanagement.invoice.dto.InvoiceSummaryDTO;

@Component("invoiceSummaryBuilder")
public class InvoiceSummaryBuilder {

	Logger logger = Logger.getLogger(InvoiceSummaryBuilder.class);

	private final InvoiceDao invoiceDao;
	private final InvoiceBuilder invoiceBuilder;

	@Autowired
	public InvoiceSummaryBuilder(InvoiceDao invoiceDao,
			InvoiceBuilder invoiceBuilder) {
		this.invoiceDao = invoiceDao;
		this.invoiceBuilder = invoiceBuilder;
	}

	public void saveInvoiceSummary(Milestone milestone, Invoice invoice,
			Invoice clonedInvoice) {

		if (milestone != null) {

			Project project = milestone.getProject();
			com.raybiztech.projectmanagement.business.Client client = project
					.getClient();
			ChangeRequest changeRequest = milestone.getChangeRequest();

			InvoiceSummary invoiceSummary = (changeRequest != null) ? invoiceDao
					.getSummaryOfChangeRequest(project, client, changeRequest)
					: invoiceDao.getInvoiceSummary(project, client);

			if (invoiceSummary != null) {
				updateInvoiceSummary(invoiceSummary, invoice, clonedInvoice);
			} else {
				newInvoiceSummary(project, changeRequest, client, invoice);
			}

		}

	}

	public void newInvoiceSummary(Project project, ChangeRequest changeRequest,
			Client client, Invoice invoice) {

		InvoiceSummary invoiceSummary = new InvoiceSummary();

		invoiceSummary.setProject(project);
		invoiceSummary.setClient(client);
		invoiceSummary.setChangeRequest((changeRequest != null) ? changeRequest
				: null);

		// if change request is not null get cr numbers else get project numbers
		// and set to invoicesummary total amount
		ProjectNumbers projectNumbers = (changeRequest != null) ? invoiceDao
				.getAmountofCr(changeRequest) : invoiceDao
				.getProjectNumbers(project);

		String numbers = null;
		if (projectNumbers != null) {
			AES256Encryption projectNumbersAes256Encryption = new AES256Encryption(
					String.valueOf(projectNumbers.getProject().getId()),
					projectNumbers.getSaltKey());

			numbers = projectNumbersAes256Encryption.decrypt(projectNumbers
					.getProjectAmount());
		}

		// Need to Encrypt Invoice summary we are encrypting TOTAL AMOUNT,SENT
		// AMOUNT,RECEIVED AMOUNT,PENDING AMOUNT//

		String saltKey = KeyGenerators.string().generateKey();
		AES256Encryption invoicesummaryAes256Encryption = new AES256Encryption(
				String.valueOf(project.getId()), saltKey);

		invoiceSummary
				.setTotalAmount((numbers != null) ? invoicesummaryAes256Encryption
						.encrypt(numbers.toString())
						: invoicesummaryAes256Encryption.encrypt("0"));

		// here we are using getDecryptedInvoiceAmount method to decrypt and get
		// invoice amount
		String invoiceAmount = getDecryptedInvoiceAmount(project.getType()
				.toString(), invoice);

		// Invoice summary have set of sentInvoices
		Set<SentInvoiceSummary> invoiceSentInvoiceList = null;
		SentInvoiceSummary summary = null;
		if (invoice != null) {
			String invoiceStatus = invoice.getInvoiceStatus();
			if (invoiceStatus.equalsIgnoreCase("SENT")) {
				// Creating new set of invoices sent and new sentInvoiceSummary
				// object//
				invoiceSentInvoiceList = new HashSet<SentInvoiceSummary>();
				summary = new SentInvoiceSummary();

				// Setting sent invoice id to summary object and adding that
				// object to set and adding set to invoice summary
				summary.setSentInvoiceId(invoice.getId());
				invoiceSentInvoiceList.add(summary);
				invoiceSummary.setSentInvoices(invoiceSentInvoiceList);

				invoiceSummary.setSentAmount(invoicesummaryAes256Encryption
						.encrypt(invoiceAmount));

				invoiceSummary.setCurrency(invoice.getRemittance()
						.getCurrencyType());
			}
		}
		invoiceSummary.setReceivedAmount(invoicesummaryAes256Encryption
				.encrypt("0"));

		// Here removing sent amount from total project amount and saving it to
		// pending amount

		// here numbers will be not null only for fixed bid invoices
		if (numbers != null) {

			Long pendingAmount = Math.round(Double.valueOf(numbers)
					- Double.valueOf(invoiceAmount));

			/*
			 * Long pendingAmount = Long.valueOf(numbers) -
			 * Long.valueOf(invoiceAmount);
			 */

			invoiceSummary.setPendingAmount(invoicesummaryAes256Encryption
					.encrypt(pendingAmount.toString()));
		}

		invoiceSummary.setSaltKey(saltKey);

		invoiceDao.save(invoiceSummary);

	}

	public void updateInvoiceSummary(InvoiceSummary invoiceSummary,
			Invoice invoice, Invoice clonedInvoice) {

		AES256Encryption invoiceSummaryAes256Encryption = new AES256Encryption(
				String.valueOf(invoiceSummary.getProject().getId()),
				invoiceSummary.getSaltKey());

		String decryptedInvoiceSummarySentAmount = invoiceSummaryAes256Encryption
				.decrypt(invoiceSummary.getSentAmount());

		String decryptedInvoiceSummaryReceivedAmount = invoiceSummaryAes256Encryption
				.decrypt(invoiceSummary.getReceivedAmount());

		String decryptedInvoiceSummaryPendingAmount = invoiceSummaryAes256Encryption
				.decrypt(invoiceSummary.getPendingAmount());

		String projectType = invoiceSummary.getProject().getType().toString();

		AES256Encryption invoiceaes256Encryption = new AES256Encryption(
				String.valueOf(invoice.getId()), invoice.getSaltKey());

		String invoiceAmount = getDecryptedInvoiceAmount(projectType, invoice);

		String invoiceReceivedAmount = invoiceaes256Encryption.decrypt(invoice
				.getReceivedAmount());

		String InvoiceBaseAmount = null;

		if (projectType.equalsIgnoreCase("RETAINER") && projectType.equalsIgnoreCase("SUPPORT")) {
			InvoiceBaseAmount = invoiceaes256Encryption.decrypt(invoice
					.getSubTotal());
		} else {
			InvoiceBaseAmount = invoiceaes256Encryption.decrypt(invoice
					.getAmount());
		}

		String invoiceTotalAmount = invoiceaes256Encryption.decrypt(invoice
				.getTotalAmount());

		// while updating invoice if status is sent
		if (invoice.getInvoiceStatus().equalsIgnoreCase("SENT")) {

			// getting all sent invoices of invoice summary//
			Set<SentInvoiceSummary> sentInvoices = invoiceSummary
					.getSentInvoices();

			// creating invoice id list from sent invoice list
			Set<Long> invoiceidList = new HashSet<Long>();

			for (SentInvoiceSummary invoiceSummary2 : sentInvoices) {
				invoiceidList.add(invoiceSummary2.getSentInvoiceId());
			}

			// here if invoice we are editing is already in sentinvoicesummary
			// list
			if (invoiceidList.contains(invoice.getId())) {

				// then decrypt cloned invoice object this is object is
				// retrevied from invoice service impl
				String clonedInvoiceAmount = getDecryptedInvoiceAmount(
						projectType, clonedInvoice);

				// remove cloned invoice objects amount from invoice summary
				// and update newly updating invoice amount
				Long cloneAmountRemoved = Long
						.valueOf(decryptedInvoiceSummarySentAmount)
						- Long.valueOf(clonedInvoiceAmount);

				Long newSentAmount = cloneAmountRemoved
						+ Long.valueOf(invoiceAmount);

				// set to invoice summary amount and update invoice summary
				invoiceSummary.setSentAmount(invoiceSummaryAes256Encryption
						.encrypt(newSentAmount.toString()));

				// add cloned invoice object's amount pending amount and then
				// remove updating invoice amount

				Long updatingpendingAmount = 0L;
				if ((!projectType.equalsIgnoreCase("Retainer") ) && (!projectType.equalsIgnoreCase("SUPPORT"))) {
					Long CloneAmountAddedPendingAmount = Long
							.valueOf(decryptedInvoiceSummaryPendingAmount)
							+ Long.valueOf(clonedInvoiceAmount);

					updatingpendingAmount = CloneAmountAddedPendingAmount
							- Long.valueOf(invoiceAmount);
				}

				// set updated pending amount to invoice summary
				invoiceSummary
						.setPendingAmount((projectType
								.equalsIgnoreCase("Retainer") && projectType.equalsIgnoreCase("SUPPORT")) ? invoiceSummaryAes256Encryption
								.encrypt("0") : invoiceSummaryAes256Encryption
								.encrypt(updatingpendingAmount.toString()));

				invoiceDao.save(invoiceSummary);

			} else {
				// creating new summary object and adding to already existing
				// sentinvoicesummary list
				SentInvoiceSummary summary = new SentInvoiceSummary();
				summary.setSentInvoiceId(invoice.getId());
				sentInvoices.add(summary);
				invoiceSummary.setSentInvoices(sentInvoices);

				Long updatedSentAmount = Long
						.valueOf(decryptedInvoiceSummarySentAmount)
						+ Long.valueOf(invoiceAmount);

				invoiceSummary.setSentAmount(invoiceSummaryAes256Encryption
						.encrypt(updatedSentAmount.toString()));

				// for retainer any how decryoting pending amount will be 0 and
				// loop wont work
				Long pendingAmount = 0L;
				if (!projectType.equalsIgnoreCase("Retainer") && !projectType.equalsIgnoreCase("SUPPORT")) {
					// calculating pending amount and setting it to invoice
					// summary
					pendingAmount = Long
							.valueOf(decryptedInvoiceSummaryPendingAmount)
							- Long.valueOf(invoiceAmount);

				} else {
					pendingAmount = 0L;
				}

				invoiceSummary.setPendingAmount(invoiceSummaryAes256Encryption
						.encrypt(pendingAmount.toString()));

				invoiceDao.save(invoiceSummary);
			}

		} else if (invoice.getInvoiceStatus().equalsIgnoreCase(
				"PARTIALLY RECEIVED")
				|| invoice.getInvoiceStatus().equalsIgnoreCase("RECEIVED")) {

			// calculating partially received amount with out tax and discount

			Double doubleInvoiceReceivedAmount = Double
					.valueOf(invoiceReceivedAmount);
			Double doubleInvoiceBaseAmount = Double.valueOf(InvoiceBaseAmount);
			Double doubleInvoiceTotalAmount = Double
					.valueOf(invoiceTotalAmount);

			Double reversedValue = (doubleInvoiceReceivedAmount * doubleInvoiceBaseAmount)
					/ doubleInvoiceTotalAmount;

			Long roundedReverseValue = (long) Math.round(Double
					.valueOf(reversedValue));

			Set<Long> receivedInvoiceIdList = new HashSet<Long>();
			for (ReceivedInvoiceSummary summary : invoiceSummary
					.getReceivedinvoices()) {
				receivedInvoiceIdList.add(summary.getReceivedInvoiceId());
			}
			if (receivedInvoiceIdList.contains(invoice.getId())) {

				Long updatedReceivedInvoiceSummaryAmount = (Long
						.valueOf(decryptedInvoiceSummaryReceivedAmount) + Long
						.valueOf(roundedReverseValue));

				invoiceSummary
						.setReceivedAmount(invoiceSummaryAes256Encryption
								.encrypt(updatedReceivedInvoiceSummaryAmount
										.toString()));

				invoiceDao.save(invoiceSummary);

			} else {

				// Set<ReceivedInvoiceSummary> receivedInvoiceList = new
				// HashSet<ReceivedInvoiceSummary>();
				// create new received invoice summary object and set updating
				// invoice
				// id
				ReceivedInvoiceSummary receivedInvoiceSummary = new ReceivedInvoiceSummary();
				receivedInvoiceSummary.setReceivedInvoiceId(invoice.getId());
				// add received invoice summary object to set
				// receivedInvoiceList.add(receivedInvoiceSummary);
				invoiceSummary.getReceivedinvoices()
						.add(receivedInvoiceSummary);

				Long updatedReceivedInvoiceSummaryAmount = (Long
						.valueOf(decryptedInvoiceSummaryReceivedAmount) + Long
						.valueOf(roundedReverseValue));

				invoiceSummary
						.setReceivedAmount(invoiceSummaryAes256Encryption
								.encrypt(updatedReceivedInvoiceSummaryAmount
										.toString()));

				invoiceDao.save(invoiceSummary);

			}

		} else if (invoice.getInvoiceStatus().equalsIgnoreCase("WRITE OFF")) {

			AES256Encryption clonedaes256Encryption = new AES256Encryption(
					String.valueOf(clonedInvoice.getId()),
					clonedInvoice.getSaltKey());

			String oldwriteOffAmount = clonedaes256Encryption
					.decrypt(clonedInvoice.getWriteoffAmount());

			Double invoiceWriteOffAmount = Double
					.valueOf(invoiceaes256Encryption.decrypt(invoice
							.getWriteoffAmount()));

			Double invoiceBaseAmount = Double.valueOf(InvoiceBaseAmount);
			Double InvoiceTotalAmount = Double.valueOf(invoiceTotalAmount);

			Long reversedValue = (long) Math
					.round(((invoiceWriteOffAmount * invoiceBaseAmount) / InvoiceTotalAmount));

			if (oldwriteOffAmount == null || oldwriteOffAmount == "") {
				Long newReceivedAmount = Long
						.valueOf(decryptedInvoiceSummaryPendingAmount)
						+ reversedValue;

				invoiceSummary.setPendingAmount(invoiceSummaryAes256Encryption
						.encrypt(newReceivedAmount.toString()));
				invoiceDao.saveOrUpdate(invoiceSummary);
			} else {

				String oldInvoiceAmount = getDecryptedInvoiceAmount(
						projectType, clonedInvoice);
				Double doubleoldInvoiceAmount = Double
						.valueOf(oldInvoiceAmount);
				Double oldInvoiceTotalAmount = Double
						.valueOf(clonedaes256Encryption.decrypt(clonedInvoice
								.getTotalAmount()));
				Double oldWriteOffAmount = Double
						.valueOf(clonedaes256Encryption.decrypt(clonedInvoice
								.getWriteoffAmount()));

				Long roundedValue = (long) ((oldWriteOffAmount * doubleoldInvoiceAmount) / oldInvoiceTotalAmount);

				Long removedWriteOffAmount = 0L;
				if (!projectType.equalsIgnoreCase("retainer") && !projectType.equalsIgnoreCase("SUPPORT")) {
					removedWriteOffAmount = Long
							.valueOf(decryptedInvoiceSummaryPendingAmount)
							- roundedValue;
				}
				Long newlyAddedWriteOffAmount = removedWriteOffAmount
						+ reversedValue;
				invoiceSummary.setPendingAmount(invoiceSummaryAes256Encryption
						.encrypt(newlyAddedWriteOffAmount.toString()));

				invoiceDao.saveOrUpdate(invoiceSummary);

			}
		}

	}

	public void deleteInvoiceAmountFromSummary(Invoice invoice,
			Milestone milestone) {

		if (milestone != null) {
			Project project = milestone.getProject();
			com.raybiztech.projectmanagement.business.Client client = project
					.getClient();
			ChangeRequest changeRequest = milestone.getChangeRequest();

			InvoiceSummary invoiceSummary = (changeRequest != null) ? invoiceDao
					.getSummaryOfChangeRequest(project, client, changeRequest)
					: invoiceDao.getInvoiceSummary(project, client);

			// see getDecryptedInvoiceAmount method
			String invoiceAmount = getDecryptedInvoiceAmount(project.getType()
					.toString(), invoice);

			Set<SentInvoiceSummary> sentInvoicesList = invoiceSummary
					.getSentInvoices();

			AES256Encryption invoicesummaryAes256Encryption = new AES256Encryption(
					String.valueOf(invoiceSummary.getProject().getId()),
					invoiceSummary.getSaltKey());

			String decryptedInvoiceSummarySentAmount = invoicesummaryAes256Encryption
					.decrypt(invoiceSummary.getSentAmount());

			String decryptedInvoiceSummaryPendingAmount = invoicesummaryAes256Encryption
					.decrypt(invoiceSummary.getPendingAmount());

			// removing deleting invoice amount from summary//
			Long removedAmount = Long
					.valueOf(decryptedInvoiceSummarySentAmount)
					- Long.valueOf(invoiceAmount);

			// removing deleting invoice from sent invoice summary now here we
			// may get deleting id two

			List<SentInvoiceSummary> deletingInvoiceIdinSummary = invoiceDao
					.getAllOfProperty(SentInvoiceSummary.class,
							"sentInvoiceId", invoice.getId());
			for (SentInvoiceSummary deletingSentInvoice : deletingInvoiceIdinSummary) {
				sentInvoicesList.remove(deletingSentInvoice);
			}

			// setting amount after removal to invoice summary
			invoiceSummary.setSentAmount(invoicesummaryAes256Encryption
					.encrypt(removedAmount.toString()));

			String projectType = project.getType().toString();

			if (!projectType.equalsIgnoreCase("Retainer") && !projectType.equalsIgnoreCase("SUPPORT")) {
				// adding deleting invoice amount to pending amount
				Long updatedPendingAmount = Long
						.valueOf(decryptedInvoiceSummaryPendingAmount)
						+ Long.valueOf(invoiceAmount);

				// setting it to pending amount
				invoiceSummary.setPendingAmount(invoicesummaryAes256Encryption
						.encrypt(updatedPendingAmount.toString()));
			}

			// here while calculating INVOICE AMOUNT from invoice summary we are
			// checking whether INVOICE SUMMARY SENT AMOUNT is 0 if summary sent
			// amount is zero then delete invoice summary else update invoice
			// summary
			if (removedAmount == 0) {
				invoiceDao.delete(invoiceSummary);

			} else {
				invoiceDao.save(invoiceSummary);
			}

		}

	}

	public void deletepartiallyReceivedInvoiceAmountFromSummary(
			ReceivedInvoiceAmount amount, Invoice invoice) {

		if (invoice != null) {
			Milestone milestone = invoice.getMilsestone();
			Project project = milestone.getProject();
			Client client = project.getClient();
			ChangeRequest changeRequest = milestone.getChangeRequest();

			InvoiceSummary invoiceSummary = (changeRequest != null) ? invoiceDao
					.getSummaryOfChangeRequest(project, client, changeRequest)
					: invoiceDao.getInvoiceSummary(project, client);

			AES256Encryption invoicesummaryAes256Encryption = new AES256Encryption(
					String.valueOf(invoiceSummary.getProject().getId()),
					invoiceSummary.getSaltKey());

			String decryptedInvoiceSummaryReceivedAmount = invoicesummaryAes256Encryption
					.decrypt(invoiceSummary.getReceivedAmount());

			AES256Encryption aes256Encryption = new AES256Encryption(
					String.valueOf(invoice.getId()), invoice.getSaltKey());

			String InvoiceBaseAmount = null;
			if (project.getType().toString().equalsIgnoreCase("Retainer") && project.getType().toString().equalsIgnoreCase("SUPPORT")) {
				InvoiceBaseAmount = aes256Encryption.decrypt(invoice
						.getSubTotal());
			} else {
				InvoiceBaseAmount = aes256Encryption.decrypt(invoice
						.getAmount());
			}

			String invoiceTotalAmount = aes256Encryption.decrypt(invoice
					.getTotalAmount());

			// calculating partially received amount with out tax and discount
			Double doubleInvoiceBaseAmount = Double.valueOf(InvoiceBaseAmount);
			Double doubleInvoiceTotalAmount = Double
					.valueOf(invoiceTotalAmount);

			AES256Encryption amountEncryption = new AES256Encryption(
					String.valueOf(invoice.getId()), amount.getSaltkey());

			Double deletingPartialAmount = Double.valueOf(amountEncryption
					.decrypt(amount.getReceivedAmount()));

			Double reversedValue = (deletingPartialAmount * doubleInvoiceBaseAmount)
					/ doubleInvoiceTotalAmount;

			Long roundedReverseValue = (long) Math.round(Double
					.valueOf(reversedValue));

			Long removedPartialReceivedAmount = Long
					.valueOf(decryptedInvoiceSummaryReceivedAmount)
					- roundedReverseValue;

			invoiceSummary.setReceivedAmount(invoicesummaryAes256Encryption
					.encrypt(removedPartialReceivedAmount.toString()));
			invoiceDao.update(invoiceSummary);

		}

	}

	public List<InvoiceSummaryDTO> covertInvoiceSummaryToDTO(
			List<InvoiceSummary> invoiceSummaries) {

		List<InvoiceSummaryDTO> invoiceSummaryDTOs = null;
		InvoiceSummaryDTO dto = null;
		if (invoiceSummaries != null) {
			invoiceSummaryDTOs = new ArrayList<InvoiceSummaryDTO>();
			for (InvoiceSummary summary : invoiceSummaries) {

				dto = new InvoiceSummaryDTO();

				AES256Encryption aes256Encryption = new AES256Encryption(
						String.valueOf(summary.getProject().getId()),
						summary.getSaltKey());

				dto.setId(summary.getId());

				dto.setClientName(summary.getClient().getName());

				dto.setProjectName(summary.getProject().getProjectName());

				dto.setProjectType(summary.getProject().getType().toString());

				dto.setCrName((summary.getChangeRequest() != null) ? summary
						.getChangeRequest().getName() : null);

				dto.setTotalAmount(aes256Encryption.decrypt(summary
						.getTotalAmount()));

				dto.setSentAmount(aes256Encryption.decrypt(summary
						.getSentAmount()));

				dto.setSentInvoiceCount(new Long(summary.getSentInvoices()
						.size()));

				dto.setReceivedAmount(aes256Encryption.decrypt(summary
						.getReceivedAmount()));

				dto.setReceivedInvoiceCount(new Long(summary
						.getReceivedinvoices().size()));

				dto.setPendingAmount((summary.getPendingAmount() != null) ? aes256Encryption
						.decrypt(summary.getPendingAmount()) : "0");

				dto.setCurrency(summary.getCurrency());

				dto.setSaltKey(summary.getSaltKey());

				invoiceSummaryDTOs.add(dto);
			}
		}

		return invoiceSummaryDTOs;

	}

	public List<InvoiceDTO> getInvoices(Set<Long> invoiceIds) {

		List<InvoiceDTO> invoiceList = null;
		if (invoiceIds != null) {
			invoiceList = new ArrayList<InvoiceDTO>();
			for (Long invoiceid : invoiceIds) {
				if (invoiceid != null) {
					invoiceList.add(invoiceBuilder.entityToDto(invoiceDao
							.findBy(Invoice.class, invoiceid)));
				}
			}
		}
		return invoiceList;

	}

	public String getDecryptedInvoiceAmount(String projectType, Invoice invoice) {

		AES256Encryption aes256Encryption = new AES256Encryption(
				String.valueOf(invoice.getId()), invoice.getSaltKey());

		// For fixed bid invoice amount is set to amount varaible where as for
		// RETAINER it is set to subtotal see invoice business and json you will
		// get idea
		String invoiceAmount = (projectType.equalsIgnoreCase("Retainer") && projectType.equalsIgnoreCase("SUPPORT")) ? aes256Encryption
				.decrypt(invoice.getSubTotal()) : aes256Encryption
				.decrypt(invoice.getAmount());

		Long roundedInvoiceAmount = (long) Math.round(Double
				.valueOf(invoiceAmount));

		return roundedInvoiceAmount.toString();
	}

	public String getDecryptedInvoiceReceivedAmount(String projectType,
			Invoice invoice) {

		AES256Encryption aes256Encryption = new AES256Encryption(
				String.valueOf(invoice.getId()), invoice.getSaltKey());

		String invoiceReceivedAmount = aes256Encryption.decrypt(invoice
				.getReceivedAmount());

		Long roundedInvoiceAmount = (long) Math.round(Double
				.valueOf(invoiceReceivedAmount));

		return roundedInvoiceAmount.toString();
	}

	public void calculateSummary(List<Milestone> milestones, Project project,
			ChangeRequest changeRequest) {

		String projectType = project.getType().toString();

		ProjectNumbers projectNumbers = (changeRequest != null) ? invoiceDao
				.getAmountofCr(changeRequest) : invoiceDao
				.getProjectNumbers(project);
		String numbers = "0";
		if (projectNumbers != null) {
			AES256Encryption projectNumbersAes256Encryption = new AES256Encryption(
					String.valueOf(projectNumbers.getProject().getId()),
					projectNumbers.getSaltKey());
			numbers = projectNumbersAes256Encryption.decrypt(projectNumbers
					.getProjectAmount());
		}

		Long summaryReceivedAmount = 0L;
		Long overDueAmount = 0L;
		Long summarySentAmount = 0L;
		Long pendingAmount = 0L;

		Set<SentInvoiceSummary> sentinvoiceSummaries = new HashSet<SentInvoiceSummary>();
		Set<ReceivedInvoiceSummary> receivedInvoiceSummaries = new HashSet<ReceivedInvoiceSummary>();
		SentInvoiceSummary sentInvoice = null;
		ReceivedInvoiceSummary receivedInvoice = null;

		List<Invoice> invoiceList = null;
		for (Milestone milestone : milestones) {
			invoiceList = invoiceDao.getinvoiceForMilestone(milestone);

			logger.warn("Milestone " + milestone.getTitle() + " Have "
					+ invoiceList.size() + "Invoices");

			for (Invoice invoice : invoiceList) {
				String invoicestatus = invoice.getInvoiceStatus();
				AES256Encryption aes256Encryption = new AES256Encryption(
						String.valueOf(invoice.getId()), invoice.getSaltKey());

				String invoiceAmount = "0";
				if (!projectType.equalsIgnoreCase("RETAINER")) {
					invoiceAmount = aes256Encryption.decrypt(invoice
							.getAmount());
				} else if (projectType.equalsIgnoreCase("RETAINER") && projectType.equalsIgnoreCase("SUPPORT")) {
					invoiceAmount = aes256Encryption.decrypt(invoice
							.getSubTotal());
				}

				if (invoicestatus.equalsIgnoreCase("Received")) {
					logger.warn("Calculating Invoice Details whose status is RECEIVED");
					summaryReceivedAmount = summaryReceivedAmount
							+ (long) Math.round(Double.valueOf(invoiceAmount));

					receivedInvoice = new ReceivedInvoiceSummary();
					receivedInvoice.setReceivedInvoiceId(invoice.getId());
					receivedInvoiceSummaries.add(receivedInvoice);
					sentInvoice = new SentInvoiceSummary();
					sentInvoice.setSentInvoiceId(invoice.getId());
					sentinvoiceSummaries.add(sentInvoice);

				}
				if (invoicestatus.equalsIgnoreCase("OVER DUE")
						|| invoicestatus.equalsIgnoreCase("SENT")) {
					logger.warn("Calculating Invoice Details whose status is SET/OVERDUE");
					overDueAmount = overDueAmount
							+ (long) Math.round(Double.valueOf(invoiceAmount));
					sentInvoice = new SentInvoiceSummary();
					sentInvoice.setSentInvoiceId(invoice.getId());
					sentinvoiceSummaries.add(sentInvoice);
				}

			}

		}
		summarySentAmount = summaryReceivedAmount + overDueAmount;

		if (projectNumbers != null) {
			pendingAmount = Long.valueOf(numbers) - summarySentAmount;
		}

		logger.warn("Total Amount " + numbers);
		logger.warn("Over Due Amount " + overDueAmount);
		logger.warn("Summary Sent Amount " + summarySentAmount);
		logger.warn("Summary Received Amount " + summaryReceivedAmount);
		logger.warn("Pending Amount " + pendingAmount);

		if (summarySentAmount != 0) {
			String saltKey = KeyGenerators.string().generateKey();
			AES256Encryption invoicesummaryAes256Encryption = new AES256Encryption(
					String.valueOf(project.getId()), saltKey);

			InvoiceSummary invoiceSummary = new InvoiceSummary();
			invoiceSummary.setProject(project);
			invoiceSummary.setClient(project.getClient());
			invoiceSummary
					.setChangeRequest((changeRequest != null) ? changeRequest
							: null);

			invoiceSummary.setTotalAmount(invoicesummaryAes256Encryption
					.encrypt(numbers));

			invoiceSummary.setSentAmount(invoicesummaryAes256Encryption
					.encrypt(summarySentAmount.toString()));

			invoiceSummary.setReceivedAmount(invoicesummaryAes256Encryption
					.encrypt(summaryReceivedAmount.toString()));

			invoiceSummary.setPendingAmount(invoicesummaryAes256Encryption
					.encrypt(pendingAmount.toString()));

			invoiceSummary.setSentInvoices(sentinvoiceSummaries);

			invoiceSummary.setReceivedinvoices(receivedInvoiceSummaries);

			if (projectNumbers != null) {
				invoiceSummary.setCurrency(projectNumbers.getCurrency());
				logger.warn("Getting Currency from project numbers");
			} else if (invoiceList.size() != 0) {
				Invoice invoice = invoiceList.get(0);
				logger.warn("Getting Currency from invoice");
				invoiceSummary.setCurrency(invoice.getRemittance()
						.getCurrencyType());
			}
			invoiceSummary.setSaltKey(saltKey);

			logger.warn("----------saving invoice summary--------");
			invoiceDao.save(invoiceSummary);
		}

	}
	public static void main(String[] args) {
		System.out.println(Long.valueOf(null));
	}
}
