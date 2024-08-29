package com.igurman.gur_car_bot.service.telegram.handler;

import com.igurman.gur_car_bot.constant.I18nType;
import com.igurman.gur_car_bot.constant.MessageType;
import com.igurman.gur_car_bot.constant.RouteType;
import com.igurman.gur_car_bot.model.dto.UpdateDto;
import com.igurman.gur_car_bot.util.button.StubCallback;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

import static com.igurman.gur_car_bot.dictionary.I18nDictionary.getI18n;

/**
 * Обработчик страницы "Старт/Меню"
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class StartMenuHandler implements CommandHandler {

    @Override
    public boolean canExecute(UpdateDto updateDto) {
        return RouteType.START_ROUTE.equals(updateDto.getRoute())
               || RouteType.MENU_ROUTE.equals(updateDto.getRoute());
    }

    @Override
    public List<Object> executeList(UpdateDto updateDto) {
        log.info("*** StartHandler, chatId: {}, userName: {}", updateDto.getChatId(), updateDto.getFirstName());

        List<Object> result = new ArrayList<>();

        if (MessageType.CALLBACK_QUERY.equals(updateDto.getMessageType())) {
            result.add(StubCallback.answerStop(updateDto.getUpdate().getCallbackQuery().getId()));
        }

        I18nType message = (MessageType.CALLBACK_QUERY.equals(updateDto.getMessageType()))
                ? I18nType.MENU_MESSAGE
                : I18nType.START_MESSAGE;

        SendMessage sendMessage = SendMessage.builder()
                .chatId(updateDto.getChatId())
                .text(String.format(getI18n(message, updateDto.getLang()), updateDto.getFirstName()))
                .replyMarkup(getKeyboard(updateDto))
                .build();
        result.add(sendMessage);
        return result;
    }

    private ReplyKeyboardMarkup getKeyboard(UpdateDto updateDto) {

        KeyboardRow row1 = new KeyboardRow();
        row1.add("🚗 " + getI18n(I18nType.CATALOG_MENU, updateDto.getLang()));
        row1.add("ℹ️ " + getI18n(I18nType.ABOUT_US_MENU, updateDto.getLang()));

        KeyboardRow row2 = new KeyboardRow();
        row2.add("📞 " + getI18n(I18nType.CONTACT_MENU, updateDto.getLang()));
        row2.add("📄 " + getI18n(I18nType.HELP_MENU, updateDto.getLang()));

        KeyboardRow row3 = new KeyboardRow();
        row3.add("⚙️ " + getI18n(I18nType.SETTINGS_MENU, updateDto.getLang()));
        row3.add("🤔 " + getI18n(I18nType.QUESTION_MENU, updateDto.getLang()));

        return ReplyKeyboardMarkup.builder()
                .keyboard(List.of(row1, row2, row3))
                .resizeKeyboard(true)
                .build();
    }

}
