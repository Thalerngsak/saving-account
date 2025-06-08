package com.example.saving.account.config;

import com.example.saving.account.repository.AccountRepository;
import com.example.saving.account.repository.UserRepository;
import org.springframework.stereotype.Component;

@Component
public class AccountSecurity {
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    public AccountSecurity(AccountRepository accountRepository, UserRepository userRepository) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
    }

    public boolean isOwner(String accountNumber, String email) {
        System.out.println("Checking account " + accountNumber + " for user " + email);
        return accountRepository.findByAccountNumber(accountNumber)
                .flatMap(account -> userRepository.findByEmail(email)
                        .map(user -> user.getCitizenId().equals(account.getCitizenId())))
                .orElse(false);
    }
}
