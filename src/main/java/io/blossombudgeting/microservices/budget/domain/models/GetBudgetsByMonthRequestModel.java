/*
 * Copyright (c) 2020. Blossom Budgeting LLC
 * All Rights Reserved
 */

package io.blossombudgeting.microservices.budget.domain.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@ToString
@Getter
@Setter
@NoArgsConstructor
public class GetBudgetsByMonthRequestModel {

    @Valid
    private String phone;

    @Valid
    @NotBlank
    private String monthYear;
}
