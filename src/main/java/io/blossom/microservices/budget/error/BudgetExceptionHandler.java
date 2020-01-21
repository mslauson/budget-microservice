/*
 * Copyright (c) 2020. blossom.io.
 * All rights reserved.
 */

package io.blossom.microservices.budget.error;

import io.blossom.util.budgetcommonutil.exception.GenericExceptionHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class BudgetExceptionHandler extends GenericExceptionHandler {

}
