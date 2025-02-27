package com.example.demo.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class FXRateCorrectionCoefList {

    @JsonProperty("FXRateCorrectionCoef")
    private List<FXRateCorrectionCoef> fxRateCorrectionCoefList;

}
