package com.mendel.transactionsapi.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mendel.transactionsapi.dto.Transaction;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Slf4j
@Repository
public class TransactionRepository {

    private final Map<Long, Transaction> transactions = new ConcurrentHashMap<>();
    private final Map<String, List<Long>> typeIndex = new ConcurrentHashMap<>();

    /**
     * Loads in-memory examples from seed-data.json
     */
    @PostConstruct
    public void loadInitialData() {
        ObjectMapper mapper = new ObjectMapper();
        TypeReference<List<Transaction>> typeReference = new TypeReference<List<Transaction>>() {
        };
        InputStream inputStream = TypeReference.class.getResourceAsStream("/seed-data.json");

        try {
            if (inputStream != null) {
                List<Transaction> seedTransactions = mapper.readValue(inputStream, typeReference);
                for (Transaction tx : seedTransactions) {
                    this.save(tx);
                }
                log.info("Successfully loaded {} transactions into repository on startup", seedTransactions.size());
            } else {
                log.warn("seed-data.json not found in resources folder. Starting with empty repository.");
            }
        } catch (Exception e) {
            log.error("Unable to load initial seed data: {}", e.getMessage());
        }
    }

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
        if (existing.getType() != null && typeIndex.containsKey(existing.getType())) {
            typeIndex.get(existing.getType()).remove(existing.getId());
        }
    }

    private void addToIndexes(Transaction transaction) {
        if (transaction.getType() != null) {
            typeIndex.computeIfAbsent(transaction.getType(), k -> new ArrayList<>()).add(transaction.getId());
        }
    }

    public Transaction getTransaction(long transactionId) {
        return transactions.get(transactionId);
    }

    public List<Long> getByType(String type) {
        log.debug("Fetching transactions by type: {}", type);
        return typeIndex.getOrDefault(type, new ArrayList<>());
    }

    public double getSum(long transactionId) {
        log.debug("Calculating sum for transaction connected to: {}", transactionId);
        return calculateSumRecursive(transactionId);
    }

    private double calculateSumRecursive(long transactionId) {
        Transaction tx = transactions.get(transactionId);
        if (tx == null) {
            return 0.0;
        }

        double sum = tx.getAmount();

        // Find all children (transactions whose parentId is this transactionId)
        for (Transaction child : transactions.values()) {
            if (child.getParentId() != null && child.getParentId() == transactionId) {
                sum += calculateSumRecursive(child.getId());
            }
        }

        return sum;
    }
}
