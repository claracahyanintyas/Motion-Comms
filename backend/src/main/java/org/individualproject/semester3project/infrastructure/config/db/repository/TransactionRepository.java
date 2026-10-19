package org.individualproject.semester3project.infrastructure.config.db.repository;

import org.individualproject.semester3project.domain.transaction.model.TransactionStatus;
import org.individualproject.semester3project.domain.transaction.model.TransactionType;
import org.individualproject.semester3project.infrastructure.config.db.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {
    List<TransactionEntity> findAllByCommissionOrderId(Long commissionOrderId);
    boolean existsByCommissionOrderIdAndTypeInAndStatusIn(Long commissionOrderId, List<TransactionType> type, List<TransactionStatus> status);
}
