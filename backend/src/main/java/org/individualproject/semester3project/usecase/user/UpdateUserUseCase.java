package org.individualproject.semester3project.usecase.user;

import org.individualproject.semester3project.usecase.user.dto.UpdateUserInput;

public interface UpdateUserUseCase {
    void updateUser(Long userId, UpdateUserInput input);
}
