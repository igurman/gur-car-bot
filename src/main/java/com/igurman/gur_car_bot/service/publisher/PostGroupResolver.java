package com.igurman.gur_car_bot.service.publisher;

import com.igurman.gur_car_bot.constant.EngineType;
import com.igurman.gur_car_bot.model.entity.VehicleEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class PostGroupResolver {
    @Value("${application.telegram.group.theme.all-id}")
    private Integer allId;
    @Value("${application.telegram.group.theme.japan-id}")
    private Integer japanId;
    @Value("${application.telegram.group.theme.america-id}")
    private Integer americaId;
    @Value("${application.telegram.group.theme.premium-id}")
    private Integer premiumId;
    @Value("${application.telegram.group.theme.hybrids-id}")
    private Integer hybridsId;
    @Value("${application.telegram.group.theme.germany-id}")
    private Integer germanyId;
    @Value("${application.telegram.group.theme.electric-id}")
    private Integer electricId;
    @Value("${application.telegram.group.theme.before-6-id}")
    private Integer before6Id;

    private static final List<String> JAPAN = List.of("Toyota", "Lexus", "Acura", "Honda", "Infinity", "Mazda", "Nissan", "Subaru", "MITSUBISHI", "DAIHATSU", "SCION");
    private static final List<String> AMERICA = List.of("Buick", "Cadillac", "Chevrolet", "Chrysler", "CORVETTE", "Dodge", "Ford", "Jeep", "Lincoln");
    private static final List<String> GERMANY = List.of("Audi", "BMW", "Mercedes-Benz", "MINI", "PORSCHE", "Volkswagen");
    private static final List<String> PREMIUM = List.of("Acura", "ALFA ROMEO", "Aston Martin", "Audi", "BMW", "BENTLEY", "FERRARI", "FISKER", "Infinity", "JAGUAR", "LAMBORGINI", "Land Rover", "Lexus", "MASERATI", "MAYBACH", "MCLAREN", "Mercedes-Benz", "PORSCHE", "RIVIAN", "ROLLS-ROYCE", "Tesla");

    public List<Integer> resolve(VehicleEntity vehicleEntity) {
        List<Integer> result = new ArrayList<>();

        result.add(allId);

        if(JAPAN.contains(vehicleEntity.getMakeTitle())) {
            result.add(japanId);
        }

        if(AMERICA.contains(vehicleEntity.getMakeTitle())) {
            result.add(americaId);
        }

        if(GERMANY.contains(vehicleEntity.getMakeTitle())) {
            result.add(germanyId);
        }

        if(PREMIUM.contains(vehicleEntity.getMakeTitle())) {
            result.add(premiumId);
        }

        if(EngineType.HYBRID.equals(vehicleEntity.getEngineType())) {
            result.add(hybridsId);
        }

        if(EngineType.ELECTRIC.equals(vehicleEntity.getEngineType())) {
            result.add(electricId);
        }

        if (vehicleEntity.getPrice() != null && vehicleEntity.getPrice() <= 6000) {
            result.add(before6Id);
        }

        return result;
    }
}
