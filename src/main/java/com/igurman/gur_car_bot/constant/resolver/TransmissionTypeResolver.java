package com.igurman.gur_car_bot.constant.resolver;

import com.igurman.gur_car_bot.constant.TransmissionType;
import lombok.experimental.UtilityClass;

import java.util.HashMap;
import java.util.Map;

/**
 * Определитель типа трансмиссии авто
 * могут быть разные обозначения у разных поставщиков
 * приводит данные к единому типу
 */
@UtilityClass
public class TransmissionTypeResolver {
    private static final Map<String, TransmissionType> map = new HashMap<>();

    static {
        map.put("Automatic", TransmissionType.AUTOMATIC);
        map.put("Manual", TransmissionType.MANUAL);
        map.put("5-speed Manual", TransmissionType.MANUAL);
        map.put("6-speed Manual", TransmissionType.MANUAL);
        map.put("Steptronic", TransmissionType.STEPTRONIC);
    }

    public static TransmissionType getType(String code) {
        return map.getOrDefault(code, TransmissionType.UNKNOWN);
    }
}
