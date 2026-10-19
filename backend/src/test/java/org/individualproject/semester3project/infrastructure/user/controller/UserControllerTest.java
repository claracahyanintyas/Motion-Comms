package org.individualproject.semester3project.infrastructure.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.individualproject.semester3project.infrastructure.user.dto.CreateUserRequest;
import org.individualproject.semester3project.infrastructure.config.db.entity.UserEntity;
import org.individualproject.semester3project.infrastructure.config.db.repository.UserRepository;
import org.individualproject.semester3project.infrastructure.user.dto.UpdateUserRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import java.time.OffsetDateTime;

import static org.hamcrest.Matchers.greaterThan;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.is;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    private UserEntity user;

    @BeforeEach
    void seedTestUser() {
        if (!userRepository.existsByUsername("existing")) {
            user = new UserEntity();
            user.setUsername("existing");
            user.setEmail("existing@email.com");
            user.setPasswordHash("password");
            user.setCreatedAt(OffsetDateTime.now());
            userRepository.save(user);
        }
        if (!userRepository.existsByUsername("otherUser")) {
            UserEntity otherUser = new UserEntity();
            otherUser.setUsername("otherUser");
            otherUser.setEmail("otherUser@email.com");
            otherUser.setPasswordHash("password");
            otherUser.setCreatedAt(OffsetDateTime.now());
            userRepository.save(otherUser);
        }
    }

    @Test
    void createUser_shouldReturnId() throws Exception {
        CreateUserRequest request = CreateUserRequest.builder()
                .username("newuser")
                .email("newuser@email.com")
                .password("password")
                .build();

        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.id").value(greaterThan(0)));
    }

    @Test
    void createUser_usernameTaken_shouldReturn() throws Exception {

        CreateUserRequest request = CreateUserRequest.builder()
                .username("existing")
                .email("newuser@email.com")
                .password("password")
                .build();

        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Username taken"))
                .andExpect(jsonPath("$.message").isNotEmpty());
    }

    @Test
    void createUser_emailTaken_shouldReturn() throws Exception {

        CreateUserRequest request = CreateUserRequest.builder()
                .username("newUser")
                .email("existing@email.com")
                .password("password")
                .build();

        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Email taken"))
                .andExpect(jsonPath("$.message").isNotEmpty());
    }

    @Test
    void getUser_existingUser_shouldReturnUser() throws Exception {
        mockMvc.perform(get("/api/users/" + user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user.id", is(user.getId().intValue())))
                .andExpect(jsonPath("$.user.username", is("existing")));
    }

    @Test
    void getUser_nonExistingUser_shouldReturn404() throws Exception {
        mockMvc.perform(get("/api/users/" + 1))
                .andExpect(status().isNotFound());
    }


    @WithMockUser(username = "existing")
    @Test
    void updateUser_happyFlow_shouldReturnNoContent() throws Exception {

        UpdateUserRequest request = new UpdateUserRequest();
        request.setUsername("updatedUser");
        request.setEmail("updatedUser@email.com");

        mockMvc.perform(put("/api/users/" + user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());
    }

    @WithMockUser(username = "existing")
    @Test
    void updateUser_usernameTaken_shouldReturnConflict() throws Exception {
        UpdateUserRequest request = new UpdateUserRequest();
        request.setUsername("otherUser");

        mockMvc.perform(put("/api/users/" + user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @WithMockUser(username = "existing")
    @Test
    void updateUser_emailTaken_shouldReturnConflict() throws Exception {
        UpdateUserRequest request = new UpdateUserRequest();
        request.setEmail("otherUser@email.com");

        mockMvc.perform(put("/api/users/" + user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateUser_unauthenticated_shouldReturnUnauthorized() throws Exception {
        UpdateUserRequest request = new UpdateUserRequest();
        request.setUsername("updatedUser");

        mockMvc.perform(put("/api/users/" + user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser(username = "existing")
    @Test
    void updateUser_shouldReturnNoContent() throws Exception {

        mockMvc.perform(delete("/api/users/" + user.getId()))
                .andExpect(status().isNoContent());
    }
}
