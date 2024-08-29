package com.igurman.gur_car_bot.dictionary;

import com.igurman.gur_car_bot.constant.EngineTypeType;
import com.igurman.gur_car_bot.dictionary.common.KeyValueBasedDictionary;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class EngineTypeTypeDictionary extends KeyValueBasedDictionary {

    public static final String DICTIONARY_NAME = "engineType";

    private final List<Map<String, String>> dictionary;

    public EngineTypeTypeDictionary() {
        dictionary = new ArrayList<>();
        addKeyValue(dictionary, EngineTypeType.GAS.code(), "GAS");
        addKeyValue(dictionary, EngineTypeType.HYBRID.code(), "HYBRID");
        addKeyValue(dictionary, EngineTypeType.ELECTRIC.code(), "ELECTRIC");
        addKeyValue(dictionary, EngineTypeType.PLUG_IN.code(), "PLUG_IN");
        addKeyValue(dictionary, EngineTypeType.DIESEL.code(), "DIESEL");
        addKeyValue(dictionary, EngineTypeType.FLEX.code(), "FLEX");
        addKeyValue(dictionary, EngineTypeType.HYDROGEN.code(), "HYDROGEN");
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
