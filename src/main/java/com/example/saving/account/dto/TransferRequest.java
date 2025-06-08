package com.example.saving.account.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

/**
 * Payload for money transfer requests.
 */
public record TransferRequest(
        @NotBlank String toAccount,
        @NotNull @DecimalMin(value = "1") BigDecimal amount,
        @NotBlank String pin) {
}
