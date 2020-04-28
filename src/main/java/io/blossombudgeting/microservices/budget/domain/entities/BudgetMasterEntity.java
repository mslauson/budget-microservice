/*
 * Copyright (c) 2020. Blossom Budgeting LLC
 * All Rights Reserved
 */

package io.blossombudgeting.microservices.budget.domain.entities;

import io.blossombudgeting.util.budgetcommonutil.model.accounts.Category;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document("budget-master")
public class BudgetMasterEntity {

    @Id
    private String id;

    private List<Category> categories;
}
