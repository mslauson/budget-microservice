package io.blossombudgeting.microservices.budget.domain.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

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
