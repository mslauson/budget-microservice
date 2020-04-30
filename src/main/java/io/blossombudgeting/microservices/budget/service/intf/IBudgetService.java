/*
 * Copyright (c) 2020. Blossom Budgeting LLC
 * All Rights Reserved
 */

package io.blossombudgeting.microservices.budget.service.intf;

import io.blossombudgeting.microservices.budget.domain.models.BudgetBase;
import io.blossombudgeting.microservices.budget.domain.models.BudgetResponseModel;
import io.blossombudgeting.util.budgetcommonutil.model.GenericSuccessResponseModel;

import java.time.LocalDate;

public interface IBudgetService {

    BudgetResponseModel saveBudget(BudgetBase request);

    BudgetResponseModel getBudgetById(String id);

    BudgetResponseModel getAllBudgetsByEmail(String username);

    BudgetResponseModel getAllBudgetsByYearAndMonth(LocalDate monthYear);

    BudgetResponseModel getAllBudgetsByCategory(String category);

    BudgetResponseModel getAllBudgetsBySubCategory(String type);

    GenericSuccessResponseModel deleteBudgetById(String id);

}
