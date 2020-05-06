package com.pazzio.raspberry.glove.services;

import com.pazzio.raspberry.glove.dtos.LoadoutDto;
import com.pazzio.raspberry.glove.dtos.RGBValueDto;
import com.pazzio.raspberry.glove.mappers.LoadoutMapper;
import com.pazzio.raspberry.glove.mappers.RGBValueMapper;
import com.pazzio.raspberry.glove.models.Loadout;
import com.pazzio.raspberry.glove.models.RGBValue;
import com.pazzio.raspberry.glove.repositories.LoadoutRepository;
import com.pazzio.raspberry.glove.services.decorators.AccountDtoDecorator;
import com.pazzio.raspberry.glove.services.decorators.LoadoutDtoDecorator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoadoutService {

    @Autowired
    private LoadoutRepository loadoutRepository;

    @Autowired
    private RGBValueService rgbValueService;

    @Autowired
    private LoadoutMapper loadoutMapper;

    @Autowired
    private RGBValueMapper rgbValueMapper;

    public LoadoutDto save(LoadoutDto loadout) {
        final Loadout entity = loadout.getId() != null ? loadoutRepository.getOne(loadout.getId()) : new Loadout();

        if(loadout.getRgbValues() != null && !loadout.getRgbValues().isEmpty()){
            for(RGBValueDto rgbValueDto : loadout.getRgbValues()){
                rgbValueService.save(rgbValueDto);
            }
        }

        LoadoutDtoDecorator decorator = LoadoutDtoDecorator.builder().build();
        loadoutMapper.decorate(loadout, decorator);
        loadoutMapper.updateEntity(decorator.init(entity, rgbValueMapper), entity);
        loadoutRepository.save(entity);
        return loadoutMapper.toDto(entity);
    }
}
