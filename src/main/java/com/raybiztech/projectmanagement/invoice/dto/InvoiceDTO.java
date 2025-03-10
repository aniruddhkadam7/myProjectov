package com.raybiztech.projectmanagement.invoice.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.raybiztech.projectmanagement.dto.MilestoneDTO;
import com.raybiztech.projectmanagement.invoice.business.Invoice;

public class InvoiceDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5064761544215831040L;
	/**
     *
     */

	private Long id;
	private Long projectId;
	private String projectName;
	private String projectType;
	private Long clientId;
	private String client;
	private String clientContactPerson;
	private String number;
	private String referenceNumber;
	private String pattern;
	private String notes;
	private Boolean showNotesOnInvoice;
	private String subTotal;
	private String amount;
	private String totalAmount;
	private String paymentTerm;
	private String invoiceDate;
	private String dueDate;
	private String invoiceAmountReceviedDate;
	private String invoiceStatus;
	private RemittanceDTO remittance;
	private Set<LineItemDTO> lineitem;
	private Long milestoneId;
	private String discountType;
	private Set<MilestoneDTO> milestoneDTOs;
	private String discountRate;
	private String discount;
	private String taxType;
	private String taxRate;
	private String tax;
	private String country;
	private String mileStoneName;
	private String milestonePercentage;
	private String companyAdress;
	private String currencyType;
	private String numberInWords;
	private String organization;
	private Set<TaxDTO> taxDTO;
	private Boolean genarateType;
	private String eachDay;
	private String invoiceDuration;
	private String invoiceCount;
	private String invoiceRate;
	private List<String> roleAndNameCount;
	private List<String> resourcesName;
	private String countTypeToDisplay;
	private String amountAfterDiscount;
	private String percentage;
	private String invoiceAmountSentDate;
	private String poNumber;
	private String statusNotes;
	private String writeoffAmount;
	private String receivedAmount;
	private String totalReceivedAmount;
	private Set<ReceivedInvoiceAmountDTO> receivedAmountList;
	private Boolean invoiceUpdatable;
	private Boolean invoiceMutable;
	private List<String> invoiceStatusList;
	private Boolean isAdjusted;
	private Boolean canBeAdjusted;
	private Boolean showCanBeAdjusted;
	private Boolean showRestrictAdjusting;
	private Boolean showTaxDetailsOnInvoice;
	private String companyAddress;
	private String  companyName;
	private String conversionRate;
	private String gstCode;
	private Boolean invoiceNumberFlag;

	private Long crid;
	private String crduration;
	private String billingContactPerson;
	private String billingContactPersonEmail;
	private String invoiceNumber;
	private String invoicePattern;
	private String invoiceReferenceNumber;
	private String tdsAmount;
	private String netAmount;
	private String clientCountry;
	private String modifiedMilestoneName;
	private String milestoneTypeFlag;
	private Boolean proformaInvoiceFlag = false;
	private String proformaReferenceNo;
	//private Boolean invoiceReopenFlag;
	

	public String getProformaReferenceNo() {
		return proformaReferenceNo;
	}

	public void setProformaReferenceNo(String proformaReferenceNo) {
		this.proformaReferenceNo = proformaReferenceNo;
	}

	public Boolean getProformaInvoiceFlag() {
		return proformaInvoiceFlag;
	}

	public void setProformaInvoiceFlag(Boolean proformaInvoiceFlag) {
		this.proformaInvoiceFlag = proformaInvoiceFlag;
	}

	public String getMilestonePercentage() {
		return milestonePercentage;
	}

	public void setMilestonePercentage(String milestonePercentage) {
		this.milestonePercentage = milestonePercentage;
	}

	public String getAmountAfterDiscount() {
		return amountAfterDiscount;
	}

	public void setAmountAfterDiscount(String amountAfterDiscount) {
		this.amountAfterDiscount = amountAfterDiscount;
	}

	public String getCompanyAdress() {
		return companyAdress;
	}

	public void setCompanyAdress(String companyAdress) {
		this.companyAdress = companyAdress;
	}

	public String getCurrencyType() {
		return currencyType;
	}

	public void setCurrencyType(String currencyType) {
		this.currencyType = currencyType;
	}

	public String getNumberInWords() {
		return numberInWords;
	}

	public void setNumberInWords(String numberInWords) {
		this.numberInWords = numberInWords;
	}

	public String getOrganization() {
		return organization;
	}

	public void setOrganization(String organization) {
		this.organization = organization;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	private DiscountDTO discountDTO;

	public DiscountDTO getDiscountDTO() {
		return discountDTO;
	}

	public void setDiscountDTO(DiscountDTO discountDTO) {
		this.discountDTO = discountDTO;
	}

	public String getDiscount() {
		return discount;
	}

	public String getTax() {
		return tax;
	}

	public RemittanceDTO getRemittance() {
		return remittance;
	}

	public void setRemittance(RemittanceDTO remittance) {
		this.remittance = remittance;
	}

	public Set<LineItemDTO> getLineitem() {
		return lineitem;
	}

	public void setLineitem(Set<LineItemDTO> lineitem) {
		this.lineitem = lineitem;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getSubTotal() {
		return subTotal;
	}

	public void setSubTotal(String subTotal) {
		this.subTotal = subTotal;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getPaymentTerm() {
		return paymentTerm;
	}

	public void setPaymentTerm(String paymentTerm) {
		this.paymentTerm = paymentTerm;
	}

	public String getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(String invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	public String getDueDate() {
		return dueDate;
	}

	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}

	public String getInvoiceStatus() {
		return invoiceStatus;
	}

	public void setInvoiceStatus(String invoiceStatus) {
		this.invoiceStatus = invoiceStatus;
	}

	public Long getMilestoneId() {
		return milestoneId;
	}

	public void setMilestoneId(Long milestoneId) {
		this.milestoneId = milestoneId;
	}

	public Long getProjectId() {
		return projectId;
	}

	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getClient() {
		return client;
	}

	public void setClient(String client) {
		this.client = client;
	}

	public Set<MilestoneDTO> getMilestoneDTOs() {
		return milestoneDTOs;
	}

	public void setMilestoneDTOs(Set<MilestoneDTO> milestoneDTOs) {
		this.milestoneDTOs = milestoneDTOs;
	}

	public String getDiscountType() {
		return discountType;
	}

	public void setDiscountType(String discountType) {
		this.discountType = discountType;
	}

	public String getDiscountRate() {
		return discountRate;
	}

	public void setDiscountRate(String discountRate) {
		this.discountRate = discountRate;
	}

	public void setDiscount(String discount) {
		this.discount = discount;
	}

	public String getTaxType() {
		return taxType;
	}

	public void setTaxType(String taxType) {
		this.taxType = taxType;
	}

	public String getTaxRate() {
		return taxRate;
	}

	public void setTaxRate(String taxRate) {
		this.taxRate = taxRate;
	}

	public void setTax(String tax) {
		this.tax = tax;
	}

	public String getMileStoneName() {
		return mileStoneName;
	}

	public void setMileStoneName(String mileStoneName) {
		this.mileStoneName = mileStoneName;
	}

	public Set<TaxDTO> getTaxDTO() {
		return taxDTO;
	}

	public void setTaxDTO(Set<TaxDTO> taxDTO) {
		this.taxDTO = taxDTO;
	}

	public Boolean getGenarateType() {
		return genarateType;
	}

	public void setGenarateType(Boolean genarateType) {
		this.genarateType = genarateType;
	}

	public String getEachDay() {
		return eachDay;
	}

	public void setEachDay(String eachDay) {
		this.eachDay = eachDay;
	}

	public String getInvoiceDuration() {
		return invoiceDuration;
	}

	public void setInvoiceDuration(String invoiceDuration) {
		this.invoiceDuration = invoiceDuration;
	}

	public String getInvoiceCount() {
		return invoiceCount;
	}

	public void setInvoiceCount(String invoiceCount) {
		this.invoiceCount = invoiceCount;
	}

	public String getInvoiceRate() {
		return invoiceRate;
	}

	public void setInvoiceRate(String invoiceRate) {
		this.invoiceRate = invoiceRate;
	}

	public String getInvoiceAmountReceviedDate() {
		return invoiceAmountReceviedDate;
	}

	public void setInvoiceAmountReceviedDate(String invoiceAmountReceviedDate) {
		this.invoiceAmountReceviedDate = invoiceAmountReceviedDate;
	}

	public String getTotalReceivedAmount() {
		return totalReceivedAmount;
	}

	public void setTotalReceivedAmount(String totalReceivedAmount) {
		this.totalReceivedAmount = totalReceivedAmount;
	}

	public List<String> getRoleAndNameCount() {

		Map<String, List<String>> empDisMap = new HashMap<String, List<String>>();

		if (lineitem != null) {

			for (LineItemDTO ldto : lineitem) {

				if (empDisMap.get(ldto.getRole()) == null) {
					List<String> tlist = new ArrayList<String>();
					tlist.add(ldto.getEmpName());
					empDisMap.put(ldto.getRole(), tlist);
				} else {
					empDisMap.get(ldto.getRole()).add(ldto.getEmpName());

				}
			}
		}
		roleAndNameCount = new ArrayList<String>();

		for (Entry<String, List<String>> entry : empDisMap.entrySet()) {

			roleAndNameCount
					.add(entry.getValue().size() + "X" + entry.getKey());

		}

		return roleAndNameCount;
	}

	public void setRoleAndNameCount(List<String> roleAndNameCount) {
		this.roleAndNameCount = roleAndNameCount;
	}

	public List<String> getResourcesName() {

		return resourcesName;
	}

	public void setResourcesName(List<String> resourcesName) {
		this.resourcesName = resourcesName;
	}

	public String getCountTypeToDisplay() {
		return countTypeToDisplay;
	}

	public void setCountTypeToDisplay(String countTypeToDisplay) {
		this.countTypeToDisplay = countTypeToDisplay;
	}

	public String getPercentage() {
		return percentage;
	}

	public void setPercentage(String percentage) {
		this.percentage = percentage;
	}

	public String getProjectType() {
		return projectType;
	}

	public void setProjectType(String projectType) {
		this.projectType = projectType;
	}

	public String getInvoiceAmountSentDate() {
		return invoiceAmountSentDate;
	}

	public void setInvoiceAmountSentDate(String invoiceAmountSentDate) {
		this.invoiceAmountSentDate = invoiceAmountSentDate;
	}

	public Long getCrid() {
		return crid;
	}

	public void setCrid(Long crid) {
		this.crid = crid;
	}

	public String getCrduration() {
		return crduration;
	}

	public void setCrduration(String crduration) {
		this.crduration = crduration;
	}

	public String getStatusNotes() {
		return statusNotes;
	}

	public void setStatusNotes(String statusNotes) {
		this.statusNotes = statusNotes;
	}

	public String getClientContactPerson() {
		return clientContactPerson;
	}

	public void setClientContactPerson(String clientContactPerson) {
		this.clientContactPerson = clientContactPerson;
	}

	public String getPoNumber() {
		return poNumber;
	}

	public void setPoNumber(String poNumber) {
		this.poNumber = poNumber;
	}

	public String getWriteoffAmount() {
		return writeoffAmount;
	}

	public void setWriteoffAmount(String writeoffAmount) {
		this.writeoffAmount = writeoffAmount;
	}

	public Set<ReceivedInvoiceAmountDTO> getReceivedAmountList() {
		return receivedAmountList;
	}

	public void setReceivedAmountList(
			Set<ReceivedInvoiceAmountDTO> receivedAmountList) {
		this.receivedAmountList = receivedAmountList;
	}

	public String getReceivedAmount() {
		return receivedAmount;
	}

	public void setReceivedAmount(String receivedAmount) {
		this.receivedAmount = receivedAmount;
	}

	public Boolean getInvoiceUpdatable() {
		return invoiceUpdatable;
	}

	public void setInvoiceUpdatable(Boolean invoiceUpdatable) {
		this.invoiceUpdatable = invoiceUpdatable;
	}

	public Boolean getShowNotesOnInvoice() {
		return showNotesOnInvoice;
	}

	public void setShowNotesOnInvoice(Boolean showNotesOnInvoice) {
		this.showNotesOnInvoice = showNotesOnInvoice;
	}

	public List<String> getInvoiceStatusList() {
		return invoiceStatusList;
	}

	public void setInvoiceStatusList(List<String> invoiceStatusList) {
		this.invoiceStatusList = invoiceStatusList;
	}

	public Boolean getInvoiceMutable() {
		return invoiceMutable;
	}

	public void setInvoiceMutable(Boolean invoiceMutable) {
		this.invoiceMutable = invoiceMutable;
	}

	public String getReferenceNumber() {
		return referenceNumber;
	}

	public void setReferenceNumber(String referenceNumber) {
		this.referenceNumber = referenceNumber;
	}

	public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	public Long getClientId() {
		return clientId;
	}

	public void setClientId(Long clientId) {
		this.clientId = clientId;
	}

	public Boolean getIsAdjusted() {
		return isAdjusted;
	}

	public void setIsAdjusted(Boolean isAdjusted) {
		this.isAdjusted = isAdjusted;
	}

	public Boolean getCanBeAdjusted() {
		return canBeAdjusted;
	}

	public void setCanBeAdjusted(Boolean canBeAdjusted) {
		this.canBeAdjusted = canBeAdjusted;
	}

	public Boolean getShowCanBeAdjusted() {
		return showCanBeAdjusted;
	}

	public void setShowCanBeAdjusted(Boolean showCanBeAdjusted) {
		this.showCanBeAdjusted = showCanBeAdjusted;
	}

	public Boolean getShowRestrictAdjusting() {
		return showRestrictAdjusting;
	}

	public void setShowRestrictAdjusting(Boolean showRestrictAdjusting) {
		this.showRestrictAdjusting = showRestrictAdjusting;
	}

	public Boolean getShowTaxDetailsOnInvoice() {
		return showTaxDetailsOnInvoice;
	}

	public void setShowTaxDetailsOnInvoice(Boolean showTaxDetailsOnInvoice) {
		this.showTaxDetailsOnInvoice = showTaxDetailsOnInvoice;
	}

	public String getCompanyAddress() {
		return companyAddress;
	}

	public void setCompanyAddress(String companyAddress) {
		this.companyAddress = companyAddress;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getConversionRate() {
		return conversionRate;
	}

	public void setConversionRate(String conversionRate) {
		this.conversionRate = conversionRate;
	}

	public String getGstCode() {
		return gstCode;
	}

	public void setGstCode(String gstCode) {
		this.gstCode = gstCode;
	}

	public String getBillingContactPerson() {
		return billingContactPerson;
	}

	public void setBillingContactPerson(String billingContactPerson) {
		this.billingContactPerson = billingContactPerson;
	}

	public String getBillingContactPersonEmail() {
		return billingContactPersonEmail;
	}

	public void setBillingContactPersonEmail(String billingContactPersonEmail) {
		this.billingContactPersonEmail = billingContactPersonEmail;
	}

	public Boolean getInvoiceNumberFlag() {
		return invoiceNumberFlag;
	}

	public void setInvoiceNumberFlag(Boolean invoiceNumberFlag) {
		this.invoiceNumberFlag = invoiceNumberFlag;
	}

	public String getInvoiceNumber() {
		return invoiceNumber;
	}

	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}

	public String getInvoicePattern() {
		return invoicePattern;
	}

	public void setInvoicePattern(String invoicePattern) {
		this.invoicePattern = invoicePattern;
	}

	public String getInvoiceReferenceNumber() {
		return invoiceReferenceNumber;
	}

	public void setInvoiceReferenceNumber(String invoiceReferenceNumber) {
		this.invoiceReferenceNumber = invoiceReferenceNumber;
	}

	public String getTdsAmount() {
		return tdsAmount;
	}

	public void setTdsAmount(String tdsAmount) {
		this.tdsAmount = tdsAmount;
	}

	public String getNetAmount() {
		return netAmount;
	}

	public void setNetAmount(String netAmount) {
		this.netAmount = netAmount;
	}

	public String getClientCountry() {
		return clientCountry;
	}

	public void setClientCountry(String clientCountry) {
		this.clientCountry = clientCountry;
	}

	public String getModifiedMilestoneName() {
		return modifiedMilestoneName;
	}

	public void setModifiedMilestoneName(String modifiedMilestoneName) {
		this.modifiedMilestoneName = modifiedMilestoneName;
	}


	public String getMilestoneTypeFlag() {
		return milestoneTypeFlag;
	}

	public void setMilestoneTypeFlag(String milestoneTypeFlag) {
		this.milestoneTypeFlag = milestoneTypeFlag;
	}
	
	
	
	
	
	
	

	/*public Boolean getInvoiceReopenFlag() {
		return invoiceReopenFlag;
	}

	public void setInvoiceReopenFlag(Boolean invoiceReopenFlag) {
		this.invoiceReopenFlag = invoiceReopenFlag;
	}
	*/
	
	
	

	
	
	

}
