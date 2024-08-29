package com.igurman.gur_car_bot.constant.resolver;

import com.igurman.gur_car_bot.constant.DriveTrainType;
import lombok.experimental.UtilityClass;

import java.util.HashMap;
import java.util.Map;

/**
 * Определитель привода авто
 * могут быть разные обозначения у разных поставщиков
 * приводит данные к единому типу
 */
@UtilityClass
public class DriveTrainTypeResolver {
    private static final Map<String, DriveTrainType> map = new HashMap<>();

    static {
        map.put("4WD", DriveTrainType.FOUR_WD);
        map.put("RWD", DriveTrainType.RWD);
        map.put("FWD", DriveTrainType.FWD);
        map.put("AWD", DriveTrainType.AWD);
    }

    public static DriveTrainType getType(String code) {
        return map.getOrDefault(code, DriveTrainType.UNKNOWN);
    }
}
