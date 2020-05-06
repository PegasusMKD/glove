package com.pazzio.raspberry.glove.services.decorators;

import com.pazzio.raspberry.glove.dtos.LoadoutDto;
import com.pazzio.raspberry.glove.dtos.RGBValueDto;
import com.pazzio.raspberry.glove.mappers.RGBValueMapper;
import com.pazzio.raspberry.glove.models.Loadout;
import com.pazzio.raspberry.glove.models.RGBValue;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;

@SuperBuilder
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class LoadoutDtoDecorator extends LoadoutDto {

    private List<RGBValueDto> emptyRGB = new ArrayList<RGBValueDto>(){{
        add(new RGBValueDto(0,0,0,0.0));
        add(new RGBValueDto(0,0,0,0.0));
        add(new RGBValueDto(0,0,0,0.0));
        add(new RGBValueDto(0,0,0,0.0));
        add(new RGBValueDto(0,0,0,0.0));
    }};

    private List<Double> emptyPause = new ArrayList<Double>(){{
       add(0.0);
       add(0.0);
       add(0.0);
       add(0.0);
       add(0.0);
    }};

    LoadoutDto init(Loadout entity, String type, RGBValueMapper rgbValueMapper){
        active = ofNullable(active).orElse(ofNullable(entity.getActive()).orElse(false));
        rgbValues = ofNullable(rgbValues).orElse(ofNullable(entity.getRgbValues().stream().map(rgbValueMapper::toDto).collect(Collectors.toList())).orElse(emptyRGB));
        pauseValues = ofNullable(pauseValues).orElse(ofNullable(entity.getPauseValues()).orElse(emptyPause));
        return this;
    }
}
