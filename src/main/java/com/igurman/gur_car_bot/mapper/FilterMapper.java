package com.igurman.gur_car_bot.mapper;

import com.igurman.gur_car_bot.model.dto.FilterDto;
import com.igurman.gur_car_bot.model.entity.FilterEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FilterMapper {

    @Mapping(target = "makeModelList", ignore = true)
    @Mapping(target = "makeModelMap", ignore = true)
    FilterDto toDto(FilterEntity source);

}
