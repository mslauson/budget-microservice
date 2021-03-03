/*
 * Copyright (c) 2021. Blossom Budgeting LLC
 * All Rights Reserved
 */

package io.blossombudgeting.microservices.budget.repository;

import io.blossombudgeting.microservices.budget.domain.entity.TransactionEntity;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

public interface ITransactionsRepository extends PagingAndSortingRepository<TransactionEntity, String> {
    Optional<TransactionEntity> findByTransactionIdAndPhoneAndFlaggedForDeletionFalse(String id, String phone);

    Set<TransactionEntity> findByPhoneAndDateBetweenAndFlaggedForDeletionFalse(String phone, LocalDate start, LocalDate end);

    Set<TransactionEntity> findByPhoneAndDateIs(String phone, LocalDate start);

    Set<TransactionEntity> findAllByPhone(String phone);

    Set<TransactionEntity> findAllByAccountIdAndPhone(String accountId, String phone);
}
