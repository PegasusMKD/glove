package com.pazzio.raspberry.glove.mappers;

import com.pazzio.raspberry.glove.dtos.AccountDto;
import com.pazzio.raspberry.glove.models.Account;
import com.pazzio.raspberry.glove.services.decorators.AccountDtoDecorator;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.stereotype.Repository;
import org.mapstruct.Mapper;


@Repository
@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface AccountMapper {
    AccountDto toDto(Account entity);
    Account toEntity(AccountDto dto);
    void updateEntity(AccountDto dto, @MappingTarget Account entity);
    void decorate(AccountDto dto, @MappingTarget AccountDtoDecorator decorator);
}
