/*
 * Copyright (c) 2020. Blossom Budgeting LLC
 * All Rights Reserved
 */

package io.blossombudgeting.microservices.budget.domain.models;

import io.blossombudgeting.microservices.budget.domain.entities.BudgetEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Collections;
import java.util.List;

@Getter
@Setter
@ToString
public class BudgetResponseModel {

    private BudgetResponseStatusModel responseStatus;
    private List<BudgetEntity> budgets;

    public BudgetResponseModel(BudgetEntity budget) {
        this.responseStatus = new BudgetResponseStatusModel(budget);
        this.budgets = Collections.singletonList(budget);
    }

    public BudgetResponseModel(List<BudgetEntity> budgets) {
        this.responseStatus = new BudgetResponseStatusModel(budgets);
        this.budgets = budgets;
    }

}
