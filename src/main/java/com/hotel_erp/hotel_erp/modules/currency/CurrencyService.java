package com.hotel_erp.hotel_erp.modules.currency;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CurrencyService {

    private final CurrencyRepository currencyRepository;

    public List<CurrencyEntity> getAllCurrencies() {
        return currencyRepository.findAll();
    }

    public List<CurrencyEntity> getActiveCurrencies() {
        return currencyRepository.findAllByActiveTrue();
    }

    @Transactional
    public CurrencyEntity createCurrency(CurrencyEntity currency) {
        if (currency.isBaseCurrency()) {
            // Ensure only one base currency
            currencyRepository.findByBaseCurrencyTrue().ifPresent(c -> {
                c.setBaseCurrency(false);
                currencyRepository.save(c);
            });
        }
        return currencyRepository.save(currency);
    }

    @Transactional
    public CurrencyEntity updateCurrency(Long id, CurrencyEntity details) {
        CurrencyEntity currency = currencyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Currency not found"));

        currency.setName(details.getName());
        currency.setSymbol(details.getSymbol());
        currency.setExchangeRate(details.getExchangeRate());
        currency.setActive(details.isActive());

        if (details.isBaseCurrency() && !currency.isBaseCurrency()) {
            currencyRepository.findByBaseCurrencyTrue().ifPresent(c -> {
                c.setBaseCurrency(false);
                currencyRepository.save(c);
            });
            currency.setBaseCurrency(true);
        }

        return currencyRepository.save(currency);
    }

    public void deleteCurrency(Long id) {
        currencyRepository.deleteById(id);
    }
}
