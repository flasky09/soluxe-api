package com.hotel_erp.hotel_erp.modules.room;

import com.hotel_erp.hotel_erp.shared.BaseServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class RoomServiceImpl extends BaseServiceImpl<RoomEntity, Long, RoomRepository> implements RoomService {
    
    private final RoomTypeRepository roomTypeRepository;

    public RoomServiceImpl(RoomRepository repository, RoomTypeRepository roomTypeRepository) {
        super(repository);
        this.roomTypeRepository = roomTypeRepository;
    }

    @Override
    public RoomEntity save(RoomEntity entity) {
        if (entity.getRoomType() != null && entity.getRoomType().getId() != null) {
            roomTypeRepository.findById(entity.getRoomType().getId()).ifPresent(entity::setRoomType);
        }
        return super.save(entity);
    }
}
