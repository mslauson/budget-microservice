/*
 * Copyright (c) 2020. Blossom Budgeting LLC
 * All Rights Reserved
 */

package io.blossombudgeting.microservices.budget.repository;

import io.blossombudgeting.util.budgetcommonutil.entity.BudgetEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BudgetRepository extends MongoRepository<BudgetEntity, String> {

    List<BudgetEntity> findAllByPhone(String phone);

    List<BudgetEntity> findAllByPhoneAndMonthYear(String phone, String monthYear);

    List<BudgetEntity> findAllByCategory(String category);

    Long countAllByNameAndMonthYear(String name, String localDate);

}