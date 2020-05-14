/*
 * Copyright (c) 2020. Blossom Budgeting LLC
 * All Rights Reserved
 */

package io.blossombudgeting.microservices.budget.service.impl;

import io.blossombudgeting.microservices.budget.repository.BudgetRepository;
import io.blossombudgeting.microservices.budget.repository.IBudgetMasterRepository;
import io.blossombudgeting.microservices.budget.service.intf.IBudgetAdminService;
import io.blossombudgeting.microservices.budget.util.BudgetMasterMapper;
import io.blossombudgeting.util.budgetcommonutil.entity.BudgetEntity;
import io.blossombudgeting.util.budgetcommonutil.entity.BudgetMasterEntity;
import io.blossombudgeting.util.budgetcommonutil.exception.GenericNotFoundException;
import io.blossombudgeting.util.budgetcommonutil.model.GenericCategoryModel;
import io.blossombudgeting.util.budgetcommonutil.model.GenericSuccessResponseModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BudgetAdminServiceImpl implements IBudgetAdminService {

    private final BudgetMasterMapper masterMapper;
    private final IBudgetMasterRepository masterRepository;
    private final BudgetRepository budgetRepository;

    @Override
    public GenericSuccessResponseModel updateMasterRecords(GenericCategoryModel request) {
        log.info("Inside updateMasterRecords");
        BudgetMasterEntity masterEntity = masterMapper.requestToEntity(request);
        masterRepository.save(masterEntity);
        return new GenericSuccessResponseModel(true);
    }

    @Override
    public GenericCategoryModel getMasterRecords(String id) {
        log.info("Inside getMasterRecords");
        BudgetMasterEntity masterEntity = getMasterEntity(id);
        return masterMapper.entityToRequest(masterEntity);
    }

    @Override
    public GenericSuccessResponseModel createGenericBudgetsForTheMonth(String email) {
        BudgetMasterEntity masterEntity = getMasterEntity("master");
        Set<BudgetEntity> budgetEntities = masterMapper.masterToBudget(email, masterEntity);
        budgetRepository.saveAll(budgetEntities);
        return new GenericSuccessResponseModel(true);
    }

    private BudgetMasterEntity getMasterEntity(String id) {
        return masterRepository.findById(id)
                .orElseThrow(() -> new GenericNotFoundException("No master records available with the given ID"));
    }
}
