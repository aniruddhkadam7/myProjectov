package com.raybiztech.multitenancy;

import org.springframework.util.Assert;
/**
*
* @author sowmya
*/

public class TenantContextHolder {

	private static final ThreadLocal<TenantTypes> contextHolder = 
            new ThreadLocal<TenantTypes>();
	
   public static void setTenantType(TenantTypes tenantType) {
      Assert.notNull(tenantType, "tenantType cannot be null");
      contextHolder.set(tenantType);
   }

   public static TenantTypes getTenantType() {
      return (TenantTypes) contextHolder.get();
   }

   public static void clearTenantType() {
      contextHolder.remove();
   }
	
}
