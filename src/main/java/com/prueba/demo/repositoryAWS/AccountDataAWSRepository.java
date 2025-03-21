package com.prueba.demo.repositoryAWS;

import com.prueba.demo.modelAWS.AccountDataAWS;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountDataAWSRepository extends JpaRepository<AccountDataAWS, Long> {
}
