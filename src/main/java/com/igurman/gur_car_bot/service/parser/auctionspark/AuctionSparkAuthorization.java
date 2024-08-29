package com.igurman.gur_car_bot.service.parser.auctionspark;

import com.igurman.gur_car_bot.exception.ParserRuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.igurman.gur_car_bot.constant.Constant.USER_AGENT;

/**
 * Запрос авторизации на www.spark.com
 */
@Slf4j
@Service
public class AuctionSparkAuthorization {
    @Value("${application.parser.auctionspark.login}")
    private String loginUsername;
    @Value("${application.parser.auctionspark.password}")
    private String loginPassword;
    @Value("${application.parser.auctionspark.login-url}")
    private String LOGIN_URL;
    @Value("${application.parser.auctionspark.search-url}")
    private String SEARCH_URL;

    private Map<String, String> cache = new HashMap<>();

    public Map<String, String> getAuthorization() {

        try {
            log.info("*** Запрос авторизации, проверяем живы ли куки для spark.com");
            Connection.Response execute = Jsoup.connect(SEARCH_URL)
                    .userAgent(USER_AGENT)
                    .cookies(cache)
                    .followRedirects(false)
                    .execute();

            if (execute.statusCode() == 200) {
                log.info("*** Куки живы для spark.com");
                return cache;
            }

        } catch (IOException e) {
            log.info("*** Куки протухли для spark.com");

        }
        return getCookies();
    }

    private Map<String, String> getCookies() {
        log.info("*** Старт получения куки: {}", LOGIN_URL);
        try {
            Document loginPage = Jsoup.connect(LOGIN_URL)
                    .userAgent(USER_AGENT)
                    .get();

            log.info("*** Начальная страница получена");

            // Нахождение формы
            Element loginForm = loginPage.select("form.login").first();

            if (loginForm == null) {
                throw new ParserRuntimeException("*** Не смог найти форму авторизации");
            }
            log.info("*** Форма авторизации получена");

            // Подготовка данных для отправки формы
            String csrfToken = loginForm.select("input[type=hidden][name=authenticity_token]").val();
            log.info("*** csrfToken получен: {}", csrfToken);

            // Подготовка данных для отправки формы
            Connection.Response loginResponse = Jsoup.connect(LOGIN_URL)
                    .data("username", loginUsername)
                    .data("password", loginPassword)
                    .data("csrf_token", csrfToken)
                    .method(Connection.Method.POST)
                    .userAgent(USER_AGENT)
                    .followRedirects(true)
                    .execute();

            // Проверка успешности авторизации
            if (loginResponse.statusCode() == 200) {
                cache = loginResponse.cookies();
                log.info("*** Успешная авторизация! куки получены. {}", loginResponse.cookies());
                return loginResponse.cookies();
            } else {
                throw new ParserRuntimeException("*** Ошибка авторизации, код: " + loginResponse.statusCode());
            }
        } catch (IOException e) {
            throw new ParserRuntimeException("*** При авторизации произошла ошибка: " + e);
        }
    }
}
