/*
 * Copyright (c) 2021. Blossom Budgeting LLC
 * All Rights Reserved
 */

package io.blossombudgeting.microservices.budget.service.impl;

import io.blossombudgeting.microservices.budget.domain.entity.CustomerCategoriesEntity;
import io.blossombudgeting.microservices.budget.domain.entity.DefaultCategoriesEntity;
import io.blossombudgeting.microservices.budget.domain.entity.TransactionEntity;
import io.blossombudgeting.microservices.budget.domain.models.*;
import io.blossombudgeting.microservices.budget.error.BudgetNotFoundException;
import io.blossombudgeting.microservices.budget.repository.BudgetRepository;
import io.blossombudgeting.microservices.budget.repository.CustomerCategoriesRepository;
import io.blossombudgeting.microservices.budget.repository.DefaultCategoriesRepository;
import io.blossombudgeting.microservices.budget.repository.ITransactionsRepository;
import io.blossombudgeting.microservices.budget.service.intf.IBudgetService;
import io.blossombudgeting.microservices.budget.util.BudgetMapper;
import io.blossombudgeting.util.budgetcommonutil.entity.BudgetEntity;
import io.blossombudgeting.util.budgetcommonutil.entity.LinkedTransactions;
import io.blossombudgeting.util.budgetcommonutil.exception.GenericBadRequestException;
import io.blossombudgeting.util.budgetcommonutil.exception.GenericNotFoundException;
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
    private final DefaultCategoriesRepository defaultCategoriesRepository;
    private final CustomerCategoriesRepository customerCategoriesRepository;
    private final ITransactionsRepository transactionsRepository;
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
        recalculateUsed(budgetEntities);
        budgetRepo.saveAll(budgetEntities);
        log.info("removeTransactionsWhenAccountDeleted execution time -> {}ms", System.currentTimeMillis() - start);
        return new GenericSuccessResponseModel(true);
    }

    @Override
    public CategoriesModel refreshCategories(CategoriesModel requestModel) {
        long start = System.currentTimeMillis();
        DefaultCategoriesEntity categoriesEntity = budgetMapper.categoriesRequestToEntity(requestModel);
        categoriesEntity = defaultCategoriesRepository.save(categoriesEntity);
        CategoriesModel response = budgetMapper.defaultCategoriesEntityToResponse(categoriesEntity);
        log.info("refreshCategories execution time -> {}ms", System.currentTimeMillis() - start);
        return response;
    }

    @Override
    public CategoriesModel retrieveCategories(String id) {
        log.info("Querying with id -> {}", id);
        long start = System.currentTimeMillis();
        DefaultCategoriesEntity categoriesEntity = defaultCategoriesRepository.findById(id)
                .orElseThrow(() -> new GenericNotFoundException("No default categories exist for id " + id));
        CategoriesModel response = budgetMapper.defaultCategoriesEntityToResponse(categoriesEntity);
        log.info("retrieveCategories execution time -> {}ms", System.currentTimeMillis() - start);
        return response;
    }

    @Override
    public CategoriesModel initializeCustomerCategories(String phone, String id) {
        log.info("initializing for user -> {}", phone);
        long start = System.currentTimeMillis();
        DefaultCategoriesEntity categoriesEntity = defaultCategoriesRepository.findById(id)
                .orElseThrow(() -> new GenericNotFoundException("No default categories exist for id " + id));
        CustomerCategoriesEntity customerCategoriesEntity = budgetMapper.defaultToCustomer(categoriesEntity, phone);
        customerCategoriesEntity = customerCategoriesRepository.save(customerCategoriesEntity);
        initializeCustomerBudgets(customerCategoriesEntity, phone);
        CategoriesModel response = budgetMapper.customerCategoriesEntityToResponse(customerCategoriesEntity);
        log.info("initializeCustomerCategories execution time -> {}ms", System.currentTimeMillis() - start);
        return response;
    }

    @Override
    public CategoriesModel retrieveCustomerCategories(String phone) {
        log.info("initializing for user -> {}", phone);
        long start = System.currentTimeMillis();
        CustomerCategoriesEntity customerCategoriesEntity = customerCategoriesRepository.findByPhone(phone)
                .orElseThrow(() -> new GenericNotFoundException("No customer categories exist for user " + phone));
        CategoriesModel response = budgetMapper.customerCategoriesEntityToResponse(customerCategoriesEntity);
        log.info("retrieveCustomerCategories execution time -> {}ms", System.currentTimeMillis() - start);
        return response;
    }

    @Override
    public GenericSuccessResponseModel changeBudgetForTransaction(ChangeBudgetRequestModel requestModel) {
        long start = System.currentTimeMillis();
        BudgetEntity currentBudget = getBudgetEntityByIdAndPhone(requestModel.getCurrentBudgetId(), requestModel.getPhone());
        BudgetEntity newBudget = getBudgetEntityByIdAndPhone(requestModel.getNewBudgetId(), requestModel.getPhone());
        TransactionEntity transactionEntity = transactionsRepository
                .findByTransactionIdAndPhoneAndFlaggedForDeletionFalse(requestModel.getTransactionId(), requestModel.getPhone())
                .orElseThrow(() -> new GenericNotFoundException("Transaction with ID [" + requestModel.getTransactionId() + "] and Phone [" + requestModel.getPhone() + "] not found"));
        updateBudgetAndTransaction(currentBudget, newBudget, transactionEntity);
        log.info("changeBudgetForTransaction execution time -> {}ms", System.currentTimeMillis() - start);
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
     * @param budgetId Id of the budget
     * @return Budget Entity
     */
    private BudgetEntity getBudgetEntityById(String budgetId) {
        return budgetRepo
                .findById(budgetId)
                .orElseThrow(() -> new BudgetNotFoundException("Budget with ID [" + budgetId + "] not found"));
    }


    /**
     * Grabs the entity of the given budget id with the phone
     *
     * @param budgetId Id of the budget
     * @param phone    phone of the user
     * @return Budget Entity
     */
    private BudgetEntity getBudgetEntityByIdAndPhone(String budgetId, String phone) {
        return budgetRepo
                .findById(budgetId)
                .orElseThrow(() -> new BudgetNotFoundException("Budget with ID [" + budgetId + "] and Phone [" + phone + "] not found"));
    }

    /**
     * gets all budgets by phone
     *
     * @param phone users phone
     * @return list of budgets
     */
    private List<BudgetEntity> getBudgetsByPhone(String phone) {
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
            int mainChanged = 0;
            for (LinkedTransactions transactions : budgetEntity.getLinkedTransactions()) {
                boolean matches = transactionIds.stream().anyMatch(id -> id.equalsIgnoreCase(transactions.getTransactionId()));
                if (matches) {
                    List<LinkedTransactions> newLinked = budgetEntity.getLinkedTransactions()
                            .stream()
                            .filter(linkedTransactions -> !transactions.getTransactionId().equalsIgnoreCase(linkedTransactions.getTransactionId()))
                            .collect(Collectors.toList());
                    budgetEntity.setLinkedTransactions(newLinked);
                    mainChanged++;
                }
            }
            if (mainChanged != 0 ) {
                log.info("Removed {} transactions from budget {}", mainChanged, budgetEntity.getId());
            }
        });
    }

    /**
     * Recalculate used after transactions are removed
     *
     * @param budgetEntities to update
     */
    private void recalculateUsed(List<BudgetEntity> budgetEntities){
        budgetEntities.forEach(budgetEntity -> {
            double newUsed = 0d;
            for (LinkedTransactions linkedTransactions : budgetEntity.getLinkedTransactions()) {
                newUsed = newUsed + linkedTransactions.getAmount();
            }
            budgetEntity.setUsed(newUsed);
        });
    }

    private void initializeCustomerBudgets(CustomerCategoriesEntity customerCategoriesEntity, String phone) {
        List<BudgetEntity> budgetEntities = budgetMapper.createDefaultBudgets(customerCategoriesEntity, phone);
        budgetRepo.saveAll(budgetEntities);
    }

    /**
     * Updates budget of a transaction and also recalculates totals
     *
     * @param currentBudget budget transaction is currently in
     * @param newBudget     new budget to add the transaction to
     * @param transaction   transaction entity that we are moving
     */
    private void updateBudgetAndTransaction(BudgetEntity currentBudget, BudgetEntity newBudget, TransactionEntity transaction) {
        LinkedTransactions currentLinked = currentBudget.getLinkedTransactions()
                .stream()
                .filter(linkedTransaction -> linkedTransaction.getTransactionId().equalsIgnoreCase(transaction.getTransactionId()))
                .collect(Collectors.toList()).get(0);
        currentBudget.getLinkedTransactions().remove(currentLinked);

        currentBudget.setUsed(currentBudget.getUsed() - transaction.getAmount());

        newBudget.getLinkedTransactions().add(currentLinked);
        newBudget.setUsed(newBudget.getUsed() + transaction.getAmount());

        transaction.setBudgetId(newBudget.getId());

        budgetRepo.saveAll(List.of(currentBudget, newBudget));
        transactionsRepository.save(transaction);
    }

}
