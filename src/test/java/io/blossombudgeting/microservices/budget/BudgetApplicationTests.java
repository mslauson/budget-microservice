/*
 * Copyright (c) 2020. Blossom Budgeting LLC
 * All Rights Reserved
 */

package io.blossombudgeting.microservices.budget;


import com.fasterxml.jackson.databind.ObjectMapper;
import io.blossombudgeting.util.budgetcommonutil.model.GenericCategoryModel;
import io.blossombudgeting.util.budgetcommonutil.model.accounts.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class BudgetApplicationTests {

    @Autowired
    MockMvc mockMvc;
    private ObjectMapper om = new ObjectMapper();
    private GenericCategoryModel genericCategoryModel;
    @BeforeEach
    void setUp(){
        genericCategoryModel = new GenericCategoryModel(Collections.singletonList(new Category("string",Collections.singletonList("String"),  "String")));
    }

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
}
