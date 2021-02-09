/*
 * Copyright (c) 2021. Blossom Budgeting LLC
 * All Rights Reserved
 */

package io.blossombudgeting.microservices.budget.domain.models;

import io.blossombudgeting.microservices.budget.domain.entity.CategoryEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class CategoriesModel {

    @Id
    private String id;
    private List<CategoryEntity> categories;
}
