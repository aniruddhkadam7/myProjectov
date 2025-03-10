package com.raybiztech.hrm.security.rest;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;

public class HRMFilterInvocationSecurityMetadataSourceBeanPostProcessor
		implements BeanPostProcessor {

	private FilterInvocationSecurityMetadataSource metadataSource;

	@Override
	public Object postProcessBeforeInitialization(Object bean, String name)
			throws BeansException {
		if (bean instanceof FilterInvocationSecurityMetadataSource) {
			return metadataSource;
		}
		return bean;
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String name)
			throws BeansException {
		return bean;
	}

	public FilterInvocationSecurityMetadataSource getMetadataSource() {
		return metadataSource;
	}

	public void setMetadataSource(
			FilterInvocationSecurityMetadataSource metadataSource) {
		this.metadataSource = metadataSource;
	}

}
