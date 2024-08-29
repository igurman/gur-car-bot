package com.igurman.gur_car_bot.service.telegram.handler;

import com.igurman.gur_car_bot.constant.I18nType;
import com.igurman.gur_car_bot.constant.RouteType;
import com.igurman.gur_car_bot.dictionary.I18nDictionary;
import com.igurman.gur_car_bot.model.dto.UpdateDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.List;

/**
 * Обработчик страницы "Помощь"
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class HelpHandler implements CommandHandler {

    @Override
    public boolean canExecute(UpdateDto updateDto) {
        return RouteType.HELP_ROUTE.equals(updateDto.getRoute())
               || RouteType.HELP_MENU_ROUTE.equals(updateDto.getRoute());
    }

    @Override
    public List<Object> executeList(UpdateDto updateDto) {
        Long chatId = updateDto.getChatId();

        log.info("*** HelpHandler, chatId: {}, userName: {}", chatId, updateDto.getFirstName());

        return List.of(
                SendMessage.builder()
                        .chatId(chatId)
                        .text(I18nDictionary.getI18n(I18nType.HELP_1_MESSAGE, updateDto.getLang()))
                        .build(),
                SendMessage.builder()
                        .chatId(chatId)
                        .text(I18nDictionary.getI18n(I18nType.HELP_2_MESSAGE, updateDto.getLang()))
                        .build(),
                SendMessage.builder()
                        .chatId(chatId)
                        .text(I18nDictionary.getI18n(I18nType.HELP_3_MESSAGE, updateDto.getLang()))
                        .build(),
                SendMessage.builder()
                        .chatId(chatId)
                        .text(I18nDictionary.getI18n(I18nType.HELP_4_MESSAGE, updateDto.getLang()))
                        .build()
        );
    }

}
