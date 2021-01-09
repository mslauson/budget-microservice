/*
 * Copyright (c) 2020. Blossom Budgeting LLC
 * All Rights Reserved
 */

package io.blossombudgeting.microservices.budget.util;

import io.blossombudgeting.microservices.budget.domain.models.BudgetBase;
import io.blossombudgeting.microservices.budget.domain.models.UpdateBudgetRequestModel;
import io.blossombudgeting.util.budgetcommonutil.encryption.BlossomEncryptionUtility;
import io.blossombudgeting.util.budgetcommonutil.entity.BudgetEntity;
import io.blossombudgeting.util.budgetcommonutil.entity.LinkedTransactions;
import io.blossombudgeting.util.budgetcommonutil.entity.SubCategoryDocument;
import io.blossombudgeting.util.budgetcommonutil.util.DateUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BudgetMapper {

    private final BlossomEncryptionUtility encryptionUtility;

    public BudgetEntity covertToEntity(BudgetBase request) {
        return BudgetEntity.builder()
                .phone(request.getPhone().toLowerCase())
                .dateCreated(request.getDateCreated() != null ? request.getDateCreated() : DateUtils.utcTimeStamp())
                .monthYear(request.getMonthYear())
                .name(request.getName())
                .category(request.getCategory())
                .subCategory(request.getSubCategory())
                .used(request.getUsed())
                .allocation(request.getAllocation())
                .visible(request.isVisible())
                .linkedTransactions(request.getLinkedTransactions())
                .build();
    }

    public BudgetBase convertToBudgetBase(BudgetEntity budgetEntity) {
        return BudgetBase.builder()
                .id(encryptionUtility.encryptData(budgetEntity.getId().getBytes(StandardCharsets.UTF_8)).toString())
                .phone(encryptionUtility.encryptData(budgetEntity.getPhone().getBytes(StandardCharsets.UTF_8)).toString())
                .dateCreated(budgetEntity.getDateCreated() != null ? budgetEntity.getDateCreated() : LocalDateTime.now())
                .monthYear(encryptionUtility.encryptData(budgetEntity.getMonthYear().getBytes(StandardCharsets.UTF_8)).toString())
                .name(encryptionUtility.encryptData(budgetEntity.getName().getBytes(StandardCharsets.UTF_8)).toString())
                .category(encryptionUtility.encryptData(budgetEntity.getCategory().getBytes(StandardCharsets.UTF_8)).toString())
                .subCategory(encryptSubCategory(budgetEntity.getSubCategory()))
                .used(budgetEntity.getUsed())
                .allocation(budgetEntity.getAllocation())
                .visible(budgetEntity.isVisible())
                .linkedTransactions(encryptLinkedTransactions(budgetEntity.getLinkedTransactions()))
                .build();
    }

    public BudgetEntity updateCurrentEntity(UpdateBudgetRequestModel request, BudgetEntity entity) {
        entity.setDateCreated(request.getDateCreated() != null ? request.getDateCreated() : DateUtils.utcTimeStamp());
        entity.setMonthYear(request.getMonthYear());
        entity.setName(request.getName());
        entity.setCategory(request.getCategory());
        entity.setSubCategory(request.getSubCategory());
        entity.setUsed(request.getUsed());
        entity.setAllocation(request.getAllocation());
        entity.setVisible(request.isVisible());
        entity.setLinkedTransactions(request.getLinkedTransactions());

        return entity;
    }

    private List<SubCategoryDocument> encryptSubCategory(List<SubCategoryDocument> subCategoryDocuments){
        subCategoryDocuments.forEach(subCategoryDocument -> {
        encryptLinkedTransactions(subCategoryDocument.getLinkedTransactions());
        subCategoryDocument.setCategory(
                encryptionUtility.encryptData(subCategoryDocument.getCategory().getBytes(StandardCharsets.UTF_8)).toString()
        );
        subCategoryDocument.setName(
                encryptionUtility.encryptData(subCategoryDocument.getName().getBytes(StandardCharsets.UTF_8)).toString()
        );
        subCategoryDocument.setId(
                encryptionUtility.encryptData(subCategoryDocument.getId().getBytes(StandardCharsets.UTF_8)).toString()
        );
        });
        return subCategoryDocuments;
    }

    private List<LinkedTransactions> encryptLinkedTransactions(List<LinkedTransactions> linkedTransactions){
        linkedTransactions.forEach(linkedTransaction -> {
            linkedTransaction.setTransactionId(
                    encryptionUtility.encryptData(linkedTransaction.getTransactionId().getBytes(StandardCharsets.UTF_8)).toString()
            );
        });
        return linkedTransactions;
    }

}
