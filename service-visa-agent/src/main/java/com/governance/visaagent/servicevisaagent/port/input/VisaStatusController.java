package com.governance.visaagent.servicevisaagent.port.input;

import com.governance.visaagent.servicevisaagent.dal.VisaRequest;
import com.governance.visaagent.servicevisaagent.dal.VisaRequestRepository;
import com.governance.visaagent.servicevisaagent.service.VisaRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class VisaStatusController {
    private final VisaRequestService visaRequestService;

    @GetMapping("/visa-status")
    public ResponseEntity<VisaRequest> visaStatus(@RequestParam Long ticket) {
        return visaRequestService.findById(ticket).map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
