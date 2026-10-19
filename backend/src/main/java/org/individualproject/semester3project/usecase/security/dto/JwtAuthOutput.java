package org.individualproject.semester3project.usecase.security.dto;

import org.individualproject.semester3project.usecase.user.dto.UserPublicData;

public interface JwtAuthOutput {
    String getAccessToken();
    String getTokenType();
    UserPublicData getUser();
}
