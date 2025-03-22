package com.prueba.demo.repositoryAWS;

import com.prueba.demo.modelAWS.AccountDataAWS;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountDataAWSRepository extends JpaRepository<AccountDataAWS, Long> {

    Optional<AccountDataAWS> findByAccountAWS_Id(Long accountId);

}
