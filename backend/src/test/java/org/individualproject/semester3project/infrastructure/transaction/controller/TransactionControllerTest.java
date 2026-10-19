package org.individualproject.semester3project.infrastructure.transaction.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.individualproject.semester3project.domain.commissionOrder.model.CommissionOrder;
import org.individualproject.semester3project.domain.commissionOrder.model.CommissionOrderStatus;
import org.individualproject.semester3project.domain.payoutMethod.model.PayoutMethodType;
import org.individualproject.semester3project.domain.transaction.model.Transaction;
import org.individualproject.semester3project.domain.transaction.model.TransactionStatus;
import org.individualproject.semester3project.domain.transaction.model.TransactionType;
import org.individualproject.semester3project.infrastructure.config.db.entity.*;
import org.individualproject.semester3project.infrastructure.config.db.repository.*;
import org.individualproject.semester3project.usecase.media.FileStorageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

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

    private final String baseUrl = "/api/transactions";

    Long orderId;

    CommissionEntity commission;

    UserEntity user1;
    UserEntity user2;

    CommissionOrderEntity existingCommissionOrder;

    Long payoutMethodId;

    PayoutMethodEntity payoutMethod;

    TransactionEntity transactionDownPayment;
    TransactionEntity transactionRemainingPayment;

    @BeforeEach
    void setup() {
        if (!userRepository.existsByUsername("user1")) {
            user1 = new  UserEntity();
            user1.setUsername("user1");
            user1.setPasswordHash("password");
            user1.setEmail("user1@email.com");
            user1.setCreatedAt(OffsetDateTime.now());
            userRepository.save(user1);
            payoutMethod = PayoutMethodEntity.builder()
                    .user(user1)
                    .accountName("user")
                    .type(PayoutMethodType.BANK_ACCOUNT)
                    .providerName("provider")
                    .accountNumber("7308124")
                    .createdAt(OffsetDateTime.now())
                    .active(true)
                    .build();
            payoutMethodRepository.save(payoutMethod);
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

        transactionDownPayment = transactionRepository.save(TransactionEntity.builder()
                .commissionOrder(existingCommissionOrder)
                .payoutMethod(payoutMethod)
                .type(TransactionType.DOWN_PAYMENT)
                .amount(order.getDownPaymentAmount())
                        .createdAt(OffsetDateTime.now())
                        .status(TransactionStatus.PENDING)
                .proofUrl("https://dummyurl.com/proof.png")
                .build());

        transactionRemainingPayment = transactionRepository.save(TransactionEntity.builder()
                .commissionOrder(existingCommissionOrder)
                .payoutMethod(payoutMethod)
                .type(TransactionType.REMAINING_PAYMENT)
                .amount(order.getRemainingPaymentAmount())
                        .createdAt(OffsetDateTime.now())
                        .status(TransactionStatus.PENDING)
                .proofUrl("https://dummyurl.com/proof.png")
                .build());

    }
    @WithMockUser(username = "user1")
    @Test
    void confirmDownPayment_orderStatusChange() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.patch(baseUrl + "/" + transactionDownPayment.getId() + "/confirm"))
                .andExpect(status().isNoContent());
        var savedCommissionOrder = commissionOrderRepository.findById(existingCommissionOrder.getId());
        assertEquals(CommissionOrderStatus.IN_PROGRESS, savedCommissionOrder.get().getCommissionOrderStatus());
        assertTrue(savedCommissionOrder.get().isDownPaymentPaid());
    }
    @WithMockUser(username = "user1")
    @Test
    void confirmRemainingPayment_transactionNotFound_throwException() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.patch(baseUrl + "/" + 999L + "/confirm"))
                .andExpect(status().isNotFound());
    }
    @WithMockUser(username = "user1")
    @Test
    void confirmRemainingPayment_orderConfirmed_statusCompleted() throws Exception {
        existingCommissionOrder.setDownPaymentPaid(true);
        existingCommissionOrder.setConfirmedByBuyer(true);
        existingCommissionOrder.setConfirmedBySeller(true);
        existingCommissionOrder.setCommissionOrderStatus(CommissionOrderStatus.PENDING_REMAINING_PAYMENT);
        commissionOrderRepository.save(existingCommissionOrder);
        mockMvc.perform(MockMvcRequestBuilders.patch(baseUrl + "/" + transactionRemainingPayment.getId() + "/confirm"))
                .andExpect(status().isNoContent());
        var savedCommissionOrder = commissionOrderRepository.findById(existingCommissionOrder.getId());
        assertEquals(CommissionOrderStatus.COMPLETED, savedCommissionOrder.get().getCommissionOrderStatus());
        assertTrue(savedCommissionOrder.get().isFullPaymentPaid());
    }
    @WithMockUser(username = "user1")
    @Test
    void confirmRemainingPayment_confirmed_statusCompleted() throws Exception {
        existingCommissionOrder.setDownPaymentPaid(true);
        existingCommissionOrder.setConfirmedByBuyer(true);
        existingCommissionOrder.setConfirmedBySeller(true);
        existingCommissionOrder.setCommissionOrderStatus(CommissionOrderStatus.PENDING_REMAINING_PAYMENT);
        commissionOrderRepository.save(existingCommissionOrder);
        mockMvc.perform(MockMvcRequestBuilders.patch(baseUrl + "/" + transactionRemainingPayment.getId() + "/confirm"))
                .andExpect(status().isNoContent());
        var savedCommissionOrder = commissionOrderRepository.findById(existingCommissionOrder.getId());
        assertEquals(CommissionOrderStatus.COMPLETED, savedCommissionOrder.get().getCommissionOrderStatus());
        assertTrue(savedCommissionOrder.get().isFullPaymentPaid());
    }
}