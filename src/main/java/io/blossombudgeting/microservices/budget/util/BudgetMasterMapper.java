/*
 * Copyright (c) 2020. Blossom Budgeting LLC
 * All Rights Reserved
 */

package io.blossombudgeting.microservices.budget.util;

import io.blossombudgeting.microservices.budget.domain.entities.BudgetMasterEntity;
import io.blossombudgeting.util.budgetcommonutil.model.GenericCategoryModel;
import io.blossombudgeting.util.budgetcommonutil.util.DateUtils;
import org.springframework.stereotype.Component;

@Component
public class BudgetMasterMapper {

    public BudgetMasterEntity requestToEntity(GenericCategoryModel categoryModel){
        return BudgetMasterEntity
                .builder()
                .id("master")
                .lastUpdated(DateUtils.utcTimeStamp())
                .categories(categoryModel.getCategories())
                .build();
    }

    public GenericCategoryModel entityToRequest(BudgetMasterEntity budgetMasterEntity){
        return GenericCategoryModel
                .builder()
                .categories(budgetMasterEntity.getCategories())
                .build();
    }
}
