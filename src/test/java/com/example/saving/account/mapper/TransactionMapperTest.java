package com.example.saving.account.mapper;

import com.example.saving.account.dto.StatementEntry;
import com.example.saving.account.model.Account;
import com.example.saving.account.model.Transaction;
import com.example.saving.account.model.TransactionChannel;
import com.example.saving.account.model.TransactionType;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TransactionMapperTest {
    private final TransactionMapper mapper = Mappers.getMapper(TransactionMapper.class);

    @Test
    void mapsDeposit() {
        Transaction tx = new Transaction();
        tx.setAccount(new Account());
        tx.setTimestamp(LocalDateTime.of(2024, 5, 1, 12, 30));
        tx.setType(TransactionType.DEPOSIT);
        tx.setChannel(TransactionChannel.TELLER);
        tx.setAmount(new BigDecimal("50"));
        tx.setBalance(new BigDecimal("150"));
        tx.setRemark("Deposit");

        StatementEntry entry = mapper.toDto(tx);
        assertEquals("1/5/2024", entry.date());
        assertEquals("12:30", entry.time());
        assertEquals("A0", entry.code());
        assertEquals("OTC", entry.channel());
        assertEquals(new BigDecimal("50"), entry.debitCredit());
        assertEquals(new BigDecimal("150"), entry.balance());
    }

    @Test
    void mapsTransferOutNegative() {
        Transaction tx = new Transaction();
        tx.setAccount(new Account());
        tx.setTimestamp(LocalDateTime.of(2024, 5, 1, 13, 0));
        tx.setType(TransactionType.TRANSFER_OUT);
        tx.setChannel(TransactionChannel.ONLINE);
        tx.setAmount(new BigDecimal("20"));
        tx.setBalance(new BigDecimal("80"));
        tx.setRemark("To 123");

        StatementEntry entry = mapper.toDto(tx);
        assertEquals("A1", entry.code());
        assertEquals("ATS", entry.channel());
        assertEquals(new BigDecimal("-20"), entry.debitCredit());
    }
}
