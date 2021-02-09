/*
 * Copyright (c) 2021. Blossom Budgeting LLC
 * All Rights Reserved
 */

package io.blossombudgeting.microservices.budget.domain.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class Category {

    @Id
    private String id;
    private String category;
    private String icon;
}
