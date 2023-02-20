package com.governance.visaagent.servicevisaagent.dal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class VisaRequestRepositoryTest {
    @Autowired VisaRequestRepository visaRequestRepository;

    @Test
    void should_save_visa_request() {
        //when
        VisaRequest save = visaRequestRepository.save(VisaRequest.builder().status("processing").userId("U-1").build());

        //then
        assertAll(
                () -> assertThat(save.getUserId()).isEqualTo("U-1"),
                () -> assertThat(save.getId()).isNotNull().describedAs("id should be presented at saved visa request"),
                () -> assertThat(save.getStatus()).isEqualTo("processing").describedAs("should be in status processing")
        );
    }
}