/*
 * Copyright (c) 2020. Blossom Budgeting LLC
 * All Rights Reserved
 */

package io.blossombudgeting.microservices.budget.service.impl;

import io.blossombudgeting.microservices.budget.domain.models.BudgetBase;
import io.blossombudgeting.microservices.budget.domain.models.BudgetResponseModel;
import io.blossombudgeting.microservices.budget.domain.models.GetBudgetsByMonthRequestModel;
import io.blossombudgeting.microservices.budget.error.BudgetNotFoundException;
import io.blossombudgeting.microservices.budget.repository.BudgetRepository;
import io.blossombudgeting.microservices.budget.service.intf.IBudgetService;
import io.blossombudgeting.microservices.budget.util.BudgetMapper;
import io.blossombudgeting.util.budgetcommonutil.entity.BudgetEntity;
import io.blossombudgeting.util.budgetcommonutil.exception.GenericBadRequestException;
import io.blossombudgeting.util.budgetcommonutil.model.GenericSuccessResponseModel;
import io.blossombudgeting.util.budgetcommonutil.util.DateUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        checkIfDuplicateBudget(request.getName());
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
    public BudgetResponseModel getAllBudgetsByEmail(String email) {
        log.info("getAllBudgetsByUsername: email=[{}]", email);
        List<BudgetBase> budgetBases = budgetRepo.findAllByEmail(email.toLowerCase())
                .stream()
                .map(budgetMapper::convertToBudgetBase)
                .collect(Collectors.toList());
        if (budgetBases.isEmpty()) {
            throw new BudgetNotFoundException("No budgets were found for this user -> { " + email + " }");
        }
        return new BudgetResponseModel(budgetBases);
    }
    @Override
    public BudgetResponseModel getAllBudgetsByYearAndMonth(GetBudgetsByMonthRequestModel requestModel) {
        log.info("getAllBudgetsByYearAndMonth: email=[{}]", requestModel.getEmail());
        List<BudgetBase> budgets = budgetRepo.findAllByEmailAndMonthYear(requestModel.getEmail().toLowerCase()
                , requestModel.getMonthYear())
                .stream()
                .map(budgetMapper::convertToBudgetBase)
                .collect(Collectors.toList());
        if (budgets.isEmpty()) {
            throw new BudgetNotFoundException("No budgets were found for this month -> { " + requestModel.getMonthYear() + " }");
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
        if (budgets.isEmpty()) {
            throw new BudgetNotFoundException("No budgets were found for this category -> { " + category + " }");
        }
        return new BudgetResponseModel(budgets);
    }

    @Override
    public BudgetResponseModel getAllBudgetsBySubCategory(String type) {
        log.info("getAllBudgetsByType: type=[{}]", type);
//        List<BudgetBase> budgets = budgetRepo.findAllBySubCategory(type)
//                .stream()
//                .map(budgetMapper::convertToBudgetBase)
//                .collect(Collectors.toList());
//        if (budgets.isEmpty()) {
//            throw new BudgetNotFoundException("No budgets were found for this category -> { " + type + " }");
//        }
//        return new BudgetResponseModel(budgets);

        return null;
    }

    @Override
    public GenericSuccessResponseModel deleteBudgetById(String id) {
        log.info("deleteBudget: id=[{}]", id);
        if (!budgetRepo.existsById(id)){
            throw new BudgetNotFoundException(
                    "No budget found with given Id"
            );
        }
        budgetRepo.deleteById(id);
        return new GenericSuccessResponseModel(!budgetRepo.existsById(id));
    }

    /**
     * Checks to see if budget is duplicate
     *
     * @param name category of budget
     */
    private void checkIfDuplicateBudget(String name) {
        Long categoryCount = budgetRepo.countAllByNameAndMonthYear(
                name,
                String.valueOf(DateUtils.getFirstOfMonth())
        );

        if (categoryCount != 0) {
            throw new GenericBadRequestException(
                    "A budget in this category/subCategory already exists"
            );
        }
    }

}
