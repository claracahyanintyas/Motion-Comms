package org.individualproject.semester3project.infrastructure.commissionOrder.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.individualproject.semester3project.domain.commissionOrder.model.CommissionOrder;
import org.individualproject.semester3project.domain.commissionOrder.model.CommissionOrderStatus;
import org.individualproject.semester3project.domain.payoutMethod.model.PayoutMethodType;
import org.individualproject.semester3project.domain.transaction.model.TransactionStatus;
import org.individualproject.semester3project.domain.transaction.model.TransactionType;
import org.individualproject.semester3project.infrastructure.commissionOrder.dto.*;
import org.individualproject.semester3project.infrastructure.config.db.entity.*;
import org.individualproject.semester3project.infrastructure.config.db.repository.*;
import org.individualproject.semester3project.security.CustomUserDetails;
import org.individualproject.semester3project.usecase.media.FileStorageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

import static org.hamcrest.Matchers.greaterThan;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class CommissionOrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommissionRepository commissionRepository;

    @Autowired
    private CommissionOrderRepository commissionOrderRepository;

    @Autowired
    private PayoutMethodRepository payoutMethodRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @MockitoBean
    private FileStorageService fileStorageService;

    private final String baseUrl = "/api/commission-orders";

    Long orderId;

    CommissionEntity commission;

    UserEntity user1;
    UserEntity user2;

    CommissionOrderEntity existingCommissionOrder;

    Long payoutMethodId;

    PayoutMethodEntity existingPayoutMethod;

    @BeforeEach
    void setup() {
        if (!userRepository.existsByUsername("user1")) {
            user1 = new  UserEntity();
            user1.setUsername("user1");
            user1.setPasswordHash("password");
            user1.setEmail("user1@email.com");
            user1.setCreatedAt(OffsetDateTime.now());
            userRepository.save(user1);
            var payoutMethod = PayoutMethodEntity.builder()
                    .user(user1)
                    .accountName("user")
                    .type(PayoutMethodType.BANK_ACCOUNT)
                    .providerName("provider")
                    .accountNumber("7308124")
                    .createdAt(OffsetDateTime.now())
                    .active(true)
                    .build();
            existingPayoutMethod = payoutMethodRepository.save(payoutMethod);
            payoutMethodId = payoutMethod.getId();
        }
        if (!userRepository.existsByUsername("user2")) {
            user2 = new  UserEntity();
            user2.setUsername("user2");
            user2.setPasswordHash("password");
            user2.setEmail("user2@email.com");
            user2.setCreatedAt(OffsetDateTime.now());
            userRepository.save(user2);
        }

        commission = commissionRepository.save(CommissionEntity.builder()
                .commissionee(user1)
                .title("title")
                .description("desc")
                .price(BigDecimal.valueOf(100))
                .createdAt(OffsetDateTime.now())
                .updatedAt(OffsetDateTime.now())
                .downPaymentPercentage(BigDecimal.valueOf(0.3))
                .build());

        CommissionOrder order = new CommissionOrder(commission.fromEntity(), user2.fromEntity());

        existingCommissionOrder = commissionOrderRepository.save(CommissionOrderEntity.toEntity(order));

        orderId = existingCommissionOrder.getId();



        when(fileStorageService.upload(any(), anyLong(), anyString(), any(), anyString()))
                .thenReturn("https://dummyurl.com/proof.png");
    }

    @BeforeEach
    void setupSecurityContext() {
        // pick the user you want for this test (user1 or user2)
        UserEntity user = userRepository.findByUsername("user1"); // or "user2"

        CustomUserDetails userDetails = new CustomUserDetails(
                user.getId(),
                user.getUsername(),
                user.getPasswordHash(),
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                )
        );
    }

    @Test
    void createCommissionOrder_shouldSaveAndReturnId() throws Exception {
        var user2 = userRepository.findByUsername("user2");

        var commission2 = commissionRepository.save(CommissionEntity.builder()
                .commissionee(user2)
                .title("title")
                .description("desc")
                .price(BigDecimal.valueOf(100))
                .createdAt(OffsetDateTime.now())
                .updatedAt(OffsetDateTime.now())
                .downPaymentPercentage(BigDecimal.valueOf(0.3))
                .build());
        payoutMethodRepository.save(PayoutMethodEntity.builder()
                .user(user2)
                .accountName("user")
                .type(PayoutMethodType.BANK_ACCOUNT)
                .providerName("provider")
                .accountNumber("7308124")
                .createdAt(OffsetDateTime.now())
                .active(true)
                .build());

        CreateCommissionOrderRequest request = CreateCommissionOrderRequest.builder()
                .commissionId(commission2.getId())
                .build();

        mockMvc.perform(MockMvcRequestBuilders.post(baseUrl )
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(greaterThan(0)));
    }

    @Test
    void createCommissionOrder_sellerHasNoPayoutMethod_shouldThrowException() throws Exception {
        var buyer = user2;
        var seller = UserEntity.builder()
                .username("user2")
                .passwordHash("password")
                .email("email")
                .createdAt(OffsetDateTime.now())
                .build();
        userRepository.save(seller);
        commission = commissionRepository.save(CommissionEntity.builder()
                .commissionee(seller)
                .title("title")
                .description("desc")
                .price(BigDecimal.valueOf(100))
                .createdAt(OffsetDateTime.now())
                .updatedAt(OffsetDateTime.now())
                .downPaymentPercentage(BigDecimal.valueOf(0.3))
                .build());

        CreateCommissionOrderRequest request = CreateCommissionOrderRequest.builder()
                .commissionId(commission.getId())
                .buyerId(buyer.getId())
                .build();

        mockMvc.perform(MockMvcRequestBuilders.post(baseUrl )
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Commissionee / seller has no active payout method"));
    }

    @Test
    void createCommissionOrder_UnexistingCommission_shouldSaveAndReturnId() throws Exception {
        var buyer = user1;

        CreateCommissionOrderRequest request = CreateCommissionOrderRequest.builder()
                .commissionId(999L)
                .buyerId(buyer.getId())
                .build();

        mockMvc.perform(MockMvcRequestBuilders.post(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Commission not found"));
    }

    @Test
    void getUserCommissionOrders_shouldReturnOrders() throws Exception {
        var commission2 = commissionRepository.save(
                CommissionEntity.builder()
                        .commissionee(user2)
                        .title("title2")
                        .description("desc2")
                        .price(BigDecimal.valueOf(21))
                        .createdAt(OffsetDateTime.now())
                        .updatedAt(OffsetDateTime.now())
                        .downPaymentPercentage(BigDecimal.valueOf(0.3))
                        .build()
        );

        CommissionOrder commissionOrder = new CommissionOrder(commission2.fromEntity(), user1.fromEntity());

        commissionOrderRepository.save(CommissionOrderEntity.toEntity(commissionOrder));


        mockMvc.perform(MockMvcRequestBuilders.get(baseUrl )
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.commissionOrders").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.commissionOrders.length()").value(2));
    }
    @Test
    void getUserCommissionOrders_withRoleBuyer_shouldReturnOrders() throws Exception {

        var commission2 = commissionRepository.save(
                CommissionEntity.builder()
                        .commissionee(user2)
                        .title("title2")
                        .description("desc2")
                        .price(BigDecimal.valueOf(21))
                        .createdAt(OffsetDateTime.now())
                        .updatedAt(OffsetDateTime.now())
                        .downPaymentPercentage(BigDecimal.valueOf(0.5))
                        .build()
        );

        CommissionOrder commissionOrder = new CommissionOrder(commission2.fromEntity(), user1.fromEntity());

        commissionOrderRepository.save(CommissionOrderEntity.toEntity(commissionOrder));


        mockMvc.perform(MockMvcRequestBuilders.get(baseUrl)
                        .param("role", "buyer")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.commissionOrders").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.commissionOrders.length()").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.commissionOrders[0].buyer.username").value("user1"));
    }
    @Test
    void getUserCommissionOrders_withRoleSeller_shouldReturnOrders() throws Exception {
        var commission2 = commissionRepository.save(
                CommissionEntity.builder()
                        .commissionee(user2)
                        .title("title2")
                        .description("desc2")
                        .price(BigDecimal.valueOf(21))
                        .createdAt(OffsetDateTime.now())
                        .updatedAt(OffsetDateTime.now())
                        .downPaymentPercentage(BigDecimal.valueOf(0.5))
                        .build()
        );

        CommissionOrder commissionOrder = new CommissionOrder(commission2.fromEntity(), user1.fromEntity());

        commissionOrderRepository.save(CommissionOrderEntity.toEntity(commissionOrder));

        mockMvc.perform(MockMvcRequestBuilders.get(baseUrl )
                        .param("role", "seller")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.commissionOrders").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.commissionOrders.length()").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.commissionOrders[0].seller.username").value("user1"));
    }

    @Test
    void updateOrderStatus_happyPath_shouldReturnNoContent() throws Exception {
        var request = UpdateCommissionOrderStatusRequest.builder()
                .commissionOrderStatus(CommissionOrderStatus.CANCELLED)
                .build();

        mockMvc.perform(MockMvcRequestBuilders.patch(baseUrl + "/" + orderId + "/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());
    }

    @Test
    void updateOrderStatus_nullStatus_shouldReturnNoContent() throws Exception {
        var request = new UpdateCommissionOrderStatusRequest(); // null status

        mockMvc.perform(MockMvcRequestBuilders.patch(baseUrl + "/" + orderId + "/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());
    }
    @Test
    void getOrder_happyPath_shouldReturnOk() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(baseUrl + "/" + orderId))
                .andExpect(status().isOk());
    }

    @Test
    void getOrder_notFound_shouldReturn404() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(baseUrl + "/999999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void payDownPayment_shouldReturnTransactionId() throws Exception {
        UserEntity user2 = userRepository.findByUsername("user2");

        // dynamically set SecurityContext to user2
        CustomUserDetails userDetails = new CustomUserDetails(
                user2.getId(),
                user2.getUsername(),
                user2.getPasswordHash(),
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                )
        );
        // Arrange
        MockMultipartFile proofFile = new MockMultipartFile(
                "proofFile",
                "proof.png",
                "image/png",
                "dummy content".getBytes()
        );

        PayPaymentRequest request = PayPaymentRequest.builder()
                .type(TransactionType.DOWN_PAYMENT)
                .payoutMethodId(payoutMethodId)
                .amount(BigDecimal.valueOf(30))
                .build();

        when(fileStorageService.upload(any(), anyLong(), anyString(), any(), anyString()))
                .thenReturn("https://dummyurl.com/proof.png");

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.multipart(baseUrl + "/" + orderId + "/pay")
                        .file(proofFile)
                        .param("type", request.getType().name())
                        .param("payoutMethodId", request.getPayoutMethodId().toString())
                        .param("amount", request.getAmount().toString())
                        .with(req -> { req.setMethod("POST"); return req; })
                )
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNotEmpty());

        // Verify the transaction was saved correctly
        var transactions = transactionRepository.findAll();
        assertEquals(1, transactions.size());
        var tx = transactions.getFirst();
        assertEquals(TransactionType.DOWN_PAYMENT, tx.getType());
        assertEquals(request.getAmount(), tx.getAmount());
        assertEquals(orderId, tx.getCommissionOrder().getId());
        assertEquals("https://dummyurl.com/proof.png", tx.getProofUrl());
    }
    @Test
    void payDownPayment_ongoingTransactionExists_throwsException() throws Exception {
        UserEntity user2 = userRepository.findByUsername("user2");

        // dynamically set SecurityContext to user2
        CustomUserDetails userDetails = new CustomUserDetails(
                user2.getId(),
                user2.getUsername(),
                user2.getPasswordHash(),
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                )
        );
        // Arrange
        MockMultipartFile proofFile = new MockMultipartFile(
                "proofFile",
                "proof.png",
                "image/png",
                "dummy content".getBytes()
        );

        PayPaymentRequest request = PayPaymentRequest.builder()
                .type(TransactionType.DOWN_PAYMENT)
                .payoutMethodId(payoutMethodId)
                .amount(BigDecimal.valueOf(30))
                .build();

        when(fileStorageService.upload(any(), anyLong(), anyString(), any(), anyString()))
                .thenReturn("https://dummyurl.com/proof.png");

        transactionRepository.save(TransactionEntity.builder()
                .payoutMethod(existingPayoutMethod)
                .commissionOrder(existingCommissionOrder)
                .type(TransactionType.DOWN_PAYMENT)
                .amount(BigDecimal.valueOf(15))
                        .proofUrl("https://dummyurl.com/proof.png")
                        .createdAt(OffsetDateTime.now())
                        .status(TransactionStatus.PENDING)
                .build());

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.multipart(baseUrl + "/" + orderId + "/pay")
                        .file(proofFile)
                        .param("type", request.getType().name())
                        .param("payoutMethodId", request.getPayoutMethodId().toString())
                        .param("amount", request.getAmount().toString())
                        .with(req -> { req.setMethod("POST"); return req; })
                )
                .andExpect(status().isConflict());
    }

    @Test
    void payRemaining_shouldReturnTransactionId() throws Exception {
        UserEntity user2 = userRepository.findByUsername("user2");

        // dynamically set SecurityContext to user2
        CustomUserDetails userDetails = new CustomUserDetails(
                user2.getId(),
                user2.getUsername(),
                user2.getPasswordHash(),
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                )
        );
        // Arrange
        var order = commissionOrderRepository.findById(orderId).get();
        order.setDownPaymentPaid(true);
        MockMultipartFile proofFile = new MockMultipartFile(
                "proofFile",
                "proof.png",
                "image/png",
                "dummy content".getBytes()
        );

        PayPaymentRequest request = PayPaymentRequest.builder()
                .type(TransactionType.REMAINING_PAYMENT)
                .payoutMethodId(payoutMethodId)
                .amount(BigDecimal.valueOf(70))
                .build();

        when(fileStorageService.upload(any(), anyLong(), anyString(), any(), anyString()))
                .thenReturn("https://dummyurl.com/proof.png");

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.multipart(baseUrl + "/" + orderId + "/pay")
                        .file(proofFile)
                        .param("type", request.getType().name())
                        .param("payoutMethodId", request.getPayoutMethodId().toString())
                        .param("amount", request.getAmount().toString())
                        .with(req -> { req.setMethod("POST"); return req; })
                )
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNotEmpty());

        // Verify the transaction was saved correctly
        var transactions = transactionRepository.findAll();
        assertEquals(1, transactions.size());
        var tx = transactions.get(0);
        assertEquals(TransactionType.REMAINING_PAYMENT, tx.getType());
        assertEquals(request.getAmount(), tx.getAmount());
        assertEquals(orderId, tx.getCommissionOrder().getId());
        assertEquals("https://dummyurl.com/proof.png", tx.getProofUrl());
    }
    @Test
    void payRemaining_downPaymentNotPaid_ThrowsException() throws Exception {
        UserEntity user2 = userRepository.findByUsername("user2");

        // dynamically set SecurityContext to user2
        CustomUserDetails userDetails = new CustomUserDetails(
                user2.getId(),
                user2.getUsername(),
                user2.getPasswordHash(),
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                )
        );
        // Arrange
        MockMultipartFile proofFile = new MockMultipartFile(
                "proofFile",
                "proof.png",
                "image/png",
                "dummy content".getBytes()
        );

        PayPaymentRequest request = PayPaymentRequest.builder()
                .type(TransactionType.REMAINING_PAYMENT)
                .payoutMethodId(1L)
                .amount(BigDecimal.valueOf(70))
                .build();

        when(fileStorageService.upload(any(), anyLong(), anyString(), any(), anyString()))
                .thenReturn("https://dummyurl.com/proof.png");

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.multipart(baseUrl + "/" + orderId + "/pay")
                        .file(proofFile)
                        .param("type", request.getType().name())
                        .param("payoutMethodId", request.getPayoutMethodId().toString())
                        .param("amount", request.getAmount().toString())
                        .with(req -> { req.setMethod("POST"); return req; })
                )
                .andExpect(status().isConflict());

    }

    @Test
    void confirmByBuyer_shouldReturnNoContent() throws Exception {
        UserEntity user2 = userRepository.findByUsername("user2");

        // dynamically set SecurityContext to user2
        CustomUserDetails userDetails = new CustomUserDetails(
                user2.getId(),
                user2.getUsername(),
                user2.getPasswordHash(),
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                )
        );
        // Prepare order state
        CommissionOrderEntity order = commissionOrderRepository.findById(orderId).orElseThrow();
        order.setFullPaymentPaid(true);
        order.setConfirmedBySeller(true);
        order.setCommissionOrderStatus(CommissionOrderStatus.AWAITING_CONFIRMATION);
        commissionOrderRepository.save(order);

        ConfirmOrderRequest req = ConfirmOrderRequest.builder()
                .userId(user2.getId()).build();

        mockMvc.perform(MockMvcRequestBuilders.patch(baseUrl + "/" + orderId + "/confirm-by-buyer")
                        .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isNoContent());

        // Optional: verify final status
        order = commissionOrderRepository.findById(orderId).orElseThrow();
        assertTrue(order.isConfirmedByBuyer());
        assertEquals(CommissionOrderStatus.COMPLETED, order.getCommissionOrderStatus());
    }

    @Test
    void confirmBySeller_shouldReturnNoContent() throws Exception {
        // Prepare order state
        CommissionOrderEntity order = commissionOrderRepository.findById(orderId).orElseThrow();
        order.setFullPaymentPaid(true);
        order.setConfirmedByBuyer(true);
        order.setCommissionOrderStatus(CommissionOrderStatus.IN_PROGRESS);
        commissionOrderRepository.save(order);

        mockMvc.perform(MockMvcRequestBuilders.patch(baseUrl + "/" + orderId + "/confirm-by-seller")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        // Optional: verify final status
        order = commissionOrderRepository.findById(orderId).orElseThrow();
        assertTrue(order.isConfirmedBySeller());
        assertEquals(CommissionOrderStatus.COMPLETED, order.getCommissionOrderStatus());
    }

    @Test
    void shouldReturnTransactionsWhenTheyExist() throws Exception {
        var transaction = TransactionEntity.builder()
                    .amount(existingCommissionOrder.getDownPaymentAmount())
                    .status(TransactionStatus.PENDING)
                    .type(TransactionType.DOWN_PAYMENT)
                    .proofUrl("https://dummyurl.com/proof.png")
                    .commissionOrder(existingCommissionOrder)
                    .createdAt(OffsetDateTime.now())
                    .payoutMethod(existingPayoutMethod)
                    .build();
        var saved = transactionRepository.save(transaction);

        mockMvc.perform(get(baseUrl + "/" + orderId + "/transactions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transactions").isArray())
                .andExpect(jsonPath("$.transactions.length()").value(1))
                .andExpect(jsonPath("$.transactions[0].commissionOrder.id").value(orderId))
                .andExpect(jsonPath("$.transactions[0].id").value(saved.getId()));
    }

    @Test
    void getTransactions_noCommissionOrder_shouldThrowException() throws Exception {

        mockMvc.perform(get("/" + 999L + "/transactions"))
                .andExpect(status().isNotFound());
    }

}
