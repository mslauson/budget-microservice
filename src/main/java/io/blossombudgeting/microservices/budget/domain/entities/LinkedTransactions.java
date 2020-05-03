/*
 * Copyright (c) 2020. Blossom Budgeting LLC
 * All Rights Reserved
 */

package io.blossombudgeting.microservices.budget.domain.entities;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LinkedTransactions {
    private String transactionId;
    private Double amount;
}
