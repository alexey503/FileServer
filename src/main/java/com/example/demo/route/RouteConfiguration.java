package com.example.demo.route;

import com.example.demo.data.FXRateCorrectionCoefList;
import com.example.demo.data.DataForRest;
import com.example.demo.processor.FileProcessor;
import com.example.demo.processor.MapProcessor;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.apache.camel.component.jackson.ListJacksonDataFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class RouteConfiguration extends RouteBuilder {

    public static final String FILE_IN_PROCESS = "FileInProcessed";
    @Autowired
    private FileProcessor fileProcessor;

    @Autowired
    private MapProcessor mapProcessor;

    @Value("http://${rest.url}/api/v1/data/testdata")
    private String restUrl;

    @Override
    public void configure() throws Exception {

        from("timer:checkNewFiles?period={{TimeExpression:2000}}")
            .to("direct:start")
        ;

        from("direct:start")
            .process(fileProcessor)
            .choice()
                .when(exchange -> exchange.getProperty(FILE_IN_PROCESS) != null)
                    .unmarshal(new JacksonDataFormat(FXRateCorrectionCoefList.class))
                    .process(mapProcessor)
                    .marshal(new ListJacksonDataFormat(DataForRest.class))
                    .setHeader(Exchange.HTTP_METHOD, constant("PUT"))
                    .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
                    .log("Send to rest ${body}")
                    .to(restUrl)
                    .process(exchange -> log.info("The HTTP response code is: {}", exchange.getIn().getHeader(Exchange.HTTP_RESPONSE_CODE)))
                    .choice()
                        .when(exchange -> "200".equals(exchange.getIn().getHeader(Exchange.HTTP_RESPONSE_CODE).toString()))
                            .process(exchange -> log.info(exchange.getIn().getBody(String.class)))
                        .otherwise()
                            .process(exchange -> fileProcessor.moveFileTo("failed", exchange.getProperty(FILE_IN_PROCESS, File.class)))
                    .endChoice()
                    .removeProperty(FILE_IN_PROCESS)
                    .to("direct:start")
                .otherwise()
                    .log("No json files")
            .endChoice()
        ;
    }
}
