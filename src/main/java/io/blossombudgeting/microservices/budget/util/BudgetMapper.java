/*
 * Copyright (c) 2021. Blossom Budgeting LLC
 * All Rights Reserved
 */

package io.blossombudgeting.microservices.budget.util;

import io.blossombudgeting.microservices.budget.domain.entity.CategoryEntity;
import io.blossombudgeting.microservices.budget.domain.entity.CustomerCategoriesEntity;
import io.blossombudgeting.microservices.budget.domain.entity.DefaultCategoriesEntity;
import io.blossombudgeting.microservices.budget.domain.models.BudgetBase;
import io.blossombudgeting.microservices.budget.domain.models.CategoriesModel;
import io.blossombudgeting.microservices.budget.domain.models.Category;
import io.blossombudgeting.microservices.budget.domain.models.UpdateBudgetRequestModel;
import io.blossombudgeting.util.budgetcommonutil.encryption.BlossomEncryptionUtility;
import io.blossombudgeting.util.budgetcommonutil.entity.BudgetEntity;
import io.blossombudgeting.util.budgetcommonutil.util.DateUtils;
import io.blossombudgeting.util.budgetcommonutil.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
                .id(budgetEntity.getId())
                .phone(budgetEntity.getPhone())
                .dateCreated(budgetEntity.getDateCreated() != null ? budgetEntity.getDateCreated() : LocalDateTime.now())
                .monthYear(budgetEntity.getMonthYear())
                .name(budgetEntity.getName())
                .category(budgetEntity.getCategory())
                .subCategory(budgetEntity.getSubCategory())
                .used(budgetEntity.getUsed())
                .allocation(budgetEntity.getAllocation())
                .visible(budgetEntity.isVisible())
                .linkedTransactions(budgetEntity.getLinkedTransactions())
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

    public DefaultCategoriesEntity categoriesRequestToEntity(CategoriesModel requestModel) {
        List<CategoryEntity> categoryEntityList = new ArrayList<>();
        requestModel.getCategories().forEach(category -> {
            categoryEntityList.add(
                    CategoryEntity
                            .builder()
                            .id(encryptionUtility.encrypt(category.getId()))
                            .category(encryptionUtility.encrypt(category.getCategory()))
                            .icon(encryptionUtility.encrypt(category.getIcon()))
                            .enabled(category.getEnabled())
                            .build()
            );
        });

        return DefaultCategoriesEntity
                .builder()
                .categories(categoryEntityList)
                .id(requestModel.getId() == null ? "default" : requestModel.getId())
                .build();
    }

    public CategoriesModel defaultCategoriesEntityToResponse(DefaultCategoriesEntity categoriesEntity) {
        return CategoriesModel
                .builder()
                .categories(categoriesToResponseDecrypted(categoriesEntity.getCategories()))
                .id(categoriesEntity.getId())
                .build();
    }

    public CustomerCategoriesEntity defaultToCustomer(DefaultCategoriesEntity defaultEntity, String phone) {
        return CustomerCategoriesEntity
                .builder()
                .phone(phone)
                .categories(defaultEntity.getCategories())
                .build();
    }

    public CategoriesModel customerCategoriesEntityToResponse(CustomerCategoriesEntity customerCategoriesEntity) {

        return CategoriesModel
                .builder()
                .categories(categoriesToResponse(customerCategoriesEntity.getCategories()))
                .phone(customerCategoriesEntity.getPhone())
                .build();
    }

    private List<Category> categoriesToResponse(List<CategoryEntity> categoryEntityList) {
        List<Category> categories = new ArrayList<>();
        categoryEntityList.forEach(categoryEntity -> {
            categories.add(
                    Category
                            .builder()
                            .id(categoryEntity.getId())
                            .category(categoryEntity.getCategory())
                            .icon(categoryEntity.getIcon())
                            .enabled(categoryEntity.getEnabled())
                            .build()
            );
        });

        return categories;
    }

    private List<Category> categoriesToResponseDecrypted(List<CategoryEntity> categoryEntityList) {
        List<Category> categories = new ArrayList<>();
        categoryEntityList.forEach(categoryEntity -> {
            categories.add(
                    Category
                            .builder()
                            .id(encryptionUtility.decrypt(categoryEntity.getId()))
                            .category(encryptionUtility.decrypt(categoryEntity.getCategory()))
                            .icon(encryptionUtility.decrypt(categoryEntity.getIcon()))
                            .enabled(categoryEntity.getEnabled())
                            .build()
            );
        });

        return categories;
    }

    public List<BudgetEntity> createDefaultBudgets(CustomerCategoriesEntity customerCategoriesEntity, String phone) {
        LocalDate currentMonth = LocalDate.now().withDayOfMonth(1);
        List<BudgetEntity> budgetEntities = new ArrayList<>();
        customerCategoriesEntity.getCategories().forEach(categoryEntity -> {
            budgetEntities.add(
                    BudgetEntity.builder()
                            .id(StringUtils.buildStringBuffer(categoryEntity.getCategory(), currentMonth.toString(), phone))
                            .phone(phone)
                            .dateCreated(DateUtils.utcTimeStamp())
                            .name(categoryEntity.getCategory())
                            .category(categoryEntity.getCategory())
                                    .subCategory(new ArrayList<>())
                                    .used(0D)
                                    .allocation(0D)
                                    .monthYear(currentMonth.toString())
                                    .linkedTransactions(new ArrayList<>())
                                    .build()
                    );

                }
        );
        return budgetEntities;
    }
}
