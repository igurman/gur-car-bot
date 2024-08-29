package com.igurman.gur_car_bot.service.telegram.handler;

import com.igurman.gur_car_bot.constant.LanguageType;
import com.igurman.gur_car_bot.constant.RouteType;
import com.igurman.gur_car_bot.model.dto.UpdateDto;
import com.igurman.gur_car_bot.service.telegram.handler.fragment.FilterShowFragment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

import static com.igurman.gur_car_bot.constant.I18nType.CATALOG_BACK;
import static com.igurman.gur_car_bot.constant.I18nType.CATALOG_FILTER_ENGINE_TYPE;
import static com.igurman.gur_car_bot.constant.I18nType.CATALOG_FILTER_MODEL;
import static com.igurman.gur_car_bot.constant.I18nType.CATALOG_FILTER_YEAR;
import static com.igurman.gur_car_bot.constant.I18nType.CATALOG_SEARCH_START;
import static com.igurman.gur_car_bot.constant.I18nType.GRADE_FILTER;
import static com.igurman.gur_car_bot.constant.I18nType.ODOMETER_FILTER;
import static com.igurman.gur_car_bot.constant.I18nType.PRICE_FILTER;
import static com.igurman.gur_car_bot.constant.RouteDictionary.getCallbackRoute;
import static com.igurman.gur_car_bot.constant.RouteType.CATALOG_ENGINE_TYPE_ROUTE;
import static com.igurman.gur_car_bot.constant.RouteType.CATALOG_GRADE_ROUTE;
import static com.igurman.gur_car_bot.constant.RouteType.CATALOG_MAKE_ROUTE;
import static com.igurman.gur_car_bot.constant.RouteType.CATALOG_ODOMETER_ROUTE;
import static com.igurman.gur_car_bot.constant.RouteType.CATALOG_PRICE_ROUTE;
import static com.igurman.gur_car_bot.constant.RouteType.CATALOG_ROUTE;
import static com.igurman.gur_car_bot.constant.RouteType.CATALOG_SEARCH_ROUTE;
import static com.igurman.gur_car_bot.constant.RouteType.CATALOG_YEAR_ROUTE;
import static com.igurman.gur_car_bot.dictionary.I18nDictionary.getI18n;

/**
 * Обработчик страницы "Фильтры"
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CatalogFilterHandler implements CommandHandler {
    private final FilterShowFragment filterShowFragment;

    @Override
    public boolean canExecute(UpdateDto updateDto) {
        return RouteType.CATALOG_FILTER_ROUTE.equals(updateDto.getRoute());
    }

    @Override
    public Object execute(UpdateDto updateDto) {
        log.info("*** CatalogFilterHandler, chatId: {}, userName: {}, messageId: {}", updateDto.getChatId(),
                updateDto.getFirstName(), updateDto.getMessageId());

        return EditMessageText.builder()
                .chatId(updateDto.getChatId())
                .messageId(updateDto.getMessageId())
                .parseMode(ParseMode.HTML)
                .text(filterShowFragment.getView(updateDto))
                .replyMarkup(getInlineKeyboard(updateDto.getLang()))
                .build();
    }

    private InlineKeyboardMarkup getInlineKeyboard(LanguageType lang) {

        InlineKeyboardButton modelBtn = InlineKeyboardButton.builder()
                .text(getI18n(CATALOG_FILTER_MODEL, lang))
                .callbackData(getCallbackRoute(CATALOG_MAKE_ROUTE))
                .build();

        InlineKeyboardButton engineTypeBtn = InlineKeyboardButton.builder()
                .text(getI18n(CATALOG_FILTER_ENGINE_TYPE, lang))
                .callbackData(getCallbackRoute(CATALOG_ENGINE_TYPE_ROUTE))
                .build();

        InlineKeyboardButton yearBtn = InlineKeyboardButton.builder()
                .text(getI18n(CATALOG_FILTER_YEAR, lang))
                .callbackData(getCallbackRoute(CATALOG_YEAR_ROUTE))
                .build();

        InlineKeyboardButton odometerBtn = InlineKeyboardButton.builder()
                .text(getI18n(ODOMETER_FILTER, lang))
                .callbackData(getCallbackRoute(CATALOG_ODOMETER_ROUTE))
                .build();

        InlineKeyboardButton priceBtn = InlineKeyboardButton.builder()
                .text(getI18n(PRICE_FILTER, lang))
                .callbackData(getCallbackRoute(CATALOG_PRICE_ROUTE))
                .build();

        InlineKeyboardButton gradeBtn = InlineKeyboardButton.builder()
                .text(getI18n(GRADE_FILTER, lang))
                .callbackData(getCallbackRoute(CATALOG_GRADE_ROUTE))
                .build();

        InlineKeyboardButton backBtn = InlineKeyboardButton.builder()
                .text(getI18n(CATALOG_BACK, lang))
                .callbackData(getCallbackRoute(CATALOG_ROUTE))
                .build();

        InlineKeyboardButton searchBtn = InlineKeyboardButton.builder()
                .text(getI18n(CATALOG_SEARCH_START, lang))
                .callbackData(getCallbackRoute(CATALOG_SEARCH_ROUTE))
                .build();

        return InlineKeyboardMarkup.builder()
                .keyboard(
                        List.of(
                                List.of(modelBtn, engineTypeBtn),
                                List.of(yearBtn, odometerBtn),
                                List.of(priceBtn, gradeBtn),
                                List.of(backBtn),
                                List.of(searchBtn)
                        ))
                .build();
    }

}
