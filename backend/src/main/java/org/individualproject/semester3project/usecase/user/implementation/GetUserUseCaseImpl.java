package org.individualproject.semester3project.usecase.user.implementation;

import lombok.AllArgsConstructor;
import org.individualproject.semester3project.domain.user.model.User;
import org.individualproject.semester3project.infrastructure.config.db.repository.UserRepository;
import org.individualproject.semester3project.infrastructure.user.dto.GetUserResponse;
import org.individualproject.semester3project.usecase.exception.InvalidInputException;
import org.individualproject.semester3project.usecase.exception.UserNotFoundException;
import org.individualproject.semester3project.usecase.mapper.UserMapper;
import org.individualproject.semester3project.usecase.user.GetUserUseCase;
import org.individualproject.semester3project.usecase.user.dto.GetUserInput;
import org.individualproject.semester3project.usecase.user.dto.GetUserOutput;
import org.springframework.stereotype.Service;

@AllArgsConstructor @Service
public class GetUserUseCaseImpl implements GetUserUseCase {
    private final UserRepository userRepository;
    public GetUserOutput getUser(GetUserInput input) {
        Long id = input.getId();
        if (id < 1)
        {throw new InvalidInputException("Invalid user ID");}
        else {
            User user = userRepository.findById(id)
                    .orElseThrow(UserNotFoundException::new)
                    .fromEntity();
            var userPublicData = UserMapper.mapUserToUserPublicData(user);
            return GetUserResponse.builder()
                    .user(userPublicData)
                    .build();
        }
    }
}
