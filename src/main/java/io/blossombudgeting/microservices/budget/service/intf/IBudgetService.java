/*
 * Copyright (c) 2020. Blossom Budgeting LLC
 * All Rights Reserved
 */

package io.blossombudgeting.microservices.budget.service.intf;

import io.blossombudgeting.microservices.budget.domain.models.BudgetRequestModel;
import io.blossombudgeting.microservices.budget.domain.models.BudgetResponseModel;

public interface IBudgetService {

    BudgetResponseModel saveBudget(BudgetRequestModel request);

    BudgetResponseModel getBudgetById(String id);

    BudgetResponseModel getAllBudgetsByUsername(String username);

    BudgetResponseModel getAllBudgetsByYear(Integer year);

    BudgetResponseModel getAllBudgetsByYearAndMonth(Integer year, Integer month);

    BudgetResponseModel getAllBudgetsByCategory(String category);

    BudgetResponseModel getAllBudgetsByType(String type);

    boolean deleteBudgetById(String id);

}
