package org.individualproject.semester3project.domain.user.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.individualproject.semester3project.domain.role.model.Role;

import java.time.OffsetDateTime;
import java.util.Set;

@Builder @Data
@AllArgsConstructor
public class User {
    private Long id;
    private String username;
    private String passwordHash;
    private String email;
    @Builder.Default
    private OffsetDateTime createdAt =  OffsetDateTime.now();
    private Set<Role> roles;
    public void changeUsername(String username){
        this.username = username;
    }
    public void changeEmail(String email){
        this.email = email;
    }
}
