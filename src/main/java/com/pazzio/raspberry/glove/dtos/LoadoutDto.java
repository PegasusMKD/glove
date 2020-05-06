package com.pazzio.raspberry.glove.dtos;

import com.pazzio.raspberry.glove.models.RGBValue;
import lombok.*;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LoadoutDto {
    public Boolean active;
    public List<RGBValueDto> rgbValues;
    public List<Double> pauseValues;
}
