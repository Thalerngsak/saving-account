package com.example.saving.account.mapper;

import com.example.saving.account.dto.StatementEntry;
import com.example.saving.account.model.Transaction;
import com.example.saving.account.model.TransactionChannel;
import com.example.saving.account.model.TransactionType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.time.format.DateTimeFormatter;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TransactionMapper {
    DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("d/M/yyyy");
    DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("HH:mm");

    @Mapping(target = "date", expression = "java(tx.getTimestamp().toLocalDate().format(DATE_FMT))")
    @Mapping(target = "time", expression = "java(tx.getTimestamp().toLocalTime().format(TIME_FMT))")
    @Mapping(target = "code", expression = "java(mapCode(tx.getType()))")
    @Mapping(target = "channel", expression = "java(mapChannel(tx.getChannel()))")
    @Mapping(target = "debitCredit", expression = "java(tx.getType() == com.example.saving.account.model.TransactionType.TRANSFER_OUT ? tx.getAmount().negate() : tx.getAmount())")
    StatementEntry toDto(Transaction tx);

    default String mapCode(TransactionType type) {
        return switch (type) {
            case DEPOSIT -> "A0";
            case TRANSFER_OUT -> "A1";
            case TRANSFER_IN -> "A3";
            default -> throw new IllegalStateException("Unexpected value: " + type);
        };
    }

    default String mapChannel(TransactionChannel channel) {
        return switch (channel) {
            case TELLER -> "OTC";
            case ONLINE -> "ATS";
            default -> throw new IllegalStateException("Unexpected value: " + channel);
        };
    }
}