/*
 * Copyright (c) 2020. Blossom Budgeting LLC
 * All Rights Reserved
 */

package io.blossombudgeting.microservices.budget.service.impl;

import io.blossombudgeting.microservices.budget.domain.models.*;
import io.blossombudgeting.microservices.budget.error.BudgetNotFoundException;
import io.blossombudgeting.microservices.budget.repository.BudgetRepository;
import io.blossombudgeting.microservices.budget.service.intf.IBudgetService;
import io.blossombudgeting.microservices.budget.util.BudgetMapper;
import io.blossombudgeting.util.budgetcommonutil.entity.BudgetEntity;
import io.blossombudgeting.util.budgetcommonutil.entity.LinkedTransactions;
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
        BudgetEntity budgetEntity = getBudgetEntityById(id);
        BudgetBase budget = budgetMapper.convertToBudgetBase(budgetEntity);
        return new BudgetResponseModel(Collections.singletonList(budget));
    }

    @Override
    public BudgetResponseModel getAllBudgetsByPhone(String phone) {
        log.info("getAllBudgetsByUsername: phone=[{}]", phone);
        List<BudgetBase> budgetBases = getBudgetsByPhone(phone)
                .stream()
                .map(budgetMapper::convertToBudgetBase)
                .collect(Collectors.toList());
        return new BudgetResponseModel(budgetBases);
    }

    @Override
    public BudgetResponseModel getAllBudgetsByYearAndMonth(GetBudgetsByMonthRequestModel requestModel) {
        log.info("getAllBudgetsByYearAndMonth: phone=[{}]", requestModel.getPhone());
        List<BudgetBase> budgets = budgetRepo.findAllByPhoneAndMonthYear(requestModel.getPhone()
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
        return null;
    }

    @Override
    public GenericSuccessResponseModel updateBudget(UpdateBudgetRequestModel requestModel) {
        BudgetEntity entity = getBudgetEntityById(requestModel.getId());
        budgetRepo.save(budgetMapper.updateCurrentEntity(requestModel, entity));
        return new GenericSuccessResponseModel(true);
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

    @Override
    public GenericSuccessResponseModel removeTransactionsWhenAccountDeleted(RemoveTransactionsRequestModel requestModel) {
        long start = System.currentTimeMillis();
        List<BudgetEntity> budgetEntities = getBudgetsByPhone(requestModel.getPhone());
        removeTransactionsFromBudgets(budgetEntities, requestModel.getTransactionIds());
        log.info("removeTransactionsWhenAccountDeleted execution time -> {}ms", System.currentTimeMillis() - start);
        return new GenericSuccessResponseModel(true);
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

    /**
     * Grabs the entity of the given budget id
     *
     * @param budgetId  Id of the budget
     * @return          Budget Entity
     */
    private BudgetEntity getBudgetEntityById(String budgetId){
       return budgetRepo
                .findById(budgetId)
                .orElseThrow(() -> new BudgetNotFoundException("Budget with ID [" + budgetId + "] not found"));
    }

    /**
     * gets all budgets by phone
     *
     * @param phone users phone
     * @return list of budgets
     */
    private List<BudgetEntity> getBudgetsByPhone(String phone){
        List<BudgetEntity> budgetBases = budgetRepo.findAllByPhone(phone);
        if (budgetBases.isEmpty()) {
            throw new BudgetNotFoundException("No budgets were found for this user -> { " + phone + " }");
        }
        return budgetBases;
    }

    /**
     * Removes transactions from budget entities
     *
     * @param budgetEntities  to check
     * @param transactionIds to remove
     */
    private void removeTransactionsFromBudgets(List<BudgetEntity> budgetEntities, List<String> transactionIds){
        budgetEntities.forEach(budgetEntity -> {
            budgetEntity.getLinkedTransactions().forEach(transaction ->{
                boolean matches = transactionIds.stream().anyMatch(id -> id.equalsIgnoreCase(transaction.getTransactionId()));
                if (matches){
                   List<LinkedTransactions> newLinked =  budgetEntity.getLinkedTransactions()
                            .stream()
                            .filter(linkedTransactions -> !transaction.getTransactionId().equalsIgnoreCase(linkedTransactions.getTransactionId()))
                            .collect(Collectors.toList());
                   budgetEntity.setLinkedTransactions(newLinked);
                }
            });

            budgetEntity.getSubCategory().forEach(subCat ->{
                subCat.getLinkedTransactions().forEach(transaction -> {
                    boolean matches = transactionIds.stream().anyMatch(id -> id.equalsIgnoreCase(transaction.getTransactionId()));
                    if (matches) {
                        List<LinkedTransactions> newLinked = budgetEntity.getLinkedTransactions()
                                .stream()
                                .filter(linkedTransactions -> !transaction.getTransactionId().equalsIgnoreCase(linkedTransactions.getTransactionId()))
                                .collect(Collectors.toList());
                        subCat.setLinkedTransactions(newLinked);
                    }
                });
            });
        });
    }

}
