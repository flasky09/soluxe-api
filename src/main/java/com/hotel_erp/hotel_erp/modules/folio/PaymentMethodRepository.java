package com.hotel_erp.hotel_erp.modules.folio;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PaymentMethodRepository extends JpaRepository<PaymentMethodEntity, Long> {
    List<PaymentMethodEntity> findAllByActiveTrue();
}
