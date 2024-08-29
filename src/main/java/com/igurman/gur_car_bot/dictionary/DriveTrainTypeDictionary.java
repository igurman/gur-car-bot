package com.igurman.gur_car_bot.dictionary;

import com.igurman.gur_car_bot.constant.DriveTrainType;
import com.igurman.gur_car_bot.constant.LanguageType;
import org.springframework.stereotype.Component;

@Component
public class DriveTrainTypeDictionary extends AbstractDictionary {
    public static final String DICTIONARY_NAME = "driveTrain";

    DriveTrainTypeDictionary() {
        put(dictionary, LanguageType.RU, DriveTrainType.FOUR_WD.name(), "4х4");
        put(dictionary, LanguageType.RU, DriveTrainType.RWD.name(), "задний привод");
        put(dictionary, LanguageType.RU, DriveTrainType.FWD.name(), "передний привод");
        put(dictionary, LanguageType.RU, DriveTrainType.AWD.name(), "AWD");
        put(dictionary, LanguageType.RU, DriveTrainType.UNKNOWN.name(), "не определен");
    }

    @Override
    public String getDictionaryName() {
        return DICTIONARY_NAME;
    }
}
