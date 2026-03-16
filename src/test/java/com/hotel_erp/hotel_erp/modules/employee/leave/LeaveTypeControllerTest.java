package com.hotel_erp.hotel_erp.modules.employee.leave;

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
public class LeaveTypeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private LeaveTypeRepository leaveTypeRepository;

    @Autowired
    private org.springframework.jdbc.core.JdbcTemplate jdbcTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY FALSE");
        jdbcTemplate.execute("TRUNCATE TABLE leave_requests");
        jdbcTemplate.execute("TRUNCATE TABLE leave_types");
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY TRUE");
    }

    @Test
    void testCreateLeaveType() throws Exception {
        LeaveTypeDTO dto = new LeaveTypeDTO();
        dto.setName("Sick Leave");
        dto.setActive(true);

        mockMvc.perform(post("/api/leave-types")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Sick Leave")))
                .andExpect(jsonPath("$.active", is(true)));
    }

    @Test
    void testGetAllLeaveTypes() throws Exception {
        LeaveTypeEntity entity = new LeaveTypeEntity();
        entity.setName("Annual Leave");
        leaveTypeRepository.save(entity);

        mockMvc.perform(get("/api/leave-types"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$[*].name", hasItem("Annual Leave")));
    }

    @Test
    void testUpdateLeaveType() throws Exception {
        LeaveTypeEntity entity = new LeaveTypeEntity();
        entity.setName("Old Leave");
        entity = leaveTypeRepository.save(entity);

        LeaveTypeDTO dto = new LeaveTypeDTO();
        dto.setName("New Leave");
        dto.setActive(true);

        mockMvc.perform(put("/api/leave-types/" + entity.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("New Leave")));
    }

    @Test
    void testDeleteLeaveType() throws Exception {
        LeaveTypeEntity entity = new LeaveTypeEntity();
        entity.setName("To Delete");
        entity = leaveTypeRepository.save(entity);

        mockMvc.perform(delete("/api/leave-types/" + entity.getId()))
                .andExpect(status().isOk());
    }
}
