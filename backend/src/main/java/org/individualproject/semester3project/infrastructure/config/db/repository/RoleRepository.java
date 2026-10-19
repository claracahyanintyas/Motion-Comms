package org.individualproject.semester3project.infrastructure.config.db.repository;

import org.individualproject.semester3project.infrastructure.config.db.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
    RoleEntity findByName(String name);
}
