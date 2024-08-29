package com.igurman.gur_car_bot.model.dto.auction.auctionspark;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class Overview {
    private String vin;
    private String description;
    @JsonProperty("run_number")
    private String runNumber;
    @JsonProperty("has_cr")
    private Boolean hasCr;
    private String odometer;
    private String grade;
    @JsonProperty("grade_provided_by")
    private String gradeProvidedBy;
    private String announcements;
    @JsonProperty("sold_on_if")
    private Boolean soldOnIf;
    @JsonProperty("has_enhanced_if_then_auto_url")
    private Boolean hasEnhancedIfThenAutoUrl;
    private Consignor consignor;
    private List<String> lights;
    @JsonProperty("sold_date")
    private LocalDate soldDate;
    @JsonProperty("sold_label")
    private String soldLabel;
    @JsonProperty("sold_price")
    private String soldPrice;
}