package com.igurman.gur_car_bot.model.dto;

import com.igurman.gur_car_bot.model.entity.PictureEntity;
import com.igurman.gur_car_bot.model.entity.VehicleEntity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.Map;

@Getter
@Setter
public class VehicleDetalizationDto {
    private Page<VehicleEntity> data;
    private Map<Integer, PictureEntity> firstPictureMap;
}
