package com.igurman.gur_car_bot.model.dto.auction.auctionspark;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Consignor {
    @JsonProperty("seller_name")
    private String sellerName;
    private String name;
    @JsonProperty("logo_url")
    private String logoUrl;
}
