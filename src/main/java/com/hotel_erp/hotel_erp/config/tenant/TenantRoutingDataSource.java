package com.hotel_erp.hotel_erp.config.tenant;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class TenantRoutingDataSource extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        // Return the current tenant ID from the context (e.g. "hotel1")
        // If null, it will fall back to the defaultTargetDataSource
        return TenantContext.getCurrentTenant();
    }
}
