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
import io.blossombudgeting.util.budgetcommonutil.model.GenericSuccessResponseModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BudgetServiceImpl implements IBudgetService {

    private final BudgetRepository budgetRepo;
    private final BudgetMapper budgetMapper;

    @Override
    public BudgetResponseModel saveBudget(BudgetBase request) {
        log.info("saveBudget: request=[{}]", request);
        BudgetEntity budgetEntity = budgetMapper.covertToEntity(request);
        BudgetBase budgetBase = budgetMapper.convertToBudgetBase(budgetRepo.save(budgetEntity));
        return new BudgetResponseModel(Collections.singletonList(budgetBase));
    }

    @Override
    public BudgetResponseModel getBudgetById(String id) {
        log.info("getBudgetById: id=[{}]", id);
        BudgetEntity budgetEntity = budgetRepo
                .findById(id)
                .orElseThrow(() -> new BudgetNotFoundException("Budget with ID [" + id + "] not found"));
        BudgetBase budget = budgetMapper.convertToBudgetBase(budgetEntity);
        return new BudgetResponseModel(Collections.singletonList(budget));
    }

    @Override
    public BudgetResponseModel getAllBudgetsByUsername(String email) {
        log.info("getAllBudgetsByUsername: email=[{}]", email);
        List<BudgetBase> budgetBases = budgetRepo.findAllByEmail(email)
                .stream()
                .map(budgetMapper::convertToBudgetBase)
                .collect(Collectors.toList());
        if (budgetBases.isEmpty()){
            throw new BudgetNotFoundException("No budgets were found for this user -> { "+email+" }");
        }
        return new BudgetResponseModel(budgetBases);
    }

    @Override
    public BudgetResponseModel getAllBudgetsByYearAndMonth(LocalDate monthYear) {
        log.info("getAllBudgetsByYearAndMonth");
        List<BudgetBase> budgets = budgetRepo.findAllByMonthYear(monthYear)
                .stream()
                .map(budgetMapper::convertToBudgetBase)
                .collect(Collectors.toList());
        if (budgets.isEmpty()){
            throw new BudgetNotFoundException("No budgets were found for this month -> { "+monthYear+" }");
        }
        return new BudgetResponseModel(budgets);
    }

    @Override
    public BudgetResponseModel getAllBudgetsByCategory(String category) {
        log.info("getAllBudgetsByCategory: category=[{}]", category);
        List<BudgetBase> budgets = budgetRepo.findAllByCategory(category)
                .stream()
                .map(budgetMapper::convertToBudgetBase)
                .collect(Collectors.toList());
        if (budgets.isEmpty()){
            throw new BudgetNotFoundException("No budgets were found for this category -> { "+category+" }");
        }
        return new BudgetResponseModel(budgets);
    }

    @Override
    public BudgetResponseModel getAllBudgetsBySubCategory(String type) {
        log.info("getAllBudgetsByType: type=[{}]", type);
        List<BudgetBase> budgets = budgetRepo.findAllBySubCategory(type)
                .stream()
                .map(budgetMapper::convertToBudgetBase)
                .collect(Collectors.toList());
        if (budgets.isEmpty()){
            throw new BudgetNotFoundException("No budgets were found for this category -> { "+type+" }");
        }
        return new BudgetResponseModel(budgets);
    }

    @Override
    public GenericSuccessResponseModel deleteBudgetById(String id) {
        log.info("deleteBudget: id=[{}]", id);
        budgetRepo.deleteById(id);
        return new GenericSuccessResponseModel(!budgetRepo.existsById(id));
    }

}
