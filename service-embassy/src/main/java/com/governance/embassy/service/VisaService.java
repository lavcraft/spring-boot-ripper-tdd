package com.governance.embassy.service;

import com.governance.embassy.port.output.VisaHttpClientProperties;
import com.governance.embassy.port.output.VisaRequest;
import com.governance.embassy.port.output.VisaRequestResponse;
import com.governance.embassy.port.output.VisaStatusResponse;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Timer;
import java.util.concurrent.TimeUnit;

@Service
public class VisaService {
    private RestTemplate             template;
    private VisaHttpClientProperties properties;

    public VisaService(final RestTemplate visaRestTemplate,
                       final VisaHttpClientProperties endpoint
) {
        this.template   = visaRestTemplate;
        this.properties = endpoint;
    }


    @Cacheable(value = "visa-request")
    public String getStatus(String requestId) {
        ResponseEntity<VisaStatusResponse> forEntity = this.template.getForEntity(
                properties.getEndpoint() + "/visa-status" + "?ticket=" + requestId,
                VisaStatusResponse.class
        );

//        if (forEntity == null) {
//            return null;
//        }
        VisaStatusResponse body = forEntity.getBody();


        if (body == null) {
            return null;
        }
        return body.getStatus();
    }

    public String createRequest(String userId) {
        VisaRequestResponse visaRequestResponseResponseEntity = this.template
                .postForEntity(
                        properties.getEndpoint() + "/visa-request",
                        VisaRequest
                                .builder()
                                .userId(userId)
                                .build(),
                        VisaRequestResponse.class
                )
                .getBody();

        return visaRequestResponseResponseEntity.getTicket();
    }

    @CacheEvict(value = "visa-request", allEntries = true)
    public void evictRequestCache() {

    }
}
