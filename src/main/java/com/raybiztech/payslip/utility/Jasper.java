package com.raybiztech.payslip.utility;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRPdfExporterParameter;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.raybiztech.appraisals.properties.PropBean;
import com.raybiztech.date.Date;
import com.raybiztech.leavemanagement.business.algorithm.DateParser;
import com.raybiztech.payslip.dto.PayslipRetrieveDto;

@Component("jasper")
public class Jasper {

	@Autowired
	PropBean propBean;

	public static void genratepdf(PayslipRetrieveDto payslipRetrieveDto,
			InputStream inputStream, PropBean propBean) throws JRException {

		OutputStream out = null;

		String jrxmlFilepath = (String) propBean.getPropData().get(
				"jasperReports");
		String payslipPath = (String) propBean.getPropData().get("payslips");

		// inputStream = new BufferedInputStream(new FileInputStream(path
		// + "Salary.jrxml"));

		List<PayslipRetrieveDto> pay = new ArrayList<PayslipRetrieveDto>();
		pay.add(payslipRetrieveDto);

		JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(
				pay);

		JasperDesign jasperDesign = JRXmlLoader.load(inputStream);
		JasperCompileManager.compileReportToFile(jasperDesign, jrxmlFilepath
				+ "Salary.jasper");
		JasperPrint jasperPrint = JasperFillManager.fillReport(jrxmlFilepath
				+ "Salary.jasper", new HashMap<String, String>(),
				beanColDataSource);
		File returnLocation = new File(payslipPath
				+ payslipRetrieveDto.getEmployeeId() + "_"
				+ payslipRetrieveDto.getMonth() + "_"
				+ payslipRetrieveDto.getYear() + ".pdf");

		JasperExportManager.exportReportToPdfFile(jasperPrint,
				returnLocation.getAbsolutePath());

		// get Password
		String password = generatePayslipPDFPassword(payslipRetrieveDto);

		JRPdfExporter exporter = new JRPdfExporter();
		try {
			out = new FileOutputStream(returnLocation);
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, out);
			exporter.setParameter(JRPdfExporterParameter.OWNER_PASSWORD,
					password);
			exporter.setParameter(JRPdfExporterParameter.USER_PASSWORD,
					password);
			exporter.setParameter(JRPdfExporterParameter.IS_ENCRYPTED,
					Boolean.TRUE);
			exporter.exportReport();

		} catch (FileNotFoundException fne) {
			fne.printStackTrace();

		} finally {
			try {
				if (inputStream != null)
					inputStream.close();
			} catch (IOException e) {

				e.printStackTrace();
			}
			try {
				if (out != null)
					out.close();
			} catch (IOException e) {

				e.printStackTrace();
			}
		}

	}

	public static String generatePayslipPDFPassword(
			PayslipRetrieveDto payslipRetrieveDto) {
		
		
		String password = null;

		String pwdDate = null;
		try {

			Date dob = DateParser.toDateOtherFormat(payslipRetrieveDto
					.getDateOfBirth());

			String dayofmonth = String.valueOf(dob.getDayOfMonth().getValue());
			String daymonth = dayofmonth.length() == 1 ? "0" + dayofmonth
					: String.valueOf(dayofmonth);

			String monthofyear = String
					.valueOf(dob.getMonthOfYear().getValue() + 1);

			String monthYear = monthofyear.length() == 1 ? "0" + monthofyear
					: String.valueOf(monthofyear);

			pwdDate = daymonth + monthYear;
		} catch (ParseException pe) {
			pe.printStackTrace();
		}

		String panNumber = payslipRetrieveDto.getPanNumber();
		String accountNum = payslipRetrieveDto.getAccountNo();
		String halfPwd = "";
		
		
		
			if(accountNum!=null)
			{
				halfPwd = accountNum.substring(accountNum.length() - 3) + pwdDate;
			}
			if(panNumber!=null && panNumber.length()>=3)
			{
				password = panNumber.substring(panNumber.length() - 3) + halfPwd;
			}
			else
			{
				password = halfPwd;
			}
		
		
		return password;
	}

}
