package com.mendel.transactionsapi.service;

import com.mendel.transactionsapi.dto.Transaction;
import com.mendel.transactionsapi.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
     * @param transaction   The transaction payload.
     */
    public Transaction createTransaction(long transactionId, Transaction transaction) {
        log.info("Creating transaction with ID {}: {}", transactionId, transaction);

        transaction.setId(transactionId);
        Transaction saved = transactionRepository.save(transaction);
        log.info("Transaction created successfully: {}", saved);
        return saved;
    }
}
