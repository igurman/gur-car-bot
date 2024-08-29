package com.igurman.gur_car_bot.service.telegram.handler;

import com.igurman.gur_car_bot.constant.EngineType;
import com.igurman.gur_car_bot.dictionary.DictionaryProvider;
import com.igurman.gur_car_bot.dictionary.EngineTypeDictionary;
import com.igurman.gur_car_bot.dictionary.EngineTypeTypeDictionary;
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
import java.util.Map;

import static com.igurman.gur_car_bot.constant.I18nType.CATALOG_FILTER_BACK;
import static com.igurman.gur_car_bot.constant.I18nType.CATALOG_FILTER_ENGINE_CLEAN;
import static com.igurman.gur_car_bot.constant.RouteDictionary.getCallbackRoute;
import static com.igurman.gur_car_bot.constant.RouteType.CATALOG_ENGINE_TYPE_ROUTE;
import static com.igurman.gur_car_bot.constant.RouteType.CATALOG_FILTER_ROUTE;
import static com.igurman.gur_car_bot.dictionary.I18nDictionary.getI18n;

/**
 * Обработчик страницы "Выбор года"
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CatalogEngineTypeHandler implements CommandHandler {
    private final DictionaryProvider dictionaryProvider;
    private final FilterShowFragment filterShowFragment;
    public final FilterProvider filterProvider;
    public final EngineTypeTypeDictionary engineTypeTypeDictionary;
    private static final String ENGINE = "engine";
    private static final String ENGINE_QUERY = "?engine=";
    private static final String ENGINE_CLEAN = "clean";
    private static final String ENGINE_CLEAN_QUERY = "?clean=1";
    private static final String TICK = "🔶 ";

    @Override
    public boolean canExecute(UpdateDto updateDto) {
        return CATALOG_ENGINE_TYPE_ROUTE.equals(updateDto.getRoute());
    }

    @Override
    public Object execute(UpdateDto updateDto) {
        log.info("*** CatalogEngineTypeHandler, chatId: {}, userName: {}", updateDto.getChatId(), updateDto.getFirstName());

        return EditMessageText.builder()
                .chatId(updateDto.getChatId())
                .messageId(updateDto.getMessageId())
                .parseMode(ParseMode.HTML)
                .replyMarkup(getInlineKeyboard(updateDto))
                .text(filterShowFragment.getView(updateDto))
                .build();
    }

    private InlineKeyboardMarkup getInlineKeyboard(UpdateDto updateDto) {

        if (updateDto.getQueryParams().containsKey(ENGINE_CLEAN)) {
            filterProvider.engineTypeClean(updateDto.getUser().getId());
        }

        Integer engineType = updateDto.getQueryParams().getOrDefault(ENGINE, 0);
        log.info("*** engine type: {}", engineType);

        // если больше 0, значит выбрана, нужно сохранить в БД
        if (engineType > 0) {
            filterProvider.addEngineType(updateDto.getUser().getId(), engineType);
        }
        // если меньше 0, значит нужно удалить из БД
        if (engineType < 0) {
            filterProvider.deleteEngineType(updateDto.getUser().getId(), (engineType * -1));
        }

        List<List<InlineKeyboardButton>> table = new ArrayList<>();

        InlineKeyboardButton clean = InlineKeyboardButton.builder()
                .text(getI18n(CATALOG_FILTER_ENGINE_CLEAN, updateDto.getLang()))
                .callbackData(getCallbackRoute(CATALOG_ENGINE_TYPE_ROUTE) + ENGINE_CLEAN_QUERY)
                .build();
        table.add(List.of(clean));

        List<Integer> filterengineList = filterProvider.findAllEngineByUser(updateDto.getUser().getId());
        Map<String, String> engineTypeMapDictionary = dictionaryProvider.get(EngineTypeDictionary.DICTIONARY_NAME, updateDto.getLang());

        List<EngineType> engineTypeList = EngineType.getList();
        for (EngineType engineTypeItem : engineTypeList) {
            String engineName = engineTypeMapDictionary.get(engineTypeItem.name());
            Integer engineId = engineTypeItem.code();

            if (filterengineList != null && filterengineList.contains(engineId)) {
                engineName = TICK + engineName;
                engineId = engineId * -1;
            }

            table.add(List.of(InlineKeyboardButton.builder()
                    .text(engineName)
                    .callbackData(getCallbackRoute(CATALOG_ENGINE_TYPE_ROUTE) + ENGINE_QUERY + engineId)
                    .build()));
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
