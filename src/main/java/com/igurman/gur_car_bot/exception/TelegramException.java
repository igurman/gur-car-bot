package com.igurman.gur_car_bot.exception;

public class TelegramException extends CommonException{
    private static final String NAME = "Ошибка при работе телеграм";

    public TelegramException(Throwable cause) {
        super(NAME, cause);
    }

    public TelegramException(String message) {
        super(message);
    }

    public TelegramException(String message, Throwable cause) {
        super(message, cause);
    }

}
