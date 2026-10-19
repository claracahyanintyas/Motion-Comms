package org.individualproject.semester3project.usecase.user.dto;

import java.util.HashSet;

public interface GetUserByEmailOutput {
    Long getId();
    String getEmail();
    String getPasswordHash();
    HashSet<String> getRoles();
}
