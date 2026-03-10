package com.mendel.transactionsapi.repository;

import com.mendel.transactionsapi.dto.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TransactionRepositoryTest {

    private TransactionRepository transactionRepository;

    @BeforeEach
    void setUp() {
        transactionRepository = new TransactionRepository();
    }

    @Test
    void testSaveAndGetTransaction() {
        long id = 1L;
        Transaction tx = Transaction.builder().id(id).amount(100.0).type("cars").build();

        transactionRepository.save(tx);
        Transaction retrieved = transactionRepository.getTransaction(id);

        assertNotNull(retrieved, "Transaction should be retrievable after saving");
        assertEquals(tx, retrieved, "Retrieved transaction should match the saved one");
    }

    @Test
    void testGetByType() {
        transactionRepository.save(Transaction.builder().id(1L).amount(10.0).type("cars").build());
        transactionRepository.save(Transaction.builder().id(2L).amount(20.0).type("rent").build());
        transactionRepository.save(Transaction.builder().id(3L).amount(30.0).type("cars").build());

        List<Long> carsTransactions = transactionRepository.getByType("cars");

        assertNotNull(carsTransactions);
        assertEquals(2, carsTransactions.size(), "Should retrieve exactly two 'cars' transactions");
        assertTrue(carsTransactions.contains(1L));
        assertTrue(carsTransactions.contains(3L));

        List<Long> rentTransactions = transactionRepository.getByType("rent");
        assertEquals(1, rentTransactions.size());
        assertTrue(rentTransactions.contains(2L));

        List<Long> nonExistent = transactionRepository.getByType("food");
        assertTrue(nonExistent.isEmpty(), "Non-existent types should return an empty list");
    }

    @Test
    void testGetSum() {
        // Setup: Parent (id: 1) -> Child 1 (id: 2) -> Grandchild 1 (id: 4)
        // -> Child 2 (id: 3)
        transactionRepository.save(Transaction.builder().id(1L).amount(100.0).type("cars").build());
        transactionRepository.save(Transaction.builder().id(2L).amount(50.0).type("cars").parentId(1L).build());
        transactionRepository.save(Transaction.builder().id(3L).amount(25.0).type("cars").parentId(1L).build());
        transactionRepository.save(Transaction.builder().id(4L).amount(10.0).type("cars").parentId(2L).build());

        // Independent transaction
        transactionRepository.save(Transaction.builder().id(5L).amount(999.0).type("shopping").build());

        double totalSum = transactionRepository.getSum(1L);
        assertEquals(185.0, totalSum, 0.001,
                "Sum should recursively include all children and grandchildren (100+50+25+10)");

        double childSum = transactionRepository.getSum(2L);
        assertEquals(60.0, childSum, 0.001, "Sum should only include immediate children correctly (50+10)");

        double grandChildSum = transactionRepository.getSum(4L);
        assertEquals(10.0, grandChildSum, 0.001, "Sum of a leaf node should just be its own amount");

        double nonExistentSum = transactionRepository.getSum(99L);
        assertEquals(0.0, nonExistentSum, 0.001, "Sum for a non-existent transaction should be 0.0");
    }
}
