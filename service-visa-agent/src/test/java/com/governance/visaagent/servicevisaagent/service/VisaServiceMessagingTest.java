package com.governance.visaagent.servicevisaagent.service;

import com.governance.visaagent.servicevisaagent.dal.VisaRequest;
import com.governance.visaagent.servicevisaagent.dal.VisaRequestRepository;
import com.governance.visaagent.servicevisaagent.port.input.UserInfo;
import com.governance.visaagent.servicevisaagent.util.MessagingTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
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
import static org.mockito.Mockito.*;
import static org.testcontainers.shaded.org.awaitility.Awaitility.await;

//@Testcontainers
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)

//@Import(MessagingTestConfiguration.class)

//@Testcontainers
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@MessagingTest
public class VisaServiceMessagingTest {
    @Container static KafkaContainer kafka = new KafkaContainer(DockerImageName.parse(
            "confluentinc/cp-kafka:7.3.1"));

    @Autowired           VisaService    visaService;
    @MockBean  VisaRequestRepository               visaRequestRepository;
    @SpyBean   VisaServiceMessagingTest.TestConfig testConfig;
    private static CountDownLatch latch = new CountDownLatch(5);


    @Test
    void should_send_visa_request_notification_to_queue() throws InterruptedException {
        //given
        when(visaRequestRepository.save(any()))
                .thenReturn(
                        VisaRequest.builder()
                                   .id(100L)
                                   .build()
                );

        //when
        visaService.processVisaRequest(UserInfo.builder().userId("U-100").build());
        visaService.processVisaRequest(UserInfo.builder().userId("U-101").build());
        visaService.processVisaRequest(UserInfo.builder().userId("U-102").build());
        visaService.processVisaRequest(UserInfo.builder().userId("U-103").build());
        visaService.processVisaRequest(UserInfo.builder().userId("U-104").build());

        //then
        boolean await = latch.await(
                10,
                TimeUnit.SECONDS
        );
        assertThat(await).isTrue();

        verify(testConfig).listener("U-100");
        verify(testConfig).listener("U-101");
        verify(testConfig).listener("U-102");
        verify(testConfig).listener("U-103");
        verify(testConfig).listener("U-104");
    }

    @TestConfiguration
    public static class TestConfig {

        @KafkaListener(topics = "visa.requests", groupId = "test-app")
        public void listener(String in) {
            latch.countDown();
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
