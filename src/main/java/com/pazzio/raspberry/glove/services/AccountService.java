package com.pazzio.raspberry.glove.services;

import com.pazzio.raspberry.glove.models.Account;
import com.pazzio.raspberry.glove.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    public Account getAccount(String username){
        // TODO: Implement when phone stuff is done
        return new Account();
    }

    public HashMap<String, Object> getChange(String serialNumber){
        Account user = accountRepository.findBySerialNumber(serialNumber);
        HashMap<String, Object> ret = new HashMap<>();
        if(user.getEdited()){
            ret.put("active", true);
            ret.put("loadouts",user.getLoadoutList());
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


}
