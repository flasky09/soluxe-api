package com.hotel_erp.hotel_erp.modules.shift;

import com.hotel_erp.hotel_erp.modules.folio.FolioPaymentRepository;
import com.hotel_erp.hotel_erp.modules.user.UserEntity;
import com.hotel_erp.hotel_erp.modules.user.UserRepository;
import com.hotel_erp.hotel_erp.modules.user.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShiftHandoverService {

    private final ShiftHandoverRepository shiftHandoverRepository;
    private final FolioPaymentRepository folioPaymentRepository;
    private final UserRepository userRepository;

    public void validateActiveShift(Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Only enforce for receptionists
        if (user.getRole() == Role.RECEPTIONIST) {
            Optional<ShiftHandoverEntity> activeShift = shiftHandoverRepository.findByUserIdAndStatus(userId, ShiftHandoverEntity.ShiftStatus.ACTIVE);
            if (activeShift.isEmpty()) {
                throw new RuntimeException("You must be on an active shift to perform this operation. Please clock in first.");
            }
        }
    }

    @Transactional
    public ShiftHandoverDTO clockIn(Long userId, String shiftType) {
        // Check if there is already an active shift for this user
        Optional<ShiftHandoverEntity> activeShift = shiftHandoverRepository.findByUserIdAndStatus(userId, ShiftHandoverEntity.ShiftStatus.ACTIVE);
        if (activeShift.isPresent()) {
            throw new RuntimeException("User already has an active shift. Please clock out first.");
        }

        userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        ShiftHandoverEntity shift = ShiftHandoverEntity.builder()
                .userId(userId)
                .date(LocalDate.now())
                .shiftType(shiftType)
                .clockInTime(LocalDateTime.now())
                .status(ShiftHandoverEntity.ShiftStatus.ACTIVE)
                .build();

        shift = shiftHandoverRepository.save(shift);
        return mapToDTO(shift);
    }

    @Transactional
    public ShiftHandoverDTO clockOut(Long shiftId, String notes) {
        ShiftHandoverEntity shift = shiftHandoverRepository.findById(shiftId)
                .orElseThrow(() -> new RuntimeException("Shift not found"));

        if (shift.getStatus() == ShiftHandoverEntity.ShiftStatus.CLOSED) {
            throw new RuntimeException("Shift is already closed.");
        }

        LocalDateTime clockOutTime = LocalDateTime.now();
        BigDecimal totalEarnings = folioPaymentRepository.sumCollectedByUserInRange(
                shift.getUserId(), shift.getClockInTime(), clockOutTime);
        long clientsCount = folioPaymentRepository.countDistinctFoliosByUserInRange(
                shift.getUserId(), shift.getClockInTime(), clockOutTime);

        shift.setClockOutTime(clockOutTime);
        shift.setTotalEarnings(totalEarnings);
        shift.setClientsCount(clientsCount);
        shift.setNotes(notes);
        shift.setStatus(ShiftHandoverEntity.ShiftStatus.CLOSED);

        shift = shiftHandoverRepository.save(shift);
        return mapToDTO(shift);
    }

    public Optional<ShiftHandoverDTO> getActiveShift(Long userId) {
        return shiftHandoverRepository.findByUserIdAndStatus(userId, ShiftHandoverEntity.ShiftStatus.ACTIVE)
                .map(this::mapToDTO);
    }

    public List<ShiftHandoverDTO> getAllShifts() {
        return shiftHandoverRepository.findAllByOrderByClockInTimeDesc().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private ShiftHandoverDTO mapToDTO(ShiftHandoverEntity entity) {
        UserEntity user = userRepository.findById(entity.getUserId()).orElse(null);
        String fullName = user != null ? user.getFullName() : "Unknown User";

        return ShiftHandoverDTO.builder()
                .id(entity.getId())
                .userId(entity.getUserId())
                .username(user != null ? user.getUsername() : "Unknown")
                .fullName(fullName)
                .date(entity.getDate())
                .shiftType(entity.getShiftType())
                .clockInTime(entity.getClockInTime())
                .clockOutTime(entity.getClockOutTime())
                .totalEarnings(entity.getTotalEarnings())
                .clientsCount(entity.getClientsCount())
                .notes(entity.getNotes())
                .status(entity.getStatus().name())
                .build();
    }
}
