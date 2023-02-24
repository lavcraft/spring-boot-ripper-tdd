package com.governance.embassy;

import com.governance.embassy.port.input.VisaRequestResponse;
import com.governance.embassy.service.VisaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class VisaRequestControllerTest {
    @Autowired TestRestTemplate template;
    @MockBean  VisaService      visaService;

    @Test
    void should_accept_visa_request_return_ok() {
        ResponseEntity<String> forEntity = template.getForEntity(
                "/visa?userId=12345",
                String.class
        );

        assertTrue(forEntity.getStatusCode().is2xxSuccessful());
    }


    @Test
    void should_return_ticket_number_when_request_visa() {
        //given
        when(visaService.createRequest("12345")).thenReturn("T-134");

        //when
        ResponseEntity<VisaRequestResponse> forEntity = template.getForEntity(
                "/visa?userId=12345",
                VisaRequestResponse.class
        );
        VisaRequestResponse                 body      = forEntity.getBody();

        //then
        assertAll(
                "Body should exist",
                () -> assertNotNull(
                        body.getTicket(),
                        "Ticket number should be exist"
                ),
                () -> assertEquals("T-134",
                        body.getTicket(),
                        "ticket should be T-134, because it mocked"
                )
        );
    }

    @Bean
    public String string() {
        return null;
    }
}
