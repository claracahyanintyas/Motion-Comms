package org.individualproject.semester3project.usecase.mapper;

import org.individualproject.semester3project.domain.user.model.User;
import org.individualproject.semester3project.infrastructure.user.dto.UserPublicDataImpl;
import org.individualproject.semester3project.usecase.user.dto.UserPublicData;

public class UserMapper {
    public static UserPublicData mapUserToUserPublicData(User user){
        return UserPublicDataImpl.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .build();
    }
}
