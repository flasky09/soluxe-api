package com.hotel_erp.hotel_erp.modules.folio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ChargeTypeRepository extends JpaRepository<ChargeTypeEntity, Long> {
    Optional<ChargeTypeEntity> findByName(String name);
}
