/*
 * Copyright (c) 2020. Blossom Budgeting LLC
 * All Rights Reserved
 */

package io.blossombudgeting.microservices.budget.service.impl;

import io.blossombudgeting.microservices.budget.domain.entities.BudgetEntity;
import io.blossombudgeting.microservices.budget.domain.models.BudgetBase;
import io.blossombudgeting.microservices.budget.domain.models.BudgetResponseModel;
import io.blossombudgeting.microservices.budget.error.BudgetNotFoundException;
import io.blossombudgeting.microservices.budget.repository.BudgetRepository;
import io.blossombudgeting.microservices.budget.service.intf.IBudgetService;
import io.blossombudgeting.microservices.budget.util.BudgetMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
public class BudgetServiceImpl implements IBudgetService {

    private final BudgetRepository budgetRepo;
    private final BudgetMapper entityBuilder;

    @Autowired
    public BudgetServiceImpl(BudgetRepository budgetRepo, BudgetMapper entityBuilder) {
        this.budgetRepo = budgetRepo;
        this.entityBuilder = entityBuilder;
    }

    @Override
    public BudgetResponseModel saveBudget(BudgetBase request) {
        log.info("saveBudget: request=[{}]", request);
        BudgetEntity budgetEntity = entityBuilder.covertToEntity(request);
        return new BudgetResponseModel(budgetRepo.save(budgetEntity));
    }

    @Override
    public BudgetResponseModel getBudgetById(String id) {
        log.info("getBudgetById: id=[{}]", id);
        BudgetEntity budget = budgetRepo
                .findById(id)
                .orElseThrow(() -> new BudgetNotFoundException("Budget with ID [" + id + "] not found"));
        return new BudgetResponseModel(budget);
    }

    @Override
    public BudgetResponseModel getAllBudgetsByUsername(String username) {
        log.info("getAllBudgetsByUsername: username=[{}]", username);
        return new BudgetResponseModel(budgetRepo.findAllByEmail(username));
    }

    @Override
    public BudgetResponseModel getAllBudgetsByYearAndMonth(LocalDate monthYear) {
        log.info("getAllBudgetsByYearAndMonth");
        List<BudgetEntity> budgets = budgetRepo.findAllByMonthYear(monthYear);
        if (budgets.isEmpty()){
            throw new BudgetNotFoundException("No budgets were found for this month -> {"+monthYear+"}");
        }
        return new BudgetResponseModel(budgets);
    }

    @Override
    public BudgetResponseModel getAllBudgetsByCategory(String category) {
        log.info("getAllBudgetsByCategory: category=[{}]", category);
        return new BudgetResponseModel(budgetRepo.findAllByCategory(category));
    }

    @Override
    public BudgetResponseModel getAllBudgetsByType(String type) {
        log.info("getAllBudgetsByType: type=[{}]", type);
        return new BudgetResponseModel(budgetRepo.findAllBySubCategory(type));
    }

    @Override
    public boolean deleteBudgetById(String id) {
        log.info("deleteBudget: id=[{}]", id);
        budgetRepo.deleteById(id);
        return !budgetRepo.existsById(id);
    }

}
