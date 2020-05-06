package com.pazzio.raspberry.glove.dtos;

import lombok.*;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AccountDto {
    public String firebase_token;
    public String username;
    public String serialNumber;
    public List<LoadoutDto> loadoutList;

}
