package com.hotel_erp.hotel_erp.modules.inventory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface InventoryUnitRepository extends JpaRepository<InventoryUnitEntity, Long> {
    Optional<InventoryUnitEntity> findByName(String name);
}
