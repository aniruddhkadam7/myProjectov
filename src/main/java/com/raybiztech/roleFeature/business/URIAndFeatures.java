package com.raybiztech.rolefeature.business;

import java.io.Serializable;

public class URIAndFeatures implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3595911719318357333L;

	private Long id;
	private String featureUrl;
	private String urlMethod;
	private String accessType;
	private Feature feature;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFeatureUrl() {
		return featureUrl;
	}

	public void setFeatureUrl(String featureUrl) {
		this.featureUrl = featureUrl;
	}

	public String getUrlMethod() {
		return urlMethod;
	}

	public void setUrlMethod(String urlMethod) {
		this.urlMethod = urlMethod;
	}

	public Feature getFeature() {
		return feature;
	}

	public void setFeature(Feature feature) {
		this.feature = feature;
	}

	public String getAccessType() {
		return accessType;
	}

	public void setAccessType(String accessType) {
		this.accessType = accessType;
	}

}
