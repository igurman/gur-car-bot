package com.igurman.gur_car_bot.util.button;

import com.igurman.gur_car_bot.constant.I18nType;
import com.igurman.gur_car_bot.constant.LanguageType;
import com.igurman.gur_car_bot.dictionary.I18nDictionary;
import lombok.experimental.UtilityClass;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

import static com.igurman.gur_car_bot.constant.I18nType.ABOUT_TELEGRAM_URL;
import static com.igurman.gur_car_bot.constant.I18nType.SEARCH_ORDER_CARFAX;

@UtilityClass
public class StubCallback {

    public static AnswerCallbackQuery answerStop(String callbackQueryId) {
        return AnswerCallbackQuery.builder()
                .callbackQueryId(callbackQueryId)
                .build();
    }

    public static InlineKeyboardMarkup getInlineKeyboardForItem(LanguageType lang) {

        InlineKeyboardButton order = InlineKeyboardButton.builder()
                .text(I18nDictionary.getI18n(I18nType.SEARCH_ORDER, lang))
                .url(I18nDictionary.getI18n(ABOUT_TELEGRAM_URL, lang))
                .build();

        InlineKeyboardButton orderCarfax = InlineKeyboardButton.builder()
                .text(I18nDictionary.getI18n(SEARCH_ORDER_CARFAX, lang))
                .url(I18nDictionary.getI18n(ABOUT_TELEGRAM_URL, lang))
                .build();

        return InlineKeyboardMarkup.builder()
                .keyboard(List.of(
                        List.of(order, orderCarfax)
                ))
                .build();
    }
}
