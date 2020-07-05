/*
 * Copyright (c) 2020. Blossom Budgeting LLC
 * All Rights Reserved
 */

package io.blossombudgeting.microservices.budget.util;

import io.blossombudgeting.microservices.budget.domain.models.BudgetBase;
import io.blossombudgeting.microservices.budget.domain.models.UpdateBudgetRequestModel;
import io.blossombudgeting.util.budgetcommonutil.entity.BudgetEntity;
import io.blossombudgeting.util.budgetcommonutil.util.DateUtils;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class BudgetMapper {

    public BudgetEntity covertToEntity(BudgetBase request) {
        return BudgetEntity.builder()
                .email(request.getEmail().toLowerCase())
                .dateCreated(request.getDateCreated() != null ? request.getDateCreated() : DateUtils.utcTimeStamp())
                .monthYear(request.getMonthYear())
                .name(request.getName())
                .category(request.getCategory())
                .subCategory(request.getSubCategory())
                .used(request.getUsed())
                .allocation(request.getAllocation())
                .visible(request.isVisible())
                .linkedTransactions(request.getLinkedTransactions())
                .build();
    }

    public BudgetBase convertToBudgetBase(BudgetEntity budgetEntity) {
        return BudgetBase.builder()
                .id(budgetEntity.getId())
                .email(budgetEntity.getEmail().toLowerCase())
                .dateCreated(budgetEntity.getDateCreated() != null ? budgetEntity.getDateCreated() : LocalDateTime.now())
                .monthYear(budgetEntity.getMonthYear())
                .name(budgetEntity.getName())
                .category(budgetEntity.getCategory())
                .subCategory(budgetEntity.getSubCategory())
                .used(budgetEntity.getUsed())
                .allocation(budgetEntity.getAllocation())
                .visible(budgetEntity.isVisible())
                .linkedTransactions(budgetEntity.getLinkedTransactions())
                .build();
    }

    public BudgetEntity updateCurrentEntity(UpdateBudgetRequestModel request, BudgetEntity entity) {
        entity.setDateCreated(request.getDateCreated() != null ? request.getDateCreated() : DateUtils.utcTimeStamp());
        entity.setMonthYear(request.getMonthYear());
        entity.setName(request.getName());
        entity.setCategory(request.getCategory());
        entity.setSubCategory(request.getSubCategory());
        entity.setUsed(request.getUsed());
        entity.setAllocation(request.getAllocation());
        entity.setVisible(request.isVisible());
        entity.setLinkedTransactions(request.getLinkedTransactions());

        return entity;
    }

}
