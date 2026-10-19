package org.individualproject.semester3project.usecase.security.dto;

public interface LoginInput {
    String getUsernameOrEmail();
    String getPassword();
}
