package com.raybiztech.projectmanagement.invoice.utility;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.raybiztech.appraisals.properties.PropBean;
import com.raybiztech.projectmanagement.business.ProjectType;
import com.raybiztech.projectmanagement.invoice.dto.InvoiceDTO;

@Component("fixedBidInvoice")
public class GenerateInvoice {

	@Autowired
	PropBean propBean;
	static Logger longger = Logger.getLogger(GenerateInvoice.class);

	public static void generateInvoice(InvoiceDTO invoicedto,
			InputStream inputStream, PropBean propBean, ProjectType projecttype)
			throws JRException {

		int size = 0;
		if (invoicedto.getLineitem() != null) {
			if (invoicedto.getLineitem().size() > 5
					&& invoicedto.getLineitem().size() <= 11) {
				size = 10;
			} else if (invoicedto.getLineitem().size() > 11) {
				size = 20;
			}
		}
		String jrxmlFilepath = (String) propBean.getPropData().get(
				"fixedBidInvoicesJasper");
		String fixedBidInvoicePath = (String) propBean.getPropData().get(
				"fixedbidinvoices");
		List<InvoiceDTO> invoice = new ArrayList<InvoiceDTO>();
		invoice.add(invoicedto);

		JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(
				invoice);

		JasperDesign jasperDesign = JRXmlLoader.load(inputStream);
		JasperPrint jasperPrint = null;
		if (size == 10) {
			JasperCompileManager.compileReportToFile(jasperDesign,
					jrxmlFilepath + "LineItemMoreThen5.jasper");
			jasperPrint = JasperFillManager.fillReport(jrxmlFilepath
					+ "LineItemMoreThen5.jasper",
					new HashMap<String, String>(), beanColDataSource);
		} else if (size == 20) {
			JasperCompileManager.compileReportToFile(jasperDesign,
					jrxmlFilepath + "LineItemMoreThen12.jasper");
			jasperPrint = JasperFillManager.fillReport(jrxmlFilepath
					+ "LineItemMoreThen12.jasper",
					new HashMap<String, String>(), beanColDataSource);
		} else {
			JasperCompileManager.compileReportToFile(jasperDesign,
					jrxmlFilepath + "InvoicePDF.jasper");
			jasperPrint = JasperFillManager.fillReport(jrxmlFilepath
					+ "InvoicePDF.jasper", new HashMap<String, String>(),
					beanColDataSource);

		}
		File returnLocation = new File(fixedBidInvoicePath
				+ invoicedto.getNumber().replaceAll(" ", ".") + "."
				+ invoicedto.getProjectName().replaceAll(" ", ".") + "."

				+ invoicedto.getInvoiceDate().replaceAll("/", ".") + ".pdf");

		JasperExportManager.exportReportToPdfFile(jasperPrint,
				returnLocation.getAbsolutePath());

	}
}
