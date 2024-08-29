package com.igurman.gur_car_bot.service.telegram.handler;

import com.igurman.gur_car_bot.constant.LanguageType;
import com.igurman.gur_car_bot.constant.MessageType;
import com.igurman.gur_car_bot.constant.RouteType;
import com.igurman.gur_car_bot.model.dto.UpdateDto;
import com.igurman.gur_car_bot.service.provider.FilterProvider;
import com.igurman.gur_car_bot.service.telegram.handler.fragment.FilterShowFragment;
import com.igurman.gur_car_bot.util.button.StubCallback;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

import static com.igurman.gur_car_bot.constant.I18nType.CATALOG_FILTER_BUILD;
import static com.igurman.gur_car_bot.constant.I18nType.CATALOG_FILTER_CLEAN;
import static com.igurman.gur_car_bot.constant.I18nType.CATALOG_SEARCH_START;
import static com.igurman.gur_car_bot.constant.RouteDictionary.getCallbackRoute;
import static com.igurman.gur_car_bot.constant.RouteType.CATALOG_FILTER_ROUTE;
import static com.igurman.gur_car_bot.constant.RouteType.CATALOG_ROUTE;
import static com.igurman.gur_car_bot.constant.RouteType.CATALOG_SEARCH_ROUTE;
import static com.igurman.gur_car_bot.dictionary.I18nDictionary.getI18n;

/**
 * Обработчик страницы "Каталог"
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CatalogHandler implements CommandHandler {
    private final FilterProvider filterProvider;
    private final FilterShowFragment filterShowFragment;
    private static final String CLEAN_QUERY = "?clean=-1";
    private static final String CLEAN = "clean";
    private static final String MESSAGE = "message";

    @Override
    public boolean canExecute(UpdateDto updateDto) {
        return CATALOG_ROUTE.equals(updateDto.getRoute())
               || RouteType.CATALOG_MENU_ROUTE.equals(updateDto.getRoute());
    }

    @Override
    public List<Object> executeList(UpdateDto updateDto) {
        List<Object> result = new ArrayList<>();

        if (updateDto.getQueryParams().containsKey(CLEAN)) {
            filterProvider.deleteFilter(updateDto.getUser().getId());
        }

        if (MessageType.MESSAGE.equals(updateDto.getMessageType())
            || updateDto.getQueryParams().containsKey(MESSAGE)) {

            if (updateDto.getQueryParams().containsKey(MESSAGE)) {
                String callbackQueryId = updateDto.getUpdate().getCallbackQuery().getId();
                result.add(StubCallback.answerStop(callbackQueryId));
            }

            log.info("*** CatalogHandler, chatId: {}, userName: {}", updateDto.getChatId(), updateDto.getFirstName());

            SendMessage sendMessage = SendMessage.builder()
                    .chatId(updateDto.getChatId())
                    .replyToMessageId(updateDto.getMessageId())
                    .parseMode(ParseMode.HTML)
                    .text(filterShowFragment.getView(updateDto))
                    .replyMarkup(getInlineKeyboard(updateDto.getLang()))
                    .build();
            result.add(sendMessage);
            return result;
        }

        if (MessageType.CALLBACK_QUERY.equals(updateDto.getMessageType())) {
            log.info("*** CatalogHandler, chatId: {}, userName: {}, messageId: {}", updateDto.getChatId(),
                    updateDto.getFirstName(), updateDto.getMessageId());

            EditMessageText editMessageText = EditMessageText.builder()
                    .chatId(updateDto.getChatId())
                    .messageId(updateDto.getMessageId())
                    .parseMode(ParseMode.HTML)
                    .text(filterShowFragment.getView(updateDto))
                    .replyMarkup(getInlineKeyboard(updateDto.getLang()))
                    .build();
            result.add(editMessageText);
        }
        return result;
    }

    private InlineKeyboardMarkup getInlineKeyboard(LanguageType lang) {

        InlineKeyboardButton filterBtn = InlineKeyboardButton.builder()
                .text(getI18n(CATALOG_FILTER_BUILD, lang))
                .callbackData(getCallbackRoute(CATALOG_FILTER_ROUTE))
                .build();

        InlineKeyboardButton cleanFilterBtn = InlineKeyboardButton.builder()
                .text(getI18n(CATALOG_FILTER_CLEAN, lang))
                .callbackData(getCallbackRoute(CATALOG_ROUTE) + CLEAN_QUERY)
                .build();

        InlineKeyboardButton searchBtn = InlineKeyboardButton.builder()
                .text(getI18n(CATALOG_SEARCH_START, lang))
                .callbackData(getCallbackRoute(CATALOG_SEARCH_ROUTE))
                .build();

        return InlineKeyboardMarkup.builder()
                .keyboard(
                        List.of(
                                List.of(filterBtn, cleanFilterBtn),
                                List.of(searchBtn)
                        ))
                .build();
    }

}
