package com.igurman.gur_car_bot.util;

import com.igurman.gur_car_bot.constant.MessageType;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.HashMap;
import java.util.Map;

@UtilityClass
public class UpdateUtil {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(UpdateUtil.class);

    /**
     * Вытягиваем параметры с типом Integer из строки URL
     */
    public static Map<String, Integer> getQueryParams(Update update) {
        Map<String, Integer> map = new HashMap<>();

        String url = getMessage(update);
        if (url == null) {
            return map;
        }

        // удалим часть адреса не относящуюся к параметрам, вместе с ?
        if (url.contains("?")
            && url.length() > (url.indexOf("?") + 1)) {

            String substring = url.substring(url.indexOf("?") + 1);

            if (substring.contains("?")) {
                log.error("*** После очистки строка содержит ещё один '?', некорректная строка");
                return map;
            }
            // делим строку на части для получения параметров
            String[] split = substring.split("&");
            for (String s : split) {
                String[] pair = s.split("=");
                if (pair.length > 1) {
                    map.put(pair[0], Integer.parseInt(pair[1]));
                }
            }
        }
        return map;
    }

    public static String getPath(Update update) {
        //todo: надо подумать как отделять строку урла от простого сообщения, например если пользователь что то написал
        if (isMessage(update)) {
            return cleanUrlFromString(update.getMessage().getText());
        } else if (isCallbackQuery(update)) {
            return cleanUrlFromString(update.getCallbackQuery().getData());
        }
        return null;
    }

    /**
     * удалить первую часть URL из строки с параметрами
     */
    public static String cleanUrlFromString(String url) {
        if (url.contains("?")
            && url.length() > (url.indexOf("?") + 1)) {
            return url.substring(0, url.indexOf("?"));
        } else {
            return url;
        }
    }

    public static String getMessage(Update update) {
        if (isMessage(update)) {
            return update.getMessage().getText();
        } else if (isCallbackQuery(update)) {
            return update.getCallbackQuery().getData();
        }
        return null;
    }

    public static User getUserFrom(Update update) {
        return MessageType.MESSAGE.equals(getMessageType(update))
                ? update.getMessage().getFrom()
                : update.getCallbackQuery().getFrom();
    }

    /**
     * получить id пользователя в зависимости от типа сообщения
     */
    public static Long getUserId(Update update) {
        if (update.hasMessage()
            && update.getMessage().getFrom() != null) {
            return update.getMessage().getFrom().getId();
        } else if (update.hasCallbackQuery()
                   && update.getCallbackQuery().getFrom() != null) {
            return update.getCallbackQuery().getFrom().getId();
        }
        return 0L;
    }

    /**
     * получить тип сообщения
     */
    public static MessageType getMessageType(Update update) {
        if (isMessage(update)) {
            return MessageType.MESSAGE;
        } else if (isCallbackQuery(update)) {
            return MessageType.CALLBACK_QUERY;
        }
        return MessageType.NONE;
    }

    public static boolean isMessage(Update update) {
        return update.hasMessage()
               && update.getMessage().hasText();
    }

    public static boolean isCallbackQuery(Update update) {
        return update.hasCallbackQuery()
               && StringUtils.isNotEmpty(update.getCallbackQuery().getData());
    }
}
