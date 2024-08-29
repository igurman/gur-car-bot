package com.igurman.gur_car_bot.exception;

public class CommonException extends RuntimeException{
    private static final String NAME = "Общая ошибка работы сервиса";

    public CommonException(Throwable cause) {
        super(NAME, cause);
    }

    public CommonException(String message) {
        super(message);
    }

    CommonException(String message, Throwable cause) {
        super(message, cause);
    }

}
