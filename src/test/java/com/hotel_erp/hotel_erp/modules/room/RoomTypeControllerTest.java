package com.hotel_erp.hotel_erp.modules.room;

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

import java.math.BigDecimal;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@WithMockUser(username = "admin", roles = {"HOTEL_ADMIN"})
public class RoomTypeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RoomTypeRepository roomTypeRepository;

    @Autowired
    private org.springframework.jdbc.core.JdbcTemplate jdbcTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY FALSE");
        jdbcTemplate.execute("TRUNCATE TABLE rooms");
        jdbcTemplate.execute("TRUNCATE TABLE room_types");
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY TRUE");
    }

    @Test
    void testCreateRoomType() throws Exception {
        RoomTypeDTO dto = new RoomTypeDTO();
        dto.setName("Deluxe Room");
        dto.setDefaultRate(new BigDecimal("150.00"));
        dto.setWeekendRate(new BigDecimal("180.00"));
        dto.setCapacity(2);
        dto.setBedType("King");
        dto.setAmenities("WiFi, TV, AC");

        mockMvc.perform(post("/api/room-types")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Deluxe Room")))
                .andExpect(jsonPath("$.defaultRate", is(150.0)))
                .andExpect(jsonPath("$.capacity", is(2)));
    }

    @Test
    void testGetAllRoomTypes() throws Exception {
        RoomTypeEntity entity = new RoomTypeEntity();
        entity.setName("Standard Room");
        entity.setDefaultRate(new BigDecimal("100.00"));
        entity.setCapacity(2);
        roomTypeRepository.save(entity);

        mockMvc.perform(get("/api/room-types"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$[*].name", hasItem("Standard Room")));
    }

    @Test
    void testGetRoomTypeById() throws Exception {
        RoomTypeEntity entity = new RoomTypeEntity();
        entity.setName("Single Room");
        entity.setDefaultRate(new BigDecimal("80.00"));
        entity.setCapacity(1);
        entity = roomTypeRepository.save(entity);

        mockMvc.perform(get("/api/room-types/" + entity.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Single Room")));
    }

    @Test
    void testUpdateRoomType() throws Exception {
        RoomTypeEntity entity = new RoomTypeEntity();
        entity.setName("Old Name");
        entity.setDefaultRate(new BigDecimal("100.00"));
        entity = roomTypeRepository.save(entity);

        RoomTypeDTO dto = new RoomTypeDTO();
        dto.setName("New Name");
        dto.setDefaultRate(new BigDecimal("120.00"));
        dto.setCapacity(2);

        mockMvc.perform(put("/api/room-types/" + entity.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("New Name")))
                .andExpect(jsonPath("$.defaultRate", is(120.0)));
    }

    @Test
    void testDeleteRoomType() throws Exception {
        RoomTypeEntity entity = new RoomTypeEntity();
        entity.setName("To Delete");
        entity.setDefaultRate(new BigDecimal("100.00"));
        entity = roomTypeRepository.save(entity);

        mockMvc.perform(delete("/api/room-types/" + entity.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/room-types/" + entity.getId()))
                .andExpect(status().isNotFound());
    }
}
