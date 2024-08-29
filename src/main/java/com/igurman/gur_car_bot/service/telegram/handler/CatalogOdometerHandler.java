package com.igurman.gur_car_bot.service.telegram.handler;

import com.igurman.gur_car_bot.model.dto.UpdateDto;
import com.igurman.gur_car_bot.service.provider.FilterProvider;
import com.igurman.gur_car_bot.service.telegram.handler.fragment.FilterShowFragment;
import com.igurman.gur_car_bot.util.FormatUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

import static com.igurman.gur_car_bot.constant.I18nType.CATALOG_FILTER_BACK;
import static com.igurman.gur_car_bot.constant.I18nType.CATALOG_FILTER_ODOMETER_CLEAN;
import static com.igurman.gur_car_bot.constant.RouteDictionary.getCallbackRoute;
import static com.igurman.gur_car_bot.constant.RouteType.CATALOG_FILTER_ROUTE;
import static com.igurman.gur_car_bot.constant.RouteType.CATALOG_ODOMETER_ROUTE;
import static com.igurman.gur_car_bot.dictionary.I18nDictionary.getI18n;

/**
 * Обработчик страницы "Выбор года"
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CatalogOdometerHandler implements CommandHandler {
    private final FilterProvider filterProvider;
    private final FilterShowFragment filterShowFragment;

    private static final String ODOMETER_START = "odometer-start";
    private static final String ODOMETER_START_QUERY = "?odometer-start=";
    private static final String ODOMETER_END = "odometer-end";
    private static final String ODOMETER_END_QUERY = "?odometer-end=";
    private static final String ODOMETER_CLEAN = "clean";
    private static final String ODOMETER_CLEAN_QUERY = "?clean=1";

    private static final List<Integer> ODOMETER_LIST = List.of(0, 5000, 10000, 20000, 30000, 40000, 50000, 60000, 70000, 80000, 90000,
            100000, 120000, 140000, 160000, 180000, 200000, 250000, 300000, 400000);

    @Override
    public boolean canExecute(UpdateDto updateDto) {
        return CATALOG_ODOMETER_ROUTE.equals(updateDto.getRoute());
    }

    @Override
    public Object execute(UpdateDto updateDto) {
        log.info("*** CatalogOdometerHandler, chatId: {}, userName: {}", updateDto.getChatId(), updateDto.getFirstName());

        return EditMessageText.builder()
                .chatId(updateDto.getChatId())
                .messageId(updateDto.getMessageId())
                .parseMode(ParseMode.HTML)
                .replyMarkup(getInlineKeyboard(updateDto))
                .text(filterShowFragment.getView(updateDto))
                .build();
    }

    private InlineKeyboardMarkup getInlineKeyboard(UpdateDto updateDto) {

        if (updateDto.getQueryParams().containsKey(ODOMETER_CLEAN)) {
            filterProvider.odometerClean(updateDto.getUser().getId());
        }

        Integer queryOdometerStart = updateDto.getQueryParams().getOrDefault(ODOMETER_START, null);
        Integer queryOdometerEnd = updateDto.getQueryParams().getOrDefault(ODOMETER_END, null);

        if (queryOdometerStart != null) {
            log.info("*** odometer start: {}", queryOdometerStart);
            filterProvider.setOdometerStart(updateDto.getUser().getId(), queryOdometerStart);
            filterProvider.setOdometerEnd(updateDto.getUser().getId(), null);
        }

        if (queryOdometerEnd != null) {
            log.info("*** odometer end: {}", queryOdometerEnd);
            if (updateDto.getFilter().getOdometerStart() != null
                && updateDto.getFilter().getOdometerStart() > queryOdometerEnd) {
                queryOdometerEnd = updateDto.getFilter().getOdometerStart();
            }
            filterProvider.setOdometerEnd(updateDto.getUser().getId(), queryOdometerEnd);
        }

        List<List<InlineKeyboardButton>> table = new ArrayList<>();

        InlineKeyboardButton cleanBtn = InlineKeyboardButton.builder()
                .text(getI18n(CATALOG_FILTER_ODOMETER_CLEAN, updateDto.getLang()))
                .callbackData(getCallbackRoute(CATALOG_ODOMETER_ROUTE) + ODOMETER_CLEAN_QUERY)
                .build();
        table.add(List.of(cleanBtn));

        List<InlineKeyboardButton> row = new ArrayList<>();

        String odometerQuery = ODOMETER_START_QUERY;
        if (queryOdometerStart != null && queryOdometerEnd == null) {
            odometerQuery = ODOMETER_END_QUERY;
        }


        int odometerListSize = ODOMETER_LIST.size();
        for (int i = 0; i < odometerListSize; i++) {

            row.add(InlineKeyboardButton.builder()
                    .text(FormatUtil.formatInteger(ODOMETER_LIST.get(i)))
                    .callbackData(getCallbackRoute(CATALOG_ODOMETER_ROUTE) + odometerQuery + ODOMETER_LIST.get(i))
                    .build());

            if ((i + 1) % 4 == 0) {
                table.add(row);
                row = new ArrayList<>();
            }
        }

        InlineKeyboardButton back = InlineKeyboardButton.builder()
                .text(getI18n(CATALOG_FILTER_BACK, updateDto.getLang()))
                .callbackData(getCallbackRoute(CATALOG_FILTER_ROUTE))
                .build();
        table.add(List.of(back));

        return InlineKeyboardMarkup.builder()
                .keyboard(table)
                .build();
    }

}
