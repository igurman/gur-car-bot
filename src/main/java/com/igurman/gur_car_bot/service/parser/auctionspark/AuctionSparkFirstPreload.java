package com.igurman.gur_car_bot.service.parser.auctionspark;

import com.fasterxml.jackson.databind.JsonNode;
import com.igurman.gur_car_bot.constant.AuctionType;
import com.igurman.gur_car_bot.constant.TimerType;
import com.igurman.gur_car_bot.constant.VehicleStatusType;
import com.igurman.gur_car_bot.exception.ParserRuntimeException;
import com.igurman.gur_car_bot.model.entity.VehicleEntity;
import com.igurman.gur_car_bot.service.common.VehicleService;
import com.igurman.gur_car_bot.util.ObjectMapperProvider;
import com.igurman.gur_car_bot.util.TimerUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.igurman.gur_car_bot.constant.Constant.USER_AGENT;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuctionSparkFirstPreload {
    private final AuctionSparkAuthorization auctionSparkAuthorization;
    private final ObjectMapperProvider objectMapperProvider;
    private final VehicleService vehicleService;

    @Value("${application.parser.auctionspark.seed.day-future:1}")
    private Integer dayFuture;
    @Value("${application.parser.auctionspark.graphql-url}")
    private String graphqlUrl;

    private static final String GRAPHQL_JSON_PATH = "json/graphql_spark.json";
    private static final List<String> MAKE_CLEAN_FILTER = List.of("Freightliner");

    public void execute() {
        log.info("*** Пробуем скачать начальные данные для сайта spark_com");
        // получим авторизацию
        Map<String, String> authorizationCookies = auctionSparkAuthorization.getAuthorization();

        // получим данные с сайта, сколько авто есть в наличии
        Document searchData = this.getDataByGraph(getGraphqlRequest(50), authorizationCookies);

        // распарсим полученные данные в json
        JsonNode jsonModel = objectMapperProvider.textToJsonModel(searchData.text());

        // получим общее количество позиций
        int count = jsonModel.path("data").path("searchableVehicles").path("totalCount").asInt();
        log.info("*** Получили {} моделей с сайта spark_com", count);

        // если общее количество позиций больше, то сделаем запрос с увеличенным числом позиций
        if (count > 50) {
            // получим данные с сайта, с увеличенным числом позиций
            Document searchDataRepeat = this.getDataByGraph(getGraphqlRequest(count), authorizationCookies);
            // распарсим полученные данные в json
            jsonModel = objectMapperProvider.textToJsonModel(searchDataRepeat.text());
        }

        Iterator<JsonNode> elements = jsonModel.path("data").path("searchableVehicles").path("vehicles").elements();

        int i = 0;
        while (elements.hasNext()) {
            JsonNode item = elements.next();
            String stockNumber = item.path("stockNumber").asText();
            if (StringUtils.isEmpty(stockNumber)) {
                log.error("*** Получил позицию с пустым stockNumber: {}", stockNumber);
                continue;
            }

            // отфильтруем ненужных производителей
            if (MAKE_CLEAN_FILTER.contains(item.path("make").asText())) {
                log.debug("*** Получил позицию с ненужной маркой: {}, {}", item.path("make").asText(), item.path("stockNumber").asText());
                continue;
            }

            String vin = item.path("vin").asText();
            VehicleEntity vehicleByVin = vehicleService.findByVin(vin);
            if (vehicleByVin != null) {
                log.debug("*** Позиция vin: {}, уже присутствует в БД id: {}", vin, vehicleByVin.getId());
                continue;
            }

            this.saveModel(item);
            i++;
        }
        log.info("*** Закончили получение первичных данных с сайта spark_com, сохранили в БД: {}", i);
    }

    private void saveModel(JsonNode item) {
        // сохраняем модель в БД
        VehicleEntity model = VehicleEntity.builder()
                .auctionId(item.path("stockNumber").asText())
                .vin(item.path("vin").asText())
                .auction(AuctionType.AUCTION_SPARK)
                .status(VehicleStatusType.AWAIT_LOAD)
                .build();
        vehicleService.save(model);
    }

    /**
     * модификация запроса GraphQL замена даты, количества позиций
     */
    private String getGraphqlRequest(Integer count) {
        JsonNode graphqlJson = objectMapperProvider.readJsonFile(GRAPHQL_JSON_PATH);
        if (graphqlJson == null) {
            return null;
        }
        objectMapperProvider.modify(graphqlJson, "variables", "filterParams", "saleDateRange", "startDate", LocalDate.now().toString());
        objectMapperProvider.modify(graphqlJson, "variables", "filterParams", "saleDateRange", "endDate", LocalDate.now().plusDays(dayFuture).toString());
        objectMapperProvider.modify(graphqlJson, "variables", "perPage", count);
        return graphqlJson.toString();
    }

    /**
     * Запрос на  www.spark.com/graphql для получения списка авто
     * входные данные: json graphql, cookies
     */
    private Document getDataByGraph(String graphqlJson, Map<String, String> cookies) {
        log.info("*** Пробуем сделать запрос на www.spark.com/graphql");
        // если лимит запросов исчерпан, будем ждать
        TimerUtil.getPause(TimerType.AUCTION_SPARK_TIMER);
        try {
            Document searchPage = Jsoup.connect(graphqlUrl)
                    .requestBody(graphqlJson)
                    .cookies(cookies)
                    .userAgent(USER_AGENT)
                    .ignoreContentType(true)
                    .header("content-type", "application/json")
                    .header("Sec-Fetch-Mode", "cors")
                    .header("Sec-Fetch-Sitet", "same-origin")
                    .header("X-Requested-With", "XMLHttpRequest")
                    .post();
            log.info("*** Успех запроса на www.spark.com/graphql");
            return searchPage;
        } catch (IOException e) {
            throw new ParserRuntimeException("*** Запрос на www.spark.com/graphql вернул ошибку: " + e);
        }
    }

}
