/*
 * Copyright (c) 2020. blossom.io.
 * All rights reserved.
 */

package io.blossom.microservices.budget.repository;

import io.blossom.microservices.budget.domain.entities.BudgetEntity;
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