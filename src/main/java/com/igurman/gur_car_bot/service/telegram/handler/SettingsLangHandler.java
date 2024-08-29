package com.igurman.gur_car_bot.service.telegram.handler;

import com.igurman.gur_car_bot.constant.I18nType;
import com.igurman.gur_car_bot.constant.LanguageType;
import com.igurman.gur_car_bot.dictionary.I18nDictionary;
import com.igurman.gur_car_bot.dictionary.LanguageDictionary;
import com.igurman.gur_car_bot.model.dto.UpdateDto;
import com.igurman.gur_car_bot.model.entity.UserEntity;
import com.igurman.gur_car_bot.service.common.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

import static com.igurman.gur_car_bot.constant.RouteDictionary.getCallbackRoute;
import static com.igurman.gur_car_bot.constant.RouteType.SETTINGS_LANGUAGE_ROUTE;
import static com.igurman.gur_car_bot.constant.RouteType.SETTINGS_ROUTE;
import static com.igurman.gur_car_bot.dictionary.I18nDictionary.getI18n;

/**
 * Обработчик страницы "Выбор языка"
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SettingsLangHandler implements CommandHandler {
    private final UserService userService;
    private final LanguageDictionary languageDictionary;
    private static final String LANG = "lang";
    private static final String LANG_RU_QUERY = "?lang=1";
    private static final String LANG_EN_QUERY = "?lang=2";
    private static final String LANG_ES_QUERY = "?lang=3";

    @Override
    public boolean canExecute(UpdateDto updateDto) {
        return SETTINGS_LANGUAGE_ROUTE.equals(updateDto.getRoute());
    }

    @Override
    public Object execute(UpdateDto updateDto) {
        log.info("*** LangChangeChoiceHandler, chatId: {}, userName: {}, messageId: {}", updateDto.getChatId(),
                updateDto.getFirstName(), updateDto.getMessageId());

        String textMessage = "";
        if (updateDto.getQueryParams().containsKey(LANG)) {
            LanguageType language = switch (updateDto.getQueryParams().get(LANG)) {
                case 2 -> LanguageType.EN;
                case 3 -> LanguageType.ES;
                default -> LanguageType.RU;
            };

            // меняем язык в базе
            UserEntity user = updateDto.getUser();
            user.setLanguage(language.code());
            UserEntity updateUser = userService.save(user);
            String newLang = languageDictionary.getValue(updateUser.getLanguage());
            textMessage = getI18n(I18nType.SETTINGS_LANGUAGE_CHANGE, updateDto.getLang(), newLang);
        } else {
            textMessage = getI18n(I18nType.SETTINGS_LANGUAGE_MESSAGE, updateDto.getLang(), updateDto.getLang());
        }

        return EditMessageText.builder()
                .chatId(updateDto.getChatId())
                .text(textMessage)
                .messageId(updateDto.getMessageId())
                .replyMarkup(getInlineKeyboard(updateDto.getLang()))
                .build();
    }

    private InlineKeyboardMarkup getInlineKeyboard(LanguageType lang) {

        InlineKeyboardButton langRuBtn = InlineKeyboardButton.builder()
                .text(I18nDictionary.LANGUAGE_RU)
                .callbackData(getCallbackRoute(SETTINGS_LANGUAGE_ROUTE) + LANG_RU_QUERY)
                .build();

        InlineKeyboardButton langEnBtn = InlineKeyboardButton.builder()
                .text(I18nDictionary.LANGUAGE_EN)
                .callbackData(getCallbackRoute(SETTINGS_LANGUAGE_ROUTE) + LANG_EN_QUERY)
                .build();

        InlineKeyboardButton langEsBtn = InlineKeyboardButton.builder()
                .text(I18nDictionary.LANGUAGE_ES)
                .callbackData(getCallbackRoute(SETTINGS_LANGUAGE_ROUTE) + LANG_ES_QUERY)
                .build();

        InlineKeyboardButton langBackBtn = InlineKeyboardButton.builder()
                .text(I18nDictionary.getI18n(I18nType.SETTINGS_LANGUAGE_BACK, lang))
                .callbackData(getCallbackRoute(SETTINGS_ROUTE))
                .build();

        return InlineKeyboardMarkup.builder()
                .keyboard(
                        List.of(
                                List.of(langRuBtn, langEnBtn, langEsBtn),
                                List.of(langBackBtn)
                        ))
                .build();
    }

}
