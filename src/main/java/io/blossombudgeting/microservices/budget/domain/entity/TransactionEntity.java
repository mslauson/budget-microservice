/*
 * Copyright (c) 2021. Blossom Budgeting LLC
 * All Rights Reserved
 */

package io.blossombudgeting.microservices.budget.domain.entity;

import io.blossombudgeting.util.budgetcommonutil.model.Location;
import io.blossombudgeting.util.budgetcommonutil.model.PaymentMeta;
import io.blossombudgeting.util.budgetcommonutil.model.Reimbursement;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(value = "transactions")
public class
TransactionEntity {

    @Id
    private String transactionId;

    private String pendingTransactionId;

    private String transactionType;

    private LocalDate date;

    private LocalDate authorizationDate;

    private String phone;

    private String accountId;

    private String merchant;

    private Double amount;

    private List<String> categories;

    private String categoryId;

    private Location location;

    private PaymentMeta paymentMeta;

    private Reimbursement reimbursement;

    private Boolean isPending;

    private String isoCurrencyCode;

    private String tags;

    private String budgetId;

    private String subBudgetId;

    private String notes;

    private LocalDateTime creationTimeStamp;

    private LocalDateTime lastUpdated;

    private Boolean flaggedForDeletion;

    private LocalDateTime deletionTimeStamp;
}
