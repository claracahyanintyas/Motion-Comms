package org.individualproject.semester3project.infrastructure.transaction.controller;

import lombok.AllArgsConstructor;
import org.individualproject.semester3project.infrastructure.transaction.dto.ConfirmReceivedTransactionRequest;
import org.individualproject.semester3project.usecase.transaction.ConfirmReceivedTransactionUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/api/transactions")
public class TransactionController {
    private ConfirmReceivedTransactionUseCase confirmReceivedTransactionUseCase;
    @PatchMapping("/{id}/confirm")
    public ResponseEntity<Void> confirmTransaction(@PathVariable Long id){
        var request = ConfirmReceivedTransactionRequest.builder()
                .orderId(id)
                .build();
        confirmReceivedTransactionUseCase.confirm(request);
        return ResponseEntity.noContent().build();
    }
}
