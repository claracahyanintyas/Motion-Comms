package org.individualproject.semester3project.usecase.user.implementation;

import lombok.AllArgsConstructor;
import org.individualproject.semester3project.domain.user.model.User;
import org.individualproject.semester3project.infrastructure.config.db.repository.UserRepository;
import org.individualproject.semester3project.infrastructure.config.db.entity.UserEntity;
import org.individualproject.semester3project.infrastructure.user.dto.CreateUserResponse;
import org.individualproject.semester3project.usecase.exception.EmailTakenException;
import org.individualproject.semester3project.usecase.exception.UsernameTakenException;
import org.individualproject.semester3project.usecase.user.CreateUserUseCase;
import org.individualproject.semester3project.usecase.user.dto.CreateUserInput;
import org.individualproject.semester3project.usecase.user.dto.CreateUserOutput;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service @AllArgsConstructor
public class CreateUserUseCaseImpl implements CreateUserUseCase {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @Override
    public CreateUserOutput createUser(CreateUserInput input) {
        UserEntity existingUserByEmail = userRepository.findByEmail(input.getEmail());
        UserEntity existingUserByUsername = userRepository.findByUsername(input.getUsername());
        if (existingUserByEmail != null) {
            throw new EmailTakenException();
        }
        if (existingUserByUsername != null) {
            throw new UsernameTakenException();
        }
        User newUser = User.builder()
                .username(input.getUsername())
                .passwordHash(passwordEncoder.encode(input.getPassword()))
                .email(input.getEmail())
                .build();
        User returnedUser = userRepository.save(UserEntity.toEntity(newUser)).fromEntity();
        return CreateUserResponse.builder()
                .id(returnedUser.getId())
                .build();
    }
}
