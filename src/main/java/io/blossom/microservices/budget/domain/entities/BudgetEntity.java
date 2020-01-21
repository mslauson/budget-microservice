package io.blossom.microservices.budget.domain.entities;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document("budget")
public class BudgetEntity {

    @Id
    private String id;

    private String username;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime dateCreated;

    private Integer year;

    private Integer month;

    private String name;

    private String category;

    private String type;

    private BigDecimal target;

    private BigDecimal allocation;

}
