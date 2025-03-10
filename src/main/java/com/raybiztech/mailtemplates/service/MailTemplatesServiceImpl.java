package com.raybiztech.mailtemplates.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

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

import com.raybiztech.appraisals.security.utils.SecurityUtils;
import com.raybiztech.date.Second;
import com.raybiztech.mailtemplates.builder.AccountEmailBuilder;
import com.raybiztech.mailtemplates.builder.MailTemplateBuilder;
import com.raybiztech.mailtemplates.builder.MailTemplateTypeBuilder;
import com.raybiztech.mailtemplates.business.AccountEmail;
import com.raybiztech.mailtemplates.business.MailTemplate;
import com.raybiztech.mailtemplates.business.MailTemplateType;
import com.raybiztech.mailtemplates.dao.MailTemplatesDao;
import com.raybiztech.mailtemplates.dto.AccountEmailDto;
import com.raybiztech.mailtemplates.dto.MailTemplateDto;
import com.raybiztech.mailtemplates.dto.MailTemplateTypeDto;

@Service("mailTemplatesServiceImpl")
public class MailTemplatesServiceImpl implements MailTemplatesService {

	/**
	 * 
	 * @author shashank
	 * 
	 */

	@Autowired
	MailTemplateTypeBuilder mailTemplateTypeBuilder;
	@Autowired
	MailTemplatesDao mailTemplatesDaoImpl;
	@Autowired
	MailTemplateBuilder mailTemplateBuilder;
	@Autowired
	SecurityUtils securityUtils;
	@Autowired
	AccountEmailBuilder accountEmailBuilder;

	@Override
	public void addMailTemplateType(MailTemplateTypeDto dto) {

		MailTemplateType mailTemplateType = mailTemplateTypeBuilder
				.toEntity(dto);
		mailTemplatesDaoImpl.save(mailTemplateType);

	}

	@Override
	public void updateMailTemplateType(MailTemplateTypeDto dto) {
		if (dto != null) {
			MailTemplateType mailTemplateType = mailTemplatesDaoImpl.findBy(
					MailTemplateType.class, dto.getId());
			mailTemplateType.setName(dto.getName());
			mailTemplatesDaoImpl.update(mailTemplateType);
		}

	}

	@Override
	public void deleteMailTemplateType(Long id) {
		mailTemplatesDaoImpl.delete(mailTemplatesDaoImpl.findBy(
				MailTemplateType.class, id));

	}

	@Override
	public List<MailTemplateTypeDto> getAllMailTemplateTypes() {
		List<MailTemplateType> mailTemplateTypesList = mailTemplatesDaoImpl
				.get(MailTemplateType.class);
		return mailTemplateTypeBuilder.toDtoList(mailTemplateTypesList);
	}

	@Override
	public void addMailTemplate(MailTemplateDto dto) {
		MailTemplate mailTemplate = mailTemplateBuilder.toEntity(dto);
		mailTemplate.setCreatedBy(securityUtils
				.getLoggedEmployeeIdforSecurityContextHolder());
		mailTemplate.setCreatedDate(new Second());
		mailTemplatesDaoImpl.save(mailTemplate);
	}

	@Override
	public List<MailTemplateDto> getAllMailTemplates(Long type,
			String searchText) {

		List<MailTemplateDto> mailTemplateDtos = mailTemplateBuilder
				.toDtoList(mailTemplatesDaoImpl.getMailTemplates(type,
						searchText));

		return mailTemplateDtos;
	}

	@Override
	public void updateMailTemplate(MailTemplateDto dto) {

		MailTemplate mailTemplate = mailTemplatesDaoImpl.findBy(
				MailTemplate.class, dto.getId());
		mailTemplate.setTemplate(dto.getTemplate());
		mailTemplate.setUpdatedBy(securityUtils
				.getLoggedEmployeeIdforSecurityContextHolder());
		mailTemplate.setUpdatedDate(new Second());
		if (dto != null) {
			mailTemplate.setEmail(dto.getEmail());
		}
		mailTemplatesDaoImpl.update(mailTemplate);
	}

	@Override
	public void deleteMailTemplate(Long id) {

		mailTemplatesDaoImpl.delete(mailTemplatesDaoImpl.findBy(
				MailTemplate.class, id));

	}

	@Override
	public void saveEmail(AccountEmailDto accountEmailDto) {
		// TODO Auto-generated method stub
		AccountEmail accountEmail = accountEmailBuilder
				.toEntity(accountEmailDto);
		mailTemplatesDaoImpl.save(accountEmail);

	}

	@Override
	public List<AccountEmailDto> getAccountEmail() {
		// TODO Auto-generated method stub
		List<AccountEmail> accountEmailList = mailTemplatesDaoImpl
				.get(AccountEmail.class);
		List<AccountEmailDto> accountEmailDtos = accountEmailBuilder
				.toDTOList(accountEmailList);
		return accountEmailDtos;
	}

	@Override
	public void updateAccountEmail(AccountEmailDto accountEmailDto) {
		// TODO Auto-generated method stub

		AccountEmail accountEmail = mailTemplatesDaoImpl.findBy(
				AccountEmail.class, accountEmailDto.getId());
		accountEmail = accountEmailBuilder.toEditEntity(accountEmailDto,
				accountEmail.getSaltKey());
		mailTemplatesDaoImpl.update(accountEmail);

	}

	@Override
	public ByteArrayOutputStream exportMailTemplates(Long type,
			String searchText) throws IOException {
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
		cell0.setCellValue("Type");
		cell0.setCellStyle(style);

		Cell cell1 = row1.createCell(1);
		cell1.setCellValue("Title");
		cell1.setCellStyle(style);

		Cell cell2 = row1.createCell(2);
		cell2.setCellValue("Content");
		cell2.setCellStyle(style);

		for (MailTemplate dto : mailTemplatesDaoImpl.getMailTemplatesForExport(
				type, searchText)) {

			Row row = sheet.createRow(rowIndex++);

			Cell cel0 = row.createCell(0);
			cel0.setCellValue(dto.getTemplateType().getName());

			Cell cel1 = row.createCell(1);
			cel1.setCellValue(dto.getTemplateName());

			Cell cel2 = row.createCell(2);
			cel2.setCellValue(dto.getTemplate().replaceAll("\\<[^>]*>", ""));

			sheet.autoSizeColumn(0);
			sheet.autoSizeColumn(1);
			sheet.autoSizeColumn(2);

		}

		workbook.write(bos);
		bos.flush();
		bos.close();
		return bos;
	}
}
