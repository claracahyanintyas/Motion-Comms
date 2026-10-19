package org.individualproject.semester3project.usecase.user.implementation;

import org.individualproject.semester3project.infrastructure.config.db.repository.UserRepository;
import org.individualproject.semester3project.infrastructure.config.db.entity.UserEntity;
import org.individualproject.semester3project.infrastructure.user.dto.CreateUserRequest;
import org.individualproject.semester3project.usecase.exception.EmailTakenException;
import org.individualproject.semester3project.usecase.exception.UsernameTakenException;
import org.individualproject.semester3project.usecase.user.dto.CreateUserOutput;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateUserUseCaseImplTest{

    @Mock
    private UserRepository userRepoMock;

    @InjectMocks
    private CreateUserUseCaseImpl createUserUseCaseImpl;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    void createUser_valid_input_should_return_correct_userId()
    {
        // Arrange
        CreateUserRequest createUserInput = new CreateUserRequest("name", "password", "a@mail.com");

        UserEntity savedUser = UserEntity.builder()
                .id(1L)
                .username("name")
                .email("a@mail.com")
                .passwordHash("encodedPassword123")
                .build();

        when(userRepoMock.save(any(UserEntity.class))).thenReturn(savedUser);
        when(userRepoMock.findByUsername("name")).thenReturn(null);
        when(userRepoMock.findByEmail("a@mail.com")).thenReturn(null);
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword123");

        // Act
        CreateUserOutput result = createUserUseCaseImpl.createUser(createUserInput);

        // Assert
        assertEquals(savedUser.getId(), result.getId());
        verify(userRepoMock).save(any(UserEntity.class));
        verify(userRepoMock).findByUsername("name");
        verify(passwordEncoder).encode("password");
    }
    @Test
    void createUser_taken_email_should_throw_exception()
    {
        // Arrange
        CreateUserRequest createUserInput = new CreateUserRequest("name2", "password", "a@mail.com");

        UserEntity existingUser = UserEntity.builder()
                .id(1L)
                .username("name")
                .email("a@mail.com")
                .passwordHash("encodedPassword123")
                .build();

        when(userRepoMock.findByUsername("name2")).thenReturn(null);
        when(userRepoMock.findByEmail("a@mail.com")).thenReturn(existingUser);

        // Act
        EmailTakenException exception = assertThrows(EmailTakenException.class, () ->
                createUserUseCaseImpl.createUser(createUserInput));

        // Assert
        verify(userRepoMock, never()).save(any());
        verify(userRepoMock).findByEmail("a@mail.com");
        verifyNoInteractions(passwordEncoder);
        assertTrue(exception.getMessage().contains("Email taken"));
    }
    @Test
    void createUser_taken_username_should_throw_exception()
    {
        // Arrange
        CreateUserRequest createUserInput = new CreateUserRequest("name", "password", "b@mail.com");

        UserEntity existingUser = UserEntity.builder()
                .id(1L)
                .username("name")
                .email("a@mail.com")
                .passwordHash("encodedPassword123")
                .build();

        when(userRepoMock.findByUsername("name")).thenReturn(existingUser);
        when(userRepoMock.findByEmail("b@mail.com")).thenReturn(null);

        // Act
        UsernameTakenException exception = assertThrows(UsernameTakenException.class, () ->
                createUserUseCaseImpl.createUser(createUserInput));

        // Assert
        verify(userRepoMock, never()).save(any());
        verify(userRepoMock).findByEmail("b@mail.com");
        verifyNoInteractions(passwordEncoder);
        assertTrue(exception.getMessage().contains("Username taken"));
    }
}
