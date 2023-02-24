package com.governance.visaagent.servicevisaagent.port.input;

import com.governance.visaagent.servicevisaagent.dal.VisaRequest;
import com.governance.visaagent.servicevisaagent.service.VisaRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class VisaRequestController {
    private final VisaRequestService visaRequestService;

    @PostMapping("/visa-request")
    public VisaRequestResponse createVisaRequest(@RequestBody UserInfo userInfo) {
        VisaRequest save = visaRequestService.processVisaRequest(userInfo);
        return VisaRequestResponse.builder().ticket(save.getId().toString()).build();
    }
}
