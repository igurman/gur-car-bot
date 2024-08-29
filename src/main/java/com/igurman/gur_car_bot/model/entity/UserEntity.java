package com.igurman.gur_car_bot.model.entity;

import com.igurman.gur_car_bot.constant.StatusUserType;
import com.igurman.gur_car_bot.constant.UserType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
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
@ToString
@Builder
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {

    // id пользователя
    @Id
    private Long id;

    // имя
    @Column(name = "first_name")
    private String firstName;

    // логин TG
    @Column(name = "user_name")
    private String userName;

    // бот/не бот
    @Column(name = "is_bot")
    private Boolean isBot;

    // язык
    private String language;

    // тип пользователя
    @Enumerated(EnumType.STRING)
    private UserType type;

    // статус пользователя при запросах (читает/пишет)
    @Enumerated(EnumType.STRING)
    private StatusUserType status;

    // активный
    private Boolean activity;

    // дата создания
    @CreationTimestamp
    @Column(name = "create_date")
    private LocalDateTime createDate;

}
