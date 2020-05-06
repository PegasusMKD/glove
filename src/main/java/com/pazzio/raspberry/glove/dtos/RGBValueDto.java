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
    public Integer red;
    public Integer blue;
    public Integer green;
    public Double pauseTime;
}
