package org.individualproject.semester3project.infrastructure.commission.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.individualproject.semester3project.domain.commission.model.Commission;
import org.individualproject.semester3project.domain.payoutMethod.model.PayoutMethodType;
import org.individualproject.semester3project.infrastructure.commission.dto.CreateCommissionRequest;
import org.individualproject.semester3project.infrastructure.commission.dto.UpdateCommissionRequest;
import org.individualproject.semester3project.infrastructure.config.db.entity.CommissionEntity;
import org.individualproject.semester3project.infrastructure.config.db.entity.PayoutMethodEntity;
import org.individualproject.semester3project.infrastructure.config.db.entity.UserEntity;
import org.individualproject.semester3project.infrastructure.config.db.repository.CommissionRepository;
import org.individualproject.semester3project.infrastructure.config.db.repository.PayoutMethodRepository;
import org.individualproject.semester3project.infrastructure.config.db.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import static org.hamcrest.Matchers.greaterThan;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.is;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class CommissionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final String baseUrl = "/api/commissions";
    @Autowired
    private CommissionRepository commissionRepository;
    @Autowired
    private PayoutMethodRepository payoutMethodRepository;

    @BeforeEach
    void seedTestUser() {
        if (!userRepository.existsByUsername("user")) {
            var user = new UserEntity();
            user.setUsername("user");
            user.setPasswordHash(passwordEncoder.encode("password"));
            user.setEmail("user@email.com");
            user.setCreatedAt(OffsetDateTime.now());
            userRepository.save(user);
            var payoutMethod = PayoutMethodEntity.builder()
                    .user(user)
                    .accountName("user")
                    .type(PayoutMethodType.BANK_ACCOUNT)
                    .providerName("provider")
                    .accountNumber("7308124")
                    .createdAt(OffsetDateTime.now())
                    .active(true)
                    .build();
            payoutMethodRepository.save(payoutMethod);
        }

    }

    @WithMockUser(username = "user")
    @Test
    void createCommission_authenticated_shouldReturnId() throws Exception {

        var user = userRepository.findByUsername("user");

        CreateCommissionRequest request = CreateCommissionRequest.builder()
                .commissioneeId(user.getId())
                .price(BigDecimal.valueOf(21.0))
                .title("title")
                .description("desc")
                .downPaymentPercentage(BigDecimal.valueOf(20.0))
                .build();

        mockMvc.perform(post(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.id").value(greaterThan(0)));
    }
    @WithMockUser(username = "user")
    @Test
    void createCommission_noActivePayoutMethod_shouldThrowException() throws Exception {

        var user = userRepository.findByUsername("user");

        var payoutMethod = payoutMethodRepository.findByUserIdAndActiveTrue(user.getId()).getFirst();
        payoutMethod.setActive(false);
        payoutMethodRepository.save(payoutMethod);

        CreateCommissionRequest request = CreateCommissionRequest.builder()
                .commissioneeId(user.getId())
                .price(BigDecimal.valueOf(21.0))
                .title("title")
                .description("desc")
                .downPaymentPercentage(BigDecimal.valueOf(20.0))
                .build();

        mockMvc.perform(post(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Commissionee has no active payout method"));
    }
    @Test
    void createCommission_unauthenticated_shouldReturn401() throws Exception {

        CreateCommissionRequest request = CreateCommissionRequest.builder()
                .commissioneeId(1L)
                .price(BigDecimal.valueOf(21.0))
                .title("title")
                .description("desc")
                .downPaymentPercentage(BigDecimal.valueOf(20.0))
                .build();

        mockMvc.perform(post("/api/commissions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }
    @WithMockUser(username = "user")
    @Test
    void createCommission_userNotFound_shouldReturnBadRequest() throws Exception {

        var  user = userRepository.findByUsername("user");
        CreateCommissionRequest request = CreateCommissionRequest.builder()
                .commissioneeId(user.getId() + 1)
                .price(BigDecimal.valueOf(21.0))
                .title("title")
                .description("desc")
                .downPaymentPercentage(BigDecimal.valueOf(20.0))
                .build();

        mockMvc.perform(post(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("commissionee not found"));

    }
    @WithMockUser(username = "user")
    @Test
    void deleteCommission_authenticated_shouldReturnNoContent() throws Exception {

        var user = userRepository.findByUsername("user");

        var commission = commissionRepository.save(
                CommissionEntity.builder()
                        .commissionee(user)
                        .title("title")
                        .description("desc")
                        .price(BigDecimal.valueOf(21))
                        .createdAt(OffsetDateTime.now())
                        .updatedAt(OffsetDateTime.now())
                        .downPaymentPercentage(BigDecimal.valueOf(0.2))
                        .build()
        );


        mockMvc.perform(delete(baseUrl + "/" + commission.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
    @Test
    void deleteCommission_unauthenticated_shouldReturn401() throws Exception {

        var user = userRepository.findByUsername("user");

        var commission = commissionRepository.save(
                CommissionEntity.builder()
                        .commissionee(user)
                        .title("title")
                        .description("desc")
                        .price(BigDecimal.valueOf(21))
                        .createdAt(OffsetDateTime.now())
                        .updatedAt(OffsetDateTime.now())
                        .downPaymentPercentage(BigDecimal.valueOf(20.0))
                        .build()
        );


        mockMvc.perform(delete(baseUrl + "/" + commission.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }
    @Test
    void getCommission_existingCommission_shouldReturnCommission() throws Exception {
        var user = userRepository.findByUsername("user");

        var commission = commissionRepository.save(
                CommissionEntity.builder()
                        .commissionee(user)
                        .title("title")
                        .description("desc")
                        .price(BigDecimal.valueOf(21))
                        .createdAt(OffsetDateTime.now())
                        .updatedAt(OffsetDateTime.now())
                        .downPaymentPercentage(BigDecimal.valueOf(0.2))
                        .build()
        );

        mockMvc.perform(get(baseUrl + "/" + commission.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.commission.id", is(commission.getId().intValue())))
                .andExpect(jsonPath("$.commission.title", is("title")));
    }
    @Test
    void getCommission_does_not_exist_should_return_404() throws Exception {
        mockMvc.perform(get(baseUrl + "/" + 1))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is("Commission not found")));
    }
    @Test
    void getAllCommissions_exists_should_return_commissions() throws Exception {
        var user = userRepository.findByUsername("user");

        commissionRepository.save(
                CommissionEntity.builder()
                        .commissionee(user)
                        .title("title A")
                        .description("desc")
                        .price(BigDecimal.valueOf(10))
                        .createdAt(OffsetDateTime.now())
                        .updatedAt(OffsetDateTime.now())
                        .downPaymentPercentage(BigDecimal.valueOf(0.2))
                        .build()
        );

        commissionRepository.save(
                CommissionEntity.builder()
                        .commissionee(user)
                        .title("title B")
                        .description("desc2")
                        .price(BigDecimal.valueOf(20))
                        .createdAt(OffsetDateTime.now())
                        .updatedAt(OffsetDateTime.now())
                        .downPaymentPercentage(BigDecimal.valueOf(0.2))
                        .build()
        );

        mockMvc.perform(get(baseUrl))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.commissions").isArray())
                .andExpect(jsonPath("$.commissions.length()", is(2)));
    }
    @Test
    void getAllCommissions_no_commissions_found_should_return_null() throws Exception {
        mockMvc.perform(get(baseUrl))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.commissions").isArray())
                .andExpect(jsonPath("$.commissions.length()", is(0)));
    }
    @WithMockUser(username = "user")
    @Test
    void updateCommission_commissionee_exists_return_nothing() throws Exception {
        var commissionee = userRepository.findByUsername("user").fromEntity();
        Commission commission = Commission.builder()
                .title("title")
                .description("desc")
                .price(BigDecimal.valueOf(12.0))
                .commissionee(commissionee)
                .createdAt(OffsetDateTime.now())
                .updatedAt(OffsetDateTime.now())
                .downPaymentPercentage(BigDecimal.valueOf(0.2))
                .build();

        var comm = commissionRepository.save(CommissionEntity.toEntity(commission));

        UpdateCommissionRequest request = UpdateCommissionRequest.builder()
                .title("new title")
                .description("new desc")
                .price(BigDecimal.valueOf(4.0))
                .downPaymentPercentage(BigDecimal.valueOf(0.3)).build();

        mockMvc.perform(put(baseUrl + "/" + comm.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());

    }
    @WithMockUser(username = "user")
    @Test
    void updateCommission_unexisting_commissionee_throws_exception() throws Exception {

        UpdateCommissionRequest request = UpdateCommissionRequest.builder()
                .title("new title")
                .description("new desc")
                .price(BigDecimal.valueOf(4.0)).build();

        mockMvc.perform(put(baseUrl + "/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Commission not found"));
    }
}
