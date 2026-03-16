package com.hotel_erp.hotel_erp.modules.folio;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.test.context.support.WithMockUser;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.util.Optional;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class FolioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FolioService folioService;

    @MockBean
    private FolioMapper folioMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(roles = "RECEPTIONIST")
    void testAddCharge_ReturnsSuccess() throws Exception {
        FolioChargeDTO chargeDto = new FolioChargeDTO();
        chargeDto.setQuantity(new BigDecimal("1"));
        chargeDto.setUnitPrice(new BigDecimal("50"));
        chargeDto.setDescription("Test Charge");

        when(folioService.addCharge(anyLong(), any(FolioChargeDTO.class), anyLong()))
                .thenReturn(chargeDto);

        mockMvc.perform(post("/api/folios/1/charges")
                .param("userId", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(chargeDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("Test Charge"));
    }

    @Test
    @WithMockUser(roles = "RECEPTIONIST")
    void testAddCharge_WithZeroChargeTypeId_ReturnsSuccess() throws Exception {
        FolioChargeDTO chargeDto = new FolioChargeDTO();
        chargeDto.setChargeTypeId(0L); // Current bug scenario
        chargeDto.setQuantity(new BigDecimal("1"));
        chargeDto.setUnitPrice(new BigDecimal("50"));
        chargeDto.setDescription("Zero ID Charge");

        when(folioService.addCharge(eq(1L), any(FolioChargeDTO.class), eq(1L)))
                .thenReturn(chargeDto);

        mockMvc.perform(post("/api/folios/1/charges")
                .param("userId", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(chargeDto)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "RECEPTIONIST")
    @SuppressWarnings("unchecked")
    void testGetFolio_ReturnsFolio() throws Exception {
        FolioDTO folioDto = new FolioDTO();
        folioDto.setId(1L);
        folioDto.setFolioType(FolioType.STAY);
        folioDto.setStatus(FolioStatus.OPEN);

        when(folioService.findById(1L)).thenReturn(Optional.of(new FolioEntity()));
        when(folioMapper.toDto(any())).thenReturn(folioDto);

        mockMvc.perform(get("/api/folios/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }
}
