package com.prueba.demo.repositoryFreeSQL;

import com.prueba.demo.modelFreeSQL.AccountDataFreeSQL;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountDataFreeSQLRepository extends JpaRepository<AccountDataFreeSQL, Long> {

    Optional<AccountDataFreeSQL> findByAccountFreeSQL_Id(Long accountId);

}
