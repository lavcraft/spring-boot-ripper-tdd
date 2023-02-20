package com.governance.visaagent.servicevisaagent.port.input;

import com.governance.visaagent.servicevisaagent.dal.VisaRequest;
import com.governance.visaagent.servicevisaagent.dal.VisaRequestRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VisaStatusController {
    private final VisaRequestRepository visaRequestRepository;

    public VisaStatusController(final VisaRequestRepository visaRequestRepository) {
        this.visaRequestRepository = visaRequestRepository;
    }

    @GetMapping("/visa-status")
    public ResponseEntity<VisaRequest> visaStatus(@RequestParam Long ticket) {
        return visaRequestRepository.findById(ticket)
                .map(visaRequest -> ResponseEntity.ok(visaRequest)).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
