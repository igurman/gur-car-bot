package com.igurman.gur_car_bot.util;

import com.igurman.gur_car_bot.constant.TimerType;
import com.igurman.gur_car_bot.exception.ParserRuntimeException;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;

@UtilityClass
public class TimerUtil {
    private final ConcurrentMap<TimerType, ConcurrentLinkedQueue<Long>> timeMap = new ConcurrentHashMap<>();
    private static final Long TIME_LIMIT = 1_000_000_000L; // 1_000_000_000 = 1сек
    private static final Integer COUNT_LIMIT = 3;
    private static final Integer BYPASS_LIMIT = 1000; // лимит количества циклов
    private static final Integer SLEEP_TIME = 100; // милисекунды, на сколько засыпать между проверками

    /**
     * создаёт цикл, пока таймер запрещает идти дальше
     * как только таймер дал разрешение - выходит из цикла
     */
    @SneakyThrows
    public void getPause(TimerType name) {
        for (int i = 0; i < BYPASS_LIMIT; i++) {
            if (!isSleep(name)) {
                return;
            } else {
                Thread.sleep(SLEEP_TIME);
            }
        }
        throw new ParserRuntimeException("Не смог получить разрешение на временной лимит за " + BYPASS_LIMIT + " циклов");
    }

    /**
     * в потокобезопасном режиме смотрит сколько раз за определенное кол-во времени пришёл запрос
     * напр. если нужно пропускать 3 запроса в секунду, то будет выдавать true, если лимит запросов уже исчерпан
     */
    private boolean isSleep(TimerType name) {
        long currentTime = System.nanoTime();

        ConcurrentLinkedQueue<Long> timeBox = timeMap.getOrDefault(name, new ConcurrentLinkedQueue<>());

        if (timeBox.size() < COUNT_LIMIT) {
            timeBox.add(currentTime);
            timeMap.put(name, timeBox);
            return false;
        }

        long oldTime = (timeBox.peek() != null) ? timeBox.peek() : 0;
        if (currentTime - oldTime <= TIME_LIMIT) {
            return true;
        }

        timeBox.poll();
        timeBox.add(currentTime);
        timeMap.put(name, timeBox);
        return false;
    }

}
