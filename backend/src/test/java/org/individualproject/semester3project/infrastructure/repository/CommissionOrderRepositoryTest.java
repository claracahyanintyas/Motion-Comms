package org.individualproject.semester3project.infrastructure.repository;

import org.individualproject.semester3project.domain.commissionOrder.model.CommissionOrderStatus;
import org.individualproject.semester3project.infrastructure.config.db.entity.CommissionEntity;
import org.individualproject.semester3project.infrastructure.config.db.entity.CommissionOrderEntity;
import org.individualproject.semester3project.infrastructure.config.db.entity.UserEntity;
import org.individualproject.semester3project.infrastructure.config.db.repository.CommissionOrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class CommissionOrderRepositoryTest {
    @Autowired
    CommissionOrderRepository commissionOrderRepository;
    @Autowired
    TestEntityManager entityManager;

    UserEntity user1;
    UserEntity user2;
    CommissionEntity commission1;
    CommissionEntity commission2;

    @BeforeEach
    void setup(){
        user1 = UserEntity.builder()
                .email("email1")
                .username("username1")
                .passwordHash("password1")
                .createdAt(OffsetDateTime.now())
                .build();
        entityManager.persist(user1);
        user2 = UserEntity.builder()
                .email("email2")
                .username("username2")
                .passwordHash("password2")
                .createdAt(OffsetDateTime.now())
                .build();
        entityManager.persist(user2);
        commission1 = CommissionEntity.builder()
                .title("title")
                .price(BigDecimal.valueOf(10))
                .description("description")
                .downPaymentPercentage(BigDecimal.valueOf(50))
                .commissionee(user2)
                .createdAt(OffsetDateTime.now())
                .updatedAt(OffsetDateTime.now())
                .build();
        commission2 = CommissionEntity.builder()
                .title("title")
                .price(BigDecimal.valueOf(10))
                .description("description")
                .downPaymentPercentage(BigDecimal.valueOf(50))
                .commissionee(user1)
                .createdAt(OffsetDateTime.now())
                .updatedAt(OffsetDateTime.now())
                .build();
        entityManager.persist(commission1);
        entityManager.persist(commission2);
    }

    @Test
    void givenNewOrder_whenSave_returnSuccess()
    {
        var commissionOrderEntity = CommissionOrderEntity.builder()
                .commission(commission1)
                .seller(user2)
                .buyer(user1)
                .commissionOrderStatus(CommissionOrderStatus.PENDING_DOWN_PAYMENT)
                .downPaymentAmount(BigDecimal.valueOf(5))
                .remainingPaymentAmount(BigDecimal.valueOf(5))
                .totalPrice(BigDecimal.valueOf(10))
                .createdAt(OffsetDateTime.now())
                .updatedAt(OffsetDateTime.now())
                .build();
        var saved = commissionOrderRepository.save(commissionOrderEntity);
        assertThat(entityManager.find(CommissionOrderEntity.class, saved.getId())).isEqualTo(saved);
    }
    @Test
    void findByUserId_returnSuccess()
    {
        var commissionOrderEntity1 = CommissionOrderEntity.builder()
                .commission(commission1)
                .seller(user2)
                .buyer(user1)
                .commissionOrderStatus(CommissionOrderStatus.PENDING_DOWN_PAYMENT)
                .downPaymentAmount(BigDecimal.valueOf(5))
                .remainingPaymentAmount(BigDecimal.valueOf(5))
                .totalPrice(BigDecimal.valueOf(10))
                .createdAt(OffsetDateTime.now())
                .updatedAt(OffsetDateTime.now())
                .build();
        var commissionOrderEntity2 = CommissionOrderEntity.builder()
                .commission(commission2)
                .seller(user1)
                .buyer(user2)
                .commissionOrderStatus(CommissionOrderStatus.PENDING_DOWN_PAYMENT)
                .downPaymentAmount(BigDecimal.valueOf(5))
                .remainingPaymentAmount(BigDecimal.valueOf(5))
                .totalPrice(BigDecimal.valueOf(10))
                .createdAt(OffsetDateTime.now())
                .updatedAt(OffsetDateTime.now())
                .build();
        entityManager.persist(commissionOrderEntity1);
        entityManager.persist(commissionOrderEntity2);
        List<CommissionOrderEntity> savedList = commissionOrderRepository.findAllByUserId(user1.getId());
        assertThat(savedList).hasSize(2);
        assertThat(savedList).contains(commissionOrderEntity1);
    }
    @Test
    void findByUserRole_returnSuccess()
    {
        var commissionOrderEntity1 = CommissionOrderEntity.builder()
                .commission(commission1)
                .seller(user2)
                .buyer(user1)
                .commissionOrderStatus(CommissionOrderStatus.PENDING_DOWN_PAYMENT)
                .downPaymentAmount(BigDecimal.valueOf(5))
                .remainingPaymentAmount(BigDecimal.valueOf(5))
                .totalPrice(BigDecimal.valueOf(10))
                .createdAt(OffsetDateTime.now())
                .updatedAt(OffsetDateTime.now())
                .build();
        var commissionOrderEntity2 = CommissionOrderEntity.builder()
                .commission(commission2)
                .seller(user1)
                .buyer(user2)
                .commissionOrderStatus(CommissionOrderStatus.PENDING_DOWN_PAYMENT)
                .downPaymentAmount(BigDecimal.valueOf(5))
                .remainingPaymentAmount(BigDecimal.valueOf(5))
                .totalPrice(BigDecimal.valueOf(10))
                .createdAt(OffsetDateTime.now())
                .updatedAt(OffsetDateTime.now())
                .build();
        entityManager.persist(commissionOrderEntity1);
        entityManager.persist(commissionOrderEntity2);
        List<CommissionOrderEntity> savedList = commissionOrderRepository.findByUserRole(user1.getId(), "seller");
        assertThat(savedList).hasSize(1);
        assertThat(savedList).contains(commissionOrderEntity2);
    }
    @Test
    void findByUser_whenFindByUser_returnSuccess()
    {
        var commissionOrderEntity1 = CommissionOrderEntity.builder()
                .commission(commission1)
                .seller(user2)
                .buyer(user1)
                .commissionOrderStatus(CommissionOrderStatus.PENDING_DOWN_PAYMENT)
                .downPaymentAmount(BigDecimal.valueOf(5))
                .remainingPaymentAmount(BigDecimal.valueOf(5))
                .totalPrice(BigDecimal.valueOf(10))
                .createdAt(OffsetDateTime.now())
                .updatedAt(OffsetDateTime.now())
                .build();
        entityManager.persist(commissionOrderEntity1);
        List<CommissionOrderStatus> statuses = List.of(CommissionOrderStatus.PENDING_DOWN_PAYMENT);
        boolean bool = commissionOrderRepository.existsByBuyerIdAndCommissionIdAndCommissionOrderStatusIn(user1.getId(), commission1.getId(), statuses);
        assertThat(bool).isTrue();
    }
}
