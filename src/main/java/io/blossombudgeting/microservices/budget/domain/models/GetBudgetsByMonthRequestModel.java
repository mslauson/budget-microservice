/*
 * Copyright (c) 2020. Blossom Budgeting LLC
 * All Rights Reserved
 */

package io.blossombudgeting.microservices.budget.domain.models;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import java.time.LocalDate;

@ToString
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetBudgetsByMonthRequestModel {

    @Valid
    @Email
    private String email;

    @Valid
    @DateTimeFormat(pattern = "yyyy-MM-dd")

    private LocalDate monthYear;
}
