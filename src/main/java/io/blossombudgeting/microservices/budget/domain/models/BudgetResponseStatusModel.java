/*
 * Copyright (c) 2020. Blossom Budgeting LLC
 * All Rights Reserved
 */

package io.blossombudgeting.microservices.budget.domain.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.blossombudgeting.microservices.budget.domain.entities.BudgetEntity;

import io.blossombudgeting.util.budgetcommonutil.model.GenericExceptionResponse;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BudgetResponseStatusModel {

    private Integer statusCode;
    private Integer budgetFetchSize;
    private BigDecimal targetTotal;
    private BigDecimal allocationTotal;
    private GenericExceptionResponse error;

    /**
     * Create/Update/Delete response status constructor
     */
    public BudgetResponseStatusModel() {
        this.statusCode = HttpStatus.OK.value();
    }

    /**
     * Get response status constructor with fetch data for a singular
     *
     * @param budget Singular BudgetEntity
     */
    public BudgetResponseStatusModel(BudgetEntity budget) {
        this.statusCode = HttpStatus.OK.value();
        this.budgetFetchSize = 1;
        this.targetTotal = budget.getTarget();
        this.allocationTotal = budget.getAllocation();
    }

    /**
     * Get response status constructor with fetch data for a list of budgets
     *
     * @param budgets List of BudgetEntities
     */
    public BudgetResponseStatusModel(List<BudgetEntity> budgets) {
        this.statusCode = HttpStatus.OK.value();
        this.budgetFetchSize = budgets.size();
        this.targetTotal = budgets.stream().map(BudgetEntity::getTarget).reduce(BigDecimal.ZERO, BigDecimal::add);
        this.allocationTotal = budgets.stream().map(BudgetEntity::getAllocation).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Error scenario response status constructor
     */
    public BudgetResponseStatusModel(HttpStatus statusCode, GenericExceptionResponse error) {
        this.statusCode = statusCode.value();
        this.error = error;
    }

}
