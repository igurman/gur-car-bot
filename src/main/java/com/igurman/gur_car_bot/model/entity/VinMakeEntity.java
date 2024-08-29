package com.igurman.gur_car_bot.model.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Set;

@Entity
@Getter
@Setter
@ToString
@Table(name = "vin_make")
public class VinMakeEntity {

    // id производителя
    @Id
    private Integer id;

    // название производителя
    private String name;

    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    }, fetch = FetchType.EAGER)
    @JoinTable(name="vin_make_model",
            joinColumns=@JoinColumn(name="makeid"),
            inverseJoinColumns=@JoinColumn(name="modelid"))
    private Set<VinModelEntity> models;

}
