package org.individualproject.semester3project.infrastructure.user.dto;

import lombok.Builder;
import lombok.Data;
import org.individualproject.semester3project.usecase.user.dto.GetUserOutput;
import org.individualproject.semester3project.usecase.user.dto.UserPublicData;

@Builder @Data
public class GetUserResponse implements GetUserOutput {
    private UserPublicData user;
}
