package com.mendel.transactionsapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object strictly mapped to represent an incoming create
 * transaction request payload.
 * Does not contain the id as it evaluates strictly to values sent in the
 * request body.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionRequest {

    /**
     * The monetary quantity related to this new transaction.
     */
    private double amount;

    /**
     * The target string category this transaction adheres to.
     */
    private String type;

    /**
     * Optional linking to act as a transitive sub-node to an existing transaction
     * parent.
     */
    private Long parentId;
}
