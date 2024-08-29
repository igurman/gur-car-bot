package com.igurman.gur_car_bot.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString
@Table(name = "filters")
public class FilterEntity {

    // id пользователя
    @Id
    @Column(name = "user_id")
    private Long userId;

    // модель
    @Column(name = "make_model", columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<Integer, Set<Integer>> makeModel;

    // год начала поиска
    @Column(name = "year_start")
    private Integer yearStart;

    // год конца поиска
    @Column(name = "year_end")
    private Integer yearEnd;

    // город
    private String city;

    // пробег от
    @Column(name = "odometer_start")
    private Integer odometerStart;

    // пробег до
    @Column(name = "odometer_end")
    private Integer odometerEnd;

    // цена от
    @Column(name = "price_start")
    private Integer priceStart;

    // цена до
    @Column(name = "price_end")
    private Integer priceEnd;

    // оценка от
    @Column(name = "grade_start")
    private Integer gradeStart;

    // оценка до
    @Column(name = "grade_end")
    private Integer gradeEnd;

    // тип двигателя
    @Column(name = "engine_types", columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private Set<Integer> engineTypes;

    // дата создания
    @CreationTimestamp
    @Column(name = "create_date")
    private LocalDateTime createDate;

}
