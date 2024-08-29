package com.igurman.gur_car_bot.service.telegram.handler;

import com.igurman.gur_car_bot.constant.I18nType;
import com.igurman.gur_car_bot.constant.LanguageType;
import com.igurman.gur_car_bot.constant.RouteType;
import com.igurman.gur_car_bot.model.dto.UpdateDto;
import com.igurman.gur_car_bot.model.dto.VehicleDetalizationDto;
import com.igurman.gur_car_bot.model.entity.PictureEntity;
import com.igurman.gur_car_bot.model.entity.VehicleEntity;
import com.igurman.gur_car_bot.service.provider.VehicleProvider;
import com.igurman.gur_car_bot.service.telegram.handler.fragment.ItemDetailsFragment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMediaGroup;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.media.InputMedia;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaPhoto;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

import static com.igurman.gur_car_bot.constant.I18nType.ABOUT_TELEGRAM_URL;
import static com.igurman.gur_car_bot.constant.I18nType.SEARCH_ORDER_CARFAX;
import static com.igurman.gur_car_bot.constant.RouteDictionary.getCallbackRoute;
import static com.igurman.gur_car_bot.dictionary.I18nDictionary.getI18n;
import static com.igurman.gur_car_bot.util.button.StubCallback.answerStop;

/**
 * Обработчик страницы "Показ найденных элементов по фильтру"
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CatalogSearchHandler implements CommandHandler {
    private final VehicleProvider vehicleProvider;
    private final ItemDetailsFragment itemDetailsFragment;
    @Value("${application.telegram.bot.filter.search-size:3}")
    private int searchSize;
    protected static final String PAGE = "page";
    protected static final String SIZE = "size";
    private static final String PAGE_QUERY = "?page=";
    private static final String SIZE_QUERY = "&size=";
    private static final String VEHICLE_ID_QUERY = "?vehicle-id=";
    private static final String DETAILS = "details";
    private static final String DETAILS_QUERY = "?details=";


    @Override
    public boolean canExecute(UpdateDto updateDto) {
        return RouteType.CATALOG_SEARCH_ROUTE.equals(updateDto.getRoute());
    }

    @Override
    public List<Object> executeList(UpdateDto updateDto) {
        Long chatId = updateDto.getChatId();
        String callbackQueryId = updateDto.getUpdate().getCallbackQuery().getId();
        log.info("*** CatalogSearchHandler, chatId: {}, userName: {}", chatId, updateDto.getFirstName());

        if (updateDto.getQueryParams().containsKey(DETAILS)) {
            return getDetails(updateDto, chatId);
        }

        int page = updateDto.getQueryParams().getOrDefault(PAGE, 0);
        int size = updateDto.getQueryParams().getOrDefault(SIZE, searchSize);

        VehicleDetalizationDto vehicleDetalization = vehicleProvider.findAllByFilter(updateDto.getUser().getId(), PageRequest.of(page, size));
        Page<VehicleEntity> vehicleList = vehicleDetalization.getData();
        List<Object> result = new ArrayList<>();

        result.add(answerStop(callbackQueryId));

        List<SendPhoto> sendPhotoList = getItems(updateDto, chatId, vehicleDetalization);
        if (!CollectionUtils.isEmpty(sendPhotoList)) {
            result.addAll(sendPhotoList);
        }

        if (!vehicleList.isLast()) {
            result.add(getNextMessage(chatId, updateDto, vehicleDetalization));
        } else {
            result.add(getFinishMessage(chatId, updateDto));
        }

        return result;
    }

    private List<Object> getDetails(UpdateDto updateDto, Long chatId) {
        Integer vehicleId = updateDto.getQueryParams().get(DETAILS);
        log.info("*** Получили запрос на детализация авто id: {}", vehicleId);
        VehicleEntity detailsVehicleId = vehicleProvider.findById(vehicleId);

        if (detailsVehicleId != null) {
            List<PictureEntity> pictureByVehicleId = vehicleProvider.getPictureByVehicleId(vehicleId);

            List<InputMedia> pictureList = new ArrayList<>();
            List<Object> resultList = new ArrayList<>();

            for (int i = 0; i < pictureByVehicleId.size(); i++) {
                PictureEntity pictureEntity = pictureByVehicleId.get(i);

                InputMedia inputMedia = new InputMediaPhoto();
                inputMedia.setMedia(pictureEntity.getLink());
                pictureList.add(inputMedia);

                if ((i + 1) % 10 == 0 || pictureByVehicleId.size() - 1 == i) {
                    SendMediaGroup sendMediaGroup = SendMediaGroup.builder()
                            .chatId(chatId)
                            .medias(pictureList)
                            .build();
                    resultList.add(sendMediaGroup);
                    pictureList = new ArrayList<>();
                }

            }

            SendMessage sendMessage = SendMessage.builder()
                    .chatId(chatId)
                    .parseMode(ParseMode.HTML)
                    .text(itemDetailsFragment.getItemFullDetails(detailsVehicleId, updateDto.getLang()))
                    .replyMarkup(getInlineKeyboardForItemWithoutDetail(updateDto.getLang(), detailsVehicleId.getId()))
                    .build();

            resultList.add(sendMessage);
            return resultList;
        }
        return null;
    }

    private SendMessage getNextMessage(Long chatId, UpdateDto updateDto, VehicleDetalizationDto vehicleDetalization) {
        Page<VehicleEntity> vehicleList = vehicleDetalization.getData();
        int pageNumber = vehicleList.getPageable().getPageNumber();
        int pageSize = vehicleList.getPageable().getPageSize();
        int viewCount = pageNumber * pageSize + vehicleList.getNumberOfElements();
        int realPageNumber = pageNumber + 1;
        int stillPageNumber = vehicleList.getTotalPages() - realPageNumber;

        return SendMessage.builder()
                .chatId(chatId)
                .parseMode(ParseMode.HTML)
                .text(getI18n(I18nType.SEARCH_MESSAGE, updateDto.getLang(), viewCount,
                        vehicleList.getTotalElements(), realPageNumber, stillPageNumber))
                .replyMarkup(getInlineKeyboardForNextMessage(updateDto.getLang(), vehicleList))
                .build();
    }

    private InlineKeyboardMarkup getInlineKeyboardForNextMessage(LanguageType lang, Page<VehicleEntity> vehicleList) {

        InlineKeyboardButton viewNextBtn = InlineKeyboardButton.builder()
                .text(getI18n(I18nType.SEARCH_NEXT, lang))
                .callbackData(getCallbackRoute(RouteType.CATALOG_SEARCH_ROUTE) + PAGE_QUERY +
                              (vehicleList.getNumber() + 1) + SIZE_QUERY + searchSize)
                .build();

        InlineKeyboardButton backBtn = InlineKeyboardButton.builder()
                .text(getI18n(I18nType.SEARCH_BACK, lang))
                .callbackData(getCallbackRoute(RouteType.CATALOG_ROUTE))
                .build();

        return InlineKeyboardMarkup.builder()
                .keyboard(List.of(
                        List.of(viewNextBtn),
                        List.of(backBtn)
                ))
                .build();
    }

    private SendMessage getFinishMessage(Long chatId, UpdateDto updateDto) {
        return SendMessage.builder()
                .chatId(chatId)
                .text(getI18n(I18nType.SEARCH_FINISH, updateDto.getLang()))
                .replyMarkup(getInlineKeyboardForFinishMessage(updateDto))
                .build();
    }

    private InlineKeyboardMarkup getInlineKeyboardForFinishMessage(UpdateDto updateDto) {
        InlineKeyboardButton backBtn = InlineKeyboardButton.builder()
                .text(getI18n(I18nType.SEARCH_BACK, updateDto.getLang()))
                .callbackData(getCallbackRoute(RouteType.CATALOG_ROUTE))
                .build();

        return InlineKeyboardMarkup.builder()
                .keyboard(List.of(
                        List.of(backBtn)
                ))
                .build();
    }

    private List<SendPhoto> getItems(UpdateDto updateDto, Long chatId, VehicleDetalizationDto vehicleDetalization) {
        Page<VehicleEntity> vehicleList = vehicleDetalization.getData();
        // выводить правильную инфу

        List<SendPhoto> result = new ArrayList<>();
        if (vehicleList.hasContent()) {
            for (int i = 0; i < vehicleList.getContent().size(); i++) {
                VehicleEntity vehicle = vehicleList.getContent().get(i);

                SendPhoto msg = SendPhoto.builder()
                        .chatId(chatId)
                        .photo(vehicleProvider.getVehicleService().getPicture(vehicle))
                        .parseMode(ParseMode.HTML)
                        .caption(itemDetailsFragment.getItemFullDetails(vehicle, updateDto.getLang()))
                        .replyMarkup(getInlineKeyboardForItem(updateDto, vehicle.getId()))
                        .build();

                result.add(msg);
            }
        }
        return result;
    }

    private InlineKeyboardMarkup getInlineKeyboardForItem(UpdateDto updateDto, Integer id) {
        InlineKeyboardButton order = InlineKeyboardButton.builder()
                .text(getI18n(I18nType.SEARCH_ORDER, updateDto.getLang()))
                .callbackData(getCallbackRoute(RouteType.CATALOG_ORDER_ROUTE) + VEHICLE_ID_QUERY + id)
                .build();

        InlineKeyboardButton orderCarfax = InlineKeyboardButton.builder()
                .text(getI18n(SEARCH_ORDER_CARFAX, updateDto.getLang()))
                .url(getI18n(ABOUT_TELEGRAM_URL, updateDto.getLang()))
                .build();

        InlineKeyboardButton details = InlineKeyboardButton.builder()
                .text(getI18n(I18nType.SEARCH_DETAILS, updateDto.getLang()))
                .callbackData(getCallbackRoute(RouteType.CATALOG_SEARCH_ROUTE) + DETAILS_QUERY + id)
                .build();

        return InlineKeyboardMarkup.builder()
                .keyboard(List.of(
                        List.of(order, orderCarfax),
                        List.of(details)
                ))
                .build();
    }

    private InlineKeyboardMarkup getInlineKeyboardForItemWithoutDetail(LanguageType lang, Integer id) {
        InlineKeyboardButton order = InlineKeyboardButton.builder()
                .text(getI18n(I18nType.SEARCH_ORDER, lang))
                .callbackData(getCallbackRoute(RouteType.CATALOG_ORDER_ROUTE) + VEHICLE_ID_QUERY + id)
                .build();

        InlineKeyboardButton orderCarfax = InlineKeyboardButton.builder()
                .text(getI18n(SEARCH_ORDER_CARFAX, lang))
                .url(getI18n(ABOUT_TELEGRAM_URL, lang))
                .build();

        return InlineKeyboardMarkup.builder()
                .keyboard(List.of(
                        List.of(order),
                        List.of(orderCarfax)
                ))
                .build();
    }

}
