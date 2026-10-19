package org.individualproject.semester3project.infrastructure.user.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.individualproject.semester3project.infrastructure.commission.dto.CreateCommissionResponse;
import org.individualproject.semester3project.infrastructure.user.dto.*;
import org.individualproject.semester3project.usecase.user.CreateUserUseCase;
import org.individualproject.semester3project.usecase.user.DeleteUserUseCase;
import org.individualproject.semester3project.usecase.user.GetUserUseCase;
import org.individualproject.semester3project.usecase.user.UpdateUserUseCase;
import org.individualproject.semester3project.usecase.user.dto.CreateUserOutput;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/users")

public class UserController {
    CreateUserUseCase createUserUseCase;
    GetUserUseCase getUserUseCase;
    UpdateUserUseCase updateUserUseCase;
    DeleteUserUseCase deleteUserUseCase;

    @PostMapping("/register")
    public ResponseEntity<CreateUserResponse> CreateUser(@RequestBody @Valid CreateUserRequest request){
        CreateUserOutput output = createUserUseCase.createUser(request);
        CreateUserResponse response = CreateUserResponse.builder().id(output.getId()).build();
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetUserResponse> getUser(@PathVariable(value = "id") final long id) {
        var request = new GetUserRequest(id);
        var output = getUserUseCase.getUser(request);
        return ResponseEntity.ok(GetUserResponse.builder()
                .user(output.getUser()).build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<CreateCommissionResponse> updateCommission(@PathVariable Long id, @RequestBody UpdateUserRequest request){
        updateUserUseCase.updateUser(id, request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable(value = "id") final long id) {
        deleteUserUseCase.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
