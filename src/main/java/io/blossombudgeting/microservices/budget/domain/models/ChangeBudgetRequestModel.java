/*
 * Copyright (c) 2021. Blossom Budgeting LLC
 * All Rights Reserved
 */

package io.blossombudgeting.microservices.budget.domain.models;

import lombok.*;

import javax.validation.constraints.NotBlank;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChangeBudgetRequestModel {

    @NotBlank
    private String phone;

    @NotBlank
    private String currentBudgetId;

    @NotBlank
    private String newBudgetId;

    @NotBlank
    private String transactionId;
}
