package com.hotel_erp.hotel_erp.modules.folio;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.hotel_erp.hotel_erp.modules.guest.GuestRepository;
import com.hotel_erp.hotel_erp.modules.reservation.ReservationRepository;
import com.hotel_erp.hotel_erp.modules.room.RoomRepository;
import com.hotel_erp.hotel_erp.modules.stay.StayRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hotel_erp.hotel_erp.shared.BaseServiceImpl;


@Service
public class FolioServiceImpl extends BaseServiceImpl<FolioEntity, Long, FolioRepository> implements FolioService {

    private final FolioChargeRepository folioChargeRepository;
    private final FolioPaymentRepository folioPaymentRepository;
    private final PaymentMethodRepository paymentMethodRepository;
    private final FolioReceiptRepository folioReceiptRepository;
    private final ChargeTypeRepository chargeTypeRepository;
    private final FolioMapper folioMapper;
    private final FolioChargeMapper folioChargeMapper;
    private final FolioPaymentMapper folioPaymentMapper;
    private final PaymentMethodMapper paymentMethodMapper;
    private final FolioReceiptMapper folioReceiptMapper;
    
    private final StayRepository stayRepository;
    private final ReservationRepository reservationRepository;
    private final GuestRepository guestRepository;
    private final RoomRepository roomRepository;

    public FolioServiceImpl(FolioRepository repository,
                             FolioChargeRepository folioChargeRepository,
                             FolioPaymentRepository folioPaymentRepository,
                             PaymentMethodRepository paymentMethodRepository,
                             FolioReceiptRepository folioReceiptRepository,
                             ChargeTypeRepository chargeTypeRepository,
                             FolioMapper folioMapper,
                             FolioChargeMapper folioChargeMapper,
                             FolioPaymentMapper folioPaymentMapper,
                             PaymentMethodMapper paymentMethodMapper,
                             FolioReceiptMapper folioReceiptMapper,
                             StayRepository stayRepository,
                             ReservationRepository reservationRepository,
                             GuestRepository guestRepository,
                             RoomRepository roomRepository) {
        super(repository);
        this.folioChargeRepository = folioChargeRepository;
        this.folioPaymentRepository = folioPaymentRepository;
        this.paymentMethodRepository = paymentMethodRepository;
        this.folioReceiptRepository = folioReceiptRepository;
        this.chargeTypeRepository = chargeTypeRepository;
        this.folioMapper = folioMapper;
        this.folioChargeMapper = folioChargeMapper;
        this.folioPaymentMapper = folioPaymentMapper;
        this.paymentMethodMapper = paymentMethodMapper;
        this.folioReceiptMapper = folioReceiptMapper;
        this.stayRepository = stayRepository;
        this.reservationRepository = reservationRepository;
        this.guestRepository = guestRepository;
        this.roomRepository = roomRepository;
    }

    @Override
    @Transactional
    public FolioDTO createFolioForStay(Long stayId) {
        FolioEntity folio = new FolioEntity();
        folio.setStayId(stayId);
        folio.setFolioType(FolioType.STAY);
        folio.setStatus(FolioStatus.OPEN);
        folio.setOpenedAt(LocalDateTime.now());
        folio.setTotalAmount(BigDecimal.ZERO);
        
        return folioMapper.toDto(repository.save(folio));
    }

    @Override
    @Transactional
    public FolioDTO createFolioForReservation(Long reservationId) {
        FolioEntity folio = new FolioEntity();
        folio.setReservationId(reservationId);
        folio.setFolioType(FolioType.RESERVATION);
        folio.setStatus(FolioStatus.OPEN);
        folio.setOpenedAt(LocalDateTime.now());
        folio.setTotalAmount(BigDecimal.ZERO);
        
        return folioMapper.toDto(repository.save(folio));
    }

    @Override
    @Transactional
    public FolioDTO getOrCreateFolioForReservation(Long reservationId) {
        return repository.findByReservationId(reservationId)
                .map(folioMapper::toDto)
                .orElseGet(() -> createFolioForReservation(reservationId));
    }

    @Override
    @Transactional
    public FolioDTO linkReservationFolioToStay(Long reservationId, Long stayId) {
        FolioEntity folio = repository.findByReservationId(reservationId)
                .orElseThrow(() -> new RuntimeException("Folio not found for Reservation ID: " + reservationId));
        
        folio.setStayId(stayId);
        folio.setFolioType(FolioType.STAY);
        // We keep reservationId for audit trail or linking back if needed
        return folioMapper.toDto(repository.save(folio));
    }

    @Override
    @Transactional
    public FolioDTO createFolioForDining(Long sessionId) {
        FolioEntity folio = new FolioEntity();
        folio.setDiningSessionId(sessionId);
        folio.setFolioType(FolioType.DINING);
        folio.setStatus(FolioStatus.OPEN);
        folio.setOpenedAt(LocalDateTime.now());
        folio.setTotalAmount(BigDecimal.ZERO);
        
        return folioMapper.toDto(repository.save(folio));
    }

    @Override
    @Transactional
    public FolioChargeDTO addCharge(Long folioId, FolioChargeDTO chargeDto, Long userId) {
        FolioEntity folio = repository.findById(folioId)
                .orElseThrow(() -> new RuntimeException("Folio not found"));

        if (folio.getStatus() != FolioStatus.OPEN) {
            throw new RuntimeException("Folio is closed");
        }

        FolioChargeEntity charge = folioChargeMapper.toEntity(chargeDto);
        charge.setFolioId(folioId);
        charge.setChargedAt(LocalDateTime.now());
        charge.setAddedBy(userId);

        if (chargeDto.getChargeTypeId() != null && chargeDto.getChargeTypeId() > 0) {
            chargeTypeRepository.findById(chargeDto.getChargeTypeId())
                    .ifPresent(charge::setChargeType);
        }
        
        BigDecimal quantity = charge.getQuantity() != null ? charge.getQuantity() : BigDecimal.ONE;
        BigDecimal unitPrice = charge.getUnitPrice() != null ? charge.getUnitPrice() : BigDecimal.ZERO;
        BigDecimal discountPct = charge.getDiscountPct() != null ? charge.getDiscountPct() : BigDecimal.ZERO;
        BigDecimal taxPct = charge.getTaxPct() != null ? charge.getTaxPct() : BigDecimal.ZERO;

        // Subtotal = Quantity * UnitPrice * (1 - Discount/100)
        BigDecimal discountFactor = BigDecimal.ONE.subtract(discountPct.divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP));
        BigDecimal subtotal = quantity.multiply(unitPrice).multiply(discountFactor);

        // Total = Subtotal * (1 + Tax/100)
        BigDecimal taxFactor = BigDecimal.ONE.add(taxPct.divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP));
        BigDecimal totalAmount = subtotal.multiply(taxFactor).setScale(2, RoundingMode.HALF_UP);

        charge.setQuantity(quantity);
        charge.setUnitPrice(unitPrice);
        charge.setDiscountPct(discountPct);
        charge.setTaxPct(taxPct);
        charge.setTotalAmount(totalAmount);
        
        charge = folioChargeRepository.save(charge);

        // BUG 6 FIX: Recalculate totalAmount from DB aggregates to avoid race condition
        // when multiple charges are posted concurrently.
        BigDecimal totalCharges = folioChargeRepository.sumTotalByFolioId(folioId);
        BigDecimal totalPayments = folioPaymentRepository.sumAmountByFolioId(folioId);
        folio.setTotalAmount(totalCharges.subtract(totalPayments).setScale(2, RoundingMode.HALF_UP));
        repository.save(folio);

        return folioChargeMapper.toDto(charge);
    }

    @Override
    public List<PaymentMethodDTO> getAllPaymentMethods() {
        return paymentMethodRepository.findAllByActiveTrue().stream()
                .map(paymentMethodMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public PaymentMethodDTO createPaymentMethod(PaymentMethodDTO dto) {
        PaymentMethodEntity entity = paymentMethodMapper.toEntity(dto);
        entity.setActive(true);
        return paymentMethodMapper.toDto(paymentMethodRepository.save(entity));
    }

    @Override
    @Transactional
    public PaymentMethodDTO updatePaymentMethod(Long id, PaymentMethodDTO dto) {
        PaymentMethodEntity entity = paymentMethodRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment method not found"));
        
        entity.setName(dto.getName());
        entity.setActive(dto.isActive());
        
        return paymentMethodMapper.toDto(paymentMethodRepository.save(entity));
    }

    @Override
    @Transactional
    public FolioPaymentDTO addPayment(Long folioId, FolioPaymentDTO paymentDto, Long userId) {
        FolioEntity folio = repository.findById(folioId)
                .orElseThrow(() -> new RuntimeException("Folio not found"));

        if (folio.getStatus() != FolioStatus.OPEN) {
            throw new RuntimeException("Folio is closed");
        }

        FolioPaymentEntity payment = folioPaymentMapper.toEntity(paymentDto);
        payment.setFolioId(folioId);
        payment.setRecordedAt(LocalDateTime.now());
        payment.setRecordedBy(userId);
        
        // BUG 5 FIX: Prevent overpayment — reject if payment exceeds outstanding balance.
        if (payment.getAmount().compareTo(folio.getTotalAmount()) > 0) {
            throw new RuntimeException(
                "Payment amount (" + payment.getAmount() + ") exceeds outstanding balance (" + folio.getTotalAmount() + ")"
            );
        }

        payment = folioPaymentRepository.save(payment);

        // Generate receipt
        generateReceipt(payment);

        // BUG 6 FIX: Recalculate totalAmount from DB aggregates to avoid in-memory race condition.
        BigDecimal totalCharges = folioChargeRepository.sumTotalByFolioId(folioId);
        BigDecimal totalPayments = folioPaymentRepository.sumAmountByFolioId(folioId);
        folio.setTotalAmount(totalCharges.subtract(totalPayments).setScale(2, RoundingMode.HALF_UP));
        repository.save(folio);

        return folioPaymentMapper.toDto(payment);
    }

    @Override
    @Transactional
    public FolioDTO closeFolio(Long folioId, Long userId) {
        FolioEntity folio = repository.findById(folioId)
                .orElseThrow(() -> new RuntimeException("Folio not found"));

        if (folio.getStatus() != FolioStatus.OPEN) {
            throw new RuntimeException("Folio is already closed");
        }

        // In a real ERP, we'd check if balance is exactly zero.
        // For now, we allow closing if the totalAmount (balance) is <= 0
        if (folio.getTotalAmount().compareTo(BigDecimal.ZERO) > 0) {
            throw new RuntimeException("Cannot close folio with outstanding balance: " + folio.getTotalAmount());
        }

        folio.setStatus(FolioStatus.CLOSED);
        folio.setClosedAt(LocalDateTime.now());
        
        return folioMapper.toDto(repository.save(folio));
    }

    @Override
    @Transactional
    public void voidFolio(Long folioId, Long userId) {
        // BUG 4 FIX: Properly void a folio by posting a credit reversal for each charge
        // and then closing the folio so it no longer shows as outstanding.
        FolioEntity folio = repository.findById(folioId)
                .orElseThrow(() -> new RuntimeException("Folio not found: " + folioId));

        if (folio.getStatus() != FolioStatus.OPEN) {
            return; // Already closed/voided, nothing to do
        }

        // Post a single reversal charge that zeroes the balance
        BigDecimal currentBalance = folio.getTotalAmount();
        if (currentBalance.compareTo(BigDecimal.ZERO) > 0) {
            FolioChargeEntity reversal = new FolioChargeEntity();
            reversal.setFolioId(folioId);
            reversal.setDescription("Void Reversal — Stay Voided");
            reversal.setQuantity(BigDecimal.ONE);
            reversal.setUnitPrice(currentBalance.negate());
            reversal.setDiscountPct(BigDecimal.ZERO);
            reversal.setTaxPct(BigDecimal.ZERO);
            reversal.setTotalAmount(currentBalance.negate());
            reversal.setChargedAt(java.time.LocalDateTime.now());
            reversal.setAddedBy(userId);
            folioChargeRepository.save(reversal);
        }

        folio.setTotalAmount(BigDecimal.ZERO);
        folio.setStatus(FolioStatus.CLOSED);
        folio.setClosedAt(java.time.LocalDateTime.now());
        repository.save(folio);
    }

    @Override
    public List<FolioReceiptDTO> getAllReceipts() {
        return folioReceiptRepository.findAll().stream()
                .map(folioReceiptMapper::toDto)
                .toList();
    }

    @Override
    public FolioReceiptDTO getReceiptByPaymentId(Long paymentId) {
        return folioReceiptRepository.findByPaymentId(paymentId)
                .map(folioReceiptMapper::toDto)
                .orElse(null);
    }

    @Override
    public List<FolioReceiptDTO> getReceiptsByFolioId(Long folioId) {
        return folioReceiptRepository.findAllByFolioId(folioId).stream()
                .map(folioReceiptMapper::toDto)
                .toList();
    }

    private void generateReceipt(FolioPaymentEntity payment) {
        FolioReceiptEntity receipt = new FolioReceiptEntity();
        receipt.setPaymentId(payment.getId());
        receipt.setFolioId(payment.getFolioId());
        receipt.setAmount(payment.getAmount());
        receipt.setIssuedAt(LocalDateTime.now());
        receipt.setIssuedBy(payment.getRecordedBy());
        receipt.setReceiptNumber("REC-" + System.currentTimeMillis() + "-" + payment.getId());
        
        folioReceiptRepository.save(receipt);
    }

    public List<FolioDTO> findAllDTOs() {
        return repository.findAll().stream()
                .map(folioMapper::toDto)
                .map(this::enrichFolioDTO)
                .collect(Collectors.toList());
    }

    public Optional<FolioDTO> findEnrichedDtoById(Long id) {
        return repository.findById(id)
                .map(folioMapper::toDto)
                .map(this::enrichFolioDTO);
    }

    @Override
    public FolioDTO getFolioByStayId(Long stayId) {
        return repository.findByStayId(stayId)
                .map(folioMapper::toDto)
                .map(this::enrichFolioDTO)
                .orElseThrow(() -> new RuntimeException("Folio not found for Stay ID: " + stayId));
    }

    private FolioDTO enrichFolioDTO(FolioDTO dto) {
        if (dto.getStayId() != null) {
            stayRepository.findById(dto.getStayId()).ifPresent(stay -> {
                guestRepository.findById(stay.getGuestId()).ifPresent(guest -> dto.setGuestName(guest.getFullName()));
                roomRepository.findById(stay.getRoomId()).ifPresent(room -> dto.setRoomNumber(room.getRoomNumber()));
            });
        } else if (dto.getReservationId() != null) {
            reservationRepository.findById(dto.getReservationId()).ifPresent(res -> {
                guestRepository.findById(res.getGuestId()).ifPresent(guest -> dto.setGuestName(guest.getFullName()));
            });
        }
        return dto;
    }

    @Override
    public List<FolioChargeDTO> getChargesByFolioId(Long folioId) {
        return folioChargeRepository.findAllByFolioId(folioId).stream()
                .map(folioChargeMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<FolioPaymentDTO> getPaymentsByFolioId(Long folioId) {
        return folioPaymentRepository.findAllByFolioId(folioId).stream()
                .map(folioPaymentMapper::toDto)
                .collect(Collectors.toList());
    }
}
