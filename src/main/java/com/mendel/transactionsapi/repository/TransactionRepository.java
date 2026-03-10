package com.mendel.transactionsapi.repository;

import com.mendel.transactionsapi.dto.Transaction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

/**
 * In-memory repository for storing and querying transactions.
 * Designed to be thread-safe for concurrent REST requests.
 */
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Repository
public class TransactionRepository {

    private final Map<Long, Transaction> transactions = new ConcurrentHashMap<>();

    /**
     * Saves a transaction. If the transaction already exists, updates it.
     * Thread-safe updates of indexes.
     *
     * @param transaction The transaction to store.
     */
    public Transaction save(Transaction transaction) {
        log.info("Saving transaction: {}", transaction);

        long id = transaction.getId();

        Transaction existing = transactions.get(id);
        if (existing != null) {
            log.trace("Transaction {} already exists, removing from old indexes", id);
            removeFromIndexes(existing);
        }

        transactions.put(id, transaction);
        addToIndexes(transaction);

        log.info("Transaction {} saved successfully", id);
        return transaction;
    }

    private void removeFromIndexes(Transaction existing) {
        // To be implemented
    }

    private void addToIndexes(Transaction transaction) {
        // To be implemented
    }
}
