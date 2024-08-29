package com.igurman.gur_car_bot.service.telegram.handler;

import com.igurman.gur_car_bot.constant.RouteType;
import com.igurman.gur_car_bot.model.dto.UpdateDto;
import com.igurman.gur_car_bot.service.provider.FilterProvider;
import com.igurman.gur_car_bot.service.telegram.handler.fragment.FilterShowFragment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.igurman.gur_car_bot.constant.I18nType.CATALOG_FILTER_BACK;
import static com.igurman.gur_car_bot.constant.I18nType.CATALOG_FILTER_YEAR_CLEAN;
import static com.igurman.gur_car_bot.constant.RouteDictionary.getCallbackRoute;
import static com.igurman.gur_car_bot.dictionary.I18nDictionary.getI18n;

/**
 * Обработчик страницы "Выбор года"
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CatalogYearHandler implements CommandHandler {
    private final FilterProvider filterProvider;
    public final FilterShowFragment filterShowFragment;
    private static final String YEAR_START = "year-start";
    private static final String YEAR_START_QUERY = "?year-start=";
    private static final String YEAR_END = "year-end";
    private static final String YEAR_END_QUERY = "?year-end=";
    private static final String YEAR_CLEAN = "clean";
    private static final String YEAR_CLEAN_QUERY = "?clean=1";

    private static final int YEAR_START_BT = LocalDate.now().getYear() - 24 + 1;

    @Override
    public boolean canExecute(UpdateDto updateDto) {
        return RouteType.CATALOG_YEAR_ROUTE.equals(updateDto.getRoute());
    }

    @Override
    public Object execute(UpdateDto updateDto) {
        log.info("*** CatalogYearHandler, chatId: {}, userName: {}", updateDto.getChatId(), updateDto.getFirstName());

        return EditMessageText.builder()
                .chatId(updateDto.getChatId())
                .messageId(updateDto.getMessageId())
                .parseMode(ParseMode.HTML)
                .replyMarkup(getInlineKeyboard(updateDto))
                .text(filterShowFragment.getView(updateDto))
                .build();
    }

    private InlineKeyboardMarkup getInlineKeyboard(UpdateDto updateDto) {

        if (updateDto.getQueryParams().containsKey(YEAR_CLEAN)) {
            filterProvider.yearClean(updateDto.getUser().getId());
        }

        Integer queryYearStart = updateDto.getQueryParams().getOrDefault(YEAR_START, null);
        Integer queryYearEnd = updateDto.getQueryParams().getOrDefault(YEAR_END, null);

        if (queryYearStart != null) {
            log.info("*** year start: {}", queryYearStart);
            filterProvider.setYearStart(updateDto.getUser().getId(), queryYearStart);
            filterProvider.setYearEnd(updateDto.getUser().getId(), null);
        }
        if (queryYearEnd != null) {
            log.info("*** year end: {}", queryYearEnd);
            if (updateDto.getFilter().getYearStart() != null
                && updateDto.getFilter().getYearStart() > queryYearEnd) {
                queryYearEnd = updateDto.getFilter().getYearStart();
            }
            filterProvider.setYearEnd(updateDto.getUser().getId(), queryYearEnd);
        }

        List<List<InlineKeyboardButton>> table = new ArrayList<>();

        InlineKeyboardButton clean = InlineKeyboardButton.builder()
                .text(getI18n(CATALOG_FILTER_YEAR_CLEAN, updateDto.getLang()))
                .callbackData(getCallbackRoute(RouteType.CATALOG_YEAR_ROUTE) + YEAR_CLEAN_QUERY)
                .build();
        table.add(List.of(clean));

        List<InlineKeyboardButton> row = new ArrayList<>();

        String yearQuery = YEAR_START_QUERY;
        if (queryYearStart != null) {
            yearQuery = YEAR_END_QUERY;
        }

        for (int i = 0; i < 24; i++) {
            int yearNumber = YEAR_START_BT + i;

            row.add(InlineKeyboardButton.builder()
                    .text(String.valueOf(yearNumber))
                    .callbackData(getCallbackRoute(RouteType.CATALOG_YEAR_ROUTE) + yearQuery + yearNumber)
                    .build());

            if ((i + 1) % 4 == 0 || 24 - 1 == i) {
                table.add(row);
                row = new ArrayList<>();
            }
        }

        InlineKeyboardButton backBtn = InlineKeyboardButton.builder()
                .text(getI18n(CATALOG_FILTER_BACK, updateDto.getLang()))
                .callbackData(getCallbackRoute(RouteType.CATALOG_FILTER_ROUTE))
                .build();
        table.add(List.of(backBtn));

        return InlineKeyboardMarkup.builder()
                .keyboard(table)
                .build();
    }

}
