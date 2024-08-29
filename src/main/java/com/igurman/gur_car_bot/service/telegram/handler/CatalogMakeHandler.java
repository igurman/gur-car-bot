package com.igurman.gur_car_bot.service.telegram.handler;

import com.igurman.gur_car_bot.model.dto.UpdateDto;
import com.igurman.gur_car_bot.service.common.VehicleService;
import com.igurman.gur_car_bot.service.telegram.handler.fragment.FilterShowFragment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.igurman.gur_car_bot.constant.I18nType.CATALOG_FILTER_BACK;
import static com.igurman.gur_car_bot.constant.I18nType.CATALOG_NEXT;
import static com.igurman.gur_car_bot.constant.I18nType.CATALOG_PREVIOUS;
import static com.igurman.gur_car_bot.constant.RouteDictionary.getCallbackRoute;
import static com.igurman.gur_car_bot.constant.RouteType.CATALOG_FILTER_ROUTE;
import static com.igurman.gur_car_bot.constant.RouteType.CATALOG_MAKE_ROUTE;
import static com.igurman.gur_car_bot.constant.RouteType.CATALOG_MODEL_ROUTE;
import static com.igurman.gur_car_bot.dictionary.I18nDictionary.getI18n;

/**
 * Обработчик страницы "Выбор марки авто"
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CatalogMakeHandler implements CommandHandler {
    private final FilterShowFragment filterShowFragment;
    private final VehicleService vehicleService;
    private static final String TICK = "🔶 ";
    @Value("${application.telegram.bot.filter.make-size:20}")
    private int filterSize;
    private static final String PAGE_QUERY = "?page=";
    private static final String SIZE_QUERY = "&size=";
    private static final String MAKE_QUERY = "?make=";
    private static final String PAGE = "page";
    private static final String SIZE = "size";

    @Override
    public boolean canExecute(UpdateDto updateDto) {
        return CATALOG_MAKE_ROUTE.equals(updateDto.getRoute());
    }

    @Override
    public Object execute(UpdateDto updateDto) {
        log.info("*** CatalogMakeHandler, chatId: {}, userName: {}", updateDto.getChatId(), updateDto.getFirstName());

        return EditMessageText.builder()
                .chatId(updateDto.getChatId())
                .parseMode(ParseMode.HTML)
                .text(filterShowFragment.getView(updateDto))
                .messageId(updateDto.getMessageId())
                .replyMarkup(getInlineKeyboard(updateDto))
                .build();
    }

    private InlineKeyboardMarkup getInlineKeyboard(UpdateDto updateDto) {
        int page = updateDto.getQueryParams().getOrDefault(PAGE, 0);
        int size = updateDto.getQueryParams().getOrDefault(SIZE, filterSize);

        // получим все марки из имеющихся авто
        Page<Object[]> makeList = vehicleService.findAllDistinctMake(PageRequest.of(page, size));

        List<List<InlineKeyboardButton>> table = new ArrayList<>();
        List<InlineKeyboardButton> row = new ArrayList<>();

        // получим данные о сохраненных марках из фильтра
        Map<Integer, Set<Integer>> makeModel = updateDto.getFilter().getMakeModel();
        if (makeList.hasContent()) {
            for (int i = 0; i < makeList.getContent().size(); i++) {

                Object[] make = makeList.getContent().get(i);
                String text = String.valueOf(make[1]);
                // если марка есть в фильтре, пометим её тиком
                Integer makeId = (Integer) make[0];
                if (makeModel != null && makeModel.containsKey(makeId)) {
                    text = TICK + make[1];
                }
                // соберём кнопку
                row.add(InlineKeyboardButton.builder()
                        .text(text)
                        .callbackData(getCallbackRoute(CATALOG_MODEL_ROUTE) + MAKE_QUERY + make[0])
                        .build());

                // разделим кнопки на 2 ряда
                if ((i + 1) % 2 == 0 || makeList.getContent().size() - 1 == i) {
                    table.add(row);
                    row = new ArrayList<>();
                }
            }
        }

        InlineKeyboardButton leftBtn = InlineKeyboardButton.builder()
                .text(getI18n(CATALOG_PREVIOUS, updateDto.getLang()))
                .callbackData(getCallbackRoute(CATALOG_MAKE_ROUTE) + PAGE_QUERY + (page - 1) + SIZE_QUERY + size)
                .build();

        InlineKeyboardButton rightBtn = InlineKeyboardButton.builder()
                .text(getI18n(CATALOG_NEXT, updateDto.getLang()))
                .callbackData(getCallbackRoute(CATALOG_MAKE_ROUTE) + PAGE_QUERY + (page + 1) + SIZE_QUERY + size)
                .build();

        if (makeList.isFirst()) {
            table.add(List.of(rightBtn));
        } else if (makeList.isLast()) {
            table.add(List.of(leftBtn));
        } else {
            table.add(List.of(leftBtn, rightBtn));
        }

        InlineKeyboardButton backBtn = InlineKeyboardButton.builder()
                .text(getI18n(CATALOG_FILTER_BACK, updateDto.getLang()))
                .callbackData(getCallbackRoute(CATALOG_FILTER_ROUTE))
                .build();
        table.add(List.of(backBtn));

        return InlineKeyboardMarkup.builder()
                .keyboard(table)
                .build();
    }

}
