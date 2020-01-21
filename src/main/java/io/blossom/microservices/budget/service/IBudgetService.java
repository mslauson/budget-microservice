package io.blossom.microservices.budget.service;

import io.blossom.microservices.budget.domain.models.BudgetRequestModel;
import io.blossom.microservices.budget.domain.models.BudgetResponseModel;

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
