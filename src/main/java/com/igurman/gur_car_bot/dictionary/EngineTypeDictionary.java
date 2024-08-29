package com.igurman.gur_car_bot.dictionary;

import com.igurman.gur_car_bot.constant.EngineType;
import com.igurman.gur_car_bot.constant.LanguageType;
import org.springframework.stereotype.Component;

@Component
public class EngineTypeDictionary extends AbstractDictionary {

    public static final String DICTIONARY_NAME = "engineType";

    EngineTypeDictionary() {
        put(dictionary, LanguageType.RU, EngineType.GAS.name(), "бензин");
        put(dictionary, LanguageType.RU, EngineType.HYBRID.name(), "гибрид");
        put(dictionary, LanguageType.RU, EngineType.ELECTRIC.name(), "электро");
        put(dictionary, LanguageType.RU, EngineType.PLUG_IN.name(), "plug-In");
        put(dictionary, LanguageType.RU, EngineType.DIESEL.name(), "дизель");
        put(dictionary, LanguageType.RU, EngineType.FLEX.name(), "гибкое топливо");
        put(dictionary, LanguageType.RU, EngineType.HYDROGEN.name(), "водородное топливо");
        put(dictionary, LanguageType.RU, EngineType.UNKNOWN.name(), "не определено");
    }

    @Override
    public String getDictionaryName() {
        return DICTIONARY_NAME;
    }
}
