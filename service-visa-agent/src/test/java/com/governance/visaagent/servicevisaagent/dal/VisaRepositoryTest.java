package com.governance.visaagent.servicevisaagent.dal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class VisaRepositoryTest {
    @Autowired VisaRequestRepository visaRequestRepository;

    @Test
    void should_save_visa_request() {
        //when
        VisaRequest save = visaRequestRepository.save(
                VisaRequest.builder()
                           .status("processing")
                           .userId("U-1")
                           .build()
        );

        //then
        assertAll(
                () -> assertThat(save.getUserId()).isEqualTo("U-1"),
                () -> assertThat(save.getId()).isNotNull()
                                              .describedAs("id should be presented at saved visa " +
                                                           "request"),
                () -> assertThat(save.getStatus()).isEqualTo("processing")
                                                  .describedAs("should be in status processing")
        );
    }

    @Test
    @Sql(value = "tests/data-should_return_visa_request_with_status.sql")
    void should_return_visa_request_with_status() {
        //when
        Optional<VisaRequest> processingEntity = visaRequestRepository.findById(10L);
        Optional<VisaRequest> failedEntity     = visaRequestRepository.findById(20L);
        Optional<VisaRequest> acceptedEntity   = visaRequestRepository.findById(30L);

        //then
        assertAll(
                () -> assertThat(processingEntity).isPresent().describedAs("processing empty " +
                                                                           "should be exist"),
                () -> assertThat(failedEntity).isPresent().describedAs("failed empty " +
                                                                       "should be exist"),
                () -> assertThat(acceptedEntity).isPresent().describedAs("accepted empty " +
                                                                         "should be exist")
        );
        assertAll(
                () -> assertThat(processingEntity.get().getStatus()).isEqualTo("processing"),
                () -> assertThat(failedEntity.get().getStatus()).isEqualTo("failed"),
                () -> assertThat(acceptedEntity.get().getStatus()).isEqualTo("accepted")
        );
    }
}