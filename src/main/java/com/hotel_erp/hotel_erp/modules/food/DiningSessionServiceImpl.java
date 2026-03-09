package com.hotel_erp.hotel_erp.modules.food;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hotel_erp.hotel_erp.modules.folio.FolioService;
import com.hotel_erp.hotel_erp.modules.folio.FolioRepository;
import com.hotel_erp.hotel_erp.modules.folio.FolioDTO;
import com.hotel_erp.hotel_erp.modules.folio.FolioChargeDTO;
import com.hotel_erp.hotel_erp.modules.folio.FolioPaymentDTO;
import com.hotel_erp.hotel_erp.modules.folio.ChargeType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DiningSessionServiceImpl implements DiningSessionService {

    private final DiningSessionRepository diningSessionRepository;
    private final FolioService folioService;
    private final FolioRepository folioRepository;

    @Override
    public Optional<DiningSessionEntity> findById(Long id) {
        return diningSessionRepository.findById(id);
    }

    @Override
    public DiningSessionEntity save(DiningSessionEntity session) {
        return diningSessionRepository.save(session);
    }

    @Override
    @Transactional
    public DiningSessionEntity closeSession(Long id) {
        DiningSessionEntity session = diningSessionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("DiningSession not found"));
                
        BigDecimal amount = session.getTotalAmount() != null ? session.getTotalAmount() : BigDecimal.ZERO;
        Long userId = session.getServedBy() != null ? session.getServedBy().getId() : 1L;

        if (amount.compareTo(BigDecimal.ZERO) > 0) {
            String desc = "Restaurant Charge - Session " + id;
            if (session.getBillingType() == BillingType.CHARGE_TO_ROOM && session.getStay() != null) {
                folioRepository.findByStayId(session.getStay().getId()).ifPresent(folio -> {
                    FolioChargeDTO charge = new FolioChargeDTO();
                    charge.setChargeType(ChargeType.FOOD);
                    charge.setDescription(desc);
                    charge.setQuantity(BigDecimal.ONE);
                    charge.setUnitPrice(amount);
                    charge.setTotalAmount(amount);
                    folioService.addCharge(folio.getId(), charge, userId);
                });
            } else if (session.getBillingType() == BillingType.PAY_NOW) {
                FolioDTO folio = folioService.createFolioForDining(id);
                
                FolioChargeDTO charge = new FolioChargeDTO();
                charge.setChargeType(ChargeType.FOOD);
                charge.setDescription(desc);
                charge.setQuantity(BigDecimal.ONE);
                charge.setUnitPrice(amount);
                charge.setTotalAmount(amount);
                folioService.addCharge(folio.getId(), charge, userId);

                FolioPaymentDTO payment = new FolioPaymentDTO();
                payment.setAmount(amount);
                payment.setReferenceNumber("POS-" + id);
                payment.setPaymentMethodId(folioService.getAllPaymentMethods().stream().findFirst().map(p -> p.getId()).orElse(null));
                folioService.addPayment(folio.getId(), payment, userId);
                
                folioService.closeFolio(folio.getId(), userId);
            }
        }

        session.setStatus(session.getBillingType() == BillingType.PAY_NOW ? SessionStatus.PAID : SessionStatus.BILLED);
        session.setClosedAt(LocalDateTime.now());
        return diningSessionRepository.save(session);
    }

    @Override
    public void deleteById(Long id) {
        diningSessionRepository.deleteById(id);
    }

    @Override
    public List<DiningSessionEntity> findAll() {
        return diningSessionRepository.findAll();
    }

    @Override
    public List<DiningSessionEntity> findActive() {
        return diningSessionRepository.findByStatus(SessionStatus.OPEN);
    }
}
