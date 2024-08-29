package com.igurman.gur_car_bot.config;

import com.igurman.gur_car_bot.constant.LanguageType;
import com.igurman.gur_car_bot.service.telegram.TelegramBotService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.description.SetMyDescription;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.List;

@Slf4j
@Configuration
@ConditionalOnProperty(value = "application.telegram.bot.enabled", havingValue = "true")
public class TelegramConfig {
    @Value("${application.telegram.bot.name}")
    private String botName;
    @Value("${application.telegram.bot.token}")
    private String botToken;

    @Bean
    public TelegramBotsApi telegramBotsApi(TelegramBotService telegramBotService) {
        TelegramBotsApi telegramBotsApi = null;
        try {
            telegramBotService.execute(new SetMyCommands(commandsRu(), null, LanguageType.RU.code()));
            telegramBotService.execute(new SetMyCommands(commandsEn(), null, LanguageType.EN.code()));
            telegramBotService.execute(new SetMyDescription("Нажми /start и узнаешь", LanguageType.RU.code()));
            telegramBotService.execute(new SetMyDescription("Push /start and you will know", LanguageType.EN.code()));
            telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(telegramBotService);
        } catch (TelegramApiException e) {
            log.error("*** Не смог инициализировать TgBot: " + e);
        }
        return telegramBotsApi;
    }

    private List<BotCommand> commandsRu() {
        return List.of(
                new BotCommand("start", "Начать использовать"),
                new BotCommand("menu", "Открыть меню"),
                new BotCommand("about", "О нас"),
                new BotCommand("catalog", "Каталог авто"),
                new BotCommand("contact", "Связаться"),
                new BotCommand("help", "Справка"),
                new BotCommand("settings", "Настройки"),
                new BotCommand("question", "Задать вопрос")
        );
    }

    private List<BotCommand> commandsEn() {
        return List.of(
                new BotCommand("start", "Start using"),
                new BotCommand("menu", "Menu"),
                new BotCommand("about", "About us"),
                new BotCommand("catalog", "Car catalog"),
                new BotCommand("contact", "Contact"),
                new BotCommand("help", "Reference"),
                new BotCommand("settings", "Settings"),
                new BotCommand("question", "Ask a question")
        );
    }

    @Bean
    public String botName() {
        return botName;
    }

    @Bean
    public String botToken() {
        return botToken;
    }

}
