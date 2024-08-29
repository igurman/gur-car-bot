package com.igurman.gur_car_bot.service.telegram.handler;

import com.igurman.gur_car_bot.constant.I18nType;
import com.igurman.gur_car_bot.constant.LanguageType;
import com.igurman.gur_car_bot.constant.RouteType;
import com.igurman.gur_car_bot.dictionary.I18nDictionary;
import com.igurman.gur_car_bot.model.dto.UpdateDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

import static com.igurman.gur_car_bot.constant.RouteDictionary.getCallbackRoute;
import static com.igurman.gur_car_bot.dictionary.I18nDictionary.getI18n;

/**
 * Обработчик страницы "Настройки"
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SettingsHandler implements CommandHandler {

    @Override
    public boolean canExecute(UpdateDto updateDto) {
        return RouteType.SETTINGS_ROUTE.equals(updateDto.getRoute())
               || RouteType.SETTINGS_MENU_ROUTE.equals(updateDto.getRoute());
    }

    @Override
    public Object execute(UpdateDto updateDto) {
        log.info("*** SettingsHandler, chatId: {}, userName: {}", updateDto.getChatId(), updateDto.getFirstName());

        if (updateDto.getUpdate().hasMessage()) {
            return SendMessage.builder()
                    .chatId(updateDto.getChatId())
                    .text(I18nDictionary.getI18n(I18nType.SETTINGS_MESSAGE, updateDto.getLang()))
                    .replyToMessageId(updateDto.getMessageId())
                    .replyMarkup(getInlineKeyboard(updateDto.getLang()))
                    .build();
        } else {
            return EditMessageText.builder()
                    .chatId(updateDto.getChatId())
                    .text(I18nDictionary.getI18n(I18nType.SETTINGS_MESSAGE, updateDto.getLang()))
                    .messageId(updateDto.getMessageId())
                    .replyMarkup(getInlineKeyboard(updateDto.getLang()))
                    .build();
        }
    }

    private InlineKeyboardMarkup getInlineKeyboard(LanguageType lang) {

        InlineKeyboardButton settingBtn = InlineKeyboardButton.builder()
                .text(getI18n(I18nType.SETTINGS_LANGUAGE, lang, lang))
                .callbackData(getCallbackRoute(RouteType.SETTINGS_LANGUAGE_ROUTE))
                .build();

//        InlineKeyboardButton bt2 = InlineKeyboardButton.builder()
//                .text(i18n.get(I18nType.SETTINGS_CITY, updateDto.getLang(), "Sacramento"))
//                .callbackData(routeDictionary.getCallbackRoute(RouteType.SETTINGS_CITY_ROUTE))
//                .build();
//
//        InlineKeyboardButton bt3 = InlineKeyboardButton.builder()
//                .text(i18n.get(I18nType.SETTINGS_DISTANCE, updateDto.getLang(), 50))
//                .callbackData(routeDictionary.getCallbackRoute(RouteType.SETTINGS_DISTANCE_ROUTE))
//                .build();
//
//        InlineKeyboardButton bt4 = InlineKeyboardButton.builder()
//                .text(i18n.get(I18nType.SETTINGS_TAX, updateDto.getLang()))
//                .callbackData(routeDictionary.getCallbackRoute(RouteType.SETTINGS_TAX_ROUTE))
//                .build();

//        InlineKeyboardButton bt5 = InlineKeyboardButton.builder()
//                .text(i18n.get(I18nType.SETTINGS_MENU_CLOSE, updateDto.getLang()))
//                .callbackData("/close-menu")
//                .build();

        return InlineKeyboardMarkup.builder()
                .keyboard(
                        List.of(
                                List.of(settingBtn)
//                                List.of(bt2),
//                                List.of(bt3),
//                                List.of(bt4),
//                                List.of(bt5)
                        ))
                .build();
    }

}
