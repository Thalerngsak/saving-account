package com.example.saving.account.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

/**
 * Payload for money deposit requests.
 */
public record DepositRequest(
        @NotNull
        @DecimalMin(value = "1")
        BigDecimal amount) {
}
