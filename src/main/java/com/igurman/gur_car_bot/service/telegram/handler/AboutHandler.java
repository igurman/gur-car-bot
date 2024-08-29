package com.igurman.gur_car_bot.service.telegram.handler;

import com.igurman.gur_car_bot.constant.I18nType;
import com.igurman.gur_car_bot.constant.RouteType;
import com.igurman.gur_car_bot.model.dto.UpdateDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

import static com.igurman.gur_car_bot.constant.I18nType.ABOUT_FACEBOOK_TITLE;
import static com.igurman.gur_car_bot.constant.I18nType.ABOUT_FACEBOOK_URL;
import static com.igurman.gur_car_bot.constant.I18nType.ABOUT_FEEDBACK_TITLE;
import static com.igurman.gur_car_bot.constant.I18nType.ABOUT_FEEDBACK_URL;
import static com.igurman.gur_car_bot.constant.I18nType.ABOUT_INSTAGRAM_TITLE;
import static com.igurman.gur_car_bot.constant.I18nType.ABOUT_INSTAGRAM_URL;
import static com.igurman.gur_car_bot.constant.I18nType.ABOUT_TELEGRAM_TITLE;
import static com.igurman.gur_car_bot.constant.I18nType.ABOUT_TELEGRAM_URL;
import static com.igurman.gur_car_bot.constant.I18nType.ABOUT_YOUTUBE_TITLE;
import static com.igurman.gur_car_bot.constant.I18nType.ABOUT_YOUTUBE_URL;
import static com.igurman.gur_car_bot.dictionary.I18nDictionary.getI18n;

/**
 * Обработчик страницы "О нас"
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AboutHandler implements CommandHandler {

    @Override
    public boolean canExecute(UpdateDto updateDto) {
        return RouteType.ABOUT_ROUTE.equals(updateDto.getRoute())
               || RouteType.ABOUT_MENU_ROUTE.equals(updateDto.getRoute());
    }

    @Override
    public Object execute(UpdateDto updateDto) {
        log.info("*** AboutHandler chatId: {}, userName: {}", updateDto.getChatId(), updateDto.getFirstName());
        return SendMessage.builder()
                .chatId(updateDto.getChatId())
                .text(getI18n(I18nType.ABOUT_MESSAGE, updateDto.getLang()))
                .replyMarkup(getInlineKeyboard(updateDto))
                .build();
    }

    private InlineKeyboardMarkup getInlineKeyboard(UpdateDto updateDto) {

        InlineKeyboardButton telegramBtn = InlineKeyboardButton.builder()
                .text(getI18n(ABOUT_TELEGRAM_TITLE, updateDto.getLang()))
                .url(getI18n(ABOUT_TELEGRAM_URL, updateDto.getLang()))
                .build();

        InlineKeyboardButton youtubeBtn = InlineKeyboardButton.builder()
                .text(getI18n(ABOUT_YOUTUBE_TITLE, updateDto.getLang()))
                .url(getI18n(ABOUT_YOUTUBE_URL, updateDto.getLang()))
                .build();

        InlineKeyboardButton instagramBtn = InlineKeyboardButton.builder()
                .text(getI18n(ABOUT_INSTAGRAM_TITLE, updateDto.getLang()))
                .url(getI18n(ABOUT_INSTAGRAM_URL, updateDto.getLang()))
                .build();

        InlineKeyboardButton facebookBtn = InlineKeyboardButton.builder()
                .text(getI18n(ABOUT_FACEBOOK_TITLE, updateDto.getLang()))
                .url(getI18n(ABOUT_FACEBOOK_URL, updateDto.getLang()))
                .build();

        InlineKeyboardButton feedbackBtn = InlineKeyboardButton.builder()
                .text(getI18n(ABOUT_FEEDBACK_TITLE, updateDto.getLang()))
                .url(getI18n(ABOUT_FEEDBACK_URL, updateDto.getLang()))
                .build();

        return InlineKeyboardMarkup.builder()
                .keyboard(
                        List.of(
                                List.of(telegramBtn),
                                List.of(youtubeBtn, instagramBtn, facebookBtn),
                                List.of(feedbackBtn)
                        ))
                .build();
    }

}
