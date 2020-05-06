package com.pazzio.raspberry.glove.services.decorators;

import com.pazzio.raspberry.glove.dtos.LoadoutDto;
import com.pazzio.raspberry.glove.dtos.RGBValueDto;
import com.pazzio.raspberry.glove.mappers.RGBValueMapper;
import com.pazzio.raspberry.glove.models.Loadout;
import com.pazzio.raspberry.glove.models.RGBValue;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.id.UUIDGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
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


    private List<Double> emptyPause = new ArrayList<Double>(){{
        add(0.0);
        add(0.0);
        add(0.0);
        add(0.0);
        add(0.0);
    }};

    public LoadoutDto init(Loadout entity, RGBValueMapper rgbValueMapper){
        List<RGBValueDto> emptyRGB = new ArrayList<RGBValueDto>(){{
            add(new RGBValueDto(UUID.randomUUID().toString(),0,0,0,0.0));
            add(new RGBValueDto(UUID.randomUUID().toString(),0,0,0,0.0));
            add(new RGBValueDto(UUID.randomUUID().toString(),0,0,0,0.0));
            add(new RGBValueDto(UUID.randomUUID().toString(),0,0,0,0.0));
            add(new RGBValueDto(UUID.randomUUID().toString(),0,0,0,0.0));
        }};

        active = ofNullable(active).orElse(ofNullable(entity.getActive()).orElse(false));
        rgbValues = ofNullable(rgbValues).orElse(ofNullable(entity.getRgbValues().stream().map(rgbValueMapper::toDto).collect(Collectors.toList())).orElse(emptyRGB));
        pauseValues = ofNullable(pauseValues).orElse(ofNullable(entity.getPauseValues()).orElse(emptyPause));
        return this;
    }
}
