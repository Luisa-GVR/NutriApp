package com.prueba.demo.repository;

import com.prueba.demo.model.Account;
import com.prueba.demo.model.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    @Query("SELECT r FROM Report r " +
            "WHERE r.accountData.account.id = :accountId " +
            "AND r.date >= :startDate AND r.date <= :endDate")
    List<Report> findReportsByAccountAndDateRange(@Param("accountId") Long accountId,
                                                  @Param("startDate") Date startDate,
                                                  @Param("endDate") Date endDate);



}
