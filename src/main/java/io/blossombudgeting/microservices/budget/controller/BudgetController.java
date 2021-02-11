/*
 * Copyright (c) 2021. Blossom Budgeting LLC
 * All Rights Reserved
 */

package io.blossombudgeting.microservices.budget.controller;

import io.blossombudgeting.microservices.budget.domain.models.*;
import io.blossombudgeting.microservices.budget.service.intf.IBudgetService;
import io.blossombudgeting.util.budgetcommonutil.model.GenericSuccessResponseModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@Slf4j
@RestController
@RequestMapping("/budgets/api/v1")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BudgetController {

    private final IBudgetService budgetService;

    @PostMapping
    public ResponseEntity<BudgetResponseModel> saveBudgetV1(@RequestBody @Valid BudgetRequestModel request) {
        log.info("saveBudgetV1: request=[{}]", request);
        return ResponseEntity.ok(budgetService.saveBudget(request));
    }

    @GetMapping("/id")
    public ResponseEntity<BudgetResponseModel> getBudgetByIdV1(@RequestParam String id) {
        log.info("getBudgetByIdV1: id=[{}]", id);
        return ResponseEntity.ok(budgetService.getBudgetById(id));
    }

    @GetMapping("/phone")
    public ResponseEntity<BudgetResponseModel> getAllBudgetsByPhoneV1(@RequestParam String phone) {
        log.info("getAllBudgetsByPhoneV1: phone=[{}]", phone);
        return ResponseEntity.ok(budgetService.getAllBudgetsByPhone(phone));
    }

    @GetMapping("/phone/monthYear")
    public ResponseEntity<BudgetResponseModel> getAllBudgetsByYearAndMonthV1(@NotBlank @RequestParam("phone") String phone,
                                                                             @NotBlank @RequestParam("monthYear") String monthYear) {
        GetBudgetsByMonthRequestModel requestModel = new GetBudgetsByMonthRequestModel(phone, monthYear);
        log.info("getAllBudgetsByYearAndMonthV1: request=[{}]", requestModel);
        return ResponseEntity.ok(budgetService.getAllBudgetsByYearAndMonth(requestModel));
    }

    @GetMapping("/category")
    public ResponseEntity<BudgetResponseModel> getAllBudgetsByCategoryV1(@RequestParam String category) {
        log.info("getAllBudgetsByCategoryV1: category=[{}]", category);
        return ResponseEntity.ok(budgetService.getAllBudgetsByCategory(category.toUpperCase()));
    }

    @GetMapping("/subCategory")
    public ResponseEntity<BudgetResponseModel> getAllBudgetsBySubCategoryV1(@RequestParam String subCategory) {
        log.info("getAllBudgetsBySubCategoryV1: type=[{}]", subCategory);
        return ResponseEntity.ok(budgetService.getAllBudgetsBySubCategory(subCategory.toUpperCase()));
    }

    @PutMapping
    public ResponseEntity<GenericSuccessResponseModel> updateBudgetV1(@Valid @RequestBody UpdateBudgetRequestModel requestModel) {
        log.info("updateBudgetV1: type=[{}]", requestModel);
        return ResponseEntity.ok(budgetService.updateBudget(requestModel));
    }

    @PutMapping("/remove/transactions")
    public ResponseEntity<GenericSuccessResponseModel> removeDeletedAccountsTransactionsV1(@Valid @RequestBody RemoveTransactionsRequestModel requestModel) {
        log.info("removeDeletedAccountsTransactionsV1: request=[{}]", requestModel.toString());
        return ResponseEntity.ok(budgetService.removeTransactionsWhenAccountDeleted(requestModel));
    }

    @PutMapping("/categories/default")
    public ResponseEntity<CategoriesModel> updateDefaultCategoriesV1(@Valid @RequestBody CategoriesModel requestModel) {
        log.info("updateDefaultCategoriesV1: request=[{}]", requestModel.toString());
        return ResponseEntity.ok(budgetService.refreshCategories(requestModel));
    }

    @GetMapping("/categories/default")
    public ResponseEntity<CategoriesModel> getDefaultCategoriesV1(@NotBlank @RequestParam("id") String id) {
        log.info("getDefaultCategoriesV1: id=[{}]", id);
        return ResponseEntity.ok(budgetService.retrieveCategories(id));
    }

    @PutMapping("/categories/customer/initialize")
    public ResponseEntity<CategoriesModel> initializeCustomerCategoriesV1(@NotBlank @RequestParam("phone") String phone,
                                                                          @NotBlank @RequestParam("id") String id) {
        log.info("initializeCustomerCategoriesV1: phone=[{}] id=[{}]", phone, id);
        return ResponseEntity.ok(budgetService.initializeCustomerCategories(phone, id));
    }

    @DeleteMapping("/id")
    public ResponseEntity<GenericSuccessResponseModel> deleteBudgetByIdV1(@RequestParam String id) {
        log.info("deleteBudgetByIdV1: id=[{}]", id);
        return ResponseEntity.ok(budgetService.deleteBudgetById(id));
    }

}
