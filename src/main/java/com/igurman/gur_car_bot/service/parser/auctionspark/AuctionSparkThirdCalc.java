package com.igurman.gur_car_bot.service.parser.auctionspark;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.igurman.gur_car_bot.constant.AuctionType;
import com.igurman.gur_car_bot.constant.ColorType;
import com.igurman.gur_car_bot.constant.DriveTrainType;
import com.igurman.gur_car_bot.constant.EngineType;
import com.igurman.gur_car_bot.constant.PictureType;
import com.igurman.gur_car_bot.constant.TransmissionType;
import com.igurman.gur_car_bot.constant.VehicleStatusType;
import com.igurman.gur_car_bot.constant.resolver.ColorTypeResolver;
import com.igurman.gur_car_bot.constant.resolver.DriveTrainTypeResolver;
import com.igurman.gur_car_bot.constant.resolver.EngineTypeResolver;
import com.igurman.gur_car_bot.constant.resolver.TransmissionTypeResolver;
import com.igurman.gur_car_bot.model.dto.auction.auctionspark.Details;
import com.igurman.gur_car_bot.model.dto.auction.auctionspark.AuctionSparkModel;
import com.igurman.gur_car_bot.model.entity.PictureEntity;
import com.igurman.gur_car_bot.model.entity.VehicleEntity;
import com.igurman.gur_car_bot.model.entity.VinMakeEntity;
import com.igurman.gur_car_bot.model.entity.VinModelEntity;
import com.igurman.gur_car_bot.repository.VinMakeRepository;
import com.igurman.gur_car_bot.service.common.PictureService;
import com.igurman.gur_car_bot.service.common.VehicleService;
import com.igurman.gur_car_bot.util.ObjectMapperProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuctionSparkThirdCalc {
    private final ObjectMapperProvider objectMapperProvider;
    private final VehicleService vehicleService;
    private final VinMakeRepository vinMakeRepository;
    private final PictureService pictureService;
    @Value("${application.parser.auctionspark.calculation.count-calc:50}")
    private int countCalc;

    public void execute() {
        log.info("*** Пробуем распарсить json для сайта spark_com");
        // получим из БД несколько записей
        List<VehicleEntity> vehicleEntityList = vehicleService.findAllByStatusAndAuction(
                VehicleStatusType.AWAIT_CALC,
                AuctionType.AUCTION_SPARK,
                countCalc);

        if (vehicleEntityList.isEmpty()) {
            return;
        }
        log.info("*** Получили записи из БД для парсинга модели для сайта spark_com, кол-во: {}",
                vehicleEntityList.size());

        vehicleEntityList.forEach(this::parsingModel);

        log.info("*** Закончили парсинга моделей для сайта spark_com");
    }

    private void parsingModel(VehicleEntity vehicle) {
        log.info("*** Начинаем парсить json для сайта spark_com, vehicle: {}", vehicle.getId());
        // распарсим данные json в модель, если что-то не так, сохраним ошибку
        AuctionSparkModel auctionSparkModel;
        try {
            auctionSparkModel = objectMapperProvider.getObjectMapper().readValue(vehicle.getAuctionData(), AuctionSparkModel.class);
        } catch (JsonProcessingException e) {
            vehicle.setError(e.getMessage());
            vehicle.setStatus(VehicleStatusType.FAIL_PARSE);
            vehicleService.save(vehicle);
            log.error("*** ошибка при парсинге Json в модель: ", e);
            return;
        }

        // преобразуем данные с сайта в модель
        Map<String, String> errorMap = new HashMap<>();
        this.convertToModel(errorMap, vehicle, auctionSparkModel);

        // если при конвертации были ошибки, сохраним их в БД
        if (!errorMap.isEmpty()) {
            vehicle.setError(errorMap.toString());
            vehicle.setStatus(VehicleStatusType.FAIL_PARSE);
            log.error("*** ошибка при парсинге данных в модель ентити: {}", errorMap);
        } else {
            vehicle.setError(null);
            vehicle.setStatus(VehicleStatusType.WEB);
        }

        vehicleService.save(vehicle);

        // сохраним картинки
        if (CollectionUtils.isEmpty(auctionSparkModel.getVehiclePhotos())) {
            return;
        }

        log.info("*** Нашли: {} шт. картинок для: {}, пробуем сохранить",
                auctionSparkModel.getVehiclePhotos().size(), vehicle.getId());
        List<PictureEntity> pictureList = new ArrayList<>();
        for (int i = 0; i < auctionSparkModel.getVehiclePhotos().size(); i++) {

            String picture = auctionSparkModel.getVehiclePhotos().get(i);
            String link = (picture.startsWith("//"))
                    ? "https:" + picture
                    : picture;

            PictureType pictureType = (i == 0)
                    ? PictureType.FIRST
                    : PictureType.SECOND;

            PictureEntity pictureEntity = PictureEntity.builder()
                    .vehicleId(vehicle.getId())
                    .link(link)
                    .type(pictureType)
                    .build();

            pictureList.add(pictureEntity);
        }
        log.info("*** Сохранили картинки: {} шт. для: {}", pictureList.size(), vehicle.getId());
        pictureService.saveWithCleanAll(pictureList, vehicle.getId());
    }

    private void convertToModel(Map<String, String> errorMap, VehicleEntity updateModel, AuctionSparkModel auctionSparkModel) {

        this.setMakeAndMakeId(errorMap, updateModel, auctionSparkModel);
        this.setModelAndModelId(errorMap, updateModel, auctionSparkModel);
        // комплектация
        updateModel.setComplect(getComplect(errorMap, auctionSparkModel));
        // цвет авто
        updateModel.setColor(getColor(errorMap, auctionSparkModel));
        // цвет салона
        updateModel.setInteriorColor(getInteriorColor(errorMap, auctionSparkModel));
        // год выпуска
        updateModel.setYear(getYear(errorMap, auctionSparkModel));
        // пробег
        updateModel.setOdometer(getOdometer(errorMap, auctionSparkModel));
        //vin номер
        updateModel.setVin(getVin(errorMap, auctionSparkModel));
        // место нахождения
        updateModel.setLocation(getLocation(errorMap, auctionSparkModel));
        // место стоянки
        updateModel.setLane(getLane(errorMap, auctionSparkModel));
        // продавец
        updateModel.setSeller(getSeller(errorMap, auctionSparkModel));
        // двигатель
        updateModel.setEngine(getEngine(errorMap, auctionSparkModel));
        // трансмиссия
        updateModel.setTransmission(getTransmission(errorMap, auctionSparkModel));
        //
        updateModel.setDriveTrain(getDriveTrain(errorMap, auctionSparkModel));
        // цена
        updateModel.setPrice(getPrice(errorMap, auctionSparkModel));
        // Описание
        updateModel.setAnnouncement(getAnnouncement(errorMap, auctionSparkModel));
        //
        updateModel.setTitle(getTitle(errorMap, auctionSparkModel));
        // оценка
        updateModel.setGrade(getGrade(errorMap, auctionSparkModel));
        //
        updateModel.setDriveable(getDriveable(errorMap, auctionSparkModel));
        // главное фото
        updateModel.setPicture(getPicture(errorMap, auctionSparkModel));
        // дата продажи
        updateModel.setSaleDate(getSaleDate(errorMap, auctionSparkModel));
        // id типа двигателя
        updateModel.setEngineTypeId(getEngineTypeId(errorMap, auctionSparkModel));
        // тип двигателя
        updateModel.setEngineType(getEngineType(errorMap, auctionSparkModel));
        // объем двигателя
        updateModel.setDisplacement(getDisplacement(errorMap, auctionSparkModel));
    }

    private void setMakeAndMakeId(Map<String, String> errorMap, VehicleEntity updateModel, AuctionSparkModel auctionSparkModel) {
        VinMakeEntity vinMakeEntity = vinMakeRepository.findByNameIgnoreCase(auctionSparkModel.getMake());
        if (vinMakeEntity == null) {
            errorMap.put("makeTitle", "Марка не найдена: " + auctionSparkModel.getMake());
            return;
        }
        updateModel.setMakeId(vinMakeEntity.getId());
        updateModel.setMakeTitle(vinMakeEntity.getName());
    }

    private void setModelAndModelId(Map<String, String> errorMap, VehicleEntity updateModel, AuctionSparkModel auctionSparkModel) {
        if (StringUtils.isEmpty(auctionSparkModel.getModel())) {
            errorMap.put("modelTitle", "В json пустое поле: " + auctionSparkModel.getMake());
            return;
        }
        VinMakeEntity vinMakeEntity = vinMakeRepository.findByNameIgnoreCase(auctionSparkModel.getMake());
        if (vinMakeEntity == null) {
            errorMap.put("modelTitle", "Марка не найдена: " + auctionSparkModel.getMake());
            return;
        }
        if (CollectionUtils.isEmpty(vinMakeEntity.getModels())) {
            errorMap.put("modelTitle", "Список моделей пуст: " + auctionSparkModel.getModel());
            return;
        }

        VinModelEntity vinModelEntity = vinMakeEntity.getModels().stream()
                .filter(model -> auctionSparkModel.getModel().equalsIgnoreCase(model.getName()))
                .findFirst().orElse(null);

        // если не нашли модель стандартным способом, будем костылить
        if (vinModelEntity == null) {
            // некоторые модели идут без тире или других знаков

            vinModelEntity = vinMakeEntity.getModels().stream()
                    .filter(model -> equalsWithoutDashAndCase(auctionSparkModel.getModel(), model.getName()))
                    .findFirst().orElse(null);
        }

        if (vinModelEntity == null && "BMW".equals(auctionSparkModel.getMake())
            && auctionSparkModel.getDetails() != null
            && auctionSparkModel.getDetails().getDiscrepancyTrim() != null) {

            vinModelEntity = vinMakeEntity.getModels().stream()
                    .filter(model -> auctionSparkModel.getDetails().getDiscrepancyTrim().equalsIgnoreCase(model.getName()))
                    .findFirst().orElse(null);
        }

        if (vinModelEntity == null && "BMW".equals(auctionSparkModel.getMake())
            && auctionSparkModel.getDetails() != null
            && auctionSparkModel.getDetails().getTrim() != null) {

            vinModelEntity = vinMakeEntity.getModels().stream()
                    .filter(model -> auctionSparkModel.getDetails().getTrim().equalsIgnoreCase(model.getName()))
                    .findFirst().orElse(null);
        }

        if (vinModelEntity == null && "Chevrolet".equals(auctionSparkModel.getMake())
            && auctionSparkModel.getModel().contains("Silverado")) {

            vinModelEntity = vinMakeEntity.getModels().stream()
                    .filter(model -> "Silverado".equalsIgnoreCase(model.getName()))
                    .findFirst().orElse(null);
        }

        if (vinModelEntity == null && "Alfa Romeo".equals(auctionSparkModel.getMake())
            && auctionSparkModel.getModel().contains("Giulia")) {

            vinModelEntity = vinMakeEntity.getModels().stream()
                    .filter(model -> model.getName().startsWith("Giulia"))
                    .findFirst().orElse(null);
        }

        if (vinModelEntity == null && "Mercedes-Benz".equals(auctionSparkModel.getMake())) {

            vinModelEntity = vinMakeEntity.getModels().stream()
                    .filter(model -> {
                        String newModelName = auctionSparkModel.getModel();
                        if (!auctionSparkModel.getModel().contains("Class")) {
                            newModelName = auctionSparkModel.getModel() + "-Class";
                        }

                        return equalsWithoutDashAndCase(newModelName, model.getName());
                    })
                    .findFirst().orElse(null);
        }

        if (vinModelEntity == null && "Ford".equals(auctionSparkModel.getMake())
            && auctionSparkModel.getModel().contains("Vans")) {
            String newModelName = StringUtils.trim(auctionSparkModel.getModel()
                    .replace("Vans", ""));

            vinModelEntity = vinMakeEntity.getModels().stream()
                    .filter(model -> equalsWithoutDashAndCase(newModelName, model.getName()))
                    .findFirst().orElse(null);
        }

        if (vinModelEntity == null && "Ford".equals(auctionSparkModel.getMake())
            && auctionSparkModel.getModel().contains("F350")) {
            String newModelName = "F-350";

            vinModelEntity = vinMakeEntity.getModels().stream()
                    .filter(model -> newModelName.equalsIgnoreCase(model.getName()))
                    .findFirst().orElse(null);
        }

        if (vinModelEntity == null && "Ford".equals(auctionSparkModel.getMake())
            && auctionSparkModel.getModel().contains("F250")) {
            String newModelName = "F-250";

            vinModelEntity = vinMakeEntity.getModels().stream()
                    .filter(model -> newModelName.equalsIgnoreCase(model.getName()))
                    .findFirst().orElse(null);
        }

        if (vinModelEntity == null && "Lexus".equals(auctionSparkModel.getMake())) {

            String[] lexusModel = auctionSparkModel.getModel().split(" ");
            if (lexusModel.length > 0) {
                vinModelEntity = vinMakeEntity.getModels().stream()
                        .filter(model -> equalsWithoutDashAndCase(lexusModel[0], model.getName()))
                        .findFirst().orElse(null);
            }
        }

        if (vinModelEntity == null && "GMC".equals(auctionSparkModel.getMake())
            && auctionSparkModel.getModel().contains("Sierra")) {

            vinModelEntity = vinMakeEntity.getModels().stream()
                    .filter(model -> "Sierra".equalsIgnoreCase(model.getName()))
                    .findFirst().orElse(null);
        }

        if (vinModelEntity == null && "Chrysler".equals(auctionSparkModel.getMake())) {
            String newModelName = StringUtils.trim(auctionSparkModel.getModel()
                    .replace("&", "and"));

            vinModelEntity = vinMakeEntity.getModels().stream()
                    .filter(model -> newModelName.equalsIgnoreCase(model.getName()))
                    .findFirst().orElse(null);
        }

        if (vinModelEntity == null) {
            errorMap.put("modelTitle", "Модель в списке не найдена: " + auctionSparkModel.getModel());
            return;
        }

        updateModel.setModelId(vinModelEntity.getId());
        updateModel.setModelTitle(vinModelEntity.getName());
    }

    private boolean equalsWithoutDashAndCase(String str1, String str2) {
        String fromJson = StringUtils.trim(str1.replace("-", "").replace(" ", ""));

        String fromDB = StringUtils.trim(str2.replace("-", "").replace(" ", ""));

        return fromJson.equalsIgnoreCase(fromDB);
    }


    private String getComplect(Map<String, String> errorMap, AuctionSparkModel auctionSparkModel) {
        if (auctionSparkModel.getDetails() == null) {
            return null;
        }
        String result = auctionSparkModel.getDetails().getTrim();
        if ("/".equals(result)) {
            return null;
        }

        if (result == null) {
            result = auctionSparkModel.getDetails().getDiscrepancyTrim();
            if ("/".equals(result)) {
                return null;
            }
        }
        return result;
    }

    private ColorType getColor(Map<String, String> errorMap, AuctionSparkModel auctionSparkModel) {
        if (auctionSparkModel.getDetails() == null) {
            return null;
        }

        String color = auctionSparkModel.getDetails().getColor();
        ColorType colorType = ColorTypeResolver.getType(color);

        if (ColorType.UNKNOWN.equals(colorType)) {
            errorMap.put("color", "Цвет не найден в enum: " + color);
        }
        return colorType;
    }

    private String getInteriorColor(Map<String, String> errorMap, AuctionSparkModel auctionSparkModel) {
        if (auctionSparkModel.getDetails() == null) {
            return null;
        }
        return auctionSparkModel.getDetails().getInteriorColor();
    }

    private Integer getYear(Map<String, String> errorMap, AuctionSparkModel auctionSparkModel) {
        return auctionSparkModel.getYear();
    }

    private Integer getOdometer(Map<String, String> errorMap, AuctionSparkModel auctionSparkModel) {
        if (auctionSparkModel.getOverview() == null
            || StringUtils.isEmpty(auctionSparkModel.getOverview().getOdometer())) {
            return null;
        }

        String odometer = StringUtils.trim(
                auctionSparkModel.getOverview().getOdometer()
                        .replace("(Actual)", "")
                        .replace("(Not Actual)", "")
                        .replace("(Exempt)", "")
                        .replace(",", "")
                        .replace(".", "")
        );

        if (StringUtils.isNotEmpty(auctionSparkModel.getOverview().getOdometer())
            && !StringUtils.isNumeric(odometer)) {
            errorMap.put("odometer", "После очистки в поле остались буквы: " + odometer + ", исходник: " + auctionSparkModel.getOverview().getOdometer());
            return null;
        }

        if (StringUtils.isEmpty(odometer)
            || !StringUtils.isNumeric(odometer)) {
            return null;
        }
        return Integer.parseInt(odometer);
    }

    private String getVin(Map<String, String> errorMap, AuctionSparkModel auctionSparkModel) {
        if (auctionSparkModel.getDetails() == null
            || StringUtils.isEmpty(auctionSparkModel.getDetails().getVin())) {
            errorMap.put("vin", "Пришёл пустой vin");
            return null;
        }
        return auctionSparkModel.getDetails().getVin();
    }

    private String getLocation(Map<String, String> errorMap, AuctionSparkModel auctionSparkModel) {
        if (auctionSparkModel.getAuction() == null) {
            return null;
        }
        return auctionSparkModel.getAuction().getLocation();
    }

    private String getLane(Map<String, String> errorMap, AuctionSparkModel auctionSparkModel) {
        if (auctionSparkModel.getAuction() == null) {
            return null;
        }
        return auctionSparkModel.getAuction().getRunNumber();
    }

    private String getSeller(Map<String, String> errorMap, AuctionSparkModel auctionSparkModel) {
        if (auctionSparkModel.getOverview() == null
            || auctionSparkModel.getOverview().getConsignor() == null) {
            return null;
        }
        return auctionSparkModel.getOverview().getConsignor().getSellerName();
    }

    private String getEngine(Map<String, String> errorMap, AuctionSparkModel auctionSparkModel) {
        if (auctionSparkModel.getDetails() == null) {
            return null;
        }
        Details details = auctionSparkModel.getDetails();
        String engine = "";
        if (details.getDisplacement() != null) {
            engine += " " + details.getDisplacement();
        }
        if (details.getCylinders() != null) {
            engine += " " + details.getCylinders() + "CYL";
        }
        engine = engine.replace("  ", " ");

        return StringUtils.trim(engine);
    }

    private TransmissionType getTransmission(Map<String, String> errorMap, AuctionSparkModel auctionSparkModel) {
        if (auctionSparkModel.getDetails() == null) {
            return null;
        }
        String transmission = auctionSparkModel.getDetails().getTransmission();
        TransmissionType transmissionType = TransmissionTypeResolver.getType(transmission);

        if (TransmissionType.UNKNOWN.equals(transmissionType)) {
            errorMap.put("transmission", "Не найден тип трансмиссии в enum: " + transmission);
        }

        return transmissionType;
    }

    private DriveTrainType getDriveTrain(Map<String, String> errorMap, AuctionSparkModel auctionSparkModel) {
        if (auctionSparkModel.getDetails() == null) {
            return null;
        }

        String driveTrain = auctionSparkModel.getDetails().getDriveTrain();
        DriveTrainType driveTrainType = DriveTrainTypeResolver.getType(driveTrain);

        if (DriveTrainType.UNKNOWN.equals(driveTrainType)) {
            errorMap.put("driveTrain", "Не найден тип driveTrain в enum: " + driveTrain);
        }

        return driveTrainType;
    }

    private Integer getPrice(Map<String, String> errorMap, AuctionSparkModel auctionSparkModel) {
        if (auctionSparkModel.getSaleListing() == null
            || auctionSparkModel.getSaleListing().getFloorPrice() == null) {
            return null;
        }
        String priceStr = StringUtils.trim(auctionSparkModel.getSaleListing().getFloorPrice()
                .replace(",", "."));

        try {
            return Math.round(Float.parseFloat(priceStr));
        } catch (Exception e) {
            errorMap.put("price", "Не смог распарсить цену: " + auctionSparkModel.getSaleListing().getFloorPrice());
            return null;
        }
    }

    private String getAnnouncement(Map<String, String> errorMap, AuctionSparkModel auctionSparkModel) {
        if (auctionSparkModel.getOverview() == null) {
            return null;
        }
        return auctionSparkModel.getOverview().getAnnouncements();
    }

    private String getTitle(Map<String, String> errorMap, AuctionSparkModel auctionSparkModel) {
        return null;
    }

    private Integer getGrade(Map<String, String> errorMap, AuctionSparkModel auctionSparkModel) {
        if (auctionSparkModel.getOverview() == null
            || auctionSparkModel.getOverview().getGrade() == null) {
            return null;
        }
        String priceStr = StringUtils.trim(auctionSparkModel.getOverview().getGrade()
                .replace(",", "")
                .replace(".", "")
                .replace(" ", ""));

        return StringUtils.isNotEmpty(priceStr)
                ? Integer.parseInt(priceStr)
                : null;
    }

    private String getDriveable(Map<String, String> errorMap, AuctionSparkModel auctionSparkModel) {
        return null;
    }

    private String getPicture(Map<String, String> errorMap, AuctionSparkModel auctionSparkModel) {
        if (CollectionUtils.isEmpty(auctionSparkModel.getVehiclePhotos())) {
            return null;
        }
        String picture = auctionSparkModel.getVehiclePhotos().getFirst();
        if (picture.startsWith("//")) {
            picture = "http:" + picture;
        }
        return picture;
    }

    private LocalDate getSaleDate(Map<String, String> errorMap, AuctionSparkModel auctionSparkModel) {
        if (auctionSparkModel.getAuction() == null
            || auctionSparkModel.getAuction().getSaleDate() == null) {
            return null;
        }
        try {
            return LocalDate.parse(auctionSparkModel.getAuction().getSaleDate());
        } catch (DateTimeParseException e) {
            log.error("*** Не смогли распарсить дату из текста: {}.", auctionSparkModel.getAuction().getSaleDate(), e);
            return null;
        }
    }

    private Integer getEngineTypeId(Map<String, String> errorMap, AuctionSparkModel auctionSparkModel) {
        return null;
    }

    private EngineType getEngineType(Map<String, String> errorMap, AuctionSparkModel auctionSparkModel) {
        if (auctionSparkModel.getDetails() == null) {
            return null;
        }

        String fuelType = auctionSparkModel.getDetails().getFuelType();
        EngineType engineType = EngineTypeResolver.getType(fuelType);

        if ("B".equals(fuelType)) {
            engineType = EngineType.HYBRID;
        }

        if (EngineType.UNKNOWN.equals(engineType)) {
            errorMap.put("fuelType", "Не найден тип двигателя в enum: " + fuelType);
        }

        return engineType;
    }

    private Integer getDisplacement(Map<String, String> errorMap, AuctionSparkModel auctionSparkModel) {
        if (auctionSparkModel.getDetails() == null || auctionSparkModel.getDetails().getDisplacement() == null) {
            return null;
        }
        try {
            return (int) (Float.parseFloat(auctionSparkModel.getDetails().getDisplacement()) * 10);
        } catch (Exception e) {
            errorMap.put("displacement", "Не смог распарсить объём: " + auctionSparkModel.getDetails().getDisplacement());
            return null;
        }
    }

}
