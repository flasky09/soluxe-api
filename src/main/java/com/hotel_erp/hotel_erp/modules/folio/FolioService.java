package com.hotel_erp.hotel_erp.modules.folio;

import com.hotel_erp.hotel_erp.shared.BaseService;

import java.util.List;
import java.util.Optional;

public interface FolioService extends BaseService<FolioEntity, Long> {

    FolioDTO createFolioForStay(Long stayId);

    FolioDTO createFolioForReservation(Long reservationId);

    FolioDTO getOrCreateFolioForReservation(Long reservationId);

    FolioDTO linkReservationFolioToStay(Long reservationId, Long stayId);

    FolioDTO createFolioForDining(Long sessionId);

    FolioChargeDTO addCharge(Long folioId, FolioChargeDTO chargeDto, Long userId);

    List<PaymentMethodDTO> getAllPaymentMethods();

    PaymentMethodDTO createPaymentMethod(PaymentMethodDTO dto);

    PaymentMethodDTO updatePaymentMethod(Long id, PaymentMethodDTO dto);

    FolioPaymentDTO addPayment(Long folioId, FolioPaymentDTO paymentDto, Long userId);

    FolioDTO closeFolio(Long folioId, Long userId);

    void voidFolio(Long folioId, Long userId);

    List<FolioReceiptDTO> getAllReceipts();

    FolioReceiptDTO getReceiptByPaymentId(Long paymentId);

    List<FolioReceiptDTO> getReceiptsByFolioId(Long folioId);

    FolioDTO getFolioByStayId(Long stayId);

    List<FolioChargeDTO> getChargesByFolioId(Long folioId);

    List<FolioPaymentDTO> getPaymentsByFolioId(Long folioId);

    List<FolioDTO> findAllDTOs();

    Optional<FolioDTO> findEnrichedDtoById(Long id);

}
