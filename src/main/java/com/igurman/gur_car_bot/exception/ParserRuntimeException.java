package com.igurman.gur_car_bot.exception;

public class ParserRuntimeException extends CommonException {
    private static final String NAME = "Ошибка при работе парсера";

    public ParserRuntimeException(Throwable cause) {
        super(NAME, cause);
    }

    public ParserRuntimeException(String message) {
        super(message);
    }

    public ParserRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

}
