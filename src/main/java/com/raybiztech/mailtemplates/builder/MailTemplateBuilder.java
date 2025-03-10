package com.raybiztech.mailtemplates.builder;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.raybiztech.assetmanagement.business.AssetType;
import com.raybiztech.mailtemplates.business.MailTemplate;
import com.raybiztech.mailtemplates.business.MailTemplateType;
import com.raybiztech.mailtemplates.dao.MailTemplatesDao;
import com.raybiztech.mailtemplates.dto.MailTemplateDto;

@Component("mailTemplateBuilder")
public class MailTemplateBuilder {

	@Autowired
	MailTemplateTypeBuilder mailTemplateTypeBuilder;
	@Autowired
	MailTemplatesDao mailTemplatesDaoImpl;

	Logger logger = Logger.getLogger(MailTemplateBuilder.class);

	public MailTemplate toEntity(MailTemplateDto mailTemplateDto) {
		MailTemplate mailTemplate = null;
		if (mailTemplateDto != null) {
			mailTemplate = new MailTemplate();
			mailTemplate.setTemplate(mailTemplateDto.getTemplate());
			mailTemplate.setTemplateName(mailTemplateDto.getTemplateName());
			mailTemplate
					.setTemplateType(mailTemplatesDaoImpl.findBy(
							MailTemplateType.class,
							mailTemplateDto.getTemplateTypeId()));
			if (mailTemplate.getAssetType() != null) {
				mailTemplate.setAssetType(mailTemplatesDaoImpl.findBy(
						AssetType.class, mailTemplateDto.getAssetTypeId()));
				mailTemplate.setEmail(mailTemplateDto.getEmail());
			}

		}
		return mailTemplate;
	}

	public MailTemplateDto toDto(MailTemplate mailTemplate) {
		MailTemplateDto mailTemplateDto = null;
		if (mailTemplate != null) {
			mailTemplateDto = new MailTemplateDto();
			mailTemplateDto.setId(mailTemplate.getId());
			mailTemplateDto.setTemplate(mailTemplate.getTemplate());
			mailTemplateDto.setTemplateName(mailTemplate.getTemplateName());
			MailTemplateType mailTemplateType = mailTemplate.getTemplateType();
			mailTemplateDto.setTemplateType(mailTemplateType.getName());
			mailTemplateDto.setTemplateTypeId(mailTemplateType.getId());
			AssetType assetType = mailTemplate.getAssetType();
			mailTemplateDto.setAssetType(assetType != null ? assetType
					.getAssetType() : "N/A");
			mailTemplateDto.setAssetTypeId(assetType != null ? assetType
					.getId() : null);
			mailTemplateDto.setEmail(mailTemplate.getEmail());

		}
		return mailTemplateDto;
	}

	public List<MailTemplate> toEntityList(
			List<MailTemplateDto> mailTemplateDtos) {
		List<MailTemplate> mailTemplates = null;
		if (mailTemplateDtos != null) {
			mailTemplates = new ArrayList<MailTemplate>();
			for (MailTemplateDto mailTemplateDto : mailTemplateDtos) {
				mailTemplates.add(toEntity(mailTemplateDto));
			}
		}
		return mailTemplates;
	}

	public List<MailTemplateDto> toDtoList(List<MailTemplate> mailTemplates) {
		List<MailTemplateDto> templates = null;
		if (mailTemplates != null) {
			templates = new ArrayList<MailTemplateDto>();
			for (MailTemplate mailTemplate : mailTemplates) {
				templates.add(toDto(mailTemplate));
			}
		}
		return templates;
	}
}
