package org.individualproject.semester3project.usecase.user.implementation;

import lombok.AllArgsConstructor;
import org.individualproject.semester3project.infrastructure.config.db.repository.UserRepository;
import org.individualproject.semester3project.usecase.user.DeleteUserUseCase;
import org.springframework.stereotype.Service;

@Service @AllArgsConstructor
public class DeleteUserUseCaseImpl implements DeleteUserUseCase {
    UserRepository userRepository;
    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
