package com.governance.visaagent.servicevisaagent.dal;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface VisaRequestRepository extends CrudRepository<VisaRequest, java.lang.Long> {
    @Transactional
    @Modifying//(clearAutomatically = true)
    @Query("update VisaRequest v set v.status = ?1 where v.id = ?2")
    void updateStatus(String status,
                      java.lang.Long id);

    @Query("select v from VisaRequest v where v.status = 'processing' and v.userId = ?1")
    List<VisaRequest> findAllByUserIdAndStatusIsProcessing(String userId);
}
