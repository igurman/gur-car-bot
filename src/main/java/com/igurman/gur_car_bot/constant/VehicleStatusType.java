package com.igurman.gur_car_bot.constant;

import lombok.Getter;

@Getter
public enum VehicleStatusType {
    /**
     * Ожидание загрузки
     */
    AWAIT_LOAD,
    /**
     * Ожидание расчёта
     */
    AWAIT_CALC,
    /**
     * Ошибка парсинга
     */
    FAIL_PARSE,
    /**
     * Готов для загрузки в бот
     */
    WEB,
    /**
     * Загружен в бот
     */
    POSTED
}
