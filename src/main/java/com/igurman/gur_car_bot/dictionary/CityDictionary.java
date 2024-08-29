package com.igurman.gur_car_bot.dictionary;

import com.igurman.gur_car_bot.constant.CityType;
import com.igurman.gur_car_bot.dictionary.common.KeyValueBasedDictionary;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class CityDictionary extends KeyValueBasedDictionary {

    public static final String DICTIONARY_NAME = "city";

    private final List<Map<String, String>> dictionary;

    public CityDictionary() {
        dictionary = new ArrayList<>();
        addKeyValue(dictionary, CityType.CITY_1.name(), "Город 1");
        addKeyValue(dictionary, CityType.CITY_2.name(), "Город 2");
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
