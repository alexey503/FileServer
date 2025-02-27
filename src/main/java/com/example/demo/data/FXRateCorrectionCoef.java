package com.example.demo.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class FXRateCorrectionCoef {
    @JsonProperty("Date")
    private String date;
    @JsonProperty("Currency_1")
    private String currency1;
    @JsonProperty("Currency_2")
    private String currency2;
    @JsonProperty("Period_Number")
    private String periodNumber;
    @JsonProperty("Period_Unit_Code")
    private String periodUnitCode;
    @JsonProperty("Positive_Coefficient")
    private String positiveCoefficient;
    @JsonProperty("Negative_Coefficient")
    private String negativeCoefficient;
    @JsonProperty("External_ID")
    private String externalID;
}
