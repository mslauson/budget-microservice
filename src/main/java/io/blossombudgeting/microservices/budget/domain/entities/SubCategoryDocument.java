/*
 * Copyright (c) 2020. Blossom Budgeting LLC
 * All Rights Reserved
 */

package io.blossombudgeting.microservices.budget.domain.entities;

import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SubCategoryDocument {
    private String id;

    private String name;

    private String category;

    private List<SubCategoryDocument> subCategory;

    private Double used;

    private Double allocation;

    private boolean visible;

    private List<LinkedTransactions> linkedTransactions;
}
