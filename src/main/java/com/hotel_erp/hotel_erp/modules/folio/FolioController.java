package com.hotel_erp.hotel_erp.modules.folio;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/folios")
@RequiredArgsConstructor
public class FolioController {

    private final FolioService folioService;
    private final FolioMapper folioMapper;

    @PostMapping("/{id}/charges")
    public FolioChargeDTO addCharge(@PathVariable("id") Long id, @RequestBody FolioChargeDTO chargeDto, @RequestParam("userId") Long userId) {
        return folioService.addCharge(id, chargeDto, userId);
    }

    @GetMapping
    public List<FolioDTO> getAllFolios() {
        return folioService.findAllDTOs();
    }

    @GetMapping("/{id}")
    public FolioDTO getFolio(@PathVariable("id") Long id) {
        return folioService.findEnrichedDtoById(id)
                .orElseThrow(() -> new RuntimeException("Folio not found"));
    }

    @GetMapping("/stay/{stayId}")
    public FolioDTO getFolioByStayId(@PathVariable("stayId") Long stayId) {
        return folioService.getFolioByStayId(stayId);
    }

    @GetMapping("/reservation/{reservationId}")
    public FolioDTO getFolioByReservationId(@PathVariable("reservationId") Long reservationId) {
        return folioService.getOrCreateFolioForReservation(reservationId);
    }

    @GetMapping("/{id}/charges")
    public List<FolioChargeDTO> getChargesByFolioId(@PathVariable("id") Long id) {
        return folioService.getChargesByFolioId(id);
    }

    @GetMapping("/{id}/payments")
    public List<FolioPaymentDTO> getPaymentsByFolioId(@PathVariable("id") Long id) {
        return folioService.getPaymentsByFolioId(id);
    }

    @GetMapping("/payment-methods")
    public List<PaymentMethodDTO> getPaymentMethods() {
        return folioService.getAllPaymentMethods();
    }

    @PostMapping("/payment-methods")
    public PaymentMethodDTO createPaymentMethod(@RequestBody PaymentMethodDTO dto) {
        return folioService.createPaymentMethod(dto);
    }

    @PutMapping("/payment-methods/{id}")
    public PaymentMethodDTO updatePaymentMethod(@PathVariable("id") Long id, @RequestBody PaymentMethodDTO dto) {
        return folioService.updatePaymentMethod(id, dto);
    }

    @PostMapping("/{id}/payments")
    public FolioPaymentDTO addPayment(@PathVariable("id") Long id, @RequestBody FolioPaymentDTO paymentDto, @RequestParam("userId") Long userId) {
        return folioService.addPayment(id, paymentDto, userId);
    }

    @PostMapping("/{id}/close")
    public FolioDTO closeFolio(@PathVariable("id") Long id, @RequestParam("userId") Long userId) {
        return folioService.closeFolio(id, userId);
    }
}
