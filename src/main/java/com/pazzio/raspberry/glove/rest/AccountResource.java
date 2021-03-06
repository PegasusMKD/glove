package com.pazzio.raspberry.glove.rest;

import com.pazzio.raspberry.glove.dtos.LoadoutDto;
import com.pazzio.raspberry.glove.models.Loadout;
import com.pazzio.raspberry.glove.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/api/account")
public class AccountResource {

    @Autowired
    private AccountService accountService;

    @PostMapping(value = "/change", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<HashMap<String, Object>> getChange(@RequestBody HashMap<String, String> body){
        HashMap<String, Object> res;
        try{
            System.out.println(body);
            res = accountService.getChange(body.get("serialNumber"));
            System.out.println(res);
        }catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().body(res);
    }

    @PostMapping(value = "/loadouts", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<LoadoutDto>> getLoadouts(@RequestBody HashMap<String, String> body){
        List<LoadoutDto> res;
        try{
            res = accountService.getLoadouts(body.get("serialNumber"));
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().body(res);
    }






}
