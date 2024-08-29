package com.igurman.gur_car_bot.service.telegram.handler;

import com.igurman.gur_car_bot.model.dto.UpdateDto;
import com.igurman.gur_car_bot.service.provider.FilterProvider;
import com.igurman.gur_car_bot.service.telegram.handler.fragment.FilterShowFragment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

import static com.igurman.gur_car_bot.constant.I18nType.CATALOG_FILTER_BACK;
import static com.igurman.gur_car_bot.constant.I18nType.CATALOG_FILTER_PRICE_CLEAN;
import static com.igurman.gur_car_bot.constant.RouteDictionary.getCallbackRoute;
import static com.igurman.gur_car_bot.constant.RouteType.CATALOG_FILTER_ROUTE;
import static com.igurman.gur_car_bot.constant.RouteType.CATALOG_PRICE_ROUTE;
import static com.igurman.gur_car_bot.dictionary.I18nDictionary.getI18n;
import static com.igurman.gur_car_bot.util.FormatUtil.formatPrice;

/**
 * Обработчик страницы "Выбор года"
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CatalogPriceHandler implements CommandHandler {
    private final FilterProvider filterProvider;
    private final FilterShowFragment filterShowFragment;

    private static final String PRICE_START = "price-start";
    private static final String PRICE_START_QUERY = "?price-start=";
    private static final String PRICE_END = "price-end";
    private static final String PRICE_END_QUERY = "?price-end=";
    private static final String PRICE_END_QUERY_S = "&price-end=";
    private static final String PRICE_CLEAN = "clean";
    private static final String PRICE_CLEAN_QUERY = "?clean=1";

    private static final List<Pair<Integer, Integer>> PAIR_LIST = List.of(
            Pair.of(0, 5000),
            Pair.of(6000, 10000),
            Pair.of(11000, 15000),
            Pair.of(16000, 19000),
            Pair.of(20000, 25000),
            Pair.of(26000, 30000));
    private static final List<Integer> SINGLE_LIST = List.of(0, 3000, 4000, 5000, 6000, 7000, 8000, 9000, 10000, 11000, 12000, 13000,
            14000, 15000, 16000, 17000, 18000, 19000, 20000, 25000, 30000, 40000, 50000, 60000, 70000, 80000, 90000, 100000);

    @Override
    public boolean canExecute(UpdateDto updateDto) {
        return CATALOG_PRICE_ROUTE.equals(updateDto.getRoute());
    }

    @Override
    public Object execute(UpdateDto updateDto) {
        log.info("*** CatalogPriceHandler, chatId: {}, userName: {}", updateDto.getChatId(), updateDto.getFirstName());

        return EditMessageText.builder()
                .chatId(updateDto.getChatId())
                .messageId(updateDto.getMessageId())
                .parseMode(ParseMode.HTML)
                .replyMarkup(getInlineKeyboard(updateDto))
                .text(filterShowFragment.getView(updateDto))
                .build();
    }

    private InlineKeyboardMarkup getInlineKeyboard(UpdateDto updateDto) {

        if (updateDto.getQueryParams().containsKey(PRICE_CLEAN)) {
            filterProvider.priceClean(updateDto.getUser().getId());
        }

        Integer queryPriceStart = updateDto.getQueryParams().getOrDefault(PRICE_START, null);
        Integer queryPriceEnd = updateDto.getQueryParams().getOrDefault(PRICE_END, null);

        if (queryPriceStart != null && queryPriceEnd != null) {
            log.info("*** price start: {}, end: {}", queryPriceStart, queryPriceEnd);
            filterProvider.setPriceStart(updateDto.getUser().getId(), queryPriceStart);
            filterProvider.setPriceEnd(updateDto.getUser().getId(), queryPriceEnd);
        }

        if (queryPriceStart != null) {
            log.info("*** price start: {}", queryPriceStart);
            filterProvider.setPriceStart(updateDto.getUser().getId(), queryPriceStart);
            filterProvider.setPriceEnd(updateDto.getUser().getId(), null);
        }

        if (queryPriceEnd != null) {
            log.info("*** price end: {}", queryPriceEnd);
            if (updateDto.getFilter().getPriceStart() != null
                && updateDto.getFilter().getPriceStart() > queryPriceEnd) {
                queryPriceEnd = updateDto.getFilter().getPriceStart();
            }
            filterProvider.setPriceEnd(updateDto.getUser().getId(), queryPriceEnd);
        }

        List<List<InlineKeyboardButton>> table = new ArrayList<>();

        InlineKeyboardButton clean = InlineKeyboardButton.builder()
                .text(getI18n(CATALOG_FILTER_PRICE_CLEAN, updateDto.getLang()))
                .callbackData(getCallbackRoute(CATALOG_PRICE_ROUTE) + PRICE_CLEAN_QUERY)
                .build();
        table.add(List.of(clean));

        String priceQuery = PRICE_START_QUERY;
        if (queryPriceStart != null && queryPriceEnd == null) {
            priceQuery = PRICE_END_QUERY;
        }

        // сборка первой части (цена от - до)
        int sizePairList = PAIR_LIST.size();
        for (int i = 0; i < sizePairList; i++) {

            List<InlineKeyboardButton> rowPairList = new ArrayList<>();

            Pair<Integer, Integer> pair = PAIR_LIST.get(i);
            rowPairList.add(InlineKeyboardButton.builder()
                    .text(formatPrice(pair.getFirst()) + " - " + formatPrice(pair.getSecond()))
                    .callbackData(getCallbackRoute(CATALOG_PRICE_ROUTE) +
                                  PRICE_START_QUERY + pair.getFirst() + PRICE_END_QUERY_S + pair.getSecond())
                    .build());

            Pair<Integer, Integer> pair2 = PAIR_LIST.get(i + 1);
            rowPairList.add(InlineKeyboardButton.builder()
                    .text(formatPrice(pair2.getFirst()) + " - " + formatPrice(pair2.getSecond()))
                    .callbackData(getCallbackRoute(CATALOG_PRICE_ROUTE) +
                                  PRICE_START_QUERY + pair2.getFirst() + PRICE_END_QUERY_S + pair2.getSecond())
                    .build());

            table.add(rowPairList);
            i++;
        }

        // сборка второй части (цена по одной)
        List<InlineKeyboardButton> row = new ArrayList<>();
        for (int i = 0; i < SINGLE_LIST.size(); i++) {

            row.add(InlineKeyboardButton.builder()
                    .text(formatPrice(SINGLE_LIST.get(i)))
                    .callbackData(getCallbackRoute(CATALOG_PRICE_ROUTE) + priceQuery + SINGLE_LIST.get(i))
                    .build());

            if ((i + 1) % 4 == 0) {
                table.add(row);
                row = new ArrayList<>();
            }
        }

        // кнопка вернуться назад
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
