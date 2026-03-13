package com.hotel_erp.hotel_erp.modules.folio;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/folios")
@RequiredArgsConstructor
public class FolioController {

    private final FolioService folioService;
    private final FolioMapper folioMapper;

    @PostMapping("/{id}/charges")
    public FolioChargeDTO addCharge(@PathVariable Long id, @RequestBody FolioChargeDTO chargeDto, @RequestParam Long userId) {
        return folioService.addCharge(id, chargeDto, userId);
    }

    @GetMapping
    public java.util.List<FolioDTO> getAllFolios() {
        return folioService.findAll()
                .stream()
                .map(folioMapper::toDto)
                .collect(java.util.stream.Collectors.toList());
    }

    @GetMapping("/{id}")
    public FolioDTO getFolio(@PathVariable Long id) {
        return folioService.findById(id)
                .map(folioMapper::toDto)
                .orElseThrow(() -> new RuntimeException("Folio not found"));
    }

    @GetMapping("/stay/{stayId}")
    public FolioDTO getFolioByStayId(@PathVariable Long stayId) {
        return folioService.getFolioByStayId(stayId);
    }

    @GetMapping("/{id}/charges")
    public java.util.List<FolioChargeDTO> getChargesByFolioId(@PathVariable Long id) {
        return folioService.getChargesByFolioId(id);
    }

    @GetMapping("/{id}/payments")
    public java.util.List<FolioPaymentDTO> getPaymentsByFolioId(@PathVariable Long id) {
        return folioService.getPaymentsByFolioId(id);
    }

    @GetMapping("/payment-methods")
    public java.util.List<PaymentMethodDTO> getPaymentMethods() {
        return folioService.getAllPaymentMethods();
    }

    @PostMapping("/payment-methods")
    public PaymentMethodDTO createPaymentMethod(@RequestBody PaymentMethodDTO dto) {
        return folioService.createPaymentMethod(dto);
    }

    @PutMapping("/payment-methods/{id}")
    public PaymentMethodDTO updatePaymentMethod(@PathVariable Long id, @RequestBody PaymentMethodDTO dto) {
        return folioService.updatePaymentMethod(id, dto);
    }

    @PostMapping("/{id}/payments")
    public FolioPaymentDTO addPayment(@PathVariable Long id, @RequestBody FolioPaymentDTO paymentDto, @RequestParam Long userId) {
        return folioService.addPayment(id, paymentDto, userId);
    }

    @PostMapping("/{id}/close")
    public FolioDTO closeFolio(@PathVariable Long id, @RequestParam Long userId) {
        return folioService.closeFolio(id, userId);
    }
}
