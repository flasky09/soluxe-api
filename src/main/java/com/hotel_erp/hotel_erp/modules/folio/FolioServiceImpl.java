package com.hotel_erp.hotel_erp.modules.folio;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hotel_erp.hotel_erp.shared.BaseServiceImpl;

import lombok.RequiredArgsConstructor;

@Service
public class FolioServiceImpl extends BaseServiceImpl<FolioEntity, Long, FolioRepository> implements FolioService {

    private final FolioChargeRepository folioChargeRepository;
    private final FolioPaymentRepository folioPaymentRepository;
    private final PaymentMethodRepository paymentMethodRepository;
    private final FolioMapper folioMapper;
    private final FolioChargeMapper folioChargeMapper;
    private final FolioPaymentMapper folioPaymentMapper;
    private final PaymentMethodMapper paymentMethodMapper;
    private final FolioReceiptRepository folioReceiptRepository;
    private final FolioReceiptMapper folioReceiptMapper;

    public FolioServiceImpl(FolioRepository repository,
                             FolioChargeRepository folioChargeRepository,
                             FolioPaymentRepository folioPaymentRepository,
                             PaymentMethodRepository paymentMethodRepository,
                             FolioReceiptRepository folioReceiptRepository,
                             FolioMapper folioMapper,
                             FolioChargeMapper folioChargeMapper,
                             FolioPaymentMapper folioPaymentMapper,
                             PaymentMethodMapper paymentMethodMapper,
                             FolioReceiptMapper folioReceiptMapper) {
        super(repository);
        this.folioChargeRepository = folioChargeRepository;
        this.folioPaymentRepository = folioPaymentRepository;
        this.paymentMethodRepository = paymentMethodRepository;
        this.folioReceiptRepository = folioReceiptRepository;
        this.folioMapper = folioMapper;
        this.folioChargeMapper = folioChargeMapper;
        this.folioPaymentMapper = folioPaymentMapper;
        this.paymentMethodMapper = paymentMethodMapper;
        this.folioReceiptMapper = folioReceiptMapper;
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
        
        BigDecimal amount = charge.getQuantity().multiply(charge.getUnitPrice());
        charge.setTotalAmount(amount); 
        
        charge = folioChargeRepository.save(charge);
        
        folio.setTotalAmount(folio.getTotalAmount().add(charge.getTotalAmount()));
        repository.save(folio);
        
        return folioChargeMapper.toDto(charge);
    }

    @Override
    public List<PaymentMethodDTO> getAllPaymentMethods() {
        return paymentMethodRepository.findAllByActiveTrue().stream()
                .map(paymentMethodMapper::toDto)
                .collect(java.util.stream.Collectors.toList());
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
        
        payment = folioPaymentRepository.save(payment);
        
        // Generate receipt
        generateReceipt(payment);
        
        // Update folio total amount (decrease balance)
        folio.setTotalAmount(folio.getTotalAmount().subtract(payment.getAmount()));
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
}
