package com.governance.visaagent.servicevisaagent.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VisaRequest {
    private Long id;
    private String userId;
    private String status;
}
