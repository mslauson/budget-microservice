/*
 * Copyright (c) 2021. Blossom Budgeting LLC
 * All Rights Reserved
 */

package io.blossombudgeting.microservices.budget.repository;

import io.blossombudgeting.microservices.budget.domain.entity.CustomerCategoriesEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerCategoriesRepository extends MongoRepository<CustomerCategoriesEntity, String> {

    Optional<CustomerCategoriesEntity> findByPhone(String phone);
}
