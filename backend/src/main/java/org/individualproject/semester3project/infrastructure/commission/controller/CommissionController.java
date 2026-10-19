package org.individualproject.semester3project.infrastructure.commission.controller;

import org.individualproject.semester3project.infrastructure.commission.dto.*;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.individualproject.semester3project.usecase.commission.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.individualproject.semester3project.usecase.commission.dto.CreateCommissionOutput;

@AllArgsConstructor
@RestController
@RequestMapping("/api/commissions")
public class CommissionController {

    private final CreateCommissionUseCase createCommissionUseCase;
    private final GetCommissionUseCase getCommissionUseCase;
    private final DeleteCommissionUseCase deleteCommissionUseCase;
    private final GetAllCommissionsUseCase getAllCommissionsUseCase;
    private final UpdateCommissionUseCase updateCommissionUseCase;

    @PostMapping()
    public ResponseEntity<CreateCommissionResponse> createCommission(@RequestBody @Valid CreateCommissionRequest request){
        CreateCommissionOutput output = createCommissionUseCase.createCommission(request);
        CreateCommissionResponse response = CreateCommissionResponse.builder().id(output.getId()).build();
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetCommissionResponse> getCommission(@PathVariable(value = "id") final long id) {
        var request = new GetCommissionRequest(id);
        var output = getCommissionUseCase.getCommission(request);
        return ResponseEntity.ok(GetCommissionResponse.builder()
                .commission(output.getCommission()).build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCommission(@PathVariable Long id) {
        deleteCommissionUseCase.deleteCommission(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("")
    public ResponseEntity<GetAllCommissionsResponse> getCommissions() {
        var output = getAllCommissionsUseCase.getCommissions();
        if (output == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(
                GetAllCommissionsResponse.builder()
                        .commissions(output.getCommissions())
                        .build()
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateCommission(@PathVariable Long id, @RequestBody UpdateCommissionRequest request){
        updateCommissionUseCase.updateCommission(id, request);
        return ResponseEntity.noContent().build();
    }

}
