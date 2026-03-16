package com.hotel_erp.hotel_erp.modules.guest;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IdentityTypeRepository extends JpaRepository<IdentityTypeEntity, Long> {
}
