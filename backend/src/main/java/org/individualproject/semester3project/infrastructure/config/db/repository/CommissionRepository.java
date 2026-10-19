package org.individualproject.semester3project.infrastructure.config.db.repository;

import org.individualproject.semester3project.infrastructure.config.db.entity.CommissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommissionRepository extends JpaRepository<CommissionEntity, Long> {
}
