package org.individualproject.semester3project.usecase.user.implementation;

import lombok.AllArgsConstructor;
import org.individualproject.semester3project.domain.user.model.User;
import org.individualproject.semester3project.infrastructure.config.db.entity.UserEntity;
import org.individualproject.semester3project.infrastructure.config.db.repository.UserRepository;
import org.individualproject.semester3project.usecase.exception.EmailTakenException;
import org.individualproject.semester3project.usecase.exception.UserNotFoundException;
import org.individualproject.semester3project.usecase.exception.UsernameTakenException;
import org.individualproject.semester3project.usecase.user.UpdateUserUseCase;
import org.individualproject.semester3project.usecase.user.dto.UpdateUserInput;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@AllArgsConstructor
public class UpdateUserUseCaseImpl implements UpdateUserUseCase {

    UserRepository userRepository;
    @Override
    public void updateUser(Long userId, UpdateUserInput input) {
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new)
                .fromEntity();

        if (input.getUsername() != null && !Objects.equals(input.getUsername(), user.getUsername())) {
            var exists = userRepository.findByUsername(input.getUsername());
            if (exists == null){
                user.changeUsername(input.getUsername());
            } else {
                throw new UsernameTakenException();
            }

        }
        if (input.getEmail() != null && !Objects.equals(input.getEmail(), user.getEmail())) {
            var exists = userRepository.findByEmail(input.getEmail());
            if (exists == null){
                user.changeEmail(input.getEmail());
            } else {
                throw new EmailTakenException();
            }
        }
        userRepository.save(UserEntity.toEntity(user));
    }
}
