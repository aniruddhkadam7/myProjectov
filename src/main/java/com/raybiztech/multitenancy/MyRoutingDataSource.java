package com.raybiztech.multitenancy;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
*
* @author sowmya
*/

public class MyRoutingDataSource extends AbstractRoutingDataSource{

	@Override
	protected Object determineCurrentLookupKey() {
		
		TenantTypes tenant = TenantContextHolder.getTenantType();
		//System.out.println("tenant in routing:"+tenant);
		
		return TenantContextHolder.getTenantType();
	}
}
