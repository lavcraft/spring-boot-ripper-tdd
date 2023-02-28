package com.governance.visaagent.servicevisaagent.util;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Testcontainers
//@EnableKafka
//@SpringBootTest(properties = {
//        "spring.autoconfigure.exclude[0]=org.springframework.boot.autoconfigure.data.jpa" +
//        ".JpaRepositoriesAutoConfiguration",
//        "spring.autoconfigure.exclude[1]=org.springframework.boot.autoconfigure.flyway" +
//        ".FlywayAutoConfiguration"
//})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Import(MessagingTestConfiguration.class)
@ComponentScan(
        includeFilters = {
                @ComponentScan.Filter(type = FilterType.CUSTOM,
                                      classes = MessagingIncludeFilter.class)
        }
)
@ContextConfiguration(initializers = {MessagingTestConfiguration.MessagingTestInitializer.class})
public @interface MessagingTest {
    String[] basePackages() default {};

    Class<?>[] classes() default {};

    SpringBootTest.WebEnvironment webEnvironment() default SpringBootTest.WebEnvironment.NONE;
}
