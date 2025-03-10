package com.raybiztech.leavemanagement.quartz;

import org.apache.log4j.Logger;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Component;

import com.raybiztech.leavemanagement.service.ClearCacheService;

@Component("clearReportsCache")
public class ClearReportsCache {
	Logger logger = Logger.getLogger(ClearCacheService.class);

	@Caching(evict = {
			@CacheEvict(value = "timeInOfficeCache", allEntries = true),
			@CacheEvict(value = "hiveCache", allEntries = true) })
	public void clearCache() {
		logger.info("cleared reports cache");

	}

}
