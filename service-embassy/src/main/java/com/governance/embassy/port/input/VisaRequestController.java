package com.governance.embassy.port.input;

import com.governance.embassy.service.VisaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VisaRequestController {

    private VisaService visaService;

    public VisaRequestController(final VisaService visaService) {
        this.visaService = visaService;
    }

    @GetMapping("/visa")
    public ResponseEntity<Object> visa(@RequestParam String userId) {
        String ticket = visaService.createRequestOrGetStatus(userId);

        return ResponseEntity
                .ok(VisaRequestResponse
                        .builder()
                        .ticket(ticket)
                        .build()
                );
    }

    @GetMapping("/visa/{requestId}")
    public ResponseEntity<Object> visa_status(@PathVariable String requestId) {
        String status = visaService.getStatus(requestId);

        return ResponseEntity
                .ok(VisaRequestStatusResponse
                        .builder()
                        .status(status)
                        .build()
                );
    }
}
