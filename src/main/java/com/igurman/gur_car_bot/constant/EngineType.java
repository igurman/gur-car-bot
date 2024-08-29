package com.igurman.gur_car_bot.constant;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public enum EngineType {
    GAS(1),
    HYBRID(2),
    ELECTRIC(3),
    PLUG_IN(4),
    DIESEL(5),
    FLEX(6),
    HYDROGEN(7),
    UNKNOWN(8);

    private Integer code;

    EngineType(Integer code) {
        this.code = code;
    }

    public Integer code() {
        return code;
    }

    public static EngineType get(Integer code) {
        return Arrays.stream(values()).filter(a -> Objects.equals(a.code, code)).findFirst().orElse(null);
    }

    public static List<EngineType> getList() {
        return List.of(values());
    }
}
