package org.individualproject.semester3project.infrastructure.config.db.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.validator.constraints.Length;
import org.individualproject.semester3project.domain.user.model.User;

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "users")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotBlank
    @Length(min = 1 ,max = 100)
    @Column(name = "username")
    private String username;

    @NotBlank
    @Length(min = 1 ,max = 255)
    @Column(name = "email")
    private String email;

    @NotBlank
    @Length(min = 1 ,max = 255)
    @Column(name = "password_hash")
    private String passwordHash;

    @NotNull
    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    private OffsetDateTime createdAt;

    @Builder.Default
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<RoleEntity> roles = new HashSet<>();

    public static UserEntity toEntity(User user) {
        return UserEntity.builder()
                .id(user.getId())
                .email(user.getEmail())
                .username(user.getUsername())
                .passwordHash(user.getPasswordHash())
                .createdAt(user.getCreatedAt())
                .roles(user.getRoles() == null ? new HashSet<>() :
                        user.getRoles().stream().map(RoleEntity::toEntity).collect(Collectors.toSet()))
                .build();
    }

    public User fromEntity(){

        return User.builder()
                .id(id)
                .username(username)
                .email(email)
                .passwordHash(passwordHash)
                .createdAt(createdAt)
                .roles(roles == null ? new HashSet<>() :
                        roles.stream().map(RoleEntity::fromEntity).collect(Collectors.toSet()))
                .build();
    }

}