package com.hotel_erp.hotel_erp.modules.folio;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@WithMockUser(username = "admin", roles = {"HOTEL_ADMIN"})
public class ChargeTypeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ChargeTypeRepository chargeTypeRepository;

    @Autowired
    private org.springframework.jdbc.core.JdbcTemplate jdbcTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY FALSE");
        jdbcTemplate.execute("TRUNCATE TABLE folio_charges");
        jdbcTemplate.execute("TRUNCATE TABLE charge_types");
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY TRUE");
    }

    @Test
    void testCreateChargeType() throws Exception {
        ChargeTypeDTO dto = new ChargeTypeDTO();
        dto.setName("Service Charge");
        dto.setActive(true);

        mockMvc.perform(post("/api/charge-types")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andDo(org.springframework.test.web.servlet.result.MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Service Charge")))
                .andExpect(jsonPath("$.active", is(true)));
    }

    @Test
    void testCreateDuplicateChargeTypeFails() throws Exception {
        ChargeTypeEntity existing = new ChargeTypeEntity();
        existing.setName("Duplicate");
        chargeTypeRepository.saveAndFlush(existing);

        ChargeTypeDTO dto = new ChargeTypeDTO();
        dto.setName("Duplicate");

        mockMvc.perform(post("/api/charge-types")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isConflict());
    }

    @Test
    void testGetAllChargeTypes() throws Exception {
        ChargeTypeEntity c1 = new ChargeTypeEntity();
        c1.setName("Tax");
        chargeTypeRepository.save(c1);

        mockMvc.perform(get("/api/charge-types"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$[*].name", hasItem("Tax")));
    }

    @Test
    void testSafeDeleteChargeType() throws Exception {
        ChargeTypeEntity c1 = new ChargeTypeEntity();
        c1.setName("To Delete");
        c1 = chargeTypeRepository.save(c1);

        // Deleting a clean one should work (hard delete or status change depending on implementation)
        mockMvc.perform(delete("/api/charge-types/" + c1.getId()))
                .andExpect(status().isOk());

        // Verify it's either gone or inactive
        mockMvc.perform(get("/api/charge-types"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].name", not(hasItem("To Delete"))));
    }
}
