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
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/api/v1/budget/admin")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BudgetAdminController {

    private final IBudgetAdminService adminService;

    @PostMapping("/master")
    public GenericSuccessResponseModel addMasterBudgetsV1(@Valid @RequestBody GenericCategoryModel requestModel) {
        log.info("Inside BudgetAdminController.addMasterBudgetsV1 request -> [{}]", requestModel.toString());
        return adminService.updateMasterRecords(requestModel);
    }

    @GetMapping("/master/{id}")
    public GenericCategoryModel getMasterBudgetsV1(@PathVariable String id) {
        log.info("Inside BudgetAdminController.getMasterBudgetsV1 request -> [{}]", id);
        return adminService.getMasterRecords(id);
    }

}
