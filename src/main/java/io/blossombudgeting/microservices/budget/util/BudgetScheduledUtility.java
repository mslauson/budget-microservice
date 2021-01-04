package io.blossombudgeting.microservices.budget.util;


import io.blossombudgeting.microservices.budget.repository.BudgetRepository;
import io.blossombudgeting.util.budgetcommonutil.entity.BudgetEntity;
import io.blossombudgeting.util.budgetcommonutil.util.DateUtils;
import io.blossombudgeting.util.budgetcommonutil.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
public class BudgetScheduledUtility {

    private final BudgetRepository budgetRepository;

    public void reCreateBudgets(){
        Set<BudgetEntity> budgetEntities = budgetRepository.findAllByMonthYear(String.valueOf(DateUtils.getFirstOfMonth()));
        budgetRepository.saveAll(updateEntities(budgetEntities));

    }

    /**
     * Creates new budget entities that have the same properties as last months
     *
     * @param budgetEntities to copy
     * @return new months budgets
     */
    private Set<BudgetEntity> updateEntities(Set<BudgetEntity> budgetEntities){
        Set<BudgetEntity> nextMonthEntities = new HashSet<>();
        String newMonthYear = String.valueOf(DateUtils.getFirstOfMonth().plusMonths(1));
        budgetEntities.forEach(budgetEntity -> {
            String category = budgetEntity.getCategory();
            nextMonthEntities.add(
            BudgetEntity
                    .builder()
                    .id(StringUtils.buildStringBuffer(category, newMonthYear, budgetEntity.getPhone()))
                    .name(category)
                    .category(category)
                    .allocation(budgetEntity.getAllocation())
                    .monthYear(newMonthYear)
                    .build()
            );
        });

        return nextMonthEntities;
    }
}
