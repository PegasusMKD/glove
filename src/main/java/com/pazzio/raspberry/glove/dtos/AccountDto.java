package com.pazzio.raspberry.glove.dtos;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@SuperBuilder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class AccountDto {
    public String id;
    public String token;
    public String username;
    public String password;
    public String serialNumber;
    public List<LoadoutDto> loadoutList;

}
