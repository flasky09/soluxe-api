package com.hotel_erp.hotel_erp.modules.room;

import com.hotel_erp.hotel_erp.shared.BaseServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class RoomServiceImpl extends BaseServiceImpl<RoomEntity, Long, RoomRepository> implements RoomService {
    public RoomServiceImpl(RoomRepository repository) {
        super(repository);
    }
}
