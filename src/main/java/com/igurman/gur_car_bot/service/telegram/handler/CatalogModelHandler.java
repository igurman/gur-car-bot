package com.igurman.gur_car_bot.service.telegram.handler;

import com.igurman.gur_car_bot.model.dto.FilterDto;
import com.igurman.gur_car_bot.model.dto.MakeModelDto;
import com.igurman.gur_car_bot.model.dto.UpdateDto;
import com.igurman.gur_car_bot.model.entity.VehicleEntity;
import com.igurman.gur_car_bot.service.common.VehicleService;
import com.igurman.gur_car_bot.service.provider.FilterProvider;
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
import java.util.Set;

import static com.igurman.gur_car_bot.constant.I18nType.CATALOG_FILTER_BACK;
import static com.igurman.gur_car_bot.constant.I18nType.CATALOG_MODEL_DELETE_ALL;
import static com.igurman.gur_car_bot.constant.I18nType.CATALOG_MODEL_NEXT;
import static com.igurman.gur_car_bot.constant.I18nType.CATALOG_MODEL_PREVIOUS;
import static com.igurman.gur_car_bot.constant.I18nType.CATALOG_MODEL_TAKE_ALL;
import static com.igurman.gur_car_bot.constant.RouteDictionary.getCallbackRoute;
import static com.igurman.gur_car_bot.constant.RouteType.CATALOG_FILTER_ROUTE;
import static com.igurman.gur_car_bot.constant.RouteType.CATALOG_MODEL_ROUTE;
import static com.igurman.gur_car_bot.dictionary.I18nDictionary.getI18n;

/**
 * Обработчик страницы "Выбор модели авто"
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CatalogModelHandler implements CommandHandler {
    private final FilterProvider filterProvider;
    private final FilterShowFragment filterShowFragment;
    private final VehicleService vehicleService;
    @Value("${application.telegram.bot.filter.model-size:20}")
    private int filterSize;
    private static final String TICK = "🔶 ";
    private static final String MODEL_ALL = "model-all";
    private static final String MODEL_CLEAN = "model-clean";
    private static final String MAKE_QUERY = "?make=";
    private static final String MODEL_QUERY = "&model=";
    private static final String PAGE_QUERY = "&page=";
    private static final String SIZE_QUERY = "&size=";
    private static final String MODEL_ALL_QUERY = "&model-all=1";
    private static final String MODEL_CLEAN_QUERY = "&model-clean=1";
    private static final String PAGE = "page";
    private static final String SIZE = "size";
    private static final String MAKE = "make";
    private static final String MODEL = "model";

    @Override
    public boolean canExecute(UpdateDto updateDto) {
        return CATALOG_MODEL_ROUTE.equals(updateDto.getRoute());
    }

    @Override
    public Object execute(UpdateDto updateDto) {
        log.info("*** CatalogModelHandler, chatId: {}, userName: {}", updateDto.getChatId(), updateDto.getFirstName());

        return EditMessageText.builder()
                .chatId(updateDto.getChatId())
                .replyMarkup(getInlineKeyboard(updateDto))
                .parseMode(ParseMode.HTML)
                .text(filterShowFragment.getView(updateDto))
                .messageId(updateDto.getMessageId())
                .build();
    }

    private InlineKeyboardMarkup getInlineKeyboard(UpdateDto updateDto) {

        int page = updateDto.getQueryParams().getOrDefault(PAGE, 0);
        int size = updateDto.getQueryParams().getOrDefault(SIZE, filterSize);
        int makeId = updateDto.getQueryParams().getOrDefault(MAKE, 0);
        int modelId = updateDto.getQueryParams().getOrDefault(MODEL, 0);

        if (updateDto.getQueryParams().containsKey(MODEL_ALL)) {
            filterProvider.addAll(updateDto.getUser().getId(), makeId);
        }

        if (updateDto.getQueryParams().containsKey(MODEL_CLEAN)) {
            filterProvider.deleteAllModel(updateDto.getUser().getId(), makeId);
        }

        // если больше 0, значит выбрана модель, нужно сохранить в БД
        if (modelId > 0) {
            filterProvider.addModel(updateDto.getUser().getId(), makeId, modelId);
        }
        // если меньше 0, значит модель нужно удалить из БД
        if (modelId < 0) {
            filterProvider.deleteModel(updateDto.getUser().getId(), makeId, (modelId * -1));
        }

        FilterDto filter = filterProvider.getFilter(updateDto.getUser().getId());

        Page<Object[]> modelList = vehicleService.findAllDistinctModelByMake(makeId, PageRequest.of(page, size));

        List<List<InlineKeyboardButton>> table = new ArrayList<>();

        VehicleEntity vehicleEntity = vehicleService.findMakeByMakeId(makeId);

        String topButtonQuery = MODEL_ALL_QUERY;
        String addAllModelsTitle = getI18n(CATALOG_MODEL_TAKE_ALL, updateDto.getLang(), vehicleEntity.getMakeTitle());
        MakeModelDto currentModel = filter.getMakeModelList().stream()
                .filter(makeModel -> makeModel.getMakeId() == makeId)
                .findFirst().orElse(null);

        // если в модели что-то есть, значит меняем кнопку на Удалить
        if (currentModel != null) {
            addAllModelsTitle = getI18n(CATALOG_MODEL_DELETE_ALL, updateDto.getLang(), vehicleEntity.getMakeTitle());
            topButtonQuery = MODEL_CLEAN_QUERY;
        }

        List<InlineKeyboardButton> row = new ArrayList<>();
        if (modelList.hasContent()) {

            InlineKeyboardButton allModel = InlineKeyboardButton.builder()
                    .text(addAllModelsTitle)
                    .callbackData(getCallbackRoute(CATALOG_MODEL_ROUTE) + MAKE_QUERY + makeId + topButtonQuery + PAGE_QUERY + page + SIZE_QUERY + size)
                    .build();
            table.add(List.of(allModel));

            for (int i = 0; i < modelList.getContent().size(); i++) {

                Object[] model = modelList.getContent().get(i);

                String modelName = String.valueOf(model[1]);
                int modelIdDelete = Integer.parseInt(String.valueOf(model[0]));

                Set<Integer> filterModelList = filter.getMakeModelMap().get(makeId);

                if (filterModelList != null && (filterModelList.isEmpty() || filterModelList.contains(modelIdDelete))) {
                    modelName = TICK + modelName;
                    modelIdDelete = modelIdDelete * -1;
                }

                row.add(InlineKeyboardButton.builder()
                        .text(modelName)
                        .callbackData(getCallbackRoute(CATALOG_MODEL_ROUTE) + MAKE_QUERY + makeId + MODEL_QUERY + modelIdDelete + PAGE_QUERY + page + SIZE_QUERY + size)
                        .build());

                if ((i + 1) % 2 == 0 || modelList.getContent().size() - 1 == i) {
                    table.add(row);
                    row = new ArrayList<>();
                }
            }

            InlineKeyboardButton leftBtn = InlineKeyboardButton.builder()
                    .text(getI18n(CATALOG_MODEL_PREVIOUS, updateDto.getLang()))
                    .callbackData(getCallbackRoute(CATALOG_MODEL_ROUTE) + MAKE_QUERY + makeId + PAGE_QUERY + (page - 1))
                    .build();

            InlineKeyboardButton rightBtn = InlineKeyboardButton.builder()
                    .text(getI18n(CATALOG_MODEL_NEXT, updateDto.getLang()))
                    .callbackData(getCallbackRoute(CATALOG_MODEL_ROUTE) + MAKE_QUERY + makeId + PAGE_QUERY + (page + 1))
                    .build();

            if (modelList.isFirst() && modelList.hasNext()) {
                table.add(List.of(rightBtn));
            } else if (modelList.isLast() && !modelList.isFirst()) {
                table.add(List.of(leftBtn));
            } else if (!modelList.isFirst() && !modelList.isLast() && (modelList.hasNext() || modelList.hasPrevious())) {
                table.add(List.of(leftBtn, rightBtn));
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
