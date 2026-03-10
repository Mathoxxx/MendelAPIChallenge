package com.mendel.transactionsapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
    /**
     * Unique identifier for the transaction.
     */
    private long id;

    /**
     * The monetary amount of the transaction.
     */
    private double amount;

    /**
     * The kind or category of the transaction (e.g., "cars", "shopping").
     */
    private String type;

    /**
     * Optional ID of the parent transaction, used to link transactions.
     * Can be null if the transaction has no parent.
     */
    @JsonProperty("parent_id")
    private Long parentId;
}
