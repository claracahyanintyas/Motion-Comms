package org.individualproject.semester3project.infrastructure.config.db.repository;

import org.individualproject.semester3project.domain.commissionOrder.model.CommissionOrderStatus;
import org.individualproject.semester3project.infrastructure.config.db.entity.CommissionOrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommissionOrderRepository extends JpaRepository<CommissionOrderEntity, Long> {
    @Query("SELECT c FROM CommissionOrderEntity c WHERE c.buyer.id = :userId OR c.seller.id = :userId")
    List<CommissionOrderEntity> findAllByUserId(@Param("userId") Long userId);
    @Query("SELECT c FROM CommissionOrderEntity c WHERE " +
            "( :role = 'buyer' AND c.buyer.id = :userId ) OR " +
            "( :role = 'seller' AND c.seller.id = :userId )")
    List<CommissionOrderEntity> findByUserRole(@Param("userId") Long userId,
                                         @Param("role") String role);
    boolean existsByBuyerIdAndCommissionIdAndCommissionOrderStatusIn(
            Long buyerId,
            Long commissionId,
            List<CommissionOrderStatus> statuses
    );
}
