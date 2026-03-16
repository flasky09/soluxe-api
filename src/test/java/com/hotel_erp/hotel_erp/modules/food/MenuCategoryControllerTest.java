package com.hotel_erp.hotel_erp.modules.food;

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
public class MenuCategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MenuCategoryRepository menuCategoryRepository;

    @Autowired
    private org.springframework.jdbc.core.JdbcTemplate jdbcTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY FALSE");
        jdbcTemplate.execute("TRUNCATE TABLE menu_items");
        jdbcTemplate.execute("TRUNCATE TABLE menu_categories");
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY TRUE");
    }

    @Test
    void testCreateMenuCategory() throws Exception {
        MenuCategoryEntity entity = new MenuCategoryEntity();
        entity.setName("Beverages");

        mockMvc.perform(post("/api/menu-categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(entity)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Beverages")));
    }

    @Test
    void testGetAllMenuCategories() throws Exception {
        MenuCategoryEntity entity = new MenuCategoryEntity();
        entity.setName("Main Course");
        menuCategoryRepository.save(entity);

        mockMvc.perform(get("/api/menu-categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$[*].name", hasItem("Main Course")));
    }

    @Test
    void testGetMenuCategoryById() throws Exception {
        MenuCategoryEntity entity = new MenuCategoryEntity();
        entity.setName("Desserts");
        entity = menuCategoryRepository.save(entity);

        mockMvc.perform(get("/api/menu-categories/" + entity.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Desserts")));
    }

    @Test
    void testDeleteMenuCategory() throws Exception {
        MenuCategoryEntity entity = new MenuCategoryEntity();
        entity.setName("To Delete");
        entity = menuCategoryRepository.save(entity);

        mockMvc.perform(delete("/api/menu-categories/" + entity.getId()))
                .andExpect(status().isOk());
    }
}
