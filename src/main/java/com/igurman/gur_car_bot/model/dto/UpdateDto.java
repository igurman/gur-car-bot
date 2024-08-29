package com.igurman.gur_car_bot.model.dto;

import com.igurman.gur_car_bot.constant.LanguageType;
import com.igurman.gur_car_bot.constant.MessageType;
import com.igurman.gur_car_bot.constant.RouteType;
import com.igurman.gur_car_bot.model.entity.FilterEntity;
import com.igurman.gur_car_bot.model.entity.UserEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Map;

import static com.igurman.gur_car_bot.dictionary.I18nDictionary.EMPTY;

/**
 * Модель для работы бота
 * при запросе собираем сюда всю необходимую информацию и передаём в handler
 */
@Getter
@Setter
@Builder
public class UpdateDto {

    // сообщение
    @NonNull
    private Update update;

    // id чата
    @NonNull
    private Long chatId;

    // id сообщения
    private Integer messageId;

    // модель пользователя из БД
    @NonNull
    private UserEntity user;

    // имя пользователя
    private String firstName;

    // язык локализации пользователя
    private LanguageType lang;

    // фильтры пользователя
    @NonNull
    private FilterEntity filter;

    // query параметры из пути, если есть
    @NonNull
    private Map<String, Integer> queryParams;

    // тип сообщения
    @NonNull
    private MessageType messageType;

    // путь в роуте, может быть не найден
    private String path;

    // роут, может быть не найден
    private RouteType route;

    public void execSettingPatch() {
        this.doSettingLang();
        this.doSettingUserName();
    }

    private void doSettingLang() {
        this.lang = StringUtils.isNotEmpty(user.getLanguage())
                ? LanguageType.resolve(user.getLanguage())
                : LanguageType.RU;
    }

    private void doSettingUserName() {
        this.firstName = StringUtils.isNotEmpty(user.getFirstName())
                ? user.getFirstName()
                : EMPTY;
    }

}
