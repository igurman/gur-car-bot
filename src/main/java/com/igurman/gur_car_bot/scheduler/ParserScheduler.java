package com.igurman.gur_car_bot.scheduler;

import com.igurman.gur_car_bot.service.parser.auctionspark.AuctionSparkFirstPreload;
import com.igurman.gur_car_bot.service.parser.auctionspark.AuctionSparkSecondDetails;
import com.igurman.gur_car_bot.service.parser.auctionspark.AuctionSparkThirdCalc;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Класс для запуска парсеров сайта spark.com через промежутки времени
 */
@Component
@RequiredArgsConstructor
public class ParserScheduler {
    private final AuctionSparkFirstPreload auctionSparkFirstPreload;
    private final AuctionSparkSecondDetails auctionSparkSecondDetails;
    private final AuctionSparkThirdCalc auctionSparkThirdCalc;
    @Value("${application.parser.auctionspark.seed.enabled:false}")
    private boolean seedEnabled;
    @Value("${application.parser.auctionspark.details.enabled:false}")
    private boolean detailsEnabled;
    @Value("${application.parser.auctionspark.calculation.enabled:false}")
    private boolean calculationEnabled;

    @Scheduled(fixedDelayString = "${application.parser.auctionspark.seed.delay}",
            initialDelayString = "${application.parser.auctionspark.seed.delay}", timeUnit = TimeUnit.MINUTES)
    public void sparkSourceSearchParseExecute() {
        if (seedEnabled) {
            auctionSparkFirstPreload.execute();
        }
    }

    @Scheduled(fixedDelayString = "${application.parser.auctionspark.details.delay}",
            initialDelayString = "${application.parser.auctionspark.details.delay}", timeUnit = TimeUnit.MINUTES)
    public void sparkPipeLineSourceDetailsParseExecute() {
        if (detailsEnabled) {
            auctionSparkSecondDetails.execute();
        }
    }

    @Scheduled(fixedDelayString = "${application.parser.auctionspark.calculation.delay}",
            initialDelayString = "${application.parser.auctionspark.calculation.delay}", timeUnit = TimeUnit.MINUTES)
    public void sparkPipeLineCalculatorParseExecute() {
        if (calculationEnabled) {
            auctionSparkThirdCalc.execute();
        }
    }

}
