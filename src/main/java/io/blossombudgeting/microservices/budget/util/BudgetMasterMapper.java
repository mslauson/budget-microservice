/*
 * Copyright (c) 2020. Blossom Budgeting LLC
 * All Rights Reserved
 */

package io.blossombudgeting.microservices.budget.util;

import io.blossombudgeting.microservices.budget.domain.entities.BudgetEntity;
import io.blossombudgeting.microservices.budget.domain.entities.BudgetMasterEntity;
import io.blossombudgeting.util.budgetcommonutil.model.GenericCategoryModel;
import io.blossombudgeting.util.budgetcommonutil.util.DateUtils;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@Component
public class BudgetMasterMapper {

    public BudgetMasterEntity requestToEntity(GenericCategoryModel categoryModel) {
        return BudgetMasterEntity
                .builder()
                .id("master")
                .lastUpdated(DateUtils.utcTimeStamp())
                .categories(categoryModel.getCategories())
                .build();
    }

    public GenericCategoryModel entityToRequest(BudgetMasterEntity budgetMasterEntity) {
        return GenericCategoryModel
                .builder()
                .categories(budgetMasterEntity.getCategories())
                .build();
    }

    public Set<BudgetEntity> masterToBudget(String email, BudgetMasterEntity masterEntity) {
        Set<BudgetEntity> budgetEntities = new HashSet<>();
        masterEntity.getCategories().forEach(category ->
                budgetEntities.add(
                        BudgetEntity
                                .builder()
                                .email(email)
                                .monthYear(DateUtils.getFirstOfMonth())
                                .name(category.getHierarchy().get(0))
                                .categoryId(category.getCategoryId())
                                .category(category.getGroup())
                                .subCategory(category.getHierarchy().get(0))
                                .used(BigDecimal.valueOf(0))
                                .allocation(BigDecimal.valueOf(0))
                                .visible(false)
                                .linkedTransactions(new ArrayList<>())

                                .build()
                )
        );
        return budgetEntities;
    }
}
