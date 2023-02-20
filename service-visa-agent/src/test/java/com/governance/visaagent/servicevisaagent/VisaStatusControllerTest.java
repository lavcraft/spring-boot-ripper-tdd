package com.governance.visaagent.servicevisaagent;

import com.governance.visaagent.servicevisaagent.dal.VisaRequest;
import com.governance.visaagent.servicevisaagent.dal.VisaRequestRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class VisaStatusControllerTest {
    @Autowired MockMvc mockMvc;
    @MockBean VisaRequestRepository visaRequestRepository;

    @Test
    void should_return_status_for_saved_request() throws Exception {
        //given
        when(visaRequestRepository.findById(100L)).thenReturn(Optional.of(VisaRequest.builder()
                .id(100L).status("processing").userId("U-1").build()));

        //when
        mockMvc.perform(get("/visa-status").param("ticket", "100"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.status").value("processing"));
    }

    @Test
    void should_return_404_when_no_request_available() throws Exception {
        //given
        when(visaRequestRepository.findById(100L)).thenReturn(Optional.of(VisaRequest.builder()
                .id(100L).status("processing").userId("U-1").build()));

        //when
        mockMvc.perform(get("/visa-status").param("ticket", "200"))
                .andExpect(status().isNotFound());

    }
}
