package com.hotel_erp.hotel_erp.config.tenant;

import com.hotel_erp.hotel_erp.modules.master.TenantConfigRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class TenantFilter extends OncePerRequestFilter {

    private final TenantConfigRepository tenantConfigRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // Skip tenant resolution for CORS preflight requests — they carry no auth
        // and must pass through so the CORS filter can respond with the correct headers.
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            filterChain.doFilter(request, response);
            return;
        }

        // Extract subdomain from Host header (e.g. "hotelerp.mflowpos.com")
        String host = request.getServerName(); // strips port automatically

        // Look up the dbKey from Master DB
        String tenantId = tenantConfigRepository.findBySubdomain(host)
                .map(t -> t.getDbKey())
                .orElse("hotel1"); // safe fallback for localhost/unknown

        TenantContext.setCurrentTenant(tenantId);

        try {
            filterChain.doFilter(request, response);
        } finally {
            TenantContext.clear(); // prevents thread pool leakage
        }
    }
}
