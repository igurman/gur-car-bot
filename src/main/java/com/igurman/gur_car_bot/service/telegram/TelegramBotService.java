package com.igurman.gur_car_bot.service.telegram;

import com.igurman.gur_car_bot.util.ObjectMapperProvider;
import com.igurman.gur_car_bot.util.UsefulUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMediaGroup;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.send.SendSticker;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

import java.util.List;

@Slf4j
@Service
public class TelegramBotService extends TelegramLongPollingBot {
    private final ObjectMapperProvider objectMapper;
    private final TelegramBotStarter telegramBotStarter;
    private final String botName;

    TelegramBotService(String botName, String botToken
            , ObjectMapperProvider objectMapper, TelegramBotStarter telegramBotStarter) {
        super(botToken);
        this.telegramBotStarter = telegramBotStarter;
        this.objectMapper = objectMapper;
        this.botName = botName;
    }

    @Override
    public void onUpdateReceived(Update update) {
        log.info("*** Получил: {}", objectMapper.toJson(update));
        executeListWithoutAnswer(telegramBotStarter.starter(update));
    }

    private void executeListWithoutAnswer(List<Object> messageList) {
        for (Object message : messageList) {
            executeWithoutAnswer(message, 0);
        }
    }

    private void executeWithoutAnswer(Object o, int i) {
        log.info("*** Пробую отправить сообщение: {}", objectMapper.toJson(o));
        try {
            if (o instanceof SendPhoto message) {
                execute(message);
            } else if (o instanceof SendMessage message) {
                execute(message);
            } else if (o instanceof EditMessageText message) {
                execute(message);
            } else if (o instanceof DeleteMessage message) {
                execute(message);
            } else if (o instanceof AnswerCallbackQuery message) {
                execute(message);
            } else if (o instanceof SendMediaGroup message) {
                execute(message);
            } else if (o instanceof SendSticker message) {
                execute(message);
            } else {
                log.error("*** Не смог распознать ТИП сообщения для поиска нужного execute-ра");
            }
        } catch (TelegramApiRequestException e) {
            int freezeSec = UsefulUtil.calcFreeze(e);
            if (freezeSec > 0 && i < 5) {
                try {
                    log.error("*** Заснули base sender на {}ms", freezeSec);
                    Thread.sleep(freezeSec);
                } catch (InterruptedException ex) {
                    log.error("*** Упали base sender когда проснулись", ex);
                    Thread.currentThread().interrupt();
                }
                log.error("*** Проснулись base sender после {}ms", freezeSec);
                i = i + 1;
                executeWithoutAnswer(o, i);
            } else {
                log.error("*** Не смог отправить пост base sender ", e);
            }
        } catch (TelegramApiException e) {
            log.error("*** Не смог отправить сообщение base sender: " + e);
        }
    }

    @Override
    public String getBotUsername() {
        return botName;
    }

}
