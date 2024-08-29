package com.igurman.gur_car_bot.repository;

import com.igurman.gur_car_bot.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
}
