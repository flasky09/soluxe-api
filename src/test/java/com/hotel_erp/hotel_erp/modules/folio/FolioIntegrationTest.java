package com.hotel_erp.hotel_erp.modules.folio;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class FolioIntegrationTest {

    @Autowired
    private FolioService folioService;

    @Autowired
    private FolioRepository folioRepository;

    @Autowired
    private FolioChargeRepository folioChargeRepository;
    
    private FolioEntity testFolio;

    @BeforeEach
    void setUp() {
        // Create an open Folio
        testFolio = new FolioEntity();
        testFolio.setStayId(1L);
        testFolio.setFolioType(FolioType.STAY);
        testFolio.setStatus(FolioStatus.OPEN);
        testFolio.setOpenedAt(LocalDateTime.now());
        testFolio.setTotalAmount(BigDecimal.ZERO);
        testFolio = folioRepository.save(testFolio);
    }

    @Test
    void testAddChargeFlow() {
        Long userId = 1L;
        
        // Prepare Charge
        FolioChargeDTO chargeDto = FolioChargeDTO.builder()
                .chargeType(ChargeType.ROOM)
                .description("Room charge night 1")
                .quantity(BigDecimal.ONE)
                .unitPrice(new BigDecimal("150.00"))
                .discountPct(BigDecimal.ZERO)
                .taxPct(new BigDecimal("16.00"))
                .build();

        // Add Charge
        FolioChargeDTO savedCharge = folioService.addCharge(testFolio.getId(), chargeDto, userId);

        assertNotNull(savedCharge);
        assertNotNull(savedCharge.getId());
        assertEquals(testFolio.getId(), savedCharge.getFolioId());
        
        // Verify Total Amount calculation (Qty * UnitPrice for now)
        assertEquals(new BigDecimal("150.00"), savedCharge.getTotalAmount());
        
        // Verify Folio Total Amount was updated
        FolioEntity updatedFolio = folioRepository.findById(testFolio.getId()).get();
        assertEquals(new BigDecimal("150.00"), updatedFolio.getTotalAmount());
        
        // Add second charge
        FolioChargeDTO foodCharge = FolioChargeDTO.builder()
                .chargeType(ChargeType.FOOD)
                .description("Breakfast")
                .quantity(new BigDecimal("2"))
                .unitPrice(new BigDecimal("20.00"))
                .discountPct(BigDecimal.ZERO)
                .taxPct(new BigDecimal("16.00"))
                .build();
                
        folioService.addCharge(testFolio.getId(), foodCharge, userId);
        
        // Verify Total Amount aggregates correctly (150 + 40 = 190)
        FolioEntity finalFolio = folioRepository.findById(testFolio.getId()).get();
        assertEquals(new BigDecimal("190.00"), finalFolio.getTotalAmount());
    }

    @Test
    void testAddChargeToClosedFolioFails() {
        Long userId = 1L;
        testFolio.setStatus(FolioStatus.CLOSED);
        folioRepository.save(testFolio);
        
        FolioChargeDTO chargeDto = FolioChargeDTO.builder()
                .chargeType(ChargeType.ROOM)
                .description("Late charge")
                .quantity(BigDecimal.ONE)
                .unitPrice(new BigDecimal("50.00"))
                .build();

        Exception exception = assertThrows(RuntimeException.class, () -> {
            folioService.addCharge(testFolio.getId(), chargeDto, userId);
        });
        
        assertEquals("Folio is closed", exception.getMessage());
    }
}
