package com.igurman.gur_car_bot.constant.resolver;

import com.igurman.gur_car_bot.constant.ColorType;
import lombok.experimental.UtilityClass;

import java.util.HashMap;
import java.util.Map;

/**
 * Определитель цвета авто
 * могут быть разные обозначения у разных поставщиков
 * приводит данные к единому типу
 */
@UtilityClass
public class ColorTypeResolver {
    private static final Map<String, ColorType> map = new HashMap<>();

    static {
        map.put("BLACK", ColorType.BLACK);
        map.put("BLUE", ColorType.BLUE);
        map.put("BURGUNDY", ColorType.BURGUNDY);
        map.put("GOLD", ColorType.GOLD);
        map.put("GRAY", ColorType.GRAY);
        map.put("GREEN", ColorType.GREEN);
        map.put("RED", ColorType.RED);
        map.put("SILVER", ColorType.SILVER);
        map.put("WHITE", ColorType.WHITE);
        map.put("TEAL", ColorType.TEAL);
        map.put("TAN", ColorType.TAN);
        map.put("ORANGE", ColorType.ORANGE);
    }

    public static ColorType getType(String code) {
        return map.getOrDefault(code, ColorType.UNKNOWN);
    }
}
