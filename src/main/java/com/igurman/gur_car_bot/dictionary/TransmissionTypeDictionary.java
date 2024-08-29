package com.igurman.gur_car_bot.dictionary;

import com.igurman.gur_car_bot.constant.LanguageType;
import com.igurman.gur_car_bot.constant.TransmissionType;
import org.springframework.stereotype.Component;

@Component
public class TransmissionTypeDictionary extends AbstractDictionary {
    public static final String DICTIONARY_NAME = "transmissionType";

    TransmissionTypeDictionary() {
        put(dictionary, LanguageType.RU, TransmissionType.AUTOMATIC.name(), "автоматическая");
        put(dictionary, LanguageType.RU, TransmissionType.MANUAL.name(), "механическая");
        put(dictionary, LanguageType.RU, TransmissionType.STEPTRONIC.name(), "steptronic");
        put(dictionary, LanguageType.RU, TransmissionType.UNKNOWN.name(), "не определено");
    }

    @Override
    public String getDictionaryName() {
        return DICTIONARY_NAME;
    }
}
