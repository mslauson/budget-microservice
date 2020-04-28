/*
 * Copyright (c) 2020. Blossom Budgeting LLC
 * All Rights Reserved
 */

package io.blossombudgeting.microservices.budget.domain.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class BudgetRequestModel {

    private String id;

    @Size(min = 4, max = 30)
    private String username;

    private LocalDateTime dateCreated;

    @Min(1970)
    @Max(9999)
    private Integer year;

    @Min(1)
    @Max(12)
    private Integer month;

    @Size(max = 50)
    private String name;

    @Size(max = 50)
    private String category;

    @Size(max = 50)
    private String type;

    private BigDecimal target;

    private BigDecimal allocation;

}
