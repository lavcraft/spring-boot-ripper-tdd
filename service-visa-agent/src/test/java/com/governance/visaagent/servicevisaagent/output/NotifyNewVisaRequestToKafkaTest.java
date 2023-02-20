package com.governance.visaagent.servicevisaagent.output;

import com.governance.visaagent.servicevisaagent.dal.VisaRequest;
import com.governance.visaagent.servicevisaagent.port.output.VisaRequestQueueDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

import java.util.concurrent.CountDownLatch;

@SpringBootTest
public class NotifyNewVisaRequestToKafkaTest {
    KafkaContainer kafka = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.3.1"));
    @Autowired KafkaTemplate<String, VisaRequestQueueDTO> kafkaTemplate;
    CountDownLatch countDownLatch = new CountDownLatch(1);

    @Test
    void should_send_visa_request_to_queue_after_creating() {

    }

    @TestConfiguration
    public static class KafkaConsumerConfigurationTemporary {
        @KafkaListener(topics = "visa.requests")
        public void listen(VisaRequestQueueDTO visaRequestQueueDTO) {

        }
    }
}
