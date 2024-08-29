package com.igurman.gur_car_bot.model.entity;

import com.igurman.gur_car_bot.constant.AuctionType;
import com.igurman.gur_car_bot.constant.ColorType;
import com.igurman.gur_car_bot.constant.DriveTrainType;
import com.igurman.gur_car_bot.constant.EngineType;
import com.igurman.gur_car_bot.constant.TransmissionType;
import com.igurman.gur_car_bot.constant.VehicleStatusType;
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
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "vehicles")
public class VehicleEntity {

    // id записи
    @Id
    @SequenceGenerator(name = "sequence_vehicles_id", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence_vehicles_id")
    private Integer id;

    @Column(name = "auction_id")
    private String auctionId;

    // название производителя
    @Column(name = "make_title")
    private String makeTitle;

    // ид производителя
    @Column(name = "make_id")
    private Integer makeId;

    // название модели
    @Column(name = "model_title")
    private String modelTitle;

    // ид модели
    @Column(name = "model_id")
    private Integer modelId;

    // комплектация
    private String complect;

    // цвет авто
    @Enumerated(EnumType.STRING)
    private ColorType color;

    // цвет салона
    @Column(name = "interior_color")
    private String interiorColor;

    // год выпуска
    private Integer year;

    // пробег
    private Integer odometer;

    //vin номер
    private String vin;

    // место нахождения
    private String location;

    // место стоянки
    private String lane;

    // продавец
    private String seller;

    // двигатель
    private String engine;

    // трансмиссия
    @Enumerated(EnumType.STRING)
    private TransmissionType transmission;

    //
    @Enumerated(EnumType.STRING)
    @Column(name = "drive_train")
    private DriveTrainType driveTrain;

    // цена
    private Integer price;

    // Описание
    private String announcement;

    //
    private String title;

    // оценка
    private Integer grade;

    //
    private String driveable;

    // ссылка
    private String link;

    // дата продажи
    @Column(name = "sale_date")
    private LocalDate saleDate;

    // дата создания
    @CreationTimestamp
    @Column(name = "create_date")
    private LocalDateTime createDate;

    // id типа двигателя
    @Column(name = "engine_type_id")
    private Integer engineTypeId;

    // тип двигателя
    @Enumerated(EnumType.STRING)
    @Column(name = "engine_type")
    private EngineType engineType;

    // тип аукциона
    @Enumerated(EnumType.STRING)
    @Column(name = "auction")
    private AuctionType auction;

    // json сырых данных
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "auction_data")
    private String auctionData;

    // статус записи
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private VehicleStatusType status;

    // ошибки парсинга
    private String error;

    // главное фото
    private String picture;

    // главное фото сохраненное в телеграм боте
    @Column(name = "picture_tg")
    private String pictureTg;

    // объем двигателя
    private Integer displacement;

}
