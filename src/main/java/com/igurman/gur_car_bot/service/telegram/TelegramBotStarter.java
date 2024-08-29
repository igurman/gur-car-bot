package com.igurman.gur_car_bot.service.telegram;

import com.igurman.gur_car_bot.constant.MessageType;
import com.igurman.gur_car_bot.constant.StatusUserType;
import com.igurman.gur_car_bot.model.dto.UpdateDto;
import com.igurman.gur_car_bot.service.common.UserService;
import com.igurman.gur_car_bot.service.telegram.handler.CommandHandler;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class TelegramBotStarter {
    private final UpdateMapper updateMapper;
    private final UserService userService;
    private final List<CommandHandler> commandHandlers;

    public @NonNull List<Object> starter(Update update) {
        UpdateDto updateDto = updateMapper.toDto(update);
        List<Object> messages = new ArrayList<>();

        if (Boolean.FALSE.equals(updateDto.getUser().getActivity())) {
            return messages;
        }

        // сброс статуса пользователя в "чтение" на не мессадж типах сообщений
        // если пользователь хотел написать сообщение и вышел
        if (!MessageType.MESSAGE.equals(updateDto.getMessageType())
            && StatusUserType.WRITE.equals(updateDto.getUser().getStatus())) {
            userService.turnRead(updateDto.getUser().getId());
        }

        for (CommandHandler executor : commandHandlers) {
            if (executor.canExecute(updateDto)) {
                messages = executor.executeList(updateDto);
                break;
            }
        }
        return messages;
    }
}
