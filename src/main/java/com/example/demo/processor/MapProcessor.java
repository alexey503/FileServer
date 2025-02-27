package com.example.demo.processor;

import com.example.demo.data.FXRateCorrectionCoef;
import com.example.demo.data.FXRateCorrectionCoefList;
import com.example.demo.data.DataForRest;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MapProcessor implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {

        FXRateCorrectionCoefList sourceList = exchange.getIn().getBody(FXRateCorrectionCoefList.class);
        List<DataForRest> resultData = new ArrayList<>();
        for (FXRateCorrectionCoef entity : sourceList.getFxRateCorrectionCoefList()) {
            resultData.add(DataForRest.builder()
                    .date(entity.getDate())
                    .currency1(entity.getCurrency1())
                    .currency2(entity.getCurrency2())
                    .periodNumber(entity.getPeriodNumber())
                    .periodUnit(entity.getPeriodUnitCode())
                    .positiveCoefficient(entity.getPositiveCoefficient())
                    .negativeCoefficient(entity.getNegativeCoefficient())
                    .id(entity.getExternalID())
                    .build());
        }
        exchange.getIn().setBody(resultData, resultData.getClass());
    }
}
