package com.governance.visaagent.servicevisaagent.port.input;

import com.governance.visaagent.servicevisaagent.dal.VisaRequest;
import com.governance.visaagent.servicevisaagent.service.VisaRequestService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VisaRequestController {
    private VisaRequestService visaRequestService;

    public VisaRequestController(final VisaRequestService visaRequestService) {
        this.visaRequestService = visaRequestService;
    }

    @PostMapping("/visa-request")
    public VisaRequestResponse createVisaRequest(@RequestBody UserInfo userInfo) {
        VisaRequest save = visaRequestService.processVisaRequest(userInfo);
        return VisaRequestResponse.builder().ticket(save.getId().toString()).build();
    }
}
