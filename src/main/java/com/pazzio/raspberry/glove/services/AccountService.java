package com.pazzio.raspberry.glove.services;

import com.pazzio.raspberry.glove.dtos.AccountDto;
import com.pazzio.raspberry.glove.dtos.LoadoutDto;
import com.pazzio.raspberry.glove.dtos.handling.GloveException;
import com.pazzio.raspberry.glove.dtos.handling.GloveExceptionType;
import com.pazzio.raspberry.glove.mappers.AccountMapper;
import com.pazzio.raspberry.glove.mappers.LoadoutMapper;
import com.pazzio.raspberry.glove.models.Account;
import com.pazzio.raspberry.glove.models.Loadout;
import com.pazzio.raspberry.glove.repositories.AccountRepository;
import com.pazzio.raspberry.glove.services.decorators.AccountDtoDecorator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
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


    public AccountDto getAccount(AccountDto dto){
        String token = dto.getFirebase_token();
        // TODO: Add FirebaseAuth for the token, instead of just passing the token for the "uid"
        Account entity = dto.getSerialNumber() != null ? accountRepository.findBySerialNumber(dto.getSerialNumber()) :
                token != null ? accountRepository.findByUid(token) : null;

        if(entity == null){
            throw new GloveException(GloveExceptionType.NO_AUTHENTICATION);
        }

        return accountMapper.toDto(entity);
    }

    public HashMap<String, Object> getChange(String serialNumber){
        Account user = accountRepository.findBySerialNumber(serialNumber);
        HashMap<String, Object> ret = new HashMap<>();
        if(user.getEdited()){
            ret.put("active", true);
            ret.put("loadoutList",user.getLoadoutList().stream().map(loadoutMapper::toDto).collect(Collectors.toList()));
        } else {
            ret.put("active", false);
        }
        return ret;
    }

    public void changed(String serialNumber){
        Account user = accountRepository.findBySerialNumber(serialNumber);
        user.setEdited(false);
        accountRepository.save(user);
        //TODO: Implement sending the notification to the phone
    }

    public AccountDto save(AccountDto accountDto){
        String uid = accountDto.getFirebase_token(); // TODO: Implement with FirebaseAuth
        final Account entity = accountDto.getId() != null ? accountRepository.getOne(accountDto.getId()) :
                accountDto.getFirebase_token() != null ? accountRepository.findByUid(uid) : new Account();
        entity.setUid(uid);
        List<LoadoutDto> loadoutList = accountDto.getLoadoutList();
        if(loadoutList != null && !loadoutList.isEmpty()){
            for(LoadoutDto loadout : loadoutList){
                loadoutService.save(loadout);
            }
        }
        AccountDtoDecorator decorator = AccountDtoDecorator.builder().build();
        accountMapper.decorate(accountDto, decorator);
        accountMapper.updateEntity(decorator.init(entity, loadoutMapper), entity);
        accountRepository.save(entity);
        return accountMapper.toDto(entity);
    }


}
