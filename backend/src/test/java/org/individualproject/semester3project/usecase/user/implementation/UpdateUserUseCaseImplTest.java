package org.individualproject.semester3project.usecase.user.implementation;

import org.individualproject.semester3project.domain.user.model.User;
import org.individualproject.semester3project.infrastructure.config.db.entity.UserEntity;
import org.individualproject.semester3project.infrastructure.config.db.repository.UserRepository;
import org.individualproject.semester3project.infrastructure.user.dto.UpdateUserRequest;
import org.individualproject.semester3project.usecase.exception.EmailTakenException;
import org.individualproject.semester3project.usecase.exception.UsernameTakenException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class UpdateUserUseCaseImplTest {

    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UpdateUserUseCaseImpl updateUserUseCase;

    @Test
    void updateUser_valid_input_should_update() {

        User existingUser = User.builder()
                .id(1L)
                .username("oldName")
                .email("old@email.com")
                .build();

        UpdateUserRequest input = UpdateUserRequest.builder()
                .username("newUsername")
                .email("new@email.com").build();

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(UserEntity.toEntity(existingUser)));
        when(userRepository.findByUsername("newUsername")).thenReturn(null);
        when(userRepository.findByEmail("new@email.com")).thenReturn(null);

        updateUserUseCase.updateUser(1L, input);

        ArgumentCaptor<UserEntity> userCaptor = ArgumentCaptor.forClass(UserEntity.class);
        verify(userRepository).save(userCaptor.capture());
        UserEntity savedUser = userCaptor.getValue();
        assertEquals("newUsername", savedUser.getUsername());
        assertEquals("new@email.com", savedUser.getEmail());
    }

    @Test
    void updateUser_usernameTaken_shouldThrowException() {
        User user = User.builder()
                .id(1L)
                .username("oldName")
                .email("old@example.com")
                .build();

        User existingUser = User.builder()
                .id(2L)
                .username("takenName")
                .email("someone@example.com")
                .build();

        UpdateUserRequest input = UpdateUserRequest.builder()
                .username("takenName")
                .email("new@email.com").build();

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(UserEntity.toEntity(user)));
        when(userRepository.findByUsername("takenName"))
                .thenReturn(UserEntity.toEntity(existingUser));

        UsernameTakenException exception = assertThrows(UsernameTakenException.class, () -> updateUserUseCase.updateUser(1L, input));

        verify(userRepository).findById(1L);
        verify(userRepository).findByUsername("takenName");
        verify(userRepository, never()).save(any());
        assertEquals("Username taken", exception.getMessage());
    }

    @Test
    void updateUser_emailTaken_shouldThrowException() {

        User user = User.builder()
                .id(1L)
                .username("oldName")
                .email("old@example.com")
                .build();

        User existingUser = User.builder()
                .id(2L)
                .username("someone")
                .email("taken@example.com")
                .build();

        UpdateUserRequest request = UpdateUserRequest.builder().email("taken@example.com").build();

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(UserEntity.toEntity(user)));
        when(userRepository.findByEmail("taken@example.com"))
                .thenReturn(UserEntity.toEntity(existingUser));

        EmailTakenException exception = assertThrows(EmailTakenException.class, () -> updateUserUseCase.updateUser(1L, request));

        verify(userRepository).findById(1L);
        verify(userRepository).findByEmail("taken@example.com");
        verify(userRepository, never()).save(any());
        assertEquals("Email taken", exception.getMessage());
    }
}
