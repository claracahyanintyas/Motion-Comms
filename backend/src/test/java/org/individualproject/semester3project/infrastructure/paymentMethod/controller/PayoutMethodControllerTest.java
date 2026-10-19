package org.individualproject.semester3project.infrastructure.paymentMethod.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.individualproject.semester3project.domain.payoutMethod.model.PayoutMethodType;
import org.individualproject.semester3project.infrastructure.config.db.entity.PayoutMethodEntity;
import org.individualproject.semester3project.infrastructure.config.db.entity.UserEntity;
import org.individualproject.semester3project.infrastructure.config.db.repository.PayoutMethodRepository;
import org.individualproject.semester3project.infrastructure.config.db.repository.UserRepository;
import org.individualproject.semester3project.infrastructure.paymentMethod.dto.CreatePayoutMethodRequest;
import org.individualproject.semester3project.infrastructure.paymentMethod.dto.UpdatePayoutMethodRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.OffsetDateTime;
import static org.hamcrest.number.OrderingComparison.greaterThan;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class PayoutMethodControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PayoutMethodRepository payoutMethodRepository;

    private UserEntity user;
    @BeforeEach
    void setup() {
        user = new UserEntity();
        user.setUsername("user");
        user.setPasswordHash("password");
        user.setEmail("user@email.com");
        user.setCreatedAt(OffsetDateTime.now());
        userRepository.save(user);
    }

    @WithMockUser(username = "user")
    @Test
    void createPayoutMethod_shouldReturnId() throws Exception {
        CreatePayoutMethodRequest request = CreatePayoutMethodRequest.builder()
                .userId(user.getId())
                .type(PayoutMethodType.BANK_ACCOUNT)
                .accountName("John")
                .accountNumber("123456")
                .providerName("ING")
                .build();

        mockMvc.perform(post("/api/payout-methods/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.id").value(greaterThan(0)));

        // verify persisted
        assertEquals(1, payoutMethodRepository.count());
    }

    @WithMockUser
    @Test
    void getActivePayoutMethodsByUser_shouldReturnList() throws Exception {
        payoutMethodRepository.save(
                PayoutMethodEntity.builder()
                        .user(user)
                        .type(PayoutMethodType.BANK_ACCOUNT)
                        .providerName("ING")
                        .accountName("John")
                        .accountNumber("123")
                        .active(true)
                        .createdAt(OffsetDateTime.now())
                        .build()
        );

        mockMvc.perform(get("/api/payout-methods/user/" + user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payoutMethods").isArray())
                .andExpect(jsonPath("$.payoutMethods.length()").value(1));
    }

    @WithMockUser
    @Test
    void getPayoutMethod_shouldReturnPayoutMethod() throws Exception {
        PayoutMethodEntity saved = payoutMethodRepository.save(
                PayoutMethodEntity.builder()
                        .user(user)
                        .type(PayoutMethodType.BANK_ACCOUNT)
                        .accountName("John")
                        .accountNumber("123")
                        .active(true)
                        .createdAt(OffsetDateTime.now())
                        .providerName("ING")
                        .build()
        );

        mockMvc.perform(get("/api/payout-methods/" + saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payoutMethod.id").value(saved.getId()));
    }

    @WithMockUser(username = "user")
    @Test
    void updatePayoutMethod_shouldReturnEmpty() throws Exception {

        PayoutMethodEntity saved = payoutMethodRepository.save(PayoutMethodEntity
                .builder()
                .user(user)
                .type(PayoutMethodType.BANK_ACCOUNT)
                .accountName("John")
                .accountNumber("123456")
                .providerName("ING")
                .createdAt(OffsetDateTime.now())
                .build());
        UpdatePayoutMethodRequest request = UpdatePayoutMethodRequest.builder()
                .accountName("Jane")
                .providerName("ABN")
                .accountNumber("123")
                .type(PayoutMethodType.MOBILE_WALLET)
                .build();

        mockMvc.perform(put("/api/payout-methods/edit/"  + saved.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        var savedMethod =  payoutMethodRepository.findById(saved.getId()).get();

        // verify persisted
        assertEquals(1, payoutMethodRepository.count());
        assertEquals(request.getAccountName(), savedMethod.getAccountName());
        assertEquals(request.getProviderName(), savedMethod.getProviderName());
    }

}