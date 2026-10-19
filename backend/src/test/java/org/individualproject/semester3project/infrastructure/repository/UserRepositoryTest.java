package org.individualproject.semester3project.infrastructure.repository;

import org.individualproject.semester3project.infrastructure.config.db.entity.UserEntity;
import org.individualproject.semester3project.infrastructure.config.db.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.OffsetDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class UserRepositoryTest {
    @Autowired
    UserRepository userRepository;
    @Autowired
    TestEntityManager entityManager;

    @Test
    void givenNewUser_whenSave_returnSuccess(){
        UserEntity user = UserEntity.builder()
                .username("name")
                .email("email")
                .createdAt(OffsetDateTime.now())
                .passwordHash("hash")
                .build();
        UserEntity saved = userRepository.save(user);
        assertThat(entityManager.find(UserEntity.class, saved.getId())).isEqualTo(saved);
    }
}
