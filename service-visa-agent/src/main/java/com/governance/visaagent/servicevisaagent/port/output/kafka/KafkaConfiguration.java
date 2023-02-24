package com.governance.visaagent.servicevisaagent.port.output.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.kafka.config.TopicBuilder;

import static org.springframework.kafka.config.TopicBuilder.name;

@Configuration
public class KafkaConfiguration {
//    @Bean
    public NewTopic visaRequests() {
        return name("visa.requests").build();
    }
}
