package com.raybiztech.projectmanagement.invoicesummary.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.raybiztech.projectmanagement.builder.ProjectBuilder;
import com.raybiztech.projectmanagement.business.ChangeRequest;
import com.raybiztech.projectmanagement.business.Client;
import com.raybiztech.projectmanagement.business.Milestone;
import com.raybiztech.projectmanagement.business.Project;
import com.raybiztech.projectmanagement.dto.ProjectDTO;
import com.raybiztech.projectmanagement.invoice.business.InvoiceSummary;
import com.raybiztech.projectmanagement.invoice.business.ReceivedInvoiceSummary;
import com.raybiztech.projectmanagement.invoice.business.SentInvoiceSummary;
import com.raybiztech.projectmanagement.invoice.dao.InvoiceDao;
import com.raybiztech.projectmanagement.invoice.dto.InvoiceDTO;
import com.raybiztech.projectmanagement.invoice.dto.InvoiceSummaryDTO;
import com.raybiztech.projectmanagement.invoicesummary.builder.InvoiceSummaryBuilder;

@Service("invoiceSummaryServiceImpl")
public class InvoiceSummaryServiceImpl implements InvoiceSummaryService {

	Logger logger = Logger.getLogger(InvoiceSummaryServiceImpl.class);

	private final ProjectBuilder projectBuilder;
	private final InvoiceDao invoicedaoImpl;
	private final InvoiceSummaryBuilder summaryBuilder;

	@Autowired
	public InvoiceSummaryServiceImpl(InvoiceDao invoicedaoImpl,
			ProjectBuilder projectBuilder, InvoiceSummaryBuilder summaryBuilder) {
		this.invoicedaoImpl = invoicedaoImpl;
		this.projectBuilder = projectBuilder;
		this.summaryBuilder = summaryBuilder;
	}

	@Override
	public List<InvoiceSummaryDTO> getInvoiceSummary(Long projectId,
			Long clientId) {

		Project project = (projectId != null) ? invoicedaoImpl.findBy(
				Project.class, projectId) : null;
		Client client = (clientId != null) ? invoicedaoImpl.findBy(
				Client.class, clientId) : null;

		List<InvoiceSummary> invoiceSummaries = invoicedaoImpl
				.getInvoiceSummaryList(project, client);
		return summaryBuilder.covertInvoiceSummaryToDTO(invoiceSummaries);
	}

	@Override
	public Map<String, Object> getInvoiceSummaryWithTotalRoundUpamounts(
			Long projectId, Long clientId) {

		Map<String, Object> map = new HashMap<String, Object>();

		Project project = (projectId != null) ? invoicedaoImpl.findBy(
				Project.class, projectId) : null;
		Client client = (clientId != null) ? invoicedaoImpl.findBy(
				Client.class, clientId) : null;

		List<InvoiceSummary> invoiceSummaries = invoicedaoImpl
				.getInvoiceSummaryList(project, client);

		List<InvoiceSummaryDTO> summaryDtos = summaryBuilder
				.covertInvoiceSummaryToDTO(invoiceSummaries);

		Long roundUpTotalAmount = 0L;
		Long roundUpSentAmount = 0L;
		Long roundUpReceivedAmount = 0L;
		Long roundUpPendingAmount = 0L;
		String currency = null;

		if (!summaryDtos.isEmpty()) {

			for (InvoiceSummaryDTO invoiceSummary : summaryDtos) {

				if (invoiceSummary.getTotalAmount() != null) {
					roundUpTotalAmount = roundUpTotalAmount
							+ (long) Math.round(Double.valueOf(invoiceSummary
									.getTotalAmount()));
				}

				if (invoiceSummary.getSentAmount() != null) {
					roundUpSentAmount = roundUpSentAmount
							+ (long) Math.round(Double.valueOf(invoiceSummary
									.getSentAmount()));
				}

				if (invoiceSummary.getReceivedAmount() != null) {
					roundUpReceivedAmount = roundUpReceivedAmount
							+ (long) Math.round(Double.valueOf(invoiceSummary
									.getReceivedAmount()));
				}

				if (invoiceSummary.getPendingAmount() != null) {
					roundUpPendingAmount = roundUpPendingAmount
							+ (long) Math.round(Double.valueOf(invoiceSummary
									.getPendingAmount()));
				}

				currency = invoiceSummary.getCurrency();

			}

			InvoiceSummaryDTO summaryDTO = new InvoiceSummaryDTO();
			summaryDTO.setClientName("-");
			summaryDTO.setProjectName("-");
			summaryDTO.setCrName("-");
			summaryDTO.setTotalAmount(roundUpTotalAmount.toString());
			summaryDTO.setSentAmount(roundUpSentAmount.toString());
			summaryDTO.setSentInvoiceCount(null);
			summaryDTO.setReceivedAmount(roundUpReceivedAmount.toString());
			summaryDTO.setReceivedInvoiceCount(null);
			summaryDTO.setPendingAmount(roundUpPendingAmount.toString());
			summaryDTO.setCurrency(currency);
			summaryDtos.add(summaryDTO);
		}

		map.put("list", summaryDtos);
		/*
		 * map.put("roundUpTotalAmount", roundUpTotalAmount);
		 * map.put("roundUpSentAmount", roundUpSentAmount);
		 * map.put("roundUpReceivedAmount", roundUpReceivedAmount);
		 * map.put("roundUpPendingAmount", roundUpPendingAmount);
		 * map.put("currency", currency);
		 */

		return map;
	}

	@Override
	public List<ProjectDTO> getAllProjects() {
		return projectBuilder.createProjectActiveList(invoicedaoImpl
				.getAllProjects());
	}

	@Override
	public List<InvoiceDTO> getSentInvoiceForSummary(Long summaryId) {
		InvoiceSummary invoiceSummary = invoicedaoImpl.findBy(
				InvoiceSummary.class, summaryId);

		Set<Long> sentInvoiceIdSet = null;
		if (invoiceSummary != null) {
			sentInvoiceIdSet = new HashSet<Long>();
			for (SentInvoiceSummary sentInvoiceSummary : invoiceSummary
					.getSentInvoices()) {
				sentInvoiceIdSet.add(sentInvoiceSummary.getSentInvoiceId());
			}
		}

		return summaryBuilder.getInvoices(sentInvoiceIdSet);
	}

	@Override
	public List<InvoiceDTO> getReceviedInvoicesForSummary(Long summaryId) {
		InvoiceSummary invoiceSummary = invoicedaoImpl.findBy(
				InvoiceSummary.class, summaryId);

		Set<Long> receivedInvoices = null;

		if (invoiceSummary != null) {
			receivedInvoices = new HashSet<Long>();
			for (ReceivedInvoiceSummary receivedInvoiceSummary : invoiceSummary
					.getReceivedinvoices()) {
				receivedInvoices.add(receivedInvoiceSummary
						.getReceivedInvoiceId());
			}
		}

		return summaryBuilder.getInvoices(receivedInvoices);
	}

	@Override
	public ByteArrayOutputStream exportInvoiceSummary(Long projectId,
			Long clientId) throws IOException {

		ByteArrayOutputStream bos = new ByteArrayOutputStream();

		List<InvoiceSummaryDTO> dtos = getInvoiceSummary(projectId, clientId);

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
		cell0.setCellValue("Client");
		cell0.setCellStyle(style);

		Cell cell1 = row1.createCell(1);
		cell1.setCellValue("Project");
		cell1.setCellStyle(style);

		Cell cell2 = row1.createCell(2);
		cell2.setCellValue("Change Request");
		cell2.setCellStyle(style);

		Cell cell3 = row1.createCell(3);
		cell3.setCellValue("Total Amount");
		cell3.setCellStyle(style);

		Cell cell4 = row1.createCell(4);
		cell4.setCellValue("Sent Amount");
		cell4.setCellStyle(style);

		Cell cell5 = row1.createCell(5);
		cell5.setCellValue("Sent Invoice Count");
		cell5.setCellStyle(style);

		Cell cell6 = row1.createCell(6);
		cell6.setCellValue("Received Amount");
		cell6.setCellStyle(style);

		Cell cell7 = row1.createCell(7);
		cell7.setCellValue("Received Invoice Count");
		cell7.setCellStyle(style);

		Cell cell8 = row1.createCell(8);
		cell8.setCellValue("pending Amount");
		cell8.setCellStyle(style);

		for (InvoiceSummaryDTO dto : dtos) {

			Row row = sheet.createRow(rowIndex++);

			Cell cel0 = row.createCell(0);
			cel0.setCellValue(dto.getClientName());

			Cell cel1 = row.createCell(1);
			cel1.setCellValue(dto.getProjectName());

			Cell cel2 = row.createCell(2);
			cel2.setCellValue(dto.getCrName());

			Cell cel3 = row.createCell(3);
			cel3.setCellValue(dto.getTotalAmount());

			Cell cel4 = row.createCell(4);
			cel4.setCellValue(dto.getSentAmount());

			Cell cel5 = row.createCell(5);
			cel5.setCellValue(dto.getSentInvoiceCount());

			Cell cel6 = row.createCell(6);
			cel6.setCellValue(dto.getReceivedAmount());

			Cell cel7 = row.createCell(7);
			cel7.setCellValue(dto.getReceivedInvoiceCount());

			Cell cel8 = row.createCell(8);
			cel8.setCellValue(dto.getPendingAmount());

			List<InvoiceDTO> invoiceDTOs = getSentInvoiceForSummary(dto.getId());

			Row row2 = sheet.createRow(rowIndex++);
			// Inner Cells starts here
			Cell innercell0 = row2.createCell(0);
			innercell0.setCellValue("Serial Number");
			innercell0.setCellStyle(style);
			
			Cell innercell1 = row2.createCell(1);
			innercell1.setCellValue("Invoice Number");
			innercell1.setCellStyle(style);

			Cell innercell2 = row2.createCell(2);
			innercell2.setCellValue("Milestone");
			innercell2.setCellStyle(style);

			Cell innercell3 = row2.createCell(3);
			innercell3.setCellValue("Percentage");
			innercell3.setCellStyle(style);

			Cell innercell4 = row2.createCell(4);
			innercell4.setCellValue("Status");
			innercell4.setCellStyle(style);

			Cell innercell5 = row2.createCell(5);
			innercell5.setCellValue("Sent Date");
			innercell5.setCellStyle(style);

			Cell innercell6 = row2.createCell(6);
			innercell6.setCellValue("Received Date");
			innercell6.setCellStyle(style);

			Cell innercell7 = row2.createCell(7);
			innercell7.setCellValue("Invoice Amount");
			innercell7.setCellStyle(style);

			Cell innercell8 = row2.createCell(8);
			innercell8.setCellValue("Discount");
			innercell8.setCellStyle(style);

			Cell innercell9 = row2.createCell(9);
			innercell9.setCellValue("Tax");
			innercell9.setCellStyle(style);

			Cell innercell10 = row2.createCell(10);
			innercell10.setCellValue("Total Amount");
			innercell10.setCellStyle(style);

			for (InvoiceDTO invoiceDto : invoiceDTOs) {

				if (invoiceDto != null) {
					Row innerrow = sheet.createRow(rowIndex++);

					Cell innercel0 = innerrow.createCell(0);
					innercel0.setCellValue(invoiceDto.getNumber());
					
					Cell innercel1 = innerrow.createCell(1);
					innercel1.setCellValue(invoiceDto.getInvoiceNumber() != null ? invoiceDto.getInvoiceNumber() : invoiceDto.getNumber());

					Cell innercel2 = innerrow.createCell(2);
					innercel2.setCellValue(invoiceDto.getMileStoneName());

					Cell innercel3 = innerrow.createCell(3);
					innercel3.setCellValue(invoiceDto.getPercentage());

					Cell innercel4 = innerrow.createCell(4);
					innercel4.setCellValue(invoiceDto.getInvoiceStatus());

					Cell innercel5 = innerrow.createCell(5);
					innercel5.setCellValue(invoiceDto
							.getInvoiceAmountSentDate());

					Cell innercel6 = innerrow.createCell(6);
					innercel6.setCellValue(invoiceDto
							.getInvoiceAmountReceviedDate());

					Cell innercel7 = innerrow.createCell(7);
					innercel7.setCellValue(invoiceDto.getAmount());

					Cell innercel8 = innerrow.createCell(8);
					innercel8.setCellValue(invoiceDto.getDiscount());

					Cell innercel9 = innerrow.createCell(9);
					innercel9.setCellValue(invoiceDto.getTax());

					Cell innercel10 = innerrow.createCell(10);
					innercel10.setCellValue(invoiceDto.getTotalAmount());
				}
			}
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
		}

		workbook.write(bos);
		bos.flush();
		bos.close();

		return bos;
	}

	@Override
	public void calculateInvoiceSummary() {
		List<Project> projects = invoicedaoImpl
				.getFixedAndRetainerTypeProjects();
		for (Project project : projects) {

			List<Milestone> projectMilestones = invoicedaoImpl
					.getclosedBillableMilestoneOfProject(project);
			logger.warn("=========================START===================");

			logger.warn("Project " + project.getProjectName() + "Have "
					+ projectMilestones.size() + " Project Milestones");

			if (projectMilestones.size() != 0) {
				summaryBuilder.calculateSummary(projectMilestones, project,
						null);
			}

			List<ChangeRequest> changeRequests = invoicedaoImpl
					.getChangeRequestOfProject(project.getId());
			logger.warn("Project " + project.getProjectName() + "Have "
					+ changeRequests.size() + "Change Requests");
			if (changeRequests.size() != 0) {
				for (ChangeRequest changeRequest : changeRequests) {
					List<Milestone> crMilestones = invoicedaoImpl
							.getMilestoneOfCR(changeRequest);
					logger.warn("change Request " + changeRequest.getName()
							+ " have " + crMilestones.size() + "milestones");

					if (crMilestones.size() != 0) {
						summaryBuilder.calculateSummary(crMilestones, project,
								changeRequest);
					}

				}
			}

			logger.warn("======================END================");

		}
	}

}
