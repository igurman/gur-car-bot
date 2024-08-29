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
 * Обработчик действия "Отменить Задать вопрос"
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class QuestionCancelHandler implements CommandHandler {
    private final UserService userService;

    @Override
    public boolean canExecute(UpdateDto updateDto) {
        return RouteType.QUESTION_CANCEL_ROUTE.equals(updateDto.getRoute());
    }

    @Override
    public Object execute(UpdateDto updateDto) {
        log.info("*** QuestionCancelHandler, chatId: {}, userName: {}, messageId: {}", updateDto.getChatId(),
                updateDto.getFirstName(), updateDto.getMessageId());

        userService.turnRead(updateDto.getUser().getId());

        return EditMessageText.builder()
                .chatId(updateDto.getChatId())
                .messageId(updateDto.getMessageId())
                .text(I18nDictionary.getI18n(I18nType.QUESTION_CANCEL_MESSAGE, updateDto.getLang()))
                .build();
    }
}
