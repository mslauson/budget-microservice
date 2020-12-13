package io.blossombudgeting.microservices.budget.domain.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@AllArgsConstructor
public class RemoveTransactionsRequestModel {

    @NotBlank
    private final String phone;

    @NotNull
    private final List<String> transactionIds;
}
