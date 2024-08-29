package com.igurman.gur_car_bot.model.dto.auction.auctionspark;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Auction {
    private String auction;
    private String location;
    @JsonProperty("run_number")
    private String runNumber;
    @JsonProperty("stock_number")
    private String stockNumber;
    @JsonProperty("sale_date")
    private String saleDate;
    @JsonProperty("sale_type")
    private String saleType;
}
