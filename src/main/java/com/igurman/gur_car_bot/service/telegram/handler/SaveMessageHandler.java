package com.igurman.gur_car_bot.service.telegram.handler;

import com.igurman.gur_car_bot.constant.I18nType;
import com.igurman.gur_car_bot.constant.LanguageType;
import com.igurman.gur_car_bot.constant.MessageType;
import com.igurman.gur_car_bot.constant.RouteType;
import com.igurman.gur_car_bot.constant.StatusUserType;
import com.igurman.gur_car_bot.model.dto.UpdateDto;
import com.igurman.gur_car_bot.service.common.MessageService;
import com.igurman.gur_car_bot.service.common.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

import static com.igurman.gur_car_bot.constant.RouteDictionary.getCallbackRoute;
import static com.igurman.gur_car_bot.dictionary.I18nDictionary.getI18n;

/**
 * Обработчик действия "Сохранить сообщение в БД"
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SaveMessageHandler implements CommandHandler {
    private final MessageService messageService;
    private final UserService userService;


    @Override
    public boolean canExecute(UpdateDto updateDto) {
        return StatusUserType.WRITE.equals(updateDto.getUser().getStatus())
               && MessageType.MESSAGE.equals(updateDto.getMessageType())
               && updateDto.getUpdate().getMessage().getText() != null;
    }

    @Override
    public Object execute(UpdateDto updateDto) {
        log.info("*** SaveMessageHandler, chatId: {}, userName: {}, messageId: {}", updateDto.getChatId(),
                updateDto.getFirstName(), updateDto.getMessageId());

        messageService.saveNewMessage(updateDto.getUser().getId(), updateDto.getUpdate().getMessage().getText());
        userService.turnRead(updateDto.getUser().getId());
        updateDto.getUser().setStatus(StatusUserType.READ);

        return SendMessage.builder()
                .chatId(updateDto.getChatId())
                .text(getI18n(I18nType.SAVE_MESSAGE, updateDto.getLang()))
                .replyMarkup(getInlineKeyboard(updateDto.getLang()))
                .build();
    }

    private InlineKeyboardMarkup getInlineKeyboard(LanguageType lang) {

        InlineKeyboardButton menuBtn = InlineKeyboardButton.builder()
                .text(getI18n(I18nType.MENU, lang))
                .callbackData(getCallbackRoute(RouteType.MENU_ROUTE))
                .build();

        return InlineKeyboardMarkup.builder()
                .keyboard(
                        List.of(
                                List.of(menuBtn)
                        ))
                .build();
    }

}
