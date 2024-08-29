package com.igurman.gur_car_bot.service.telegram.handler;

import com.igurman.gur_car_bot.constant.I18nType;
import com.igurman.gur_car_bot.constant.LanguageType;
import com.igurman.gur_car_bot.constant.MessageType;
import com.igurman.gur_car_bot.constant.RouteType;
import com.igurman.gur_car_bot.model.dto.UpdateDto;
import com.igurman.gur_car_bot.service.common.UserService;
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
 * Обработчик страницы "Задать вопрос"
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class QuestionHandler implements CommandHandler {
    private final UserService userService;

    @Override
    public boolean canExecute(UpdateDto updateDto) {
        return RouteType.QUESTION_ROUTE.equals(updateDto.getRoute())
               || RouteType.QUESTION_MENU_ROUTE.equals(updateDto.getRoute());
    }

    @Override
    public Object execute(UpdateDto updateDto) {
        userService.turnWrite(updateDto.getUser().getId());

        if (MessageType.MESSAGE.equals(updateDto.getMessageType())) {
            log.info("*** QuestionHandler, chatId: {}, userName: {}", updateDto.getChatId(), updateDto.getFirstName());

            return SendMessage.builder()
                    .chatId(updateDto.getChatId())
                    .replyToMessageId(updateDto.getMessageId())
                    .text(getI18n(I18nType.QUESTION_MESSAGE, updateDto.getLang()))
                    .replyMarkup(getInlineKeyboard(updateDto.getLang()))
                    .build();
        } else if (MessageType.CALLBACK_QUERY.equals(updateDto.getMessageType())) {
            log.info("*** QuestionHandler, chatId: {}, userName: {}, messageId: {}", updateDto.getChatId(),
                    updateDto.getFirstName(), updateDto.getMessageId());

            return EditMessageText.builder()
                    .chatId(updateDto.getChatId())
                    .messageId(updateDto.getMessageId())
                    .text(getI18n(I18nType.QUESTION_MESSAGE, updateDto.getLang()))
                    .replyMarkup(getInlineKeyboard(updateDto.getLang()))
                    .build();
        }
        return null;
    }

    private InlineKeyboardMarkup getInlineKeyboard(LanguageType lang) {
        InlineKeyboardButton cancelBtn = InlineKeyboardButton.builder()
                .text(getI18n(I18nType.WRITE_CANCEL, lang))
                .callbackData(getCallbackRoute(RouteType.QUESTION_CANCEL_ROUTE))
                .build();

        return InlineKeyboardMarkup.builder()
                .keyboard(List.of(List.of(cancelBtn)))
                .build();
    }

}
