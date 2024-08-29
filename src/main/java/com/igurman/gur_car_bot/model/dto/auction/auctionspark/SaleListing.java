package com.igurman.gur_car_bot.model.dto.auction.auctionspark;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SaleListing {
    @JsonProperty("floor_price")
    private String floorPrice;
    private Deal deal;
}
