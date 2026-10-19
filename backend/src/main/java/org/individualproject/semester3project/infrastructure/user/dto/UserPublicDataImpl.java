package org.individualproject.semester3project.infrastructure.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.individualproject.semester3project.usecase.user.dto.UserPublicData;

@Data @AllArgsConstructor @Builder
public class UserPublicDataImpl implements UserPublicData {
    private Long id;
    private String username;
    private String email;
}
