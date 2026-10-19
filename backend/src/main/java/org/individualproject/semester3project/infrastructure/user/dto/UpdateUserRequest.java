package org.individualproject.semester3project.infrastructure.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.individualproject.semester3project.usecase.user.dto.UpdateUserInput;

@Data @Builder @NoArgsConstructor
@AllArgsConstructor
public class UpdateUserRequest implements UpdateUserInput {
    String username;
    String email;
}
