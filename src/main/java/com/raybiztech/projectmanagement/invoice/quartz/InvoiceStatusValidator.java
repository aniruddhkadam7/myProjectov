package com.raybiztech.projectmanagement.invoice.quartz;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.raybiztech.date.Date;
import com.raybiztech.projectmanagement.invoice.business.Invoice;
import com.raybiztech.projectmanagement.invoice.dao.InvoiceDao;

@Transactional
@Component("invoiceStatusValidator")
public class InvoiceStatusValidator {

	@Autowired
	InvoiceDao invoiceDao;

	Logger logger = Logger.getLogger(InvoiceStatusValidator.class);

	public void updateInvoiceStatus() {

		List<Invoice> invoiceList = invoiceDao.checkInvoiceStatus();
		logger.warn("Invoice List Size " + invoiceList.size());
		if (!invoiceList.isEmpty()) {
			for (Invoice invoice : invoiceList) {

				logger.warn("Invoice Number " + invoice.getNumber()
						+ " Todays Date " + new Date() + " Invoice Due Date "
						+ invoice.getDueDate() + " invoice status "
						+ invoice.getInvoiceStatus());

				invoice.setInvoiceStatus("OVER DUE");
				invoiceDao.saveOrUpdate(invoice);

			}
		}

	}
}
