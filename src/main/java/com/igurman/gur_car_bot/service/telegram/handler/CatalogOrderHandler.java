package com.igurman.gur_car_bot.service.telegram.handler;

import com.igurman.gur_car_bot.constant.LanguageType;
import com.igurman.gur_car_bot.constant.RouteType;
import com.igurman.gur_car_bot.dictionary.EngineTypeTypeDictionary;
import com.igurman.gur_car_bot.model.dto.UpdateDto;
import com.igurman.gur_car_bot.model.entity.VehicleEntity;
import com.igurman.gur_car_bot.service.common.MessageService;
import com.igurman.gur_car_bot.service.common.VehicleService;
import com.igurman.gur_car_bot.service.provider.FilterProvider;
import com.igurman.gur_car_bot.util.button.StubCallback;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendSticker;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

import static com.igurman.gur_car_bot.constant.I18nType.CATALOG_BACK;
import static com.igurman.gur_car_bot.constant.I18nType.SEARCH_ORDER_MESSAGE_FAILURE;
import static com.igurman.gur_car_bot.constant.I18nType.SEARCH_ORDER_MESSAGE_SUCCESS;
import static com.igurman.gur_car_bot.constant.RouteDictionary.getCallbackRoute;
import static com.igurman.gur_car_bot.constant.RouteType.CATALOG_ROUTE;
import static com.igurman.gur_car_bot.dictionary.I18nDictionary.getI18n;

/**
 * Обработчик страницы "Заказ авто"
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CatalogOrderHandler implements CommandHandler {
    public final FilterProvider filterProvider;
    public final EngineTypeTypeDictionary engineTypeTypeDictionary;
    private final VehicleService vehicleService;
    private final MessageService messageService;

    private static final String VEHICLE_ID = "vehicle-id";
    private static final String NEW_MESSAGE = "?message=1";
    private static final String NEW_ORDER_MESSAGE = "Новый заказ от пользователя id: %s, login: %s, авто: %s, vin: %s";


    @Override
    public boolean canExecute(UpdateDto updateDto) {
        return RouteType.CATALOG_ORDER_ROUTE.equals(updateDto.getRoute());
    }

    @Override
    public List<Object> executeList(UpdateDto updateDto) {
        log.info("*** CatalogOrderHandler, chatId: {}, userName: {}", updateDto.getChatId(), updateDto.getFirstName());

        SendSticker sendSticker = SendSticker.builder()
                .chatId(updateDto.getChatId())
                .sticker(new InputFile("CAACAgIAAxkBAAIGtWZ3zG_eyvqpNdKUYW8HW1KyNEK4AAJLBwACRvusBJjCZeijaQ8uNQQ"))
                .build();

        SendMessage sendMessage = SendMessage.builder()
                .chatId(updateDto.getChatId())
                .replyMarkup(getInlineKeyboard(updateDto.getLang()))
                .parseMode(ParseMode.HTML)
                .text(getMessage(updateDto))
                .build();

        return List.of(StubCallback.answerStop(updateDto.getUpdate().getCallbackQuery().getId()),
                sendSticker,
                sendMessage);
    }

    protected String getMessage(UpdateDto updateDto) {
        int vehicleId = updateDto.getQueryParams().getOrDefault(VEHICLE_ID, 0);
        VehicleEntity vehicleEntity = vehicleService.findById(vehicleId);
        if (vehicleEntity != null) {
            messageService.saveOrderMessage(updateDto.getUser().getId(),
                    String.format(NEW_ORDER_MESSAGE, updateDto.getUser().getId(), updateDto.getUser().getUserName(), vehicleEntity.getId(), vehicleEntity.getVin()));
            return getI18n(SEARCH_ORDER_MESSAGE_SUCCESS, updateDto.getLang(), vehicleEntity.getMakeTitle(), vehicleEntity.getModelTitle(), vehicleEntity.getVin());
        }
        return getI18n(SEARCH_ORDER_MESSAGE_FAILURE, updateDto.getLang());
    }

    private InlineKeyboardMarkup getInlineKeyboard(LanguageType lang) {

        InlineKeyboardButton backBtn = InlineKeyboardButton.builder()
                .text(getI18n(CATALOG_BACK, lang))
                .callbackData(getCallbackRoute(CATALOG_ROUTE) + NEW_MESSAGE)
                .build();

        return InlineKeyboardMarkup.builder()
                .keyboard(List.of(List.of(backBtn)))
                .build();
    }

}
