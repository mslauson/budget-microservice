/*
 * Copyright (c) 2020. Blossom Budgeting LLC
 * All Rights Reserved
 */

package io.blossombudgeting.microservices.budget.controller;


import io.blossombudgeting.microservices.budget.service.intf.IBudgetAdminService;
import io.blossombudgeting.util.budgetcommonutil.model.GenericCategoryModel;
import io.blossombudgeting.util.budgetcommonutil.model.GenericSuccessResponseModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Deprecated
@Slf4j
@RestController
@RequestMapping("/api/v1/budget/admin")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BudgetAdminController {

    private final IBudgetAdminService adminService;

    @PostMapping("/master")
    public ResponseEntity<GenericSuccessResponseModel> addMasterBudgetsV1(@Valid @RequestBody GenericCategoryModel requestModel) {
        log.info("Inside BudgetAdminController.addMasterBudgetsV1 request -> [{}]", requestModel.toString());
        return ResponseEntity.ok(adminService.updateMasterRecords(requestModel));
    }

    @GetMapping("/master/{id}")
    public ResponseEntity<GenericCategoryModel> getMasterBudgetsV1(@PathVariable String id) {
        log.info("Inside BudgetAdminController.getMasterBudgetsV1 request -> [{}]", id);
        return ResponseEntity.ok(adminService.getMasterRecords(id));
    }

    @PutMapping("/generate/{email}")
    public ResponseEntity<GenericSuccessResponseModel> generateGenericBudgetsV1(@PathVariable String email) {
        log.info("Inside BudgetAdminController.generateGenericBudgetsV1 request -> [{}]", email);
        return ResponseEntity.ok(adminService.createGenericBudgetsForTheMonth(email));
    }

}
