package com.hotel_erp.hotel_erp.modules.guest;

import com.hotel_erp.hotel_erp.shared.services.CloudinaryService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/guests/{guestId}/documents")
@RequiredArgsConstructor
public class GuestDocumentController {

    private final GuestDocumentRepository guestDocumentRepository;
    private final GuestRepository guestRepository;
    private final CloudinaryService cloudinaryService;

    @GetMapping
    public List<GuestDocumentDTO> getGuestDocuments(@PathVariable Long guestId) {
        if (guestId == null) {
            return java.util.Collections.emptyList();
        }
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

        try {
            Map<String, Object> uploadResult = cloudinaryService.upload(file, "guests/" + guestId);
            String url = (String) uploadResult.get("secure_url");

            GuestDocumentEntity doc = new GuestDocumentEntity();
            doc.setGuest(guest);
            doc.setDocumentType(DocumentType.valueOf(type));
            doc.setDocumentNumber(number);
            doc.setFileName(file.getOriginalFilename());
            doc.setFilePath(url);
            
            if (expiry != null && !expiry.isEmpty()) {
                doc.setExpiryDate(java.time.LocalDate.parse(expiry));
            }

            return toDto(guestDocumentRepository.save(doc));
        } catch (java.io.IOException e) {
            throw new RuntimeException("Failed to upload document to Cloudinary", e);
        }
    }

    @DeleteMapping("/{docId}")
    public void deleteDocument(@PathVariable Long docId) {
        guestDocumentRepository.deleteById(docId);
    }

    private GuestDocumentDTO toDto(GuestDocumentEntity entity) {
        if (entity == null) return null;
        GuestDocumentDTO dto = new GuestDocumentDTO();
        dto.setId(entity.getId());
        if (entity.getGuest() != null) {
            dto.setGuestId(entity.getGuest().getId());
        }
        if (entity.getDocumentType() != null) {
            dto.setDocumentType(entity.getDocumentType().name());
        }
        dto.setDocumentNumber(entity.getDocumentNumber());
        dto.setFilePath(entity.getFilePath());
        dto.setExpiryDate(entity.getExpiryDate());
        dto.setFileName(entity.getFileName());
        return dto;
    }
}
