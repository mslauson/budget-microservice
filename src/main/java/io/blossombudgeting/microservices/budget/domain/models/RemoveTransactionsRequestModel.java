package io.blossombudgeting.microservices.budget.domain.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class RemoveTransactionsRequestModel {
    private List<String> transactionIds;
}
