package ru.jsms.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class JsmsServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(JsmsServerApplication.class, args);
    }

}
