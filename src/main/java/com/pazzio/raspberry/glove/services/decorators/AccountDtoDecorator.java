package com.pazzio.raspberry.glove.services.decorators;

import com.pazzio.raspberry.glove.dtos.AccountDto;
import com.pazzio.raspberry.glove.mappers.LoadoutMapper;
import com.pazzio.raspberry.glove.models.Account;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;

@Data
@Getter
@Setter
@SuperBuilder
@ToString
@EqualsAndHashCode(callSuper = true)
public class AccountDtoDecorator extends AccountDto {

    public AccountDto init(Account entity, LoadoutMapper loadoutMapper) {
        id = ofNullable(id).orElse(ofNullable(entity.getId()).orElse(null));
        token = ofNullable(token).orElse(RandomStringUtils.randomAlphanumeric(20));
        username = ofNullable(username).orElse(ofNullable(entity.getUsername()).orElse(""));
        if (password != null) {
            password = new BCryptPasswordEncoder().encode(password);
        } else {
            password = entity.getPassword();
        }
        serialNumber = ofNullable(serialNumber).orElse(ofNullable(entity.getSerialNumber()).orElse(""));
        if (loadoutList == null && entity.getLoadoutList() != null) {
            loadoutList = entity.getLoadoutList().stream().map(loadoutMapper::toDto).collect(Collectors.toList());
        }
        return this;
    }
}
