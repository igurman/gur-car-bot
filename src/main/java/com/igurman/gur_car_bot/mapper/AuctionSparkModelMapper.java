package com.igurman.gur_car_bot.mapper;

import com.igurman.gur_car_bot.model.entity.VehicleEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public abstract class AuctionSparkModelMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createDate", ignore = true)
    @Mapping(target = "auction", ignore = true)
    @Mapping(target = "auctionId", ignore = true)
    @Mapping(target = "auctionData", ignore = true)
    @Mapping(target = "status", ignore = true)
    public abstract void mapToEntity(@MappingTarget VehicleEntity entity, VehicleEntity source);

}
