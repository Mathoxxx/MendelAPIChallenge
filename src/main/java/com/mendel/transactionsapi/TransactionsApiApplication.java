package com.mendel.transactionsapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point for the Mendel API Challenge Transactions Microservice.
 * This class sets up and launches the Spring Boot context.
 */
@SpringBootApplication
public class TransactionsApiApplication {

    /**
     * Default constructor for the Spring framework explicitly declared to attach
     * Javadoc.
     */
    public TransactionsApiApplication() {
    }

    /**
     * Main method that drives the application execution.
     *
     * @param args Array of command-line arguments passed to the application.
     */
    public static void main(String[] args) {
        SpringApplication.run(TransactionsApiApplication.class, args);
    }

}
