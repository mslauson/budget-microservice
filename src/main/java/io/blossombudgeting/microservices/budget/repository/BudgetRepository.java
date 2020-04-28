/*
 * Copyright (c) 2020. Blossom Budgeting LLC
 * All Rights Reserved
 */

package io.blossombudgeting.microservices.budget.repository;

import io.blossombudgeting.microservices.budget.domain.entities.BudgetEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface BudgetRepository extends MongoRepository<BudgetEntity, String> {

    List<BudgetEntity> findAllByUsername(String username);

    List<BudgetEntity> findAllByYear(Integer year);

    List<BudgetEntity> findAllByCategory(String category);

    List<BudgetEntity> findAllByType(String type);

}