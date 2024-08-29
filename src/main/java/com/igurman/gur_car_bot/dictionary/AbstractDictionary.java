package com.igurman.gur_car_bot.dictionary;

import com.igurman.gur_car_bot.constant.LanguageType;
import org.apache.logging.log4j.util.Strings;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractDictionary implements Dictionary {

    protected final Map<LanguageType, Map<String, String>> dictionary;

    AbstractDictionary() {
        this.dictionary = new EnumMap<>(LanguageType.class);
    }

    protected void put(Map<LanguageType, Map<String, String>> dictionary, LanguageType languageType, String key, String value) {
        dictionary.computeIfPresent(languageType, (a, b) ->{
            b.put(key, value);
            return b;
        });

        Map<String, String> map = new HashMap<>();
        map.put(key, value);
        dictionary.putIfAbsent(languageType, map);
    }

    @Override
    public Map<LanguageType, Map<String, String>> getDictionary() {
        return dictionary;
    }

    @Override
    public Map<String, String> getAsMap(LanguageType languageType) {
        return dictionary.getOrDefault(languageType, Map.of());
    }

    @Override
    public String getByType(LanguageType languageType, String key) {
        return dictionary.getOrDefault(languageType, Map.of()).getOrDefault(key, Strings.EMPTY);
    }

    @Override
    public String get(String lang, String key) {
        LanguageType type = LanguageType.resolve(lang);
        return this.getByType(type, key);
    }

    @Override
    public String get(LanguageType languageType, String key) {
        return this.getByType(languageType, key);
    }

    @Override
    public String getDictionaryName() {
        return null;
    }

}
