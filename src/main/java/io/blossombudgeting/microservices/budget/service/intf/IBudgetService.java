/*
 * Copyright (c) 2020. Blossom Budgeting LLC
 * All Rights Reserved
 */

package io.blossombudgeting.microservices.budget.service.intf;

import io.blossombudgeting.microservices.budget.domain.models.BudgetBase;
import io.blossombudgeting.microservices.budget.domain.models.BudgetResponseModel;

import java.time.LocalDate;

public interface IBudgetService {

    BudgetResponseModel saveBudget(BudgetBase request);

    BudgetResponseModel getBudgetById(String id);

    BudgetResponseModel getAllBudgetsByUsername(String username);

    BudgetResponseModel getAllBudgetsByYearAndMonth(LocalDate monthYear);

    BudgetResponseModel getAllBudgetsByCategory(String category);

    BudgetResponseModel getAllBudgetsByType(String type);

    boolean deleteBudgetById(String id);

}
