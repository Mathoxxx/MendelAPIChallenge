package com.mendel.transactionsapi.controller;

import com.mendel.transactionsapi.dto.Transaction;
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

    @PutMapping("/{transaction_id}")
    public ResponseEntity<Transaction> createTransaction(
            @PathVariable("transaction_id") long transactionId,
            @RequestBody Transaction transaction) {
        log.info("Received request to save new transaction {} with id {}", transaction, transactionId);
        Transaction savedTransaction = transactionService.createTransaction(transactionId, transaction);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedTransaction);
    }

    @GetMapping("/types/{type}")
    public ResponseEntity<List<Long>> getTransactionsByType(@PathVariable("type") String type) {
        return ResponseEntity.ok(transactionService.getTransactionsByType(type));
    }

    @GetMapping("/sum/{transaction_id}")
    public ResponseEntity<Map<String, Double>> getTransactionsSum(@PathVariable("transaction_id") long transactionId) {
        return ResponseEntity.ok(transactionService.getTransactionsSum(transactionId));
    }
}
