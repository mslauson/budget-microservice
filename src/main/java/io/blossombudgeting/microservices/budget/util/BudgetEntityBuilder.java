/*
 * Copyright (c) 2020. Blossom Budgeting LLC
 * All Rights Reserved
 */

package io.blossombudgeting.microservices.budget.util;

import io.blossombudgeting.microservices.budget.domain.entities.BudgetEntity;
import io.blossombudgeting.microservices.budget.domain.models.BudgetRequestModel;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class BudgetEntityBuilder {

    public BudgetEntity covertToEntity(BudgetRequestModel request) {
        return BudgetEntity.builder()
                .id(request.getId())
                .username(request.getUsername().toUpperCase())
                .dateCreated(request.getDateCreated() != null ? request.getDateCreated() : LocalDateTime.now())
                .year(request.getYear())
                .month(request.getMonth())
                .name(request.getName().toUpperCase())
                .category(request.getCategory().toUpperCase())
                .type(request.getType().toUpperCase())
                .target(request.getTarget())
                .allocation(request.getAllocation())
                .build();
    }

}
