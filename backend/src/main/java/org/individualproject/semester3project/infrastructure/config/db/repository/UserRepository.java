package org.individualproject.semester3project.infrastructure.config.db.repository;

import org.individualproject.semester3project.infrastructure.config.db.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByEmail(String email);
    UserEntity findByUsername(String username);
    Optional<UserEntity> findByUsernameOrEmail(String username, String email);

    boolean existsByUsername(String username);
}
