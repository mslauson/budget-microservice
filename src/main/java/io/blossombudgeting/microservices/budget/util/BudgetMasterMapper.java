/*
 * Copyright (c) 2020. Blossom Budgeting LLC
 * All Rights Reserved
 */

package io.blossombudgeting.microservices.budget.util;

import io.blossombudgeting.util.budgetcommonutil.entity.BudgetEntity;
import io.blossombudgeting.util.budgetcommonutil.entity.BudgetMasterEntity;
import io.blossombudgeting.util.budgetcommonutil.model.GenericCategoryModel;
import io.blossombudgeting.util.budgetcommonutil.util.DateUtils;
import org.springframework.stereotype.Component;

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
                                .monthYear(String.valueOf(DateUtils.getFirstOfMonth()))
                                .name(category.getHierarchy().get(0).equals("Shops") ? "Shopping" : category.getHierarchy().get(0))
                                .category(category.getGroup())
//                                .subCategory(category.getHierarchy().get(category.getHierarchy().size() - 1))
                                .used(0D)
                                .allocation(0D)
                                .visible(false)
                                .linkedTransactions(new ArrayList<>())

                                .build()
                )
        );
        return budgetEntities;
    }
}
