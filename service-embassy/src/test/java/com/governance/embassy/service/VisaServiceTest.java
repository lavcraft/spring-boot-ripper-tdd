package com.governance.embassy.service;

import com.governance.embassy.port.output.VisaHttlClientProperties;
import com.governance.embassy.port.output.VisaRequest;
import com.governance.embassy.port.output.VisaRequestResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VisaServiceTest {
    @Mock        RestTemplate httpClientMock;
    @Spy         VisaHttlClientProperties properties = new VisaHttlClientProperties("http://localhost:8081");
    @InjectMocks VisaService target;

    @Test
    void should_send_status_request_to_external_service() {
        //given
        when(this.httpClientMock.getForEntity(
                "http://localhost:8081/visa-status?requestId=U-123451",
                VisaRequestResponse.class
        )).thenReturn(ResponseEntity.ok(VisaRequestResponse
                .builder()
                .ticket("T-12345")
                .build()));

        //when
        String status = target.getStatus("U-12345");

        //then
        verify(
                this.httpClientMock,
                times(1)
        ).getForEntity(
                anyString(),
                eq(VisaRequestResponse.class)
        );
    }

    @Test
    void should_send_create_request_to_external_service() {
        //given
        when(httpClientMock.postForEntity(
                "http://localhost:8081/visa-request",
                VisaRequest.class,
                VisaRequestResponse.class
        )).thenReturn(ResponseEntity.ok(VisaRequestResponse
                .builder()
                .build()));

        //when
        String ticket = target.createRequestOrGetStatus("U-12345");

        //then

    }
}