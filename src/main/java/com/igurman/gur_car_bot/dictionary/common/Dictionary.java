package com.igurman.gur_car_bot.dictionary.common;

import java.util.List;
import java.util.Map;

public interface Dictionary<T> {
    String getDictionaryName();

    List<Map<String, T>> getDictionary();

    boolean containsCode(String code);
}
