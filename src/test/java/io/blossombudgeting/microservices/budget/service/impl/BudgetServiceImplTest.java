///*
// * Copyright (c) 2020. Blossom Budgeting LLC
// * All Rights Reserved
// */
//
//package io.blossombudgeting.microservices.budget.service.impl;
//
//import io.blossombudgeting.microservices.budget.domain.entities.BudgetEntity;
//import io.blossombudgeting.microservices.budget.domain.models.BudgetBase;
//import io.blossombudgeting.microservices.budget.domain.models.BudgetResponseModel;
//import io.blossombudgeting.microservices.budget.repository.BudgetRepository;
//import io.blossombudgeting.microservices.budget.util.BudgetMapper;
//import io.blossombudgeting.util.budgetcommonutil.model.GenericSuccessResponseModel;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import java.math.BigDecimal;
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.time.Month;
//import java.util.Arrays;
//
//import static org.mockito.Mockito.*;
//
//class BudgetServiceImplTest {
//    @Mock
//    private BudgetRepository budgetRepo;
//    @Mock
//    private BudgetMapper budgetMapper;
//
//    @InjectMocks
//    BudgetServiceImpl budgetServiceImpl;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.initMocks(this);
//    }
//
//    @Test
//    void testSaveBudget() {
//        when(budgetRepo.countAllByCategoryAndSubCategory(anyString(), anyString())).thenReturn(Long.valueOf(1));
//        when(budgetMapper.covertToEntity(any())).thenReturn(new BudgetEntity("id", "email", LocalDateTime.of(2020, Month.APRIL, 30, 18, 1, 4), LocalDate.of(2020, Month.APRIL, 30), "name", "category", "subCategory", new BigDecimal(0), new BigDecimal(0)));
//        when(budgetMapper.convertToBudgetBase(any())).thenReturn(new BudgetBase("id", "email", LocalDateTime.of(2020, Month.APRIL, 30, 18, 1, 4), LocalDate.of(2020, Month.APRIL, 30), "name", "category", "subCategory", new BigDecimal(0), new BigDecimal(0)));
//
//        BudgetResponseModel result = budgetServiceImpl.saveBudget(new BudgetBase("id", "email", LocalDateTime.of(2020, Month.APRIL, 30, 18, 1, 4), LocalDate.of(2020, Month.APRIL, 30), "name", "category", "subCategory", new BigDecimal(0), new BigDecimal(0)));
//        Assertions.assertEquals(new BudgetResponseModel(Arrays.<BudgetBase>asList(new BudgetBase("id", "email", LocalDateTime.of(2020, Month.APRIL, 30, 18, 1, 4), LocalDate.of(2020, Month.APRIL, 30), "name", "category", "subCategory", new BigDecimal(0), new BigDecimal(0)))), result);
//    }
//
//    @Test
//    void testGetBudgetById() {
//        when(budgetMapper.convertToBudgetBase(any())).thenReturn(new BudgetBase("id", "email", LocalDateTime.of(2020, Month.APRIL, 30, 18, 1, 4), LocalDate.of(2020, Month.APRIL, 30), "name", "category", "subCategory", new BigDecimal(0), new BigDecimal(0)));
//
//        BudgetResponseModel result = budgetServiceImpl.getBudgetById("id");
//        Assertions.assertEquals(new BudgetResponseModel(Arrays.<BudgetBase>asList(new BudgetBase("id", "email", LocalDateTime.of(2020, Month.APRIL, 30, 18, 1, 4), LocalDate.of(2020, Month.APRIL, 30), "name", "category", "subCategory", new BigDecimal(0), new BigDecimal(0)))), result);
//    }
//
//    @Test
//    void testGetAllBudgetsByEmail() {
//        when(budgetRepo.findAllByEmail(anyString())).thenReturn(Arrays.<BudgetEntity>asList(new BudgetEntity("id", "email", LocalDateTime.of(2020, Month.APRIL, 30, 18, 1, 4), LocalDate.of(2020, Month.APRIL, 30), "name", "category", "subCategory", new BigDecimal(0), new BigDecimal(0))));
//
//        BudgetResponseModel result = budgetServiceImpl.getAllBudgetsByEmail("email");
//        Assertions.assertEquals(new BudgetResponseModel(Arrays.<BudgetBase>asList(new BudgetBase("id", "email", LocalDateTime.of(2020, Month.APRIL, 30, 18, 1, 4), LocalDate.of(2020, Month.APRIL, 30), "name", "category", "subCategory", new BigDecimal(0), new BigDecimal(0)))), result);
//    }
//
//    @Test
//    void testGetAllBudgetsByYearAndMonth() {
//        when(budgetRepo.findAllByMonthYear(any())).thenReturn(Arrays.<BudgetEntity>asList(new BudgetEntity("id", "email", LocalDateTime.of(2020, Month.APRIL, 30, 18, 1, 4), LocalDate.of(2020, Month.APRIL, 30), "name", "category", "subCategory", new BigDecimal(0), new BigDecimal(0))));
//
//        BudgetResponseModel result = budgetServiceImpl.getAllBudgetsByYearAndMonth(LocalDate.of(2020, Month.APRIL, 30));
//        Assertions.assertEquals(new BudgetResponseModel(Arrays.<BudgetBase>asList(new BudgetBase("id", "email", LocalDateTime.of(2020, Month.APRIL, 30, 18, 1, 4), LocalDate.of(2020, Month.APRIL, 30), "name", "category", "subCategory", new BigDecimal(0), new BigDecimal(0)))), result);
//    }
//
//    @Test
//    void testGetAllBudgetsByCategory() {
//        when(budgetRepo.findAllByCategory(anyString())).thenReturn(Arrays.<BudgetEntity>asList(new BudgetEntity("id", "email", LocalDateTime.of(2020, Month.APRIL, 30, 18, 1, 4), LocalDate.of(2020, Month.APRIL, 30), "name", "category", "subCategory", new BigDecimal(0), new BigDecimal(0))));
//
//        BudgetResponseModel result = budgetServiceImpl.getAllBudgetsByCategory("category");
//        Assertions.assertEquals(new BudgetResponseModel(Arrays.<BudgetBase>asList(new BudgetBase("id", "email", LocalDateTime.of(2020, Month.APRIL, 30, 18, 1, 4), LocalDate.of(2020, Month.APRIL, 30), "name", "category", "subCategory", new BigDecimal(0), new BigDecimal(0)))), result);
//    }
//
//    @Test
//    void testGetAllBudgetsBySubCategory() {
//        when(budgetRepo.findAllBySubCategory(anyString())).thenReturn(Arrays.<BudgetEntity>asList(new BudgetEntity("id", "email", LocalDateTime.of(2020, Month.APRIL, 30, 18, 1, 4), LocalDate.of(2020, Month.APRIL, 30), "name", "category", "subCategory", new BigDecimal(0), new BigDecimal(0))));
//
//        BudgetResponseModel result = budgetServiceImpl.getAllBudgetsBySubCategory("type");
//        Assertions.assertEquals(new BudgetResponseModel(Arrays.<BudgetBase>asList(new BudgetBase("id", "email", LocalDateTime.of(2020, Month.APRIL, 30, 18, 1, 4), LocalDate.of(2020, Month.APRIL, 30), "name", "category", "subCategory", new BigDecimal(0), new BigDecimal(0)))), result);
//    }
//
//    @Test
//    void testDeleteBudgetById() {
//        GenericSuccessResponseModel result = budgetServiceImpl.deleteBudgetById("id");
//        Assertions.assertEquals(null, result);
//    }
//}
//
////Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme