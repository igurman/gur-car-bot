package com.igurman.gur_car_bot.service.publisher;

import com.igurman.gur_car_bot.constant.PosterStatusType;
import com.igurman.gur_car_bot.constant.PosterUserType;
import com.igurman.gur_car_bot.model.entity.PostEntity;
import com.igurman.gur_car_bot.model.entity.VehicleEntity;
import com.igurman.gur_car_bot.service.common.PostService;
import com.igurman.gur_car_bot.service.common.VehicleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class SeedTableGroupService {
    private final PostGroupResolver postGroupResolver;
    private final PostService postService;
    private final VehicleService vehicleService;
    @Value("${application.telegram.group.seed-table.days-before}")
    private Integer daysBefore;
    @Value("${application.telegram.group.seed-table.count-vehicles}")
    private Integer countVehicle;
    @Value("${application.telegram.group.seed-table.delete-after-days}")
    private Integer deleteAfterDays;
    @Value("${application.telegram.group.group-id}")
    private Long groupId;

    public void execute() {
        log.info("*** Запуск популятора таблицы для группы ТГ");

        List<VehicleEntity> vehicleServiceList = vehicleService.findForPublisher(daysBefore, countVehicle);
        log.info("*** Получили {} авто для расчёта популяции таблицы для группы ТГ", vehicleServiceList.size());

        if (vehicleServiceList.isEmpty()) {
            return;
        }

        for (VehicleEntity vehicleEntity : vehicleServiceList) {

            if (postService.existsByVehicleIdAndUserType(vehicleEntity.getId())) {
                continue;
            }

            List<Integer> channelList = postGroupResolver.resolve(vehicleEntity);

            if (!channelList.isEmpty()) {
                for (Integer channel : channelList) {
                    PostEntity postEntity = PostEntity.builder()
                            .vehicleId(vehicleEntity.getId())
                            .groupId(groupId)
                            .groupChannelId(channel)
                            .userType(PosterUserType.GROUP)
                            .status(PosterStatusType.CREATED)
                            .deleteDate(vehicleEntity.getSaleDate() != null ? vehicleEntity.getSaleDate().plusDays(deleteAfterDays) : null)
                            .build();
                    postService.save(postEntity);
                }
            }
        }
        log.info("*** Закончили цикл популяции таблицы для группы ТГ");
    }
}
