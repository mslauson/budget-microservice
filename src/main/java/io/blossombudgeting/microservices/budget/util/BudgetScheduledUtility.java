/*
 * Copyright (c) 2021. Blossom Budgeting LLC
 * All Rights Reserved
 */

package io.blossombudgeting.microservices.budget.util;


import io.blossombudgeting.microservices.budget.repository.BudgetRepository;
import io.blossombudgeting.util.budgetcommonutil.encryption.BlossomEncryptionUtility;
import io.blossombudgeting.util.budgetcommonutil.entity.BudgetEntity;
import io.blossombudgeting.util.budgetcommonutil.util.DateUtils;
import io.blossombudgeting.util.budgetcommonutil.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class BudgetScheduledUtility {

    private final BudgetRepository budgetRepository;
    private final BlossomEncryptionUtility encryptionUtility;

    @Scheduled(cron = "${budget.recreate}")
    public void reCreateBudgets(){
        long start = System.currentTimeMillis();
        Set<BudgetEntity> budgetEntities = budgetRepository.findAllByMonthYear(String.valueOf(DateUtils.getFirstOfMonth()));
        budgetRepository.saveAll(updateEntities(budgetEntities));
        log.info("reCreateBudgets execution time -> {}ms", System.currentTimeMillis() - start);
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
            String decryptedCat = encryptionUtility.decrypt(category);
            nextMonthEntities.add(
            BudgetEntity
                    .builder()
                    .phone(budgetEntity.getPhone())
                    .id(encryptionUtility.encrypt(StringUtils.buildStringBuffer(decryptedCat, newMonthYear, budgetEntity.getPhone())))
                    .name(category)
                    .category(category)
                    .allocation(budgetEntity.getAllocation())
                    .monthYear(newMonthYear)
                    .build()
            );
        });
        log.info("created {} budgets for {}", nextMonthEntities.size(), newMonthYear);
        return nextMonthEntities;
    }
}
