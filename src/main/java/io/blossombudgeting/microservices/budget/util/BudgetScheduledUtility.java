package io.blossombudgeting.microservices.budget.util;


import io.blossombudgeting.microservices.budget.repository.BudgetRepository;
import io.blossombudgeting.util.budgetcommonutil.entity.BudgetEntity;
import io.blossombudgeting.util.budgetcommonutil.util.DateUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;

@Slf4j
@RequiredArgsConstructor
public class BudgetScheduledUtility {

    private final BudgetRepository budgetRepository;

    public void reCreateBudgets(){
        Set<BudgetEntity> budgetEntities = budgetRepository.findAllByMonthYear(String.valueOf(DateUtils.getFirstOfMonth()));

    }
}
