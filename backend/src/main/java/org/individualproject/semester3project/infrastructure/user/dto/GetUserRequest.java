package org.individualproject.semester3project.infrastructure.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.individualproject.semester3project.usecase.user.dto.GetUserInput;

@Data @AllArgsConstructor
@NoArgsConstructor @Builder
public class GetUserRequest implements GetUserInput {
    Long id;
}
