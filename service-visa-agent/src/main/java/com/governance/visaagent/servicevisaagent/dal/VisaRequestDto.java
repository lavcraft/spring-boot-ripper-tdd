package com.governance.visaagent.servicevisaagent.dal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * A DTO for the {@link VisaRequest} entity
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class VisaRequestDto implements Serializable {
    private Long   id;
    private String userId;
    private String status;
}