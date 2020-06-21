/*
 * Copyright (c) 2020. Blossom Budgeting LLC
 * All Rights Reserved
 */

package io.blossombudgeting.microservices.budget.domain.models;

import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@ToString
@Getter
@Setter
@NoArgsConstructor
public class GetBudgetsByMonthRequestModel {

    @Valid
    @Email
    private String email;

    @Valid
    @NotBlank
    private String monthYear;
}
