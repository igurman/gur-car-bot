package com.igurman.gur_car_bot.repository;

import com.igurman.gur_car_bot.constant.AuctionType;
import com.igurman.gur_car_bot.constant.VehicleStatusType;
import com.igurman.gur_car_bot.model.entity.VehicleEntity;
import org.springframework.data.domain.Limit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface VehicleRepository extends JpaRepository<VehicleEntity, Integer> {

    Page<VehicleEntity> findAll(Specification<VehicleEntity> spec, Pageable pageable);

    List<VehicleEntity> findAllByEngineTypeIdIsNull();

    @Query("select distinct v.makeId, v.makeTitle from VehicleEntity v where v.status in (:statusList) order by v.makeTitle")
    Page<Object[]> findAllDistinctMake(List<VehicleStatusType> statusList, Pageable pageable);

    @Query("select distinct v.modelId, v.modelTitle from VehicleEntity v where v.makeId = :makeId and v.status  in (:statusList) order by v.modelTitle")
    Page<Object[]> findAllDistinctModelByMake(Integer makeId, List<VehicleStatusType> statusLis, Pageable pageable);

    @Query("select v from VehicleEntity v where v.makeId = :makeId order by v.makeId limit 1")
    Optional<VehicleEntity> findMakeByMakeId(Integer makeId);

    Optional<VehicleEntity> findByVin(String vin);

    List<VehicleEntity> findAllByStatusAndAuction(VehicleStatusType status, AuctionType auction, Limit limit);

    @Query("from VehicleEntity v where v.status = :status and v.picture is not null and v.createDate >= :createDate")
    List<VehicleEntity> findForPublisherWithStatusAndCreateDate(VehicleStatusType status, LocalDateTime createDate, Pageable pageable);
}
