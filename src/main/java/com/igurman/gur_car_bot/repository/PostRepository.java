package com.igurman.gur_car_bot.repository;

import com.igurman.gur_car_bot.constant.PosterStatusType;
import com.igurman.gur_car_bot.constant.PosterUserType;
import com.igurman.gur_car_bot.model.entity.PostEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface PostRepository extends JpaRepository<PostEntity, Integer> {

    List<PostEntity> findAllByUserTypeAndStatus(PosterUserType posterUserType
            , PosterStatusType posterStatusType, Pageable pageable);

    @Query("from PostEntity p where p.userType = :userType and p.status in (:statuses) and (p.deleteDate < :deleteDate or p.createDate < :createDate)")
    List<PostEntity> findForDelete(PosterUserType userType, List<PosterStatusType> statuses, LocalDate deleteDate, LocalDateTime createDate, Pageable pageable);

    boolean existsByVehicleIdAndUserType(Integer vehicleId, PosterUserType userType);
}
