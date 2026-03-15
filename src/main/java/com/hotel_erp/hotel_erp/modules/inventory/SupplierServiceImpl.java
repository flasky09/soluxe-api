package com.hotel_erp.hotel_erp.modules.inventory;

import com.hotel_erp.hotel_erp.shared.BaseServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class SupplierServiceImpl extends BaseServiceImpl<SupplierEntity, Long, SupplierRepository> implements SupplierService {
    public SupplierServiceImpl(SupplierRepository repository) {
        super(repository);
    }

    @Override
    public void deleteById(Long id) {
        try {
            repository.deleteById(id);
        } catch (Exception e) {
            repository.findById(id).ifPresent(entity -> {
                entity.setActive(false);
                repository.save(entity);
            });
        }
    }
}

