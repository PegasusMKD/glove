package com.pazzio.raspberry.glove.dtos;

import lombok.*;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class RGBValueDto {
    public String id;
    public Integer finger;
    public Double activeTime;
    public Integer red;
    public Integer blue;
    public Integer green;
    public Double pauseTime;
}
