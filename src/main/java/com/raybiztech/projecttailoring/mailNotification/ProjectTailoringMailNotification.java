package com.raybiztech.projecttailoring.mailNotification;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
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
import org.springframework.stereotype.Component;

import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.properties.PropBean;
import com.raybiztech.leavemanagement.exceptions.MailCantSendException;
import com.raybiztech.mailtemplates.dao.MailTemplatesDao;
import com.raybiztech.mailtemplates.util.MailContentParser;
import com.raybiztech.projectmanagement.exception.NoCheckList;
import com.raybiztech.projecttailoring.business.ProcessHeadData;
import com.raybiztech.projecttailoring.business.ProcessSubHeadData;
import com.raybiztech.projecttailoring.business.ProjectTailoring;

@Component("projectTailoringMailNotification")
public class ProjectTailoringMailNotification {

	@Autowired
	MailTemplatesDao mailTemplatesDao;

	@Autowired
	MailContentParser mailContentParser;

	@Autowired
	PropBean propBean;

	Logger logger = Logger.getLogger(ProjectTailoringMailNotification.class);

	public void sendProjectTailoringSubmissionMail(
			ProjectTailoring projectTailoring) throws IOException {

		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		int rowIndex = 1;
		Workbook workbook = new HSSFWorkbook();
		Sheet sheet = workbook.createSheet();
		Row row1 = sheet.createRow(0);
		Font font = workbook.createFont();
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		font.setFontHeightInPoints((short) 10);
		CellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
		cellStyle.setFont(font);

		Cell cell0 = row1.createCell(0);
		cell0.setCellValue("Process Head");
		cell0.setCellStyle(cellStyle);

		Cell cell1 = row1.createCell(1);
		cell1.setCellValue("Process Sub-Head");
		cell1.setCellStyle(cellStyle);

		Cell cell2 = row1.createCell(2);
		cell2.setCellValue("Process");
		cell2.setCellStyle(cellStyle);

		Cell cell3 = row1.createCell(3);
		cell3.setCellValue("Document");
		cell3.setCellStyle(cellStyle);

		Cell cell4 = row1.createCell(4);
		cell4.setCellValue("Responsible");
		cell4.setCellStyle(cellStyle);

		Cell cell5 = row1.createCell(5);
		cell5.setCellValue("Tailoring needed(Y/N)");
		cell5.setCellStyle(cellStyle);

		Cell cell6 = row1.createCell(6);
		cell6.setCellValue("Justification");
		cell6.setCellStyle(cellStyle);

		Set<ProcessHeadData> processHeadDatas = new HashSet<ProcessHeadData>();
		Set<ProcessSubHeadData> processSubHeadDatas = new HashSet<ProcessSubHeadData>();

		if (projectTailoring != null) {

			processHeadDatas = projectTailoring.getProcessHeadData();

			for (ProcessHeadData processHeadData : processHeadDatas) {

				Row ProcessHeadDataRow = sheet.createRow(rowIndex++);

				Cell cel0 = ProcessHeadDataRow.createCell(0);
				cel0.setCellValue(processHeadData.getProcessHead()
						.getProcessHeadname());

				processSubHeadDatas = processHeadData.getProcessSubHeadData();

				for (ProcessSubHeadData processSubHeadData : processSubHeadDatas) {

					Row processSubHeadDataRow = sheet.createRow(rowIndex++);

					Cell cel1 = processSubHeadDataRow.createCell(1);
					cel1.setCellValue(processSubHeadData.getProcessSubHead()
							.getProcessSubHeadName());

					Cell cel2 = processSubHeadDataRow.createCell(2);
					cel2.setCellValue(processSubHeadData.getProcessSubHead()
							.getProcessSubHeadName());

					Cell cel3 = processSubHeadDataRow.createCell(3);
					cel3.setCellValue(processSubHeadData.getProcessSubHead()
							.getDocumentName());

					Cell cel4 = processSubHeadDataRow.createCell(4);
					cel4.setCellValue(processSubHeadData.getProcessSubHead()
							.getResponsible());

					Cell cel5 = processSubHeadDataRow.createCell(5);
					cel5.setCellValue(processSubHeadData.getSpecificToProject());

					Cell cel6 = processSubHeadDataRow.createCell(6);
					cel6.setCellValue(processSubHeadData.getComments());

				}

			}

		} else {
			try {
				throw new NoCheckList(
						"Check list is not available for particular project");
			} catch (NoCheckList e) {

			}
		}
		workbook.write(byteArrayOutputStream);
		byteArrayOutputStream.flush();
		byteArrayOutputStream.close();

		FileOutputStream fos = null;
		File someFile = null;
		String filePath = null;
		try {

			filePath = (String) propBean.getPropData().get("ProjectTailoring");

			someFile = new File("ProjectTailoring" + ".csv");
			fos = new FileOutputStream(someFile);
			fos.write(byteArrayOutputStream.toByteArray());
		} catch (FileNotFoundException fne) {

		} finally {
			try {
				if (fos != null) {
					fos.flush();
				}
			} catch (IOException ie) {

			}
			try {
				if (fos != null) {
					fos.close();
				}
			} catch (IOException ie) {

			}
		}

		String to = (String) propBean.getPropData().get("mail.sqaMailId");

		Employee deliveryManager = projectTailoring.getProject()
				.getProjectManager().getManager();

		/*
		 * while
		 * (!deliveryManager.getRole().equalsIgnoreCase("Delivery Manager")) {
		 * deliveryManager = mailTemplatesDao.findBy(Employee.class,
		 * deliveryManager.getManager().getEmployeeId()); }
		 */

		Map<String, String> details = new HashMap<String, String>();
		details.put("regarding", "<![CDATA["
				+ projectTailoring.getProject().getProjectName()
				+ " Project Tailoring Document Submission]]>");
		details.put("toName", "SQA Team");
		details.put("cc", deliveryManager.getEmail());
		details.put("to", to);
		details.put("subject", projectTailoring.getProject().getProjectName());
		details.put("bcc", "");
		details.put("path", someFile.toString());
		details.put("name", projectTailoring.getProject().getProjectManager().getFullName());

		String content = mailTemplatesDao
				.getMailContent("Project Tailoring Submission");
		if (content != null) {
			mailContentParser.parseAndSendMail(details, content);

		} else {
			throw new MailCantSendException(
					"Mail Content is not available for 'Project Tailoring Approval' template Type");
		}

	}

	public void sendProjectTailoringApprovalMail(Long projectTailoringId) {

		ProjectTailoring projectTailoring = mailTemplatesDao.findBy(
				ProjectTailoring.class, projectTailoringId);

		Employee manager = projectTailoring.getProject().getProjectManager();
		Employee deliveryManager = manager.getManager();

		/*
		 * while
		 * (!deliveryManager.getRole().equalsIgnoreCase("Delivery Manager")) {
		 * deliveryManager = mailTemplatesDao.findBy(Employee.class,
		 * deliveryManager.getManager().getEmployeeId()); }
		 */

		Map<String, String> details = new HashMap<String, String>();
		details.put("regarding", "<![CDATA["
				+ projectTailoring.getProject().getProjectName()
				+ " Project Tailoring Document Approval]]>");
		details.put("toName", manager.getFullName());
		details.put("cc", deliveryManager.getEmail());
		details.put("to", manager.getEmail());
		details.put("subject", projectTailoring.getProject().getProjectName());
		details.put("bcc", "");

		String content = mailTemplatesDao
				.getMailContent("Project Tailoring Approval");
		if (content != null) {
			mailContentParser.parseAndSendMail(details, content);

		} else {
			throw new MailCantSendException(
					"Mail Content is not available for 'Project Tailoring Approval' template Type");
		}

	}

	public void sendProjectTailoringRejectionMail(Long projectTailoringId) {

		ProjectTailoring projectTailoring = mailTemplatesDao.findBy(
				ProjectTailoring.class, projectTailoringId);

		Employee manager = projectTailoring.getProject().getProjectManager();
		Employee deliveryManager = manager.getManager();

		/*
		 * while
		 * (!deliveryManager.getRole().equalsIgnoreCase("Delivery Manager")) {
		 * deliveryManager = mailTemplatesDao.findBy(Employee.class,
		 * deliveryManager.getManager().getEmployeeId()); }
		 */

		Map<String, String> details = new HashMap<String, String>();
		details.put("regarding", "<![CDATA["
				+ projectTailoring.getProject().getProjectName()
				+ " Project Tailoring Document Rejection]]>");
		details.put("toName", manager.getFullName());
		details.put("cc", deliveryManager.getEmail());
		details.put("to", manager.getEmail());
		details.put("subject", projectTailoring.getProject().getProjectName());
		details.put("Comment", projectTailoring.getRejectComments());
		details.put("bcc", "");

		String content = mailTemplatesDao
				.getMailContent("Project Tailoring Rejection");
		if (content != null) {
			mailContentParser.parseAndSendMail(details, content);

		} else {
			throw new MailCantSendException(
					"Mail Content is not available for 'Project Tailoring Approval' template Type");
		}
	}
}
