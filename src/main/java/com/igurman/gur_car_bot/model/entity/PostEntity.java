package com.igurman.gur_car_bot.model.entity;

import com.igurman.gur_car_bot.constant.PosterStatusType;
import com.igurman.gur_car_bot.constant.PosterUserType;
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
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@Builder
@Table(name = "posts")
@NoArgsConstructor
@AllArgsConstructor
public class PostEntity {

    @Id
    @SequenceGenerator(name = "sequence_posters_id", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence_posters_id")
    private Integer id;

    // id пользователя
    @Column(name = "user_id")
    private Long userId;

    // id группы
    @Column(name = "group_id")
    private Long groupId;

    // id канала группы
    @Column(name = "group_channel_id")
    private Integer groupChannelId;

    // id авто
    @Column(name = "vehicle_id")
    private Integer vehicleId;

    // id постаов
    @Column(name = "posts_id", columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private List<Integer> postList;

    // статус поста
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private PosterStatusType status;

    // статус поста
    @Enumerated(EnumType.STRING)
    @Column(name = "user_type")
    private PosterUserType userType;

    // дата удаления
    @Column(name = "delete_date")
    private LocalDate deleteDate;

    // дата создания
    @CreationTimestamp
    @Column(name = "create_date")
    private LocalDateTime createDate;

}
