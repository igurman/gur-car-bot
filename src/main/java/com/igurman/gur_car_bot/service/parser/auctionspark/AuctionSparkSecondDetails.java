package com.igurman.gur_car_bot.service.parser.auctionspark;

import com.fasterxml.jackson.databind.JsonNode;
import com.igurman.gur_car_bot.constant.AuctionType;
import com.igurman.gur_car_bot.constant.TimerType;
import com.igurman.gur_car_bot.constant.VehicleStatusType;
import com.igurman.gur_car_bot.model.entity.VehicleEntity;
import com.igurman.gur_car_bot.service.common.VehicleService;
import com.igurman.gur_car_bot.util.ObjectMapperProvider;
import com.igurman.gur_car_bot.util.TimerUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static com.igurman.gur_car_bot.constant.Constant.USER_AGENT;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuctionSparkSecondDetails {
    private final AuctionSparkAuthorization auctionSparkAuthorization;
    private final ObjectMapperProvider objectMapperProvider;
    private final VehicleService vehicleService;

    @Value("${application.parser.auctionspark.details.count-details:50}")
    private Integer countDetails;
    @Value("${application.parser.auctionspark.details.url}")
    private String URL_DETAILS;

    public void execute() {
        log.info("*** Пробуем получить ИД для скачивания детализации для сайта spark_com");
        // получим из БД несколько записей
        List<VehicleEntity> vehicleEntityList = vehicleService.findAllByStatusAndAuction(
                VehicleStatusType.AWAIT_LOAD,
                AuctionType.AUCTION_SPARK,
                countDetails);

        if (vehicleEntityList.isEmpty()) {
            return;
        }
        log.info("*** Получили записи из БД для скачивания детализации с сайта spark_com, кол-во: {}",
                vehicleEntityList.size());

        // получим авторизацию
        Map<String, String> authorizationCookies = auctionSparkAuthorization.getAuthorization();
        vehicleEntityList.forEach(a -> this.downloadPage(a, authorizationCookies));

        log.info("*** Закончили получение детализации с сайта spark_com");
    }

    private void downloadPage(VehicleEntity vehicle, Map<String, String> authorizationCookies) {
        // Получим данные о детализации
        Document searchData = getDataByItem(vehicle.getAuctionId(), authorizationCookies);
        if (searchData == null) {
            return;
        }

        // распарсим полученные данные в json
        JsonNode jsonModel = objectMapperProvider.textToJsonModel(searchData.text());

        // сохраним в БД со сменой статуса
        vehicle.setLink(URL_DETAILS + vehicle.getAuctionId());
        vehicle.setAuctionData(objectMapperProvider.toJson(jsonModel.path("vehicle")));
        vehicle.setStatus(VehicleStatusType.AWAIT_CALC);
        vehicleService.save(vehicle);
    }

    private Document getDataByItem(String number, Map<String, String> cookies) {
        log.info("*** Пробуем сделать запрос на {} {}", URL_DETAILS, number);
        // если лимит запросов исчерпан, будем ждать
        TimerUtil.getPause(TimerType.AUCTION_SPARK_TIMER);
        try {
            Document searchPage = Jsoup.connect(URL_DETAILS + number)
                    .cookies(cookies)
                    .userAgent(USER_AGENT)
                    .ignoreContentType(true)
                    .header("content-type", "application/json")
                    .header("Sec-Fetch-Mode", "cors")
                    .header("Sec-Fetch-Sitet", "same-origin")
                    .header("X-Requested-With", "XMLHttpRequest")
                    .post();
            log.info("*** Успех запроса на {} {}", URL_DETAILS, number);
            return searchPage;
        } catch (IOException e) {
            // наверное не нужно бросать ошибку, если одна страница не получена, возможно она удалена
            // но возможно нужно делать подсчёт ошибок и если их больше определенного, то останавливать весь парсинг
            // м.б. стоит просто помониторить этот вопрос
            log.error("*** Запрос на {} {} вернул ошибку", URL_DETAILS, number, e);
            return null;
        }
    }

}
