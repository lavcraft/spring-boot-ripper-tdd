package com.governance.embassy.service;

import com.governance.embassy.port.output.VisaHttpClientProperties;
import com.governance.embassy.port.output.VisaRequest;
import com.governance.embassy.port.output.VisaRequestResponse;
import com.governance.embassy.port.output.VisaStatusResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VisaServiceTest {
    @Mock        RestTemplate             httpClientMock;
    @Spy         VisaHttpClientProperties properties = new VisaHttpClientProperties("http://localhost:8081");
    @InjectMocks VisaService              target;

    @Test
    void should_send_status_request_to_external_service() {
        //given
        when(this.httpClientMock.getForEntity(
                "http://localhost:8081/visa-status?ticket=T-12345",
                VisaStatusResponse.class
        )).thenReturn(ResponseEntity.ok(VisaStatusResponse
                .builder()
                .status("processing")
                .build()));

        //when
        String status = target.getStatus("T-12345");

        //then
        verify(
                this.properties,
                times(1)
        ).getEndpoint();
        assertEquals(
                "processing",
                status,
                "ticket status should be returned"
        );
    }

    @Test
    void should_send_create_request_to_external_service() {
        //given
        when(httpClientMock.postForEntity(
                "http://localhost:8081/visa-request",
                VisaRequest
                        .builder()
                        .userId("U-12345")
                        .build(),
                VisaRequestResponse.class
        )).thenReturn(ResponseEntity.ok(VisaRequestResponse
                .builder()
                .ticket("T-12345")
                .build()));

        //when
        String ticket = target.createRequest("U-12345");

        //then
        assertEquals("T-12345", ticket, "ticket num expected not null");
    }

    @Test
    void should_return_ticket_and_status_when_request_already_created() {
        //given
        when(httpClientMock.postForEntity(
                "http://localhost:8081/visa-request",
                VisaRequest
                        .builder()
                        .userId("U-12345")
                        .build(),
                VisaRequestResponse.class
        )).thenReturn(ResponseEntity.badRequest()
                                    .body(VisaRequestResponse
                                            .builder()
                                            .ticket("T-12345")
                                            .build()));

        //when
        String ticket = target.createRequest("U-12345");

        //then
        assertEquals("T-12345", ticket, "ticket num expected not null");
    }
}