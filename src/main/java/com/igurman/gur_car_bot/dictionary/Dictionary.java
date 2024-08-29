package com.igurman.gur_car_bot.dictionary;

import com.igurman.gur_car_bot.constant.LanguageType;

import java.util.Map;

public interface Dictionary {
    String getDictionaryName();
    Map<LanguageType, Map<String, String>> getDictionary();
    Map<String, String> getAsMap(LanguageType languageType);
    String getByType(LanguageType languageType, String key);
    String get(LanguageType languageType, String key);
    String get(String lang, String key);
}
