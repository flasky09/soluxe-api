package com.hotel_erp.hotel_erp.modules.user;

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

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    // -----------------------------------------------------------------------
    // Helper payloads
    // -----------------------------------------------------------------------

    private String validPayload(String username, String email) {
        return """
            {
                "username": "%s",
                "fullName": "Test User",
                "email": "%s",
                "phoneNumber": "0700000000",
                "password": "password123",
                "role": "RECEPTIONIST",
                "isActive": true
            }
            """.formatted(username, email);
    }

    // -----------------------------------------------------------------------
    // POST /api/users
    // -----------------------------------------------------------------------

    @Test
    @WithMockUser(roles = "HOTEL_ADMIN")
    public void testCreateUser_whenValid_thenReturns200WithBody() throws Exception {
        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(validPayload("boni_test", "boni_test@example.com")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.username", is("boni_test")))
                .andExpect(jsonPath("$.email", is("boni_test@example.com")))
                .andExpect(jsonPath("$.role", is("RECEPTIONIST")))
                .andExpect(jsonPath("$.isActive", is(true)))
                // password hash should never be exposed
                .andExpect(jsonPath("$.password").doesNotExist());
    }

    @Test
    @WithMockUser(roles = "HOTEL_ADMIN")
    public void testCreateUser_whenUsernameMissing_thenReturns400() throws Exception {
        String payload = """
            {
                "fullName": "No Username User",
                "email": "nousername@example.com",
                "password": "password123",
                "role": "RECEPTIONIST",
                "isActive": true
            }
            """;

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "HOTEL_ADMIN")
    public void testCreateUser_whenEmailMissing_thenReturns400() throws Exception {
        String payload = """
            {
                "username": "noemail_user",
                "fullName": "No Email User",
                "password": "password123",
                "role": "RECEPTIONIST",
                "isActive": true
            }
            """;

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "HOTEL_ADMIN")
    public void testCreateUser_whenEmailInvalid_thenReturns400() throws Exception {
        String payload = """
            {
                "username": "bademail_user",
                "fullName": "Bad Email User",
                "email": "not-an-email",
                "password": "password123",
                "role": "RECEPTIONIST",
                "isActive": true
            }
            """;

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "HOTEL_ADMIN")
    public void testCreateUser_whenRoleMissing_thenReturns400() throws Exception {
        String payload = """
            {
                "username": "norole_user",
                "fullName": "No Role User",
                "email": "norole@example.com",
                "password": "password123",
                "isActive": true
            }
            """;

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "HOTEL_ADMIN")
    public void testCreateUser_whenDuplicateEmail_thenReturns4xx() throws Exception {
        // First creation should succeed
        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(validPayload("first_user", "duplicate@example.com")))
                .andExpect(status().isOk());

        // Second with same email should fail
        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(validPayload("second_user", "duplicate@example.com")))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void testCreateUser_whenUnauthenticated_thenReturns401() throws Exception {
        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(validPayload("unauthed_user", "unauthed@example.com")))
                .andExpect(status().isUnauthorized());
    }

    // -----------------------------------------------------------------------
    // GET /api/users
    // -----------------------------------------------------------------------

    @Test
    @WithMockUser(roles = "HOTEL_ADMIN")
    public void testGetAllUsers_returnsJsonArray() throws Exception {
        // Create a user first
        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(validPayload("list_test_user", "list_test@example.com")))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()));
    }

    @Test
    public void testGetAllUsers_whenUnauthenticated_thenReturns401() throws Exception {
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isUnauthorized());
    }

    // -----------------------------------------------------------------------
    // GET /api/users/{id}
    // -----------------------------------------------------------------------

    @Test
    @WithMockUser(roles = "HOTEL_ADMIN")
    public void testGetUserById_whenExists_thenReturnsUser() throws Exception {
        // Create a user
        String createResponse = mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(validPayload("fetchable_user", "fetchable@example.com")))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long createdId = objectMapper.readTree(createResponse).get("id").asLong();

        mockMvc.perform(get("/api/users/" + createdId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is("fetchable_user")))
                .andExpect(jsonPath("$.email", is("fetchable@example.com")));
    }

    // -----------------------------------------------------------------------
    // PUT /api/users/{id}
    // -----------------------------------------------------------------------

    @Test
    @WithMockUser(roles = "HOTEL_ADMIN")
    public void testUpdateUser_whenValid_thenReturnsUpdatedUser() throws Exception {
        // Create
        String createResponse = mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(validPayload("update_target", "update_target@example.com")))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long createdId = objectMapper.readTree(createResponse).get("id").asLong();

        // Update
        String updatePayload = """
            {
                "username": "update_target",
                "fullName": "Updated Name",
                "email": "update_target@example.com",
                "phoneNumber": "0799999999",
                "role": "MANAGER",
                "isActive": false
            }
            """;

        mockMvc.perform(put("/api/users/" + createdId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(updatePayload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName", is("Updated Name")))
                .andExpect(jsonPath("$.role", is("MANAGER")))
                .andExpect(jsonPath("$.isActive", is(false)));
    }

    @Test
    @WithMockUser(roles = "HOTEL_ADMIN")
    public void testUpdateUser_whenUserNotFound_thenReturns5xx() throws Exception {
        String updatePayload = """
            {
                "username": "ghost",
                "fullName": "Ghost User",
                "email": "ghost@example.com",
                "role": "RECEPTIONIST",
                "isActive": true
            }
            """;

        mockMvc.perform(put("/api/users/999999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(updatePayload))
                .andExpect(status().is5xxServerError());
    }

    @Test
    public void testUpdateUser_whenUnauthenticated_thenReturns401() throws Exception {
        mockMvc.perform(put("/api/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(validPayload("x", "x@x.com")))
                .andExpect(status().isUnauthorized());
    }

    // -----------------------------------------------------------------------
    // DELETE /api/users/{id}
    // -----------------------------------------------------------------------

    @Test
    @WithMockUser(roles = "HOTEL_ADMIN")
    public void testDeleteUser_whenExists_thenReturns200() throws Exception {
        // Create
        String createResponse = mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(validPayload("delete_me", "delete_me@example.com")))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long createdId = objectMapper.readTree(createResponse).get("id").asLong();

        // Delete
        mockMvc.perform(delete("/api/users/" + createdId))
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteUser_whenUnauthenticated_thenReturns401() throws Exception {
        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isUnauthorized());
    }
}
