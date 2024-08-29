package com.igurman.gur_car_bot.service.telegram.handler;

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

import java.util.ArrayList;
import java.util.List;

import static com.igurman.gur_car_bot.constant.I18nType.CATALOG_FILTER_BACK;
import static com.igurman.gur_car_bot.constant.I18nType.CATALOG_FILTER_GRADE_CLEAN;
import static com.igurman.gur_car_bot.constant.RouteDictionary.getCallbackRoute;
import static com.igurman.gur_car_bot.constant.RouteType.CATALOG_FILTER_ROUTE;
import static com.igurman.gur_car_bot.constant.RouteType.CATALOG_GRADE_ROUTE;
import static com.igurman.gur_car_bot.dictionary.I18nDictionary.getI18n;
import static com.igurman.gur_car_bot.util.FormatUtil.formatGrade;

/**
 * Обработчик страницы "Выбор оценки"
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CatalogGradeHandler implements CommandHandler {
    private final FilterProvider filterProvider;
    private final FilterShowFragment filterShowFragment;

    private static final String GRADE_START = "grade-start";
    private static final String GRADE_START_QUERY = "?grade-start=";
    private static final String GRADE_END = "grade-end";
    private static final String GRADE_END_QUERY = "?grade-end=";
    private static final String GRADE_CLEAN = "clean";
    private static final String GRADE_CLEAN_QUERY = "?clean=1";

    private static final List<Integer> SINGLE_LIST = List.of(0, 15, 20, 25, 30, 31, 32, 33, 34, 35,
            36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50);

    @Override
    public boolean canExecute(UpdateDto updateDto) {
        return CATALOG_GRADE_ROUTE.equals(updateDto.getRoute());
    }

    @Override
    public Object execute(UpdateDto updateDto) {
        log.info("*** CatalogGradeHandler, chatId: {}, userName: {}", updateDto.getChatId(), updateDto.getFirstName());

        return EditMessageText.builder()
                .chatId(updateDto.getChatId())
                .messageId(updateDto.getMessageId())
                .parseMode(ParseMode.HTML)
                .replyMarkup(getInlineKeyboard(updateDto))
                .text(filterShowFragment.getView(updateDto))
                .build();
    }

    private InlineKeyboardMarkup getInlineKeyboard(UpdateDto updateDto) {

        if (updateDto.getQueryParams().containsKey(GRADE_CLEAN)) {
            filterProvider.gradeClean(updateDto.getUser().getId());
        }

        Integer queryGradeStart = updateDto.getQueryParams().getOrDefault(GRADE_START, null);
        Integer queryGradeEnd = updateDto.getQueryParams().getOrDefault(GRADE_END, null);

        if (queryGradeStart != null && queryGradeEnd != null) {
            log.info("*** grade start: {}, end: {}", queryGradeStart, queryGradeEnd);
            filterProvider.setGradeStart(updateDto.getUser().getId(), queryGradeStart);
            filterProvider.setGradeEnd(updateDto.getUser().getId(), queryGradeEnd);
        }

        if (queryGradeStart != null) {
            log.info("*** grade start: {}", queryGradeStart);
            filterProvider.setGradeStart(updateDto.getUser().getId(), queryGradeStart);
            filterProvider.setGradeEnd(updateDto.getUser().getId(), null);
        }

        if (queryGradeEnd != null) {
            log.info("*** grade end: {}", queryGradeEnd);
            if (updateDto.getFilter().getGradeStart() != null
                && updateDto.getFilter().getGradeStart() > queryGradeEnd) {
                queryGradeEnd = updateDto.getFilter().getGradeStart();
            }
            filterProvider.setGradeEnd(updateDto.getUser().getId(), queryGradeEnd);
        }

        List<List<InlineKeyboardButton>> table = new ArrayList<>();

        InlineKeyboardButton clean = InlineKeyboardButton.builder()
                .text(getI18n(CATALOG_FILTER_GRADE_CLEAN, updateDto.getLang()))
                .callbackData(getCallbackRoute(CATALOG_GRADE_ROUTE) + GRADE_CLEAN_QUERY)
                .build();
        table.add(List.of(clean));

        String gradeQuery = GRADE_START_QUERY;
        if (queryGradeStart != null && queryGradeEnd == null) {
            gradeQuery = GRADE_END_QUERY;
        }

        // сборка второй части (цена по одной)
        List<InlineKeyboardButton> row = new ArrayList<>();
        for (int i = 0; i < SINGLE_LIST.size(); i++) {
            row.add(InlineKeyboardButton.builder()
                    .text(formatGrade(SINGLE_LIST.get(i)))
                    .callbackData(getCallbackRoute(CATALOG_GRADE_ROUTE) + gradeQuery + SINGLE_LIST.get(i))
                    .build());

            if ((i + 1) % 5 == 0) {
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
