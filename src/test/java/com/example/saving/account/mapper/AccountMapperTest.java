package com.example.saving.account.mapper;

import com.example.saving.account.dto.AccountRequest;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AccountMapperTest {
    private final AccountMapper mapper = Mappers.getMapper(AccountMapper.class);

    @Test
    void mapsInitialDeposit() {
        AccountRequest request = new AccountRequest("123", "Thai", "Eng", new BigDecimal("100"));
        var account = mapper.toEntity(request);
        assertEquals(new BigDecimal("100"), account.getBalance());
        assertEquals("123", account.getCitizenId());
    }

    @Test
    void mapsZeroWhenDepositNull() {
        AccountRequest request = new AccountRequest("123", "Thai", "Eng", null);
        var account = mapper.toEntity(request);
        assertEquals(BigDecimal.ZERO, account.getBalance());
    }
}
