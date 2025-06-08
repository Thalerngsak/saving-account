package com.example.saving.account.mapper;

import com.example.saving.account.dto.AccountRequest;
import com.example.saving.account.model.Account;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, imports = java.math.BigDecimal.class)
public interface AccountMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "balance", expression = "java(request.initialDeposit() != null ? request.initialDeposit() : BigDecimal.ZERO)")
    Account toEntity(AccountRequest request);
}
