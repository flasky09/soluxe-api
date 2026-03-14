package com.hotel_erp.hotel_erp.modules.guest;

import lombok.Data;
import java.time.LocalDate;

@Data
public class GuestDocumentDTO {
    private Long id;
    private Long guestId;
    private String documentType;
    private String documentNumber;
    private String filePath;
    private LocalDate expiryDate;
    private String fileName;
}
