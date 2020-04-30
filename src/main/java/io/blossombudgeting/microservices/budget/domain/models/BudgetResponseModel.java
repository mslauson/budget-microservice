/*
 * Copyright (c) 2020. Blossom Budgeting LLC
 * All Rights Reserved
 */

package io.blossombudgeting.microservices.budget.domain.models;

import io.blossombudgeting.microservices.budget.domain.entities.BudgetEntity;
import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class BudgetResponseModel {

    private List<BudgetEntity> budgets;

}
