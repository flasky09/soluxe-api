package com.hotel_erp.hotel_erp.modules.currency;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/currencies")
@RequiredArgsConstructor
public class CurrencyController {

    private final CurrencyService currencyService;

    @GetMapping
    public ResponseEntity<List<CurrencyEntity>> getAllCurrencies() {
        return ResponseEntity.ok(currencyService.getAllCurrencies());
    }

    @GetMapping("/active")
    public ResponseEntity<List<CurrencyEntity>> getActiveCurrencies() {
        return ResponseEntity.ok(currencyService.getActiveCurrencies());
    }

    @PostMapping
    public ResponseEntity<CurrencyEntity> createCurrency(@RequestBody CurrencyEntity currency) {
        return ResponseEntity.ok(currencyService.createCurrency(currency));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CurrencyEntity> updateCurrency(@PathVariable Long id, @RequestBody CurrencyEntity currency) {
        return ResponseEntity.ok(currencyService.updateCurrency(id, currency));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCurrency(@PathVariable Long id) {
        currencyService.deleteCurrency(id);
        return ResponseEntity.noContent().build();
    }
}
