/*
 * Copyright (c) 2020. Blossom Budgeting LLC
 * All Rights Reserved
 */

package io.blossombudgeting.microservices.budget.domain.models;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class BudgetBase {

    private String id;

    @NotBlank
    @Size(min = 4, max = 30)
    @Email
    private String email;

    private LocalDateTime dateCreated;

    @NotNull
    private LocalDate monthYear;

    @NotBlank
    @Size(max = 50)
    private String name;

    @NotBlank
    @Size(max = 50)
    private String category;

    @NotBlank
    @Size(max = 50)
    private String subCategory;

    @NotNull
    private BigDecimal used;

    @NotNull
    private BigDecimal allocation;

    private boolean visible;

}
