package org.individualproject.semester3project.infrastructure.paymentMethod.controller;


import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.individualproject.semester3project.domain.payoutMethod.model.PayoutMethodType;
import org.individualproject.semester3project.infrastructure.paymentMethod.dto.*;
import org.individualproject.semester3project.usecase.payoutMethod.*;
import org.individualproject.semester3project.usecase.payoutMethod.dto.CreatePayoutMethodOutput;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/payout-methods")
public class PayoutMethodController {

    CreatePayoutMethodUseCase createPayoutMethodUseCase;
    GetActivePayoutMethodsByUserUseCase getActivePayoutMethodsByUserUseCase;
    GetPayoutMethodUseCase getPayoutMethodUseCase;
    UpdatePayoutMethodUseCase updatePayoutMethodUseCase;
    SoftDeletePayoutMethodUseCase softDeletePayoutMethodUseCase;

    @PostMapping("/create")
    public ResponseEntity<CreatePayoutMethodResponse> createCommission(HttpServletRequest httpRequest, @RequestBody CreatePayoutMethodRequest request){
        CreatePayoutMethodOutput output = createPayoutMethodUseCase.createPayoutMethod(request);
        CreatePayoutMethodResponse response = CreatePayoutMethodResponse.builder().id(output.getId()).build();
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<GetActivePayoutMethodsByUserResponse> getActivePayoutMethodsByUser(HttpServletRequest httpRequest, @PathVariable Long id){
        var request = GetActivePayoutMethodsByUserRequest.builder().userId(id).build();
        var output = getActivePayoutMethodsByUserUseCase.getActivePayoutMethodByUser(request);
        var response = GetActivePayoutMethodsByUserResponse.builder()
                .payoutMethods(output.getPayoutMethods()).build();
        return ResponseEntity.ok().body(response);

    }

    @GetMapping("/{id}")
    public ResponseEntity<GetPayoutMethodResponse> getPayoutMethod(HttpServletRequest httpRequest, @PathVariable Long id){
        var request = GetPayoutMethodRequest.builder().id(id).build();
        var output = getPayoutMethodUseCase.getPayoutMethod(request);
        var response = GetPayoutMethodResponse.builder()
                .payoutMethod(output.getPayoutMethod()).build();
        return ResponseEntity.ok().body(response);

    }

    @GetMapping("/types")
    public List<String> getPayoutMethodTypes() {
        return Arrays.stream(PayoutMethodType.values())
                .map(Enum::name)
                .toList();
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<Void> updatePayoutMethod(HttpServletRequest httpRequest, @PathVariable Long id, @RequestBody UpdatePayoutMethodRequest request){
        updatePayoutMethodUseCase.updatePayoutMethod(id, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePayoutMethod(HttpServletRequest httpRequest, @PathVariable Long id){
        softDeletePayoutMethodUseCase.softDeletePayoutMethod(id);
        return ResponseEntity.ok().build();
    }

}
