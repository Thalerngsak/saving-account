package com.example.saving.account.controller;

import com.example.saving.account.config.AccountSecurity;
import com.example.saving.account.controller.AccountController;
import com.example.saving.account.dto.AccountRequest;
import com.example.saving.account.model.Account;
import com.example.saving.account.security.JwtService;
import com.example.saving.account.service.AccountService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(AccountController.class)
class AccountControllerTest {

    @TestConfiguration
    static class TestSecurityConfig {
        @Bean
        @Primary
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
            http
                    .csrf(csrf -> csrf.ignoringRequestMatchers("/api/accounts/**"));
            return http.build();
        }
    }

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService accountService;


    @MockBean
    private AccountSecurity accountSecurity;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private com.example.saving.account.repository.UserRepository userRepository;

    @Test
    @WithMockUser(username = "teller@example.com", roles = "TELLER")
    @Disabled
    void createAccount() throws Exception {
        String json = "{\"citizenId\":\"987654321\",\"thaiName\":\"Thai\",\"englishName\":\"English\",\"initialDeposit\":100.0}";

        Account account = new Account();
        account.setAccountNumber("1234567");

        doReturn(account)
                .when(accountService)
                .createAccount(any(AccountRequest.class));

        io.jsonwebtoken.Claims claims = io.jsonwebtoken.Jwts.claims().setSubject("teller@example.com");
        claims.put("role", "TELLER");
        doReturn(claims)
                .when(jwtService)
                .parse("token");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/accounts")
                        .header("Authorization", "Bearer token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());
    }


    @Test
    @WithMockUser(username = "teller@example.com", roles = "TELLER")
    @Disabled
    void depositMoney() throws Exception {
        String json = "{\"amount\":50.0}";

        Account account = new Account();
        account.setAccountNumber("1234567");
        account.setBalance(new java.math.BigDecimal("150.0"));

        doReturn(account)
                .when(accountService)
                .deposit(eq("1234567"), any());

        io.jsonwebtoken.Claims claims = io.jsonwebtoken.Jwts.claims().setSubject("teller@example.com");
        claims.put("role", "TELLER");
        doReturn(claims)
                .when(jwtService)
                .parse("token");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/accounts/1234567/deposit")
                        .header("Authorization", "Bearer token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "test@example.com", roles = "CUSTOMER")
    @Disabled
    void viewOwnAccount() throws Exception {
        Account account = new Account();
        account.setAccountNumber("1234567");
        account.setCitizenId("987654321");
        account.setBalance(new java.math.BigDecimal("100"));

        doReturn(account)
                .when(accountService)
                .getAccountForUser("1234567", "test@example.com");

        doReturn(true)
                .when(accountSecurity)
                .isOwner("1234567", "test@example.com");

        io.jsonwebtoken.Claims claims = io.jsonwebtoken.Jwts.claims().setSubject("test@example.com");
        claims.put("role", "CUSTOMER");
        doReturn(claims)
                .when(jwtService)
                .parse("token");

        mockMvc.perform(MockMvcRequestBuilders.get("/api/accounts/1234567")
                        .header("Authorization", "Bearer token"))
                .andExpect(status().isOk());
    }


    @Test
    @WithMockUser(username = "test@example.com", roles = "CUSTOMER")
    @Disabled
    void transferMoney() throws Exception {
        String json = """
        {
            "toAccount": "7654321",
            "amount": 50.00,
            "pin": "123456"
        }""";

        Account account = new Account();
        account.setAccountNumber("1234567");
        account.setBalance(new java.math.BigDecimal("150.00"));

        doReturn(true)
                .when(accountSecurity)
                .isOwner(eq("1234567"), eq("test@example.com"));

        doReturn(account)
                .when(accountService)
                .transfer(
                        eq("1234567"),
                        eq("7654321"),
                        eq(new java.math.BigDecimal("50.00")),
                        eq("test@example.com"),
                        eq("123456")
                );

        mockMvc.perform(MockMvcRequestBuilders.post("/api/accounts/1234567/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());
    }


    @Test
    @WithMockUser(username = "test@example.com", roles = "CUSTOMER")
    @Disabled
    void viewStatement() throws Exception {
        String json = "{\"month\":\"2025-05\",\"pin\":\"123456\"}";
        com.example.saving.account.dto.StatementEntry entry = new com.example.saving.account.dto.StatementEntry(
                "15/12/2023", "10:30", "A0", "ATS", new java.math.BigDecimal("100"),
                new java.math.BigDecimal("200"), "Deposit");
        java.util.List<com.example.saving.account.dto.StatementEntry> list = java.util.Collections.singletonList(entry);

        doReturn(list)
                .when(accountService)
                .getStatement(eq("1234567"), eq("test@example.com"), eq("123456"), any());

        doReturn(true)
                .when(accountSecurity)
                .isOwner("1234567", "test@example.com");

        io.jsonwebtoken.Claims claims = io.jsonwebtoken.Jwts.claims().setSubject("test@example.com");
        claims.put("role", "CUSTOMER");
        doReturn(claims)
                .when(jwtService)
                .parse("token");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/accounts/1234567/statement")
                        .header("Authorization", "Bearer token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isOk());
    }

}