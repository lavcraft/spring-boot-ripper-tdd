package com.governance.visaagent.servicevisaagent.port.input;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VisaRequestResponse {
    String ticket;
}
