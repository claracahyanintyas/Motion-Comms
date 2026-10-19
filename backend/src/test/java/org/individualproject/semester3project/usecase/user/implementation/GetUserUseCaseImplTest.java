package org.individualproject.semester3project.usecase.user.implementation;

import org.individualproject.semester3project.infrastructure.config.db.repository.UserRepository;
import org.individualproject.semester3project.infrastructure.config.db.entity.UserEntity;
import org.individualproject.semester3project.infrastructure.user.dto.GetUserRequest;
import org.individualproject.semester3project.usecase.exception.InvalidInputException;
import org.individualproject.semester3project.usecase.exception.UserNotFoundException;
import org.individualproject.semester3project.usecase.user.dto.GetUserOutput;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetUserUseCaseImplTest {

    @Mock
    private UserRepository userRepositoryMock;

    @InjectMocks
    private GetUserUseCaseImpl getUserUseCaseImpl;

    @Test
    void getUser_should_return_right_value() {
        GetUserRequest request = GetUserRequest.builder().id(1L).build();
        UserEntity savedUser = UserEntity.builder()
                .id(1L)
                .username("name")
                .passwordHash("pass")
                .email("a@mail.com")
                .build();

        when(userRepositoryMock.findById(1L)).thenReturn(Optional.ofNullable(savedUser));

        GetUserOutput actual = getUserUseCaseImpl.getUser(request);

        assertEquals(1L,actual.getUser().getId());
        assertEquals("name",actual.getUser().getUsername());
        assertEquals("a@mail.com",actual.getUser().getEmail());
        verify(userRepositoryMock).findById(1L);
        verifyNoMoreInteractions(userRepositoryMock);

    }

    @Test
    void getUser_unexisting_user_should_throw_exception() {
        GetUserRequest request = GetUserRequest.builder().id(1L).build();

        when(userRepositoryMock.findById(1L)).thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () ->
                getUserUseCaseImpl.getUser(request));

        verify(userRepositoryMock).findById(1L);
        verifyNoMoreInteractions(userRepositoryMock);
        assertTrue(exception.getMessage().contains("User not found"));
    }
    @Test
    void getUser_invalid_id_should_throw_exception() {
        GetUserRequest request = GetUserRequest.builder().id(-1L).build();

        InvalidInputException exception = assertThrows(InvalidInputException.class, () ->
                getUserUseCaseImpl.getUser(request));

        verifyNoInteractions(userRepositoryMock);
        assertTrue(exception.getMessage().contains("Invalid user ID"));
    }

}