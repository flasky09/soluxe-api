package com.hotel_erp.hotel_erp.modules.maintenance;

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

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@WithMockUser(username = "admin", roles = {"HOTEL_ADMIN"})
public class MaintenanceIssueTypeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MaintenanceIssueTypeRepository maintenanceIssueTypeRepository;

    @Autowired
    private org.springframework.jdbc.core.JdbcTemplate jdbcTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY FALSE");
        jdbcTemplate.execute("TRUNCATE TABLE maintenance_requests");
        jdbcTemplate.execute("TRUNCATE TABLE maintenance_issue_types");
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY TRUE");
    }

    @Test
    void testCreateMaintenanceIssueType() throws Exception {
        MaintenanceIssueTypeDTO dto = new MaintenanceIssueTypeDTO();
        dto.setName("Plumbing");
        dto.setActive(true);

        mockMvc.perform(post("/api/maintenance-issue-types")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Plumbing")))
                .andExpect(jsonPath("$.active", is(true)));
    }

    @Test
    void testGetAllMaintenanceIssueTypes() throws Exception {
        MaintenanceIssueTypeEntity entity = new MaintenanceIssueTypeEntity();
        entity.setName("Electrical");
        maintenanceIssueTypeRepository.save(entity);

        mockMvc.perform(get("/api/maintenance-issue-types"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$[*].name", hasItem("Electrical")));
    }

    @Test
    void testUpdateMaintenanceIssueType() throws Exception {
        MaintenanceIssueTypeEntity entity = new MaintenanceIssueTypeEntity();
        entity.setName("Old Issue");
        entity = maintenanceIssueTypeRepository.save(entity);

        MaintenanceIssueTypeDTO dto = new MaintenanceIssueTypeDTO();
        dto.setName("New Issue");
        dto.setActive(true);

        mockMvc.perform(put("/api/maintenance-issue-types/" + entity.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("New Issue")));
    }

    @Test
    void testDeleteMaintenanceIssueType() throws Exception {
        MaintenanceIssueTypeEntity entity = new MaintenanceIssueTypeEntity();
        entity.setName("To Delete");
        entity = maintenanceIssueTypeRepository.save(entity);

        mockMvc.perform(delete("/api/maintenance-issue-types/" + entity.getId()))
                .andExpect(status().isOk());
    }
}
