package com.igurman.gur_car_bot.dictionary;

import com.igurman.gur_car_bot.constant.ColorType;
import com.igurman.gur_car_bot.constant.LanguageType;
import org.springframework.stereotype.Component;

@Component
public class ColorTypeDictionary extends AbstractDictionary {

    public static final String DICTIONARY_NAME = "color";

    ColorTypeDictionary() {
        put(dictionary, LanguageType.RU, ColorType.BLACK.name(), "черный");
        put(dictionary, LanguageType.RU, ColorType.BLUE.name(), "синий");
        put(dictionary, LanguageType.RU, ColorType.BURGUNDY.name(), "бургундия");
        put(dictionary, LanguageType.RU, ColorType.GOLD.name(), "золотой");
        put(dictionary, LanguageType.RU, ColorType.GRAY.name(), "серый");
        put(dictionary, LanguageType.RU, ColorType.GREEN.name(), "зеленый");
        put(dictionary, LanguageType.RU, ColorType.RED.name(), "красный");
        put(dictionary, LanguageType.RU, ColorType.SILVER.name(), "серебристый");
        put(dictionary, LanguageType.RU, ColorType.WHITE.name(), "белый");
        put(dictionary, LanguageType.RU, ColorType.TEAL.name(), "зеленовато-синий");
        put(dictionary, LanguageType.RU, ColorType.TAN.name(), "оттенок коричневого");
        put(dictionary, LanguageType.RU, ColorType.ORANGE.name(), "оранжевый");
        put(dictionary, LanguageType.RU, ColorType.UNKNOWN.name(), "не определен");
    }

    @Override
    public String getDictionaryName() {
        return DICTIONARY_NAME;
    }
}
