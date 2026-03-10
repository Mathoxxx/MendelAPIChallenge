package com.mendel.transactionsapi.integration;

import com.mendel.transactionsapi.dto.Transaction;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TransactionsApiIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void testTransactionsWorkflow() {
        // Given
        long parentId = 101L;
        long childId = 102L;
        String transactionType = "groceries";

        createTransaction(parentId, 100.0, transactionType, null);
        createTransaction(childId, 50.5, transactionType, parentId);

        // When
        ResponseEntity<List> typeResponse = restTemplate.getForEntity("/transactions/types/" + transactionType,
                List.class);
        ResponseEntity<Map> sumResponse = restTemplate.getForEntity("/transactions/sum/" + parentId, Map.class);
        ResponseEntity<Transaction> getResponse = restTemplate.getForEntity("/transactions/" + parentId,
                Transaction.class);

        // Then
        assertTransactionsByTypeContains(typeResponse, parentId, childId);
        assertTransactionSumEquals(sumResponse, 150.5);
        assertTransactionEquals(getResponse, parentId, 100.0, transactionType, null);
    }

    private void createTransaction(long id, double amount, String type, Long parentId) {
        Transaction transaction = Transaction.builder()
                .amount(amount)
                .type(type)
                .parentId(parentId)
                .build();

        HttpEntity<Transaction> request = new HttpEntity<>(transaction);
        ResponseEntity<Transaction> response = restTemplate.exchange(
                "/transactions/" + id,
                HttpMethod.PUT,
                request,
                Transaction.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode(), "Transaction creation should return 201 CREATED");
    }

    private void assertTransactionsByTypeContains(ResponseEntity<List> response, long parentId, long childId) {
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Getting transactions by type should return 200 OK");
        assertNotNull(response.getBody(), "Response body should not be null");

        List rawList = response.getBody();
        assertTrue(rawList.contains((int) parentId), "List should contain the parent transaction ID");
        assertTrue(rawList.contains((int) childId), "List should contain the child transaction ID");
    }

    private void assertTransactionSumEquals(ResponseEntity<Map> response, double expectedSum) {
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Getting transaction sum should return 200 OK");
        assertNotNull(response.getBody(), "Response body should not be null");

        Double actualSum = (Double) response.getBody().get("sum");
        assertNotNull(actualSum, "Sum value should be present in the response map");
        assertEquals(expectedSum, actualSum, 0.001,
                "The calculated transaction sum should exactly match the expected sum");
    }

    private void assertTransactionEquals(ResponseEntity<Transaction> response, long expectedId, double expectedAmount,
            String expectedType, Long expectedParentId) {
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Getting transaction by ID should return 200 OK");
        Transaction body = response.getBody();
        assertNotNull(body, "Transaction response body should not be null");
        assertEquals(expectedId, body.getId());
        assertEquals(expectedAmount, body.getAmount());
        assertEquals(expectedType, body.getType());
        assertEquals(expectedParentId, body.getParentId());
    }
}
