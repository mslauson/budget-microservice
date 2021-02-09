/*
 * Copyright (c) 2021. Blossom Budgeting LLC
 * All Rights Reserved
 */

package io.blossombudgeting.microservices.budget;


import com.fasterxml.jackson.databind.ObjectMapper;
import io.blossombudgeting.microservices.budget.domain.models.*;
import io.blossombudgeting.util.budgetcommonutil.entity.LinkedTransactions;
import io.blossombudgeting.util.budgetcommonutil.entity.SubCategoryDocument;
import io.blossombudgeting.util.budgetcommonutil.util.DateUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Collections;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@TestMethodOrder(MethodOrderer.Alphanumeric.class)
@SpringBootTest
@AutoConfigureMockMvc
public class BudgetApplicationTests {

    private static String budgetId;

    @Autowired
    MockMvc mockMvc;
    private final ObjectMapper om = new ObjectMapper();
    private BudgetBase budgetBase;
    private RemoveTransactionsRequestModel removeTransactionsRequestModel;
    private Category category;
    private CategoriesModel categoriesModel;


    @BeforeEach
    void setUp() {
        SubCategoryDocument subCategoryDocument = new SubCategoryDocument();
        subCategoryDocument.setLinkedTransactions(List.of(new LinkedTransactions("id", 1d), new LinkedTransactions("id2", 2d)));
        budgetBase = new BudgetBase("id", "12345678901", LocalDateTime.of(2020, Month.APRIL, 30, 18, 1, 4), String.valueOf(DateUtils.getFirstOfMonth()), "name", "category", Collections.singletonList(subCategoryDocument), 0D, 0D, false, List.of(new LinkedTransactions("id", 1d), new LinkedTransactions("id2", 2d)));
        removeTransactionsRequestModel = new RemoveTransactionsRequestModel();
        removeTransactionsRequestModel.setPhone("12345678901");
        removeTransactionsRequestModel.setTransactionIds(List.of("id"));
        category = new Category("testing", "testing", "testing");
        categoriesModel = new CategoriesModel("testing", List.of(category));
    }

    @Test
    void ATestAddBudget() throws Exception {
        MvcResult result = mockMvc.perform(post("/budgets/api/v1")
                .content(om.writeValueAsString(budgetBase))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        String string = result.getResponse().getContentAsString();
        BudgetResponseModel rm = om.readValue(string, BudgetResponseModel.class);
        setBudgetId(rm.getBudgets().get(0).getId());
    }

    @Test
    void BTestAddBudgetDuplicate() throws Exception {
        mockMvc.perform(post("/budgets/api/v1")
                .content(om.writeValueAsString(budgetBase))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("A budget in this category/subCategory already exists"));
    }

    @Test
    void testAddBudgetNoEmail() throws Exception {
        budgetBase.setPhone(null);
        mockMvc.perform(post("/budgets/api/v1")
                .content(om.writeValueAsString(budgetBase))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Required param phone is missing."));
    }

    @Test
    void testAddBudgetNoMonthYear() throws Exception {
        budgetBase.setMonthYear(null);
        mockMvc.perform(post("/budgets/api/v1")
                .content(om.writeValueAsString(budgetBase))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Required param monthYear is missing."));
    }

    @Test
    void testAddBudgetNoName() throws Exception {
        budgetBase.setName(null);
        mockMvc.perform(post("/budgets/api/v1")
                .content(om.writeValueAsString(budgetBase))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Required param name is missing."));
    }

    @Test
    void testAddBudgetNoCategory() throws Exception {
        budgetBase.setCategory(null);
        mockMvc.perform(post("/budgets/api/v1")
                .content(om.writeValueAsString(budgetBase))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Required param category is missing."));
    }

    @Test
    void testAddBudgetNoUsed() throws Exception {
        budgetBase.setUsed(null);
        mockMvc.perform(post("/budgets/api/v1")
                .content(om.writeValueAsString(budgetBase))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Required param used is missing."));
    }

    @Test
    void testAddBudgetNoAllocated() throws Exception {
        budgetBase.setAllocation(null);
        mockMvc.perform(post("/budgets/api/v1")
                .content(om.writeValueAsString(budgetBase))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Required param allocation is missing."));
    }

    @Test
    void CTestGetBudgetById() throws Exception {
        mockMvc.perform(get("/budgets/api/v1/id")
                .param("id", getBudgetId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testGetBudgetByIdNotFound() throws Exception {
        mockMvc.perform(get("/budgets/api/v1/id")
                .param("id", "asdlkfj")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Budget with ID [asdlkfj] not found"));
    }

    @Test
    void DTestGetBudgetByEmail() throws Exception {
        MvcResult result = mockMvc.perform(get("/budgets/api/v1/phone")
                .param("phone", "12345678901")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        String body = result.getResponse().getContentAsString();
        System.out.println(body);
    }

    @Test
    void testGetBudgetByEmailNotFound() throws Exception {
        mockMvc.perform(get("/budgets/api/v1/phone")
                .param("phone", "0000000000")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("No budgets were found for this user -> { 0000000000 }"));
    }

    @Test
    void testGetBudgetByDateNotFound() throws Exception {
        mockMvc.perform(get("/budgets/api/v1/phone/monthYear")
                .param("phone", "12345678901")
                .param("monthYear", "2000-04-01")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("No budgets were found for this month -> { 2000-04-01 }"));
    }

    @Test
    void testGetBudgetByDateBadEmail() throws Exception {
        mockMvc.perform(get("/budgets/api/v1/phone/monthYear")
                .param("phone", "asdfjklsdf")
                .param("monthYear", "2000-04-01")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("No budgets were found for this month -> { 2000-04-01 }"));
    }

    @Test
    void testGetBudgetByCategoryNotFound() throws Exception {
        mockMvc.perform(get("/budgets/api/v1/category")
                .param("category", "CATESDFGORY")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("No budgets were found for this category -> { CATESDFGORY }"));
    }

    @Test
    void GTestGetBudgetBySubCategory() throws Exception {
        mockMvc.perform(get("/budgets/api/v1/subCategory")
                .param("subCategory", "12345678901")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void HTestUpdateBudget() throws Exception {
        budgetBase.setId(getBudgetId());
        mockMvc.perform(put("/budgets/api/v1")
                .content(om.writeValueAsString(budgetBase))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testUpdateBudgetNoEmail() throws Exception {
        budgetBase.setPhone(null);
        mockMvc.perform(put("/budgets/api/v1")
                .content(om.writeValueAsString(budgetBase))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Required param phone is missing."));
    }

    @Test
    void testUpdateBudgetNoMonthYear() throws Exception {
        budgetBase.setMonthYear(null);
        mockMvc.perform(put("/budgets/api/v1")
                .content(om.writeValueAsString(budgetBase))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Required param monthYear is missing."));
    }

    @Test
    void testUpdateBudgetNoName() throws Exception {
        budgetBase.setName(null);
        mockMvc.perform(put("/budgets/api/v1")
                .content(om.writeValueAsString(budgetBase))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Required param name is missing."));
    }

    @Test
    void testUpdateBudgetNoCategory() throws Exception {
        budgetBase.setCategory(null);
        mockMvc.perform(put("/budgets/api/v1")
                .content(om.writeValueAsString(budgetBase))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Required param category is missing."));
    }

    @Test
    void testUpdateBudgetNoUsed() throws Exception {
        budgetBase.setUsed(null);
        mockMvc.perform(put("/budgets/api/v1")
                .content(om.writeValueAsString(budgetBase))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Required param used is missing."));
    }

    @Test
    void testUpdateBudgetNoAllocated() throws Exception {
        budgetBase.setAllocation(null);
        mockMvc.perform(put("/budgets/api/v1")
                .content(om.writeValueAsString(budgetBase))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Required param allocation is missing."));
    }

    @Test
    void ITestDeleteRemoveTransaction() throws Exception {
        mockMvc.perform(put("/budgets/api/v1/remove/transactions")
                .content(om.writeValueAsString(removeTransactionsRequestModel))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteRemoveTransactionsNotFound() throws Exception {
        removeTransactionsRequestModel.setPhone("sdf");
        mockMvc.perform(put("/budgets/api/v1/remove/transactions")
                .content(om.writeValueAsString(removeTransactionsRequestModel))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("No budgets were found for this user -> { sdf }"));
    }

    @Test
    void JTestDeleteBudgetById() throws Exception {
        mockMvc.perform(delete("/budgets/api/v1/id")
                .param("id", getBudgetId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteBudgetById() throws Exception {
        mockMvc.perform(delete("/budgets/api/v1/id")
                .param("id", getBudgetId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("No budget found with given Id"));
    }

    @Test
    void KTestRefreshCategories() throws Exception {
        mockMvc.perform(put("/budgets/api/v1/categories/default")
                .content(om.writeValueAsString(categoriesModel))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testRefreshCategoriesNoCategoryId() throws Exception {
        categoriesModel.getCategories().get(0).setId("");
        mockMvc.perform(put("/budgets/api/v1/categories/default")
                .content(om.writeValueAsString(categoriesModel))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Required param categories[0].id is missing."));
    }

    @Test
    void testRefreshCategoriesNoCategory() throws Exception {
        categoriesModel.getCategories().get(0).setCategory("");
        mockMvc.perform(put("/budgets/api/v1/categories/default")
                .content(om.writeValueAsString(categoriesModel))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Required param categories[0].category is missing."));
    }

    @Test
    void testRefreshCategoriesNoCategoryList() throws Exception {
        categoriesModel.setCategories(null);
        mockMvc.perform(put("/budgets/api/v1/categories/default")
                .content(om.writeValueAsString(categoriesModel))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Required param categories is missing."));
    }

    @Test
    void LTestGetCategories() throws Exception {
        mockMvc.perform(get("/budgets/api/v1/categories/default")
                .param("id", "testing")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testGetCategoriesNotFound() throws Exception {
        mockMvc.perform(get("/budgets/api/v1/categories/default")
                .param("id", "fake")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("No default categories exist for id fake"));
    }

    @Test
    void testHealthCheck1() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk());
    }

    @Test
    void testHealthCheck2() throws Exception {
        mockMvc.perform(get("/budgets"))
                .andExpect(status().isOk());
    }

    public static String getBudgetId() {
        return budgetId;
    }

    public static void setBudgetId(String budgetId) {
        BudgetApplicationTests.budgetId = budgetId;
    }
}
