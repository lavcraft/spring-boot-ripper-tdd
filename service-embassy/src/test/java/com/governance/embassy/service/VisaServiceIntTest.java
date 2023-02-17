package com.governance.embassy.service;

import com.governance.embassy.port.output.VisaStatusResponse;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.client.RestTemplate;

import javax.security.auth.login.AccountLockedException;
import java.time.Clock;
import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@SpringBootTest
public class VisaServiceIntTest {
    @Autowired VisaService  visaService;
    @MockBean  RestTemplate visaRestTemplate;

    @Test
    void should_cache_by_request_id() {
        when(visaRestTemplate.getForEntity(
                anyString(),
                eq(VisaStatusResponse.class)
        )).thenReturn(ResponseEntity.ok(VisaStatusResponse
                .builder()
                .status("processing")
                .build()));

        //when
        String status1 = visaService.getStatus("T-1234");
        String status2 = visaService.getStatus("T-1234");

        //then
        assertEquals(status1, status2, "status from cache should be equal actual status");

        verify(
                        visaRestTemplate,
                        times(1)
                )
                .getForEntity(
                        anyString(),
                        eq(VisaStatusResponse.class)
                );
    }

    @Test
    void should_visa_request_update_status() {
        when(visaRestTemplate.getForEntity(
                anyString(),
                eq(VisaStatusResponse.class)
        )).thenReturn(ResponseEntity.ok(VisaStatusResponse
                .builder()
                .status("processing")
                .build()));

        //when
        String status1 = visaService.getStatus("T-1234");
        String status2 = visaService.getStatus("T-1235");

        //then
        assertEquals(status1, status2, "status from cache should be equal actual status");

        verify(
                visaRestTemplate,
                times(2)
        )
                .getForEntity(
                        anyString(),
                        eq(VisaStatusResponse.class)
                );
    }
}
