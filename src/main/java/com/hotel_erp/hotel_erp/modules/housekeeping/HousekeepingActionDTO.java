package com.hotel_erp.hotel_erp.modules.housekeeping;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HousekeepingActionDTO {
    @NotNull(message = "Room ID is required")
    private Long roomId;

    @NotNull(message = "Action is required")
    private HousekeepingAction action;

    private String notes;
}
