package com.igurman.gur_car_bot.model.dto.auction.auctionspark;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Declarations {
    @JsonProperty("title_status")
    private String titleStatus;
    @JsonProperty("title_state")
    private String titleState;
    private String drivable;
    private String frame;
}
