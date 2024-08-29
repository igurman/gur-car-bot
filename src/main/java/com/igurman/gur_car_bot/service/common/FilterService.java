package com.igurman.gur_car_bot.service.common;

import com.igurman.gur_car_bot.model.entity.FilterEntity;
import com.igurman.gur_car_bot.repository.FilterRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilterService {
    private final FilterRepository filterRepository;

    public void yearClean(Long userId) {
        if (userId == null) {
            return;
        }
        FilterEntity filterEntity = this.findFilterByUserId(userId).orElse(null);
        if (filterEntity == null) {
            return;
        }
        filterEntity.setYearStart(null);
        filterEntity.setYearEnd(null);
        filterRepository.save(filterEntity);
    }

    public void setYearStart(Long userId, Integer value) {
        if (userId == null) {
            return;
        }
        FilterEntity filterEntity = this.findFilterByUserId(userId)
                .orElseGet(() -> this.initModel(userId));
        filterEntity.setYearStart(value);
        filterRepository.save(filterEntity);
    }

    public void setYearEnd(Long userId, Integer value) {
        if (userId == null) {
            return;
        }
        FilterEntity filterEntity = this.findFilterByUserId(userId)
                .orElseGet(() -> this.initModel(userId));
        filterEntity.setYearEnd(value);
        filterRepository.save(filterEntity);
    }

    public Optional<FilterEntity> findFilterByUserId(Long userId) {
        if (userId == null) {
            return Optional.empty();
        }
        return filterRepository.findByUserId(userId);
    }

    public void addModels(Long userId, Integer makeId, Set<Integer> modeIdList) {
        if (userId == null || makeId == null || CollectionUtils.isEmpty(modeIdList)) {
            return;
        }
        FilterEntity filterEntity = this.findFilterByUserId(userId)
                .orElseGet(() -> this.initModel(userId));
        filterEntity.getMakeModel().computeIfPresent(makeId, (a, b) -> {
            b.addAll(modeIdList);
            return b;
        });

        filterEntity.getMakeModel().putIfAbsent(makeId, modeIdList);

        filterRepository.save(filterEntity);
    }

    public void deleteModel(Long userId, Integer makeId, Set<Integer> modeIdList) {
        if (userId == null || makeId == null || CollectionUtils.isEmpty(modeIdList)) {
            return;
        }
        FilterEntity filterEntityUpdate = this.findFilterByUserId(userId).orElse(null);
        if (filterEntityUpdate == null) {
            return;
        }

        filterEntityUpdate.getMakeModel().computeIfPresent(makeId, (a, b) -> {
            b.removeAll(modeIdList);
            return b;
        });

        Map<Integer, Set<Integer>> makeModelClean =  filterEntityUpdate.getMakeModel().entrySet().stream()
                .filter(pair -> !CollectionUtils.isEmpty(pair.getValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        filterEntityUpdate.setMakeModel(makeModelClean);

        filterRepository.save(filterEntityUpdate);
    }

    @Transactional
    public void deleteFilterByUserId(Long userId) {
        filterRepository.deleteAllByUserId(userId);
    }

    public void deleteAllModelByMake(Long userId, Integer makeId) {
       FilterEntity filterEntity = this.findFilterByUserId(userId).orElse(null);
        if (filterEntity == null) return;
        filterEntity.getMakeModel().remove(makeId);
        filterRepository.save(filterEntity);
    }

    public void priceClean(Long userId) {
        if (userId == null) {
            return;
        }
        FilterEntity filterEntity = this.findFilterByUserId(userId).orElse(null);
        if (filterEntity == null) {
            return;
        }
        filterEntity.setPriceStart(null);
        filterEntity.setPriceEnd(null);
        filterRepository.save(filterEntity);
    }

    public void setPriceStart(Long userId, Integer value) {
        if (userId == null) {
            return;
        }
        FilterEntity filterEntity = this.findFilterByUserId(userId)
                .orElseGet(() -> this.initModel(userId));
        filterEntity.setPriceStart(value);
        filterRepository.save(filterEntity);
    }

    public void setPriceEnd(Long userId, Integer value) {
        if (userId == null) {
            return;
        }
        FilterEntity filterEntity = this.findFilterByUserId(userId)
                .orElseGet(() -> this.initModel(userId));
        filterEntity.setPriceEnd(value);
        filterRepository.save(filterEntity);
    }

    public void odometerClean(Long userId) {
        if (userId == null) {
            return;
        }
        FilterEntity filterEntity = this.findFilterByUserId(userId).orElse(null);
        if (filterEntity == null) {
            return;
        }
        filterEntity.setOdometerStart(null);
        filterEntity.setOdometerEnd(null);
        filterRepository.save(filterEntity);
    }

    private FilterEntity initModel(Long userId) {
        FilterEntity model = new FilterEntity();
        model.setUserId(userId);
        model.setMakeModel(new HashMap<>());
        model.setEngineTypes(new HashSet<>());
        return model;
    }

    public void setOdometerStart(Long userId, Integer value) {
        if (userId == null) {
            return;
        }
        FilterEntity filterEntity = this.findFilterByUserId(userId)
                .orElseGet(() -> this.initModel(userId));
        filterEntity.setOdometerStart(value);
        filterRepository.save(filterEntity);
    }

    public void setOdometerEnd(Long userId, Integer value) {
        if (userId == null) {
            return;
        }
        FilterEntity filterEntity = this.findFilterByUserId(userId)
                .orElseGet(() -> this.initModel(userId));
        filterEntity.setOdometerEnd(value);
        filterRepository.save(filterEntity);
    }

    public void engineTypeClean(Long userId) {
        if (userId == null) {
            return;
        }
        FilterEntity filterEntity = this.findFilterByUserId(userId).orElse(null);
        if (filterEntity == null) {
            return;
        }
        filterEntity.setEngineTypes(null);
        filterRepository.save(filterEntity);
    }

    public void addEngineType(Long userId, Set<Integer> valueSet) {
        if (userId == null ||  CollectionUtils.isEmpty(valueSet)) {
            return;
        }
        FilterEntity filterEntity = this.findFilterByUserId(userId)
                .orElseGet(() -> this.initModel(userId));

        if (CollectionUtils.isEmpty(filterEntity.getEngineTypes())) {
            filterEntity.setEngineTypes(new HashSet<>());
        }
        filterEntity.getEngineTypes().addAll(valueSet);

        filterRepository.save(filterEntity);
    }

    public void deleteEngineType(Long userId, Set<Integer> valueSet) {
        if (userId == null || CollectionUtils.isEmpty(valueSet)) {
            return;
        }
        FilterEntity filterEntity = this.findFilterByUserId(userId).orElse(null);
        if (filterEntity == null || CollectionUtils.isEmpty(filterEntity.getEngineTypes())) {
            return;
        }
        filterEntity.getEngineTypes().removeAll(valueSet);
        filterRepository.save(filterEntity);
    }

    public void gradeClean(Long userId) {
        if (userId == null) {
            return;
        }
        FilterEntity filterEntity = this.findFilterByUserId(userId).orElse(null);
        if (filterEntity == null) {
            return;
        }
        filterEntity.setGradeStart(null);
        filterEntity.setGradeEnd(null);
        filterRepository.save(filterEntity);
    }

    public void setGradeStart(Long userId, Integer value) {
        if (userId == null) {
            return;
        }
        FilterEntity filterEntity = this.findFilterByUserId(userId)
                .orElseGet(() -> this.initModel(userId));
        filterEntity.setGradeStart(value);
        filterRepository.save(filterEntity);
    }

    public void setGradeEnd(Long userId, Integer value) {
        if (userId == null) {
            return;
        }
        FilterEntity filterEntity = this.findFilterByUserId(userId)
                .orElseGet(() -> this.initModel(userId));
        filterEntity.setGradeEnd(value);
        filterRepository.save(filterEntity);
    }

}
