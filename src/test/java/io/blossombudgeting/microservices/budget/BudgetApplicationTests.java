/*
 * Copyright (c) 2020. Blossom Budgeting LLC
 * All Rights Reserved
 */

package io.blossombudgeting.microservices.budget;


import com.fasterxml.jackson.databind.ObjectMapper;
import io.blossombudgeting.microservices.budget.domain.models.BudgetBase;
import io.blossombudgeting.microservices.budget.domain.models.BudgetResponseModel;
import io.blossombudgeting.util.budgetcommonutil.entity.LinkedTransactions;
import io.blossombudgeting.util.budgetcommonutil.entity.SubCategoryDocument;
import io.blossombudgeting.util.budgetcommonutil.model.GenericCategoryModel;
import io.blossombudgeting.util.budgetcommonutil.model.accounts.Category;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@TestMethodOrder(MethodOrderer.Alphanumeric.class)
@SpringBootTest
@AutoConfigureMockMvc
public class BudgetApplicationTests {

    private static String budgetId;

    @Autowired
    MockMvc mockMvc;
    private final ObjectMapper om = new ObjectMapper();
    private GenericCategoryModel genericCategoryModel;
    private BudgetBase budgetBase;

    @BeforeEach
    void setUp() {
        genericCategoryModel = new GenericCategoryModel(Collections.singletonList(new Category("string", Collections.singletonList("String"), "String")));
        budgetBase = new BudgetBase("id", "email@email.com", LocalDateTime.of(2020, Month.APRIL, 30, 18, 1, 4), String.valueOf(DateUtils.getFirstOfMonth()), "name", "category", Collections.singletonList(new SubCategoryDocument()), 0D, 0D, false, Collections.singletonList(new LinkedTransactions()));
    }

    @Test
    void ATestAddBudget() throws Exception {
        MvcResult result = mockMvc.perform(post("/api/v1")
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
        mockMvc.perform(post("/api/v1")
                .content(om.writeValueAsString(budgetBase))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("A budget in this category/subCategory already exists"));
    }

    @Test
    void testAddBudgetNoEmail() throws Exception {
        budgetBase.setEmail(null);
        mockMvc.perform(post("/api/v1")
                .content(om.writeValueAsString(budgetBase))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Required param email is missing."));
    }

    @Test
    void testAddBudgetBadEmail() throws Exception {
        budgetBase.setEmail("null");
        mockMvc.perform(post("/api/v1")
                .content(om.writeValueAsString(budgetBase))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Value passed for email is not in the correct format"));
    }

    @Test
    void testAddBudgetShortEmail() throws Exception {
        budgetBase.setEmail("nusdfsdf");
        mockMvc.perform(post("/api/v1")
                .content(om.writeValueAsString(budgetBase))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Value passed for email is not in the correct format"));
    }

    @Test
    void testAddBudgetLongEmail() throws Exception {
        budgetBase.setEmail("nuaslkdjflsdkjflskdjflsksdfsdfsdfsdfj@gmail.com");
        mockMvc.perform(post("/api/v1")
                .content(om.writeValueAsString(budgetBase))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Value passed for email does not have a valid length"));
    }

    @Test
    void testAddBudgetNoMonthYear() throws Exception {
        budgetBase.setMonthYear(null);
        mockMvc.perform(post("/api/v1")
                .content(om.writeValueAsString(budgetBase))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Required param monthYear is missing."));
    }

    @Test
    void testAddBudgetNoName() throws Exception {
        budgetBase.setName(null);
        mockMvc.perform(post("/api/v1")
                .content(om.writeValueAsString(budgetBase))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Required param name is missing."));
    }

    @Test
    void testAddBudgetLongName() throws Exception {
        budgetBase.setName("abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz");
        mockMvc.perform(post("/api/v1")
                .content(om.writeValueAsString(budgetBase))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Value passed for name does not have a valid length"));
    }

    @Test
    void testAddBudgetNoCategory() throws Exception {
        budgetBase.setCategory(null);
        mockMvc.perform(post("/api/v1")
                .content(om.writeValueAsString(budgetBase))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Required param category is missing."));
    }

    @Test
    void testAddBudgetLongCategory() throws Exception {
        budgetBase.setCategory("abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz");
        mockMvc.perform(post("/api/v1")
                .content(om.writeValueAsString(budgetBase))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Value passed for category does not have a valid length"));
    }


//    @Test
//    void testAddBudgetLongSubCategory() throws Exception {
//        budgetBase.setSubCategory("abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz");
//        mockMvc.perform(post("/api/v1")
//                .content(om.writeValueAsString(budgetBase))
//                .contentType(MediaType.APPLICATION_JSON)
//                .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isBadRequest())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Value passed for subCategory does not have a valid length"));
//    }

    @Test
    void testAddBudgetNoUsed() throws Exception {
        budgetBase.setUsed(null);
        mockMvc.perform(post("/api/v1")
                .content(om.writeValueAsString(budgetBase))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Required param used is missing."));
    }

    @Test
    void testAddBudgetNoAllocated() throws Exception {
        budgetBase.setAllocation(null);
        mockMvc.perform(post("/api/v1")
                .content(om.writeValueAsString(budgetBase))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Required param allocation is missing."));
    }

    @Test
    void CTestGetBudgetById() throws Exception {
        mockMvc.perform(get("/api/v1/" + getBudgetId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testGetBudgetByIdNotFound() throws Exception {
        mockMvc.perform(get("/api/v1/asdlkfj")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Budget with ID [asdlkfj] not found"));
    }

    @Test
    void DTestGetBudgetByEmail() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/v1/email/email@email.com")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        String body = result.getResponse().getContentAsString();
        System.out.println(body);
    }

    @Test
    void testGetBudgetByEmailNotFound() throws Exception {
        mockMvc.perform(get("/api/v1/email/a@a.com")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("No budgets were found for this user -> { A@A.COM }"));
    }

//    @Test
//    void ETestGetBudgetByMonthYear() throws Exception {
//        mockMvc.perform(get("/api/v1/email@email.com/month/2020-05-01")
//                .contentType(MediaType.APPLICATION_JSON)
//                .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk());
//    }

    @Test
    void testGetBudgetByDateNotFound() throws Exception {
        mockMvc.perform(get("/api/v1/email@email.com/month/2000-04-01")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("No budgets were found for this month -> { 2000-04-01 }"));
    }

    @Test
    void testGetBudgetByDateBadEmail() throws Exception {
        mockMvc.perform(get("/api/v1/emailemail.com/month/2000-04-01")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Required param email is missing."));
    }


//    @Test
//    void FTestGetBudgetByCategory() throws Exception {
//        mockMvc.perform(get("/api/v1/category/category")
//                .contentType(MediaType.APPLICATION_JSON)
//                .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk());
//    }

    @Test
    void testGetBudgetByCategoryNotFound() throws Exception {
        mockMvc.perform(get("/api/v1/category/catesdfgory")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("No budgets were found for this category -> { CATESDFGORY }"));
    }

    @Test
    void GTestGetBudgetBySubCategory() throws Exception {
        mockMvc.perform(get("/api/v1/subCategory/subCategory")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

//    @Test
//    void testGetBudgetBySubCategoryNotFound() throws Exception {
//        mockMvc.perform(get("/api/v1/subCategory/asdklfj")
//                .contentType(MediaType.APPLICATION_JSON)
//                .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isNotFound())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("No budgets were found for this category -> { ASDKLFJ }"));
//    }

//    @Test
//    void HTestDeleteBudgetById() throws Exception {
//        mockMvc.perform(delete("/api/v1/" + getBudgetId())
//                .contentType(MediaType.APPLICATION_JSON)
//                .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk());
//    }

//    @Test
//    void testDeleteBudgetById() throws Exception {
//        mockMvc.perform(delete("/api/v1/" + getBudgetId())
//                .contentType(MediaType.APPLICATION_JSON)
//                .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isNotFound())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("No budget found with given Id"));
//    }

    @Test
    void testAddMasterNoCategories() throws Exception {
        genericCategoryModel.setCategories(null);
        mockMvc.perform(post("/api/v1/budget/admin/master")
                .content(om.writeValueAsString(genericCategoryModel))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Required param categories is missing."));
    }

    @Test
    void testAddMasterNoCategoryId() throws Exception {
        genericCategoryModel.getCategories().get(0).setCategoryId(null);
        mockMvc.perform(post("/api/v1/budget/admin/master")
                .content(om.writeValueAsString(genericCategoryModel))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Required param categories[0].categoryId is missing."));
    }

    @Test
    void testAddMasterNoCategoryHeirarchy() throws Exception {
        genericCategoryModel.getCategories().get(0).setHierarchy(null);
        mockMvc.perform(post("/api/v1/budget/admin/master")
                .content(om.writeValueAsString(genericCategoryModel))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Required param categories[0].hierarchy is missing."));
    }

    @Test
    void testAddMasterNoCategoryGroup() throws Exception {
        genericCategoryModel.getCategories().get(0).setGroup(null);
        mockMvc.perform(post("/api/v1/budget/admin/master")
                .content(om.writeValueAsString(genericCategoryModel))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Required param categories[0].group is missing."));
    }

    public static String getBudgetId() {
        return budgetId;
    }

    public static void setBudgetId(String budgetId) {
        BudgetApplicationTests.budgetId = budgetId;
    }
}
