package com.igurman.gur_car_bot.util;

import lombok.experimental.UtilityClass;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

@UtilityClass
public class UsefulUtil {

    public static int calcFreeze(TelegramApiRequestException ex) {
        if (ex.getErrorCode() == 429) {
            return ex.getParameters().getRetryAfter() * 1000;
        }
        return 0;
    }
}
