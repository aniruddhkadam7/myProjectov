package com.raybiztech.leavemanagement.service;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Service;

import com.raybiztech.leavemanagement.quartz.ClearReportsCache;

@Service("clearCacheService")
public class ClearCacheService extends QuartzJobBean {

	private ClearReportsCache clearReportsCache;

	public ClearReportsCache getClearReportsCache() {
		return clearReportsCache;
	}

	public void setClearReportsCache(ClearReportsCache clearReportsCache) {
		this.clearReportsCache = clearReportsCache;
	}

	@Override
	protected void executeInternal(JobExecutionContext arg0)
			throws JobExecutionException {
		clearReportsCache.clearCache();

	}

}
