package com.governance.visaagent.servicevisaagent.port.output;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VisaRequestQueueDTO {
    Long ticket;
    String userId;
    String status;
}
