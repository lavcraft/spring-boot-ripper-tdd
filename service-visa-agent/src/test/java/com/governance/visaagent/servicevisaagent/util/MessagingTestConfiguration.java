package com.governance.visaagent.servicevisaagent.util;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.lifecycle.Startables;
import org.testcontainers.utility.DockerImageName;

@TestConfiguration
public class MessagingTestConfiguration {
    public static KafkaContainer kafka = new KafkaContainer(
            DockerImageName.parse("confluentinc/cp-kafka:7.3.1")
    ).withReuse(true);

    static {
        Startables.deepStart(kafka).join();
    }

    public static class MessagingTestInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            TestPropertyValues.of(
                    "spring.kafka.bootstrap-servers=" + kafka.getBootstrapServers()
            ).applyTo(applicationContext.getEnvironment());
        }
    }
}
