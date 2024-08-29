package com.igurman.gur_car_bot.service.telegram.handler;

import com.igurman.gur_car_bot.model.dto.UpdateDto;

import java.util.ArrayList;
import java.util.List;

public interface CommandHandler {

    boolean canExecute(UpdateDto updateDto);

    default Object execute(UpdateDto updateDto) {
        return null;
    }

    default List<Object> executeList(UpdateDto updateDto) {
        Object execute = execute(updateDto);
        return execute != null
                ? List.of(execute)
                : new ArrayList<>();
    }

}
