package com.igurman.gur_car_bot.service.provider;

import com.igurman.gur_car_bot.mapper.FilterMapper;
import com.igurman.gur_car_bot.model.dto.FilterDto;
import com.igurman.gur_car_bot.model.dto.MakeModelDto;
import com.igurman.gur_car_bot.model.dto.ModelDto;
import com.igurman.gur_car_bot.model.entity.FilterEntity;
import com.igurman.gur_car_bot.model.entity.VehicleEntity;
import com.igurman.gur_car_bot.model.entity.VinModelEntity;
import com.igurman.gur_car_bot.repository.VinModelRepository;
import com.igurman.gur_car_bot.service.common.FilterService;
import com.igurman.gur_car_bot.service.common.VehicleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class FilterProvider {
    private final FilterService filterService;
    private final FilterMapper filterMapper;
    private final VehicleService vehicleService;
    private final VinModelRepository vinModelRepository;


    public void yearClean(Long userId) {
        if (userId == null) {
            return;
        }
        filterService.yearClean(userId);
    }

    public void setYearStart(Long userId, Integer value) {
        if (userId == null) {
            return;
        }
        filterService.setYearStart(userId, value);
    }

    public void setYearEnd(Long userId, Integer value) {
        if (userId == null) {
            return;
        }
        filterService.setYearEnd(userId, value);
    }

    public void deleteModel(Long userId, Integer makeId, Integer modelId) {
        if (userId == null || makeId == null || modelId == null) {
            return;
        }
        filterService.deleteModel(userId, makeId, Set.of(modelId));
    }

    public void addModel(Long userId, Integer makeId, Integer modelId) {
        if (userId == null || makeId == null || modelId == null) {
            return;
        }
        filterService.addModels(userId, makeId, Set.of(modelId));
    }

    public FilterDto getFilter(Long userId) {
        if (userId == null) {
            return new FilterDto();
        }

        FilterEntity filterEntity = filterService.findFilterByUserId(userId).orElse(null);
        FilterDto filterDto = filterMapper.toDto(filterEntity);
        if (filterDto == null || filterEntity == null) {
            return new FilterDto();
        }
        filterDto.setMakeModelList(getMakeModelList(filterEntity));
        return filterDto;
    }

    public void deleteAllModel(Long userId, Integer makeId) {
        filterService.deleteAllModelByMake(userId, makeId);
    }

    public void deleteFilter(Long userId) {
        filterService.deleteFilterByUserId(userId);
    }

    private List<MakeModelDto> getMakeModelList(FilterEntity source) {
        if (source.getMakeModel() == null) {
            return new ArrayList<>();
        }
        return source.getMakeModel().entrySet().stream()
                .map(a -> {
                    VehicleEntity vehicleEntity = vehicleService.findMakeByMakeId(a.getKey());
                    if (vehicleEntity == null) {
                        return null;
                    }

                    MakeModelDto result = new MakeModelDto();
                    result.setMakeId(vehicleEntity.getMakeId());
                    result.setMakeName(vehicleEntity.getMakeTitle());
                    List<VinModelEntity> vinModelEntityList = vinModelRepository.findAllById(a.getValue());

                    result.setModels(vinModelEntityList.stream()
                            .map(model -> new ModelDto(model.getId(), model.getName()))
                            .sorted(Comparator.comparing(c -> c.getName().toLowerCase()))
                            .collect(Collectors.toList()));

                    return result;
                })
                .filter(Objects::nonNull)
                .sorted(Comparator.comparing(c -> c.getMakeName().toLowerCase()))
                .collect(Collectors.toList());
    }

    public void addAll(Long userId, Integer makeId) {
        filterService.addModels(userId, makeId, Set.of(0));
    }

    public void priceClean(Long userId) {
        if (userId == null) {
            return;
        }
        filterService.priceClean(userId);
    }

    public void setPriceStart(Long userId, Integer value) {
        if (userId == null) {
            return;
        }
        filterService.setPriceStart(userId, value);
    }

    public void setPriceEnd(Long userId, Integer value) {
        if (userId == null) {
            return;
        }
        filterService.setPriceEnd(userId, value);
    }

    public void odometerClean(Long userId) {
        if (userId == null) {
            return;
        }
        filterService.odometerClean(userId);
    }

    public void setOdometerStart(Long userId, Integer value) {
        if (userId == null) {
            return;
        }
        filterService.setOdometerStart(userId, value);
    }

    public void setOdometerEnd(Long userId, Integer value) {
        if (userId == null) {
            return;
        }
        filterService.setOdometerEnd(userId, value);
    }

    public void engineTypeClean(Long userId) {
        if (userId == null) {
            return;
        }
        filterService.engineTypeClean(userId);
    }

    public void addEngineType(Long userId, Integer value) {
        if (userId == null || value == null) {
            return;
        }
        filterService.addEngineType(userId, Set.of(value));
    }

    public void deleteEngineType(Long userId, Integer value) {
        if (userId == null || value == null) {
            return;
        }
        filterService.deleteEngineType(userId, Set.of(value));
    }

    public List<Integer> findAllEngineByUser(Long userId) {
        FilterEntity filterByUserId = filterService.findFilterByUserId(userId).orElse(null);
        if (filterByUserId != null && !CollectionUtils.isEmpty(filterByUserId.getEngineTypes())) {
            return new ArrayList<>(filterByUserId.getEngineTypes());

        }
        return new ArrayList<>();
    }

    public void gradeClean(Long userId) {
        if (userId == null) {
            return;
        }
        filterService.gradeClean(userId);
    }

    public void setGradeStart(Long userId, Integer value) {
        if (userId == null) {
            return;
        }
        filterService.setGradeStart(userId, value);
    }

    public void setGradeEnd(Long userId, Integer value) {
        if (userId == null) {
            return;
        }
        filterService.setGradeEnd(userId, value);
    }
}
