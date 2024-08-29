package com.igurman.gur_car_bot.service.telegram.handler;

import com.igurman.gur_car_bot.constant.I18nType;
import com.igurman.gur_car_bot.constant.RouteType;
import com.igurman.gur_car_bot.dictionary.I18nDictionary;
import com.igurman.gur_car_bot.model.dto.UpdateDto;
import com.igurman.gur_car_bot.service.common.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

/**
 * Обработчик страницы "Написать администратору"
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class WriteToAdminHandler implements CommandHandler {
    private final UserService userService;

    @Override
    public boolean canExecute(UpdateDto updateDto) {
        return RouteType.WRITE_TO_ADMIN_ROUTE.equals(updateDto.getRoute());
    }

    @Override
    public Object execute(UpdateDto updateDto) {
        log.info("*** WriteAdminHandler, chatId: {}, userName: {}, messageId: {}", updateDto.getChatId(),
                updateDto.getFirstName(), updateDto.getMessageId());

        userService.turnWrite(updateDto.getUser().getId());

        return EditMessageText.builder()
                .chatId(updateDto.getChatId())
                .messageId(updateDto.getMessageId())
                .text(I18nDictionary.getI18n(I18nType.WRITE_TO_ADMIN_MESSAGE, updateDto.getLang()))
                .build();
    }

}
