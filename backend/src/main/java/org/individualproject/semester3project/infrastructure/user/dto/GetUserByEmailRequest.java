package org.individualproject.semester3project.infrastructure.user.dto;

import lombok.Builder;
import lombok.Data;
import org.individualproject.semester3project.usecase.user.dto.GetUserByEmailInput;

@Builder @Data
public class GetUserByEmailRequest implements GetUserByEmailInput {
    private String email;
}
