package com.igurman.gur_car_bot.repository;

import com.igurman.gur_car_bot.model.entity.FilterEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FilterRepository extends JpaRepository<FilterEntity, Long> {

    Optional<FilterEntity> findByUserId(Long userId);

    void deleteAllByUserId(Long userId);

}
