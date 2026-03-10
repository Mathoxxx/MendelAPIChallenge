package com.mendel.transactionsapi.controller;

import com.mendel.transactionsapi.dto.Transaction;
import com.mendel.transactionsapi.service.TransactionsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TransactionsControllerTest {

    @InjectMocks
    private TransactionController transactionController;

    @Mock
    private TransactionsService transactionService;

    private Transaction transaction;

    @BeforeEach
    void setUp() {
        transaction = Transaction.builder()
                .amount(5000.0)
                .type("cars")
                .build();
    }

    @Test
    void testCreateTransactionOK() {
        long transactionId = 10L;
        when(transactionService.createTransaction(transactionId, transaction)).thenReturn(transaction);

        ResponseEntity<Transaction> response = transactionController.createTransaction(transactionId, transaction);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(transaction, response.getBody());
    }

}
