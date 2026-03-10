package com.mendel.transactionsapi.service;

import com.mendel.transactionsapi.dto.Transaction;
import com.mendel.transactionsapi.dto.TransactionRequest;
import com.mendel.transactionsapi.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Service layer containing the business logic for managing transactions.
 * Interacts with the in-memory repository for storage.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionsService {

    private final TransactionRepository transactionRepository;

    /**
     * Creates or updates a transaction in the repository.
     *
     * @param transactionId The ID provided in the URI path.
     * @param request       The transaction payload without ID.
     * @return The saved Transaction entity mapping.
     */
    public Transaction createTransaction(long transactionId, TransactionRequest request) {
        log.info("Creating transaction with ID {}: {}", transactionId, request);

        Transaction transaction = Transaction.builder()
                .id(transactionId)
                .amount(request.getAmount())
                .type(request.getType())
                .parentId(request.getParentId())
                .build();

        Transaction saved = transactionRepository.save(transaction);
        log.info("Transaction created successfully: {}", saved);
        return saved;
    }

    /**
     * Gets a list of transactions belonging to the same specified type.
     *
     * @param type The type category to filter by.
     * @return A list containing the long IDs of the found transactions.
     */
    public List<Long> getTransactionsByType(String type) {
        log.info("Fetching transactions for type: {}", type);
        return transactionRepository.getByType(type);
    }

    /**
     * Calculates the total amount for a given transaction plus the amounts
     * of all children transitively linked to it via parentId.
     *
     * @param transactionId The ID belonging to the root transaction to sum up.
     * @return A map representation holding the calculated "sum".
     */
    public Map<String, Double> getTransactionsSum(long transactionId) {
        log.info("Calculating sum for transaction ID: {}", transactionId);
        double sum = transactionRepository.getSum(transactionId);
        return Map.of("sum", sum);
    }

    /**
     * Retrieves a single transaction entity from the repository.
     *
     * @param transactionId The transaction ID to target.
     * @return The Transaction if found, or null.
     */
    public Transaction getTransactionById(long transactionId) {
        log.info("Fetching transaction with ID: {}", transactionId);
        return transactionRepository.getTransaction(transactionId);
    }
}
