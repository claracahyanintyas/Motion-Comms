package org.individualproject.semester3project.domain.payoutMethod.model;

import lombok.Builder;
import lombok.Getter;
import org.individualproject.semester3project.domain.user.model.User;

import java.time.OffsetDateTime;

@Builder
@Getter
public class PayoutMethod {
    private Long id;
    private User user;
    private PayoutMethodType type;
    private String accountName;
    private String accountNumber;
    private String providerName;

    @Builder.Default
    private OffsetDateTime createdAt =  OffsetDateTime.now();

    @Builder.Default
    private boolean active = true;

    private OffsetDateTime deletedAt;

    public void deactivate() {
        this.active = false;
        this.deletedAt = OffsetDateTime.now();
    }

    public void changeAccountName(String accountName) {
        this.accountName = accountName;
    }
    public void changeAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }
    public void changeProviderName(String providerName) {
        this.providerName = providerName;
    }
    public void changeType(PayoutMethodType type) {
        this.type = type;
    }
}
