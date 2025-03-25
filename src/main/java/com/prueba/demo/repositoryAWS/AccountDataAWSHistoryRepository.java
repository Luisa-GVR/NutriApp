package com.prueba.demo.repositoryAWS;

import com.prueba.demo.modelAWS.AccountAWS;
import com.prueba.demo.modelAWS.AccountDataAWSHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountDataAWSHistoryRepository extends JpaRepository<AccountDataAWSHistory, Long> {
}
