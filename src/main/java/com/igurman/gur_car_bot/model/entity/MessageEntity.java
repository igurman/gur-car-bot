package com.igurman.gur_car_bot.model.entity;

import com.igurman.gur_car_bot.constant.MessageStatusType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
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
@Builder
@ToString
@Table(name = "messages")
@NoArgsConstructor
@AllArgsConstructor
public class MessageEntity {

    // id сообщения
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Ид пользователя
    @Column(name = "user_id")
    private Long userId;

    // сообщение
    private String message;

    // статус сообщения новое/прочитанное
    @Enumerated(EnumType.STRING)
    private MessageStatusType status;

    // дата создания
    @CreationTimestamp
    @Column(name = "create_date")
    private LocalDateTime createDate;

}
