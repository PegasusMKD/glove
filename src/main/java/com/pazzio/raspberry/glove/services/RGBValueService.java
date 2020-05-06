package com.pazzio.raspberry.glove.services;

import com.pazzio.raspberry.glove.dtos.RGBValueDto;
import com.pazzio.raspberry.glove.mappers.RGBValueMapper;
import com.pazzio.raspberry.glove.models.RGBValue;
import com.pazzio.raspberry.glove.repositories.RGBValueRepository;
import com.pazzio.raspberry.glove.services.decorators.RGBValueDtoDecorator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RGBValueService {

    @Autowired
    private RGBValueRepository rgbValueRepository;

    @Autowired
    private RGBValueMapper rgbValueMapper;

    public RGBValueDto save(RGBValueDto rgbValueDto) {
        final RGBValue entity = rgbValueDto.getId() != null ? rgbValueRepository.getOne(rgbValueDto.getId()) : new RGBValue();
        RGBValueDtoDecorator decorator = RGBValueDtoDecorator.builder().build();
        rgbValueMapper.decorate(rgbValueDto, decorator);
        rgbValueMapper.updateEntity(decorator.init(entity), entity);
        rgbValueRepository.save(entity);
        return rgbValueMapper.toDto(entity);
    }
}
