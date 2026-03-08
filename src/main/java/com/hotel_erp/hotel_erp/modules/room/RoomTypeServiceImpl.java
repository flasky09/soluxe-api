package com.hotel_erp.hotel_erp.modules.room;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoomTypeServiceImpl implements RoomTypeService {

    private final RoomTypeRepository roomTypeRepository;

    @Override
    public List<RoomTypeEntity> findAll() {
        return roomTypeRepository.findAll();
    }

    @Override
    public Optional<RoomTypeEntity> findById(Long id) {
        return roomTypeRepository.findById(id);
    }

    @Override
    public RoomTypeEntity save(RoomTypeEntity roomType) {
        return roomTypeRepository.save(roomType);
    }

    @Override
    public void deleteById(Long id) {
        roomTypeRepository.deleteById(id);
    }
}
