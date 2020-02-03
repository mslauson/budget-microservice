package io.blossom.microservices.budget.service.impl;

import io.blossom.microservices.budget.domain.entities.BudgetEntity;
import io.blossom.microservices.budget.domain.models.BudgetRequestModel;
import io.blossom.microservices.budget.domain.models.BudgetResponseModel;
import io.blossom.microservices.budget.error.BudgetNotFoundException;
import io.blossom.microservices.budget.repository.BudgetRepository;
import io.blossom.microservices.budget.service.IBudgetService;
import io.blossom.microservices.budget.util.BudgetEntityBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class BudgetServiceImpl implements IBudgetService {

    private final BudgetRepository budgetRepo;
    private final BudgetEntityBuilder entityBuilder;

    @Autowired
    public BudgetServiceImpl(BudgetRepository budgetRepo, BudgetEntityBuilder entityBuilder) {
        this.budgetRepo = budgetRepo;
        this.entityBuilder = entityBuilder;
    }

    @Override
    public BudgetResponseModel saveBudget(BudgetRequestModel request) {
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
        return new BudgetResponseModel(budgetRepo.findAllByUsername(username));
    }

    @Override
    public BudgetResponseModel getAllBudgetsByYear(Integer year) {
        log.info("getAllBudgetsByYear: year=[{}]", year);
        return new BudgetResponseModel(budgetRepo.findAllByYear(year));
    }

    @Override
    public BudgetResponseModel getAllBudgetsByYearAndMonth(Integer year, Integer month) {
        log.info("getAllBudgetsByYearAndMonth: year=[{}] month=[{}]", year, month);
        List<BudgetEntity> budgets = budgetRepo.findAllByYear(year).stream()
                .filter(budget -> budget.getMonth().equals(month))
                .collect(Collectors.toList());
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
        return new BudgetResponseModel(budgetRepo.findAllByType(type));
    }

    @Override
    public boolean deleteBudgetById(String id) {
        log.info("deleteBudget: id=[{}]", id);
        budgetRepo.deleteById(id);
        return !budgetRepo.existsById(id);
    }

}
