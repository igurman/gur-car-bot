package com.igurman.gur_car_bot.repository;

import com.igurman.gur_car_bot.model.entity.VinMakeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VinMakeRepository extends JpaRepository<VinMakeEntity, Integer> {
    VinMakeEntity findByNameIgnoreCase(String name);
}
