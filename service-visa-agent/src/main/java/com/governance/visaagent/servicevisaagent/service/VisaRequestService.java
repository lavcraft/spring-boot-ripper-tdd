package com.governance.visaagent.servicevisaagent.service;

import com.governance.visaagent.servicevisaagent.dal.VisaRequest;
import com.governance.visaagent.servicevisaagent.port.input.UserInfo;
import org.springframework.stereotype.Service;

@Service
public class VisaRequestService {
    public VisaRequest processVisaRequest(UserInfo userInfo) {
        return VisaRequest.builder().build();
    }
}
