package com.pazzio.raspberry.glove.mappers;

import com.pazzio.raspberry.glove.dtos.LoadoutDto;
import com.pazzio.raspberry.glove.models.Loadout;
import com.pazzio.raspberry.glove.services.decorators.LoadoutDtoDecorator;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.stereotype.Repository;

@Repository
@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface LoadoutMapper {
    LoadoutDto toDto(Loadout entity);
    Loadout toEntity(LoadoutDto dto);
    void updateEntity(LoadoutDto dto, @MappingTarget Loadout entity);
    void decorate(LoadoutDto dto, @MappingTarget LoadoutDtoDecorator decorator);
}
