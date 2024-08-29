package com.igurman.gur_car_bot.constant;

import lombok.experimental.UtilityClass;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import static com.igurman.gur_car_bot.constant.RouteType.UNKNOWN;

@UtilityClass
public class RouteDictionary {
    private static final Map<RouteType, String> typeToRouteMessageMap = new EnumMap<>(RouteType.class);
    private static final Map<String, RouteType> routeToTypeMessageMap = new HashMap<>();
    private static final Map<RouteType, String> typeToRouteCallbackMap = new EnumMap<>(RouteType.class);
    private static final Map<String, RouteType> routeToTypeCallbackMap = new HashMap<>();

    static {
        // роуты для сообщений
        typeToRouteMessageMap.put(RouteType.START_ROUTE, "/start");
        typeToRouteMessageMap.put(RouteType.MENU_ROUTE, "/menu");
        typeToRouteMessageMap.put(RouteType.ABOUT_ROUTE, "/about");
        typeToRouteMessageMap.put(RouteType.CATALOG_ROUTE, "/catalog");
        typeToRouteMessageMap.put(RouteType.CONTACT_ROUTE, "/contact");
        typeToRouteMessageMap.put(RouteType.HELP_ROUTE, "/help");
        typeToRouteMessageMap.put(RouteType.SETTINGS_ROUTE, "/settings");
        typeToRouteMessageMap.put(RouteType.QUESTION_ROUTE, "/question");
        typeToRouteMessageMap.put(RouteType.ABOUT_MENU_ROUTE, "ℹ️");
        typeToRouteMessageMap.put(RouteType.CATALOG_MENU_ROUTE, "🚗");
        typeToRouteMessageMap.put(RouteType.CONTACT_MENU_ROUTE, "📞");
        typeToRouteMessageMap.put(RouteType.HELP_MENU_ROUTE, "📄");
        typeToRouteMessageMap.put(RouteType.SETTINGS_MENU_ROUTE, "⚙️");
        typeToRouteMessageMap.put(RouteType.QUESTION_MENU_ROUTE, "🤔");
        typeToRouteMessageMap.put(RouteType.MIGRATE_ROUTE, "/migrate");

        // роуты для коллбека
        typeToRouteCallbackMap.put(RouteType.WRITE_TO_ADMIN_ROUTE, "/e4b9g6/contact/write-to-admin");
        typeToRouteCallbackMap.put(RouteType.CONTACT_ROUTE, "/h7b9a8/contact");
        typeToRouteCallbackMap.put(RouteType.CATALOG_MAKE_ROUTE, "/c2a0b1/b9g6b1/catalog/make");
        typeToRouteCallbackMap.put(RouteType.CATALOG_MODEL_ROUTE, "/h7h7c2/catalog/model");
        typeToRouteCallbackMap.put(RouteType.CATALOG_ROUTE, "/e4b1a0/catalog");
        typeToRouteCallbackMap.put(RouteType.CATALOG_FILTER_ROUTE, "/d3a8b9/catalog/filter");
        typeToRouteCallbackMap.put(RouteType.CATALOG_YEAR_ROUTE, "/e4e4g6/catalog/year");
        typeToRouteCallbackMap.put(RouteType.CATALOG_ODOMETER_ROUTE, "/f5f5e4/catalog/odometer");
        typeToRouteCallbackMap.put(RouteType.CATALOG_ENGINE_TYPE_ROUTE, "/d3g6g6/catalog/enginetype");
        typeToRouteCallbackMap.put(RouteType.CATALOG_PRICE_ROUTE, "/a8f5d3/catalog/price");
        typeToRouteCallbackMap.put(RouteType.CATALOG_GRADE_ROUTE, "/h7h7h7/catalog/grade");
        typeToRouteCallbackMap.put(RouteType.CATALOG_SEARCH_ROUTE, "/c2f5f5/catalog/search");
        typeToRouteCallbackMap.put(RouteType.CATALOG_ORDER_ROUTE, "/e4b9f5/catalog/order");
        typeToRouteCallbackMap.put(RouteType.SETTINGS_LANGUAGE_ROUTE, "/b9c2c2/settings/language");
        typeToRouteCallbackMap.put(RouteType.SETTINGS_CITY_ROUTE, "/b1b1a8/settings/city");
        typeToRouteCallbackMap.put(RouteType.SETTINGS_DISTANCE_ROUTE, "/b9e4h7/settings/distance");
        typeToRouteCallbackMap.put(RouteType.SETTINGS_TAX_ROUTE, "/d3e4d3/settings/tax");
        typeToRouteCallbackMap.put(RouteType.SETTINGS_ROUTE, "/b9e4e4/settings");
        typeToRouteCallbackMap.put(RouteType.MENU_ROUTE, "/g6a8d3/menu");
        typeToRouteCallbackMap.put(RouteType.QUESTION_CANCEL_ROUTE, "/c2a8b1/question/cancel");


//    typeToRouteCallbackMap.put(RouteType.CATALOG_MAKE, "d3f5b9");
//    typeToRouteCallbackMap.put(RouteType.CATALOG_MAKE, "h7b9a0");
//    typeToRouteCallbackMap.put(RouteType.CATALOG_MAKE, "b9g6f5");
//    typeToRouteCallbackMap.put(RouteType.CATALOG_MAKE, "h7c2d3");
//    typeToRouteCallbackMap.put(RouteType.CATALOG_MAKE, "b9b9b9");
//    typeToRouteCallbackMap.put(RouteType.CATALOG_MAKE, "h7e4f5");
//    typeToRouteCallbackMap.put(RouteType.CATALOG_MAKE, "f5c2g6");
//    typeToRouteCallbackMap.put(RouteType.CATALOG_MAKE, "b9b1b1");
//    typeToRouteCallbackMap.put(RouteType.CATALOG_MAKE, "a0h7a8");
//    typeToRouteCallbackMap.put(RouteType.CATALOG_MAKE, "b9a8g6");
//    typeToRouteCallbackMap.put(RouteType.CATALOG_MAKE, "d3b9b1");
//    typeToRouteCallbackMap.put(RouteType.CATALOG_MAKE, "h7h7a8");
//    typeToRouteCallbackMap.put(RouteType.CATALOG_MAKE, "f5b9c2");
//    typeToRouteCallbackMap.put(RouteType.CATALOG_MAKE, "a8f5h7");
//    typeToRouteCallbackMap.put(RouteType.CATALOG_MAKE, "h7e4b9");
//    typeToRouteCallbackMap.put(RouteType.CATALOG_MAKE, "e4h7b9");
//    typeToRouteCallbackMap.put(RouteType.CATALOG_MAKE, "g6g6b9");
//    typeToRouteCallbackMap.put(RouteType.CATALOG_MAKE, "f5e4f5");
//    typeToRouteCallbackMap.put(RouteType.CATALOG_MAKE, "g6a0f5");
//    typeToRouteCallbackMap.put(RouteType.CATALOG_MAKE, "e4a8e4");
//    typeToRouteCallbackMap.put(RouteType.CATALOG_MAKE, "g6b9b1");
//    typeToRouteCallbackMap.put(RouteType.CATALOG_MAKE, "g6e4a8");
//    typeToRouteCallbackMap.put(RouteType.CATALOG_MAKE, "f5c2a8");

        typeToRouteMessageMap.forEach((key, value) -> routeToTypeMessageMap.put(value, key));
        typeToRouteCallbackMap.forEach((key, value) -> routeToTypeCallbackMap.put(value, key));
    }

    /**
     * поиск роута для сообщения
     * сначала ищем по полному вхождению, если не нашли, ищем по началу сообщения
     */
    public static RouteType getRouteByMessage(String route) {
        RouteType routeType = routeToTypeMessageMap.getOrDefault(route, RouteType.UNKNOWN);
        if (!UNKNOWN.equals(routeType)) {
            return routeType;
        }
        return routeToTypeMessageMap.entrySet().stream()
            .filter(pair -> route.startsWith(pair.getKey()))
            .map(Map.Entry::getValue)
            .findFirst().orElse(RouteType.UNKNOWN);
    }

    /**
     * поиск роута для коллбека
     * сначала ищем по полному вхождению, если не нашли, ищем по началу сообщения
     */
    public static RouteType getRouteByCallback(String route) {
        RouteType routeType = routeToTypeCallbackMap.getOrDefault(route, RouteType.UNKNOWN);
        if (!UNKNOWN.equals(routeType)) {
            return routeType;
        }
        return routeToTypeCallbackMap.entrySet().stream()
                .filter(pair -> route.startsWith(pair.getKey()))
                .map(Map.Entry::getValue)
                .findFirst().orElse(RouteType.UNKNOWN);
    }

    public static String getCallbackRoute(RouteType type) {
        return typeToRouteCallbackMap.get(type);
    }

}
