package com.hotel_erp.hotel_erp.modules.inventory.purchaseorder;

import com.hotel_erp.hotel_erp.modules.inventory.SupplierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/purchase-orders")
@RequiredArgsConstructor
public class PurchaseOrderController {
    private final PurchaseOrderService purchaseOrderService;
    private final PurchaseOrderMapper purchaseOrderMapper;
    private final SupplierRepository supplierRepository;

    @GetMapping
    public List<PurchaseOrderDTO> getAll() {
        return purchaseOrderService.findAll().stream()
                .map(purchaseOrderMapper::toDto)
                .toList();
    }

    @PostMapping
    public PurchaseOrderDTO create(@RequestBody PurchaseOrderDTO dto) {
        PurchaseOrderEntity entity = purchaseOrderMapper.toEntity(dto);
        return purchaseOrderMapper.toDto(purchaseOrderService.save(entity));
    }

    @GetMapping("/{id}")
    public PurchaseOrderDTO getById(@PathVariable Long id) {
        return purchaseOrderService.findById(id)
                .map(purchaseOrderMapper::toDto)
                .orElseThrow(() -> new RuntimeException("PurchaseOrder not found"));
    }

    @PutMapping("/{id}")
    public PurchaseOrderDTO update(@PathVariable Long id, @RequestBody PurchaseOrderDTO dto) {
        PurchaseOrderEntity entity = purchaseOrderService.findById(id)
                .orElseThrow(() -> new RuntimeException("PurchaseOrder not found"));
        
        if (dto.getSupplierId() != null) {
            entity.setSupplier(supplierRepository.findById(dto.getSupplierId())
                .orElseThrow(() -> new RuntimeException("Supplier not found")));
        }
        if (dto.getExpectedDate() != null) {
            entity.setExpectedDate(dto.getExpectedDate());
        }
        if (dto.getStatus() != null) {
            entity.setStatus(dto.getStatus());
        }
        if (dto.getNotes() != null) {
            entity.setNotes(dto.getNotes());
        }
        
        return purchaseOrderMapper.toDto(purchaseOrderService.save(entity));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        purchaseOrderService.deleteById(id);
    }
}
