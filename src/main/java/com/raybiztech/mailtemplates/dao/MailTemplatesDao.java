package com.raybiztech.mailtemplates.dao;

import java.util.List;

import com.raybiztech.appraisals.dao.DAO;
import com.raybiztech.assetmanagement.business.AssetType;
import com.raybiztech.mailtemplates.business.MailTemplate;

public interface MailTemplatesDao extends DAO {

	public List<MailTemplate> getMailTemplates(Long type, String searchText);

	public String getMailContent(String mailTemplateName);

	public List<MailTemplate> getMailTemplatesForExport(Long type,
			String searchText);

	public MailTemplate getmailTemplateOfAssetType(AssetType assetType);
}
