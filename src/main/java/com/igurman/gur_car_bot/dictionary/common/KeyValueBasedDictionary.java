package com.igurman.gur_car_bot.dictionary.common;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

public abstract class KeyValueBasedDictionary implements StringValueDictionary {

    public static final String KEY_ATTR = "id";
    public static final String VALUE_ATTR = "name";

    protected void addKeyValue(List<Map<String, String>> dictionary, final String key, final String name) {
        Map<String, String> item = new HashMap<>();
        item.put(KEY_ATTR, key);
        item.put(VALUE_ATTR, name);
        dictionary.add(item);
    }

    public String getValue(final String key) {
        return getDictionaryAsMap().get(key);
    }

    public Map<String, String> getDictionaryAsMap() {
        return getDictionary().stream()
                .collect(toMap(el -> el.get(KEY_ATTR), el -> el.get(VALUE_ATTR)));
    }

    @Override
    public boolean containsCode(String code) {
        if (code == null) {
            return false;
        }

        Map<String, String> dictionary = getDictionaryAsMap();
        return dictionary != null && dictionary.containsKey(code);
    }
}
