package com.pazzio.raspberry.glove.dtos;

import lombok.*;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RGBValueDto {
    public Integer red;
    public Integer blue;
    public Integer green;
    public Integer pauseTime;

}
