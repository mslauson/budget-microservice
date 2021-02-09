/*
 * Copyright (c) 2021. Blossom Budgeting LLC
 * All Rights Reserved
 */

package io.blossombudgeting.microservices.budget.service.intf;

import io.blossombudgeting.microservices.budget.domain.models.*;
import io.blossombudgeting.util.budgetcommonutil.model.GenericSuccessResponseModel;

public interface IBudgetService {

    BudgetResponseModel saveBudget(BudgetBase request);

    BudgetResponseModel getBudgetById(String id);

    BudgetResponseModel getAllBudgetsByPhone(String phone);

    BudgetResponseModel getAllBudgetsByYearAndMonth(GetBudgetsByMonthRequestModel requestModel);

    BudgetResponseModel getAllBudgetsByCategory(String category);

    BudgetResponseModel getAllBudgetsBySubCategory(String type);

    GenericSuccessResponseModel updateBudget(UpdateBudgetRequestModel requestModel);

    GenericSuccessResponseModel deleteBudgetById(String id);

    GenericSuccessResponseModel removeTransactionsWhenAccountDeleted(RemoveTransactionsRequestModel requestModel);

    CategoriesModel refreshCategories(CategoriesModel requestModel);

}
