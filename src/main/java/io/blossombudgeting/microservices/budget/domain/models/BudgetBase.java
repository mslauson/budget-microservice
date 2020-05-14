/*
 * Copyright (c) 2020. Blossom Budgeting LLC
 * All Rights Reserved
 */

package io.blossombudgeting.microservices.budget.domain.models;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.blossombudgeting.util.budgetcommonutil.entity.LinkedTransactions;
import io.blossombudgeting.util.budgetcommonutil.entity.SubCategoryDocument;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class BudgetBase {

    private String id;

    @NotBlank
    @Size(min = 4, max = 30)
    @Email
    private String email;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime dateCreated;

    @NotBlank
    private String monthYear;

    @NotBlank
    @Size(max = 50)
    private String name;

    @NotBlank
    @Size(max = 50)
    private String category;

    private List<SubCategoryDocument> subCategory;

    @NotNull
    private Double used;

    @NotNull
    private Double allocation;

    private boolean visible;

    private List<LinkedTransactions> linkedTransactions;

}
