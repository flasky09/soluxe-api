package com.hotel_erp.hotel_erp.modules.master;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TenantConfigRepository extends JpaRepository<TenantConfigEntity, Long> {
    Optional<TenantConfigEntity> findBySubdomain(String subdomain);
}
