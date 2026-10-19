package org.individualproject.semester3project.infrastructure.security.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.individualproject.semester3project.usecase.security.dto.LoginInput;

@Data @AllArgsConstructor
@NoArgsConstructor @Builder
public class LoginRequest implements LoginInput {
    private String usernameOrEmail;
    private String password;
}
