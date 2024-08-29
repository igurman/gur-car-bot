package com.igurman.gur_car_bot.service.common;

import com.igurman.gur_car_bot.constant.PictureType;
import com.igurman.gur_car_bot.model.entity.PictureEntity;
import com.igurman.gur_car_bot.repository.PictureRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PictureService {
    private final PictureRepository pictureRepository;

    public List<PictureEntity> findFirstByVehicleIds(List<Integer> vehicleIds) {
        if (CollectionUtils.isEmpty(vehicleIds)) {
            return new ArrayList<>();
        }
        return pictureRepository.findAllByVehicleIdInAndType(vehicleIds, PictureType.FIRST);
    }

    public List<PictureEntity> findByVehicleId(Integer vehicleId) {
        if (vehicleId == null) {
            return new ArrayList<>();
        }
        return pictureRepository.findAllByVehicleId(vehicleId);
    }

    @Transactional
    public void saveWithCleanAll(List<PictureEntity> pictureList, Integer vehicleId) {
        if (CollectionUtils.isEmpty(pictureList)) {
            return;
        }
        pictureRepository.deleteAllByVehicleId(vehicleId);
        pictureRepository.saveAll(pictureList);
    }
}
