package com.igurman.gur_car_bot.scheduler;

import com.igurman.gur_car_bot.service.publisher.DeletePostGroupService;
import com.igurman.gur_car_bot.service.publisher.SeedTableGroupService;
import com.igurman.gur_car_bot.service.publisher.SendPostGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Класс для запуска генерации/публикации/удаления постов через промежутки времени
 */
@Component
@RequiredArgsConstructor
public class PublisherScheduler {
    private final SeedTableGroupService seedTableGroupService;
    private final SendPostGroupService sendPostGroupService;
    private final DeletePostGroupService deletePostGroupService;
    @Value("${application.publisher.seed.enabled:false}")
    private boolean seedEnabled;
    @Value("${application.publisher.post.enabled:false}")
    private boolean postEnabled;
    @Value("${application.publisher.delete.enabled:false}")
    private boolean deleteEnabled;

    @Scheduled(fixedDelayString = "${application.publisher.seed.delay}", initialDelayString = "${application.publisher.seed.delay}",
            timeUnit = TimeUnit.MINUTES)
    public void seedTableGroupServiceExecute() {
        if (seedEnabled) {
            seedTableGroupService.execute();
        }
    }

    @Scheduled(fixedDelayString = "${application.publisher.post.delay}", initialDelayString = "${application.publisher.post.delay}",
            timeUnit = TimeUnit.MINUTES)
    public void sendPostGroupServiceExecute() {
        if (postEnabled) {
            sendPostGroupService.execute();
        }
    }

    @Scheduled(fixedDelayString = "${application.publisher.delete.delay}", initialDelayString = "${application.publisher.delete.delay}", timeUnit = TimeUnit.MINUTES)
    public void deleteGroupServiceExecute() {
        if (deleteEnabled) {
            deletePostGroupService.execute();
        }
    }

}
