package org.individualproject.semester3project.infrastructure.commissionOrder.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.individualproject.semester3project.domain.commissionOrder.model.CommissionOrderStatus;
import org.individualproject.semester3project.infrastructure.commissionOrder.dto.*;
import org.individualproject.semester3project.infrastructure.media.Bucket;
import org.individualproject.semester3project.infrastructure.transaction.dto.GetTransactionsByOrderRequest;
import org.individualproject.semester3project.infrastructure.transaction.dto.GetTransactionsByOrderResponse;
import org.individualproject.semester3project.security.CustomUserDetails;
import org.individualproject.semester3project.usecase.commissionOrders.*;
import org.individualproject.semester3project.usecase.commissionOrders.dto.CreateCommissionOrderOutput;
import org.individualproject.semester3project.usecase.commissionOrders.dto.GetCommissionOrdersByUserOutput;
import org.individualproject.semester3project.usecase.media.FileStorageService;
import org.individualproject.semester3project.usecase.transaction.GetTransactionsByOrderUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping("/api/commission-orders")
public class CommissionOrderController {

    private final CreateCommissionOrderUseCase createCommissionOrderUseCase;
    private final GetCommissionOrdersByUserUseCase getCommissionOrdersByUserUseCase;
    private final UpdateCommissionOrderStatusUseCase updateCommissionOrderStatusUseCase;
    private final GetCommissionOrderUseCase getCommissionOrderUseCase;
    private final ConfirmOrderByBuyerUseCase  confirmOrderByBuyerUseCase;
    private final ConfirmOrderBySellerUseCase  confirmOrderBySellerUseCase;
    private final PayPaymentUseCase payPaymentUseCase;
    private final FileStorageService fileStorageService;
    private final GetTransactionsByOrderUseCase getTransactionsByOrderUseCase;

    @PostMapping()
    public ResponseEntity<CreateCommissionOrderResponse> createCommissionOrder(@RequestBody CreateCommissionOrderRequest request){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof CustomUserDetails user)) {
            throw new AccessDeniedException("Unauthenticated");
        }
        Long userId = user.getId();
        request.setBuyerId(userId);
        CreateCommissionOrderOutput output = createCommissionOrderUseCase.createCommissionOrder(request);
        CreateCommissionOrderResponse response = CreateCommissionOrderResponse.builder().id(output.getId()).build();
        return ResponseEntity.ok().body(response);
    }
    @GetMapping("")
    public ResponseEntity<GetCommissionOrdersByUserResponse> getUserCommissionOrders(@RequestParam(required = false) String role){
        GetCommissionOrdersByUserRequest request = GetCommissionOrdersByUserRequest.builder().role(role).build();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof CustomUserDetails user)) {
            throw new AccessDeniedException("Unauthenticated");
        }
        Long userId = user.getId();
        request.setUserId(userId);
        GetCommissionOrdersByUserOutput output = getCommissionOrdersByUserUseCase.getCommissionOrdersByUser(request);

        GetCommissionOrdersByUserResponse response = GetCommissionOrdersByUserResponse.builder()
                .commissionOrders(output.getCommissionOrders())
                .build();
       return ResponseEntity.ok().body(response);
    }
    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> updateCommissionOrderStatus(@PathVariable Long id,
                                                                                     @RequestBody @Valid UpdateCommissionOrderStatusRequest request){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof CustomUserDetails user)) {
            throw new AccessDeniedException("Unauthenticated");
        }
        updateCommissionOrderStatusUseCase.updateCommissionOrderStatus(id, request);

        return ResponseEntity.noContent().build();
    }
    @GetMapping("/{id}")
    public ResponseEntity<GetCommissionOrderResponse> getCommissionOrder(@PathVariable(value = "id") final long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof CustomUserDetails user)) {
            throw new AccessDeniedException("Unauthenticated");
        }
        var request = new GetCommissionOrderRequest(id);
        var output = getCommissionOrderUseCase.getCommissionOrder(request);
        return ResponseEntity.ok(GetCommissionOrderResponse.builder()
                .commissionOrder(output.getCommissionOrder()).build());
    }
    @PatchMapping("/{id}/confirm-by-seller")
    public ResponseEntity<Void>  confirmBySeller(@PathVariable(value = "id") final long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof CustomUserDetails user)) {
            throw new AccessDeniedException("Unauthenticated");
        }
        Long userId = user.getId();
        var request = ConfirmOrderRequest.builder().userId(userId).build();
        confirmOrderBySellerUseCase.confirmBySeller(id, request);
        return ResponseEntity.noContent().build();
    }
    @PatchMapping("/{id}/confirm-by-buyer")
    public ResponseEntity<Void>  confirmByBuyer(@PathVariable(value = "id") final long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof CustomUserDetails user)) {
            throw new AccessDeniedException("Unauthenticated");
        }
        Long userId = user.getId();
        var request = ConfirmOrderRequest.builder().userId(userId).build();
        confirmOrderByBuyerUseCase.confirmByBuyer(id, request);
        return ResponseEntity.noContent().build();
    }
    @PostMapping("/{id}/pay")
    public ResponseEntity<PayPaymentResponse>  payRemaining(@RequestParam MultipartFile proofFile,
                                              @PathVariable(value = "id") final long id,
                                              PayPaymentRequest request) throws IOException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof CustomUserDetails user)) {
            throw new AccessDeniedException("Unauthenticated");
        }
        Long userId = user.getId();

        String objectName = UUID.randomUUID().toString();
        var proofUrl = fileStorageService.upload(
                proofFile.getInputStream(),
                proofFile.getSize(),
                proofFile.getContentType(),
                Bucket.TRANSACTION_PROOFS,
                objectName
        );
        request.setProofUrl(proofUrl);
        request.setOrderId(id);
        request.setUserId(userId);
        var output = payPaymentUseCase.pay(request);
        var response = PayPaymentResponse.builder()
                .id(output.getId()).build();
        return ResponseEntity.ok().body(response);
    }
    @GetMapping("/{orderId}/transactions")
    public ResponseEntity<GetTransactionsByOrderResponse> getTransactions(
            @PathVariable Long orderId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof CustomUserDetails user)) {
            throw new AccessDeniedException("Unauthenticated");
        }
        Long userId = user.getId();

        var request = GetTransactionsByOrderRequest.builder().orderId(orderId).build();
        var output = getTransactionsByOrderUseCase.getTransactionsByOrder(request);
        GetTransactionsByOrderResponse response;
        if (output.getTransactions() != null) {
            response =  GetTransactionsByOrderResponse.builder().transactions(output.getTransactions()).build();
        }
        else {
            response = GetTransactionsByOrderResponse.builder().build();
        }
        return ResponseEntity.ok().body(response);
    }
    @GetMapping("/statuses")
    public List<String> getCommissionOrderStatuses() {
        return Arrays.stream(CommissionOrderStatus.values())
                .map(Enum::name)
                .toList();
    }
}
