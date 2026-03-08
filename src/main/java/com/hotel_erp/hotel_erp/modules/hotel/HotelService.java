package com.hotel_erp.hotel_erp.modules.hotel;

import com.hotel_erp.hotel_erp.shared.BaseService;
import java.util.List;

public interface HotelService extends BaseService<HotelEntity, Long> {
    List<HotelEntity> findAll();
}
