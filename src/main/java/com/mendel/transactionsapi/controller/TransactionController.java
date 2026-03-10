package com.mendel.transactionsapi.controller;

import com.mendel.transactionsapi.dto.Transaction;
import com.mendel.transactionsapi.dto.TransactionRequest;
import com.mendel.transactionsapi.service.TransactionsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * REST Controller for managing generic CRUD operations on Transactions.
 */
@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
@Slf4j
public class TransactionController {

    private final TransactionsService transactionService;

    /**
     * Creates a new transaction or updates an existing one if the ID already
     * exists.
     *
     * @param transactionId The unique identifier provided in the URL path.
     * @param request       The payload containing amount, type, and optional
     *                      parentId.
     * @return The newly created Transaction.
     */
    @PutMapping("/{transaction_id}")
    public ResponseEntity<Transaction> createTransaction(
            @PathVariable("transaction_id") long transactionId,
            @RequestBody TransactionRequest request) {
        log.info("Received request to save new transaction {} with id {}", request, transactionId);
        Transaction savedTransaction = transactionService.createTransaction(transactionId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedTransaction);
    }

    /**
     * Retrieves a list of transaction IDs that share the exact type specified.
     *
     * @param type The category of transactions (e.g., "cars").
     * @return A list of matching transaction IDs.
     */
    @GetMapping("/types/{type}")
    public ResponseEntity<List<Long>> getTransactionsByType(@PathVariable("type") String type) {
        return ResponseEntity.ok(transactionService.getTransactionsByType(type));
    }

    /**
     * Calculates the recursive sum of a transaction and all its linked
     * sub-transactions.
     *
     * @param transactionId The ID of the parent/root transaction.
     * @return A map containing the "sum" key with the total double value.
     */
    @GetMapping("/sum/{transaction_id}")
    public ResponseEntity<Map<String, Double>> getTransactionsSum(@PathVariable("transaction_id") long transactionId) {
        return ResponseEntity.ok(transactionService.getTransactionsSum(transactionId));
    }

    /**
     * Retrieves a specific transaction by its unique identifier.
     *
     * @param transactionId The unique ID of the transaction to search for.
     * @return The Transaction object if found, or 404 Not Found otherwise.
     */
    @GetMapping("/{transaction_id}")
    public ResponseEntity<Transaction> getTransactionById(@PathVariable("transaction_id") long transactionId) {
        Transaction transaction = transactionService.getTransactionById(transactionId);
        if (transaction == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(transaction);
    }
}
