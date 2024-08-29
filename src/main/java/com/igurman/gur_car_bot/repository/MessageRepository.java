package com.igurman.gur_car_bot.repository;

import com.igurman.gur_car_bot.model.entity.MessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<MessageEntity, Integer> {

    List<MessageEntity> findAllByUserId(Long userId);

}
