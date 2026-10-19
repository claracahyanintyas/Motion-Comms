package org.individualproject.semester3project.infrastructure.config.db.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.individualproject.semester3project.domain.role.model.Role;

@Entity
@Table(name = "roles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    public static RoleEntity toEntity(Role role) {
        return RoleEntity.builder()
                .id(role.getId())
                .name(role.getName())
                .build();
    }
    public Role fromEntity() {
        return Role.builder()
                .id(id)
                .name(name)
                .build();
    }
}