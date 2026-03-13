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

    @Autowired
    private ChargeTypeRepository chargeTypeRepository;
    
    private FolioEntity testFolio;
    private Long roomChargeTypeId;
    private Long foodChargeTypeId;

    @BeforeEach
    void setUp() {
        // Create charge types
        ChargeTypeEntity roomType = new ChargeTypeEntity();
        roomType.setName("ROOM");
        roomType = chargeTypeRepository.save(roomType);
        roomChargeTypeId = roomType.getId();

        ChargeTypeEntity foodType = new ChargeTypeEntity();
        foodType.setName("FOOD");
        foodType = chargeTypeRepository.save(foodType);
        foodChargeTypeId = foodType.getId();

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
                .chargeTypeId(roomChargeTypeId)
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
        
        // Verify Total Amount calculation (Qty * UnitPrice * (1 + Tax/100))
        assertEquals(new BigDecimal("174.00"), savedCharge.getTotalAmount());
        
        // Verify Folio Total Amount was updated
        FolioEntity updatedFolio = folioRepository.findById(testFolio.getId()).get();
        assertEquals(new BigDecimal("174.00"), updatedFolio.getTotalAmount());
        
        // Add second charge
        FolioChargeDTO foodCharge = FolioChargeDTO.builder()
                .chargeTypeId(foodChargeTypeId)
                .description("Breakfast")
                .quantity(new BigDecimal("2"))
                .unitPrice(new BigDecimal("20.00"))
                .discountPct(BigDecimal.ZERO)
                .taxPct(new BigDecimal("16.00"))
                .build();
                
        folioService.addCharge(testFolio.getId(), foodCharge, userId);
        
        // Verify Total Amount aggregates correctly (174.00 + 46.40 = 220.40)
        FolioEntity finalFolio = folioRepository.findById(testFolio.getId()).get();
        assertEquals(new BigDecimal("220.40"), finalFolio.getTotalAmount());
    }

    @Test
    void testAddChargeToClosedFolioFails() {
        Long userId = 1L;
        testFolio.setStatus(FolioStatus.CLOSED);
        folioRepository.save(testFolio);
        
        FolioChargeDTO chargeDto = FolioChargeDTO.builder()
                .chargeTypeId(roomChargeTypeId)
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
