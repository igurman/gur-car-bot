package com.igurman.gur_car_bot.service.publisher;

import com.igurman.gur_car_bot.constant.LanguageType;
import com.igurman.gur_car_bot.constant.PosterStatusType;
import com.igurman.gur_car_bot.constant.VehicleStatusType;
import com.igurman.gur_car_bot.exception.ParserRuntimeException;
import com.igurman.gur_car_bot.model.entity.PictureEntity;
import com.igurman.gur_car_bot.model.entity.PostEntity;
import com.igurman.gur_car_bot.model.entity.VehicleEntity;
import com.igurman.gur_car_bot.service.common.PictureService;
import com.igurman.gur_car_bot.service.common.PostService;
import com.igurman.gur_car_bot.service.common.VehicleService;
import com.igurman.gur_car_bot.service.telegram.TelegramBotService;
import com.igurman.gur_car_bot.service.telegram.handler.fragment.ItemDetailsFragment;
import com.igurman.gur_car_bot.util.UsefulUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMediaGroup;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.media.InputMedia;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaPhoto;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.igurman.gur_car_bot.util.button.StubCallback.getInlineKeyboardForItem;

@Slf4j
@Component
@RequiredArgsConstructor
public class SendPostGroupService {
    private final TelegramBotService telegramBotService;
    private final PostService postService;
    private final VehicleService vehicleService;
    private final PictureService pictureService;
    private final ItemDetailsFragment itemDetailsFragment;
    @Value("${application.telegram.group.sent-post.count-posts}")
    private Integer countPosts;
    private static final LanguageType GROUP_POST_LANG = LanguageType.RU;

    public void execute() {
        log.info("*** Запуск отправителя сообщений для группы ТГ");

        List<PostEntity> postEntityList = postService.findForGroupPost(countPosts);
        log.info("*** Получили {} записей для постинга в группу ТГ", postEntityList.size());

        if (postEntityList.isEmpty()) {
            return;
        }

        for (PostEntity postEntity : postEntityList) {

            VehicleEntity vehicleEntity = vehicleService.findById(postEntity.getVehicleId());
            List<PictureEntity> pictureEntityList = pictureService.findByVehicleId(postEntity.getVehicleId());
            log.info("*** Для авто id: {}, нашли картинок: {}шт", vehicleEntity.getId(), pictureEntityList.size());

            // соберём 7 картинок в лист
            List<InputMedia> pictureList = pictureEntityList.stream()
                    .map(a -> {
                        InputMedia inputMedia = new InputMediaPhoto();
                        inputMedia.setMedia(a.getLink());
                        return inputMedia;
                    })
                    .limit(7)
                    .collect(Collectors.toCollection(ArrayList::new));

            if (pictureList.size() < 2) {
                //postOneMessage(postEntity, vehicleEntity, 0);

                vehicleEntity.setStatus(VehicleStatusType.POSTED);
                vehicleService.save(vehicleEntity);
                log.info("*** Не постим авто id: {}, картинок: {}шт", vehicleEntity.getId(), pictureEntityList.size());
            } else {
                postGroup(pictureList, postEntity, vehicleEntity, 0);
            }
        }
        log.info("*** Закончили цикл отправки сообщений для группы ТГ");
    }

    private void postGroup(List<InputMedia> pictureList, PostEntity postEntity, VehicleEntity vehicleEntity, int i) {
        try {
            SendMediaGroup sendMediaGroup = SendMediaGroup.builder()
                    .chatId(postEntity.getGroupId())
                    .messageThreadId(postEntity.getGroupChannelId())
                    .medias(pictureList)
                    .build();

            List<Message> mediaResult = telegramBotService.execute(sendMediaGroup);

            List<Integer> collect = mediaResult.stream()
                    .map(Message::getMessageId)
                    .toList();

            SendMessage sendMessage = SendMessage.builder()
                    .chatId(postEntity.getGroupId())
                    .messageThreadId(postEntity.getGroupChannelId())
                    .parseMode(ParseMode.HTML)
                    .text(itemDetailsFragment.getItemFullDetails(vehicleEntity, GROUP_POST_LANG))
                    .replyMarkup(getInlineKeyboardForItem(GROUP_POST_LANG))
                    .build();

            Message messageResult = telegramBotService.execute(sendMessage);

            List<Integer> postIds = new ArrayList<>(collect);
            postIds.add(messageResult.getMessageId());

            // сохраним результаты постов в БД
            postEntity.setPostList(postIds);
            postEntity.setStatus(PosterStatusType.POSTED);
            postService.save(postEntity);

            vehicleEntity.setStatus(VehicleStatusType.POSTED);
            vehicleService.save(vehicleEntity);
            log.info("*** Сохранили результат отправки поста в БД SendMediaGroup {}", postEntity.getId());
        } catch (TelegramApiRequestException e) {
            if (e.getErrorCode() == 400 && "Bad Request: chat not found".equals(e.getApiResponse())) {
                throw new ParserRuntimeException("*** Группа не найдена с id: " + postEntity.getGroupId());
            }
            int freezeSec = UsefulUtil.calcFreeze(e);
            if (freezeSec > 0 && i < 5) {
                try {
                    log.error("*** Заснули SendMediaGroup на {}ms, id: {}", freezeSec, postEntity.getId());
                    Thread.sleep(freezeSec);
                } catch (InterruptedException ex) {
                    log.error("*** Упали SendPhoto когда проснулись", ex);
                    Thread.currentThread().interrupt();
                }
                log.error("*** Проснулись SendMediaGroup после {}ms, id: {}", freezeSec, postEntity.getId());
                i = i + 1;
                postGroup(pictureList, postEntity, vehicleEntity, i);
            } else {
                log.error("*** Не смог отправить пост SendMediaGroup {}, {}", postEntity, pictureList, e);
            }
        } catch (TelegramApiException e) {
            log.error("*** Не смог отправить SendMediaGroup TelegramApiException в группу {}, {}", postEntity, pictureList, e);
        }
    }

    private void postOneMessage(PostEntity postEntity, VehicleEntity vehicleEntity, int i) {
        try {
            SendPhoto sendPhoto = SendPhoto.builder()
                    .chatId(postEntity.getGroupId())
                    .messageThreadId(postEntity.getGroupChannelId())
                    .photo(vehicleService.getPicture(vehicleEntity))
                    .parseMode("HTML")
                    .caption(itemDetailsFragment.getItemFullDetails(vehicleEntity, GROUP_POST_LANG))
                    .replyMarkup(getInlineKeyboardForItem(GROUP_POST_LANG))
                    .build();

            Message responseMessage = telegramBotService.execute(sendPhoto);

            // сохраним результаты постов в БД
            postEntity.setPostList(List.of(responseMessage.getMessageId()));
            postEntity.setStatus(PosterStatusType.POSTED);
            postService.save(postEntity);

            vehicleEntity.setStatus(VehicleStatusType.POSTED);
            vehicleService.save(vehicleEntity);
            log.info("*** Сохранили результат отправки поста в БД SendPhoto {}", postEntity.getId());
        } catch (TelegramApiRequestException e) {
            int freezeSec = UsefulUtil.calcFreeze(e);
            if (freezeSec > 0 && i < 5) {
                try {
                    log.error("*** Заснули SendPhoto на {}ms, id: {}", freezeSec, postEntity.getId());
                    Thread.sleep(freezeSec);
                } catch (InterruptedException ex) {
                    log.error("*** Упали SendPhoto когда проснулись", ex);
                    Thread.currentThread().interrupt();
                }
                log.error("*** Проснулись SendPhoto после {}ms, id: {}", freezeSec, postEntity.getId());
                i = i + 1;
                postOneMessage(postEntity, vehicleEntity, i);
            } else {
                log.error("*** Не смог отправить пост SendPhoto ", e);
            }
        } catch (TelegramApiException e) {
            log.error("*** Не смог отправить SendPhoto TelegramApiException в группу {}", postEntity);
        }
    }
}
