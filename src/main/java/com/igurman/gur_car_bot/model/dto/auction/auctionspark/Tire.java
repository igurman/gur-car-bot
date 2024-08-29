package com.igurman.gur_car_bot.model.dto.auction.auctionspark;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Tire {
    private String location;
    private String brand;
    private String size;
    @JsonProperty("tread_depth")
    private String treadDepth;
}
