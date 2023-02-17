package com.governance.embassy;

import com.governance.embassy.service.VisaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest
public class VisaRequestControllerWebMVCTest {
    @Autowired MockMvc mockMvc;
    @MockBean
    VisaService visaService;

    @Test
    void shouldshould_accept_visa_request_with_id_and_return_ok() throws Exception {
        when(visaService.createRequest(anyString())).thenReturn("T-1234");

        mockMvc
                .perform(get("/visa?userId=1234").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.ticket").isString());

    }
}
