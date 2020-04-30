/*
 * Copyright (c) 2020. Blossom Budgeting LLC
 * All Rights Reserved
 */

package io.blossombudgeting.microservices.budget.domain.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@ToString
@NoArgsConstructor
public class BudgetBase {

    @NotBlank
    private String id;

    @NotBlank
    @Size(min = 4, max = 30)
    private String email;

    @NotNull
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

}
