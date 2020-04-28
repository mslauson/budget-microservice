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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/budget/admin")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BudgetAdminController {

    private final IBudgetAdminService adminService;

   @PutMapping("/master")
   public GenericSuccessResponseModel addMasterBudgetsV1(GenericCategoryModel requestModel){
        log.info("Inside BudgetAdminController.addMasterBudgetsV1 request -> [{}]", requestModel.toString());
        return adminService.updateMasterRecords(requestModel);
    }

    @PutMapping("/master/{id}")
   public GenericCategoryModel getMasterBudgetsV1(@PathVariable String id){
        log.info("Inside BudgetAdminController.getMasterBudgetsV1 request -> [{}]",id);
        return adminService.getMasterRecords(id);
    }

}
