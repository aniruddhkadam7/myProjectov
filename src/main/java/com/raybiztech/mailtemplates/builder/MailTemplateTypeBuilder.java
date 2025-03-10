package com.raybiztech.mailtemplates.builder;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.raybiztech.mailtemplates.business.MailTemplateType;
import com.raybiztech.mailtemplates.dto.MailTemplateTypeDto;

@Component("mailTemplateTypeBuilder")
public class MailTemplateTypeBuilder {

	/**
	 * 
	 * @author shashank
	 * 
	 */

	public MailTemplateTypeDto toDto(MailTemplateType mailTemplateType) {
		MailTemplateTypeDto dto = null;
		if (mailTemplateType != null) {
			dto = new MailTemplateTypeDto();
			dto.setId(mailTemplateType.getId());
			dto.setName(mailTemplateType.getName());
		}
		return dto;
	}

	public MailTemplateType toEntity(MailTemplateTypeDto dto) {
		MailTemplateType mailTemplateType = null;
		if (dto != null) {
			mailTemplateType = new MailTemplateType();
			mailTemplateType.setId(dto.getId());
			mailTemplateType.setName(dto.getName());
		}
		return mailTemplateType;
	}

	public List<MailTemplateTypeDto> toDtoList(
			List<MailTemplateType> mailTemplateTypes) {

		List<MailTemplateTypeDto> mailTemplateTypesList = null;
		if (mailTemplateTypes != null) {
			mailTemplateTypesList = new ArrayList<MailTemplateTypeDto>();
			for (MailTemplateType mailTemplateType : mailTemplateTypes) {
				mailTemplateTypesList.add(toDto(mailTemplateType));
			}
		}
		return mailTemplateTypesList;

	}

}
