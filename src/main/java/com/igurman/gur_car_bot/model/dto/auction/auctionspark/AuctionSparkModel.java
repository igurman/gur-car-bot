package com.igurman.gur_car_bot.model.dto.auction.auctionspark;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AuctionSparkModel {
    @JsonProperty("catalyst_company_id")
    private Long catalystCompanyId;
    private String make;
    private String model;
    private Integer year;
    @JsonProperty("option_list")
    private List<String> optionList;
    private Long id;
    private String description;
    @JsonProperty("short_description")
    private String shortDescription;
    private Long age;
    @JsonProperty("vehicle_photos")
    private List<String> vehiclePhotos;
    @JsonProperty("checked_in_at")
    private String checkedInAt;
    @JsonProperty("sale_listing")
    private SaleListing saleListing;
    private Auction auction;
    private Declarations declarations;
    private Details details;
    private Overview overview;
    private List<Tire> tires;
}
