package com.governance.visaagent.servicevisaagent;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableKafka
public class ServiceVisaAgentApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceVisaAgentApplication.class, args);
    }

}
