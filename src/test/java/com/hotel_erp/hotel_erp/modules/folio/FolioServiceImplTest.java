package com.hotel_erp.hotel_erp.modules.folio;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.Optional;

import com.hotel_erp.hotel_erp.modules.activity.ActivityLogService;
import com.hotel_erp.hotel_erp.modules.guest.GuestRepository;
import com.hotel_erp.hotel_erp.modules.reservation.ReservationRepository;
import com.hotel_erp.hotel_erp.modules.room.RoomRepository;
import com.hotel_erp.hotel_erp.modules.stay.StayRepository;
import com.hotel_erp.hotel_erp.modules.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class FolioServiceImplTest {

    @Mock
    private FolioRepository folioRepository;
    @Mock
    private FolioChargeRepository folioChargeRepository;
    @Mock
    private FolioPaymentRepository folioPaymentRepository;
    @Mock
    private PaymentMethodRepository paymentMethodRepository;
    @Mock
    private FolioReceiptRepository folioReceiptRepository;
    @Mock
    private ChargeTypeRepository chargeTypeRepository;
    @Mock
    private FolioMapper folioMapper;
    @Mock
    private FolioChargeMapper folioChargeMapper;
    @Mock
    private FolioPaymentMapper folioPaymentMapper;
    @Mock
    private PaymentMethodMapper paymentMethodMapper;
    @Mock
    private FolioReceiptMapper folioReceiptMapper;
    @Mock
    private StayRepository stayRepository;
    @Mock
    private ReservationRepository reservationRepository;
    @Mock
    private GuestRepository guestRepository;
    @Mock
    private RoomRepository roomRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ActivityLogService activityLogService;

    @InjectMocks
    private FolioServiceImpl folioService;

    @BeforeEach
    void setUp() {
        // @InjectMocks cannot reach @Autowired fields declared in superclasses.
        // Inject the mock manually into BaseServiceImpl.userRepository.
        ReflectionTestUtils.setField(folioService, com.hotel_erp.hotel_erp.shared.BaseServiceImpl.class, "userRepository", userRepository, com.hotel_erp.hotel_erp.modules.user.UserRepository.class);
        // Stub validateUser so any Long userId is treated as valid
        when(userRepository.existsById(any())).thenReturn(true);
    }

    @Test
    void testAddCharge_CalculatesCorrectTotal() {
        // Arrange
        Long folioId = 1L;
        Long userId = 1L;
        FolioEntity folio = new FolioEntity();
        folio.setId(folioId);
        folio.setStatus(FolioStatus.OPEN);
        folio.setTotalAmount(BigDecimal.ZERO);

        FolioChargeDTO chargeDto = new FolioChargeDTO();
        chargeDto.setQuantity(new BigDecimal("2"));
        chargeDto.setUnitPrice(new BigDecimal("100"));
        chargeDto.setDiscountPct(new BigDecimal("10")); // 10% discount
        chargeDto.setTaxPct(new BigDecimal("5"));      // 5% tax

        FolioChargeEntity chargeEntity = new FolioChargeEntity();
        chargeEntity.setQuantity(chargeDto.getQuantity());
        chargeEntity.setUnitPrice(chargeDto.getUnitPrice());
        chargeEntity.setDiscountPct(chargeDto.getDiscountPct());
        chargeEntity.setTaxPct(chargeDto.getTaxPct());

        when(folioRepository.findById(folioId)).thenReturn(Optional.of(folio));
        when(folioChargeMapper.toEntity(any(FolioChargeDTO.class))).thenReturn(chargeEntity);
        when(folioChargeRepository.save(any(FolioChargeEntity.class))).thenAnswer(i -> i.getArgument(0));
        // Stub aggregate queries used by addCharge to recalculate folio balance
        when(folioChargeRepository.sumTotalByFolioId(folioId)).thenReturn(new BigDecimal("189.00"));
        when(folioPaymentRepository.sumAmountByFolioId(folioId)).thenReturn(BigDecimal.ZERO);

        // Act
        folioService.addCharge(folioId, chargeDto, userId);

        // Assert
        // Calculation: 2 * 100 * (1 - 0.1) = 180
        // 180 * (1 + 0.05) = 189.00
        assertEquals(new BigDecimal("189.00"), chargeEntity.getTotalAmount());
        assertEquals(new BigDecimal("189.00"), folio.getTotalAmount());
        verify(folioRepository).save(folio);
    }

    @Test
    void testAddPayment_DecreasesBalance() {
        // Arrange
        Long folioId = 1L;
        Long userId = 1L;
        FolioEntity folio = new FolioEntity();
        folio.setId(folioId);
        folio.setStatus(FolioStatus.OPEN);
        folio.setTotalAmount(new BigDecimal("500"));

        FolioPaymentDTO paymentDto = new FolioPaymentDTO();
        paymentDto.setAmount(new BigDecimal("200"));
        paymentDto.setReferenceNumber("REF123");
        paymentDto.setPaymentMethodId(1L);

        FolioPaymentEntity paymentEntity = new FolioPaymentEntity();
        paymentEntity.setAmount(paymentDto.getAmount());
        paymentEntity.setReferenceNumber(paymentDto.getReferenceNumber());
        paymentEntity.setFolioId(folioId);

        PaymentMethodEntity pMethod = new PaymentMethodEntity();
        pMethod.setId(1L);
        pMethod.setName("Cash");

        when(folioRepository.findById(folioId)).thenReturn(Optional.of(folio));
        when(paymentMethodRepository.findById(1L)).thenReturn(Optional.of(pMethod));
        when(folioPaymentMapper.toEntity(any(FolioPaymentDTO.class))).thenReturn(paymentEntity);
        when(folioPaymentRepository.save(any(FolioPaymentEntity.class))).thenAnswer(i -> i.getArgument(0));
        // Stub aggregate queries: 500 in charges, 200 paid -> balance 300
        when(folioChargeRepository.sumTotalByFolioId(folioId)).thenReturn(new BigDecimal("500"));
        when(folioPaymentRepository.sumAmountByFolioId(folioId)).thenReturn(new BigDecimal("200"));

        // Act
        folioService.addPayment(folioId, paymentDto, userId);

        // Assert
        assertEquals(new BigDecimal("300.00"), folio.getTotalAmount());
        assertEquals("REF123", paymentEntity.getReferenceNumber());
        verify(folioReceiptRepository).save(any(FolioReceiptEntity.class));
    }

    @Test
    void testCloseFolio_FailsIfBalancePositive() {
        // Arrange
        Long folioId = 1L;
        FolioEntity folio = new FolioEntity();
        folio.setId(folioId);
        folio.setStatus(FolioStatus.OPEN);
        folio.setTotalAmount(new BigDecimal("10.50"));

        when(folioRepository.findById(folioId)).thenReturn(Optional.of(folio));

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> {
            folioService.closeFolio(folioId, 1L);
        });
        assertTrue(exception.getMessage().contains("outstanding balance"));
    }
}
