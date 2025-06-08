package com.example.saving.account.dto;

import java.math.BigDecimal;

/**
 * Representation of a line item in a bank statement.
 */
public record StatementEntry(
        String date,
        String time,
        String code,
        String channel,
        BigDecimal debitCredit,
        BigDecimal balance,
        String remark) {
}
