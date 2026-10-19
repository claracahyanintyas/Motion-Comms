package org.individualproject.semester3project.usecase.user;

import org.individualproject.semester3project.usecase.user.dto.GetUserInput;
import org.individualproject.semester3project.usecase.user.dto.GetUserOutput;

public interface GetUserUseCase {
    GetUserOutput getUser(GetUserInput getUserInput);
}
