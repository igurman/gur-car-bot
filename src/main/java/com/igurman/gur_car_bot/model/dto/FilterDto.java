package com.igurman.gur_car_bot.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class FilterDto {
    private Long userId;
    private List<MakeModelDto> makeModelList;
    private Map<Integer, Set<Integer>> makeModelMap;
    private Integer yearStart;
    private Integer yearEnd;
    private List<Integer> engineTypes;
    private String city;
    private Integer odometerStart;
    private Integer odometerEnd;
    private Integer priceStart;
    private Integer priceEnd;
    private Integer gradeStart;
    private Integer gradeEnd;

    public FilterDto() {
        this.init();
    }

    private void init() {
        this.makeModelList = new ArrayList<>();
        this.makeModelMap = new HashMap<>();
    }



    public void setMakeModelList(List<MakeModelDto> makeModelList) {
        if (CollectionUtils.isEmpty(makeModelList)) {
            this.init();
            return;
        }
        this.makeModelList = makeModelList;
        this.makeModelMap = makeModelList.stream()
                .collect(Collectors.toMap(MakeModelDto::getMakeId, a -> a.getModels().stream()
                        .map(ModelDto::getId)
                        .collect(Collectors.toSet())));
    }
}
