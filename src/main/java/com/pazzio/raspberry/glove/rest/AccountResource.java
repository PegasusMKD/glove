package com.pazzio.raspberry.glove.rest;

import com.pazzio.raspberry.glove.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@RequestMapping("/api/account")
public class AccountResource {

    @Autowired
    private AccountService accountService;

    @PostMapping("/change")
    public ResponseEntity<HashMap<String, Object>> getChange(@RequestBody HashMap<String, String> body){
        HashMap<String, Object> res;
        try{
            res = accountService.getChange(body.get("serialNumber"));
        }catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().body(res);
    }






}
