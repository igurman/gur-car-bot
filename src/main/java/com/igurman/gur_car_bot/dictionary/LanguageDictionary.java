package com.igurman.gur_car_bot.dictionary;

import com.igurman.gur_car_bot.constant.LanguageType;
import com.igurman.gur_car_bot.dictionary.common.KeyValueBasedDictionary;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class LanguageDictionary extends KeyValueBasedDictionary {

    public static final String DICTIONARY_NAME = "language";

    private final List<Map<String, String>> dictionary;

    public LanguageDictionary() {
        dictionary = new ArrayList<>();
        addKeyValue(dictionary, LanguageType.RU.code(), "Русский");
        addKeyValue(dictionary, LanguageType.EN.code(), "English");
        addKeyValue(dictionary, LanguageType.ES.code(), "Español");
    }


    @Override
    public List<Map<String, String>> getDictionary() {
        return dictionary;
    }

    @Override
    public String getDictionaryName() {
        return DICTIONARY_NAME;
    }

}
