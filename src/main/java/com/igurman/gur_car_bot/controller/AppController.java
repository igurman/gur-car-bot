package com.igurman.gur_car_bot.controller;

import com.igurman.gur_car_bot.service.application.AppService;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AppController {
    private final AppService appService;

    @PostMapping(value = "/v1/file/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void uploadFileV1(
            @RequestHeader(name = "X-API-Key", required = false) String apiKey,
            @Parameter(description = "vin") @RequestParam(name = "vin", required = false) String vin,
            @Parameter(description = "Файл для загрузки", required = true) @RequestParam(name = "filename") MultipartFile fileName) {
        log.info("/v1/file/upload: vin={}, file={}", vin, fileName.getOriginalFilename());

        appService.uploadFile(vin, fileName);
    }

    @GetMapping(value = "/v1/send_data")
    public void sendDataV1(
            @RequestHeader(name = "X-API-Key", required = false) String apiKey,
            @RequestParam String vin,
            @RequestParam Integer mmrPrice,
            @RequestParam Integer marketPrice,
            @RequestParam Integer distance) {
        log.info("/v1/send_data: vin={}, mmrPrice={}, marketPrice={}, distance={}", vin, mmrPrice, marketPrice, distance);

        appService.sendData(vin, mmrPrice, marketPrice, distance);
    }

}