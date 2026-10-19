package org.individualproject.semester3project.infrastructure.config.db.repository;

import org.individualproject.semester3project.infrastructure.config.db.entity.PayoutMethodEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PayoutMethodRepository extends JpaRepository<PayoutMethodEntity,Long> {
    List<PayoutMethodEntity> findByUserIdAndActiveTrue(Long userId);

    List<PayoutMethodEntity> findByUserId(Long userId);

    boolean existsByUserIdAndActiveTrue(Long userId);
}
