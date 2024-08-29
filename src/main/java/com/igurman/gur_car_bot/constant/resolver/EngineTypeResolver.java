package com.igurman.gur_car_bot.constant.resolver;

import com.igurman.gur_car_bot.constant.EngineType;
import lombok.experimental.UtilityClass;

import java.util.HashMap;
import java.util.Map;

/**
 * Определитель типа двигателя авто
 * могут быть разные обозначения у разных поставщиков
 * приводит данные к единому типу
 */
@UtilityClass
public class EngineTypeResolver {
    private static final Map<String, EngineType> map = new HashMap<>();

    static {
        map.put("Gasoline", EngineType.GAS);
        map.put("Diesel", EngineType.DIESEL);
        map.put("Flex Fuel", EngineType.FLEX);
        map.put("Electric", EngineType.ELECTRIC);

    }

    public static EngineType getType(String code) {
        return map.getOrDefault(code, EngineType.UNKNOWN);
    }
}
