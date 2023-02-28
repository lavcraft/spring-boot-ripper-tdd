package com.governance.visaagent.servicevisaagent.service;

import com.governance.visaagent.servicevisaagent.dal.VisaRequest;
import com.governance.visaagent.servicevisaagent.dal.VisaRequestDto;
import com.governance.visaagent.servicevisaagent.dal.VisaRequestMapper;
import com.governance.visaagent.servicevisaagent.dal.VisaRequestRepository;
import com.governance.visaagent.servicevisaagent.port.input.UserInfo;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VisaService {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final VisaRequestRepository visaRequestRepository;
    private final VisaRequestMapper     visaRequestMapper;

    @SneakyThrows
    public Long processVisaRequest(UserInfo userInfo) {
//        List<VisaRequest> allByUserIdAndStatusIsProcessing =
//                visaRequestRepository.findAllByUserIdAndStatusIsProcessing(userInfo.getUserId());
//
//        if (allByUserIdAndStatusIsProcessing.size() > 1) {
//            throw new IllegalStateException("Many processed tickets for user " + userInfo.getUserId());
//        }
//
//        if (allByUserIdAndStatusIsProcessing.size() == 1) {
//            return allByUserIdAndStatusIsProcessing.get(0).getId();
//        }

        System.out.println("Send request with user "+ userInfo.getUserId());
        kafkaTemplate.send(
                "visa.requests",
                userInfo.getUserId(),
                userInfo.getUserId()
        ).whenComplete(
                (stringStringSendResult, throwable) -> {
                    System.out.println("Complete with :" + stringStringSendResult);
                    if (throwable != null) {
                        throwable.printStackTrace();
                    }
                }
        );

        VisaRequest entity = visaRequestRepository.save(VisaRequest.builder()
                                                                       .status("processing")
                                                                       .userId(userInfo.getUserId())
                                                                       .build());

        return entity.getId();
    }

    public Optional<VisaRequest> findById(java.lang.Long ticket) {
        return null;
    }
}
