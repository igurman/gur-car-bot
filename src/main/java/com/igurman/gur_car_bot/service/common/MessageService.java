package com.igurman.gur_car_bot.service.common;

import com.igurman.gur_car_bot.constant.MessageStatusType;
import com.igurman.gur_car_bot.model.entity.MessageEntity;
import com.igurman.gur_car_bot.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;

    public void saveNewMessage(Long userId, String message) {
        if (userId == null || StringUtils.isEmpty(message)) {
            return;
        }

        MessageEntity model = MessageEntity.builder()
                .userId(userId)
                .message(message)
                .status(MessageStatusType.NEW)
                .build();

        messageRepository.save(model);
    }

    public void saveOrderMessage(Long userId, String message) {
        if (userId == null || StringUtils.isEmpty(message)) {
            return;
        }

        MessageEntity model = MessageEntity.builder()
                .userId(userId)
                .message(message)
                .status(MessageStatusType.ORDER)
                .build();

        messageRepository.save(model);
    }

    public List<MessageEntity> findAllByUserId(Long userId) {
        return messageRepository.findAllByUserId(userId);
    }

}
