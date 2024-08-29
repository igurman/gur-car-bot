package com.igurman.gur_car_bot.service.telegram.handler;

import com.igurman.gur_car_bot.constant.EngineTypeType;
import com.igurman.gur_car_bot.constant.RouteType;
import com.igurman.gur_car_bot.model.dto.UpdateDto;
import com.igurman.gur_car_bot.model.entity.VehicleEntity;
import com.igurman.gur_car_bot.model.entity.VinMakeEntity;
import com.igurman.gur_car_bot.model.entity.VinModelEntity;
import com.igurman.gur_car_bot.repository.VehicleRepository;
import com.igurman.gur_car_bot.repository.VinMakeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Обработчик страницы "Миграция"
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class MigrateHandler implements CommandHandler {
    private final VinMakeRepository vinMakeRepository;
    private final VehicleRepository vehicleRepository;

    @Override
    public boolean canExecute(UpdateDto updateDto) {
//        return false;
        return RouteType.MIGRATE_ROUTE.equals(updateDto.getRoute());
    }

    @Override
    public Object execute(UpdateDto updateDto) {
//        migrateMakeModel(updateDto);
//        migrateEngineType(updateDto);
        return null;
    }

    private void migrateEngineType(UpdateDto updateDto) {
        List<VehicleEntity> vehicleEntityList = vehicleRepository.findAllByEngineTypeIdIsNull();


        for (VehicleEntity vehicleEntity : vehicleEntityList) {
            String engine = vehicleEntity.getEngine();
            log.info("*** Получили {}, {}", vehicleEntity.getId(), engine);




                if (engine.contains("HYDROGEN")) {
                    vehicleEntity.setEngineTypeId(Integer.parseInt(EngineTypeType.HYDROGEN.code()));
                    vehicleRepository.save(vehicleEntity);
                }


        }
    }




    private void migrateMakeModel(UpdateDto updateDto){
        List<Integer> listId = List.of(7511, 412, 415, 427, 649, 1123, 1126, 1128, 1129, 1130, 1131, 1132, 1134, 1290, 1315, 1334, 1340, 1355, 1357, 1358, 2296, 2303, 2315, 2332, 2881, 2882, 2883, 2884, 2901, 2902, 2903, 2904, 3182, 3696, 3699, 3707, 3725, 4216, 4236, 4253, 4524, 4530, 4719, 4741, 4752, 4859, 4943, 6228, 6230, 6675, 6676, 6678, 6724, 6813, 6814, 6884, 6923, 6952, 6991, 7029, 7153, 7211, 7221, 7251, 7256, 7282, 7291, 7311, 7340, 7341, 7360, 7393, 7411, 7417, 7418, 7419, 7420, 7421, 7422, 7423, 7424, 7425, 7426, 7427, 7428, 7430, 7431, 7432, 7434, 7435, 7467, 7524, 7616, 7632, 7640, 7645, 7670, 7671, 7697, 7724, 7733, 7740, 7903, 7925, 8033, 8045, 8089, 8097, 8105, 8108, 8116, 8220, 8225, 8232, 8238, 8322, 8323, 8324, 8328, 8329, 8330, 8361, 8396, 8397, 8543, 8544, 420, 1348, 2880, 3727, 6221, 7433, 8043);

        List<VehicleEntity> vehicleEntityList = vehicleRepository.findAllById(listId);


        for (VehicleEntity vehicleEntity : vehicleEntityList) {

            VinMakeEntity vin = vinMakeRepository.findByNameIgnoreCase(vehicleEntity.getMakeTitle());
            if (vin != null) {
                Set<VinModelEntity> models = vin.getModels();
                if (models != null) {
                    List<VinModelEntity> vOks = new ArrayList<>();
                    // перебираем все модели из ВИН
                    for (VinModelEntity model : models) {
                        // пробуем найти все вхождения в начало
                        boolean b = vehicleEntity.getModelTitle().toUpperCase().startsWith(model.getName().toUpperCase());
                        if (b) {
                            vOks.add(model);
                        }
                    }

                    if (!vOks.isEmpty()) {

                        VinModelEntity vOk = null;
                        int t = 0;
                        List<Integer> sss = new ArrayList<>();
                        for (VinModelEntity vvvv : vOks) {
                            int length = vvvv.getName().length();
                            sss.add(length);


                            if (length > t) {
                                t = length;
                                vOk = vvvv;
                            }
                        }

                        List<Integer> sorted = sss.stream()
                                .sorted(Comparator.reverseOrder())
                                .collect(Collectors.toList());

                        if (sorted.get(0).compareTo(sorted.get(1)) == 0) {
                            log.error("=== больше 2х одинаковых совпадений: {}", vehicleEntity.getId());
                            return;
                        }


                        vehicleEntity.setMakeId(vin.getId());
                        vehicleEntity.setMakeTitle(vin.getName());
                        vehicleEntity.setModelId(vOk.getId());
                        vehicleEntity.setModelTitle(vOk.getName());
                        vehicleEntity.setComplect(vehicleEntity.getModelTitle().substring(vOk.getName().length(), vehicleEntity.getModelTitle().length()).trim());
                        vehicleRepository.save(vehicleEntity);

                    } else {
                        log.info("=== на нашли ни одного вхождения в начало для: {}", vehicleEntity.getModelTitle());
                    }
                } else {
                    log.info("=== на нашли ни одной модели в vinModels для: {}", vehicleEntity.getMakeTitle());
                }
            } else {
                log.info("=== на нашли ни одной марки findByName: {}", vehicleEntity.getMakeTitle());
            }


        }
    }


}
