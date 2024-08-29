package com.igurman.gur_car_bot.dictionary;

import com.igurman.gur_car_bot.constant.I18nType;
import com.igurman.gur_car_bot.constant.LanguageType;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import static com.igurman.gur_car_bot.constant.I18nType.ABOUT_FACEBOOK_TITLE;
import static com.igurman.gur_car_bot.constant.I18nType.ABOUT_FACEBOOK_URL;
import static com.igurman.gur_car_bot.constant.I18nType.ABOUT_FEEDBACK_TITLE;
import static com.igurman.gur_car_bot.constant.I18nType.ABOUT_FEEDBACK_URL;
import static com.igurman.gur_car_bot.constant.I18nType.ABOUT_INSTAGRAM_TITLE;
import static com.igurman.gur_car_bot.constant.I18nType.ABOUT_INSTAGRAM_URL;
import static com.igurman.gur_car_bot.constant.I18nType.ABOUT_MESSAGE;
import static com.igurman.gur_car_bot.constant.I18nType.ABOUT_TELEGRAM_TITLE;
import static com.igurman.gur_car_bot.constant.I18nType.ABOUT_TELEGRAM_URL;
import static com.igurman.gur_car_bot.constant.I18nType.ABOUT_US_MENU;
import static com.igurman.gur_car_bot.constant.I18nType.ABOUT_YOUTUBE_TITLE;
import static com.igurman.gur_car_bot.constant.I18nType.ABOUT_YOUTUBE_URL;
import static com.igurman.gur_car_bot.constant.I18nType.BACK;
import static com.igurman.gur_car_bot.constant.I18nType.CATALOG_BACK;
import static com.igurman.gur_car_bot.constant.I18nType.CATALOG_FILTER_BACK;
import static com.igurman.gur_car_bot.constant.I18nType.CATALOG_FILTER_BUILD;
import static com.igurman.gur_car_bot.constant.I18nType.CATALOG_FILTER_CLEAN;
import static com.igurman.gur_car_bot.constant.I18nType.CATALOG_FILTER_ENGINE_CLEAN;
import static com.igurman.gur_car_bot.constant.I18nType.CATALOG_FILTER_ENGINE_TYPE;
import static com.igurman.gur_car_bot.constant.I18nType.CATALOG_FILTER_GRADE_CLEAN;
import static com.igurman.gur_car_bot.constant.I18nType.CATALOG_FILTER_MODEL;
import static com.igurman.gur_car_bot.constant.I18nType.CATALOG_FILTER_ODOMETER_CLEAN;
import static com.igurman.gur_car_bot.constant.I18nType.CATALOG_FILTER_PRICE_CLEAN;
import static com.igurman.gur_car_bot.constant.I18nType.CATALOG_FILTER_YEAR;
import static com.igurman.gur_car_bot.constant.I18nType.CATALOG_FILTER_YEAR_CLEAN;
import static com.igurman.gur_car_bot.constant.I18nType.CATALOG_MAKE_MESSAGE;
import static com.igurman.gur_car_bot.constant.I18nType.CATALOG_MENU;
import static com.igurman.gur_car_bot.constant.I18nType.CATALOG_MESSAGE;
import static com.igurman.gur_car_bot.constant.I18nType.CATALOG_MODEL_BACK;
import static com.igurman.gur_car_bot.constant.I18nType.CATALOG_MODEL_DELETE_ALL;
import static com.igurman.gur_car_bot.constant.I18nType.CATALOG_MODEL_MESSAGE;
import static com.igurman.gur_car_bot.constant.I18nType.CATALOG_MODEL_NEXT;
import static com.igurman.gur_car_bot.constant.I18nType.CATALOG_MODEL_PREVIOUS;
import static com.igurman.gur_car_bot.constant.I18nType.CATALOG_MODEL_TAKE_ALL;
import static com.igurman.gur_car_bot.constant.I18nType.CATALOG_NEXT;
import static com.igurman.gur_car_bot.constant.I18nType.CATALOG_PREVIOUS;
import static com.igurman.gur_car_bot.constant.I18nType.CATALOG_SEARCH_START;
import static com.igurman.gur_car_bot.constant.I18nType.CONTACT_MENU;
import static com.igurman.gur_car_bot.constant.I18nType.CONTACT_MESSAGE;
import static com.igurman.gur_car_bot.constant.I18nType.DIESEL;
import static com.igurman.gur_car_bot.constant.I18nType.ELECTRIC;
import static com.igurman.gur_car_bot.constant.I18nType.FLEX;
import static com.igurman.gur_car_bot.constant.I18nType.GAS;
import static com.igurman.gur_car_bot.constant.I18nType.GRADE_FILTER;
import static com.igurman.gur_car_bot.constant.I18nType.HELP_1_MESSAGE;
import static com.igurman.gur_car_bot.constant.I18nType.HELP_2_MESSAGE;
import static com.igurman.gur_car_bot.constant.I18nType.HELP_3_MESSAGE;
import static com.igurman.gur_car_bot.constant.I18nType.HELP_4_MESSAGE;
import static com.igurman.gur_car_bot.constant.I18nType.HELP_MENU;
import static com.igurman.gur_car_bot.constant.I18nType.HYBRID;
import static com.igurman.gur_car_bot.constant.I18nType.HYDROGEN;
import static com.igurman.gur_car_bot.constant.I18nType.MENU;
import static com.igurman.gur_car_bot.constant.I18nType.MENU_MESSAGE;
import static com.igurman.gur_car_bot.constant.I18nType.MODEL_ALL;
import static com.igurman.gur_car_bot.constant.I18nType.ODOMETER_FILTER;
import static com.igurman.gur_car_bot.constant.I18nType.PLUG_IN;
import static com.igurman.gur_car_bot.constant.I18nType.PRICE_FILTER;
import static com.igurman.gur_car_bot.constant.I18nType.PUT_PHONE_NUMBER;
import static com.igurman.gur_car_bot.constant.I18nType.QUESTION_CANCEL_MESSAGE;
import static com.igurman.gur_car_bot.constant.I18nType.QUESTION_MENU;
import static com.igurman.gur_car_bot.constant.I18nType.QUESTION_MESSAGE;
import static com.igurman.gur_car_bot.constant.I18nType.SAVE_MESSAGE;
import static com.igurman.gur_car_bot.constant.I18nType.SEARCH_BACK;
import static com.igurman.gur_car_bot.constant.I18nType.SEARCH_DETAILS;
import static com.igurman.gur_car_bot.constant.I18nType.SEARCH_FINISH;
import static com.igurman.gur_car_bot.constant.I18nType.SEARCH_MESSAGE;
import static com.igurman.gur_car_bot.constant.I18nType.SEARCH_NEXT;
import static com.igurman.gur_car_bot.constant.I18nType.SEARCH_ORDER;
import static com.igurman.gur_car_bot.constant.I18nType.SEARCH_ORDER_CARFAX;
import static com.igurman.gur_car_bot.constant.I18nType.SEARCH_ORDER_CARFAX_URL;
import static com.igurman.gur_car_bot.constant.I18nType.SEARCH_ORDER_MESSAGE_FAILURE;
import static com.igurman.gur_car_bot.constant.I18nType.SEARCH_ORDER_MESSAGE_SUCCESS;
import static com.igurman.gur_car_bot.constant.I18nType.SETTINGS_CITY;
import static com.igurman.gur_car_bot.constant.I18nType.SETTINGS_DISTANCE;
import static com.igurman.gur_car_bot.constant.I18nType.SETTINGS_LANGUAGE;
import static com.igurman.gur_car_bot.constant.I18nType.SETTINGS_LANGUAGE_BACK;
import static com.igurman.gur_car_bot.constant.I18nType.SETTINGS_LANGUAGE_CHANGE;
import static com.igurman.gur_car_bot.constant.I18nType.SETTINGS_LANGUAGE_MESSAGE;
import static com.igurman.gur_car_bot.constant.I18nType.SETTINGS_MENU;
import static com.igurman.gur_car_bot.constant.I18nType.SETTINGS_MENU_CLOSE;
import static com.igurman.gur_car_bot.constant.I18nType.SETTINGS_MESSAGE;
import static com.igurman.gur_car_bot.constant.I18nType.SETTINGS_TAX;
import static com.igurman.gur_car_bot.constant.I18nType.START_MESSAGE;
import static com.igurman.gur_car_bot.constant.I18nType.WRITE_CANCEL;
import static com.igurman.gur_car_bot.constant.I18nType.WRITE_TO_ADMIN;
import static com.igurman.gur_car_bot.constant.I18nType.WRITE_TO_ADMIN_MESSAGE;
import static lombok.AccessLevel.PRIVATE;

@Slf4j
@Component
@AllArgsConstructor(access = PRIVATE)
public class I18nDictionary {
    protected static final Map<I18nType, Map<LanguageType, String>> map = new EnumMap<>(I18nType.class);
    public static final String SPACE = " ";
    public static final String ELLIPSIS = "...";
    public static final String FROM_TO = "%s - %s";
    public static final String EMPTY = "";
    public static final String COLON_SPACE = ": ";
    public static final String COMMA_SPACE = ", ";
    public static final String NEXT_LINE = "\n";
    public static final String LANGUAGE_RU = "Русский";
    public static final String LANGUAGE_EN = "English";
    public static final String LANGUAGE_ES = "Español";

    static {
        put(ABOUT_TELEGRAM_TITLE, LanguageType.RU, "Telegram");
        put(ABOUT_TELEGRAM_URL, LanguageType.RU, "https://t.me/test");
        put(ABOUT_YOUTUBE_TITLE, LanguageType.RU, "Youtube");
        put(ABOUT_YOUTUBE_URL, LanguageType.RU, "https://www.youtube.com/@test/videos");
        put(ABOUT_INSTAGRAM_TITLE, LanguageType.RU, "Instagram");
        put(ABOUT_INSTAGRAM_URL, LanguageType.RU, "https://instagram.com/abc");
        put(ABOUT_FACEBOOK_TITLE, LanguageType.RU, "Facebook");
        put(ABOUT_FACEBOOK_URL, LanguageType.RU, "https://facebook.com/abc");
        put(ABOUT_FEEDBACK_TITLE, LanguageType.RU, "Отзывы");
        put(ABOUT_FEEDBACK_URL, LanguageType.RU, "https://aaa.com/abc");
        put(ABOUT_MESSAGE, LanguageType.RU, """
                Мы продаём автомобили любые автомобили с аукционов US.
                ✅ Вы можете посмотреть их в нашем каталоге.
                ✅ Покупайте больше.
                Выбирайте лучшее.""");
        put(CONTACT_MESSAGE, LanguageType.RU, """
                Вы можете связаться с нами:
                            
                📞 позвонить по телефону: 916-000-0000
                            
                            
                📄 написать администратору лично: @telegram
                            
                            
                ⏳ оставить номер телефона в сообщении, мы сами позвоним вам
                            
                приехать к нам лично, по адресу:
                            
                🗺 1111 JohnDoe 99999, CA, US
                            
                """);
        put(WRITE_TO_ADMIN, LanguageType.RU, "Написать администратору");
        put(PUT_PHONE_NUMBER, LanguageType.RU, "Оставить номер");
        put(WRITE_TO_ADMIN_MESSAGE, LanguageType.RU, """
                Пожалуйста опишите ниже ваш вопрос как можно подробнее.
                            
                Чем больше вы напишите информации, тем качественнее будет наш ответ.
                            
                🔥 Мы стараемся отвечать как можно скорее.
                            
                """);
        put(BACK, LanguageType.RU, "Назад");
        put(QUESTION_MESSAGE, LanguageType.RU, """
                Вы можете задать нам любой вопрос, в сообщении.
                                
                Нажмите кнопку ниже, напишите свой вопрос и отправьте нам.
                            
                Старайтесь указать все интересующие вас детали, так нам будет проще наиболее полно ответить на ваш запрос.
                            
                """);
        put(HELP_1_MESSAGE, LanguageType.RU, """
                ❓ Вопрос:
                Почему выгодно покупать у нас?
                            
                #️⃣ Ответ:
                Потому что!""");
        put(HELP_2_MESSAGE, LanguageType.RU, """
                ❓ Вопрос:
                Сколько мне это будет стоить?
                            
                #️⃣ Ответ:
                Дешевле, чем у других диллеров.""");
        put(HELP_3_MESSAGE, LanguageType.RU, """
                ❓ Вопрос:
                Как я должен оплатить автомобиль?
                            
                #️⃣ Ответ:
                Любым доступным вам способом. Мы принимаем банковские переводы, наличные, обмен на шкурки.""");
        put(HELP_4_MESSAGE, LanguageType.RU, """
                ❓ Вопрос:
                Есть ли у вас кредит?
                            
                #️⃣ Ответ:
                Да, у нас есть кредит. Процент от 0,00001% до 146%.""");
        put(START_MESSAGE, LanguageType.RU, """
                Добрый день, %s!
                Выберите интересующий вас пункт меню.""");
        put(MENU_MESSAGE, LanguageType.RU, "Выберите интересующий вас пункт меню.");
        put(ABOUT_US_MENU, LanguageType.RU, "О нас");
        put(CATALOG_MENU, LanguageType.RU, "Каталог");
        put(CONTACT_MENU, LanguageType.RU, "Связаться");
        put(HELP_MENU, LanguageType.RU, "Справка");
        put(SETTINGS_MENU, LanguageType.RU, "Настройки");
        put(QUESTION_MENU, LanguageType.RU, "Задать вопрос");
        put(CATALOG_MESSAGE, LanguageType.RU, """
                Все параметры фильтра, которые были сохренены для дальнейшего поиска:
                                                   
                <b>Модели:</b> %s
                                                   
                <b>Тип двигателя:</b> %s
                                                   
                <b>Год:</b> %s
                                                   
                <b>Пробег:</b> %s
                                                   
                <b>Цена:</b> %s
                                
                <b>Оценка:</b> %s
                """);
        put(CATALOG_FILTER_BUILD, LanguageType.RU, "Задать фильтр");
        put(CATALOG_FILTER_CLEAN, LanguageType.RU, "Очистить фильтр");
        put(CATALOG_SEARCH_START, LanguageType.RU, "▶️ Начать поиск");
        put(CATALOG_MAKE_MESSAGE, LanguageType.RU, "Выберите нужную марку автомобиля и фильтр перейдёт в выбор модели");
        put(CATALOG_BACK, LanguageType.RU, "↩️ Назад в каталог");
        put(CATALOG_FILTER_BACK, LanguageType.RU, "↩️ Назад в фильтры");
        put(CATALOG_NEXT, LanguageType.RU, "Следующая ➡️");
        put(CATALOG_PREVIOUS, LanguageType.RU, "⬅️ Предыдущая");
        put(CATALOG_MODEL_MESSAGE, LanguageType.RU, "Выберите нужную модель автомобиля и она сохранится в фильтр");
        put(CATALOG_MODEL_TAKE_ALL, LanguageType.RU, "Выбрать все модели %s сразу");
        put(CATALOG_MODEL_DELETE_ALL, LanguageType.RU, "Очистить все модели %s");
        put(CATALOG_MODEL_BACK, LanguageType.RU, "↩️ Назад в выбор марки авто");
        put(CATALOG_MODEL_NEXT, LanguageType.RU, "Следующая ➡️");
        put(CATALOG_MODEL_PREVIOUS, LanguageType.RU, "⬅️ Предыдущая");
        put(CATALOG_FILTER_MODEL, LanguageType.RU, "🚗 Модель");
        put(CATALOG_FILTER_YEAR, LanguageType.RU, "♈️ Год");
        put(CATALOG_FILTER_ENGINE_TYPE, LanguageType.RU, "⚠️ Тип двигателя");
        put(ODOMETER_FILTER, LanguageType.RU, "🏃 Пробег");
        put(PRICE_FILTER, LanguageType.RU, "💵 Цена");
        put(GRADE_FILTER, LanguageType.RU, "🏆 Оценка");
        put(CATALOG_FILTER_YEAR_CLEAN, LanguageType.RU, "Очистить год");
        put(CATALOG_FILTER_PRICE_CLEAN, LanguageType.RU, "Очистить цену");
        put(CATALOG_FILTER_GRADE_CLEAN, LanguageType.RU, "Очистить оценку");
        put(CATALOG_FILTER_ODOMETER_CLEAN, LanguageType.RU, "Очистить пробег");
        put(CATALOG_FILTER_ENGINE_CLEAN, LanguageType.RU, "Очистить тип двигателя");
        put(SEARCH_MESSAGE, LanguageType.RU, "Показано вариантов <b>%s</b>. Всего найдено авто: <b>%s</b>. Текущая страница: <b>%s</b>. Осталось просмотреть страниц: <b>%s</b>.");
        put(SEARCH_NEXT, LanguageType.RU, "Показать ещё");
        put(SEARCH_BACK, LanguageType.RU, "↩️ Вернуться в каталог");
        put(SEARCH_FINISH, LanguageType.RU, "Я показал все найденные автомобили. Для следующего поиска попробуйте другие настройки фильтра.");
        put(SEARCH_ORDER, LanguageType.RU, "Заказать");
        put(SETTINGS_MESSAGE, LanguageType.RU, "Настройте бота для себя нажав нужную опцию.");
        put(SETTINGS_LANGUAGE, LanguageType.RU, "🌎 Язык пользователя: %s");
        put(SETTINGS_CITY, LanguageType.RU, "🏠 Город: %s");
        put(SETTINGS_DISTANCE, LanguageType.RU, "↔️ Дистанция поиска от города: %s миль");
        put(SETTINGS_TAX, LanguageType.RU, "✅ Включить расчёт налога");
        put(SETTINGS_MENU_CLOSE, LanguageType.RU, "🙈 Закрыть меню");
        put(SETTINGS_LANGUAGE_BACK, LanguageType.RU, "↩️ Назад");
        put(SETTINGS_LANGUAGE_MESSAGE, LanguageType.RU, "Меняет язык общения с ботом. Сейчас: %s\n\nДля изменения нажмите нужный язык внизу.");
        put(SETTINGS_LANGUAGE_CHANGE, LanguageType.RU, "Язык изменен на: %s");
        put(SAVE_MESSAGE, LanguageType.RU, "Ваше сообщение получено. Мы ответим вам так быстро, как только сможем.\nСпасибо за обращение!");
        put(WRITE_CANCEL, LanguageType.RU, "🙅 Отмена");
        put(MENU, LanguageType.RU, "Меню");
        put(QUESTION_CANCEL_MESSAGE, LanguageType.RU, "Отправка сообщения отменена.\n\nПопробуйте воспользоваться разделом Справка /help.");
        put(GAS, LanguageType.RU, "Бензиновый");
        put(HYBRID, LanguageType.RU, "Гибридный");
        put(ELECTRIC, LanguageType.RU, "Электрический");
        put(PLUG_IN, LanguageType.RU, "Plug-in");
        put(DIESEL, LanguageType.RU, "Дизель");
        put(FLEX, LanguageType.RU, "Гибкое топливо");
        put(HYDROGEN, LanguageType.RU, "Водородное топливо");
        put(SEARCH_ORDER_MESSAGE_SUCCESS, LanguageType.RU, "Ваш заказ на автомобиль марка: %s, модель: %s, vin: %s. Получен.\nМы свяжемся с вами в ближайшее время.");
        put(SEARCH_ORDER_MESSAGE_FAILURE, LanguageType.RU, "Что-то пошло не так. Мы не смогли получить ваш заказ." +
                                                           "Пожалуйста скопируйте данные авто и отправьте их через меню Задать вопрос, или свяжитесь с администратором группы.");
        put(SEARCH_ORDER_CARFAX, LanguageType.RU, "Заказать Carfax");
        put(SEARCH_ORDER_CARFAX_URL, LanguageType.RU, "https://t.me/test");
        put(SEARCH_DETAILS, LanguageType.RU, "Подробнее");
        put(MODEL_ALL, LanguageType.RU, "все");


    }

    public static String getI18n(I18nType type, LanguageType lang) {
        String text = map.get(type).get(lang);
        if (text == null) {
            log.error("*** Не найден текст для языка :{}", lang);
            return ELLIPSIS;
        }
        return text;
    }

    public static String getI18n(I18nType type, LanguageType lang, Object... formatObjects) {
        String text = map.get(type).get(lang);
        if (text == null) {
            log.error("*** Не найден текст для языка :{}", lang);
            return ELLIPSIS;
        }
        return String.format(text, formatObjects);
    }

    private static void put(I18nType type, LanguageType languageType, String str) {
        map.computeIfPresent(type, (t, m) -> {
            m.put(languageType, str);
            return m;
        });
        map.putIfAbsent(type, new HashMap<>() {{
            put(languageType, str);
        }});
    }

}

