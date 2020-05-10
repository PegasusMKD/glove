package com.pazzio.raspberry.glove.services.decorators;

import com.pazzio.raspberry.glove.dtos.LoadoutDto;
import com.pazzio.raspberry.glove.dtos.RGBValueDto;
import com.pazzio.raspberry.glove.mappers.RGBValueMapper;
import com.pazzio.raspberry.glove.models.Loadout;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
        List<RGBValueDto> emptyRGB = new ArrayList<RGBValueDto>() {{
            add(new RGBValueDto(UUID.randomUUID().toString(), 1, 0.0, 0, 0, 0, 0.0));
            add(new RGBValueDto(UUID.randomUUID().toString(), 2, 0.0, 0, 0, 0, 0.0));
            add(new RGBValueDto(UUID.randomUUID().toString(), 3, 0.0, 0, 0, 0, 0.0));
            add(new RGBValueDto(UUID.randomUUID().toString(), 4, 0.0, 0, 0, 0, 0.0));
            add(new RGBValueDto(UUID.randomUUID().toString(), 5, 0.0, 0, 0, 0, 0.0));
        }};

        active = ofNullable(active).orElse(ofNullable(entity.getActive()).orElse(false));
        System.out.println(rgbValues);
        rgbValues = ofNullable(rgbValues).orElse(emptyRGB);
        pauseValues = ofNullable(pauseValues).orElse(ofNullable(entity.getPauseValues()).orElse(emptyPause));
        return this;
    }
}
