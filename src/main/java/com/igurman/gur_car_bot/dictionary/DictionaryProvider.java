package com.igurman.gur_car_bot.dictionary;

import com.igurman.gur_car_bot.constant.LanguageType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class DictionaryProvider {
    private Map<String, Dictionary> map;

    @Autowired
    private void getDictionary(List<Dictionary> dictionaryList) {
        map = dictionaryList.stream()
                .collect(Collectors.toMap(Dictionary::getDictionaryName, Function.identity()));
    }

    public String get(String dictionaryName, String lang, String key) {
        return map.get(dictionaryName).get(lang, key);
    }

    public String get(String dictionaryName, LanguageType languageType, String key) {
        return map.get(dictionaryName).get(languageType, key);
    }

    public Map<String, String> get(String dictionaryName, LanguageType languageType) {
        return map.get(dictionaryName).getAsMap(languageType);
    }
}
