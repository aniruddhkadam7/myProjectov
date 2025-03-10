package com.raybiztech.mailtemplates.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.raybiztech.mailtemplates.dto.AccountEmailDto;
import com.raybiztech.mailtemplates.dto.MailTemplateDto;
import com.raybiztech.mailtemplates.dto.MailTemplateTypeDto;
import com.raybiztech.mailtemplates.service.MailTemplatesService;

@Controller
@RequestMapping("/mailTemplates")
public class MailTemplatesController {

	/**
	 * 
	 * @author shashank
	 * 
	 */

	Logger logger = Logger.getLogger(MailTemplatesController.class);

	@Autowired
	MailTemplatesService mailTemplatesServiceImpl;

	@RequestMapping(value = "/addMailTemplateType", method = RequestMethod.POST)
	public @ResponseBody void addMailTemplateType(
			@RequestBody MailTemplateTypeDto mailTemplateTypeDto) {
		mailTemplatesServiceImpl.addMailTemplateType(mailTemplateTypeDto);
	}

	@RequestMapping(value = "/updateMailTemplateType", method = RequestMethod.PUT)
	public @ResponseBody void updateMailTemplateType(
			@RequestBody MailTemplateTypeDto mailTemplateTypeDto) {

		mailTemplatesServiceImpl.updateMailTemplateType(mailTemplateTypeDto);
	}

	@RequestMapping(value = "/deleteMailTemplateType", params = { "id" }, method = RequestMethod.DELETE)
	public @ResponseBody void deleteMailTemplateType(@RequestParam Long id) {

		mailTemplatesServiceImpl.deleteMailTemplateType(id);
	}

	@RequestMapping(value = "/getMailTemplateTypes", method = RequestMethod.GET)
	public @ResponseBody List<MailTemplateTypeDto> getMailTemplateTypeList() {
		return mailTemplatesServiceImpl.getAllMailTemplateTypes();
	}

	@RequestMapping(value = "/addMailTemplate", method = RequestMethod.POST)
	public @ResponseBody void addMailTemplate(
			@RequestBody MailTemplateDto mailTemplateDto) {
		mailTemplatesServiceImpl.addMailTemplate(mailTemplateDto);
	}

	@RequestMapping(value = "/getMailTemplates", params = { "type",
			"searchText" }, method = RequestMethod.GET)
	public @ResponseBody List<MailTemplateDto> getAllMailTemplates(
			@RequestParam Long type, @RequestParam String searchText) {

		return mailTemplatesServiceImpl.getAllMailTemplates(type, searchText);
	}

	@RequestMapping(value = "/updateMailTemplate", method = RequestMethod.PUT)
	public @ResponseBody void updateMailTemplate(
			@RequestBody MailTemplateDto dto) {
		mailTemplatesServiceImpl.updateMailTemplate(dto);

	}

	@RequestMapping(value = "/deleteMailTemplate", params = { "id" }, method = RequestMethod.DELETE)
	public @ResponseBody void deleteMailTemplate(@RequestParam Long id) {
		mailTemplatesServiceImpl.deleteMailTemplate(id);
	}

	@RequestMapping(value = "/saveEmail", method = RequestMethod.POST)
	public @ResponseBody void saveEmail(
			@RequestBody AccountEmailDto accountEmailDto) {
		mailTemplatesServiceImpl.saveEmail(accountEmailDto);
	}

	@RequestMapping(value = "/getAccountEmail", method = RequestMethod.GET)
	public @ResponseBody List<AccountEmailDto> getAccountEmail() {
		return mailTemplatesServiceImpl.getAccountEmail();
	}

	@RequestMapping(value = "/updateAccountEmail", method = RequestMethod.PUT)
	public @ResponseBody void updateAccountEmail(
			@RequestBody AccountEmailDto accountEmailDto) {

		mailTemplatesServiceImpl.updateAccountEmail(accountEmailDto);

	}

	@RequestMapping(value = "/exportMailTemplates", params = { "type",
			"searchText" }, method = RequestMethod.GET)
	@ResponseBody
	public void exportInvoiceList(@RequestParam Long type,
			@RequestParam String searchText,
			HttpServletResponse httpServletResponse) throws IOException {
		httpServletResponse.setContentType("text/csv");
		httpServletResponse.setHeader("Content-Disposition",
				"attachment; filename=\"MailTemplatesList.csv\"");
		ByteArrayOutputStream os = mailTemplatesServiceImpl
				.exportMailTemplates(type, searchText);
		httpServletResponse.getOutputStream().write(os.toByteArray());

	}

}
