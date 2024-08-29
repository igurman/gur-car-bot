package com.igurman.gur_car_bot.service.telegram.handler.fragment;

import com.igurman.gur_car_bot.constant.LanguageType;
import com.igurman.gur_car_bot.dictionary.ColorTypeDictionary;
import com.igurman.gur_car_bot.dictionary.DictionaryProvider;
import com.igurman.gur_car_bot.dictionary.DriveTrainTypeDictionary;
import com.igurman.gur_car_bot.dictionary.EngineTypeDictionary;
import com.igurman.gur_car_bot.dictionary.TransmissionTypeDictionary;
import com.igurman.gur_car_bot.model.entity.VehicleEntity;
import com.igurman.gur_car_bot.util.FormatUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.igurman.gur_car_bot.dictionary.I18nDictionary.SPACE;

@Component
@RequiredArgsConstructor
public class ItemDetailsFragment {
    private final DictionaryProvider dictionary;
    private static final DateTimeFormatter DT = DateTimeFormatter.ofPattern("d MMMM uuuu");

    public String getItemDetails(VehicleEntity vehicleEntity, LanguageType languageType) {
        String result = cleanString(vehicleEntity.getMakeTitle()) + " " + cleanString(vehicleEntity.getModelTitle());

        if (StringUtils.isNotBlank(vehicleEntity.getComplect())) {
            result += " " + cleanString(vehicleEntity.getComplect());
        }

        if (vehicleEntity.getGrade() != null) {
            result += " Grade: " + cleanGrade(vehicleEntity.getGrade());
        }
        result += "\n\n";

        result += "<b>Год:</b> " + cleanInteger(vehicleEntity.getYear()) + "\n";
        result += "<b>Пробег:</b> " + FormatUtil.formatIntegerOrElse(vehicleEntity.getOdometer(), SPACE) + "\n";
        result += "<b>VIN:</b> " + cleanString(vehicleEntity.getVin()) + "\n";
        result += "<b>Двигатель:</b> " + cleanString(vehicleEntity.getEngine()) + " " + getEngineType(vehicleEntity, languageType) + "\n";
        result += "<b>Трансмиссия:</b> " + getTransmission(vehicleEntity, languageType) + "\n";
        result += "<b>Дата продажи:</b> " + cleanData(vehicleEntity.getSaleDate());

        if (vehicleEntity.getPrice() != null && vehicleEntity.getPrice() != 0) {
            result += "\n\n💰 <b>Ставка (примерная):</b> " + FormatUtil.formatPriceOrElse(vehicleEntity.getPrice(), SPACE);
        }

        return result;
    }

    public String getItemFullDetails(VehicleEntity vehicleEntity, LanguageType languageType) {
        return "<b>Производитель:</b> " + cleanString(vehicleEntity.getMakeTitle()) + "\n" +
               "<b>Модель:</b> " + cleanString(vehicleEntity.getModelTitle()) + "\n" +
               "<b>Комплектация:</b> " + cleanString(vehicleEntity.getComplect()) + "\n" +
               "<b>Цвет:</b> " + getColor(vehicleEntity, languageType) + "\n" +
               "<b>Цвет интерьера:</b> " + cleanString(vehicleEntity.getInteriorColor()) + "\n" +
               "<b>Год:</b> " + cleanInteger(vehicleEntity.getYear()) + "\n" +
               "<b>Пробег:</b> " + FormatUtil.formatIntegerOrElse(vehicleEntity.getOdometer(), SPACE) + "\n" +
               "<b>VIN:</b> " + cleanString(vehicleEntity.getVin()) + "\n" +
               "<b>Место:</b> " + cleanString(vehicleEntity.getLocation()) + "\n" +
               "<b>Линия:</b> " + cleanString(vehicleEntity.getLane()) + "\n" +
               "<b>Продавец:</b> " + cleanString(vehicleEntity.getSeller()) + "\n" +
               "<b>Двигатель:</b> " + cleanString(vehicleEntity.getEngine()) + "\n" +
               "<b>Тип двигателя:</b> " + getEngineType(vehicleEntity, languageType) + "\n" +
               "<b>Трансмиссия:</b> " + getTransmission(vehicleEntity, languageType) + "\n" +
               "<b>Привод:</b> " + getDriveTrain(vehicleEntity, languageType) + "\n" +
               "<b>Ставка (примерная):</b> " + FormatUtil.formatPriceOrElse(vehicleEntity.getPrice(), SPACE) + "\n" +
               "<b>Описание:</b> " + cleanString(vehicleEntity.getAnnouncement()) + "\n" +
               "<b>Состояние:</b> " + cleanString(vehicleEntity.getTitle()) + "\n" +
               "<b>Оценка:</b> " + cleanGrade(vehicleEntity.getGrade()) + "\n" +
               "<b>Передвигается:</b> " + cleanString(vehicleEntity.getDriveable()) + "\n" +
               "<b>Дата продажи:</b> " + cleanData(vehicleEntity.getSaleDate()) + "\n";
    }

    private String getColor(VehicleEntity vehicleEntity, LanguageType languageType) {
        return cleanString(dictionary.get(ColorTypeDictionary.DICTIONARY_NAME, languageType, vehicleEntity.getColor().name()));
    }

    private String getTransmission(VehicleEntity vehicleEntity, LanguageType languageType) {
        return cleanString(dictionary.get(TransmissionTypeDictionary.DICTIONARY_NAME, languageType, vehicleEntity.getTransmission().name()));
    }

    private String getDriveTrain(VehicleEntity vehicleEntity, LanguageType languageType) {
        return cleanString(dictionary.get(DriveTrainTypeDictionary.DICTIONARY_NAME, languageType, vehicleEntity.getDriveTrain().name()));
    }

    private String getEngineType(VehicleEntity vehicleEntity, LanguageType languageType) {
        return cleanString(dictionary.get(EngineTypeDictionary.DICTIONARY_NAME, languageType, vehicleEntity.getEngineType().name()));
    }

    private String cleanString(String value) {
        return value != null
                ? value
                : SPACE;
    }

    private String cleanInteger(Integer value) {
        return value != null
                ? value.toString()
                : SPACE;
    }

    private String cleanData(LocalDate value) {
        return value != null
                ? DT.format(value)
                : SPACE;
    }

    private String cleanGrade(Integer value) {
        if (value != null) {
            int data = value == 1 ? 10 : value;
            return String.valueOf((float) data / 10);
        }
        return SPACE;
    }
}
