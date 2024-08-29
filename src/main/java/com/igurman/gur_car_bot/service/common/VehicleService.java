package com.igurman.gur_car_bot.service.common;

import com.igurman.gur_car_bot.constant.AuctionType;
import com.igurman.gur_car_bot.constant.VehicleStatusType;
import com.igurman.gur_car_bot.exception.TelegramException;
import com.igurman.gur_car_bot.model.entity.VehicleEntity;
import com.igurman.gur_car_bot.repository.VehicleRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Limit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.InputFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class VehicleService {
    private final VehicleRepository vehicleRepository;
    private static final String NO_PICTURE_PATH = "static/files/no_photo.png";

    public Page<VehicleEntity> findAll(Specification<VehicleEntity> spec, Pageable pageable) {
        return vehicleRepository.findAll(spec, pageable);
    }

    public VehicleEntity findById(Integer id) {
        if (id == null) {
            return null;
        }
        return vehicleRepository.findById(id).orElse(null);
    }

    public Page<Object[]> findAllDistinctMake(Pageable pageable) {
        return vehicleRepository.findAllDistinctMake(List.of(VehicleStatusType.WEB, VehicleStatusType.POSTED), pageable);
    }

    public Page<Object[]> findAllDistinctModelByMake(Integer makeId, Pageable pageable) {
        return vehicleRepository.findAllDistinctModelByMake(makeId, List.of(VehicleStatusType.WEB, VehicleStatusType.POSTED), pageable);
    }

    public VehicleEntity findMakeByMakeId(Integer makeId) {
        if (makeId == 0) {
            return null;
        }
        return vehicleRepository.findMakeByMakeId(makeId).orElse(null);
    }

    public VehicleEntity save(VehicleEntity model) {
        if (model == null) {
            return null;
        }
        return vehicleRepository.save(model);
    }

    public VehicleEntity findByVin(String vin) {
        if (vin == null) {
            return null;
        }
        return vehicleRepository.findByVin(vin).orElse(null);
    }

    public List<VehicleEntity> findAllByStatusAndAuction(VehicleStatusType status, AuctionType auction, int count) {
        if (status == null || auction == null) {
            return new ArrayList<>();
        }
        return vehicleRepository.findAllByStatusAndAuction(status, auction, Limit.of(count));
    }

    public void savePictureTg(String pictureUrl, Integer vehicheId) {
        if (pictureUrl != null && vehicheId != null) {
            this.vehicleRepository.findById(vehicheId).ifPresent(vehicleEntity -> vehicleEntity.setPictureTg(pictureUrl));
        }
    }

    public List<VehicleEntity> findForPublisher(@NonNull Integer days, @NonNull Integer count) {
        return this.vehicleRepository.findForPublisherWithStatusAndCreateDate(
                VehicleStatusType.WEB,
                LocalDateTime.now().minusDays(days),
                PageRequest.of(0, count));
    }

    public InputFile getPicture(VehicleEntity vehicle) {
        if (vehicle.getPicture() != null && vehicle.getPicture().startsWith("http")) {
            return new InputFile(vehicle.getPicture());
        } else {
            try {
                return new InputFile(new ClassPathResource(NO_PICTURE_PATH).getInputStream(), "no_photo.png");
            } catch (IOException e) {
                log.error("*** Не смог загрузить фото НЕТ АВТО{}", e.getMessage());
                throw new TelegramException("*** Не смог загрузить фото НЕТ АВТО{}", e);
            }
        }
    }
}
