package io.blossombudgeting.microservices.budget.util;

import io.blossombudgeting.microservices.budget.repository.BudgetRepository;
import io.blossombudgeting.util.budgetcommonutil.entity.BudgetEntity;
import io.blossombudgeting.util.budgetcommonutil.util.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.Mockito.*;

class BudgetScheduledUtilityTest {
    @Mock
    BudgetRepository budgetRepository;
    @Mock
    Logger log;
    @InjectMocks
    BudgetScheduledUtility budgetScheduledUtility;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testReCreateBudgets() {
        when(budgetRepository.findAllByMonthYear(anyString())).thenReturn(new HashSet<BudgetEntity>(List.of(BudgetEntity
                .builder()
                .id("test")
                .phone("12345678901")
                .name("test")
                .category("test")
                .allocation(300d)
                .monthYear("newMonthYear")
                .build()
        )));

        assertThatCode(() -> budgetScheduledUtility.reCreateBudgets()).doesNotThrowAnyException();
    }
}