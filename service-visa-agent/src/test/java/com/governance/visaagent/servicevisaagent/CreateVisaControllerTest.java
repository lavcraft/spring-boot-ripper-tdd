package com.governance.visaagent.servicevisaagent;

import com.governance.visaagent.servicevisaagent.port.input.UserInfo;
import com.governance.visaagent.servicevisaagent.service.VisaService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class CreateVisaControllerTest {
    @Autowired MockMvc     mockMvc;
    @MockBean  VisaService visaRequestService;
    ArgumentCaptor<UserInfo> userInfoCaptor = ArgumentCaptor.forClass(UserInfo.class);

    @Test
    void should_receive_visa_request_and_return_ticket() throws Exception {
        //given
        when(visaRequestService.processVisaRequest(userInfoCaptor.capture())).thenReturn(1234L);

        //when
        mockMvc.perform(post("/visa-request")
                                //language=json
                                .content("""
                                        {"userId": "U-123"}
                                        """).contentType(MediaType.APPLICATION_JSON_VALUE))
               .andExpect(jsonPath("$.ticket").value(Matchers.equalTo("1234")));

        //then
        assertThat(userInfoCaptor.getValue().getUserId()).isEqualTo("U-123");
    }


    @Test
    void should_inform_about_invalid_state_when_multiple_tickets_in_processing_status() throws Exception {
        //given
        when(visaRequestService.processVisaRequest(any())).thenThrow(IllegalStateException.class);

        //when
        mockMvc.perform(
                       post("/visa-request")
                               //language=json
                               .content("""
                                       {"userId": "test"}
                                       """)
                               .contentType(MediaType.APPLICATION_JSON_VALUE)
               )
               .andExpect(status().is5xxServerError())
               .andExpect(jsonPath("$.message").value("Internal error. Please contact later"));
    }
}
