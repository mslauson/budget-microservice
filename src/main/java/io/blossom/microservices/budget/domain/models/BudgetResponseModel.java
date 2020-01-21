package io.blossom.microservices.budget.domain.models;

import io.blossom.microservices.budget.domain.entities.BudgetEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Collections;
import java.util.List;

@Getter
@Setter
@ToString
public class BudgetResponseModel {

    private List<BudgetEntity> budgets;

    public BudgetResponseModel(BudgetEntity budgetEntity) {
        this.budgets = Collections.singletonList(budgetEntity);
    }

    public BudgetResponseModel(List<BudgetEntity> budgetEntities) {
        this.budgets = budgetEntities;
    }

}
