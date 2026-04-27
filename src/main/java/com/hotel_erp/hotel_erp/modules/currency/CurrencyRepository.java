package com.hotel_erp.hotel_erp.modules.currency;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CurrencyRepository extends JpaRepository<CurrencyEntity, Long> {
    Optional<CurrencyEntity> findByCode(String code);

    List<CurrencyEntity> findAllByActiveTrue();

    Optional<CurrencyEntity> findByBaseCurrencyTrue();
}
