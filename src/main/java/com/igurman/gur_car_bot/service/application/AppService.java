package com.igurman.gur_car_bot.service.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

@Slf4j
@Component
@RequiredArgsConstructor
public class AppService {
    @Value("${application.app.files-path}")
    private String PATH_FOLDER;

    public void uploadFile(String vin, MultipartFile multipartFile) {

        File file = new File(PATH_FOLDER + multipartFile.getOriginalFilename());

        try (OutputStream os = new FileOutputStream(file)) {
            os.write(multipartFile.getBytes());
        } catch (Exception e) {
            log.info("Ошибка сохранения файла: {}, ", multipartFile.getOriginalFilename(), e);
        }
    }

    public void sendData(String vin, Integer mmrPrice, Integer marketPrice, Integer distance) {
        log.info("Запрос на сохранение данных: mmrPrice={}, marketPrice={}, distance={}", mmrPrice, marketPrice, distance);
        validateData(vin, mmrPrice, marketPrice, distance);
        // todo: подумать о создании записи о авто по первичным данным
    }

    private void validateData(String vin, Integer mmrPrice, Integer marketPrice, Integer distance) {
        if (StringUtils.isEmpty(vin) || mmrPrice == null || marketPrice == null || distance == null) {
            throw new RuntimeException("Ошибка валидации данных, заполните все поля");
        }
    }
}
