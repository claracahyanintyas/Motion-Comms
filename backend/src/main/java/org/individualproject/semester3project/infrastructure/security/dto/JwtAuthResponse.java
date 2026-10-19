package org.individualproject.semester3project.infrastructure.security.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.individualproject.semester3project.usecase.security.dto.JwtAuthOutput;
import org.individualproject.semester3project.usecase.user.dto.UserPublicData;

@Data @AllArgsConstructor
@NoArgsConstructor @Builder
public class JwtAuthResponse implements JwtAuthOutput {
    private String accessToken;
    @Builder.Default
    private String tokenType = "Bearer";
    private UserPublicData user;
}
