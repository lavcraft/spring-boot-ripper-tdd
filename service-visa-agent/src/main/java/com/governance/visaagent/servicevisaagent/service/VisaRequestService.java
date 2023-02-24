package com.governance.visaagent.servicevisaagent.service;

import com.governance.visaagent.servicevisaagent.dal.VisaRequest;
import com.governance.visaagent.servicevisaagent.port.input.UserInfo;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@Service
@RequiredArgsConstructor
public class VisaRequestService {
    private final KafkaTemplate<String, String> kafkaTemplate;

    @SneakyThrows
    public VisaRequest processVisaRequest(UserInfo userInfo) {
        System.out.println("Send request with user "+ userInfo.getUserId());
        kafkaTemplate.send(
                "visa.requests",
                userInfo.getUserId(),
                userInfo.getUserId()
        ).whenComplete(
                (stringStringSendResult, throwable) -> {
                    System.out.println("Complete with :" + stringStringSendResult);
                    if (throwable != null) {
                        System.err.println(throwable);
                    }
                }
        );
        kafkaTemplate.flush();
        return VisaRequest.builder().build();
    }

    public Optional<VisaRequest> findById(Long ticket) {
        return null;
    }
}
