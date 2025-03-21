package com.prueba.demo.repositoryAWS;

import com.prueba.demo.modelAWS.AccountAWS;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountAWSRepository extends JpaRepository<AccountAWS, Long> {
    Optional<AccountAWS> findByEmail(String email);

}
