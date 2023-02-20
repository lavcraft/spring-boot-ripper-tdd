package com.governance.visaagent.servicevisaagent.service;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class VisaRequestServiceTest {
    @InjectMocks VisaRequestService visaRequestService;

    @Test
    void should_persist_new_request_and_return_id() {
    }

    @Test
    void should_send_new_request_to_queue_after_persist() {

    }
}
