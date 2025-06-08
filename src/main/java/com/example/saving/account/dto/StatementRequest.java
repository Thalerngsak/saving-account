package com.example.saving.account.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Request payload for bank statement retrieval.
 */
public record StatementRequest(
        @NotBlank String month,
        @NotBlank String pin) {
}
