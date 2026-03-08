package com.hotel_erp.hotel_erp.modules.hotel;

import com.hotel_erp.hotel_erp.shared.BaseServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class HotelServiceImpl extends BaseServiceImpl<HotelEntity, Long, HotelRepository> implements HotelService {
    public HotelServiceImpl(HotelRepository repository) {
        super(repository);
    }

    @Override
    public java.util.List<HotelEntity> findAll() {
        return repository.findAll();
    }
}
