package com.igurman.gur_car_bot.service.publisher;

import com.igurman.gur_car_bot.constant.PosterStatusType;
import com.igurman.gur_car_bot.constant.TimerType;
import com.igurman.gur_car_bot.model.entity.PostEntity;
import com.igurman.gur_car_bot.service.common.PostService;
import com.igurman.gur_car_bot.service.telegram.TelegramBotService;
import com.igurman.gur_car_bot.util.TimerUtil;
import com.igurman.gur_car_bot.util.UsefulUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessages;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeletePostGroupService {
    private final PostService postService;
    private final TelegramBotService telegramBotService;
    @Value("${application.telegram.group.delete-post.count-posts}")
    private Integer countPosts;

    public void execute() {
        log.info("*** Запуск удалителя постов из группы ТГ");

        List<PostEntity> postEntityList = postService.findForGroupDelete(countPosts);
        log.info("*** Получили {} записей для удаления из группы ТГ", postEntityList.size());

        if (postEntityList.isEmpty()) {
            return;
        }

        for (PostEntity postEntity : postEntityList) {
            deleteByOne(postEntity, 0);
        }

        log.info("*** Закончили цикл удаления из группы ТГ");
    }

    private void deleteByOne(PostEntity postEntity, int i) {
        Integer last = postEntity.getPostList().getLast();
        log.info("*** Пробую удалить сообщение  postEntity: {}, id: {} ", postEntity, last);

        EditMessageText editMessageText = EditMessageText.builder()
                .chatId(postEntity.getGroupId())
                .messageId(last)
                .text("delete")
                .build();
        try {
            TimerUtil.getPause(TimerType.GROUP_SEND);
            telegramBotService.execute(editMessageText);
            postEntity.setStatus(PosterStatusType.DELETED);
        } catch (TelegramApiRequestException e) {
            int freezeSec = UsefulUtil.calcFreeze(e);
            if (freezeSec > 0 && i < 5) {
                try {
                    log.error("*** Заснули delete на {}ms, id: {}", freezeSec, postEntity.getId());
                    Thread.sleep(freezeSec);
                } catch (InterruptedException ex) {
                    log.error("*** Упали delete когда проснулись", ex);
                    Thread.currentThread().interrupt();
                }
                log.error("*** Проснулись delete после {}ms, post: {}, id: {}", freezeSec, postEntity, last);
                i = i + 1;
                deleteByOne(postEntity, i);
            } else {
                log.error("*** Не смог удалить пост delete {}, id: {}", postEntity, last, e);
                postEntity.setStatus(PosterStatusType.DELETED_ERROR);
            }
        } catch (TelegramApiException e) {
            log.error("*** Не смог удалить delete TelegramApiException в группу {}, id: {} ", postEntity, last, e);
            postEntity.setStatus(PosterStatusType.DELETED_ERROR);
        }
//        } catch (TelegramApiException e) {
//            log.error("*** Не смог удалить из группы ТГ: {}", postEntity, e);
//            postEntity.setStatus(PosterStatusType.DELETED_ERROR);
//        }
        // пока костыль, подумать как удалять sendPhoto
        if (postEntity.getPostList().size() == 1) {
            postEntity.setStatus(PosterStatusType.DELETED);
        }
        postService.save(postEntity);
    }

    private void delete() {
        log.info("*** Запуск удалителя постов из группы ТГ");

        List<PostEntity> postEntityList = postService.findForGroupDelete(countPosts);
        log.info("*** Получили {} записей для удаления из группы ТГ", postEntityList.size());

        if (postEntityList.isEmpty()) {
            return;
        }

        for (PostEntity postEntity : postEntityList) {

            DeleteMessages deleteMessage = DeleteMessages.builder()
                    .chatId(postEntity.getGroupId())
                    .messageIds(postEntity.getPostList())
                    .build();
            try {
                TimerUtil.getPause(TimerType.GROUP_SEND);
                boolean isDelete = telegramBotService.execute(deleteMessage);
                if (isDelete) {
                    postEntity.setStatus(PosterStatusType.DELETED);
                } else {
                    postEntity.setStatus(PosterStatusType.DELETED_ERROR);
                }
            } catch (TelegramApiException e) {
                log.error("*** Не смог удалить из группы ТГ: {}", postEntity, e);
                postEntity.setStatus(PosterStatusType.DELETED_ERROR);
            }

            postService.save(postEntity);
        }
        log.info("*** Закончили цикл удаления из группы ТГ");
    }
}
