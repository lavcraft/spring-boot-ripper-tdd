package com.governance.embassy;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.governance.embassy.port.input.VisaRequestResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

@JsonTest
public class VisaRequestPassToDownstream {
    @Autowired ObjectMapper objectMapper;

    @Autowired JacksonTester<VisaRequestResponse> jacksonTester;

    @Test
    void should_serialize_output_visa_request() {

    }
}
