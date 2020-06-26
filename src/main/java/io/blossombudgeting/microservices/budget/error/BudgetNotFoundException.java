/*
 * Copyright (c) 2020. Blossom Budgeting LLC
 * All Rights Reserved
 */

package io.blossombudgeting.microservices.budget.error;

import io.blossombudgeting.util.budgetcommonutil.exception.GenericNotFoundException;

public class BudgetNotFoundException extends GenericNotFoundException {

    public BudgetNotFoundException(String message) {
        super(message);
    }

}
