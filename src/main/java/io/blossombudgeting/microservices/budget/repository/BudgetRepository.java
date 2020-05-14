/*
 * Copyright (c) 2020. Blossom Budgeting LLC
 * All Rights Reserved
 */

package io.blossombudgeting.microservices.budget.repository;

import io.blossombudgeting.util.budgetcommonutil.entity.BudgetEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BudgetRepository extends MongoRepository<BudgetEntity, String> {

    List<BudgetEntity> findAllByEmail(String email);

    List<BudgetEntity> findAllByEmailAndMonthYear(String email, String monthYear);

    List<BudgetEntity> findAllByCategory(String category);

//    List<BudgetEntity> findAllBySubCategory(String type);

    Long countAllByNameAndMonthYear(String name, LocalDate localDate);

}