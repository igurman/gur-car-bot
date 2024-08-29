package com.igurman.gur_car_bot.dictionary;

import com.igurman.gur_car_bot.constant.AuctionType;
import com.igurman.gur_car_bot.constant.LanguageType;
import org.springframework.stereotype.Component;

@Component
public class AuctionTypeDictionary extends AbstractDictionary {

    private static final String DICTIONARY_NAME = "auction";

    AuctionTypeDictionary() {
        put(dictionary, LanguageType.RU, AuctionType.AUCTION_WIN.name(), "Win");
        put(dictionary, LanguageType.RU, AuctionType.AUCTION_SPARK.name(), "Spark");
    }

    @Override
    public String getDictionaryName() {
        return DICTIONARY_NAME;
    }
}
