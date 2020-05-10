package com.pazzio.raspberry.glove.mappers;

import com.pazzio.raspberry.glove.dtos.AccountDto;
import com.pazzio.raspberry.glove.models.Account;
import com.pazzio.raspberry.glove.services.decorators.AccountDtoDecorator;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.stereotype.Repository;


@Repository
@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface AccountMapper {
    @Mapping(target = "password", ignore = true)
    AccountDto toDto(Account entity);
    Account toEntity(AccountDto dto);
    void updateEntity(AccountDto dto, @MappingTarget Account entity);
    void decorate(AccountDto dto, @MappingTarget AccountDtoDecorator decorator);
}
