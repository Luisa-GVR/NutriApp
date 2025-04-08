package com.prueba.demo.repositoryFreeSQL;

import com.prueba.demo.modelFreeSQL.AccountDataFreeSQLHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountDataFreeSQLHistoryRepository extends JpaRepository<AccountDataFreeSQLHistory, Long> {
}
