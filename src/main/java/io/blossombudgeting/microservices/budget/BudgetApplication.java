/*
 * Copyright (c) 2020. Blossom Budgeting LLC
 * All Rights Reserved
 */

package io.blossombudgeting.microservices.budget;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan({"io.blossombudgeting.microservices.budget", "io.blossombudgeting.util.budgetcommonutil"})
@SpringBootApplication
public class BudgetApplication {

    public static void main(String[] args) {
        SpringApplication.run(BudgetApplication.class, args);
    }

}
