package com.pazzio.raspberry.glove.services;

import com.pazzio.raspberry.glove.dtos.LoadoutDto;
import com.pazzio.raspberry.glove.mappers.LoadoutMapper;
import com.pazzio.raspberry.glove.mappers.RGBValueMapper;
import com.pazzio.raspberry.glove.models.Loadout;
import com.pazzio.raspberry.glove.repositories.LoadoutRepository;
import com.pazzio.raspberry.glove.services.decorators.LoadoutDtoDecorator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public LoadoutDto save(LoadoutDto loadout) {
        final Loadout entity = loadout.getId() != null ? loadoutRepository.getOne(loadout.getId()) : new Loadout();
        LoadoutDtoDecorator decorator = LoadoutDtoDecorator.builder().build();
        loadoutMapper.decorate(loadout, decorator);
        loadoutMapper.updateEntity(decorator.init(entity, rgbValueMapper), entity);
        loadoutRepository.save(entity);
        return loadoutMapper.toDto(entity);
    }
}
