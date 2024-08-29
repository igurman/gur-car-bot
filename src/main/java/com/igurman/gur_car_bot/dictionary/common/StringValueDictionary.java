package com.igurman.gur_car_bot.dictionary.common;

import java.util.List;
import java.util.Map;

public interface StringValueDictionary extends Dictionary<String> {

    @Override
    List<Map<String, String>> getDictionary();
}
