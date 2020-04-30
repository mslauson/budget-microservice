/*
 * Copyright (c) 2020. Blossom Budgeting LLC
 * All Rights Reserved
 */

package io.blossombudgeting.microservices.budget.util;

import io.blossombudgeting.microservices.budget.domain.entities.BudgetEntity;
import io.blossombudgeting.microservices.budget.domain.models.BudgetBase;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class BudgetMapper {

    public BudgetEntity covertToEntity(BudgetBase request) {
        return BudgetEntity.builder()
                .id(request.getId())
                .email(request.getEmail().toUpperCase())
                .dateCreated(request.getDateCreated() != null ? request.getDateCreated() : LocalDateTime.now())
                .monthYear(request.getMonthYear())
                .name(request.getName().toUpperCase())
                .category(request.getCategory().toUpperCase())
                .subCategory(request.getSubCategory().toUpperCase())
                .used(request.getUsed())
                .allocation(request.getAllocation())
                .build();
    }

    public BudgetBase convertToBudgetBase(BudgetEntity budgetEntity) {
        return BudgetBase.builder()
                .id(budgetEntity.getId())
                .email(budgetEntity.getEmail().toUpperCase())
                .dateCreated(budgetEntity.getDateCreated() != null ? budgetEntity.getDateCreated() : LocalDateTime.now())
                .monthYear(budgetEntity.getMonthYear())
                .name(budgetEntity.getName().toUpperCase())
                .category(budgetEntity.getCategory().toUpperCase())
                .subCategory(budgetEntity.getSubCategory().toUpperCase())
                .used(budgetEntity.getUsed())
                .allocation(budgetEntity.getAllocation())
                .build();
    }

}
