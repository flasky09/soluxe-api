package com.hotel_erp.hotel_erp.modules.folio;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/folios/receipts")
@RequiredArgsConstructor
public class FolioReceiptController {

    private final FolioService folioService;

    @GetMapping
    public List<FolioReceiptDTO> getAllReceipts() {
        return folioService.getAllReceipts();
    }

    @GetMapping("/folio/{folioId}")
    public List<FolioReceiptDTO> getReceiptsByFolioId(@PathVariable Long folioId) {
        return folioService.getReceiptsByFolioId(folioId);
    }

    @GetMapping("/payment/{paymentId}")
    public FolioReceiptDTO getReceiptByPaymentId(@PathVariable Long paymentId) {
        return folioService.getReceiptByPaymentId(paymentId);
    }
}
