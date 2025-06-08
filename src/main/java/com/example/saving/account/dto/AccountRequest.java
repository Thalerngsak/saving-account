package com.example.saving.account.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

/**
 * Payload for account creation requests.
 */
public record AccountRequest(
        @NotBlank String citizenId,
        @NotBlank String thaiName,
        @NotBlank String englishName,
        @PositiveOrZero BigDecimal initialDeposit) {
}