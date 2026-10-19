package org.individualproject.semester3project.infrastructure.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.individualproject.semester3project.usecase.user.dto.CreateUserInput;

@Builder @AllArgsConstructor @Data
public class CreateUserRequest implements CreateUserInput {
    String username;
    String password;
    String email;
}
