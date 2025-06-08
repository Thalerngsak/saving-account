package com.example.saving.account.controller;

import com.example.saving.account.dto.AccountRequest;
import com.example.saving.account.service.AccountService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Testcontainers
@SpringBootTest
@Transactional
class AccountServiceIntegrationTest {

    @Container
    static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0")
            .withReuse(true);

    @DynamicPropertySource
    static void mysqlProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysql::getJdbcUrl);
        registry.add("spring.datasource.username", mysql::getUsername);
        registry.add("spring.datasource.password", mysql::getPassword);
    }

    @Autowired
    private AccountService accountService;

    @Test
    void createAndRetrieveAccount() {
        AccountRequest req = new AccountRequest("1111111111111", "Thai", "Eng", new BigDecimal("100"));
        var account = accountService.createAccount(req);
        var found = accountService.getAccount(account.getAccountNumber());
        assertEquals(new BigDecimal("100"), found.getBalance());
    }

    @Test
    void depositIncreasesBalance() {
        AccountRequest req = new AccountRequest("2222222222222", "T", "E", BigDecimal.ZERO);
        var account = accountService.createAccount(req);
        accountService.deposit(account.getAccountNumber(), new BigDecimal("50"));
        var found = accountService.getAccount(account.getAccountNumber());
        assertEquals(new BigDecimal("50"), found.getBalance());
    }
}
