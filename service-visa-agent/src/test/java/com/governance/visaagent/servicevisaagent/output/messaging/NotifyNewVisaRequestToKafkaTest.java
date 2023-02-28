package com.governance.visaagent.servicevisaagent.output.messaging;

import com.governance.visaagent.servicevisaagent.port.input.UserInfo;
import com.governance.visaagent.servicevisaagent.service.VisaService;
import com.governance.visaagent.servicevisaagent.util.MessagingIncludeFilter;
import com.governance.visaagent.servicevisaagent.util.MessagingTestConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
//@Import(MessagingTestConfiguration.class)
//@ComponentScan(
//        includeFilters = {
//                @ComponentScan.Filter(type = FilterType.CUSTOM,
//                                      classes = MessagingIncludeFilter.class)
//        }
//)
public class NotifyNewVisaRequestToKafkaTest {
    @Container static KafkaContainer kafka = new KafkaContainer(DockerImageName.parse(
            "confluentinc/cp-kafka:7.3.1"));

    @Autowired VisaService                                                         visaRequestService;
    @SpyBean   NotifyNewVisaRequestToKafkaTest.KafkaConsumerConfigurationTemporary listener;
    static     CountDownLatch                                                      countDownLatch = new CountDownLatch(5);

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

        assertAll(
                () -> assertThat(await)
                              .describedAs("Notification should be delivered " +
                                           "via queue")
                              .isTrue(),
                () -> verify(
                        listener,
                        times(1)
                ).listen("U-100"),
                () -> verify(
                        listener,
                        times(1)
                ).listen("U-101"),
                () -> verify(
                        listener,
                        times(1)
                ).listen("U-102"),
                () -> verify(
                        listener,
                        times(1)
                ).listen("U-103"),
                () -> verify(
                        listener,
                        times(1)
                ).listen("U-104")
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
