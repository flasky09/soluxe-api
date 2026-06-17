package com.hotel_erp.hotel_erp.modules.currency;

import com.hotel_erp.hotel_erp.shared.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "currencies")
@EqualsAndHashCode(callSuper = true)
public class CurrencyEntity extends BaseEntity {

    @Column(unique = true, nullable = false)
    private String code; // e.g., USD, CNY, EUR

    @Column(nullable = false)
    private String symbol; // e.g., $, ¥, €

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    @Builder.Default
    private BigDecimal exchangeRate = BigDecimal.ONE; // Rate relative to base currency

    @Column(nullable = false)
    @Builder.Default
    private boolean baseCurrency = false;

    @Column(nullable = false)
    @Builder.Default
    private boolean active = true;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public boolean isBaseCurrency() {
        return baseCurrency;
    }

    public void setBaseCurrency(boolean baseCurrency) {
        this.baseCurrency = baseCurrency;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
