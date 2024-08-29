package com.igurman.gur_car_bot.service.telegram.handler;

import com.igurman.gur_car_bot.constant.I18nType;
import com.igurman.gur_car_bot.constant.RouteType;
import com.igurman.gur_car_bot.dictionary.I18nDictionary;
import com.igurman.gur_car_bot.model.dto.UpdateDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

/**
 * Обработчик страницы "Контакты"
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ContactHandler implements CommandHandler {

    @Override
    public boolean canExecute(UpdateDto updateDto) {
        return RouteType.CONTACT_ROUTE.equals(updateDto.getRoute())
               || RouteType.CONTACT_MENU_ROUTE.equals(updateDto.getRoute());
    }

    @Override
    public Object execute(UpdateDto updateDto) {
        log.info("*** ContactHandler, chatId: {}, userName: {}", updateDto.getChatId(), updateDto.getFirstName());

        return SendMessage.builder()
                .chatId(updateDto.getChatId())
                .text(I18nDictionary.getI18n(I18nType.CONTACT_MESSAGE, updateDto.getLang()))
                .build();
    }

}
