package com.governance.embassy.service;

import com.governance.embassy.port.output.VisaHttlClientProperties;
import com.governance.embassy.port.output.VisaRequestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;

@Service
public class VisaService {
    private RestTemplate template;
    private VisaHttlClientProperties properties;

    public VisaService(@Autowired RestTemplate visaRestTemplate, @Autowired VisaHttlClientProperties visaHttlClientProperties) {
        this.template = visaRestTemplate;
        this.properties = visaHttlClientProperties;
    }

    public String getStatus(String requestId) {
        ResponseEntity<VisaRequestResponse> forEntity = this.template.getForEntity("http://localhost:8081/visa-status?requestId="+requestId, VisaRequestResponse.class);
//        if (forEntity == null) {
//            return null;
//        }
        VisaRequestResponse body = forEntity.getBody();

        if (body == null) {
            return null;
        }
        return body.getTicket();
    }

    public String createRequestOrGetStatus(String userId) {
        return null;
    }
}
