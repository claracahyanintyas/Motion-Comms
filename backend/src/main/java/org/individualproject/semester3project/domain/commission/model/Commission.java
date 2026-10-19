package org.individualproject.semester3project.domain.commission.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.individualproject.semester3project.domain.user.model.User;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Builder
@Getter
@AllArgsConstructor

public class Commission {
    private Long id;
    private String title;
    private String description;
    private BigDecimal price;
    private User commissionee;
    private BigDecimal downPaymentPercentage;
    @Builder.Default
    private OffsetDateTime createdAt =  OffsetDateTime.now();
    @Builder.Default
    private OffsetDateTime updatedAt =  OffsetDateTime.now();

    public void changeTitle(String newTitle) {
        this.title = newTitle;
    }
    public void changeDescription(String newDescription) {
        this.description = newDescription;
    }
    public void changePrice(BigDecimal newPrice) {
        this.price = newPrice;
    }
    public void changeDownPaymentPercentage(BigDecimal newPercentage) {
        this.downPaymentPercentage = newPercentage;
    }
}
