package com.igurman.gur_car_bot.service.provider;

import com.igurman.gur_car_bot.constant.AuctionType;
import com.igurman.gur_car_bot.constant.EngineType;
import com.igurman.gur_car_bot.model.dto.VehicleDetalizationDto;
import com.igurman.gur_car_bot.model.entity.FilterEntity;
import com.igurman.gur_car_bot.model.entity.PictureEntity;
import com.igurman.gur_car_bot.model.entity.VehicleEntity;
import com.igurman.gur_car_bot.repository.specification.VehicleEntitySpecifications;
import com.igurman.gur_car_bot.service.common.FilterService;
import com.igurman.gur_car_bot.service.common.PictureService;
import com.igurman.gur_car_bot.service.common.VehicleService;
import com.igurman.gur_car_bot.util.ObjectMapperProvider;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.igurman.gur_car_bot.repository.specification.VehicleEntitySpecifications.auctionEqualsIn;
import static com.igurman.gur_car_bot.repository.specification.VehicleEntitySpecifications.engineEqualsIn;
import static com.igurman.gur_car_bot.repository.specification.VehicleEntitySpecifications.gradeLessOrEquals;
import static com.igurman.gur_car_bot.repository.specification.VehicleEntitySpecifications.gradeMoreOrEquals;
import static com.igurman.gur_car_bot.repository.specification.VehicleEntitySpecifications.makeEq;
import static com.igurman.gur_car_bot.repository.specification.VehicleEntitySpecifications.modelIn;
import static com.igurman.gur_car_bot.repository.specification.VehicleEntitySpecifications.odometerLessOrEquals;
import static com.igurman.gur_car_bot.repository.specification.VehicleEntitySpecifications.odometerMoreOrEquals;
import static com.igurman.gur_car_bot.repository.specification.VehicleEntitySpecifications.priceLessOrEquals;
import static com.igurman.gur_car_bot.repository.specification.VehicleEntitySpecifications.priceMoreOrEquals;
import static com.igurman.gur_car_bot.repository.specification.VehicleEntitySpecifications.yearLessOrEquals;
import static com.igurman.gur_car_bot.repository.specification.VehicleEntitySpecifications.yearMoreOrEquals;

@Slf4j
@Component
@RequiredArgsConstructor
public class VehicleProvider {
    @Getter
    private final VehicleService vehicleService;
    private final PictureService pictureService;
    private final FilterService filterService;
    private final ObjectMapperProvider objectMapper;

    public VehicleDetalizationDto findAllByFilter(Long userId, Pageable pageable) {
        VehicleDetalizationDto result = new VehicleDetalizationDto();

        // собрать фильтр
        Page<VehicleEntity> data = vehicleService.findAll(getSpecification(userId), pageable);
        result.setData(data);

        if (data.hasContent()) {
            log.info("*** Поиск нашёл: эл={}, стр={}, текущая={}",
                    data.getTotalElements(), data.getTotalPages(), data.getNumber());
            List<Integer> vehicleIds = data.getContent().stream()
                    .map(VehicleEntity::getId)
                    .toList();

            List<PictureEntity> pictureList = pictureService.findFirstByVehicleIds(vehicleIds);
            result.setFirstPictureMap(pictureList.stream()
                    .collect(Collectors.toMap(PictureEntity::getVehicleId, Function.identity())));
        }

        return result;
    }

    private Specification<VehicleEntity> getSpecification(Long userId) {
        FilterEntity filter = filterService.findFilterByUserId(userId).orElse(null);

        if (filter == null) {
            return null;
        }

        log.info("*** Фильтр для поиска: {}", objectMapper.toJson(filter));

        // сборка фильрации марка/модель
        List<Specification<VehicleEntity>> makeModelList = new ArrayList<>();

        if (!filter.getMakeModel().isEmpty()) {
                filter.getMakeModel().forEach((key, val) -> {
                    // если в val лежит сет с нулём, значит все модели
                    if (val.contains(0)) {
                        makeModelList.add(Specification
                                .where(makeEq(key)));
                    } else {
                        makeModelList.add(Specification
                                .where(makeEq(key))
                                .and(modelIn(val)));
                    }
                });
        }

        return Specification.where(VehicleEntitySpecifications.addCommon())
                .and(Specification.anyOf(makeModelList))
                .and(yearMoreOrEquals(filter.getYearStart()))
                .and(yearLessOrEquals(filter.getYearEnd()))
                .and(odometerMoreOrEquals(filter.getOdometerStart()))
                .and(odometerLessOrEquals(filter.getOdometerEnd()))
                .and(engineEqualsIn(getEngineSet(filter)))
                .and(priceMoreOrEquals(filter.getPriceStart()))
                .and(priceLessOrEquals(filter.getPriceEnd()))
                .and(gradeMoreOrEquals(filter.getGradeStart()))
                .and(gradeLessOrEquals(filter.getGradeEnd()))
                .and(auctionEqualsIn(Set.of(AuctionType.AUCTION_SPARK)));
    }

    private Set<String> getEngineSet(FilterEntity filter) {
        if (CollectionUtils.isEmpty(filter.getEngineTypes())) {
            return Set.of();
        }
        return filter.getEngineTypes().stream()
                .map(type -> EngineType.get(type).name())
                .collect(Collectors.toSet());
    }

    public VehicleEntity findById(Integer vehicleId) {
        return vehicleService.findById(vehicleId);
    }

    public List<PictureEntity> getPictureByVehicleId(Integer vehicleId) {
        return pictureService.findByVehicleId(vehicleId);
    }

}
