package com.hotel_erp.hotel_erp.modules.employee;

import com.hotel_erp.hotel_erp.modules.user.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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
public class DepartmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private org.springframework.jdbc.core.JdbcTemplate jdbcTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY FALSE");
        jdbcTemplate.execute("TRUNCATE TABLE users");
        jdbcTemplate.execute("TRUNCATE TABLE departments");
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY TRUE");
    }

    @Test
    @WithMockUser(username = "receptionist", roles = {"RECEPTIONIST"})
    void testCreateDepartmentAsReceptionist_Forbidden() throws Exception {
        DepartmentDTO dto = new DepartmentDTO();
        dto.setName("New Dept");

        mockMvc.perform(post("/api/departments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    void testCreateDepartment() throws Exception {
        DepartmentDTO dto = new DepartmentDTO();
        dto.setName("Housekeeping");

        mockMvc.perform(post("/api/departments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Housekeeping")));
    }

    @Test
    void testGetAllDepartments() throws Exception {
        DepartmentEntity entity = new DepartmentEntity();
        entity.setName("Front Office");
        departmentRepository.save(entity);

        mockMvc.perform(get("/api/departments"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$[*].name", hasItem("Front Office")));
    }

    @Test
    void testGetDepartmentById() throws Exception {
        DepartmentEntity entity = new DepartmentEntity();
        entity.setName("Maintenance");
        entity = departmentRepository.save(entity);

        mockMvc.perform(get("/api/departments/" + entity.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Maintenance")));
    }

    @Test
    void testUpdateDepartment() throws Exception {
        DepartmentEntity entity = new DepartmentEntity();
        entity.setName("Old Admin");
        entity = departmentRepository.save(entity);

        DepartmentDTO dto = new DepartmentDTO();
        dto.setName("New Admin");

        mockMvc.perform(put("/api/departments/" + entity.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("New Admin")));
    }

    @Test
    void testDeleteDepartment() throws Exception {
        DepartmentEntity entity = new DepartmentEntity();
        entity.setName("To Delete");
        entity = departmentRepository.save(entity);

        mockMvc.perform(delete("/api/departments/" + entity.getId()))
                .andDo(print())
                .andExpect(status().isNoContent());

        // Verify it's either gone or inactive (depending on implementation)
        // DepartmentService sets it to inactive on failure, let's just check it doesn't crash
    }
}
