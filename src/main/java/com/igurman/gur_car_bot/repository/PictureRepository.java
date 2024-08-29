package com.igurman.gur_car_bot.repository;

import com.igurman.gur_car_bot.constant.PictureType;
import com.igurman.gur_car_bot.model.entity.PictureEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PictureRepository extends JpaRepository<PictureEntity, Integer> {

    List<PictureEntity> findAllByVehicleIdInAndType(List<Integer> vehicleIds, PictureType type);

    List<PictureEntity> findAllByVehicleId(Integer vehicleId);

    void deleteAllByVehicleId(Integer vehicleId);

}
