/*
 * Copyright (c) 2021. Blossom Budgeting LLC
 * All Rights Reserved
 */

package io.blossombudgeting.microservices.budget.repository;

import io.blossombudgeting.microservices.budget.domain.entity.DefaultCategoriesEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DefaultCategoriesRepository extends MongoRepository<DefaultCategoriesEntity, String> {
}
