package com.igurman.gur_car_bot.repository.specification;

import com.igurman.gur_car_bot.constant.AuctionType;
import com.igurman.gur_car_bot.model.entity.VehicleEntity;
import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.CollectionUtils;

import java.util.Set;

@UtilityClass
public class VehicleEntitySpecifications {

    public Specification<VehicleEntity> addCommon() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();
    }

    public static Specification<VehicleEntity> modelIn(Set<Integer> valueList) {
        return (root, query, criteriaBuilder) -> {
            if (CollectionUtils.isEmpty(valueList)) {
                return criteriaBuilder.conjunction();
            }
            return root.get("modelId").in(valueList);
        };
    }

    public static Specification<VehicleEntity> yearMoreOrEquals(Integer value) {
        return (root, query, criteriaBuilder) -> {
            if (value == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.greaterThanOrEqualTo(root.get("year"), value);
        };
    }

    public static Specification<VehicleEntity> yearLessOrEquals(Integer value) {
        return (root, query, criteriaBuilder) -> {
            if (value == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.lessThanOrEqualTo(root.get("year"), value);
        };
    }

    public static Specification<VehicleEntity> makeEq(Integer value) {
        return (root, query, criteriaBuilder) -> {
            if (value == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("makeId"), value);
        };
    }

    public static Specification<VehicleEntity> odometerMoreOrEquals(Integer value) {
        return (root, query, criteriaBuilder) -> {
            if (value == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.greaterThanOrEqualTo(root.get("odometer"), value);
        };
    }

    public static Specification<VehicleEntity> odometerLessOrEquals(Integer value) {
        return (root, query, criteriaBuilder) -> {
            if (value == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.lessThanOrEqualTo(root.get("odometer"), value);
        };
    }

    public static Specification<VehicleEntity> priceMoreOrEquals(Integer value) {
        return (root, query, criteriaBuilder) -> {
            if (value == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.greaterThanOrEqualTo(root.get("price"), value);
        };
    }

    public static Specification<VehicleEntity> priceLessOrEquals(Integer value) {
        return (root, query, criteriaBuilder) -> {
            if (value == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.lessThanOrEqualTo(root.get("price"), value);
        };
    }

    public static Specification<VehicleEntity> engineEqualsIn(Set<String> valueList) {
        return (root, query, criteriaBuilder) -> {
            if (CollectionUtils.isEmpty(valueList)) {
                return criteriaBuilder.conjunction();
            }
            return root.get("engineType").in(valueList);
        };
    }

    public static Specification<VehicleEntity> auctionEqualsIn(Set<AuctionType> valueList) {
        return (root, query, criteriaBuilder) -> {
            if (CollectionUtils.isEmpty(valueList)) {
                return criteriaBuilder.conjunction();
            }
            return root.get("auction").in(valueList);
        };
    }

    public static Specification<VehicleEntity> gradeMoreOrEquals(Integer value) {
        return (root, query, criteriaBuilder) -> {
            if (value == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.greaterThanOrEqualTo(root.get("grade"), value);
        };
    }

    public static Specification<VehicleEntity> gradeLessOrEquals(Integer value) {
        return (root, query, criteriaBuilder) -> {
            if (value == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.lessThanOrEqualTo(root.get("grade"), value);
        };
    }

}
