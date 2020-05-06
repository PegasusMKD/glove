package com.pazzio.raspberry.glove.services.decorators;

import com.pazzio.raspberry.glove.dtos.AccountDto;
import com.pazzio.raspberry.glove.dtos.handling.GloveException;
import com.pazzio.raspberry.glove.dtos.handling.GloveExceptionType;
import com.pazzio.raspberry.glove.mappers.LoadoutMapper;
import com.pazzio.raspberry.glove.models.Account;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;

@Data
@Getter
@Setter
@SuperBuilder
@ToString
@EqualsAndHashCode(callSuper = true)
public class AccountDtoDecorator extends AccountDto {

    public AccountDto init(Account entity, LoadoutMapper loadoutMapper){
        firebase_token = ofNullable(firebase_token).orElseThrow(() -> new GloveException(GloveExceptionType.MISSING_TOKEN));
        username = ofNullable(username).orElse(ofNullable(entity.getUsername()).orElse(""));
        serialNumber = ofNullable(serialNumber).orElse(ofNullable(entity.getSerialNumber()).orElse(""));
        loadoutList = entity.getLoadoutList().stream().map(loadoutMapper::toDto).collect(Collectors.toList());
        return this;
    }
}
