package com.raybiztech.mailtemplates.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import com.raybiztech.mailtemplates.dto.AccountEmailDto;
import com.raybiztech.mailtemplates.dto.MailTemplateDto;
import com.raybiztech.mailtemplates.dto.MailTemplateTypeDto;

public interface MailTemplatesService {

	public void addMailTemplateType(MailTemplateTypeDto dto);

	public void updateMailTemplateType(MailTemplateTypeDto dto);

	public void deleteMailTemplateType(Long id);

	public List<MailTemplateTypeDto> getAllMailTemplateTypes();

	public void addMailTemplate(MailTemplateDto dto);

	public List<MailTemplateDto> getAllMailTemplates(Long type,
			String searchText);

	public void updateMailTemplate(MailTemplateDto dto);

	public void deleteMailTemplate(Long id);

	public void saveEmail(AccountEmailDto accountEmailDto);

	public List<AccountEmailDto> getAccountEmail();

	public void updateAccountEmail(AccountEmailDto accountEmailDto);

	public ByteArrayOutputStream exportMailTemplates(Long type,
			String searchText) throws IOException;

}
