package com.igurman.gur_car_bot.controller;

import com.igurman.gur_car_bot.service.parser.auctionspark.AuctionSparkFirstPreload;
import com.igurman.gur_car_bot.service.parser.auctionspark.AuctionSparkSecondDetails;
import com.igurman.gur_car_bot.service.parser.auctionspark.AuctionSparkThirdCalc;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ParserController {
    private final AuctionSparkFirstPreload auctionSparkFirstPreload;
    private final AuctionSparkSecondDetails auctionSparkSecondDetails;
    private final AuctionSparkThirdCalc auctionSparkThirdCalc;

    @GetMapping("/parser/spark/preLoad")
    public void searchSpark() {
        auctionSparkFirstPreload.execute();
    }

    @GetMapping("/parser/spark/detailLoad")
    public void getDetalizationSpark() {
        auctionSparkSecondDetails.execute();
    }

    @GetMapping("/parser/spark/calc")
    public void getParseJson() {
        auctionSparkThirdCalc.execute();
    }

}
