package com.igurman.gur_car_bot.model.dto.auction.auctionspark;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Details {
    private String trim;
    private String vin;
    private String color;
    @JsonProperty("interior_color")
    private String interiorColor;
    @JsonProperty("interior_material")
    private String interiorMaterial;
    private String transmission;
    @JsonProperty("drive_train")
    private String driveTrain;
    @JsonProperty("body_style")
    private String bodyStyle;
    private String displacement;
    private String doors;
    private String cylinders;
    @JsonProperty("fuel_type")
    private String fuelType;
    @JsonProperty("discrepancy_trim")
    private String discrepancyTrim;
}
