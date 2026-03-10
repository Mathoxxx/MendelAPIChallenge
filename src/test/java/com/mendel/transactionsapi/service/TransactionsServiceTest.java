package com.mendel.transactionsapi.service;

import com.mendel.transactionsapi.dto.Transaction;
import com.mendel.transactionsapi.dto.TransactionRequest;
import com.mendel.transactionsapi.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TransactionsServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionsService transactionsService;

    private Transaction transaction;

    @BeforeEach
    void setUp() {
        transaction = Transaction.builder()
                .amount(5000)
                .type("cars")
                .build();
    }

    @Test
    void testCreateTransaction() {
        long transactionId = 10L;
        TransactionRequest request = new TransactionRequest(
                5000.0, "cars", null);
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(i -> i.getArguments()[0]);

        Transaction result = transactionsService.createTransaction(transactionId, request);

        assertNotNull(result);
        assertEquals(transactionId, result.getId());
        assertEquals(5000, result.getAmount());

        transaction.setId(transactionId);
        verify(transactionRepository).save(transaction);
    }

    @Test
    void testGetTransactionsByType() {
        String type = "cars";
        List<Long> expectedIds = Arrays.asList(10L, 11L);
        when(transactionRepository.getByType(type)).thenReturn(expectedIds);

        List<Long> resultIds = transactionsService.getTransactionsByType(type);

        assertEquals(expectedIds, resultIds);
        verify(transactionRepository).getByType(type);
    }

    @Test
    void testGetTransactionsSum() {
        long transactionId = 10L;
        double expectedSumValue = 15000.0;
        when(transactionRepository.getSum(transactionId)).thenReturn(expectedSumValue);

        Map<String, Double> result = transactionsService.getTransactionsSum(transactionId);

        assertNotNull(result);
        assertEquals(expectedSumValue, result.get("sum"));
        verify(transactionRepository).getSum(transactionId);
    }

    @Test
    void testGetTransactionById() {
        long transactionId = 10L;
        transaction.setId(transactionId);
        when(transactionRepository.getTransaction(transactionId)).thenReturn(transaction);

        Transaction result = transactionsService.getTransactionById(transactionId);

        assertNotNull(result);
        assertEquals(transactionId, result.getId());
        assertEquals(transaction.getAmount(), result.getAmount());
        assertEquals(transaction.getType(), result.getType());
        verify(transactionRepository).getTransaction(transactionId);
    }
}
