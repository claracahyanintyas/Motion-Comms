package org.individualproject.semester3project.usecase.user;

import org.individualproject.semester3project.usecase.user.dto.CreateUserInput;
import org.individualproject.semester3project.usecase.user.dto.CreateUserOutput;

public interface CreateUserUseCase {
    CreateUserOutput createUser(CreateUserInput createUserInput);
}
