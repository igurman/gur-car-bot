package com.igurman.gur_car_bot.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@Table(name = "vin_model")
public class VinModelEntity {

    // id модели
    @Id
    private Integer id;

    // название модели
    private String name;

}
