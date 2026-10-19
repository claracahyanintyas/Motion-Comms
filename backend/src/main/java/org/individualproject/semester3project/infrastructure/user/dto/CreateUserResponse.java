package org.individualproject.semester3project.infrastructure.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.individualproject.semester3project.usecase.user.dto.CreateUserOutput;

@Builder @AllArgsConstructor @Data
public class CreateUserResponse implements CreateUserOutput {
    Long id;
}
