package com.pazzio.raspberry.glove.services.decorators;

import com.pazzio.raspberry.glove.dtos.RGBValueDto;
import com.pazzio.raspberry.glove.models.RGBValue;
import lombok.*;
import lombok.experimental.SuperBuilder;

import static java.util.Optional.ofNullable;

@Data
@Getter
@Setter
@SuperBuilder
@ToString
@EqualsAndHashCode(callSuper = true)
public class RGBValueDtoDecorator extends RGBValueDto {

    public RGBValueDto init(RGBValue entity){
        id = ofNullable(id).orElse(ofNullable(entity.getId()).orElse(null));
        red = ofNullable(red).orElse(ofNullable(entity.getRed()).orElse(0));
        green = ofNullable(green).orElse(ofNullable(entity.getGreen()).orElse(0));
        blue = ofNullable(blue).orElse(ofNullable(entity.getBlue()).orElse(0));
        pauseTime = ofNullable(pauseTime).orElse(ofNullable(entity.getPauseTime()).orElse(0.0));
        return this;
    }
}
