package com.igurman.gur_car_bot.controller;

import com.igurman.gur_car_bot.service.publisher.DeletePostGroupService;
import com.igurman.gur_car_bot.service.publisher.SeedTableGroupService;
import com.igurman.gur_car_bot.service.publisher.SendPostGroupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/publisher")
@RequiredArgsConstructor
public class PublisherController {
    private final SeedTableGroupService seedTableGroupService;
    private final SendPostGroupService sendPostGroupService;
    private final DeletePostGroupService deletePostGroupService;

    @GetMapping("/seed")
    public void seed() {
        seedTableGroupService.execute();
    }

    @GetMapping("/send")
    public void send() {
        sendPostGroupService.execute();
    }

    @GetMapping("/delete")
    public void delete() {
        deletePostGroupService.execute();
    }
}
