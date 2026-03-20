package com.hotel_erp.hotel_erp.modules.room;

import java.util.List;
import java.util.Optional;

public interface RoomTypeService {

    List<RoomTypeEntity> findAll();

    Optional<RoomTypeEntity> findById(Long id);

    RoomTypeEntity save(RoomTypeEntity roomType);

    void deleteById(Long id);

}
