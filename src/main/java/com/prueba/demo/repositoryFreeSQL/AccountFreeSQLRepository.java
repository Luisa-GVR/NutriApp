package com.prueba.demo.repositoryFreeSQL;

import com.prueba.demo.modelFreeSQL.AccountFreeSQL;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountFreeSQLRepository extends JpaRepository<AccountFreeSQL, Long> {
    Optional<AccountFreeSQL> findByEmail(String email);

}
