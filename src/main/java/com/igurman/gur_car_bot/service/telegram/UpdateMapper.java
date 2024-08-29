package com.igurman.gur_car_bot.service.telegram;

import com.igurman.gur_car_bot.constant.LanguageType;
import com.igurman.gur_car_bot.constant.MessageType;
import com.igurman.gur_car_bot.constant.RouteDictionary;
import com.igurman.gur_car_bot.constant.RouteType;
import com.igurman.gur_car_bot.constant.StatusUserType;
import com.igurman.gur_car_bot.constant.UserType;
import com.igurman.gur_car_bot.model.dto.UpdateDto;
import com.igurman.gur_car_bot.model.entity.FilterEntity;
import com.igurman.gur_car_bot.model.entity.UserEntity;
import com.igurman.gur_car_bot.service.common.FilterService;
import com.igurman.gur_car_bot.service.common.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.Optional;

import static com.igurman.gur_car_bot.util.UpdateUtil.getMessageType;
import static com.igurman.gur_car_bot.util.UpdateUtil.getPath;
import static com.igurman.gur_car_bot.util.UpdateUtil.getQueryParams;
import static com.igurman.gur_car_bot.util.UpdateUtil.getUserFrom;
import static com.igurman.gur_car_bot.util.UpdateUtil.getUserId;
import static com.igurman.gur_car_bot.util.UpdateUtil.isCallbackQuery;
import static com.igurman.gur_car_bot.util.UpdateUtil.isMessage;

@Slf4j
@Component
@RequiredArgsConstructor
public class UpdateMapper {
    private final UserService userService;
    private final FilterService filterService;

    public UpdateDto toDto(Update update) {
        UpdateDto updateDto = UpdateDto.builder()
                .update(update)
                .chatId(this.getChatId(update))
                .messageId(this.getMessageId(update))
                .user(this.getUserOrCreateNew(update))
                .filter(this.getFilter(update))
                .queryParams(getQueryParams(update))
                .messageType(getMessageType(update))
                .path(getPath(update))
                .route(this.getRoute(update))
                .build();

        updateDto.execSettingPatch();
        return updateDto;
    }

    private Integer getMessageId(Update update) {
        if (MessageType.MESSAGE.equals(getMessageType(update))) {
            return update.getMessage().getMessageId();
        } else if (MessageType.CALLBACK_QUERY.equals(getMessageType(update))) {
            return update.getCallbackQuery().getMessage().getMessageId();
        }
        return 0;
    }

    private Long getChatId(Update update) {
        if (MessageType.MESSAGE.equals(getMessageType(update))) {
            return update.getMessage().getChatId();
        } else if (MessageType.CALLBACK_QUERY.equals(getMessageType(update))) {
            return update.getCallbackQuery().getMessage().getChatId();
        }
        return 0L;
    }

    private FilterEntity getFilter(Update update) {
        Long userId = getUserId(update);
        return filterService.findFilterByUserId(userId)
                .orElse(new FilterEntity());
    }

    private UserEntity getUserOrCreateNew(Update update) {
        Optional<UserEntity> userEntity = userService.findById(getUserId(update));
        return userEntity.orElseGet(() -> this.createNewUser(update));
    }

    private UserEntity createNewUser(Update update) {
        User cameUser = getUserFrom(update);

        UserEntity newUser = UserEntity.builder()
                .id(cameUser.getId())
                .firstName(cameUser.getFirstName())
                .userName(cameUser.getUserName())
                .isBot(cameUser.getIsBot())
                .type(UserType.USER)
                .status(StatusUserType.READ)
                .language(LanguageType.RU.code())
//                    .language(LanguageType.resolve(cameUser.getLanguageCode()).code())
                .activity(true)
                .build();
        return userService.save(newUser);
    }

    private RouteType getRoute(Update update) {
        RouteType route = null;
        if (isMessage(update)) {
            route = RouteDictionary.getRouteByMessage(update.getMessage().getText());
        } else if (isCallbackQuery(update)) {
            route = RouteDictionary.getRouteByCallback(update.getCallbackQuery().getData());
        }
        log.info("*** Найден роут: {}", route);
        return route;
    }

}
