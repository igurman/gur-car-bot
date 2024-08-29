package com.igurman.gur_car_bot.service.common;

import com.igurman.gur_car_bot.constant.StatusUserType;
import com.igurman.gur_car_bot.model.entity.UserEntity;
import com.igurman.gur_car_bot.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public UserEntity save(UserEntity user) {
        return userRepository.save(user);
    }

    public Optional<UserEntity> findById(Long userId) {
        return userRepository.findById(userId);
    }

    @Transactional
    public void turnWrite(Long userId) {
        if (userId == null) {
            return;
        }
        UserEntity user = this.findById(userId).orElse(null);
        if (user == null) {
            return;
        }
        user.setStatus(StatusUserType.WRITE);
    }

    @Transactional
    public void turnRead(Long userId) {
        if (userId == null) {
            return;
        }
        UserEntity user = this.findById(userId).orElse(null);
        if (user == null) {
            return;
        }
        user.setStatus(StatusUserType.READ);
    }

}
