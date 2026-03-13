package com.hotel_erp.hotel_erp.modules.guest;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface IdTypeRepository extends JpaRepository<IdTypeEntity, Long> {
    Optional<IdTypeEntity> findByName(String name);
}
