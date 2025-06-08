package com.example.saving.account.repository;

import com.example.saving.account.model.Account;
import com.example.saving.account.model.Transaction;
import com.example.saving.account.model.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByAccountAndTimestampBetweenOrderByTimestampAsc(Account account,
            LocalDateTime start, LocalDateTime end);

    @Query("select coalesce(sum(t.amount), 0) from Transaction t where t.account = :account and t.type = :type and t.timestamp >= :start and t.timestamp < :end")
    BigDecimal sumAmountByAccountAndTypeAndTimestampBetween(Account account,
            TransactionType type, LocalDateTime start, LocalDateTime end);
}