package org.individualproject.semester3project.usecase.security;

import org.individualproject.semester3project.usecase.security.dto.JwtAuthOutput;
import org.individualproject.semester3project.usecase.security.dto.LoginInput;

public interface AuthService {
    JwtAuthOutput login(LoginInput loginInput);
}
