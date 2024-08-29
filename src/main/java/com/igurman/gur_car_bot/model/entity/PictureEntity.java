package com.igurman.gur_car_bot.model.entity;

import com.igurman.gur_car_bot.constant.PictureType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "pictures")
public class PictureEntity {

    // id файла
    @Id
    @SequenceGenerator(name = "sequence_pictures_id", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence_pictures_id")
    private Integer id;

    // id авто
    @Column(name = "vehicle_id")
    private Integer vehicleId;

    // название файла
    private String link;

    // название файла
    @Column(name = "link_tg")
    private String linkTg;

    // тип фото
    @Enumerated(EnumType.STRING)
    private PictureType type;

    // дата создания
    @CreationTimestamp
    @Column(name = "create_date")
    private LocalDateTime createDate;

}
