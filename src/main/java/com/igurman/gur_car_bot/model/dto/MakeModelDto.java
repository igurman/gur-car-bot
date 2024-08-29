package com.igurman.gur_car_bot.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MakeModelDto {
    private Integer makeId;
    private String makeName;
    private List<ModelDto> models;
}
