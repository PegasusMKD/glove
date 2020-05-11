package com.pazzio.raspberry.glove.services;

import com.pazzio.raspberry.glove.dtos.AccountDto;
import com.pazzio.raspberry.glove.dtos.handling.GloveException;
import com.pazzio.raspberry.glove.dtos.handling.GloveExceptionType;
import com.pazzio.raspberry.glove.mappers.AccountMapper;
import com.pazzio.raspberry.glove.mappers.LoadoutMapper;
import com.pazzio.raspberry.glove.models.Account;
import com.pazzio.raspberry.glove.repositories.AccountRepository;
import com.pazzio.raspberry.glove.services.decorators.AccountDtoDecorator;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.stream.Collectors;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private LoadoutMapper loadoutMapper;

    @Autowired
    private LoadoutService loadoutService;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Transactional(readOnly = true)
    public AccountDto getAccount(AccountDto dto) {
        String token = dto.getToken();
        Account entity = dto.getSerialNumber() != null ? accountRepository.findBySerialNumber(dto.getSerialNumber()) :
                token != null ? accountRepository.findByToken(token) : dto.getUsername() != null ? accountRepository.findByUsername(dto.getUsername()) : null;

        if (entity == null) {
            throw new GloveException(GloveExceptionType.NO_AUTHENTICATION);
        }

        if (dto.getToken() == null && encoder.matches(dto.getPassword(), entity.getPassword())) {
            entity.setToken(RandomStringUtils.randomAlphanumeric(20));
        }
        return accountMapper.toDto(entity);
    }


    public HashMap<String, Object> getChange(String serialNumber){
        Account user = accountRepository.findBySerialNumber(serialNumber);
        System.out.println(user);
        HashMap<String, Object> ret = new HashMap<>();
        if(user.getEdited()){
            user.setEdited(false);
            ret.put("active", true);
            ret.put("loadoutList",user.getLoadoutList().stream().map(loadoutMapper::toDto).collect(Collectors.toList()));
            accountRepository.save(user);
        } else {
            ret.put("active", false);
        }
        System.out.println(ret);
        return ret;
    }

    public void changed(String serialNumber) {
        Account user = accountRepository.findBySerialNumber(serialNumber);
        user.setEdited(false);
        accountRepository.save(user);
        //TODO: Implement sending the notification to the phone
    }

    @Transactional
    public AccountDto save(AccountDto accountDto) {
        String token = accountDto.getToken();
        final Account entity = accountDto.getId() != null ? accountRepository.getOne(accountDto.getId()) :
                accountDto.getToken() != null ? accountRepository.findByToken(token) : new Account();
        entity.setToken(token);
        AccountDtoDecorator decorator = AccountDtoDecorator.builder().build();
        accountMapper.decorate(accountDto, decorator);
        accountMapper.updateEntity(decorator.init(entity, loadoutMapper), entity);
        entity.setEdited(true);
        accountRepository.save(entity);
        return accountMapper.toDto(entity);
    }


}
