package com.governance.visaagent.servicevisaagent.output;

import com.governance.visaagent.servicevisaagent.port.input.UserInfo;
import com.governance.visaagent.servicevisaagent.service.VisaRequestService;
import jakarta.annotation.PostConstruct;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.stream.StreamSupport;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.verify;

@Testcontainers
@SpringBootTest
public class NotifyNewVisaRequestToKafkaTest {
    @Container static KafkaContainer kafka = new KafkaContainer(DockerImageName.parse(
            "confluentinc/cp-kafka:7.3.1"));

//    @SpyBean TestKafkaListener testKafkaListener;

    @Autowired VisaRequestService visaRequestService;
    static     CountDownLatch     countDownLatch = new CountDownLatch(5);

    @Autowired KafkaProperties kafkaProperties;

    @Test
    void should_send_visa_request_to_queue_after_creating() throws InterruptedException {
        UserInfo build = UserInfo.builder().build();
        visaRequestService.processVisaRequest(build.withUserId("U-100"));
        visaRequestService.processVisaRequest(build.withUserId("U-101"));
        visaRequestService.processVisaRequest(build.withUserId("U-102"));
        visaRequestService.processVisaRequest(build.withUserId("U-103"));
        visaRequestService.processVisaRequest(build.withUserId("U-104"));

        boolean await = countDownLatch.await(
                10L,
                TimeUnit.SECONDS
        );

//        verify(testKafkaListener, times(5)).acceptDebug(anyString());
        assertAll(
                () -> assertThat(await)
                              .describedAs("Notification should be delivered " +
                                           "via queue")
                              .isTrue(),

                () -> assertThat(countDownLatch.getCount())
                              .describedAs("Should receive only one notification")
                              .isEqualTo(0)
        );

    }

    @TestConfiguration
    public static class KafkaConsumerConfigurationTemporary {
        @KafkaListener(topics = "visa.requests", groupId = "test-app")
        public void listen(String in) {
            System.out.println("Receive message from user with id: " + in);
            countDownLatch.countDown();
        }
    }

    @DynamicPropertySource
    public static void registerKafka(DynamicPropertyRegistry dynamicPropertyRegistry) {
        dynamicPropertyRegistry.add(
                "spring.kafka.bootstrap-servers",
                kafka::getBootstrapServers
        );
    }
}
