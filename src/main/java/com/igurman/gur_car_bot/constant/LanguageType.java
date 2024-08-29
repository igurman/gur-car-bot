package com.igurman.gur_car_bot.constant;

import java.util.stream.Stream;

public enum LanguageType {
    EN("en"),
    RU("ru"),
    ES("es");

    private final String code;

    LanguageType(String code) {
        this.code = code;
    }

    public String code() {
        return code;
    }

    public boolean equals(String code) {
        return this.code.equals(code);
    }

    public static boolean isCodeExists(String code) {
        return Stream.of(values())
                .anyMatch(reason -> reason.code.equals(code));
    }

    public static boolean isCodeNotExists(String code) {
        return !isCodeExists(code);
    }

    public static LanguageType resolve(String code) {
        return Stream.of(LanguageType.values())
                .filter(item -> item.code.equals(code))
                .findFirst()
                .orElse(LanguageType.RU);
    }
}
