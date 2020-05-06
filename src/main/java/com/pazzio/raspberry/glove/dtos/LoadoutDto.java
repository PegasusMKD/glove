package com.pazzio.raspberry.glove.dtos;

import com.pazzio.raspberry.glove.models.RGBValue;
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
public class LoadoutDto {
    public Boolean active;
    public List<RGBValueDto> rgbValues;
    public List<Double> pauseValues;
}
