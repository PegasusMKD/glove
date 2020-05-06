package com.pazzio.raspberry.glove.mappers;

import com.pazzio.raspberry.glove.dtos.RGBValueDto;
import com.pazzio.raspberry.glove.models.RGBValue;
import com.pazzio.raspberry.glove.services.decorators.RGBValueDtoDecorator;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.stereotype.Repository;

@Repository
@Mapper(componentModel = "spring")
public interface RGBValueMapper {
    RGBValueDto toDto(RGBValue entity);
    RGBValue toEntity(RGBValueDto dto);
    void updateEntity(RGBValueDto dto, @MappingTarget RGBValue entity);
    void decorate(RGBValueDto dto, @MappingTarget RGBValueDtoDecorator decorator);
}
