package com.igurman.gur_car_bot.service.telegram.handler.fragment;

import com.igurman.gur_car_bot.constant.I18nType;
import com.igurman.gur_car_bot.constant.LanguageType;
import com.igurman.gur_car_bot.dictionary.EngineTypeTypeDictionary;
import com.igurman.gur_car_bot.model.dto.FilterDto;
import com.igurman.gur_car_bot.model.dto.ModelDto;
import com.igurman.gur_car_bot.model.dto.UpdateDto;
import com.igurman.gur_car_bot.service.provider.FilterProvider;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.stream.Collectors;

import static com.igurman.gur_car_bot.constant.I18nType.CATALOG_MESSAGE;
import static com.igurman.gur_car_bot.dictionary.I18nDictionary.COLON_SPACE;
import static com.igurman.gur_car_bot.dictionary.I18nDictionary.COMMA_SPACE;
import static com.igurman.gur_car_bot.dictionary.I18nDictionary.EMPTY;
import static com.igurman.gur_car_bot.dictionary.I18nDictionary.FROM_TO;
import static com.igurman.gur_car_bot.dictionary.I18nDictionary.NEXT_LINE;
import static com.igurman.gur_car_bot.dictionary.I18nDictionary.SPACE;
import static com.igurman.gur_car_bot.dictionary.I18nDictionary.getI18n;
import static com.igurman.gur_car_bot.util.FormatUtil.formatGrade;
import static com.igurman.gur_car_bot.util.FormatUtil.formatInteger;
import static com.igurman.gur_car_bot.util.FormatUtil.formatPrice;
import static java.lang.String.format;

@Component
@RequiredArgsConstructor
public class FilterShowFragment {
    protected final FilterProvider filterProvider;
    protected final EngineTypeTypeDictionary engineTypeTypeDictionary;

    public String getView(UpdateDto updateDto) {
        FilterDto filter = filterProvider.getFilter(updateDto.getUser().getId());
        if (filter.getUserId() != null) {
            return getI18n(CATALOG_MESSAGE, updateDto.getLang(),
                    getModels(filter, updateDto.getLang()), // Модели
                    getEngineTypes(filter, updateDto.getLang()), // Типы двигателя
                    getYears(filter), // Годы от - до
                    getOdometer(filter), // Пробег от - до
                    getPrice(filter), // Цены от - до
                    getGrade(filter) // Цены от - до
            );
        }
        return getI18n(CATALOG_MESSAGE, updateDto.getLang(), SPACE, SPACE, SPACE, SPACE, SPACE, SPACE);
    }

    private String getModels(FilterDto filter, LanguageType lang) {
        String lineModels = filter.getMakeModelList().stream()
                .map(make -> {
                    String result = "<u>" + make.getMakeName() + "</u>" + COLON_SPACE;
                    String list = make.getModels().stream()
                            .map(ModelDto::getName)
                            .collect(Collectors.joining(COMMA_SPACE));

                    // если в списке ничего нет, значит выбраны все
                    return result + (StringUtils.isNotEmpty(list) ? list : getI18n(I18nType.MODEL_ALL, lang));
                })
                .collect(Collectors.joining(NEXT_LINE));
        if (filter.getMakeModelList().size() > 1) {
            return NEXT_LINE + lineModels;
        }
        return lineModels;
    }

    private String getPrice(FilterDto filter) {
        return (filter.getPriceStart() != null
                || filter.getPriceEnd() != null)
                ? format(FROM_TO, formatPrice(filter.getPriceStart()), formatPrice(filter.getPriceEnd()))
                : EMPTY;
    }

    private String getGrade(FilterDto filter) {
        return (filter.getGradeStart() != null
                || filter.getGradeEnd() != null)
                ? format(FROM_TO, formatGrade(filter.getGradeStart()), formatGrade(filter.getGradeEnd()))
                : EMPTY;
    }

    private String getOdometer(FilterDto filter) {
        return (filter.getOdometerStart() != null || filter.getOdometerEnd() != null)
                ? format(FROM_TO, formatInteger(filter.getOdometerStart()), formatInteger(filter.getOdometerEnd()))
                : EMPTY;
    }

    private String getEngineTypes(FilterDto filter, LanguageType lang) {
        return (!CollectionUtils.isEmpty(filter.getEngineTypes()))
                ? filter.getEngineTypes().stream()
                .map(type -> getI18n(I18nType.valueOf(engineTypeTypeDictionary.getValue(String.valueOf(type))), lang).toLowerCase())
                .collect(Collectors.joining(COMMA_SPACE))
                : EMPTY;
    }

    private String getYears(FilterDto filter) {
        if (filter.getYearStart() != null || filter.getYearEnd() != null) {
            String yearStart = filter.getYearStart() != null ? String.valueOf(filter.getYearStart()) : SPACE;
            String yearEnd = filter.getYearEnd() != null ? String.valueOf(filter.getYearEnd()) : SPACE;
            return format(FROM_TO, yearStart, yearEnd);
        }
        return EMPTY;
    }
}
