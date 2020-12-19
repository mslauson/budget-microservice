package io.blossombudgeting.microservices.budget.domain.models;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RemoveTransactionsRequestModel {

    @NotBlank
    private String phone;

    @NotNull
    private List<String> transactionIds;
}
