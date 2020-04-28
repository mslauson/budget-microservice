/*
 * Copyright (c) 2020. Blossom Budgeting LLC
 * All Rights Reserved
 */

package io.blossombudgeting.microservices.budget.service.intf;

import io.blossombudgeting.util.budgetcommonutil.model.GenericCategoryModel;
import io.blossombudgeting.util.budgetcommonutil.model.GenericSuccessResponseModel;

public interface IBudgetAdminService {
    GenericSuccessResponseModel updateMasterRecords(GenericCategoryModel request);
    GenericCategoryModel getMasterRecords(String id);
}
