package com.governance.embassy;

import com.governance.embassy.port.input.VisaRequestStatusResponse;
import com.governance.embassy.service.VisaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class VisaStatusTest {
    @Autowired
    TestRestTemplate template;
    @MockBean
    VisaService visaService;

    @Test
    void should_get_status_for_visa_request() {
        //given
        when(visaService.getStatus(anyString())).thenReturn("in_process");

        //when
        ResponseEntity<VisaRequestStatusResponse> forEntity = template.getForEntity("/visa/1234560", VisaRequestStatusResponse.class);
        VisaRequestStatusResponse body = forEntity.getBody();

        //then
        assertAll(
                "Body should exist",
                () -> assertEquals("in_process", body.getStatus(), "Status should be processing")
        );
    }
}
