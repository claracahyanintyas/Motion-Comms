package org.individualproject.semester3project.infrastructure.user.dto;

import lombok.Builder;
import lombok.Data;
import org.individualproject.semester3project.usecase.user.dto.GetUserByEmailOutput;

import java.util.HashSet;

@Builder @Data
public class GetUserByEmailResponse implements GetUserByEmailOutput {
    private Long id;
    private String email;
    private String passwordHash;
    private HashSet<String> roles;
}
