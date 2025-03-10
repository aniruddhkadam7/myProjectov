package com.raybiztech.mailtemplates.business;

import java.io.Serializable;

import com.raybiztech.assetmanagement.business.AssetType;
import com.raybiztech.date.Second;

public class MailTemplate implements Serializable {

	/**
	 * @author shashank
	 */
	private static final long serialVersionUID = -163905946675536098L;

	private Long id;
	private String templateName;
	private String template;
	private MailTemplateType templateType;
	private Long createdBy;
	private Second createdDate;
	private Long updatedBy;
	private Second updatedDate;
	private AssetType assetType;
	private String email;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public MailTemplateType getTemplateType() {
		return templateType;
	}

	public void setTemplateType(MailTemplateType templateType) {
		this.templateType = templateType;
	}

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

	public Long getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}

	public Second getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Second createdDate) {
		this.createdDate = createdDate;
	}

	public Long getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Long updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Second getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Second updatedDate) {
		this.updatedDate = updatedDate;
	}

	public AssetType getAssetType() {
		return assetType;
	}

	public void setAssetType(AssetType assetType) {
		this.assetType = assetType;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	
	
}
