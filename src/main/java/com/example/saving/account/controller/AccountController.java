package com.example.saving.account.controller;

import com.example.saving.account.dto.*;
import com.example.saving.account.model.Account;
import com.example.saving.account.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.YearMonth;
import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@Tag(name = "Accounts")
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    //@PreAuthorize("hasRole('TELLER')")
    @Operation(summary = "Create new account")
    public ResponseEntity<Account> createAccount(@RequestBody AccountRequest request) {
        Account account = accountService.createAccount(request);
        return ResponseEntity.ok(account);
    }

    @PostMapping("/{accountNumber}/deposit")
    @PreAuthorize("hasRole('TELLER')")
    @Operation(summary = "Deposit money")
    public ResponseEntity<Account> deposit(@PathVariable(name = "accountNumber") String accountNumber,
                                           @RequestBody DepositRequest request,
                                           Authentication auth) {
        Account account = accountService.deposit(accountNumber, request.amount());
        return ResponseEntity.ok(account);
    }

    @PostMapping("/{accountNumber}/transfer")
    @PreAuthorize("hasRole('CUSTOMER') and @accountSecurity.isOwner(#accountNumber, authentication.principal)")
    @Operation(summary = "Transfer money")
    public ResponseEntity<Account> transfer(@PathVariable(name = "accountNumber") String accountNumber,
                                            @RequestBody TransferRequest request,
                                            Authentication auth) {
        Account account = accountService.transfer(accountNumber,
                request.toAccount(), request.amount(), auth.getName(), request.pin());
        return ResponseEntity.ok(account);
    }

    @PostMapping("/{accountNumber}/statement")
   @PreAuthorize("hasRole('CUSTOMER') and @accountSecurity.isOwner(#accountNumber, authentication.principal)")
    @Operation(summary = "Get bank statement for month")
    public ResponseEntity<List<StatementEntry>> statement(@PathVariable(name = "accountNumber") String accountNumber,
                                                          @RequestBody StatementRequest request,
                                                          Authentication auth) {
        YearMonth month = YearMonth.parse(request.month());
        List<StatementEntry> txs = accountService.getStatement(accountNumber, auth.getPrincipal().toString(), request.pin(), month);
        return ResponseEntity.ok(txs);
    }

    @GetMapping("/{accountNumber}")
    @PreAuthorize("hasRole('CUSTOMER') and @accountSecurity.isOwner(#accountNumber, authentication.principal)")
    @Operation(summary = "Get account information")
    public ResponseEntity<Account> getAccount(@PathVariable(name = "accountNumber")  String accountNumber, Authentication auth) {
        Account account = accountService.getAccountForUser(accountNumber, auth.getName());
        return ResponseEntity.ok(account);
    }
}