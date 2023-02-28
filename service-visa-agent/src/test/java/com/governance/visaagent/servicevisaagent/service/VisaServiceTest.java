package com.governance.visaagent.servicevisaagent.service;


import com.governance.visaagent.servicevisaagent.dal.VisaRequest;
import com.governance.visaagent.servicevisaagent.dal.VisaRequestRepository;
import com.governance.visaagent.servicevisaagent.port.input.UserInfo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.client.HttpServerErrorException;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VisaServiceTest {
    @InjectMocks VisaService                        visaRequestService;
    @Mock        VisaRequestRepository              visaRequestRepository;
    @Mock        KafkaTemplate<String, VisaRequest> kafkaTemplate;

    @Test
    void should_persist_new_request_and_return_id() {
        //given
        when(visaRequestRepository.save(any())).thenReturn(VisaRequest.builder()
                                                                      .id(1L)
                                                                      .build());

        //when
        Long visaRequest = visaRequestService.processVisaRequest(UserInfo.builder()
                                                                         .userId("U-100")
                                                                         .build());

        //then
        assertThat(visaRequest).isEqualTo(1L);
    }

    @Test
    void should_return_old_ticket_id_if_visa_request_for_user_processing() {
        //given
        when(visaRequestRepository.findAllByUserIdAndStatusIsProcessing("U-100")).thenReturn(Collections.singletonList(
                VisaRequest.builder()
                           .id(100L)
                           .userId("U-100")
                           .status("processing")
                           .build()
        ));

        //when
        Long visaRequest = visaRequestService.processVisaRequest(UserInfo.builder()
                                                                         .userId("U-100")
                                                                         .build());

        //then
        assertThat(visaRequest).isEqualTo(100L)
                               .describedAs("Expect old ticket id");
        verify(
                visaRequestRepository,
                times(0)
        ).save(any());
    }

    @Test
    void should_throw_exception_when_many_tickets_in_processing_status_because_db_in_invalid_state() {
        //given
        when(visaRequestRepository.findAllByUserIdAndStatusIsProcessing("U-1000"))
                .thenReturn(List.of(
                                VisaRequest.builder().build(),
                                VisaRequest.builder().build(),
                                VisaRequest.builder().build()
                        )
                );
        //expect
        assertThatThrownBy(
                () -> visaRequestService.processVisaRequest(UserInfo.builder()
                                                                    .userId("U-1000")
                                                                    .build()))
                .isInstanceOf(IllegalStateException.class)
                .describedAs("Should throw when database contain many processing tickets for one " +
                             "user");
    }

//    @Test
//    void should_try_send_notification_to_queue() {
//        CompletableFuture<VisaRequest> stringCompletableFuture =
//                CompletableFuture.completedFuture(VisaRequest.builder().build());
//        when(kafkaTemplate.send(
//                "visa.requests",
//                "U-1000",
//                VisaRequest.builder()
//                           .userId("U-1000")
//                           .id(1000L)
//                           .build()
//        )).thenReturn(stringCompletableFuture);
//    }

    @Test
    void should_return_status_by_ticket_id() {
        throw new UnsupportedOperationException();
    }

    @Test
    void should_send_new_request_to_queue_after_persist() {
        throw new UnsupportedOperationException();
    }
}
