package com.hotel_erp.hotel_erp.modules.guest;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/guests/{guestId}/documents")
@RequiredArgsConstructor
public class GuestDocumentController {

    private final GuestDocumentRepository guestDocumentRepository;
    private final GuestRepository guestRepository;

    @GetMapping
    public List<GuestDocumentDTO> getGuestDocuments(@PathVariable Long guestId) {
        return guestDocumentRepository.findAllByGuestId(guestId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @PostMapping
    public GuestDocumentDTO uploadDocument(
            @PathVariable Long guestId,
            @RequestParam("file") MultipartFile file,
            @RequestParam("type") String type,
            @RequestParam(value = "number", required = false) String number,
            @RequestParam(value = "expiry", required = false) String expiry) {
        
        GuestEntity guest = guestRepository.findById(guestId)
                .orElseThrow(() -> new RuntimeException("Guest not found"));

        GuestDocumentEntity doc = new GuestDocumentEntity();
        doc.setGuest(guest);
        doc.setDocumentType(DocumentType.valueOf(type));
        doc.setDocumentNumber(number);
        doc.setFileName(file.getOriginalFilename());
        // In a real app, we'd save the file to a cloud bucket or local FS
        // For this demo, we'll just simulate a path
        doc.setFilePath("/uploads/guests/" + guestId + "/" + file.getOriginalFilename());
        
        if (expiry != null && !expiry.isEmpty()) {
            doc.setExpiryDate(java.time.LocalDate.parse(expiry));
        }

        return toDto(guestDocumentRepository.save(doc));
    }

    @DeleteMapping("/{docId}")
    public void deleteDocument(@PathVariable Long docId) {
        guestDocumentRepository.deleteById(docId);
    }

    private GuestDocumentDTO toDto(GuestDocumentEntity entity) {
        GuestDocumentDTO dto = new GuestDocumentDTO();
        dto.setId(entity.getId());
        dto.setGuestId(entity.getGuest().getId());
        dto.setDocumentType(entity.getDocumentType().name());
        dto.setDocumentNumber(entity.getDocumentNumber());
        dto.setFilePath(entity.getFilePath());
        dto.setExpiryDate(entity.getExpiryDate());
        dto.setFileName(entity.getFileName());
        return dto;
    }
}
