/*
 * Copyright (c) 2020. Blossom Budgeting LLC
 * All Rights Reserved
 */

package io.blossombudgeting.microservices.budget.controller;

import io.blossombudgeting.microservices.budget.domain.models.BudgetBase;
import io.blossombudgeting.microservices.budget.domain.models.BudgetRequestModel;
import io.blossombudgeting.microservices.budget.domain.models.BudgetResponseModel;
import io.blossombudgeting.microservices.budget.domain.models.BudgetResponseStatusModel;
import io.blossombudgeting.microservices.budget.service.intf.IBudgetService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import javax.validation.constraints.Size;
import java.time.LocalDate;

@Slf4j
@RestController
@RequestMapping("/api/v1//budget")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BudgetController {

    private final IBudgetService budgetService;

    @PostMapping
    public ResponseEntity<BudgetResponseModel> saveBudgetV1(@RequestBody @Valid BudgetRequestModel request) {
        log.info("saveBudgetV1: request=[{}]", request);
        return ResponseEntity.ok(budgetService.saveBudget(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BudgetResponseModel> getBudgetByIdV1(@PathVariable String id) {
        log.info("getBudgetByIdV1: id=[{}]", id);
        return ResponseEntity.ok(budgetService.getBudgetById(id));
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<BudgetResponseModel> getAllBudgetsByUsernameV1(@PathVariable @Size(min = 4, max = 30) String username) {
        log.info("getAllBudgetsByUsernameV1: username=[{}]", username);
        return ResponseEntity.ok(budgetService.getAllBudgetsByUsername(username.toUpperCase()));
    }

    @GetMapping("/month/{monthYear}")
    public ResponseEntity<BudgetResponseModel> getAllBudgetsByYearAndMonthV1(
            @PathVariable LocalDate monthYear) {

        log.info("getAllBudgetsByYearAndMonthV1: monthYear=[{}]", monthYear);
        return ResponseEntity.ok(budgetService.getAllBudgetsByYearAndMonth(monthYear));
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<BudgetResponseModel> getAllBudgetsByCategoryV1(@PathVariable @Size(max = 50) String category) {
        log.info("getAllBudgetsByCategoryV1: category=[{}]", category);
        return ResponseEntity.ok(budgetService.getAllBudgetsByCategory(category.toUpperCase()));
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<BudgetResponseModel> getAllBudgetsByTypeV1(@PathVariable @Size(max = 50) String type) {
        log.info("getAllBudgetsByTypeV1: type=[{}]", type);
        return ResponseEntity.ok(budgetService.getAllBudgetsByType(type.toUpperCase()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BudgetResponseStatusModel> deleteBudgetByIdV1(@PathVariable String id) {
        log.info("deleteBudgetByIdV1: id=[{}]", id);
        return ResponseEntity.ok(new BudgetResponseStatusModel());
    }

}
