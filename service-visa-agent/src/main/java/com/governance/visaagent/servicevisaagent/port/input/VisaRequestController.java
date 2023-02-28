package com.governance.visaagent.servicevisaagent.port.input;

import com.governance.visaagent.servicevisaagent.service.VisaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class VisaRequestController {
    private final VisaService visaRequestService;

    @PostMapping("/visa-request")
    public VisaRequestResponse createVisaRequest(@RequestBody UserInfo userInfo) {
        Long save = visaRequestService.processVisaRequest(userInfo);
        return VisaRequestResponse.builder().ticket(save.toString()).build();
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<VisaErrorResponse> handleError() {
        return ResponseEntity.internalServerError().body(VisaErrorResponse.builder()
                                                                          .message("Internal " +
                                                                                   "error. Please" +
                                                                                   " contact later")
                                                                          .build());
    }

}
