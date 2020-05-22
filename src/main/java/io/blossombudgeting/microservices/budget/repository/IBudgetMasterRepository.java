/*
 * Copyright (c) 2020. Blossom Budgeting LLC
 * All Rights Reserved
 */

package io.blossombudgeting.microservices.budget.repository;

import io.blossombudgeting.util.budgetcommonutil.entity.BudgetMasterEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface IBudgetMasterRepository extends MongoRepository<BudgetMasterEntity, String> {
}
