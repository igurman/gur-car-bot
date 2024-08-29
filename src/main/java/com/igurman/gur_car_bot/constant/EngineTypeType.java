package com.igurman.gur_car_bot.constant;

public enum EngineTypeType {
    GAS("1"),
    HYBRID("2"),
    ELECTRIC("3"),
    PLUG_IN("4"),
    DIESEL("5"),
    FLEX("6"),
    HYDROGEN("7");

    private String code;

    EngineTypeType(String code) {
        this.code = code;
    }

    public String code() {
        return code;
    }
}
