package com.igurman.gur_car_bot.service.common;

import com.igurman.gur_car_bot.constant.PosterStatusType;
import com.igurman.gur_car_bot.constant.PosterUserType;
import com.igurman.gur_car_bot.model.entity.PostEntity;
import com.igurman.gur_car_bot.repository.PostRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;

    public PostEntity save(PostEntity postEntity) {
        if (postEntity == null) {
            return null;
        }
        return postRepository.save(postEntity);
    }

    public List<PostEntity> findForGroupPost(@NonNull Integer count) {
        return postRepository.findAllByUserTypeAndStatus(
                PosterUserType.GROUP,
                PosterStatusType.CREATED,
                PageRequest.of(0, count));
    }

    public List<PostEntity> findForGroupDelete(@NonNull Integer count) {
        return postRepository.findForDelete(
                PosterUserType.GROUP,
                List.of(PosterStatusType.POSTED, PosterStatusType.DELETED_ERROR),
                LocalDate.now(),
                LocalDateTime.now().minusDays(20),
                PageRequest.of(0, count));
    }

    public boolean existsByVehicleIdAndUserType(Integer vehicleId) {
        return postRepository.existsByVehicleIdAndUserType(vehicleId, PosterUserType.GROUP);
    }

}
