package com.prueba.demo.repositoryFreeSQL;

import com.prueba.demo.modelFreeSQL.AccountDataFreeSQLHistory;
import com.prueba.demo.modelFreeSQL.AccountFreeSQL;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
public interface AccountDataFreeSQLHistoryRepository extends JpaRepository<AccountDataFreeSQLHistory, Long> {

    @Query("SELECT MIN(h.date) FROM AccountDataFreeSQLHistory h WHERE h.accountFreeSQL.id = :accountId")
    Date findOldestDateByAccountId(@Param("accountId") Long accountId);

    List<AccountDataFreeSQLHistory> findByAccountFreeSQLAndDateBetween(
            AccountFreeSQL account, Date startDate, Date endDate);


    List<AccountDataFreeSQLHistory> findByAccountFreeSQL(AccountFreeSQL accountFreeSQL);
}
