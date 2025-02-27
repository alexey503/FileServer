package com.example.demo.data;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DataForRest {
    private String id;
    private String date;
    private String currency1;
    private String currency2;
    private String periodNumber;
    private String periodUnit;
    private String positiveCoefficient;
    private String negativeCoefficient;

}
